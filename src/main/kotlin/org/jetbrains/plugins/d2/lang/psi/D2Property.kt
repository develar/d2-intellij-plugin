// This is a generated file. Not intended for manual editing.
package org.jetbrains.plugins.d2.lang.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElementVisitor

class D2Property(node: ASTNode) : ASTWrapperPsiElement(node) {
  fun accept(visitor: D2Visitor) {
    visitor.visitProperty(this)
  }

  override fun accept(visitor: PsiElementVisitor) {
    if (visitor is D2Visitor) accept(visitor)
    else super.accept(visitor)
  }

  val propertyKey: D2PropertyKey
    get() = findNotNullChildByClass(D2PropertyKey::class.java)

  val propertyValue: D2PropertyValue
    get() = findNotNullChildByClass(D2PropertyValue::class.java)
}
