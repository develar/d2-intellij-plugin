// This is a generated file. Not intended for manual editing.
package org.jetbrains.plugins.d2.lang.psi

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.PsiReference
import com.intellij.psi.util.descendantsOfType
import com.intellij.psi.util.elementType
import org.jetbrains.plugins.d2.lang.D2ElementTypes

internal class ShapeDeclaration(node: ASTNode) : AstWrapperPsiElement(node) {
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

internal class ShapeId(node: ASTNode) : AstWrapperPsiElement(node), PsiNamedElement {
    fun getValueTextRange(): TextRange {
        val child = firstChild
        val textLength = child.textLength
        return if (child.elementType == D2ElementTypes.STRING) {
            if (textLength == 2) {
                // empty
                TextRange.EMPTY_RANGE
            } else {
                TextRange.from(1, textLength - 1)
            }
        } else {
            TextRange.from(0, textLength)
        }
    }

    override fun getName(): String? {
        val child = firstChild
        return if (child.elementType == D2ElementTypes.STRING) {
            child.text.removeSurrounding("\"")
        } else {
            child.text
        }
    }

    override fun setName(name: String): PsiElement? {
        val child = firstChild ?: return null
        val project = child.project
        val newPsi = PsiFileFactory.getInstance(project).createFileFromText(name, child.containingFile) ?: return null
        return firstChild?.replace(newPsi.descendantsOfType<ShapeId>().first().firstChild)
    }

    override fun getReference(): PsiReference? {
        return if (parent is ShapeRef) ShapePsiReference(this) else null
    }
}

class ShapeConnection(node: ASTNode) : AstWrapperPsiElement(node)

class ShapeConnectionRef(node: ASTNode) : AstWrapperPsiElement(node)

class ShapeRef(node: ASTNode) : AstWrapperPsiElement(node)

class Connector(node: ASTNode) : AstWrapperPsiElement(node)

class BlockDefinition(node: ASTNode) : AstWrapperPsiElement(node)