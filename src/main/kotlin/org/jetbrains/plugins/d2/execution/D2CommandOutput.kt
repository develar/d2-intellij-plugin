package org.jetbrains.plugins.d2.execution

import com.dvd.intellij.d2.components.D2Layout

sealed class D2CommandOutput {
  data class Version(val version: String) : D2CommandOutput()

  data class LayoutEngines(val layouts: List<D2Layout>) : D2CommandOutput()

  data class Format(val content: String?) : D2CommandOutput()
}