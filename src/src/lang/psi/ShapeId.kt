// This is a generated file. Not intended for manual editing.
package org.jetbrains.plugins.d2.lang.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.PsiReference
import com.intellij.psi.util.descendantsOfType
import com.intellij.psi.util.elementType
import org.jetbrains.plugins.d2.lang.D2ElementTypes

internal class ShapeDeclaration(node: ASTNode) : ASTWrapperPsiElement(node) {
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

class ShapeConnection(node: ASTNode) : ASTWrapperPsiElement(node)

class Connector(node: ASTNode) : ASTWrapperPsiElement(node)

class BlockDefinition(node: ASTNode) : ASTWrapperPsiElement(node)