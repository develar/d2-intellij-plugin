package com.dvd.intellij.d2.ide.service

import com.dvd.intellij.d2.components.D2Layout
import com.dvd.intellij.d2.ide.action.ConversionOutput
import com.dvd.intellij.d2.ide.execution.D2CommandOutput
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.vfs.VirtualFile


interface D2Service {
  val map: Map<FileEditor, D2CommandOutput.Generate>

  val isCompilerInstalled: Boolean
  val compilerVersion: String?

  val layoutEngines: List<D2Layout>?

  fun compile(fileEditor: FileEditor)
  fun closeFile(fileEditor: FileEditor)

  fun convert(file: VirtualFile, format: ConversionOutput): ByteArray
}