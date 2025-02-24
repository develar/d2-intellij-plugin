package org.jetbrains.plugins.d2.editor

import com.intellij.openapi.util.Key
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import com.intellij.spellchecker.inspections.CommentSplitter
import com.intellij.spellchecker.inspections.PlainTextSplitter
import com.intellij.spellchecker.tokenizer.SpellcheckingStrategy
import com.intellij.spellchecker.tokenizer.TokenConsumer
import com.intellij.spellchecker.tokenizer.Tokenizer
import org.jetbrains.plugins.d2.lang.D2ElementTypes

private class D2SpellCheckerStrategy : SpellcheckingStrategy() {
    override fun getTokenizer(element: PsiElement): Tokenizer<*> {
        return when {
            element is PsiComment -> D2CommentTokenizer
            element.elementType == D2ElementTypes.STRING -> StringLiteralTokenizer
            element.elementType == D2ElementTypes.UNQUOTED_STRING -> UnquotedStringLiteralTokenizer
            else -> EMPTY_TOKENIZER
        }
    }
}

private object D2CommentTokenizer : Tokenizer<PsiComment>() {
    override fun tokenize(element: PsiComment, consumer: TokenConsumer) {
        val startIndex = element.textToCharArray()
            .indexOfFirst { it != '#' && it != ' ' }
            .takeIf { it >= 0 } ?: return

        consumer.consumeToken(
            element,
            element.text,
            false,
            0,
            TextRange.create(startIndex, element.textLength),
            CommentSplitter.getInstance()
        )
    }
}

private val STRING_FRAGMENTS = Key<List<Pair<TextRange, String>>>("D2 string fragments")
private const val escapeTable = "\"\"\\\\//b\bf\u000cn\nr\rt\t"

private object UnquotedStringLiteralTokenizer : Tokenizer<PsiElement>() {
    override fun tokenize(element: PsiElement, consumer: TokenConsumer) {
        consumer.consumeToken(element, PlainTextSplitter.getInstance())
    }
}

private object StringLiteralTokenizer : Tokenizer<PsiElement>() {
    override fun tokenize(element: PsiElement, consumer: TokenConsumer) {
        val textSplitter = PlainTextSplitter.getInstance()
        if (element.textContains('\\')) {
            val fragments = getTextFragments(element)
            for (fragment in fragments) {
                val fragmentRange = fragment.first
                val escaped = fragment.second
                if (escaped.length == fragmentRange.length && !escaped.startsWith("\\")) {
                    consumer.consumeToken(
                        element,
                        escaped,
                        false,
                        fragmentRange.startOffset,
                        TextRange.allOf(escaped),
                        textSplitter
                    )
                }
            }
        } else {
            consumer.consumeToken(element, textSplitter)
        }
    }
}

private fun getTextFragments(literal: PsiElement): List<Pair<TextRange, String>> {
    literal.getUserData(STRING_FRAGMENTS)?.let {
        return it
    }

    val result = ArrayList<Pair<TextRange, String>>()
    val text = literal.text
    val length = text.length
    var pos = 1
    var unescapedSequenceStart = 1
    while (pos < length) {
        if (text[pos] == '\\') {
            if (unescapedSequenceStart != pos) {
                result.add(Pair(TextRange(unescapedSequenceStart, pos), text.substring(unescapedSequenceStart, pos)))
            }
            if (pos == length - 1) {
                result.add(Pair(TextRange(pos, pos + 1), "\\"))
                break
            }
            when (val next = text[pos + 1]) {
                '"', '\\', '/', 'b', 'f', 'n', 'r', 't' -> {
                    val index = escapeTable.indexOf(next)
                    result.add(Pair(TextRange(pos, pos + 2), escapeTable.substring(index + 1, index + 2)))
                    pos += 2
                }

                'u' -> {
                    var i = pos + 2
                    while (i < pos + 6) {
                        if (i == length || !StringUtil.isHexDigit(text[i])) {
                            break
                        }
                        i++
                    }
                    result.add(Pair(TextRange(pos, i), text.substring(pos, i)))
                    pos = i
                }

                'x' -> {
                    result.add(Pair(TextRange(pos, pos + 2), text.substring(pos, pos + 2)))
                    pos += 2
                }

                else -> {
                    result.add(Pair(TextRange(pos, pos + 2), text.substring(pos, pos + 2)))
                    pos += 2
                }
            }
            unescapedSequenceStart = pos
        } else {
            pos++
        }
    }
    val contentEnd = if (text[0] == text[length - 1]) length - 1 else length
    if (unescapedSequenceStart < contentEnd) {
        result.add(
            Pair(
                TextRange(unescapedSequenceStart, contentEnd),
                text.substring(unescapedSequenceStart, contentEnd)
            )
        )
    }

    literal.putUserData(STRING_FRAGMENTS, result)
    return result
}