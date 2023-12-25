package org.jetbrains.plugins.d2.completion

import com.dvd.intellij.d2.ide.utils.ColorStyleValidator
import com.dvd.intellij.d2.ide.utils.FontStyleValidator
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ui.ColorIcon

val BOOLEAN_COMPLETION = listOf(true, false).map { LookupElementBuilder.create(it).bold() }

val COLOR_COMPLETION = ColorStyleValidator.NAMED_COLORS.map { (name, color) ->
  LookupElementBuilder.create(name)
    .withTypeIconRightAligned(true) // todo: this doesn't work, at least on new ui
    .run {
      if (color != null) withIcon(ColorIcon(16, color))
      else this
    }
}

val FONT_COMPLETION = FontStyleValidator.SYSTEM_FONTS.map { LookupElementBuilder.create(it) }