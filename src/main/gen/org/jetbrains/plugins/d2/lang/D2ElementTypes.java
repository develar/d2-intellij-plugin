// This is a generated file. Not intended for manual editing.
package org.jetbrains.plugins.d2.lang;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.plugins.d2.lang.psi.impl.*;

public interface D2ElementTypes {

  IElementType BLOCK_DEFINITION = new D2ElementType("BLOCK_DEFINITION");
  IElementType CONNECTOR = new D2ElementType("CONNECTOR");
  IElementType INLINE_SHAPE_DEFINITION = new D2ElementType("INLINE_SHAPE_DEFINITION");
  IElementType LABEL_DEFINITION = new D2ElementType("LABEL_DEFINITION");
  IElementType PROPERTY = new D2ElementType("PROPERTY");
  IElementType PROPERTY_KEY = new D2ElementType("PROPERTY_KEY");
  IElementType PROPERTY_VALUE = new D2ElementType("PROPERTY_VALUE");
  IElementType SHAPE_CONNECTION = new D2ElementType("SHAPE_CONNECTION");
  IElementType SHAPE_DEFINITION = new D2ElementType("SHAPE_DEFINITION");
  IElementType SUB_SHAPE_DEFINITION = new D2ElementType("SUB_SHAPE_DEFINITION");

  IElementType ARROW = new D2TokenType("ARROW");
  IElementType COLON = new D2TokenType("COLON");
  IElementType COMMENT = new D2TokenType("COMMENT");
  IElementType DOT = new D2TokenType("DOT");
  IElementType DOUBLE_ARROW = new D2TokenType("DOUBLE_ARROW");
  IElementType DOUBLE_HYPHEN_ARROW = new D2TokenType("DOUBLE_HYPHEN_ARROW");
  IElementType FALSE = new D2TokenType("FALSE");
  IElementType FLOAT = new D2TokenType("FLOAT");
  IElementType ID = new D2TokenType("ID");
  IElementType INT = new D2TokenType("INT");
  IElementType LBRACE = new D2TokenType("LBRACE");
  IElementType RBRACE = new D2TokenType("RBRACE");
  IElementType RESERVED_KEYWORD_HOLDERS = new D2TokenType("RESERVED_KEYWORD_HOLDERS");
  IElementType REVERSE_ARROW = new D2TokenType("REVERSE_ARROW");
  IElementType SEMICOLON = new D2TokenType("SEMICOLON");
  IElementType SIMPLE_RESERVED_KEYWORDS = new D2TokenType("SIMPLE_RESERVED_KEYWORDS");
  IElementType STRING = new D2TokenType("STRING");
  IElementType TRUE = new D2TokenType("TRUE");
  IElementType UNQUOTED_STRING = new D2TokenType("UNQUOTED_STRING");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == BLOCK_DEFINITION) {
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
      else if (type == PROPERTY) {
        return new D2PropertyImpl(node);
      }
      else if (type == PROPERTY_KEY) {
        return new D2PropertyKeyImpl(node);
      }
      else if (type == PROPERTY_VALUE) {
        return new D2PropertyValueImpl(node);
      }
      else if (type == SHAPE_CONNECTION) {
        return new D2ShapeConnectionImpl(node);
      }
      else if (type == SHAPE_DEFINITION) {
        return new D2ShapeDefinitionImpl(node);
      }
      else if (type == SUB_SHAPE_DEFINITION) {
        return new D2SubShapeDefinitionImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
