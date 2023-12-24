package com.dvd.intellij.d2.ide.parser

import com.dvd.intellij.d2.ide.file.D2File
import com.dvd.intellij.d2.ide.lang.D2Language
import org.jetbrains.plugins.d2.lang.D2ElementTypes
import com.dvd.intellij.d2.lang.D2LexerAdapter
import org.jetbrains.plugins.d2.lang.D2Parser
import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet

class D2ParserDefinition : ParserDefinition {
  companion object {
    val FILE = IFileElementType(D2Language)
  }

  override fun createLexer(p: Project?): Lexer = D2LexerAdapter()
  override fun getCommentTokens(): TokenSet = D2TokenSets.COMMENT
  override fun getStringLiteralElements(): TokenSet = TokenSet.EMPTY
  override fun createParser(p: Project?): PsiParser = D2Parser()
  override fun getFileNodeType(): IFileElementType = FILE
  override fun createFile(provider: FileViewProvider): PsiFile = D2File(provider)
  override fun createElement(node: ASTNode?): PsiElement = D2ElementTypes.Factory.createElement(node)
}