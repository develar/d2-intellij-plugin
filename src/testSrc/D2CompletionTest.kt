package org.jetbrains.plugins.d2

import com.intellij.codeInsight.completion.CompletionType
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.plugins.d2.completion.directions
import org.jetbrains.plugins.d2.completion.varsAndClasses
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.RegisterExtension

class D2CompletionTest : D2LightCodeInsightFixtureTestCase() {
  @Test
  fun block() {
    fixture.configureByText(
      "test.d2",
      """
        s: {
          <caret>
        }
      """
    )
    fixture.complete(CompletionType.BASIC)
    assertThat(fixture.lookupElementStrings).containsOnlyElementsOf((SIMPLE_RESERVED_KEYWORDS + KEYWORD_HOLDERS).toList())
  }

  @Test
  fun direction() {
    fixture.configureByText(
      "test.d2",
      "direction: <caret>"
    )
    fixture.complete(CompletionType.BASIC)
    assertThat(fixture.lookupElementStrings).containsOnlyElementsOf(directions.map { it.lookupString })
  }

  @Test
  fun `no completion on edge (connector caret)`() {
    fixture.configureByText(
      "test.d2",
      "test -> <caret>"
    )
    fixture.complete(CompletionType.BASIC)
    assertThat(fixture.lookupElementStrings).isEmpty()
  }

  @Test
  fun `no completion on edge (caret connector)`() {
    fixture.configureByText(
      "test.d2",
      "test.<caret> ->"
    )
    fixture.complete(CompletionType.BASIC)
    assertThat(fixture.lookupElementStrings).isEmpty()
  }

  @Test
  fun file() {
    fixture.configureByText(
      "test.d2",
      """
        <caret>
      """
    )
    fixture.complete(CompletionType.BASIC)
    assertThat(fixture.lookupElementStrings).containsOnlyElementsOf((SIMPLE_RESERVED_KEYWORDS + KEYWORD_HOLDERS).toList() + varsAndClasses.map { it.lookupString })
  }

  @Test
  @Disabled
  fun connection() {
    fixture.configureByText(
      "test.d2",
      """
        s <caret>
      """
    )
    fixture.complete(CompletionType.BASIC)
    assertThat(fixture.lookupElementStrings).containsOnlyElementsOf(listOf("--", "->", "<-", "<->"))
  }

  @Test
  fun `style sub properties for existing color value`() {
    fixture.configureByText(
      "test.d2",
      """
        logs.style.<caret>: "#694024"
      """
    )
    fixture.complete(CompletionType.BASIC)
    // listOf("fill", "font-color", "stroke")
    assertThat(fixture.lookupElementStrings).containsOnlyElementsOf(allShapeStyleKeywords())
  }

  @Test
  fun `style sub properties for existing float value`() {
    fixture.configureByText(
      "test.d2",
      """
        logs.style.<caret>: 0.1
      """
    )
    fixture.complete(CompletionType.BASIC)
    // listOf("opacity")
    assertThat(fixture.lookupElementStrings).containsOnlyElementsOf(allShapeStyleKeywords())
  }

  @Test
  fun `style sub properties for existing int value`() {
    fixture.configureByText(
      "test.d2",
      """
        logs.style.<caret>: 1
      """
    )
    fixture.complete(CompletionType.BASIC)
    // listOf("border-radius", "font-size", "opacity", "stroke-dash", "stroke-width")
    assertThat(fixture.lookupElementStrings).containsOnlyElementsOf(allShapeStyleKeywords())
  }

  @Test
  fun `style sub properties for existing boolean value`() {
    fixture.configureByText(
      "test.d2",
      """
        logs.style.<caret>: true
      """
    )
    fixture.complete(CompletionType.BASIC)
    assertThat(fixture.lookupElementStrings).containsOnlyElementsOf(
      //listOf(
      //  "3d",
      //  "animated",
      //  "bold",
      //  "double-border",
      //  "filled",
      //  "italic",
      //  "multiple",
      //  "shadow",
      //  "underline"
      //)
      allShapeStyleKeywords()
    )
  }

  @Test
  fun `style sub properties without value`() {
    fixture.configureByText(
      "test.d2",
      """
        logs.style.<caret>
      """
    )
    fixture.complete(CompletionType.BASIC)
    assertThat(fixture.lookupElementStrings).containsOnlyElementsOf(allShapeStyleKeywords())
  }

  @Test
  fun `style font`() {
    fixture.configureByText(
      "test.d2",
      """
        logs.style.font: <caret>
      """
    )
    fixture.complete(CompletionType.BASIC)
    assertThat(fixture.lookupElementStrings).containsOnlyElementsOf(listOf("mono"))
  }

  @Test
  fun `style font with valid prefix`() {
    fixture.configureByText(
      "test.d2",
      """
        logs.style.font: mo<caret>
      """
    )
    fixture.complete(CompletionType.BASIC)
    assertThat(fixture.lookupElementStrings).isNull()
  }
}

private fun allShapeStyleKeywords() = ShapeStyles.values().map { it.keyword }

abstract class D2LightCodeInsightFixtureTestCase {
  @Suppress("JUnitMalformedDeclaration")
  @RegisterExtension
  private val testCase = object : BasePlatformTestCase(), BeforeEachCallback, AfterEachCallback {
    override fun beforeEach(context: ExtensionContext?) {
      setUp()
    }

    override fun afterEach(context: ExtensionContext?) {
      tearDown()
    }

    val fixture: CodeInsightTestFixture
      get() = myFixture
  }

  protected val fixture: CodeInsightTestFixture
    get() = testCase.fixture
}
