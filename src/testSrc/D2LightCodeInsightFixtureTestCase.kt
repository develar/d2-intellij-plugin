package org.jetbrains.plugins.d2

import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixture4TestCase
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.RegisterExtension

abstract class D2LightCodeInsightFixtureTestCase {
    @RegisterExtension
    private val testCase = object : LightPlatformCodeInsightFixture4TestCase(), BeforeEachCallback, AfterEachCallback {
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