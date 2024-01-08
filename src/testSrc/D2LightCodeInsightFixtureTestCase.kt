package org.jetbrains.plugins.d2

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import org.assertj.core.api.Assertions
import org.jetbrains.plugins.d2.lang.psi.ShapeDeclaration
import org.jetbrains.plugins.d2.lang.psi.ShapeId
import org.jetbrains.plugins.d2.lang.psi.ShapePsiReference
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.RegisterExtension

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

  val fixture: CodeInsightTestFixture
    get() = testCase.fixture
}