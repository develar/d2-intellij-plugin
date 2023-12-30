// This is a generated file. Not intended for manual editing.
package org.jetbrains.plugins.d2.lang;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static org.jetbrains.plugins.d2.lang.D2ElementTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

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
  // STRING | ID | INT | FLOAT | TRUE | FALSE | DOT | COLOR
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
    if (!r) r = consumeToken(b, COLOR);
    return r;
  }

  /* ********************************************************** */
  // LBRACE (ShapeIdWithPropery | ShapeDefinitions)* RBRACE
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

  // (ShapeIdWithPropery | ShapeDefinitions)*
  private static boolean BlockDefinition_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BlockDefinition_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!BlockDefinition_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "BlockDefinition_1", c)) break;
    }
    return true;
  }

  // ShapeIdWithPropery | ShapeDefinitions
  private static boolean BlockDefinition_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BlockDefinition_1_0")) return false;
    boolean r;
    r = ShapeIdWithPropery(b, l + 1);
    if (!r) r = ShapeDefinitions(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // BLOCK_STRING_OPEN BLOCK_STRING_LANG? BLOCK_STRING_BODY BLOCK_STRING_CLOSE
  public static boolean BlockString(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BlockString")) return false;
    if (!nextTokenIs(b, BLOCK_STRING_OPEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BLOCK_STRING_OPEN);
    r = r && BlockString_1(b, l + 1);
    r = r && consumeTokens(b, 0, BLOCK_STRING_BODY, BLOCK_STRING_CLOSE);
    exit_section_(b, m, BLOCK_STRING, r);
    return r;
  }

  // BLOCK_STRING_LANG?
  private static boolean BlockString_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BlockString_1")) return false;
    consumeToken(b, BLOCK_STRING_LANG);
    return true;
  }

  /* ********************************************************** */
  // COLOR
  public static boolean ColorValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ColorValue")) return false;
    if (!nextTokenIs(b, COLOR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLOR);
    exit_section_(b, m, COLOR_VALUE, r);
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
  // !<<eof>> (ShapeDeclaration | ShapeIdWithPropery | ShapeDefinitions | COMMENT | SEMICOLON)*
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

  // (ShapeDeclaration | ShapeIdWithPropery | ShapeDefinitions | COMMENT | SEMICOLON)*
  private static boolean File_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "File_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!File_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "File_1", c)) break;
    }
    return true;
  }

  // ShapeDeclaration | ShapeIdWithPropery | ShapeDefinitions | COMMENT | SEMICOLON
  private static boolean File_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "File_1_0")) return false;
    boolean r;
    r = ShapeDeclaration(b, l + 1);
    if (!r) r = ShapeIdWithPropery(b, l + 1);
    if (!r) r = ShapeDefinitions(b, l + 1);
    if (!r) r = consumeToken(b, COMMENT);
    if (!r) r = consumeToken(b, SEMICOLON);
    return r;
  }

  /* ********************************************************** */
  // AttributeValue | BlockString
  public static boolean OtherValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "OtherValue")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, OTHER_VALUE, "<other value>");
    r = AttributeValue(b, l + 1);
    if (!r) r = BlockString(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // PropertyKey COLON PropertyValue
  public static boolean Property(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Property")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PROPERTY, "<property>");
    r = PropertyKey(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && PropertyValue(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // SIMPLE_RESERVED_KEYWORDS | (RESERVED_KEYWORD_HOLDERS DOT ID) | (STYLE_KEYWORD DOT STYLE_KEYWORDS)
  public static boolean PropertyKey(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PropertyKey")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PROPERTY_KEY, "<property key>");
    r = consumeToken(b, SIMPLE_RESERVED_KEYWORDS);
    if (!r) r = PropertyKey_1(b, l + 1);
    if (!r) r = PropertyKey_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // RESERVED_KEYWORD_HOLDERS DOT ID
  private static boolean PropertyKey_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PropertyKey_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, RESERVED_KEYWORD_HOLDERS, DOT, ID);
    exit_section_(b, m, null, r);
    return r;
  }

  // STYLE_KEYWORD DOT STYLE_KEYWORDS
  private static boolean PropertyKey_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PropertyKey_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, STYLE_KEYWORD, DOT, STYLE_KEYWORDS);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // UnquotedStringValue | StringValue | ColorValue | OtherValue
  static boolean PropertyValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PropertyValue")) return false;
    boolean r;
    r = UnquotedStringValue(b, l + 1);
    if (!r) r = StringValue(b, l + 1);
    if (!r) r = ColorValue(b, l + 1);
    if (!r) r = OtherValue(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // (Connector ShapeId)+
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

  // Connector ShapeId
  private static boolean ShapeConnection_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeConnection_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = Connector(b, l + 1);
    r = r && ShapeId(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ShapeId (DOT ShapeId)* COLON (ShapeLabel | BlockString)? BlockDefinition?
  public static boolean ShapeDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeDeclaration")) return false;
    if (!nextTokenIs(b, "<shape declaration>", ID, STRING)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SHAPE_DECLARATION, "<shape declaration>");
    r = ShapeId(b, l + 1);
    r = r && ShapeDeclaration_1(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && ShapeDeclaration_3(b, l + 1);
    r = r && ShapeDeclaration_4(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (DOT ShapeId)*
  private static boolean ShapeDeclaration_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeDeclaration_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ShapeDeclaration_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ShapeDeclaration_1", c)) break;
    }
    return true;
  }

  // DOT ShapeId
  private static boolean ShapeDeclaration_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeDeclaration_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DOT);
    r = r && ShapeId(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (ShapeLabel | BlockString)?
  private static boolean ShapeDeclaration_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeDeclaration_3")) return false;
    ShapeDeclaration_3_0(b, l + 1);
    return true;
  }

  // ShapeLabel | BlockString
  private static boolean ShapeDeclaration_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeDeclaration_3_0")) return false;
    boolean r;
    r = ShapeLabel(b, l + 1);
    if (!r) r = BlockString(b, l + 1);
    return r;
  }

  // BlockDefinition?
  private static boolean ShapeDeclaration_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeDeclaration_4")) return false;
    BlockDefinition(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // ShapeId (ShapeConnection | SubShapeDefinition)* ShapeExtras?
  static boolean ShapeDefinitions(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeDefinitions")) return false;
    if (!nextTokenIs(b, "", ID, STRING)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = ShapeId(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, ShapeDefinitions_1(b, l + 1));
    r = p && ShapeDefinitions_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (ShapeConnection | SubShapeDefinition)*
  private static boolean ShapeDefinitions_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeDefinitions_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ShapeDefinitions_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ShapeDefinitions_1", c)) break;
    }
    return true;
  }

  // ShapeConnection | SubShapeDefinition
  private static boolean ShapeDefinitions_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeDefinitions_1_0")) return false;
    boolean r;
    r = ShapeConnection(b, l + 1);
    if (!r) r = SubShapeDefinition(b, l + 1);
    return r;
  }

  // ShapeExtras?
  private static boolean ShapeDefinitions_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeDefinitions_2")) return false;
    ShapeExtras(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // COLON ((ShapeLabel | BlockString) BlockDefinition? | BlockDefinition)
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

  // (ShapeLabel | BlockString) BlockDefinition? | BlockDefinition
  private static boolean ShapeExtras_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeExtras_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ShapeExtras_1_0(b, l + 1);
    if (!r) r = BlockDefinition(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (ShapeLabel | BlockString) BlockDefinition?
  private static boolean ShapeExtras_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeExtras_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ShapeExtras_1_0_0(b, l + 1);
    r = r && ShapeExtras_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ShapeLabel | BlockString
  private static boolean ShapeExtras_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeExtras_1_0_0")) return false;
    boolean r;
    r = ShapeLabel(b, l + 1);
    if (!r) r = BlockString(b, l + 1);
    return r;
  }

  // BlockDefinition?
  private static boolean ShapeExtras_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeExtras_1_0_1")) return false;
    BlockDefinition(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // ID | STRING
  public static boolean ShapeId(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeId")) return false;
    if (!nextTokenIs(b, "<shape id>", ID, STRING)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SHAPE_ID, "<shape id>");
    r = consumeToken(b, ID);
    if (!r) r = consumeToken(b, STRING);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // (ShapeId (DOT ShapeId)* DOT)? Property
  static boolean ShapeIdWithPropery(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeIdWithPropery")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ShapeIdWithPropery_0(b, l + 1);
    r = r && Property(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (ShapeId (DOT ShapeId)* DOT)?
  private static boolean ShapeIdWithPropery_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeIdWithPropery_0")) return false;
    ShapeIdWithPropery_0_0(b, l + 1);
    return true;
  }

  // ShapeId (DOT ShapeId)* DOT
  private static boolean ShapeIdWithPropery_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeIdWithPropery_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ShapeId(b, l + 1);
    r = r && ShapeIdWithPropery_0_0_1(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  // (DOT ShapeId)*
  private static boolean ShapeIdWithPropery_0_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeIdWithPropery_0_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ShapeIdWithPropery_0_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ShapeIdWithPropery_0_0_1", c)) break;
    }
    return true;
  }

  // DOT ShapeId
  private static boolean ShapeIdWithPropery_0_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeIdWithPropery_0_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DOT);
    r = r && ShapeId(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // UNQUOTED_STRING | STRING
  public static boolean ShapeLabel(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeLabel")) return false;
    if (!nextTokenIs(b, "<shape label>", STRING, UNQUOTED_STRING)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SHAPE_LABEL, "<shape label>");
    r = consumeToken(b, UNQUOTED_STRING);
    if (!r) r = consumeToken(b, STRING);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // STRING
  public static boolean StringValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "StringValue")) return false;
    if (!nextTokenIs(b, STRING)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, STRING);
    exit_section_(b, m, STRING_VALUE, r);
    return r;
  }

  /* ********************************************************** */
  // DOT ShapeId ShapeExtras?
  static boolean SubShapeDefinition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SubShapeDefinition")) return false;
    if (!nextTokenIs(b, DOT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DOT);
    r = r && ShapeId(b, l + 1);
    r = r && SubShapeDefinition_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ShapeExtras?
  private static boolean SubShapeDefinition_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SubShapeDefinition_2")) return false;
    ShapeExtras(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // UNQUOTED_STRING
  public static boolean UnquotedStringValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "UnquotedStringValue")) return false;
    if (!nextTokenIs(b, UNQUOTED_STRING)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, UNQUOTED_STRING);
    exit_section_(b, m, UNQUOTED_STRING_VALUE, r);
    return r;
  }

}
