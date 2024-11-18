package org.jetbrains.plugins.d2.editor

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.ProcessHandler
import com.intellij.openapi.vfs.VirtualFile
import org.jetbrains.plugins.d2.D2Layout
import java.nio.file.Files
import java.nio.file.Path

internal class D2CompilerState(
  val input: VirtualFile,
  val targetFile: Path,
  val port: Int,
  val theme: String?,
  val layout: D2Layout?,
  val sketch: Boolean,
  val log: StringBuilder,
) {
  var process: ProcessHandler? = null

  fun deleteTargetFile() {
    targetFile.let { Files.deleteIfExists(it) }
  }

  fun createCommandLine(watch: Boolean, targetFile: Path): GeneralCommandLine {
    val command = GeneralCommandLine().withCharset(Charsets.UTF_8)
    val parameters = command.parametersList
    command.setWorkDirectory(input.parent.path)
    command.exePath = "d2"
    if (watch) {
      parameters.add("--watch")
      parameters.add("--browser", "0")

      parameters.add("--port", port.toString())
    }

    layout?.let {
      parameters.add("--layout", it.name)
    }

    theme?.let {
      parameters.add("--theme", it)
    }

    if (sketch) {
      parameters.add("--sketch")
    }

    parameters.add(input.path)
    parameters.add(targetFile.toString())
    return command
  }
}