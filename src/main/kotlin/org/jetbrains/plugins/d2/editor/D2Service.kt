package org.jetbrains.plugins.d2.editor

import com.dvd.intellij.d2.components.D2Layout
import com.dvd.intellij.d2.components.D2Theme
import com.dvd.intellij.d2.ide.action.ConversionOutput
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.*
import com.intellij.ide.ui.LafManagerListener
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.io.BufferExposingByteArrayOutputStream
import com.intellij.openapi.util.removeUserData
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.EditorNotifications
import com.intellij.ui.JBColor
import com.intellij.util.EnvironmentUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.plugins.d2.D2Bundle
import org.jetbrains.plugins.d2.execution.D2Command
import java.io.File
import java.net.ServerSocket
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.CancellationException
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Supplier
import javax.imageio.ImageIO
import kotlin.system.measureTimeMillis

private val LOG = logger<D2Service>()

internal val D2_FILE_NOTIFICATION: Key<Supplier<String>> = Key("d2EditorNotification")

@Service
class D2Service(private val coroutineScope: CoroutineScope) : Disposable {
  private val viewerToState = ConcurrentHashMap<D2Viewer, GenerateCommand>()

  val map: Map<D2Viewer, GenerateCommand> = viewerToState

  init {
    ApplicationManager.getApplication().messageBus.connect(coroutineScope).subscribe(LafManagerListener.TOPIC, LafManagerListener {
      for (viewer in java.util.List.copyOf(viewerToState.keys)) {
        compileAndWatch(viewer)
      }
    })
  }

  fun getCompilerVersion(): String? {
    try {
      return executeAndGetOutput(D2Command.Version)?.version
    } catch (ignore: ProcessNotCreatedException) {
    } catch (e: Throwable) {
      LOG.error(e)
    }
    return null
  }

  fun isCompilerInstalled() = getCompilerVersion() != null

  fun getLayoutEngines(): List<D2Layout>? = executeAndGetOutputOrNull(D2Command.LayoutEngines)?.layouts

  fun scheduleCompile(fileEditor: D2Viewer, project: Project) {
    coroutineScope.launch {
      if (!fileEditor.isValid) {
        return@launch
      }

      if (project.isDisposed) {
        return@launch
      }

      try {
        withContext(Dispatchers.IO) {
          compileAndWatch(fileEditor)
        }
        if (fileEditor.removeUserData(D2_FILE_NOTIFICATION) != null) {
          EditorNotifications.getInstance(project).updateNotifications(fileEditor.file)
        }
      } catch (e: CancellationException) {
        throw e
      } catch (e: Exception) {
        if (getCompilerVersion() == null) {
          D2_FILE_NOTIFICATION.set(fileEditor, D2Bundle.messagePointer("d2.executable.not.found.notification"))
        } else {
          LOG.error(e)

          D2_FILE_NOTIFICATION.set(fileEditor) { "Internal error" }
          EditorNotifications.getInstance(project).updateNotifications(fileEditor.file)
        }
      }
    }
  }

  fun compileAndWatch(fileEditor: D2Viewer) {
    val command: GenerateCommand
    synchronized(fileEditor) {
      val oldCommand = viewerToState.get(fileEditor)

      val theme = fileEditor.getUserData(D2_FILE_THEME)
      val layout = fileEditor.getUserData(D2_FILE_LAYOUT)
      val targetFile = Files.createTempFile("d2_temp_svg", ".svg")
      if (oldCommand == null) {
        // find a free port
        val port = ServerSocket(0).use {
          it.localPort
        }

        command = GenerateCommand(
          input = fileEditor.file,
          targetFile = targetFile,
          port = port,
          theme = theme,
          layout = layout,
          log = StringBuilder("[plugin ] info: starting process...\n"),
        )
      } else {
        oldCommand.process?.let {
          it.destroyProcess()
          @Suppress("ControlFlowWithEmptyBody")
          val terminationTime = measureTimeMillis {
            // background process? ~5ms
            while (!it.isProcessTerminated) {
            }
          }
          oldCommand.log.append("[plugin ] [info] D2 process termination ${terminationTime}ms\n")
        }

        command = GenerateCommand(
          input = fileEditor.file,
          targetFile = targetFile,
          port = oldCommand.port,
          theme = theme,
          layout = layout,
          log = StringBuilder(oldCommand.log).append("[plugin ] info: restarting process...\n"),
        )
      }

      command.process = prepare(command, object : ProcessListener {
        override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
          onTextAvailable(event = event, outputType = outputType, log = command.log)
        }

        override fun processWillTerminate(event: ProcessEvent, willBeDestroyed: Boolean) {
          command.deleteTargetFile()
        }
      })

      viewerToState.put(fileEditor, command)
    }

    command.process?.startNotify()
    fileEditor.refreshD2(command.port)
  }

  fun closeFile(fileEditor: D2Viewer) {
    fileEditor.putUserData(D2_FILE_LAYOUT, null)
    fileEditor.putUserData(D2_FILE_THEME, null)

    synchronized(fileEditor) {
      viewerToState.remove(fileEditor)
    }?.process?.destroyProcess()
  }

  fun format(file: File): D2FormatterResult {
    val out = executeAndGetOutputOrNull(D2Command.Format(file))?.content
    return when {
      out == null -> D2FormatterResult.Error("Unknown error")
      out.contains("err: failed") -> D2FormatterResult.Error(out)
      else -> D2FormatterResult.Success(out)
    }
  }

  fun convert(file: Path, format: ConversionOutput): ByteArray {
    if (format == ConversionOutput.SVG) {
      return Files.readAllBytes(file)
    }

    val input = Files.newInputStream(file).use { ImageIO.read(it) }
    val ext = when (format) {
      ConversionOutput.PNG -> "png"
      ConversionOutput.JPG -> "jpeg"
      ConversionOutput.TIFF -> "tiff"
      else -> {
        error("cannot be")
      }
    }
    val out = BufferExposingByteArrayOutputStream()
    ImageIO.write(input, ext, out)
    return out.toByteArray()
  }

  override fun dispose() {
    for (command in viewerToState.values) {
      command.deleteTargetFile()
    }
    viewerToState.clear()
  }

  private fun prepare(command: GenerateCommand, listener: ProcessListener?): KillableColoredProcessHandler.Silent {
    val commandLine = GeneralCommandLine(command.getArgs())
      .withCharset(Charsets.UTF_8)
      .withEnvironment(command.envVars())
    return KillableColoredProcessHandler.Silent(commandLine).apply {
      if (listener != null) {
        addProcessListener(listener)
      }
    }
  }
}

private val timestampRegexp = Regex("\\[?\\d{2}:\\d{2}:\\d{2}]? ")

private fun onTextAvailable(event: ProcessEvent, outputType: Key<*>, log: StringBuilder) {
  log.append("[process] ")
  if (outputType == ProcessOutputType.SYSTEM) {
    log.append("info: ")
    log.append(event.text)
  } else {
    // remove timestamp
    log.append(event.text.replace(timestampRegexp, ""))
  }
}

// null if d2 executable not found
private fun <O> executeAndGetOutputOrNull(command: D2Command<O>): O? {
  try {
    return executeAndGetOutput(command)
  } catch (e: Exception) {
    LOG.error(e)
    return null
  }
}

private fun <O> executeAndGetOutput(command: D2Command<O>): O? {
  val processOut = ScriptRunnerUtil.getProcessOutput(
    command.createCommandLine(),
    ScriptRunnerUtil.STDOUT_OR_STDERR_OUTPUT_KEY_FILTER,
    500
  )
  return command.parseOutput(processOut)
}

class GenerateCommand(
  val input: VirtualFile,
  val targetFile: Path,
  val port: Int,
  val theme: D2Theme?,
  val layout: D2Layout?,
  val log: StringBuilder,
) {
  var process: ProcessHandler? = null

  fun deleteTargetFile() {
    targetFile.let { Files.deleteIfExists(it) }
  }

  fun getArgs(): List<String> = buildList {
    add("d2")

    add("--watch")

    add("--port")
    add(port.toString())

    if (layout != null) {
      add("--layout")
      add(layout.name)
    }

    if (theme != null) {
      add("--theme")
      add(theme.id.toString())
    } else if (!JBColor.isBright()) {
      // https://github.com/develar/d2-intellij-plugin/issues/1
      add("--theme")
      add(EnvironmentUtil.getValue("D2_DARK_THEME")?.takeIf { it.isNotBlank() } ?: "200")
    }

    add(input.path)
    add(targetFile.toString())
  }

  fun envVars(): Map<String, String> = java.util.Map.of("BROWSER", "0")
}