// This is a generated file. Not intended for manual editing.
package org.jetbrains.plugins.d2.lang.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.ui.ColorHexUtil
import org.jetbrains.plugins.d2.NAMED_COLORS
import java.awt.Color

sealed interface D2PropertyValue : PsiElement

class OtherValue(node: ASTNode) : ASTWrapperPsiElement(node), D2PropertyValue {
  fun accept(visitor: D2Visitor) {
    visitor.visitPropertyValue(this)
  }

  override fun accept(visitor: PsiElementVisitor) {
    if (visitor is D2Visitor) accept(visitor)
    else super.accept(visitor)
  }
}

class StringValue(node: ASTNode) : ASTWrapperPsiElement(node), D2PropertyValue, ColorValueProvider {
  override fun getColor(): Color? = NAMED_COLORS.get(text.removeSurrounding("\""))
}

class UnquotedStringValue(node: ASTNode) : ASTWrapperPsiElement(node), D2PropertyValue, ColorValueProvider {
  override fun getColor(): Color? = NAMED_COLORS.get(text)
}

class ColorValue(node: ASTNode) : ASTWrapperPsiElement(node), D2PropertyValue, ColorValueProvider {
  override fun getColor(): Color? = ColorHexUtil.fromHexOrNull(text.removeSurrounding("\""))
}
