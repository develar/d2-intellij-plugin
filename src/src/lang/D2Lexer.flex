package org.jetbrains.plugins.d2.lang;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.openapi.util.text.StringUtil;

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
    return startBlockString(BLOCK_STRING_LANG_STATE);
  }

  private IElementType startBlockString(int blockStringLangState) {
    yybegin(blockStringLangState);
    blockStringToken = new StringBuilder(yytext()).reverse();
    return BLOCK_STRING_OPEN;
  }


  private int suffixLength(CharSequence text, int pos, char repeatingChar) {
      int length = 0;
      while (length <= pos && text.charAt(pos - length) == repeatingChar) {
          length++;
      }
      return length;
  }

  /**
   * End-of-line characters are only alowed as part of line continuations.
   */
  private int skipSpacesAndContinuationsBackward(CharSequence text, int pos) {
    while (true) {
      if (pos == 0) return pos;
      char current = text.charAt(pos);
      if (current == ' ' || current == '\t' || current == '\f' ) {
        if (suffixLength(text,pos-1,'\\') % 2 == 1) {
          return pos; // escaped spaces are ok
        } else {
          pos--;
        }
      } else if (StringUtil.endsWith(text, 0, pos, "\\\r")) {
        pos -= 2;
      } else if (StringUtil.endsWith(text, 0, pos, "\\\n")) {
        pos -= 2;
      } else if (StringUtil.endsWith(text, 0, pos, "\\\r\n")) {
        pos -= 3;
      } else {
        return pos; // non-whitespace characters are ok
      }
    }
  }

  private boolean isEOFNext() {
    return zzCurrentPos == zzEndRead;
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

// does not match newline, see Dot (.) at https://jflex.de/manual.html
EscapeSequence=\\.
LabelSymbol=[^\s;{}#\\]
LabelSymbolOrEscape={LabelSymbol}|{EscapeSequence}
UnquotedLabelString = ([{LabelSymbol}&&[^|]]|{EscapeSequence}){LabelSymbolOrEscape}*({SpaceContinuation}+{LabelSymbolOrEscape}+)*
// [] is not supported - it is array
ValueSymbol=[{LabelSymbol}&&[^\[\]]]
ValueSymbolOrEscape={ValueSymbol}|{EscapeSequence}
UnquotedString = ([{ValueSymbol}&&[^|]]|{EscapeSequence}){ValueSymbolOrEscape}*({SpaceContinuation}+{ValueSymbolOrEscape}+)*

IdSymbol=[[^:.<>&-]&&{ValueSymbol}]
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

%states LABEL_STATE PROPERTY_VALUE_BEGIN_STATE PROPERTY_VALUE_STATE BLOCK_STRING_LANG_STATE BLOCK_STRING_BODY_STATE ARRAY_STATE EXPECT_IMPLICIT_SEMICOLON EDGE_GROUP_STATE EDGE_GROUP_BLOCK_STRING_LANG_STATE EDGE_GROUP_BLOCK_STRING_BODY_STATE EXPECT_EDGE_INDEX_STATE EDGE_INDEX_STATE

%%
<YYINITIAL> {
  {WhiteSpace} { return WHITE_SPACE; }

}

<EXPECT_IMPLICIT_SEMICOLON, PROPERTY_VALUE_BEGIN_STATE, PROPERTY_VALUE_STATE, LABEL_STATE, EDGE_GROUP_STATE, EXPECT_EDGE_INDEX_STATE, EDGE_INDEX_STATE> {
  {WhiteSpaceWithoutNewLines} { return WHITE_SPACE; }
  {WhiteSpace} { yybegin(YYINITIAL); yypushback(yylength()); return IMPLICIT_SEMICOLON; }
}

<YYINITIAL, EXPECT_IMPLICIT_SEMICOLON> {
  {LBrace} { yybegin(YYINITIAL); return LBRACE; }
  {RBrace} { yybegin(YYINITIAL); return RBRACE; }
  "." { yybegin(EXPECT_IMPLICIT_SEMICOLON); return DOT; }
  {Semicolon} { yybegin(YYINITIAL); return SEMICOLON; }
  ":" { yybegin(LABEL_STATE); return COLON; }
  "(" { yybegin(EDGE_GROUP_STATE); return LPAREN; }

  {ARROW}                     { yybegin(EXPECT_IMPLICIT_SEMICOLON); return ARROW; }
  {REVERSE_ARROW}             { yybegin(EXPECT_IMPLICIT_SEMICOLON); return REVERSE_ARROW; }
  {DOUBLE_HYPHEN_ARROW}       { yybegin(EXPECT_IMPLICIT_SEMICOLON); return DOUBLE_HYPHEN_ARROW; }
  {DOUBLE_ARROW}              { yybegin(EXPECT_IMPLICIT_SEMICOLON); return DOUBLE_ARROW; }
  {Comment} | {BlockComment}  { yybegin(YYINITIAL); return COMMENT; }

		{CompositeReservedKeywords} { yybegin(EXPECT_IMPLICIT_SEMICOLON); return COMPOSITE_RESERVED_KEYWORDS; }
		{SimpleReservedKeywords} { yybegin(PROPERTY_VALUE_BEGIN_STATE); return SIMPLE_RESERVED_KEYWORDS; }
		{ReservedKeywordHolders} { yybegin(EXPECT_IMPLICIT_SEMICOLON); return RESERVED_KEYWORD_HOLDERS; }
		{StyleKeyword} { yybegin(EXPECT_IMPLICIT_SEMICOLON); return STYLE_KEYWORD; }
		{StyleKeywords} { yybegin(PROPERTY_VALUE_BEGIN_STATE); return STYLE_KEYWORDS; }
		{ContainerLessKeywords} { yybegin(PROPERTY_VALUE_BEGIN_STATE); return CONTAINER_LESS_KEYWORDS; }

  "_" { yybegin(EXPECT_IMPLICIT_SEMICOLON); return PARENT_SHAPE_REF; }
  {Id} { yybegin(EXPECT_IMPLICIT_SEMICOLON); return ID; }

  // allows to avoid using regex for completion/color provider and other functionality that utilizes color
  {Color} { yybegin(EXPECT_IMPLICIT_SEMICOLON); return COLOR; }
  {String} { yybegin(EXPECT_IMPLICIT_SEMICOLON); return STRING; }
  {BlockStringStart} { return startBlockString(); }
}

<PROPERTY_VALUE_BEGIN_STATE> {
    ":" { yybegin(PROPERTY_VALUE_STATE); return COLON; }
}
// block string is not allowed for property value
<PROPERTY_VALUE_STATE> {
		{Int} { return INT; }
		{Float} { return FLOAT; }
		{True} { return TRUE; }
		{False} { return FALSE; }
		{Color} { return COLOR; }
		{String} { return STRING; }
		{LBracket} { yybegin(ARRAY_STATE); return LBRACKET; }
		{RBracket} { return RBRACKET; }
		{UnquotedString} { return UNQUOTED_STRING; }

		{Semicolon} { yybegin(YYINITIAL); return SEMICOLON; }

		// inline shape definition: {shape: person}
		{RBrace} { yybegin(YYINITIAL); return RBRACE; }
}

<ARRAY_STATE> {
		{Semicolon} { return SEMICOLON; }
		// D2 supports nested arrays, but we don't for now (nested states are nnot supported, can be implemented, but not clear yet for what)
		{LBracket} { return LBRACKET; }
		{RBracket} { yybegin(EXPECT_IMPLICIT_SEMICOLON); return RBRACKET; }
		{UnquotedString} { return UNQUOTED_STRING; }
		{WhiteSpace} { return WHITE_SPACE; }
}

<LABEL_STATE> {
    {BlockStringStart} { return startBlockString(); }
    {Semicolon} { return SEMICOLON; }
    {String} { return STRING; }
    {UnquotedLabelString} { return UNQUOTED_STRING; }
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
		else if (StringUtil.endsWith(yytext(), blockStringToken)) {
				// push back to register on next step as a BLOCK_STRING_CLOSE token,
				// (we neeed it to easily implement embededed language, brace matcher and so on)
				yypushback(blockStringToken.length());
				blockStringToken = null;
				return BLOCK_STRING_BODY;
		}
    }

  <<EOF>> { yybegin(YYINITIAL); return BAD_CHARACTER; }
}

<EDGE_GROUP_STATE> {
    "." { return DOT; }
    {ARROW}                     { return ARROW; }
    {REVERSE_ARROW}             { return REVERSE_ARROW; }
    {DOUBLE_HYPHEN_ARROW}       { return DOUBLE_HYPHEN_ARROW; }
    {DOUBLE_ARROW}              { return DOUBLE_ARROW; }
    ")" { yybegin(EXPECT_EDGE_INDEX_STATE); return RPAREN; }
    {Id}{SpaceContinuation}*")"{SpaceContinuation}*[\n#{}\[\]:.] {
        CharSequence text = yytext();
        int lastIndex = yylength() - 1;
        int rightParIndex = StringUtil.lastIndexOfAny(text, ")");
        int lastIdSymbolIndex = skipSpacesAndContinuationsBackward(text, rightParIndex - 1);
        yypushback(lastIndex - lastIdSymbolIndex);
        return ID;
    }
    {Id}{SpaceContinuation}*")"{SpaceContinuation}* {
        CharSequence text = yytext();
        int lastIndex = yylength() - 1;
        int rightParIndex = StringUtil.lastIndexOfAny(text, ")");
        if (isEOFNext()) {
            int lastIdSymbolIndex = skipSpacesAndContinuationsBackward(text, rightParIndex - 1);
            yypushback(lastIndex - lastIdSymbolIndex);
        } else {
            yypushback(lastIndex - rightParIndex);
        }
        return ID;
    }
    {Id} { return ID; }
    {String} { return STRING; }
    {BlockStringStart} { return startBlockString(EDGE_GROUP_BLOCK_STRING_LANG_STATE); }
}

<EDGE_GROUP_BLOCK_STRING_LANG_STATE> {
    [^\s|]+ { yybegin(EDGE_GROUP_BLOCK_STRING_BODY_STATE); return BLOCK_STRING_LANG; }
    \s+|[^\s|]*\| {
        yypushback(yylength());
        yybegin(EDGE_GROUP_BLOCK_STRING_BODY_STATE);
    }
    <<EOF>> { yybegin(YYINITIAL); return BAD_CHARACTER; }
}

<EDGE_GROUP_BLOCK_STRING_BODY_STATE> {
	[^|]*\|+ {
		if (blockStringToken == null) {
				yybegin(EDGE_GROUP_STATE);
				blockStringToken = null;
				return BLOCK_STRING_CLOSE;
		}
		else if (StringUtil.endsWith(yytext(), blockStringToken)) {
				// push back to register on next step as a BLOCK_STRING_CLOSE token,
				// (we neeed it to easily implement embededed language, brace matcher and so on)
				yypushback(blockStringToken.length());
				blockStringToken = null;
				return BLOCK_STRING_BODY;
		}
    }

  <<EOF>> { yybegin(YYINITIAL); return BAD_CHARACTER; }
}

<EXPECT_EDGE_INDEX_STATE> {
    {LBracket} { yybegin(EDGE_INDEX_STATE); return LBRACKET; }
    ":" { yybegin(LABEL_STATE); return COLON; }
    "." { yybegin(EXPECT_IMPLICIT_SEMICOLON); return DOT; }
    {LBrace} { yybegin(YYINITIAL); return LBRACE; }
}

<EDGE_INDEX_STATE> {
    {Int} { return INT; }
    {RBracket} { yybegin(EXPECT_IMPLICIT_SEMICOLON); return RBRACKET; }
}

[^] { return BAD_CHARACTER; }
