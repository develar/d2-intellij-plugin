// This is a generated file. Not intended for manual editing.
package org.jetbrains.plugins.d2.lang.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor

class D2Connector(node: ASTNode) : ASTWrapperPsiElement(node) {
  fun accept(visitor: D2Visitor) {
    visitor.visitConnector(this)
  }

  override fun accept(visitor: PsiElementVisitor) {
    if (visitor is D2Visitor) accept(visitor)
    else super.accept(visitor)
  }
}
