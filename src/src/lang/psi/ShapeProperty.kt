// This is a generated file. Not intended for manual editing.
package org.jetbrains.plugins.d2.lang.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.ui.ColorHexUtil
import org.jetbrains.plugins.d2.NAMED_COLORS
import java.awt.Color

class ShapeProperty(node: ASTNode) : ASTWrapperPsiElement(node)

class ShapePropertyKey(node: ASTNode) : ASTWrapperPsiElement(node)

class IdPropertyMap(node: ASTNode) : ASTWrapperPsiElement(node)
class IdProperty(node: ASTNode) : ASTWrapperPsiElement(node)

class D2Array(node: ASTNode) : ASTWrapperPsiElement(node)

sealed interface PropertyValue : PsiElement

class OtherValue(node: ASTNode) : ASTWrapperPsiElement(node), PropertyValue

class StringValue(node: ASTNode) : ASTWrapperPsiElement(node), PropertyValue, ColorValueProvider {
  override fun getColor(): Color? = NAMED_COLORS.get(text.removeSurrounding("\""))
}

class UnquotedStringValue(node: ASTNode) : ASTWrapperPsiElement(node), PropertyValue, ColorValueProvider {
  override fun getColor(): Color? = NAMED_COLORS.get(text)
}

class ColorValue(node: ASTNode) : ASTWrapperPsiElement(node), PropertyValue, ColorValueProvider {
  override fun getColor(): Color? = ColorHexUtil.fromHexOrNull(text.removeSurrounding("\""))
}
