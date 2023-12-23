package com.dvd.intellij.d2.ide.service

import com.dvd.intellij.d2.components.D2Layout
import com.dvd.intellij.d2.ide.action.ConversionOutput
import com.dvd.intellij.d2.ide.execution.D2CommandOutput
import com.dvd.intellij.d2.ide.format.D2FormatterResult
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.TextEditor
import java.io.File
import java.nio.file.Path

interface D2Service {
  val map: Map<FileEditor, D2CommandOutput.Generate>

  fun isCompilerInstalled(): Boolean

  fun getCompilerVersion(): String?

  fun getLayoutEngines(): List<D2Layout>?

  fun compile(fileEditor: FileEditor)

  fun scheduleCompile(fileEditor: TextEditor)

  fun closeFile(fileEditor: FileEditor)

  fun format(file: File): D2FormatterResult

  fun convert(file: Path, format: ConversionOutput): ByteArray
}