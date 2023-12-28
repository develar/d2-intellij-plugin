package org.jetbrains.plugins.d2.lang

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.lexer.FlexAdapter
import com.intellij.lexer.Lexer
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType

fun createD2Lexer(): Lexer = FlexAdapter(D2FlexLexer(null))

interface D2CompositeElement : PsiElement

open class D2CompositeElementImpl(node: ASTNode) : ASTWrapperPsiElement(node), D2CompositeElement

internal object D2ElementTypeFactory {
  @JvmStatic
  fun token(name: String): IElementType {
    return when (name) {
      "BLOCK_STRING_BODY" -> BlockStringElementType("BLOCK_STRING_BODY")
      else -> IElementType(name, D2Language)
    }
  }

  @JvmStatic
  fun element(name: String): IElementType = IElementType(name, D2Language)
}

