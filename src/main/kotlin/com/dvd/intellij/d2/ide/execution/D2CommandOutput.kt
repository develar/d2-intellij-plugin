package com.dvd.intellij.d2.ide.execution

import com.dvd.intellij.d2.components.D2Layout
import com.intellij.execution.process.ProcessHandler

sealed class D2CommandOutput {
  data class Version(val version: String) : D2CommandOutput()
  data class LayoutEngines(val layouts: List<D2Layout>) : D2CommandOutput()
  data class Generate(val cmd: D2Command.Generate, val log: String) : D2CommandOutput() {
    fun appendLog(log: String): Generate = copy(log = this.log + log)
  }
}