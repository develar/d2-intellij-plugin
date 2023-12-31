// This is a generated file. Not intended for manual editing.
package org.jetbrains.plugins.d2.lang.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.PsiReference
import com.intellij.psi.util.descendants
import com.intellij.psi.util.descendantsOfType
import com.intellij.psi.util.elementType
import org.jetbrains.plugins.d2.lang.D2ElementTypes

internal class ShapeDeclaration(node: ASTNode) : ASTWrapperPsiElement(node) {
  //override fun getName(): String? {
  //  return findId()?.name
  //}

  fun findId(): ShapeId? {
    var child: PsiElement? = firstChild
    var id: ShapeId? = null
    while (child != null) {
      if (child is ShapeId) {
        // todo support FQN (for now, just use the latest ID)
        id = child
      } else if (child.elementType == D2ElementTypes.COLON) {
        break
      }

      child = child.nextSibling
    }
    return id
  }

  //override fun getNameIdentifier(): PsiElement? = findId()?.getNameIdentifier()

  //override fun setName(name: String): PsiElement? = findId()?.setName(name)
}

internal class ShapeId(node: ASTNode) : ASTWrapperPsiElement(node), PsiNamedElement {
  fun getValueTextRange(): TextRange {
    val child = firstChild
    val textLength = child.textLength
    if (child.elementType == D2ElementTypes.STRING) {
      if (textLength == 2) {
        // empty
        return TextRange.EMPTY_RANGE
      } else {
        return TextRange.from(1, textLength - 1)
      }
    } else {
      return TextRange.from(0, textLength)
    }
  }

  override fun getName(): String? {
    val child = firstChild
    if (child.elementType == D2ElementTypes.STRING) {
      return child.text.removeSurrounding("\"")
    } else {
      return child.text
    }
  }

  //fun getNameIdentifier(): PsiElement? = firstChild

  override fun setName(name: String): PsiElement? {
    val child = firstChild ?: return null
    val project = child.project
    val newPsi = PsiFileFactory.getInstance(project).createFileFromText(name, child.containingFile) ?: return null
    return firstChild?.replace(newPsi.descendantsOfType<ShapeId>().first().firstChild)
  }

  override fun getReference(): PsiReference? {
    if (parent is ShapeDeclaration) {
      return null
    }
    return ShapePsiReference(this)
  }
}

private class ShapePsiReference(private val reference: ShapeId) : PsiReference {
  override fun getElement() = reference

  override fun getRangeInElement() = reference.getValueTextRange()

  // todo FQN
  override fun resolve(): PsiElement? {
    var lastCandidate: ShapeId? = null
    for (element in reference.containingFile.descendants()) {
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

class ShapeLabel(node: ASTNode) : ASTWrapperPsiElement(node)

class ShapeConnection(node: ASTNode) : ASTWrapperPsiElement(node)

class Connector(node: ASTNode) : ASTWrapperPsiElement(node)

class BlockDefinition(node: ASTNode) : ASTWrapperPsiElement(node)