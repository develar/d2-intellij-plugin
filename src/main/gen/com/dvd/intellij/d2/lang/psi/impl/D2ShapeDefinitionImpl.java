// This is a generated file. Not intended for manual editing.
package com.dvd.intellij.d2.lang.psi.impl;

import com.dvd.intellij.d2.lang.D2CompositeElementImpl;
import com.dvd.intellij.d2.lang.psi.D2AttributeValue;
import com.dvd.intellij.d2.lang.psi.D2ShapeDefinition;
import com.dvd.intellij.d2.lang.psi.D2Visitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

public class D2ShapeDefinitionImpl extends D2CompositeElementImpl implements D2ShapeDefinition {

  public D2ShapeDefinitionImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull D2Visitor visitor) {
    visitor.visitShapeDefinition(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof D2Visitor) accept((D2Visitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public D2AttributeValue getAttributeValue() {
    return findNotNullChildByClass(D2AttributeValue.class);
  }

}
