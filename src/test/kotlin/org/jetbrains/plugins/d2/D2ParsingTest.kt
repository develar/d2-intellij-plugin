package org.jetbrains.plugins.d2

import org.junit.jupiter.api.Test

class D2ParsingTest : D2ParsingTestCase() {
  @Test
  fun parsing() {
    test()
  }

  @Test
  fun idAndReservedKeyword() {
    test("""
      logs.style.stroke: "#694024"
    """.trimIndent())
  }
}