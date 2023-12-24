package com.dvd.intellij.d2.ide.editor

import com.dvd.intellij.d2.ide.lang.D2Language
import com.dvd.intellij.d2.ide.utils.ColorStyleValidator
import com.dvd.intellij.d2.lang.D2ElementTypes
import com.intellij.openapi.editor.ElementColorProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.PsiFileFactoryImpl
import com.intellij.psi.util.elementType
import java.awt.Color

class D2ColorProvider : ElementColorProvider {
  private val Color.hex: String
    get() = String.format("#%02x%02x%02x", red, green, blue)

  override fun getColorFrom(element: PsiElement): Color? {
    if (element.elementType !in listOf(D2ElementTypes.ID, D2ElementTypes.STRING)) {
      return null
    }
    if (element.parent.elementType != D2ElementTypes.ATTRIBUTE_VALUE) {
      return null
    }

    val text = element.text.removeSurrounding("\"")
    return when {
      ColorStyleValidator.COLOR_REGEX.matches(text) -> Color.decode(text)
      text in ColorStyleValidator.NAMED_COLORS -> ColorStyleValidator.NAMED_COLORS[text]
      else -> null
    }
  }

  // todo set not correctly working due to wrong psi
  override fun setColorTo(element: PsiElement, color: Color) {
    val psiManager = PsiManager.getInstance(element.project)
    val factory = PsiFileFactoryImpl(psiManager)
    val newPsi = factory.createElementFromText(
      "\"${color.hex}\"",
      D2Language,
      D2ElementTypes.ATTRIBUTE_VALUE,
      element.context
    ) ?: return

    // write action?
    element.parent.replace(newPsi)
  }
}