// This is a generated file. Not intended for manual editing.
package org.jetbrains.plugins.d2.lang.psi.impl;

import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.dvd.intellij.d2.lang.D2PsiTreeUtil;
import com.dvd.intellij.d2.lang.D2CompositeElementImpl;
import com.dvd.intellij.d2.lang.psi.*;
import org.jetbrains.plugins.d2.lang.psi.D2AttributeValue;
import org.jetbrains.plugins.d2.lang.psi.D2LabelDefinition;
import org.jetbrains.plugins.d2.lang.psi.D2Visitor;

public class D2LabelDefinitionImpl extends D2CompositeElementImpl implements D2LabelDefinition {

  public D2LabelDefinitionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull D2Visitor visitor) {
    visitor.visitLabelDefinition(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof D2Visitor) accept((D2Visitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public D2AttributeValue getAttributeValue() {
    return findChildByClass(D2AttributeValue.class);
  }

}
