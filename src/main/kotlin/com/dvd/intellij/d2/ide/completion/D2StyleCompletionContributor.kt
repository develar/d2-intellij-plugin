package com.dvd.intellij.d2.ide.completion

import com.dvd.intellij.d2.ide.utils.ShapeStyles
import com.dvd.intellij.d2.lang.D2ElementTypes
import com.dvd.intellij.d2.lang.psi.impl.D2ShapeDefinitionImpl
import com.dvd.intellij.d2.lang.psi.impl.D2ShapeDefinitionsImpl
import com.dvd.intellij.d2.lang.psi.impl.D2SubShapeDefinitionImpl
import com.intellij.codeInsight.AutoPopupController
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.util.childrenOfType
import com.intellij.psi.util.parentOfType
import com.intellij.util.ProcessingContext


class D2StyleCompletionContributor : CompletionContributor() {

  private val styleAttrs = ShapeStyles.values().map {
    LookupElementBuilder.create(it.keyword)
      .withIcon(AllIcons.Actions.Show)
      .withInsertHandler { context, _ ->
        with(context) {
          val caretOffset = editor.caretModel.offset

          val colon = ": "
          document.insertString(selectionEndOffset, colon)
          editor.caretModel.moveToOffset(caretOffset + colon.length)

          if (it.completionElements != null) {
            // invoke contributor again
            AutoPopupController.getInstance(project).autoPopupMemberLookup(editor, null)
          }
        }
      }
  }

  init {
    // list style attributes
    extend(
      CompletionType.BASIC,
      PlatformPatterns.psiElement(D2ElementTypes.IDENTIFIER),
      object : CompletionProvider<CompletionParameters>() {
        override fun addCompletions(
          parameters: CompletionParameters,
          context: ProcessingContext,
          result: CompletionResultSet
        ) {
          val parent = parameters.position.parentOfType<D2SubShapeDefinitionImpl>() ?: return
          val subShapes = parent.childrenOfType<D2ShapeDefinitionImpl>()
          when {
            subShapes.first().text != "style" -> return
            CompletionUtilCore.DUMMY_IDENTIFIER_TRIMMED !in subShapes.last().text -> return
          }

          // todo: filter for shape
          result.addAllElements(styleAttrs)
        }
      }
    )

    // list style attribute values
    val styleValueCompletion = object : CompletionProvider<CompletionParameters>() {
      override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
      ) {
        val parent = parameters.position.parentOfType<D2ShapeDefinitionsImpl>() ?: return
        val subShapes = parent.childrenOfType<D2SubShapeDefinitionImpl>().firstOrNull()?.children ?: return
        subShapes.firstOrNull { it.text == "style" } ?: return

        val styleAttr = subShapes[1].text
        val elements = ShapeStyles.fromKeyword(styleAttr)?.completionElements ?: return

        result.addAllElements(elements)
      }
    }
    extend(CompletionType.BASIC, PlatformPatterns.psiElement(D2ElementTypes.IDENTIFIER), styleValueCompletion)
    extend(CompletionType.SMART, PlatformPatterns.psiElement(D2ElementTypes.IDENTIFIER), styleValueCompletion)
  }

}
