// This is a generated file. Not intended for manual editing.
package com.dvd.intellij.d2.lang;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.dvd.intellij.d2.lang.D2ElementTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class D2Parser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType root_, PsiBuilder builder_) {
    parseLight(root_, builder_);
    return builder_.getTreeBuilt();
  }

  public void parseLight(IElementType root_, PsiBuilder builder_) {
    boolean result_;
    builder_ = adapt_builder_(root_, builder_, this, null);
    Marker marker_ = enter_section_(builder_, 0, _COLLAPSE_, null);
    result_ = parse_root_(root_, builder_);
    exit_section_(builder_, 0, marker_, root_, result_, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType root_, PsiBuilder builder_) {
    return parse_root_(root_, builder_, 0);
  }

  static boolean parse_root_(IElementType root_, PsiBuilder builder_, int level_) {
    return File(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // STRING_LITERAL | IDENTIFIER | NUMERIC_LITERAL | FLOAT_LITERAL | TRUE | FALSE
  public static boolean AttributeValue(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "AttributeValue")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ATTRIBUTE_VALUE, "<attribute value>");
    result_ = consumeToken(builder_, STRING_LITERAL);
    if (!result_) result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = consumeToken(builder_, NUMERIC_LITERAL);
    if (!result_) result_ = consumeToken(builder_, FLOAT_LITERAL);
    if (!result_) result_ = consumeToken(builder_, TRUE);
    if (!result_) result_ = consumeToken(builder_, FALSE);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // LBRACE ShapeDefinitions* RBRACE
  public static boolean BlockDefinition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "BlockDefinition")) return false;
    if (!nextTokenIs(builder_, LBRACE)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, BLOCK_DEFINITION, null);
    result_ = consumeToken(builder_, LBRACE);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, BlockDefinition_1(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, RBRACE) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // ShapeDefinitions*
  private static boolean BlockDefinition_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "BlockDefinition_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!ShapeDefinitions(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "BlockDefinition_1", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // ARROW | REVERSE_ARROW | DOUBLE_ARROW | DOUBLE_HYPHEN_ARROW
  public static boolean Connector(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "Connector")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CONNECTOR, "<connector>");
    result_ = consumeToken(builder_, ARROW);
    if (!result_) result_ = consumeToken(builder_, REVERSE_ARROW);
    if (!result_) result_ = consumeToken(builder_, DOUBLE_ARROW);
    if (!result_) result_ = consumeToken(builder_, DOUBLE_HYPHEN_ARROW);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // !<<eof>> ShapeDefinitions*
  static boolean File(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "File")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = File_0(builder_, level_ + 1);
    result_ = result_ && File_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // !<<eof>>
  private static boolean File_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "File_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !eof(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // ShapeDefinitions*
  private static boolean File_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "File_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!ShapeDefinitions(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "File_1", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // (SEMICOLON ShapeDefinition)+
  public static boolean InlineShapeDefinition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "InlineShapeDefinition")) return false;
    if (!nextTokenIs(builder_, SEMICOLON)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _LEFT_, INLINE_SHAPE_DEFINITION, null);
    result_ = InlineShapeDefinition_0(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!InlineShapeDefinition_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "InlineShapeDefinition", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // SEMICOLON ShapeDefinition
  private static boolean InlineShapeDefinition_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "InlineShapeDefinition_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, SEMICOLON);
    result_ = result_ && ShapeDefinition(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // AttributeValue
  public static boolean LabelDefinition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "LabelDefinition")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, LABEL_DEFINITION, "<label definition>");
    result_ = AttributeValue(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // (Connector ShapeDefinition)+
  public static boolean ShapeConnection(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ShapeConnection")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _LEFT_, SHAPE_CONNECTION, "<shape connection>");
    result_ = ShapeConnection_0(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!ShapeConnection_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "ShapeConnection", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // Connector ShapeDefinition
  private static boolean ShapeConnection_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ShapeConnection_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = Connector(builder_, level_ + 1);
    result_ = result_ && ShapeDefinition(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // AttributeValue
  public static boolean ShapeDefinition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ShapeDefinition")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, SHAPE_DEFINITION, "<shape definition>");
    result_ = AttributeValue(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // ShapeDefinition (ShapeConnection | SubShapeDefinition | InlineShapeDefinition)* ShapeExtras?
  public static boolean ShapeDefinitions(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ShapeDefinitions")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, SHAPE_DEFINITIONS, "<shape definitions>");
    result_ = ShapeDefinition(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, ShapeDefinitions_1(builder_, level_ + 1));
    result_ = pinned_ && ShapeDefinitions_2(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // (ShapeConnection | SubShapeDefinition | InlineShapeDefinition)*
  private static boolean ShapeDefinitions_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ShapeDefinitions_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!ShapeDefinitions_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "ShapeDefinitions_1", pos_)) break;
    }
    return true;
  }

  // ShapeConnection | SubShapeDefinition | InlineShapeDefinition
  private static boolean ShapeDefinitions_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ShapeDefinitions_1_0")) return false;
    boolean result_;
    result_ = ShapeConnection(builder_, level_ + 1);
    if (!result_) result_ = SubShapeDefinition(builder_, level_ + 1);
    if (!result_) result_ = InlineShapeDefinition(builder_, level_ + 1);
    return result_;
  }

  // ShapeExtras?
  private static boolean ShapeDefinitions_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ShapeDefinitions_2")) return false;
    ShapeExtras(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // COLON (LabelDefinition BlockDefinition? | BlockDefinition)
  static boolean ShapeExtras(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ShapeExtras")) return false;
    if (!nextTokenIs(builder_, COLON)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = consumeToken(builder_, COLON);
    pinned_ = result_; // pin = 1
    result_ = result_ && ShapeExtras_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // LabelDefinition BlockDefinition? | BlockDefinition
  private static boolean ShapeExtras_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ShapeExtras_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = ShapeExtras_1_0(builder_, level_ + 1);
    if (!result_) result_ = BlockDefinition(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // LabelDefinition BlockDefinition?
  private static boolean ShapeExtras_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ShapeExtras_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = LabelDefinition(builder_, level_ + 1);
    result_ = result_ && ShapeExtras_1_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // BlockDefinition?
  private static boolean ShapeExtras_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ShapeExtras_1_0_1")) return false;
    BlockDefinition(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // DOT ShapeDefinition ShapeExtras?
  public static boolean SubShapeDefinition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "SubShapeDefinition")) return false;
    if (!nextTokenIs(builder_, DOT)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _LEFT_, SUB_SHAPE_DEFINITION, null);
    result_ = consumeToken(builder_, DOT);
    result_ = result_ && ShapeDefinition(builder_, level_ + 1);
    result_ = result_ && SubShapeDefinition_2(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // ShapeExtras?
  private static boolean SubShapeDefinition_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "SubShapeDefinition_2")) return false;
    ShapeExtras(builder_, level_ + 1);
    return true;
  }

}
