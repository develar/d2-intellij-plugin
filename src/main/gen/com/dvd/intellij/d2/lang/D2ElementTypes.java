// This is a generated file. Not intended for manual editing.
package com.dvd.intellij.d2.lang;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.dvd.intellij.d2.lang.psi.impl.*;

public interface D2ElementTypes {

  IElementType ATTRIBUTE_VALUE = new D2ElementType("ATTRIBUTE_VALUE");
  IElementType BLOCK_DEFINITION = new D2ElementType("BLOCK_DEFINITION");
  IElementType CONNECTOR = new D2ElementType("CONNECTOR");
  IElementType INLINE_SHAPE_DEFINITION = new D2ElementType("INLINE_SHAPE_DEFINITION");
  IElementType LABEL_DEFINITION = new D2ElementType("LABEL_DEFINITION");
  IElementType SHAPE_CONNECTION = new D2ElementType("SHAPE_CONNECTION");
  IElementType SHAPE_DEFINITION = new D2ElementType("SHAPE_DEFINITION");
  IElementType SHAPE_DEFINITIONS = new D2ElementType("SHAPE_DEFINITIONS");
  IElementType SUB_SHAPE_DEFINITION = new D2ElementType("SUB_SHAPE_DEFINITION");

  IElementType ARROW = new D2TokenType("ARROW");
  IElementType COLON = new D2TokenType(":");
  IElementType COMMENT = new D2TokenType("COMMENT");
  IElementType DOT = new D2TokenType(".");
  IElementType DOUBLE_ARROW = new D2TokenType("DOUBLE_ARROW");
  IElementType DOUBLE_HYPHEN_ARROW = new D2TokenType("DOUBLE_HYPHEN_ARROW");
  IElementType FALSE = new D2TokenType("false");
  IElementType FLOAT_LITERAL = new D2TokenType("FLOAT_LITERAL");
  IElementType IDENTIFIER = new D2TokenType("IDENTIFIER");
  IElementType LBRACE = new D2TokenType("{");
  IElementType NUMERIC_LITERAL = new D2TokenType("NUMERIC_LITERAL");
  IElementType RBRACE = new D2TokenType("}");
  IElementType REVERSE_ARROW = new D2TokenType("REVERSE_ARROW");
  IElementType SEMICOLON = new D2TokenType(";");
  IElementType STAR = new D2TokenType("*");
  IElementType STRING_LITERAL = new D2TokenType("STRING_LITERAL");
  IElementType TRUE = new D2TokenType("true");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ATTRIBUTE_VALUE) {
        return new D2AttributeValueImpl(node);
      }
      else if (type == BLOCK_DEFINITION) {
        return new D2BlockDefinitionImpl(node);
      }
      else if (type == CONNECTOR) {
        return new D2ConnectorImpl(node);
      }
      else if (type == INLINE_SHAPE_DEFINITION) {
        return new D2InlineShapeDefinitionImpl(node);
      }
      else if (type == LABEL_DEFINITION) {
        return new D2LabelDefinitionImpl(node);
      }
      else if (type == SHAPE_CONNECTION) {
        return new D2ShapeConnectionImpl(node);
      }
      else if (type == SHAPE_DEFINITION) {
        return new D2ShapeDefinitionImpl(node);
      }
      else if (type == SHAPE_DEFINITIONS) {
        return new D2ShapeDefinitionsImpl(node);
      }
      else if (type == SUB_SHAPE_DEFINITION) {
        return new D2SubShapeDefinitionImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
