// This is a generated file. Not intended for manual editing.
package com.dvd.intellij.d2.lang.psi;

import com.dvd.intellij.d2.lang.D2CompositeElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface D2InlineShapeDefinition extends D2CompositeElement {

  @Nullable
  D2InlineShapeDefinition getInlineShapeDefinition();

  @Nullable
  D2ShapeConnection getShapeConnection();

  @NotNull
  List<D2ShapeDefinition> getShapeDefinitionList();

  @Nullable
  D2SubShapeDefinition getSubShapeDefinition();

}
