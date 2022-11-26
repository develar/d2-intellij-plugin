package com.dvd.intellij.d2.ide.utils

import com.intellij.patterns.ElementPattern
import com.intellij.patterns.ElementPatternCondition
import com.intellij.patterns.InitialPatternCondition
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext

object TruePattern : ElementPattern<PsiElement> {
  override fun accepts(o: Any?): Boolean {
    return false
  }

  override fun accepts(o: Any?, context: ProcessingContext): Boolean {
    return true
  }

  override fun getCondition(): ElementPatternCondition<PsiElement> {
    return ElementPatternCondition(object : InitialPatternCondition<PsiElement>(
      PsiElement::class.java
    ) {
      override fun accepts(o: Any?, context: ProcessingContext?): Boolean = true
    })
  }
}