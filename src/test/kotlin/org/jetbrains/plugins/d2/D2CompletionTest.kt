package org.jetbrains.plugins.d2

import com.dvd.intellij.d2.ide.utils.KEYWORD_HOLDERS
import com.dvd.intellij.d2.ide.utils.SIMPLE_RESERVED_KEYWORDS
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.plugins.d2.completion.varsAndClasses
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
    val lookupElementStrings = fixture.lookupElementStrings
    assertThat(lookupElementStrings).isNotNull()
    assertThat(lookupElementStrings).containsOnlyElementsOf((SIMPLE_RESERVED_KEYWORDS + KEYWORD_HOLDERS).toList())
  }

  @Test
  fun `no completion on edge (connector caret)`() {
    fixture.configureByText(
      "test.d2",
      "test -> <caret>"
    )
    fixture.complete(CompletionType.BASIC)
    val lookupElementStrings = fixture.lookupElementStrings
    assertThat(lookupElementStrings).isEmpty()
  }

  @Test
  fun `no completion on edge (caret connector)`() {
    fixture.configureByText(
      "test.d2",
      "test.<caret> ->"
    )
    fixture.complete(CompletionType.BASIC)
    val lookupElementStrings = fixture.lookupElementStrings
    assertThat(lookupElementStrings).isEmpty()
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
    val lookupElementStrings = fixture.lookupElementStrings
    assertThat(lookupElementStrings).isNotNull()
    assertThat(lookupElementStrings).containsOnlyElementsOf((SIMPLE_RESERVED_KEYWORDS + KEYWORD_HOLDERS).toList() + varsAndClasses.map { it.lookupString })
  }

  @Test
  fun connection() {
    fixture.configureByText(
      "test.d2",
      """
        s <caret>
      """
    )
    fixture.complete(CompletionType.BASIC)
    val lookupElementStrings = fixture.lookupElementStrings
    assertThat(lookupElementStrings).isNotNull()
    assertThat(lookupElementStrings).containsOnlyElementsOf(listOf("--", "->", "<-", "<->"))
  }
}

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
