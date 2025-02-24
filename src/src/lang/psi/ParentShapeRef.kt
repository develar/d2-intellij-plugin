package org.jetbrains.plugins.d2.lang.psi

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.PsiReference
import com.intellij.psi.util.parentOfType

internal class ParentShapeRef(node: ASTNode) : AstWrapperPsiElement(node), PsiNamedElement, PsiReference {
    override fun getName() = "_"

    override fun setName(name: String) = null

    override fun getReference() = this

    override fun getElement() = this

    override fun getRangeInElement() = TextRange.from(0, 1)

    override fun resolve(): PsiElement? {
        return parentOfType<ShapeDeclaration>()?.parentOfType<ShapeDeclaration>()?.findId()
    }

    override fun getCanonicalText(): String = name

    override fun handleElementRename(newElementName: String): PsiElement? {
        return setName(newElementName)
    }

    override fun bindToElement(element: PsiElement): PsiElement? = null

    override fun isReferenceTo(element: PsiElement): Boolean =
        element is ShapeId && manager.areElementsEquivalent(element, resolve())

    override fun isSoft() = true
}
