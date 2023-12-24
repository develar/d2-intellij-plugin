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
  public List<D2Property> getPropertyList() {
    return D2PsiTreeUtil.getChildrenOfTypeAsList(this, D2Property.class);
  }

  @Override
  @NotNull
  public List<D2ShapeDefinitions> getShapeDefinitionsList() {
    return D2PsiTreeUtil.getChildrenOfTypeAsList(this, D2ShapeDefinitions.class);
  }

}
