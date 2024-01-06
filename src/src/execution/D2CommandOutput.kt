package org.jetbrains.plugins.d2.execution

sealed class D2CommandOutput {
  data class Version(val version: String) : D2CommandOutput()

  data class Format(val content: String?) : D2CommandOutput()
}