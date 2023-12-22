package com.dvd.intellij.d2.lang;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.dvd.intellij.d2.lang.D2ElementTypes.*;

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

EOL=\R
WHITE_SPACE=\s+

ARROW=-+>
REVERSE_ARROW=<-+
DOUBLE_HYPHEN_ARROW=--+
DOUBLE_ARROW=<-+>
COMMENT=#.*
NUMERIC_LITERAL=[0-9]+
FLOAT_LITERAL=[0-9]+\.[0-9]+
STRING_LITERAL=('([^'\\]|\\.)*'|\"([^\"\\]|\\\"|\'|\\)*\")
IDENTIFIER=[a-zA-Z_*0-9]+(-[a-zA-Z_*0-9]+)*
NEWLINE=\r?\n
SPACE=[ \t\n\x0B\f\r]+

%%
<YYINITIAL> {
  {WHITE_SPACE}               { return WHITE_SPACE; }

  "{"                         { return LBRACE; }
  "}"                         { return RBRACE; }
  "."                         { return DOT; }
  ";"                         { return SEMICOLON; }
  ":"                         { return COLON; }
  "*"                         { return STAR; }
  "true"                      { return TRUE; }
  "false"                     { return FALSE; }

  {ARROW}                     { return ARROW; }
  {REVERSE_ARROW}             { return REVERSE_ARROW; }
  {DOUBLE_HYPHEN_ARROW}       { return DOUBLE_HYPHEN_ARROW; }
  {DOUBLE_ARROW}              { return DOUBLE_ARROW; }
  {COMMENT}                   { return COMMENT; }
  {NUMERIC_LITERAL}           { return NUMERIC_LITERAL; }
  {FLOAT_LITERAL}             { return FLOAT_LITERAL; }
  {STRING_LITERAL}            { return STRING_LITERAL; }
  {IDENTIFIER}                { return IDENTIFIER; }
  {NEWLINE}                   { return NEWLINE; }
  {SPACE}                     { return SPACE; }

}

[^] { return BAD_CHARACTER; }
