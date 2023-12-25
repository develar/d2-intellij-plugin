package org.jetbrains.plugins.d2.editor

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.spellchecker.inspections.CommentSplitter
import com.intellij.spellchecker.tokenizer.SpellcheckingStrategy
import com.intellij.spellchecker.tokenizer.TokenConsumer
import com.intellij.spellchecker.tokenizer.Tokenizer

private class D2SpellCheckerStrategy : SpellcheckingStrategy() {
  override fun getTokenizer(element: PsiElement): Tokenizer<*> {
    return when (element) {
      is PsiComment -> D2CommentTokenizer
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
      /* element = */ element,
      /* text = */ element.text,
      /* useRename = */false,
      /* offset = */ 0,
      /* rangeToCheck = */ TextRange.create(startIndex, element.textLength),
      /* splitter = */ CommentSplitter.getInstance()
    )
  }
}
