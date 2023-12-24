package com.dvd.intellij.d2.ide.parser

import com.intellij.psi.tree.TokenSet
import org.jetbrains.plugins.d2.lang.D2ElementTypes

object D2TokenSets {
  val IDENTIFIERS = TokenSet.create(D2ElementTypes.ID)
  val KEYWORDS = TokenSet.create(D2ElementTypes.TRUE, D2ElementTypes.FALSE)
  val COMMENT = TokenSet.create(D2ElementTypes.COMMENT)
  val ARROWS = TokenSet.create(
    D2ElementTypes.ARROW,
    D2ElementTypes.REVERSE_ARROW,
    D2ElementTypes.DOUBLE_ARROW,
    D2ElementTypes.DOUBLE_HYPHEN_ARROW
  )
  val NUMBERS = TokenSet.create(D2ElementTypes.INT, D2ElementTypes.FLOAT)
  val BRACES = TokenSet.create(D2ElementTypes.LBRACE, D2ElementTypes.RBRACE)

  //  val COLONS = TokenSet.create(D2ElementTypes.COLON, D2ElementTypes.SEMICOLON)
//  val DOT = TokenSet.create(D2ElementTypes.DOT)
  val STRING = TokenSet.create(D2ElementTypes.STRING)
}