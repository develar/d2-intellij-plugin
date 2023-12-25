// This is a generated file. Not intended for manual editing.
package org.jetbrains.plugins.d2.lang;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LightPsiParser;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;

import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import static org.jetbrains.plugins.d2.lang.D2ElementTypes.*;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class D2Parser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return File(b, l + 1);
  }

  /* ********************************************************** */
  // STRING | ID | INT | FLOAT | TRUE | FALSE | DOT
  static boolean AttributeValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "AttributeValue")) return false;
    boolean r;
    r = consumeToken(b, STRING);
    if (!r) r = consumeToken(b, ID);
    if (!r) r = consumeToken(b, INT);
    if (!r) r = consumeToken(b, FLOAT);
    if (!r) r = consumeToken(b, TRUE);
    if (!r) r = consumeToken(b, FALSE);
    if (!r) r = consumeToken(b, DOT);
    return r;
  }

  /* ********************************************************** */
  // LBRACE (Property | ShapeDefinitions)* RBRACE
  public static boolean BlockDefinition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BlockDefinition")) return false;
    if (!nextTokenIs(b, LBRACE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, BLOCK_DEFINITION, null);
    r = consumeToken(b, LBRACE);
    p = r; // pin = 1
    r = r && report_error_(b, BlockDefinition_1(b, l + 1));
    r = p && consumeToken(b, RBRACE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (Property | ShapeDefinitions)*
  private static boolean BlockDefinition_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BlockDefinition_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!BlockDefinition_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "BlockDefinition_1", c)) break;
    }
    return true;
  }

  // Property | ShapeDefinitions
  private static boolean BlockDefinition_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BlockDefinition_1_0")) return false;
    boolean r;
    r = Property(b, l + 1);
    if (!r) r = ShapeDefinitions(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // ARROW | REVERSE_ARROW | DOUBLE_ARROW | DOUBLE_HYPHEN_ARROW
  public static boolean Connector(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Connector")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CONNECTOR, "<connector>");
    r = consumeToken(b, ARROW);
    if (!r) r = consumeToken(b, REVERSE_ARROW);
    if (!r) r = consumeToken(b, DOUBLE_ARROW);
    if (!r) r = consumeToken(b, DOUBLE_HYPHEN_ARROW);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // !<<eof>> (Property | ShapeDefinitions | COMMENT)*
  static boolean File(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "File")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = File_0(b, l + 1);
    r = r && File_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // !<<eof>>
  private static boolean File_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "File_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !eof(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (Property | ShapeDefinitions | COMMENT)*
  private static boolean File_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "File_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!File_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "File_1", c)) break;
    }
    return true;
  }

  // Property | ShapeDefinitions | COMMENT
  private static boolean File_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "File_1_0")) return false;
    boolean r;
    r = Property(b, l + 1);
    if (!r) r = ShapeDefinitions(b, l + 1);
    if (!r) r = consumeToken(b, COMMENT);
    return r;
  }

  /* ********************************************************** */
  // (SEMICOLON ShapeDefinition)+
  public static boolean InlineShapeDefinition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "InlineShapeDefinition")) return false;
    if (!nextTokenIs(b, SEMICOLON)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _LEFT_, INLINE_SHAPE_DEFINITION, null);
    r = InlineShapeDefinition_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!InlineShapeDefinition_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "InlineShapeDefinition", c)) break;
    }
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // SEMICOLON ShapeDefinition
  private static boolean InlineShapeDefinition_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "InlineShapeDefinition_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SEMICOLON);
    r = r && ShapeDefinition(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // UNQUOTED_STRING | AttributeValue
  public static boolean LabelDefinition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "LabelDefinition")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, LABEL_DEFINITION, "<label definition>");
    r = consumeToken(b, UNQUOTED_STRING);
    if (!r) r = AttributeValue(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // (AttributeValue (DOT AttributeValue)*)? PropertyKey COLON PropertyValue
  public static boolean Property(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Property")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PROPERTY, "<property>");
    r = Property_0(b, l + 1);
    r = r && PropertyKey(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && PropertyValue(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (AttributeValue (DOT AttributeValue)*)?
  private static boolean Property_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Property_0")) return false;
    Property_0_0(b, l + 1);
    return true;
  }

  // AttributeValue (DOT AttributeValue)*
  private static boolean Property_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Property_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = AttributeValue(b, l + 1);
    r = r && Property_0_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (DOT AttributeValue)*
  private static boolean Property_0_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Property_0_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Property_0_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "Property_0_0_1", c)) break;
    }
    return true;
  }

  // DOT AttributeValue
  private static boolean Property_0_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Property_0_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DOT);
    r = r && AttributeValue(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // (SIMPLE_RESERVED_KEYWORDS | RESERVED_KEYWORD_HOLDERS) (DOT ID)*
  public static boolean PropertyKey(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PropertyKey")) return false;
    if (!nextTokenIs(b, "<property key>", RESERVED_KEYWORD_HOLDERS, SIMPLE_RESERVED_KEYWORDS)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PROPERTY_KEY, "<property key>");
    r = PropertyKey_0(b, l + 1);
    r = r && PropertyKey_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // SIMPLE_RESERVED_KEYWORDS | RESERVED_KEYWORD_HOLDERS
  private static boolean PropertyKey_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PropertyKey_0")) return false;
    boolean r;
    r = consumeToken(b, SIMPLE_RESERVED_KEYWORDS);
    if (!r) r = consumeToken(b, RESERVED_KEYWORD_HOLDERS);
    return r;
  }

  // (DOT ID)*
  private static boolean PropertyKey_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PropertyKey_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!PropertyKey_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "PropertyKey_1", c)) break;
    }
    return true;
  }

  // DOT ID
  private static boolean PropertyKey_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PropertyKey_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, DOT, ID);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // UNQUOTED_STRING | AttributeValue
  public static boolean PropertyValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PropertyValue")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PROPERTY_VALUE, "<property value>");
    r = consumeToken(b, UNQUOTED_STRING);
    if (!r) r = AttributeValue(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // (Connector ShapeDefinition)+
  public static boolean ShapeConnection(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeConnection")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _LEFT_, SHAPE_CONNECTION, "<shape connection>");
    r = ShapeConnection_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!ShapeConnection_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ShapeConnection", c)) break;
    }
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // Connector ShapeDefinition
  private static boolean ShapeConnection_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeConnection_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = Connector(b, l + 1);
    r = r && ShapeDefinition(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // AttributeValue
  public static boolean ShapeDefinition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeDefinition")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SHAPE_DEFINITION, "<shape definition>");
    r = AttributeValue(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // ShapeDefinition (ShapeConnection | SubShapeDefinition | InlineShapeDefinition)* ShapeExtras?
  static boolean ShapeDefinitions(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeDefinitions")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = ShapeDefinition(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, ShapeDefinitions_1(b, l + 1));
    r = p && ShapeDefinitions_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (ShapeConnection | SubShapeDefinition | InlineShapeDefinition)*
  private static boolean ShapeDefinitions_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeDefinitions_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ShapeDefinitions_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ShapeDefinitions_1", c)) break;
    }
    return true;
  }

  // ShapeConnection | SubShapeDefinition | InlineShapeDefinition
  private static boolean ShapeDefinitions_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeDefinitions_1_0")) return false;
    boolean r;
    r = ShapeConnection(b, l + 1);
    if (!r) r = SubShapeDefinition(b, l + 1);
    if (!r) r = InlineShapeDefinition(b, l + 1);
    return r;
  }

  // ShapeExtras?
  private static boolean ShapeDefinitions_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeDefinitions_2")) return false;
    ShapeExtras(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // COLON (LabelDefinition BlockDefinition? | BlockDefinition)
  static boolean ShapeExtras(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeExtras")) return false;
    if (!nextTokenIs(b, COLON)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COLON);
    p = r; // pin = 1
    r = r && ShapeExtras_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // LabelDefinition BlockDefinition? | BlockDefinition
  private static boolean ShapeExtras_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeExtras_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ShapeExtras_1_0(b, l + 1);
    if (!r) r = BlockDefinition(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // LabelDefinition BlockDefinition?
  private static boolean ShapeExtras_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeExtras_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = LabelDefinition(b, l + 1);
    r = r && ShapeExtras_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // BlockDefinition?
  private static boolean ShapeExtras_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeExtras_1_0_1")) return false;
    BlockDefinition(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // DOT ShapeDefinition ShapeExtras?
  public static boolean SubShapeDefinition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SubShapeDefinition")) return false;
    if (!nextTokenIs(b, DOT)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _LEFT_, SUB_SHAPE_DEFINITION, null);
    r = consumeToken(b, DOT);
    r = r && ShapeDefinition(b, l + 1);
    r = r && SubShapeDefinition_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ShapeExtras?
  private static boolean SubShapeDefinition_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SubShapeDefinition_2")) return false;
    ShapeExtras(b, l + 1);
    return true;
  }

}
