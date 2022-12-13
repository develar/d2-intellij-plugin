// This is a generated file. Not intended for manual editing.
package com.dvd.intellij.d2.lang.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.dvd.intellij.d2.lang.D2CompositeElement;

public class D2Visitor extends PsiElementVisitor {

  public void visitAttributeValue(@NotNull D2AttributeValue o) {
    visitCompositeElement(o);
  }

  public void visitBlockDefinition(@NotNull D2BlockDefinition o) {
    visitCompositeElement(o);
  }

  public void visitConnector(@NotNull D2Connector o) {
    visitCompositeElement(o);
  }

  public void visitInlineShapeDefinition(@NotNull D2InlineShapeDefinition o) {
    visitCompositeElement(o);
  }

  public void visitLabelDefinition(@NotNull D2LabelDefinition o) {
    visitCompositeElement(o);
  }

  public void visitShapeConnection(@NotNull D2ShapeConnection o) {
    visitCompositeElement(o);
  }

  public void visitShapeDefinition(@NotNull D2ShapeDefinition o) {
    visitCompositeElement(o);
  }

  public void visitShapeDefinitions(@NotNull D2ShapeDefinitions o) {
    visitCompositeElement(o);
  }

  public void visitSubShapeDefinition(@NotNull D2SubShapeDefinition o) {
    visitCompositeElement(o);
  }

  public void visitCompositeElement(@NotNull D2CompositeElement o) {
    visitElement(o);
  }

}
