// This is a generated file. Not intended for manual editing.
package org.jetbrains.plugins.d2.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.d2.lang.D2CompositeElementImpl;
import org.jetbrains.plugins.d2.lang.D2PsiTreeUtil;
import org.jetbrains.plugins.d2.lang.psi.*;

import java.util.List;

public class D2InlineShapeDefinitionImpl extends D2CompositeElementImpl implements D2InlineShapeDefinition {

  public D2InlineShapeDefinitionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull D2Visitor visitor) {
    visitor.visitInlineShapeDefinition(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof D2Visitor) accept((D2Visitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public D2BlockDefinition getBlockDefinition() {
    return findChildByClass(D2BlockDefinition.class);
  }

  @Override
  @Nullable
  public D2BlockString getBlockString() {
    return findChildByClass(D2BlockString.class);
  }

  @Override
  @Nullable
  public D2InlineShapeDefinition getInlineShapeDefinition() {
    return findChildByClass(D2InlineShapeDefinition.class);
  }

  @Override
  @Nullable
  public D2LabelDefinition getLabelDefinition() {
    return findChildByClass(D2LabelDefinition.class);
  }

  @Override
  @Nullable
  public D2ShapeConnection getShapeConnection() {
    return findChildByClass(D2ShapeConnection.class);
  }

  @Override
  @NotNull
  public List<D2ShapeDefinition> getShapeDefinitionList() {
    return D2PsiTreeUtil.getChildrenOfTypeAsList(this, D2ShapeDefinition.class);
  }

}
