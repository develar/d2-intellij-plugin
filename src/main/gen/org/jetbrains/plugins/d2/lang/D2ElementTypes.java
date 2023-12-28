// This is a generated file. Not intended for manual editing.
package org.jetbrains.plugins.d2.lang;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.plugins.d2.lang.psi.impl.*;

public interface D2ElementTypes {

  IElementType BLOCK_DEFINITION = D2ElementTypeFactory.element("BLOCK_DEFINITION");
  IElementType BLOCK_STRING = D2ElementTypeFactory.element("BLOCK_STRING");
  IElementType CONNECTOR = D2ElementTypeFactory.element("CONNECTOR");
  IElementType INLINE_SHAPE_DEFINITION = D2ElementTypeFactory.element("INLINE_SHAPE_DEFINITION");
  IElementType LABEL_DEFINITION = D2ElementTypeFactory.element("LABEL_DEFINITION");
  IElementType PROPERTY = D2ElementTypeFactory.element("PROPERTY");
  IElementType PROPERTY_KEY = D2ElementTypeFactory.element("PROPERTY_KEY");
  IElementType PROPERTY_VALUE = D2ElementTypeFactory.element("PROPERTY_VALUE");
  IElementType SHAPE_CONNECTION = D2ElementTypeFactory.element("SHAPE_CONNECTION");
  IElementType SHAPE_DEFINITION = D2ElementTypeFactory.element("SHAPE_DEFINITION");

  IElementType ARROW = D2ElementTypeFactory.token("ARROW");
  IElementType BLOCK_STRING_BODY = D2ElementTypeFactory.token("BLOCK_STRING_BODY");
  IElementType BLOCK_STRING_CLOSE = D2ElementTypeFactory.token("BLOCK_STRING_CLOSE");
  IElementType BLOCK_STRING_LANG = D2ElementTypeFactory.token("BLOCK_STRING_LANG");
  IElementType BLOCK_STRING_OPEN = D2ElementTypeFactory.token("BLOCK_STRING_OPEN");
  IElementType COLON = D2ElementTypeFactory.token("COLON");
  IElementType COMMENT = D2ElementTypeFactory.token("COMMENT");
  IElementType DOT = D2ElementTypeFactory.token("DOT");
  IElementType DOUBLE_ARROW = D2ElementTypeFactory.token("DOUBLE_ARROW");
  IElementType DOUBLE_HYPHEN_ARROW = D2ElementTypeFactory.token("DOUBLE_HYPHEN_ARROW");
  IElementType FALSE = D2ElementTypeFactory.token("FALSE");
  IElementType FLOAT = D2ElementTypeFactory.token("FLOAT");
  IElementType ID = D2ElementTypeFactory.token("ID");
  IElementType INT = D2ElementTypeFactory.token("INT");
  IElementType LBRACE = D2ElementTypeFactory.token("LBRACE");
  IElementType RBRACE = D2ElementTypeFactory.token("RBRACE");
  IElementType RESERVED_KEYWORD_HOLDERS = D2ElementTypeFactory.token("RESERVED_KEYWORD_HOLDERS");
  IElementType REVERSE_ARROW = D2ElementTypeFactory.token("REVERSE_ARROW");
  IElementType SEMICOLON = D2ElementTypeFactory.token("SEMICOLON");
  IElementType SIMPLE_RESERVED_KEYWORDS = D2ElementTypeFactory.token("SIMPLE_RESERVED_KEYWORDS");
  IElementType STRING = D2ElementTypeFactory.token("STRING");
  IElementType TRUE = D2ElementTypeFactory.token("TRUE");
  IElementType UNQUOTED_STRING = D2ElementTypeFactory.token("UNQUOTED_STRING");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == BLOCK_DEFINITION) {
        return new D2BlockDefinitionImpl(node);
      }
      else if (type == BLOCK_STRING) {
        return new D2BlockStringImpl(node);
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
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
