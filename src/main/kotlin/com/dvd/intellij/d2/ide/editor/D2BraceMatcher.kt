package com.dvd.intellij.d2.ide.editor

import org.jetbrains.plugins.d2.lang.D2ElementTypes
import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType

// todo
class D2BraceMatcher : PairedBraceMatcher {
  override fun getPairs(): Array<BracePair> = arrayOf(BracePair(D2ElementTypes.LBRACE, D2ElementTypes.RBRACE, true))

  // todo
  override fun isPairedBracesAllowedBeforeType(type: IElementType, tokenType: IElementType?): Boolean = true

  override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int = openingBraceOffset
}