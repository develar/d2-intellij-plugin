package org.jetbrains.plugins.d2.lang.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.util.descendants
import com.intellij.psi.util.parentOfType
import org.jetbrains.annotations.VisibleForTesting

internal class ShapePsiReference(private val shapeId: ShapeId) : PsiReference {
    override fun getElement() = shapeId

    override fun getRangeInElement() = shapeId.getValueTextRange()

    override fun resolve(): PsiElement? {
        val qualified = buildFQN(shapeId)
        if (qualified.isEmpty()) {
            return null
        }

        for (element in shapeId.containingFile.descendants(canGoInside = { it !is IdPropertyMap && it !is D2Array && it !is ShapeProperty })) {
            if (element is ShapeId) {
                if (element === shapeId) {
                    break
                }

                if (compare(buildFQN(element), qualified)) {
                    return element
                }
            }
        }

        return null
    }

    override fun getCanonicalText(): String = shapeId.name ?: ""

    override fun handleElementRename(newElementName: String): PsiElement? {
        return shapeId.setName(newElementName)
    }

    override fun bindToElement(element: PsiElement): PsiElement? = null

    override fun isReferenceTo(element: PsiElement): Boolean {
        if (element !is ShapeId) {
            return false
        }

        // todo FQN
        // check first text length to avoid string compare
        return compare(shapeId, element)
    }

    override fun isSoft() = true
}

@VisibleForTesting
internal fun buildFQN(shapeId: ShapeId): List<ShapeId> {
    val qualified = ArrayList<ShapeId>()
    qualified.add(shapeId)
    var child = shapeId.prevSibling
    var parentShapeRef: ParentShapeRef? = null
    // qualify by ref chain
    while (child != null) {
        if (child is ShapeId) {
            qualified.add(child)
        } else if (child is ParentShapeRef && child.prevSibling == null) {
            parentShapeRef = child
        }

        child = child.prevSibling
        if (child === shapeId || child == null) {
            break
        }
    }

    // qualify by parent containers
    var container =
        if (parentShapeRef == null) shapeId.parent?.parentOfType<ShapeDeclaration>() else parentShapeRef.resolve() as ShapeDeclaration?
    while (container != null) {
        val id = container.findId() ?: continue
        qualified.add(id)

        container = container.parentOfType<ShapeDeclaration>()
    }
    return qualified
}

private fun compare(a: List<ShapeId>, b: List<ShapeId>): Boolean {
    return a.size == b.size && a.indices.all { compare(a[it], b[it]) }
}

private fun compare(a: ShapeId, b: ShapeId): Boolean {
    return a.getValueTextRange().length == b.getValueTextRange().length && a.name == b.name
}