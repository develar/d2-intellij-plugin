// This is a generated file. Not intended for manual editing.
package com.dvd.intellij.d2.lang.psi.impl;

import com.dvd.intellij.d2.lang.D2CompositeElementImpl;
import com.dvd.intellij.d2.lang.D2PsiTreeUtil;
import com.dvd.intellij.d2.lang.psi.D2BlockDefinition;
import com.dvd.intellij.d2.lang.psi.D2ShapeDefinitions;
import com.dvd.intellij.d2.lang.psi.D2Visitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class D2BlockDefinitionImpl extends D2CompositeElementImpl implements D2BlockDefinition {

  public D2BlockDefinitionImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull D2Visitor visitor) {
    visitor.visitBlockDefinition(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof D2Visitor) accept((D2Visitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<D2ShapeDefinitions> getShapeDefinitionsList() {
    return D2PsiTreeUtil.getChildrenOfTypeAsList(this, D2ShapeDefinitions.class);
  }

}
