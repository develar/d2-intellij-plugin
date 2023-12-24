package org.jetbrains.plugins.d2.editor

import com.dvd.intellij.d2.ide.utils.ColorStyleValidator
import com.intellij.openapi.editor.ElementColorProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.util.descendantsOfType
import com.intellij.psi.util.elementType
import com.intellij.ui.ColorUtil
import org.jetbrains.plugins.d2.lang.D2ElementTypes
import org.jetbrains.plugins.d2.lang.psi.D2PropertyValue
import java.awt.Color

class D2ColorProvider : ElementColorProvider {
  override fun getColorFrom(element: PsiElement): Color? {
    // Not a leaf child with a value must be checked, but a holder.
    // On setColorTo we replace the leaf child, so the holder element will become invalid, and after the first change the PSI element will be invalid (as we replaced it).
    if (element.elementType != D2ElementTypes.PROPERTY_VALUE) {
      return null
    }

    val firstChild = element.firstChild
    if (firstChild.elementType != D2ElementTypes.STRING) {
       return null
     }

    val textLength = firstChild.textLength
    if (textLength > 9 || textLength < 4) {
      return null
    }

    val text = firstChild.text.removeSurrounding("\"")
    return when {
      ColorStyleValidator.COLOR_REGEX.matches(text) -> Color.decode(text)
      text in ColorStyleValidator.NAMED_COLORS -> ColorStyleValidator.NAMED_COLORS[text]
      else -> null
    }
  }

  override fun setColorTo(element: PsiElement, color: Color) {
    val project = element.project
    val newPsi = PsiFileFactory.getInstance(project).createFileFromText(
      "shapeId.style.stroke: \"#${ColorUtil.toHex(color)}\"",
      element.containingFile
    ) ?: return

    element.firstChild.replace(newPsi.descendantsOfType<D2PropertyValue>().first().firstChild)
  }
}