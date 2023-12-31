package com.dvd.intellij.d2.ide.action

import com.intellij.ide.BrowserUtil
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.components.service
import com.intellij.openapi.fileChooser.FileChooserFactory
import com.intellij.openapi.fileChooser.FileSaverDescriptor
import com.intellij.openapi.util.io.FileUtilRt
import com.intellij.openapi.vfs.VirtualFile
import org.jetbrains.plugins.d2.D2Bundle
import org.jetbrains.plugins.d2.d2FileEditor
import org.jetbrains.plugins.d2.editor.D2Service
import org.jetbrains.plugins.d2.editor.D2Viewer
import org.jetbrains.plugins.d2.editor.GenerateCommand
import java.nio.file.Files

enum class ConversionOutput { SVG, PNG, JPG, TIFF }

private fun getGeneratedCommand(fileEditor: D2Viewer): GenerateCommand? = service<D2Service>().map.get(fileEditor)

@OptIn(ExperimentalStdlibApi::class)
private class D2ExportAction : AnAction() {
  override fun actionPerformed(e: AnActionEvent) {
    val defaultFileName = FileUtilRt.getNameWithoutExtension(e.getData(PlatformDataKeys.VIRTUAL_FILE)!!.presentableName)

    val fileWrapper = FileChooserFactory.getInstance().createSaveFileDialog(
      FileSaverDescriptor(
        D2Bundle.message("d2.export.image.title"),
        """${D2Bundle.message("d2.export.image.description")} ${ConversionOutput.entries.joinToString(", ")}""",
        *ConversionOutput.entries.map { it.name.lowercase() }.toTypedArray()
      ),
      e.project
    ).save((e.getData(PlatformDataKeys.VIRTUAL_FILE) as VirtualFile).parent, defaultFileName) ?: return

    val d2File = getGeneratedCommand(e.d2FileEditor)?.targetFile ?: error("no d2 file")
    val converted = service<D2Service>().convert(d2File, ConversionOutput.valueOf(fileWrapper.file.extension.uppercase()))
    Files.write(fileWrapper.file.toPath(), converted)
  }
}

private class D2LiveBrowserAction : AnAction() {
  override fun actionPerformed(e: AnActionEvent) {
    val port = getGeneratedCommand(e.d2FileEditor)?.port ?: error("port not found")
    BrowserUtil.browse("http://localhost:$port")
  }
}