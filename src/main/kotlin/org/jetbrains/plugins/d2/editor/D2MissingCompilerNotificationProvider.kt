package org.jetbrains.plugins.d2.editor

import com.dvd.intellij.d2.ide.service.D2_FILE_NOTIFICATION
import com.dvd.intellij.d2.ide.utils.isD2
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.EditorNotificationPanel
import com.intellij.ui.EditorNotificationProvider
import org.jetbrains.annotations.Nls
import java.util.function.Function
import java.util.function.Supplier
import javax.swing.JComponent

private class D2MissingCompilerNotificationProvider : EditorNotificationProvider, DumbAware {
  override fun collectNotificationData(project: Project, file: VirtualFile): Function<in FileEditor, out JComponent?>? {
    if (!file.isD2) {
      return null
    }

    return Function { fileEditor ->
      fileEditor.getUserData(D2_FILE_NOTIFICATION)?.let {
        D2MissingCompilerNotificationPanel(fileEditor, it)
      }
    }
  }
}

private class D2MissingCompilerNotificationPanel(
  fileEditor: FileEditor,
  text: Supplier<@Nls String>,
) : EditorNotificationPanel(fileEditor, Status.Warning) {
  init {
    setText(text.get())
  }
}