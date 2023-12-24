package org.jetbrains.plugins.d2.lang

import com.dvd.intellij.d2.ide.lang.D2Language
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.lexer.FlexAdapter
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType

class D2TokenType(private val name: String) : IElementType("D2_TOKEN", D2Language) {
  override fun toString() = "D2TokenType.$name"
}

class D2ElementType(private val name: String) : IElementType("D2_ELEMENT", D2Language) {
  override fun toString() = "D2ElementType.$name"
}

class D2LexerAdapter : FlexAdapter(_D2Lexer(null))

interface D2CompositeElement : PsiElement

open class D2CompositeElementImpl(node: ASTNode) : ASTWrapperPsiElement(node), D2CompositeElement {

}

