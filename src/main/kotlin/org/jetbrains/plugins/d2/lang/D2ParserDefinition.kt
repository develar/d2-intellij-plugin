package org.jetbrains.plugins.d2.lang

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IFileElementType
import org.jetbrains.plugins.d2.file.D2File

private val D2_FILE = IFileElementType(D2Language)

class D2ParserDefinition : ParserDefinition {
  override fun createLexer(p: Project?) = D2LexerAdapter()

  override fun getCommentTokens() = D2TokenSets.COMMENT

  override fun getStringLiteralElements() = D2TokenSets.STRING

  override fun createParser(p: Project?) = D2Parser()

  override fun getFileNodeType() = D2_FILE

  override fun createFile(provider: FileViewProvider) = D2File(provider)

  override fun createElement(node: ASTNode?): PsiElement = D2ElementTypes.Factory.createElement(node)
}