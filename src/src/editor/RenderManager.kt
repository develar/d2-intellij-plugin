@file:Suppress("BlockingMethodInNonBlockingContext")

package org.jetbrains.plugins.d2.editor

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.*
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.EditorNotifications
import com.intellij.ui.JBColor
import com.intellij.util.EnvironmentUtil
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import org.jetbrains.plugins.d2.D2Bundle
import org.jetbrains.plugins.d2.D2Layout
import org.jetbrains.plugins.d2.D2Theme
import java.net.ServerSocket
import java.nio.file.Files
import java.util.concurrent.CancellationException
import java.util.function.Supplier
import kotlin.coroutines.resume
import kotlin.system.measureTimeMillis
import kotlin.time.Duration.Companion.milliseconds

internal data class RenderRequest(
  @JvmField val theme: D2Theme?,
  @JvmField val layout: D2Layout?,
  @JvmField val sketch: Boolean,
)

private val LOG: Logger
  get() = logger<RenderManager>()

@OptIn(FlowPreview::class)
internal class RenderManager(
  coroutineScope: CoroutineScope,
  private val project: Project,
  private val file: VirtualFile,
  private val rendered: (Int) -> Unit,
) {
  private val requests = MutableSharedFlow<RenderRequest>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

  var state: D2CompilerState? = null
    private set

  var fileNotification: Supplier<String>? = null

  val d2Info: MutableStateFlow<D2Info> = MutableStateFlow(D2Info("", emptyList()))

  init {
    coroutineScope.launch {
      requests
        .debounce(10.milliseconds)
        .collect {
          compileAndWatch(it)
        }
    }

    coroutineScope.coroutineContext.job.invokeOnCompletion {
      state?.let {
        state = null
        it.process?.destroyProcess()
      }
    }
  }

  fun request(request: RenderRequest) {
    check(requests.tryEmit(request))
  }

  private suspend fun compileAndWatch(request: RenderRequest) {
    coroutineScope {
      val d2Version: Deferred<String> = async {
        computeD2Version()
      }

      try {
        withContext(Dispatchers.IO) {
          doCompileAndWatch(request)
        }
        if (fileNotification != null) {
          EditorNotifications.getInstance(project).updateNotifications(file)
        }
      } catch (e: CancellationException) {
        throw e
      } catch (e: Exception) {
        if (d2Version.await().isEmpty()) {
          fileNotification = D2Bundle.messagePointer("d2.executable.not.found.notification")
        } else {
          thisLogger().error(e)

          fileNotification = Supplier { "Internal error" }
          EditorNotifications.getInstance(project).updateNotifications(file)
          return@coroutineScope
        }
      }

      val layouts = computeLayoutInfo()
      d2Info.value = D2Info(version = d2Version.await(), layouts = layouts)
    }
  }

  private suspend fun doCompileAndWatch(request: RenderRequest) {
    val oldState = state

    val targetFile = Files.createTempFile("d2_temp_svg", ".svg")
    val newState: D2CompilerState
    if (oldState == null) {
      // find a free port
      val port = ServerSocket(0).use {
        it.localPort
      }

      newState = D2CompilerState(
        input = file,
        targetFile = targetFile,
        port = port,
        theme = getThemeId(request),
        layout = request.layout,
        sketch = request.sketch,
        log = StringBuilder("[plugin ] info: starting process...\n"),
      )
    } else {
      oldState.process?.let { oldProcess ->
        oldProcess.destroyProcess()
        val terminationTime = measureTimeMillis {
          // background process? ~5ms
          while (!oldProcess.isProcessTerminated) {
            delay(1.milliseconds)
          }
        }
        oldState.log.append("[plugin ] [info] D2 process termination ${terminationTime}ms\n")
      }

      newState = D2CompilerState(
        input = file,
        targetFile = targetFile,
        port = oldState.port,
        theme = getThemeId(request),
        layout = request.layout,
        log = StringBuilder(oldState.log).append("[plugin ] info: restarting process...\n"),
        sketch = request.sketch,
      )
    }

    // If `rendered` is called right after start for the second invocation, the page may not yet be ready
    state = newState
    newState.process = createProcessHandler(newState)
    newState.process?.startNotify()
  }

  private fun createProcessHandler(state: D2CompilerState): KillableColoredProcessHandler.Silent {
    val command = state.createCommandLine(watch = true, targetFile = state.targetFile)
    val processHandler = KillableColoredProcessHandler.Silent(command)
    var firstText = true
    processHandler.addProcessListener(object : ProcessListener {
      override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
        val log = state.log
        log.append("[process] ")
        if (outputType == ProcessOutputType.SYSTEM) {
          log.append("info: ").append(event.text)
        } else {
          if (firstText) {
            firstText = false
            rendered(state.port)
          }

          // remove timestamp
          log.append(event.text.replace(timestampRegexp, ""))
        }
      }

      override fun processTerminated(event: ProcessEvent) {
        state.deleteTargetFile()
      }
    })
    return processHandler
  }
}

private val timestampRegexp = Regex("\\[?\\d{2}:\\d{2}:\\d{2}]? ")

private suspend fun computeD2Version(): String {
  val command = GeneralCommandLine().withCharset(Charsets.UTF_8)
  command.exePath = "d2"
  command.addParameter("--version")
  val (exitCode, output) = executeAndReadOutput(command, logErrorIfBadExitCode = false)
  return if (exitCode == 0) {
    output.trim().removePrefix("v").toString()
  } else {
    ""
  }
}

private suspend fun executeAndReadOutput(command: GeneralCommandLine, logErrorIfBadExitCode: Boolean = true): Pair<Int, StringBuilder> {
  val output = StringBuilder()
  val exitCode = suspendCancellableCoroutine { continuation ->
    val processHandler = OSProcessHandler(command)
    processHandler.addProcessListener(object : ProcessListener {
      override fun processTerminated(event: ProcessEvent) {
        continuation.resume(event.exitCode)
      }

      override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
        when (outputType) {
          ProcessOutputType.STDOUT -> output.append(event.text)
          ProcessOutputType.STDOUT -> LOG.error(event.text)
        }
      }
    })

    processHandler.startNotify()
  }

  if (logErrorIfBadExitCode && exitCode != 0) {
    LOG.error("Cannot execute $command (exitCode=$exitCode")
  }
  return exitCode to output
}

private val LAYOUT_ENGINE_REGEX = Regex("(\\w+)(?:\\s(\\(bundled\\)))?\\s-\\s(.*)")

private suspend fun computeLayoutInfo(): List<D2Layout> {
  val command = GeneralCommandLine().withCharset(Charsets.UTF_8)
  command.exePath = "d2"
  command.addParameter("layout")
  val (exitCode, output) = executeAndReadOutput(command)
  if (exitCode != 0) {
    return emptyList()
  }

  val layouts = output.splitToSequence('\n')
    .mapNotNull {
      val m = LAYOUT_ENGINE_REGEX.matchEntire(it.trim()) ?: return@mapNotNull null
      val (name, bundled, desc) = m.destructured
      D2Layout(name = name, bundled = bundled.isNotEmpty(), description = desc)
    }
    .toList()
  return layouts
}

private fun getThemeId(request: RenderRequest): String? {
  val theme = request.theme
  if (theme == null) {
    if (JBColor.isBright()) {
      return null
    } else {
      // https://github.com/develar/d2-intellij-plugin/issues/1
      return EnvironmentUtil.getValue("D2_DARK_THEME")?.takeIf { it.isNotBlank() } ?: "200"
    }
  }
  return theme.id.toString()
}