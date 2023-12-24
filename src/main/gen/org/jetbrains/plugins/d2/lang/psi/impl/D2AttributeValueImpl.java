// This is a generated file. Not intended for manual editing.
package org.jetbrains.plugins.d2.lang.psi.impl;

import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.plugins.d2.lang.D2PsiTreeUtil;
import org.jetbrains.plugins.d2.lang.D2CompositeElementImpl;
import org.jetbrains.plugins.d2.lang.psi.*;
import org.jetbrains.plugins.d2.lang.psi.D2AttributeValue;
import org.jetbrains.plugins.d2.lang.psi.D2Visitor;

public class D2AttributeValueImpl extends D2CompositeElementImpl implements D2AttributeValue {

  public D2AttributeValueImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull D2Visitor visitor) {
    visitor.visitAttributeValue(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof D2Visitor) accept((D2Visitor)visitor);
    else super.accept(visitor);
  }

}
