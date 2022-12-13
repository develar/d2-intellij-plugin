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

public class D2ConnectorImpl extends D2CompositeElementImpl implements D2Connector {

  public D2ConnectorImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull D2Visitor visitor) {
    visitor.visitConnector(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof D2Visitor) accept((D2Visitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiElement getArrow() {
    return findChildByType(ARROW);
  }

  @Override
  @Nullable
  public PsiElement getDoubleArrow() {
    return findChildByType(DOUBLE_ARROW);
  }

  @Override
  @Nullable
  public PsiElement getDoubleHyphenArrow() {
    return findChildByType(DOUBLE_HYPHEN_ARROW);
  }

  @Override
  @Nullable
  public PsiElement getReverseArrow() {
    return findChildByType(REVERSE_ARROW);
  }

}
