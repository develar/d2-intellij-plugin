package org.jetbrains.plugins.d2.execution

import com.dvd.intellij.d2.components.D2Layout
import com.intellij.execution.configurations.GeneralCommandLine
import java.io.File

sealed class D2Command<O> {
  abstract val args: List<String>

  open fun envVars(): Map<String, String> = emptyMap()

  abstract fun parseOutput(output: String): O

  data object Version : D2Command<D2CommandOutput.Version>() {
    override val args = listOf("--version")

    override fun parseOutput(output: String): D2CommandOutput.Version = D2CommandOutput.Version(output.removePrefix("v").removeSuffix("\n"))
  }

  data object LayoutEngines : D2Command<D2CommandOutput.LayoutEngines>() {
    private val LAYOUT_ENGINE_REGEX = "(\\w+)(?:\\s(\\(bundled\\)))?\\s-\\s(.*)".toRegex()

    override val args = listOf("layout")

    override fun parseOutput(output: String): D2CommandOutput.LayoutEngines {
      val layouts = output.splitToSequence('\n')
        .mapNotNull {
          val m = LAYOUT_ENGINE_REGEX.matchEntire(it.trim()) ?: return@mapNotNull null
          val (name, bundled, desc) = m.destructured
          D2Layout(name = name, bundled = bundled.isNotEmpty(), description = desc)
        }
        .toList()
      return D2CommandOutput.LayoutEngines(layouts)
    }
  }

  data class Format(val d2: File) : D2Command<D2CommandOutput.Format>() {
    private val formatted = File.createTempFile("d2_temp_${d2.name}", ".d2.formatted")
    override val args = buildList {
      add("fmt")

      d2.copyTo(formatted, overwrite = true)
      add(formatted.path)
    }

    // if failed during formatting, the console output is returned
    override fun parseOutput(output: String): D2CommandOutput.Format {
      val content = formatted.readText()
      formatted.delete()
      if ("err: failed" in output) return D2CommandOutput.Format(output)

      return D2CommandOutput.Format(content)
    }
  }

  fun createCommandLine(): GeneralCommandLine = GeneralCommandLine("d2", *args.toTypedArray()).withCharset(Charsets.UTF_8)
}