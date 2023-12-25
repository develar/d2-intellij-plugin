package org.jetbrains.plugins.d2.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.util.ProcessingContext
import org.jetbrains.plugins.d2.lang.D2ElementTypes

class D2ConnectorCompletionContributor : CompletionContributor() {
  init {
    extend(
      CompletionType.BASIC,
      PlatformPatterns.psiElement(D2ElementTypes.CONNECTOR),
      object : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(
          parameters: CompletionParameters,
          context: ProcessingContext,
          result: CompletionResultSet
        ) {
          result.addAllElements(listOf("--", "->", "<-", "<->").map {
            LookupElementBuilder.create(it)
          })
        }
      }
    )
  }
}