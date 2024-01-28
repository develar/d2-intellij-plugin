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
  // LBRACKET (ShapePropertyValue SEMICOLON?)* RBRACKET
  public static boolean Array(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Array")) return false;
    if (!nextTokenIs(b, LBRACKET)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACKET);
    r = r && Array_1(b, l + 1);
    r = r && consumeToken(b, RBRACKET);
    exit_section_(b, m, ARRAY, r);
    return r;
  }

  // (ShapePropertyValue SEMICOLON?)*
  private static boolean Array_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Array_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!Array_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "Array_1", c)) break;
    }
    return true;
  }

  // ShapePropertyValue SEMICOLON?
  private static boolean Array_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Array_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ShapePropertyValue(b, l + 1);
    r = r && Array_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // SEMICOLON?
  private static boolean Array_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "Array_1_0_1")) return false;
    consumeToken(b, SEMICOLON);
    return true;
  }

  /* ********************************************************** */
  // LBRACE ContainerContent* RBRACE
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

  // ContainerContent*
  private static boolean BlockDefinition_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "BlockDefinition_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ContainerContent(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "BlockDefinition_1", c)) break;
    }
    return true;
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
  // ShapeDeclaration | ShapeIdWithProperty | ShapeConnection | ShapeId | IdPropertyMap | COMMENT | SEMICOLON
  static boolean ContainerContent(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ContainerContent")) return false;
    boolean r;
    r = ShapeDeclaration(b, l + 1);
    if (!r) r = ShapeIdWithProperty(b, l + 1);
    if (!r) r = ShapeConnection(b, l + 1);
    if (!r) r = ShapeId(b, l + 1);
    if (!r) r = IdPropertyMap(b, l + 1);
    if (!r) r = consumeToken(b, COMMENT);
    if (!r) r = consumeToken(b, SEMICOLON);
    return r;
  }

  /* ********************************************************** */
  // !<<eof>> ContainerContent*
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

  // ContainerContent*
  private static boolean File_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "File_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ContainerContent(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "File_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // IdPropertyKey COLON IdPropertyValue
  public static boolean IdProperty(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IdProperty")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ID_PROPERTY, "<id property>");
    r = IdPropertyKey(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && IdPropertyValue(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // PropertyKey | ID | STRING
  public static boolean IdPropertyKey(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IdPropertyKey")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ID_PROPERTY_KEY, "<id property key>");
    r = PropertyKey(b, l + 1);
    if (!r) r = consumeToken(b, ID);
    if (!r) r = consumeToken(b, STRING);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // (COMPOSITE_RESERVED_KEYWORDS | STYLE_KEYWORD) COLON LBRACE PropertyMapContent* RBRACE
  public static boolean IdPropertyMap(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IdPropertyMap")) return false;
    if (!nextTokenIs(b, "<id property map>", COMPOSITE_RESERVED_KEYWORDS, STYLE_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ID_PROPERTY_MAP, "<id property map>");
    r = IdPropertyMap_0(b, l + 1);
    r = r && consumeTokens(b, 0, COLON, LBRACE);
    r = r && IdPropertyMap_3(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // COMPOSITE_RESERVED_KEYWORDS | STYLE_KEYWORD
  private static boolean IdPropertyMap_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IdPropertyMap_0")) return false;
    boolean r;
    r = consumeToken(b, COMPOSITE_RESERVED_KEYWORDS);
    if (!r) r = consumeToken(b, STYLE_KEYWORD);
    return r;
  }

  // PropertyMapContent*
  private static boolean IdPropertyMap_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IdPropertyMap_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!PropertyMapContent(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "IdPropertyMap_3", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // SubIdPropertyMap | UnquotedStringValue | StringValue | ColorValue | OtherValue | Array
  static boolean IdPropertyValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "IdPropertyValue")) return false;
    boolean r;
    r = SubIdPropertyMap(b, l + 1);
    if (!r) r = UnquotedStringValue(b, l + 1);
    if (!r) r = StringValue(b, l + 1);
    if (!r) r = ColorValue(b, l + 1);
    if (!r) r = OtherValue(b, l + 1);
    if (!r) r = Array(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // STRING | ID | INT | FLOAT | TRUE | FALSE | DOT | COLOR | BlockString
  public static boolean OtherValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "OtherValue")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, OTHER_VALUE, "<other value>");
    r = consumeToken(b, STRING);
    if (!r) r = consumeToken(b, ID);
    if (!r) r = consumeToken(b, INT);
    if (!r) r = consumeToken(b, FLOAT);
    if (!r) r = consumeToken(b, TRUE);
    if (!r) r = consumeToken(b, FALSE);
    if (!r) r = consumeToken(b, DOT);
    if (!r) r = consumeToken(b, COLOR);
    if (!r) r = BlockString(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // PARENT_SHAPE_REF
  public static boolean ParentShapeRefPsi(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ParentShapeRefPsi")) return false;
    if (!nextTokenIs(b, PARENT_SHAPE_REF)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PARENT_SHAPE_REF);
    exit_section_(b, m, PARENT_SHAPE_REF_PSI, r);
    return r;
  }

  /* ********************************************************** */
  // SIMPLE_RESERVED_KEYWORDS | (RESERVED_KEYWORD_HOLDERS DOT ID) | (STYLE_KEYWORD DOT STYLE_KEYWORDS) | CONTAINER_LESS_KEYWORDS | STYLE_KEYWORDS
  static boolean PropertyKey(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PropertyKey")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SIMPLE_RESERVED_KEYWORDS);
    if (!r) r = PropertyKey_1(b, l + 1);
    if (!r) r = PropertyKey_2(b, l + 1);
    if (!r) r = consumeToken(b, CONTAINER_LESS_KEYWORDS);
    if (!r) r = consumeToken(b, STYLE_KEYWORDS);
    exit_section_(b, m, null, r);
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
  // SubIdPropertyMap | IdProperty | COMMENT | SEMICOLON
  static boolean PropertyMapContent(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "PropertyMapContent")) return false;
    boolean r;
    r = SubIdPropertyMap(b, l + 1);
    if (!r) r = IdProperty(b, l + 1);
    if (!r) r = consumeToken(b, COMMENT);
    if (!r) r = consumeToken(b, SEMICOLON);
    return r;
  }

  /* ********************************************************** */
  // ShapeRef (Connector ShapeRef)+ (COLON (ShapeLabel | BlockString)?)? BlockDefinition?
  public static boolean ShapeConnection(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeConnection")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SHAPE_CONNECTION, "<shape connection>");
    r = ShapeRef(b, l + 1);
    r = r && ShapeConnection_1(b, l + 1);
    r = r && ShapeConnection_2(b, l + 1);
    r = r && ShapeConnection_3(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (Connector ShapeRef)+
  private static boolean ShapeConnection_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeConnection_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ShapeConnection_1_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!ShapeConnection_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ShapeConnection_1", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // Connector ShapeRef
  private static boolean ShapeConnection_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeConnection_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = Connector(b, l + 1);
    r = r && ShapeRef(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (COLON (ShapeLabel | BlockString)?)?
  private static boolean ShapeConnection_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeConnection_2")) return false;
    ShapeConnection_2_0(b, l + 1);
    return true;
  }

  // COLON (ShapeLabel | BlockString)?
  private static boolean ShapeConnection_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeConnection_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && ShapeConnection_2_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (ShapeLabel | BlockString)?
  private static boolean ShapeConnection_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeConnection_2_0_1")) return false;
    ShapeConnection_2_0_1_0(b, l + 1);
    return true;
  }

  // ShapeLabel | BlockString
  private static boolean ShapeConnection_2_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeConnection_2_0_1_0")) return false;
    boolean r;
    r = ShapeLabel(b, l + 1);
    if (!r) r = BlockString(b, l + 1);
    return r;
  }

  // BlockDefinition?
  private static boolean ShapeConnection_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeConnection_3")) return false;
    BlockDefinition(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // ShapeIdChain COLON (ShapeLabel | BlockString)? BlockDefinition?
  public static boolean ShapeDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeDeclaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SHAPE_DECLARATION, "<shape declaration>");
    r = ShapeIdChain(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && ShapeDeclaration_2(b, l + 1);
    r = r && ShapeDeclaration_3(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (ShapeLabel | BlockString)?
  private static boolean ShapeDeclaration_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeDeclaration_2")) return false;
    ShapeDeclaration_2_0(b, l + 1);
    return true;
  }

  // ShapeLabel | BlockString
  private static boolean ShapeDeclaration_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeDeclaration_2_0")) return false;
    boolean r;
    r = ShapeLabel(b, l + 1);
    if (!r) r = BlockString(b, l + 1);
    return r;
  }

  // BlockDefinition?
  private static boolean ShapeDeclaration_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeDeclaration_3")) return false;
    BlockDefinition(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // BlockString | ID | STRING
  public static boolean ShapeId(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeId")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SHAPE_ID, "<shape id>");
    r = BlockString(b, l + 1);
    if (!r) r = consumeToken(b, ID);
    if (!r) r = consumeToken(b, STRING);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // ShapeId (DOT ShapeId)*
  static boolean ShapeIdChain(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeIdChain")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ShapeId(b, l + 1);
    r = r && ShapeIdChain_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (DOT ShapeId)*
  private static boolean ShapeIdChain_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeIdChain_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ShapeIdChain_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ShapeIdChain_1", c)) break;
    }
    return true;
  }

  // DOT ShapeId
  private static boolean ShapeIdChain_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeIdChain_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DOT);
    r = r && ShapeId(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // (ShapeRef DOT)? ShapeProperty
  static boolean ShapeIdWithProperty(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeIdWithProperty")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ShapeIdWithProperty_0(b, l + 1);
    r = r && ShapeProperty(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (ShapeRef DOT)?
  private static boolean ShapeIdWithProperty_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeIdWithProperty_0")) return false;
    ShapeIdWithProperty_0_0(b, l + 1);
    return true;
  }

  // ShapeRef DOT
  private static boolean ShapeIdWithProperty_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeIdWithProperty_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ShapeRef(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // UNQUOTED_STRING | STRING
  static boolean ShapeLabel(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeLabel")) return false;
    if (!nextTokenIs(b, "", STRING, UNQUOTED_STRING)) return false;
    boolean r;
    r = consumeToken(b, UNQUOTED_STRING);
    if (!r) r = consumeToken(b, STRING);
    return r;
  }

  /* ********************************************************** */
  // ShapePropertyKey COLON ShapePropertyValue
  public static boolean ShapeProperty(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeProperty")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SHAPE_PROPERTY, "<shape property>");
    r = ShapePropertyKey(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && ShapePropertyValue(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // PropertyKey
  public static boolean ShapePropertyKey(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapePropertyKey")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SHAPE_PROPERTY_KEY, "<shape property key>");
    r = PropertyKey(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // UnquotedStringValue | StringValue | ColorValue | OtherValue | Array
  static boolean ShapePropertyValue(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapePropertyValue")) return false;
    boolean r;
    r = UnquotedStringValue(b, l + 1);
    if (!r) r = StringValue(b, l + 1);
    if (!r) r = ColorValue(b, l + 1);
    if (!r) r = OtherValue(b, l + 1);
    if (!r) r = Array(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // (ParentShapeRefPsi DOT)? ShapeIdChain
  public static boolean ShapeRef(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeRef")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SHAPE_REF, "<shape ref>");
    r = ShapeRef_0(b, l + 1);
    r = r && ShapeIdChain(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (ParentShapeRefPsi DOT)?
  private static boolean ShapeRef_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeRef_0")) return false;
    ShapeRef_0_0(b, l + 1);
    return true;
  }

  // ParentShapeRefPsi DOT
  private static boolean ShapeRef_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ShapeRef_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ParentShapeRefPsi(b, l + 1);
    r = r && consumeToken(b, DOT);
    exit_section_(b, m, null, r);
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
  // IdPropertyKey COLON LBRACE PropertyMapContent* RBRACE
  public static boolean SubIdPropertyMap(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SubIdPropertyMap")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SUB_ID_PROPERTY_MAP, "<sub id property map>");
    r = IdPropertyKey(b, l + 1);
    r = r && consumeTokens(b, 0, COLON, LBRACE);
    r = r && SubIdPropertyMap_3(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // PropertyMapContent*
  private static boolean SubIdPropertyMap_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "SubIdPropertyMap_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!PropertyMapContent(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "SubIdPropertyMap_3", c)) break;
    }
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
