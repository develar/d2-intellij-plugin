// This is a generated file. Not intended for manual editing.
package org.jetbrains.plugins.d2.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.plugins.d2.lang.D2PsiTreeUtil;
import static org.jetbrains.plugins.d2.lang.D2ElementTypes.*;
import org.jetbrains.plugins.d2.lang.D2CompositeElementImpl;
import org.jetbrains.plugins.d2.lang.psi.*;

public class D2ShapeConnectionImpl extends D2CompositeElementImpl implements D2ShapeConnection {

  public D2ShapeConnectionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull D2Visitor visitor) {
    visitor.visitShapeConnection(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof D2Visitor) accept((D2Visitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<D2Connector> getConnectorList() {
    return D2PsiTreeUtil.getChildrenOfTypeAsList(this, D2Connector.class);
  }

  @Override
  @Nullable
  public D2InlineShapeDefinition getInlineShapeDefinition() {
    return findChildByClass(D2InlineShapeDefinition.class);
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

  @Override
  @Nullable
  public D2SubShapeDefinition getSubShapeDefinition() {
    return findChildByClass(D2SubShapeDefinition.class);
  }

}
