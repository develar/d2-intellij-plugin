package com.dvd.intellij.d2.ide.editor

import com.dvd.intellij.d2.ide.service.D2Service
import com.dvd.intellij.d2.ide.utils.isD2
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.fileEditor.TextEditorWithPreview
import com.intellij.openapi.vfs.VirtualFile

class D2FileCloseListener : FileEditorManagerListener.Before {
  override fun beforeFileClosed(source: FileEditorManager, file: VirtualFile) {
    val service = service<D2Service>()

    if (!file.isD2) return
    if (!service.isCompilerInstalled()) return

    val fileEditor = (source.getSelectedEditor(file) as? TextEditorWithPreview)?.previewEditor ?: return
    service.closeFile(fileEditor)
  }
}