@file:Suppress("BlockingMethodInNonBlockingContext")

package org.jetbrains.plugins.d2.action

import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.execution.process.ProcessOutputType
import com.intellij.ide.BrowserUtil
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.fileChooser.FileChooserFactory
import com.intellij.openapi.fileChooser.FileSaverDescriptor
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.NlsContexts
import com.intellij.openapi.util.io.FileUtilRt
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.ide.progress.withBackgroundProgress
import com.intellij.platform.util.progress.withRawProgressReporter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import org.jetbrains.plugins.d2.D2Bundle
import org.jetbrains.plugins.d2.D2_NOTIFICATION_GROUP
import org.jetbrains.plugins.d2.editor.D2CompilerState
import org.jetbrains.plugins.d2.editor.D2Viewer
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import javax.imageio.ImageIO
import kotlin.coroutines.resume
import kotlin.io.path.extension

private enum class ConversionOutput { SVG, PNG, JPG, TIFF }

private class D2ExportAction : AnAction(), DumbAware {
    override fun actionPerformed(e: AnActionEvent) {
        val defaultFileName =
            FileUtilRt.getNameWithoutExtension(e.getData(PlatformDataKeys.VIRTUAL_FILE)!!.presentableName) + ".svg"

        val project = e.project
        val fileWrapper = FileChooserFactory.getInstance().createSaveFileDialog(
            FileSaverDescriptor(
                D2Bundle.message("d2.export.image.title"),
                """${D2Bundle.message("d2.export.image.description")} ${ConversionOutput.entries.joinToString(", ")}""",
                *ConversionOutput.entries.map { it.name.lowercase() }.toTypedArray()
            ),
            project
        ).save((e.getData(PlatformDataKeys.VIRTUAL_FILE) as VirtualFile).parent, defaultFileName) ?: return

        val viewer = e.d2FileEditor!!
        val targetFile = fileWrapper.file.toPath()
        convert(targetFile = targetFile, viewer = viewer)
    }

    override fun getActionUpdateThread() = ActionUpdateThread.BGT
}

private fun convert(targetFile: Path, viewer: D2Viewer) {
    Files.createDirectories(targetFile.parent)

    val formatName = targetFile.extension.uppercase()
    val format = try {
        ConversionOutput.valueOf(formatName)
    } catch (_: IllegalArgumentException) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup(D2_NOTIFICATION_GROUP)
            .createNotification(
                D2Bundle.message("d2"),
                D2Bundle.message("image.format.not.supported", formatName),
                NotificationType.ERROR
            )
            .notify(viewer.project)
        return
    }

    val state = viewer.renderManager.state!!
    if (format == ConversionOutput.SVG) {
        val svgFile = state.targetFile
        Files.copy(svgFile, targetFile, StandardCopyOption.REPLACE_EXISTING)
        return
    }

    viewer.coroutineScope.launch(Dispatchers.IO) {
        withBackgroundProgress(
            viewer.project,
            D2Bundle.message("progress.title.compiling.to", state.targetFile.fileName, targetFile.fileName)
        ) {
            convertSvg(
                state = state,
                viewer = viewer,
                formatName = formatName,
                format = format,
                targetFile = targetFile
            )
        }
    }
}

private suspend fun convertSvg(
    state: D2CompilerState,
    viewer: D2Viewer,
    formatName: String,
    format: ConversionOutput,
    targetFile: Path
) {
    // convert to PNG
    val pngFile = compileToPng(state)
    if (pngFile == null) {
        notify(viewer.project, D2Bundle.message("cannot.export.internal.error", formatName), NotificationType.ERROR)
        return
    }

    val ext = when (format) {
        ConversionOutput.PNG -> {
            Files.move(pngFile, targetFile, StandardCopyOption.REPLACE_EXISTING)
            return
        }

        ConversionOutput.JPG -> "jpeg"
        ConversionOutput.TIFF -> "tiff"
        else -> error("$format is not supported")
    }

    val input = Files.newInputStream(pngFile).use { ImageIO.read(it) }
    Files.newOutputStream(targetFile).use {
        ImageIO.write(input, ext, it)
    }
}

private suspend fun compileToPng(state: D2CompilerState): Path? {
    val result = Files.createTempFile(state.targetFile.fileName.toString(), ".d2.png")
    val command = state.createCommandLine(watch = false, targetFile = result).withRedirectErrorStream(true)
    val processHandler = OSProcessHandler(command)
    val log = StringBuilder()

    @Suppress("UnstableApiUsage") val exitCode = withRawProgressReporter {
        // location of rawProgressReporter is changed - no way to use it and be compatible with 2023.2 and 2024.1
        //val reporter = coroutineContext.rawProgressReporter
        suspendCancellableCoroutine { continuation ->
            processHandler.addProcessListener(object : ProcessListener {
                override fun processTerminated(event: ProcessEvent) {
                    continuation.resume(event.exitCode)
                }

                override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
                    if (outputType != ProcessOutputType.SYSTEM) {
                        //reporter?.text(event.text)
                        log.append(event.text)
                    }
                }
            })

            processHandler.startNotify()
        }
    }

    if (exitCode == 0) {
        return result
    } else {
        Files.deleteIfExists(result)
        logger<D2ExportAction>().error("Cannot execute $command: $log")
        return null
    }
}

internal fun notify(project: Project?, @NlsContexts.NotificationContent content: String, type: NotificationType) {
    NotificationGroupManager.getInstance().getNotificationGroup(D2_NOTIFICATION_GROUP)
        .createNotification(D2Bundle.message("d2"), content, type)
        .notify(project)
}

private class D2LiveBrowserAction : AnAction(), DumbAware {
    override fun actionPerformed(e: AnActionEvent) {
        val port = e.d2FileEditor!!.renderManager.state?.port ?: error("port not found")
        BrowserUtil.browse("http://localhost:$port", e.project)
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = e.d2FileEditor?.renderManager?.state?.process != null
    }

    override fun getActionUpdateThread() = ActionUpdateThread.BGT
}