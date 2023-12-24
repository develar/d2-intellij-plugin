package org.jetbrains.plugins.d2.lang

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import org.jetbrains.plugins.d2.lang.psi.D2LabelDefinition
import org.jetbrains.plugins.d2.lang.psi.D2PropertyKey

/**
 * Syntax highlighter works on a lexer level - to highlight composite things like property keys, Annotator is required
 */
class D2LiteralAnnotator : Annotator {
  override fun annotate(element: PsiElement, holder: AnnotationHolder) {
    if (element is D2PropertyKey) {
      holder.newSilentAnnotation(HighlightSeverity.INFORMATION).textAttributes(D2SyntaxHighlighter.PROPERTY_KEY).create()
    }
    else if (element is D2LabelDefinition) {
      holder.newSilentAnnotation(HighlightSeverity.INFORMATION).textAttributes(D2SyntaxHighlighter.STRING).create()
    }
  }
}