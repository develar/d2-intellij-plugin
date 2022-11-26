package com.dvd.intellij.d2.ide.completion

import com.dvd.intellij.d2.ide.utils.Shapes
import com.dvd.intellij.d2.ide.utils.TruePattern
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.util.ProcessingContext

class D2ShapesTypesCompletionContributor : CompletionContributor() {

  private val shapes = Shapes.values().map {
    LookupElementBuilder.create(it.prettyName)
      .withIcon(AllIcons.Nodes.Constant)
  }

  init {
    extend(CompletionType.BASIC, TruePattern, object : CompletionProvider<CompletionParameters>() {
      override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
      ) = result.addAllElements(shapes)
    })
  }

}

