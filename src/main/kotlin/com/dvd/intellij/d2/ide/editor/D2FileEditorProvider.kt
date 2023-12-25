package com.dvd.intellij.d2.ide.editor

import com.dvd.intellij.d2.ide.editor.images.D2SvgViewer
import com.dvd.intellij.d2.ide.utils.D2_EDITOR_NAME
import com.dvd.intellij.d2.ide.utils.isD2
import com.intellij.openapi.fileEditor.*
import com.intellij.openapi.fileEditor.impl.text.TextEditorProvider
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

// https://github.com/JetBrains/intellij-community/blob/master/images/src/org/intellij/images/editor/impl/ImageFileEditorProvider.java
private class D2FileEditorProvider : FileEditorProvider, DumbAware {
  override fun accept(project: Project, file: VirtualFile): Boolean = file.isD2

  // 2023.3
  @Suppress("unused")
  fun acceptRequiresReadAction(): Boolean {
    return false
  }

  override fun createEditor(project: Project, file: VirtualFile): FileEditor {
    val view = D2SvgViewer(project, file)
    val editor = TextEditorProvider.getInstance().createEditor(project, file) as TextEditor
    return TextEditorWithPreview(editor, view, D2_EDITOR_NAME, TextEditorWithPreview.Layout.SHOW_EDITOR_AND_PREVIEW)
  }

  override fun getEditorTypeId(): String = D2_EDITOR_NAME

  override fun getPolicy(): FileEditorPolicy = FileEditorPolicy.HIDE_DEFAULT_EDITOR
}