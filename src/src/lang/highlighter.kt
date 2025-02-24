package org.jetbrains.plugins.d2.lang

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
import java.util.*

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

        // KEYWORD - as Markdown CODE_FENCE_MARKER
        private val BLOCK_STRING_MARKER: TextAttributesKey =
            createTextAttributesKey("D2_BLOCK_STRING_MARKER", DefaultLanguageHighlighterColors.KEYWORD)

        private val SHAPE_IDS = createTextAttributesKey("D2_SHAPE_IDS", DefaultLanguageHighlighterColors.LOCAL_VARIABLE)

        val ATTRIBUTES: Map<IElementType, TextAttributesKey> =
            IdentityHashMap<IElementType, TextAttributesKey>().apply {
                fillMap(this, D2TokenSets.IDENTIFIERS, IDENTIFIERS)
                fillMap(this, D2TokenSets.KEYWORDS, KEYWORDS)
                fillMap(this, D2TokenSets.ARROWS, ARROWS)
                fillMap(this, D2TokenSets.NUMBERS, NUMBERS)

                put(D2ElementTypes.ID, SHAPE_IDS)
                put(D2ElementTypes.PARENT_SHAPE_REF, SHAPE_IDS)

                put(D2ElementTypes.COMMENT, COMMENT)

                // shape and other simple keywords highlight as instance field, like JSON_PROPERTY_KEY in a JSON highlighter does
                put(D2ElementTypes.SIMPLE_RESERVED_KEYWORDS, FIELDS)
                put(D2ElementTypes.COMPOSITE_RESERVED_KEYWORDS, FIELDS)

                put(D2ElementTypes.STYLE_KEYWORD, FIELDS)
                put(D2ElementTypes.STYLE_KEYWORDS, FIELDS)
                put(D2ElementTypes.RESERVED_KEYWORD_HOLDERS, FIELDS)
                put(D2ElementTypes.CONTAINER_LESS_KEYWORDS, FIELDS)

                put(D2ElementTypes.STRING, STRING)
                put(D2ElementTypes.UNQUOTED_STRING, STRING)
                put(D2ElementTypes.COLOR, STRING)

                put(D2ElementTypes.DOT, DOT)

                put(D2ElementTypes.COLON, COLON)
                put(D2ElementTypes.SEMICOLON, SEMICOLON)

                put(D2ElementTypes.LBRACE, BRACES)
                put(D2ElementTypes.RBRACE, BRACES)

                put(D2ElementTypes.LBRACKET, BRACES)
                put(D2ElementTypes.RBRACKET, BRACES)

                put(D2ElementTypes.BLOCK_STRING_OPEN, BLOCK_STRING_MARKER)
                put(D2ElementTypes.BLOCK_STRING_CLOSE, BLOCK_STRING_MARKER)
                put(D2ElementTypes.BLOCK_STRING_LANG, IDENTIFIERS)
            }
    }

    override fun getHighlightingLexer(): Lexer = createD2Lexer()

    override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> = pack(ATTRIBUTES[tokenType])
}

class D2SyntaxHighlighterFactory : SyntaxHighlighterFactory() {
    override fun getSyntaxHighlighter(
        project: Project?,
        virtualFile: VirtualFile?
    ): SyntaxHighlighter = D2SyntaxHighlighter()
}