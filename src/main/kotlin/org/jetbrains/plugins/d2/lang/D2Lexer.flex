package org.jetbrains.plugins.d2.lang;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.openapi.util.text.StringUtilRt;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static org.jetbrains.plugins.d2.lang.D2ElementTypes.*;

%%

%{
  public D2FlexLexer() {
    this((java.io.Reader)null);
  }

  private StringBuilder blockStringToken;
%}

%public
%class D2FlexLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

// non style/holder keywords, see SimpleReservedKeywords in d2 source code,
// `classes` is not SimpleReservedKeyword, from CompositeReservedKeywords
SimpleReservedKeywords = label | desc | shape | icon | constraint | tooltip | link | near | width | height | direction | top | left |
grid-rows | grid-columns | grid-gap |
vertical-gap | horizontal-gap |
class | vars | classes

// reservedKeywordHolders are reserved keywords that are meaningless on its own and must hold composites
ReservedKeywordHolders = style | source-arrowhead | target-arrowhead

ARROW=-+>
REVERSE_ARROW=<-+
DOUBLE_HYPHEN_ARROW=--+
DOUBLE_ARROW=<-+>
Comment=#.*
Int=[0-9]+
Float=[0-9]+\.[0-9]+
String=('([^'\\]|\\.)*'|\"([^\"\\]|\\\"|\'|\\)*\")
Id=[a-zA-Z_*0-9]+(-[a-zA-Z_*0-9]+)*
WhiteSpace=[ \t\n\x0B\f\r]+

LBrace="{"
RBrace="}"

%states LABEL BLOCK_STRING_LANG_STATE BLOCK_STRING_BODY_STATE

%%
<YYINITIAL> {
  {WhiteSpace} { return WHITE_SPACE; }

  {LBrace} { return LBRACE; }
  {RBrace} { return RBRACE; }
  "."                         { return DOT; }
  ";"                         { return SEMICOLON; }
  ":"                         { yybegin(LABEL); return COLON; }
  "true"                      { return TRUE; }
  "false"                     { return FALSE; }

  {ARROW}                     { return ARROW; }
  {REVERSE_ARROW}             { return REVERSE_ARROW; }
  {DOUBLE_HYPHEN_ARROW}       { return DOUBLE_HYPHEN_ARROW; }
  {DOUBLE_ARROW}              { return DOUBLE_ARROW; }
  {Comment}                   { return COMMENT; }
  {Int} { return INT; }
  {Float} { return FLOAT; }

		{SimpleReservedKeywords} { return SIMPLE_RESERVED_KEYWORDS; }
		{ReservedKeywordHolders} { return RESERVED_KEYWORD_HOLDERS; }

  {Id} { return ID; }

  {String} { return STRING; }
}

<LABEL> {
		{Int} { return INT; }
		{Float} { return FLOAT; }
		"true" { return TRUE; }
		"false" { return FALSE; }

		// docs: D2 actually allows you to use any special symbols (not alphanumeric, space, or _) after the first pipe
		\|+[^a-zA-Z0-9\s_|]* {
								yybegin(BLOCK_STRING_LANG_STATE);
								blockStringToken = new StringBuilder(yytext()).reverse();
								return BLOCK_STRING_OPEN;
      }

	 {String} { return STRING; }
	 [^\s{}|]+([ \t]+[^\s{}]+)* { return UNQUOTED_STRING; }
	 [ \t]+ { return WHITE_SPACE; }
	 [\r\n]+ { yybegin(YYINITIAL); return WHITE_SPACE; }
	 {LBrace} { yybegin(YYINITIAL); return LBRACE; }
	 // inline shape definition: {shape: person}
	 {RBrace} { yybegin(YYINITIAL); return RBRACE; }
}

<BLOCK_STRING_LANG_STATE> {
		[^\s|]+ { yybegin(BLOCK_STRING_BODY_STATE); return BLOCK_STRING_LANG; }

		\s+ { return WHITE_SPACE; }

  <<EOF>> { yybegin(YYINITIAL); return BAD_CHARACTER; }
}

// IntelliJ incremental lexer restores only zero state,
// so, we can be sure that lexing will be never performed in the middle of block string (always from BLOCK_STRING_OPEN)
<BLOCK_STRING_BODY_STATE> {
	[^|]*\|+ {
									if (blockStringToken == null) {
											yybegin(YYINITIAL);
											blockStringToken = null;
											return BLOCK_STRING_CLOSE;
									}
									else if (StringUtilRt.endsWith(yytext(), blockStringToken)) {
											// push back to register on next step as a BLOCK_STRING_CLOSE token,
											// (we neeed it to easily implement embededed language, brace matcher and so on)
											yypushback(blockStringToken.length());
											blockStringToken = null;
											return BLOCK_STRING_BODY;
									}
							}

  <<EOF>> { yybegin(YYINITIAL); return BAD_CHARACTER; }
}

[^] { return BAD_CHARACTER; }
