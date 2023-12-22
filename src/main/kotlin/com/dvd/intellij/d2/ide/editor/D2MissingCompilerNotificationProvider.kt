package com.dvd.intellij.d2.ide.editor

import com.dvd.intellij.d2.ide.service.D2Service
import com.dvd.intellij.d2.ide.utils.D2Bundle
import com.dvd.intellij.d2.ide.utils.isD2
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.EditorNotificationPanel
import com.intellij.ui.EditorNotificationProvider
import java.util.function.Function
import javax.swing.JComponent

private class D2MissingCompilerNotificationProvider : EditorNotificationProvider, DumbAware {
  override fun collectNotificationData(project: Project, file: VirtualFile): Function<in FileEditor, out JComponent?>? {
    val installed = service<D2Service>().isCompilerInstalled()

    return if (file.isD2 && !installed) {
      Function { fileEditor -> D2MissingCompilerNotificationPanel(fileEditor) }
    } else null
  }

  private class D2MissingCompilerNotificationPanel(
    fileEditor: FileEditor
  ) : EditorNotificationPanel(fileEditor, Status.Warning) {
    init {
      text = D2Bundle.message("d2.executable.not.found.notification")
    }
  }
}