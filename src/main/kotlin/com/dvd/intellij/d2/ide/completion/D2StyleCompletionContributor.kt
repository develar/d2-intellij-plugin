package com.dvd.intellij.d2.ide.completion

import com.dvd.intellij.d2.ide.utils.ShapeStyles
import com.dvd.intellij.d2.ide.utils.TruePattern
import com.intellij.codeInsight.AutoPopupController
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.util.ProcessingContext


class D2StyleCompletionContributor : CompletionContributor() {

  // todo: filter for shape
  private val styleAttrs = ShapeStyles.values().map {
    LookupElementBuilder.create(it.keyword)
      .withIcon(AllIcons.Actions.Show)
      .withInsertHandler { context, _ ->
        with(context) {
          val caretOffset = editor.caretModel.offset

          val colon = ": "
          document.insertString(selectionEndOffset, colon)
          editor.caretModel.moveToOffset(caretOffset + colon.length + 1)

          if (it.completionContributor != null) {
            // invoke contributor again
            AutoPopupController.getInstance(project).autoPopupMemberLookup(editor, null)
          }
        }
      }
  }

  init {
    extend(CompletionType.BASIC, TruePattern, object : CompletionProvider<CompletionParameters>() {
      override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
      ) = result.addAllElements(styleAttrs)
    })
  }

}
