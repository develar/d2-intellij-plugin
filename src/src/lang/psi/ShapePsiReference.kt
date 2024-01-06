package org.jetbrains.plugins.d2.lang.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.util.descendants

internal class ShapePsiReference(private val reference: ShapeId) : PsiReference {
  override fun getElement() = reference

  override fun getRangeInElement() = reference.getValueTextRange()

  // todo FQN
  override fun resolve(): PsiElement? {
    var lastCandidate: ShapeId? = null
    for (element in reference.containingFile.descendants(canGoInside = { it !is IdPropertyMap && it !is D2Array && it !is ShapeProperty })) {
      if (element is ShapeDeclaration) {
        val shapeId = element.findId()
        if (shapeId != null && compare(shapeId, reference)) {
          return shapeId
        }
      } else if (element is ShapeId) {
        if (compare(element, reference)) {
          lastCandidate = element
        }
      }

      if (element === reference) {
        break
      }
    }

    return lastCandidate
  }

  override fun getCanonicalText(): String = reference.name ?: ""

  override fun handleElementRename(newElementName: String): PsiElement? {
    return reference.setName(newElementName)
  }

  override fun bindToElement(element: PsiElement): PsiElement? = null

  override fun isReferenceTo(element: PsiElement): Boolean {
    if (element !is ShapeId) {
      return false
    }

    // todo FQN
    // check first text length to avoid string compare
    return compare(reference, element)
  }

  override fun isSoft() = true
}

private fun compare(a: ShapeId, b: ShapeId): Boolean {
  return a.getValueTextRange().length == b.getValueTextRange().length && a.name == b.name
}