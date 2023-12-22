package com.dvd.intellij.d2.ide.utils

import com.dvd.intellij.d2.ide.editor.images.D2FileEditorImpl
import com.dvd.intellij.d2.ide.execution.D2Command
import com.dvd.intellij.d2.ide.file.D2FileType
import com.dvd.intellij.d2.ide.service.D2Service
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.TextEditorWithPreview
import com.intellij.openapi.vfs.VirtualFile
import java.nio.file.Path
import kotlin.io.path.Path


val VirtualFile.isD2: Boolean
  get() = fileType == D2FileType

val AnActionEvent.file: VirtualFile
  get() = getData(PlatformDataKeys.VIRTUAL_FILE) as VirtualFile

val AnActionEvent.d2FileEditor
  get() = (getData(PlatformDataKeys.FILE_EDITOR) as TextEditorWithPreview).previewEditor as D2FileEditorImpl

val FileEditor.generatedCommand: D2Command.Generate?
  get() = service<D2Service>().map[this]?.cmd

val FileEditor.generatedFile: VirtualFile?
  get() = generatedCommand?.dest
