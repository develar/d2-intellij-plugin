package org.jetbrains.plugins.d2.editor

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.plugins.d2.lang.D2ElementTypes
import org.jetbrains.plugins.d2.lang.psi.ShapeId

private val BRACE_PAIRS = arrayOf(
    BracePair(D2ElementTypes.LBRACE, D2ElementTypes.RBRACE, true),
    BracePair(D2ElementTypes.LBRACKET, D2ElementTypes.RBRACKET, true),
    BracePair(D2ElementTypes.BLOCK_STRING_OPEN, D2ElementTypes.BLOCK_STRING_CLOSE, true),
)

private class D2BraceMatcher : PairedBraceMatcher {
    override fun getPairs() = BRACE_PAIRS

    override fun isPairedBracesAllowedBeforeType(type: IElementType, tokenType: IElementType?): Boolean = true

    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int {
        val element = file!!.findElementAt(openingBraceOffset)
        if (element == null || element is PsiFile) {
            return openingBraceOffset
        }

        val closestRuleset = PsiTreeUtil.getParentOfType(element, ShapeId::class.java)
        return closestRuleset?.textRange?.startOffset ?: openingBraceOffset
    }
}