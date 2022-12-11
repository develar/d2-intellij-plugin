package com.dvd.intellij.d2.ide.execution

import com.dvd.intellij.d2.components.D2Layout
import com.dvd.intellij.d2.components.D2Theme
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.ProcessHandler
import com.intellij.openapi.vfs.VirtualFile
import java.io.File

sealed class D2Command<O> {
  abstract val args: List<String>
  open fun envVars(): Map<String, String> = emptyMap()
  abstract fun parseOutput(output: String): O

  data class Generate(
    val d2: VirtualFile,
    val dest: VirtualFile,
    val port: Int,
    val theme: D2Theme = D2Theme.DEFAULT,
    val layout: D2Layout = D2Layout.DEFAULT,
  ) : D2Command<D2CommandOutput.Generate>() {
    var process: ProcessHandler? = null

    override val args = buildList {
      add("--watch")

      add("--port")
      add(port.toString())

      add("--layout")
      add(layout.name)

      add("--theme")
      add(theme.id.toString())

      add(d2.path)
      add(dest.path)
    }

    override fun envVars(): Map<String, String> = mapOf(
      "BROWSER" to "0",
    )

    override fun parseOutput(output: String): D2CommandOutput.Generate = D2CommandOutput.Generate(this, output)
  }

  /*data*/ object Version : D2Command<D2CommandOutput.Version>() {
    override val args = listOf("--version")
    override fun parseOutput(output: String): D2CommandOutput.Version =
      D2CommandOutput.Version(output.removePrefix("v").removeSuffix("\n"))
  }

  /*data*/ object LayoutEngines : D2Command<D2CommandOutput.LayoutEngines>() {
    private val LAYOUT_ENGINE_REGEX = "(\\w+)(?:\\s(\\(bundled\\)))?\\s-\\s(.*)".toRegex()

    override val args = listOf("layout")
    override fun parseOutput(output: String): D2CommandOutput.LayoutEngines {
      val layouts = output.split("\n").mapNotNull {
        val m = LAYOUT_ENGINE_REGEX.matchEntire(it.trim()) ?: return@mapNotNull null
        val (name, bundled, desc) = m.destructured

        D2Layout(name, bundled.isNotEmpty(), desc)
      }
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

    override fun parseOutput(output: String): D2CommandOutput.Format {
      val content = formatted.readText()
      formatted.delete()
      return D2CommandOutput.Format(content)
    }
  }

  val gcl get() = GeneralCommandLine("d2", *args.toTypedArray())
}