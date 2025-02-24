package org.jetbrains.plugins.d2.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.fileEditor.TextEditorWithPreview
import com.intellij.openapi.vfs.VirtualFile
import org.jetbrains.plugins.d2.editor.D2Viewer
import org.jetbrains.plugins.d2.file.D2FileType

internal val VirtualFile.isD2: Boolean
    get() = fileType == D2FileType

internal val AnActionEvent.d2FileEditor: D2Viewer?
    get() = (getData(PlatformDataKeys.FILE_EDITOR) as TextEditorWithPreview?)?.previewEditor as D2Viewer?