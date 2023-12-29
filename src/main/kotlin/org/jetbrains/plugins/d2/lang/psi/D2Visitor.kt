// This is a generated file. Not intended for manual editing.
package org.jetbrains.plugins.d2.lang.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor

class D2Visitor : PsiElementVisitor() {
  fun visitBlockDefinition(o: D2BlockDefinition) {
    visitCompositeElement(o)
  }

  fun visitBlockString(o: D2BlockString) {
    visitCompositeElement(o)
  }

  fun visitConnector(o: D2Connector) {
    visitCompositeElement(o)
  }

  fun visitProperty(o: D2Property) {
    visitCompositeElement(o)
  }

  fun visitPropertyKey(o: D2PropertyKey) {
    visitCompositeElement(o)
  }

  fun visitPropertyValue(o: D2PropertyValue) {
    visitCompositeElement(o)
  }


  fun visitCompositeElement(o: PsiElement) {
    visitElement(o)
  }
}
