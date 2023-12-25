package org.jetbrains.plugins.d2

import com.intellij.codeInsight.completion.CompletionType
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.RegisterExtension

class D2CompletionTest : D2LightCodeInsightFixtureTestCase() {
  @Test
  fun `block completion`() {
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
    assertThat(lookupElementStrings).containsExactlyInAnyOrder(
      "constraint",
      "desc",
      "height",
      "icon",
      "label",
      "link",
      "near",
      "shape",
      "source-arrowhead",
      "style",
      "target-arrowhead",
      "tooltip",
      "width"
    )
  }
}

abstract class D2LightCodeInsightFixtureTestCase() {
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
