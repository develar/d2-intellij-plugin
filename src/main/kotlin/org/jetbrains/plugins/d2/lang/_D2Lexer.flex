package org.jetbrains.plugins.d2.lang;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static org.jetbrains.plugins.d2.lang.D2ElementTypes.*;

%%

%{
  public _D2Lexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _D2Lexer
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

UnquotedString=[^ \t\n\r{}]+([ \t]+[^ \n\r{}]+)*

%states LABEL

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

	{String} { return STRING; }
	{UnquotedString} { return UNQUOTED_STRING; }
	[ \t]+ { return WHITE_SPACE; }
	[\r\n]+ { yybegin(YYINITIAL); return WHITE_SPACE; }
	{LBrace} { yybegin(YYINITIAL); return LBRACE; }
	// inline shape definition: {shape: person}
	{RBrace} { yybegin(YYINITIAL); return RBRACE; }
}

[^] { return BAD_CHARACTER; }
