// This is a generated file. Not intended for manual editing.
package org.jetbrains.plugins.d2.lang.psi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.d2.lang.D2CompositeElement;

import java.util.List;

public interface D2InlineShapeDefinition extends D2CompositeElement {

  @Nullable
  D2BlockDefinition getBlockDefinition();

  @Nullable
  D2InlineShapeDefinition getInlineShapeDefinition();

  @Nullable
  D2LabelDefinition getLabelDefinition();

  @Nullable
  D2ShapeConnection getShapeConnection();

  @NotNull
  List<D2ShapeDefinition> getShapeDefinitionList();

}
