package com.dvd.intellij.d2.ide.completion

import com.dvd.intellij.d2.ide.utils.ColorStyleValidator
import com.dvd.intellij.d2.ide.utils.FontStyleValidator
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext
import com.intellij.util.ui.ColorIcon

val BOOLEAN_COMPLETION = object : CompletionProvider<CompletionParameters>() {
  override fun addCompletions(
    parameters: CompletionParameters,
    context: ProcessingContext,
    result: CompletionResultSet
  ) {
    val elements = listOf(true, false).map { LookupElementBuilder.create(it).bold() }
    result.addAllElements(elements)
  }
}

val COLOR_COMPLETION = object : CompletionProvider<CompletionParameters>() {
  override fun addCompletions(
    parameters: CompletionParameters,
    context: ProcessingContext,
    result: CompletionResultSet
  ) {
    val elements = ColorStyleValidator.NAMED_COLORS.map { (name, color) ->
      LookupElementBuilder.create(name)
        .withTypeIconRightAligned(true) // todo: this doesn't work, at least on new ui
        .run {
          if (color != null) withIcon(ColorIcon(16, color))
          else this
        }
    }
    result.addAllElements(elements)
  }
}

val FONT_COMPLETION = object : CompletionProvider<CompletionParameters>() {
  override fun addCompletions(
    parameters: CompletionParameters,
    context: ProcessingContext,
    result: CompletionResultSet
  ) {
    val elements = FontStyleValidator.SYSTEM_FONTS.map { LookupElementBuilder.create(it) }
    result.addAllElements(elements)
  }
}