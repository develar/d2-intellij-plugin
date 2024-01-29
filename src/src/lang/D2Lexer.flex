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

  private IElementType startBlockString() {
    yybegin(BLOCK_STRING_LANG_STATE);
    blockStringToken = new StringBuilder(yytext()).reverse();
    return BLOCK_STRING_OPEN;
  }
%}

%public
%class D2FlexLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

// non style/holder keywords, see SimpleReservedKeywords in d2 source code,
SimpleReservedKeywords = label | desc | shape | icon | constraint | tooltip | link | near | width | height | top | left |
grid-rows | grid-columns | grid-gap |
vertical-gap | horizontal-gap |
class

CompositeReservedKeywords = classes | vars

StyleKeyword = style

// keywors that can be used without container (top-level)
ContainerLessKeywords=direction

// StyleKeywords are reserved keywords which cannot exist outside of the "style" keyword
StyleKeywords = opacity | stroke | fill | fill-pattern | stroke-width | stroke-dash | border-radius | font | font-size | font-color |
bold | italic | underline | text-transform | shadow | multiple | double-border | 3d | animated | filled

// reservedKeywordHolders are reserved keywords that are meaningless on its own and must hold composites
ReservedKeywordHolders = source-arrowhead | target-arrowhead

Continuation=\\\R
Space=[ \t\f]
SpaceContinuationNewLine=(\s|{Continuation})
SpaceContinuation=({Space}|{Continuation})
ContinuationClosure={Continuation}{SpaceContinuation}*
ARROW=-(-|{ContinuationClosure})*>
REVERSE_ARROW=<(-|{ContinuationClosure})*-
DOUBLE_HYPHEN_ARROW=-(-|{ContinuationClosure})*-
DOUBLE_ARROW=<{ContinuationClosure}*-(-|{ContinuationClosure})*>

Comment=#.*
BlockComment = "\"\"\""~"\"\"\""
Int=[0-9]([0-9]|{ContinuationClosure})*
Float=[0-9]([0-9]|{ContinuationClosure})*\.{ContinuationClosure}?[0-9]([0-9]|{ContinuationClosure})*
Semicolon=";"

// docs: D2 actually allows you to use any special symbols (not alphanumeric, space, or _) after the first pipe
BlockStringStart=\|+[^a-zA-Z0-9\s_|]*

SingleQuotedString = '[^'\R]*'?
DoubleQuotedString = \"([^\"\R\\]|{Continuation}|{EscapeSequence})*\"?
String={SingleQuotedString}|{DoubleQuotedString}

Hex=[a-fA-F0-9]
// rune literals are allowed, see https://go.dev/ref/spec#Rune_literals
EscapedHex=\\[{Hex}--[abf]]
SingleQuotedColor='#({Hex}{3}){1,2}'?
DoubleQuotedColor=\"{ContinuationClosure}?\\?#{ContinuationClosure}?((({Hex}|{EscapedHex}){ContinuationClosure}?){3}){1,2}\"?
Color={SingleQuotedColor}|{DoubleQuotedColor}

True=t{ContinuationClosure}?r{ContinuationClosure}?u{ContinuationClosure}?e
False=f{ContinuationClosure}?a{ContinuationClosure}?l{ContinuationClosure}?s{ContinuationClosure}?e

EscapeSequence=\\.
LabelSymbol=[^\s;{}#]
UnquotedLabelString = [{LabelSymbol}&&[^|]]{LabelSymbol}*({SpaceContinuation}+{LabelSymbol}+)*
// [] is not supported - it is array
ValueSymbol=[{LabelSymbol}&&[^\[\]]]
UnquotedString = [{ValueSymbol}&&[^|]]{ValueSymbol}*({SpaceContinuation}+{ValueSymbol}+)*

IdSymbol=[[^:.<>&\\-]&&{ValueSymbol}]
IdDashSubstring=-{SpaceContinuation}*([{IdSymbol}&&[^->*]]|{EscapeSequence})
AllowedInId=({IdSymbol}|{EscapeSequence}|{IdDashSubstring})
IdBody={AllowedInId}*({SpaceContinuation}+{AllowedInId}+)*
ExclamationId=(\!({SpaceContinuation}*([[^&]&&{IdSymbol}]|{EscapeSequence}|{IdDashSubstring}){IdBody})?)
AllowedFirstInId=([{IdSymbol}&&[^|!('\"$@]]|{EscapeSequence}|{IdDashSubstring})
RegularId={AllowedFirstInId}{IdBody}
Id=({RegularId}|{ExclamationId})
WhiteSpaceWithoutNewLines={SpaceContinuation}+
WhiteSpace={SpaceContinuationNewLine}+

LBrace="{"
RBrace="}"

LBracket="["
RBracket="]"

%states LABEL_STATE PROPERTY_VALUE_BEGIN_STATE PROPERTY_VALUE_STATE BLOCK_STRING_LANG_STATE BLOCK_STRING_BODY_STATE ARRAY_STATE

%%
<YYINITIAL> {
  {WhiteSpace} { return WHITE_SPACE; }

  {LBrace} { return LBRACE; }
  {RBrace} { return RBRACE; }
  "." { return DOT; }
  {Semicolon} { return SEMICOLON; }
  ":" { yybegin(LABEL_STATE); return COLON; }

  {ARROW}                     { return ARROW; }
  {REVERSE_ARROW}             { return REVERSE_ARROW; }
  {DOUBLE_HYPHEN_ARROW}       { return DOUBLE_HYPHEN_ARROW; }
  {DOUBLE_ARROW}              { return DOUBLE_ARROW; }
  {Comment} | {BlockComment}  { return COMMENT; }

		{CompositeReservedKeywords} { return COMPOSITE_RESERVED_KEYWORDS; }
		{SimpleReservedKeywords} { yybegin(PROPERTY_VALUE_BEGIN_STATE); return SIMPLE_RESERVED_KEYWORDS; }
		{ReservedKeywordHolders} { return RESERVED_KEYWORD_HOLDERS; }
		{StyleKeyword} { return STYLE_KEYWORD; }
		{StyleKeywords} { yybegin(PROPERTY_VALUE_BEGIN_STATE); return STYLE_KEYWORDS; }
		{ContainerLessKeywords} { yybegin(PROPERTY_VALUE_BEGIN_STATE); return CONTAINER_LESS_KEYWORDS; }

  "_" { return PARENT_SHAPE_REF; }
  {Id} { return ID; }

  {String} { return STRING; }
  {BlockStringStart} { return startBlockString(); }
		// allows to avoid using regex for completion/color provider and other functionality that utilizes color
  {Color} { return COLOR; }
}

<PROPERTY_VALUE_BEGIN_STATE> {
		":" { yybegin(PROPERTY_VALUE_STATE); return COLON; }
		{WhiteSpace} { return WHITE_SPACE; }
}

// block string is not allowed for property value
<PROPERTY_VALUE_STATE> {
  ":" { return COLON; }

		{Int} { return INT; }
		{Float} { return FLOAT; }
		{True} { return TRUE; }
		{False} { return FALSE; }
		{Color} { return COLOR; }
		{String} { return STRING; }
		{LBracket} { yybegin(ARRAY_STATE); return LBRACKET; }
		{RBracket} { return RBRACKET; }
		{UnquotedString} { return UNQUOTED_STRING; }

		{WhiteSpaceWithoutNewLines} { return WHITE_SPACE; }
		{WhiteSpace} { yybegin(YYINITIAL); return WHITE_SPACE; }
		{Semicolon} { yybegin(YYINITIAL); return SEMICOLON; }

		// inline shape definition: {shape: person}
		{RBrace} { yybegin(YYINITIAL); return RBRACE; }
}

<ARRAY_STATE> {
		{Semicolon} { return SEMICOLON; }
		// D2 supports nested arrays, but we don't for now (nested states are nnot supported, can be implemented, but not clear yet for what)
		{LBracket} { return LBRACKET; }
		{RBracket} { yybegin(YYINITIAL); return RBRACKET; }
		{UnquotedString} { return UNQUOTED_STRING; }

		{WhiteSpace} { return WHITE_SPACE; }
}

<LABEL_STATE> {
    {BlockStringStart} { return startBlockString(); }

	 {String} { return STRING; }
	 {UnquotedLabelString} { return UNQUOTED_STRING; }
	 {WhiteSpaceWithoutNewLines} { return WHITE_SPACE; }
	 {WhiteSpace} { yybegin(YYINITIAL); return WHITE_SPACE; }
	 {LBrace} { yybegin(YYINITIAL); return LBRACE; }
	 // inline shape definition: {shape: person}
	 {RBrace} { yybegin(YYINITIAL); return RBRACE; }
}

<BLOCK_STRING_LANG_STATE> {
    [^\s|]+ { yybegin(BLOCK_STRING_BODY_STATE); return BLOCK_STRING_LANG; }
    \s+|[^\s|]*\| {
        yypushback(yylength());
        yybegin(BLOCK_STRING_BODY_STATE);
    }
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
