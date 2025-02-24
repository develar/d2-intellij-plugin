package org.jetbrains.plugins.d2.editor

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.TextEditorWithPreview
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.EditorNotificationPanel
import com.intellij.ui.EditorNotificationProvider
import org.jetbrains.annotations.Nls
import org.jetbrains.plugins.d2.action.isD2
import java.util.function.Function
import java.util.function.Supplier
import javax.swing.JComponent

private class D2MissingCompilerNotificationProvider : EditorNotificationProvider, DumbAware {
    override fun collectNotificationData(
        project: Project,
        file: VirtualFile
    ): Function<in FileEditor, out JComponent?>? {
        if (!file.isD2) {
            return null
        }

        return Function { fileEditor ->
            ((fileEditor as? TextEditorWithPreview)?.previewEditor as? D2Viewer)?.renderManager?.fileNotification?.let {
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