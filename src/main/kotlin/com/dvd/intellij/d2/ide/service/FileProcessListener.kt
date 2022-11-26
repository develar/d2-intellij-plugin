package com.dvd.intellij.d2.ide.service

import com.intellij.execution.process.ProcessEvent
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.util.Key

interface FileProcessListener {
  fun onTextAvailable(fileEditor: FileEditor, event: ProcessEvent, outputType: Key<*>)
  fun processWillTerminate(fileEditor: FileEditor, event: ProcessEvent, willBeDestroyed: Boolean)
}