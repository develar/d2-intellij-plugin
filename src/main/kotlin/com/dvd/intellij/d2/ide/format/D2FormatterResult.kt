package com.dvd.intellij.d2.ide.format

sealed class D2FormatterResult {
  data class Success(val content: String) : D2FormatterResult()
  data class Error(val error: String) : D2FormatterResult()
}