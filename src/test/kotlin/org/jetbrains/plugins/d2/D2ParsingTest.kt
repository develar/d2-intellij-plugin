package org.jetbrains.plugins.d2

import org.junit.jupiter.api.Test

class D2ParsingTest : D2ParsingTestCase() {
  @Test
  fun parsing() {
    test()
  }

  @Test
  fun `just id`() {
    test("""
      s
    """.trimIndent())
  }

  @Test
  fun `label with dot`() {
    test("""
      s: 1.2
    """.trimIndent())
  }

  @Test
  fun `id with spaces`() {
    test("""
      logs foo bar -> logs
    """.trimIndent())
  }

  @Test
  fun `id with spaces and dash`() {
    test("""
      logs-foo bar - dash -> logs
    """.trimIndent())
  }

  @Test
  fun idAndReservedKeyword() {
    test("""
      logs.style.stroke: "#694024"
    """.trimIndent())
  }

  @Test
  fun `markdown pipe as marker`() {
    test("""
        explanation: |md
          # I can do headers
          - lists
          - lists
        
          And other normal markdown stuff
        |
    """.trimIndent())
  }

  @Test
  fun `markdown double pipe as marker`() {
    test("""
        explanation: ||md
          # I can do headers
          - lists
          - lists
        
          And other normal markdown stuff
        ||
    """.trimIndent())
  }

  @Test
  fun `markdown double pipe and quote as marker`() {
    test("""
      # Much cleaner!
      my_code: |`ts
        declare function getSmallPet(): Fish | Bird;
        const works = (a > 1) || (b < 2)
      `|
    """.trimIndent())
  }
}