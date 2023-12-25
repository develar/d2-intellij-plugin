package com.dvd.intellij.d2.ide.utils

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.fileEditor.TextEditorWithPreview
import com.intellij.openapi.vfs.VirtualFile
import org.jetbrains.plugins.d2.editor.D2SvgViewer
import org.jetbrains.plugins.d2.file.D2FileType

val VirtualFile.isD2: Boolean
  get() = fileType == D2FileType

val AnActionEvent.file: VirtualFile
  get() = getData(PlatformDataKeys.VIRTUAL_FILE) as VirtualFile

val AnActionEvent.d2FileEditor
  get() = (getData(PlatformDataKeys.FILE_EDITOR) as TextEditorWithPreview).previewEditor as D2SvgViewer