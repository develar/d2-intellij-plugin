package org.jetbrains.plugins.d2.completion

import com.dvd.intellij.d2.ide.utils.D2Icons
import com.dvd.intellij.d2.ide.utils.KEYWORD_HOLDERS
import com.dvd.intellij.d2.ide.utils.SIMPLE_RESERVED_KEYWORDS
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.elementType
import com.intellij.psi.util.siblings
import org.jetbrains.annotations.VisibleForTesting
import org.jetbrains.plugins.d2.lang.D2ElementTypes
import org.jetbrains.plugins.d2.lang.psi.D2BlockDefinition
import org.jetbrains.plugins.d2.lang.psi.D2Connector
import org.jetbrains.plugins.d2.lang.psi.D2ShapeDefinition

private val keywords = (SIMPLE_RESERVED_KEYWORDS + KEYWORD_HOLDERS).map {
  val builder = LookupElementBuilder.create(it)
    .withIcon(D2Icons.ATTRIBUTE)
  if (it != "style") {
    builder.withInsertHandler(ColonLookupElementInsertHandler)
  }
  builder
}

private object ColonLookupElementInsertHandler : InsertHandler<LookupElement> {
  override fun handleInsert(context: InsertionContext, item: LookupElement) {
    val caretOffset = context.editor.caretModel.offset
    val colon = ": "
    context.document.insertString(context.selectionEndOffset, colon)
    context.editor.caretModel.moveToOffset(caretOffset + colon.length)
  }
}

@VisibleForTesting
internal val varsAndClasses: List<LookupElementBuilder> = sequenceOf("vars", "classes")
  .map {
    LookupElementBuilder.create(it)
      .withIcon(D2Icons.ATTRIBUTE)
      .withInsertHandler(ColonLookupElementInsertHandler)
  }
  .toList()

private val connections = sequenceOf("--", "->", "<-", "<->")
  .map {
    LookupElementBuilder.create(it)
      .withIcon(D2Icons.CONNECTION)
  }
  .toList()


private class D2BasicCompletionContributor : CompletionContributor() {
  override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
    if (parameters.completionType != CompletionType.BASIC) {
      return
    }

    val position = parameters.position
    val parent = position.context
    if (parent !is D2ShapeDefinition) {
      return
    }

    // see D2CompletionTest.connection
    if (position.prevSibling is PsiWhiteSpace) {
      result.addAllElements(connections)
    } else {
      // reserved keywords are prohibited in edges
      if (parent.siblings(withSelf = false).none(::notConnectorOrArrow) &&
        parent.siblings(withSelf = false, forward = false).none(::notConnectorOrArrow)) {
        val inMap = parent.context is D2BlockDefinition
        result.addAllElements(keywords)
        if (!inMap) {
          // classes and vars only in a file context
          result.addAllElements(varsAndClasses)
        }
      }
    }
  }
}

private fun notConnectorOrArrow(it: PsiElement) = it is D2Connector || it.elementType == D2ElementTypes.ARROW