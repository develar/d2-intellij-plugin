package com.dvd.intellij.d2.ide.lang

import com.dvd.intellij.d2.ide.parser.D2TokenSets
import com.dvd.intellij.d2.lang.D2ElementTypes
import com.dvd.intellij.d2.lang.D2LexerAdapter
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.tree.IElementType

class D2SyntaxHighlighter : SyntaxHighlighterBase() {
  companion object {
    internal val IDENTIFIERS =
      createTextAttributesKey("D2_IDENTIFIERS", DefaultLanguageHighlighterColors.IDENTIFIER)
    internal val KEYWORDS = createTextAttributesKey("D2_KEYWORDS", DefaultLanguageHighlighterColors.KEYWORD)
    internal val COMMENT = createTextAttributesKey("D2_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
    internal val ARROWS = createTextAttributesKey("D2_ARROWS", DefaultLanguageHighlighterColors.OPERATION_SIGN)
    internal val BRACES = createTextAttributesKey("D2_BRACES", DefaultLanguageHighlighterColors.BRACES)
    internal val COLON = createTextAttributesKey("D2_COLON", DefaultLanguageHighlighterColors.OPERATION_SIGN)
    internal val SEMICOLON = createTextAttributesKey("D2_SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON)
    internal val DOT = createTextAttributesKey("D2_DOT", DefaultLanguageHighlighterColors.DOT)
    internal val STRING = createTextAttributesKey("D2_STRING", DefaultLanguageHighlighterColors.STRING)
    internal val NUMBERS = createTextAttributesKey("D2_NUMBERS", DefaultLanguageHighlighterColors.NUMBER)
    internal val FIELDS = createTextAttributesKey("D2_FIELDS", DefaultLanguageHighlighterColors.INSTANCE_FIELD)

    val ATTRIBUTES = buildMap<IElementType, TextAttributesKey> {
      fillMap(this, D2TokenSets.IDENTIFIERS, IDENTIFIERS)
      fillMap(this, D2TokenSets.KEYWORDS, KEYWORDS)
      fillMap(this, D2TokenSets.ARROWS, ARROWS)
      fillMap(this, D2TokenSets.NUMBERS, NUMBERS)
      this += (D2ElementTypes.COMMENT to COMMENT)
      this += (D2ElementTypes.DOT to DOT)
      this += (D2ElementTypes.COLON to COLON)
      this += (D2ElementTypes.SEMICOLON to SEMICOLON)
      this += (D2ElementTypes.STRING_LITERAL to STRING)
      this += (D2ElementTypes.LABEL_DEFINITION to STRING)
      fillMap(this, D2TokenSets.BRACES, BRACES)
      // todo string, fields
    }
  }

  override fun getHighlightingLexer(): Lexer = D2LexerAdapter()

  override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> = pack(ATTRIBUTES[tokenType])
}

class D2SyntaxHighlighterFactory : SyntaxHighlighterFactory() {
  override fun getSyntaxHighlighter(
    project: Project?,
    virtualFile: VirtualFile?
  ): SyntaxHighlighter = D2SyntaxHighlighter()
}