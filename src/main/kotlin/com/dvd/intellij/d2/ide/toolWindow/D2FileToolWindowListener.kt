package com.dvd.intellij.d2.ide.toolWindow

import com.dvd.intellij.d2.ide.utils.isD2
import com.dvd.intellij.d2.ide.utils.isD2Installed
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.fileEditor.TextEditorWithPreview
import com.intellij.openapi.vfs.VirtualFile

class D2FileToolWindowListener : FileEditorManagerListener, FileEditorManagerListener.Before {
  override fun fileOpened(source: FileEditorManager, file: VirtualFile) {
    if (!isD2Installed) return
    if (!file.isD2) return

    source.getSelectedEditor(file)?.let {
      source.project.service<D2ToolWindowService>().editorOpened((it as TextEditorWithPreview).previewEditor)
    }
  }

  override fun beforeFileClosed(source: FileEditorManager, file: VirtualFile) {
    if (!isD2Installed) return
    if (!file.isD2) return

    source.getSelectedEditor(file)?.let {
      source.project.service<D2ToolWindowService>().editorClosed((it as TextEditorWithPreview).previewEditor)
    }
  }
}