// This is a generated file. Not intended for manual editing.
package com.dvd.intellij.d2.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.dvd.intellij.d2.lang.D2PsiTreeUtil;
import static com.dvd.intellij.d2.lang.D2ElementTypes.*;
import com.dvd.intellij.d2.lang.D2CompositeElementImpl;
import com.dvd.intellij.d2.lang.psi.*;

public class D2ShapeDefinitionsImpl extends D2CompositeElementImpl implements D2ShapeDefinitions {

  public D2ShapeDefinitionsImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull D2Visitor visitor) {
    visitor.visitShapeDefinitions(this);
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
  @Nullable
  public D2ShapeDefinition getShapeDefinition() {
    return findChildByClass(D2ShapeDefinition.class);
  }

  @Override
  @Nullable
  public D2SubShapeDefinition getSubShapeDefinition() {
    return findChildByClass(D2SubShapeDefinition.class);
  }

}
