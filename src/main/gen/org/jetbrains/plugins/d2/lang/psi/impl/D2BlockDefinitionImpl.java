// This is a generated file. Not intended for manual editing.
package org.jetbrains.plugins.d2.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.d2.lang.D2CompositeElementImpl;
import org.jetbrains.plugins.d2.lang.D2PsiTreeUtil;
import org.jetbrains.plugins.d2.lang.psi.*;

import java.util.List;

public class D2BlockDefinitionImpl extends D2CompositeElementImpl implements D2BlockDefinition {

  public D2BlockDefinitionImpl(@NotNull ASTNode node) {
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
  public List<D2BlockDefinition> getBlockDefinitionList() {
    return D2PsiTreeUtil.getChildrenOfTypeAsList(this, D2BlockDefinition.class);
  }

  @Override
  @NotNull
  public List<D2BlockString> getBlockStringList() {
    return D2PsiTreeUtil.getChildrenOfTypeAsList(this, D2BlockString.class);
  }

  @Override
  @NotNull
  public List<D2InlineShapeDefinition> getInlineShapeDefinitionList() {
    return D2PsiTreeUtil.getChildrenOfTypeAsList(this, D2InlineShapeDefinition.class);
  }

  @Override
  @NotNull
  public List<D2LabelDefinition> getLabelDefinitionList() {
    return D2PsiTreeUtil.getChildrenOfTypeAsList(this, D2LabelDefinition.class);
  }

  @Override
  @NotNull
  public List<D2Property> getPropertyList() {
    return D2PsiTreeUtil.getChildrenOfTypeAsList(this, D2Property.class);
  }

  @Override
  @NotNull
  public List<D2ShapeConnection> getShapeConnectionList() {
    return D2PsiTreeUtil.getChildrenOfTypeAsList(this, D2ShapeConnection.class);
  }

  @Override
  @NotNull
  public List<D2ShapeDefinition> getShapeDefinitionList() {
    return D2PsiTreeUtil.getChildrenOfTypeAsList(this, D2ShapeDefinition.class);
  }

}
