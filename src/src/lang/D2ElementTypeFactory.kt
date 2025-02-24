package org.jetbrains.plugins.d2.lang

import com.intellij.lexer.FlexAdapter
import com.intellij.lexer.Lexer
import com.intellij.psi.tree.IElementType

fun createD2Lexer(): Lexer = FlexAdapter(D2FlexLexer(null))

internal object D2ElementTypeFactory {
    @JvmStatic
    fun token(name: String): IElementType {
        return when (name) {
            "BLOCK_STRING_BODY" -> BlockStringElementType("BLOCK_STRING_BODY")
            else -> IElementType(name, D2Language)
        }
    }

    @JvmStatic
    fun element(name: String): IElementType {
        return IElementType(name, D2Language)
    }
}

