// This is a generated file. Not intended for manual editing.
package org.jetbrains.plugins.d2.lang.psi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.d2.lang.D2CompositeElement;

import java.util.List;

public interface D2BlockDefinition extends D2CompositeElement {

  @NotNull
  List<D2BlockDefinition> getBlockDefinitionList();

  @NotNull
  List<D2InlineShapeDefinition> getInlineShapeDefinitionList();

  @NotNull
  List<D2LabelDefinition> getLabelDefinitionList();

  @NotNull
  List<D2Property> getPropertyList();

  @NotNull
  List<D2ShapeConnection> getShapeConnectionList();

  @NotNull
  List<D2ShapeDefinition> getShapeDefinitionList();

  @NotNull
  List<D2SubShapeDefinition> getSubShapeDefinitionList();

}
