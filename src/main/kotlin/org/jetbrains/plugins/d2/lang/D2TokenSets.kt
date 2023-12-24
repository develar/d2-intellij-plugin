package org.jetbrains.plugins.d2.lang

import com.intellij.psi.tree.TokenSet

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

  val STRING = TokenSet.create(D2ElementTypes.STRING, D2ElementTypes.UNQUOTED_STRING)
}