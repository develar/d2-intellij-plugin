package org.jetbrains.plugins.d2

import com.intellij.testFramework.ParsingTestCase
import com.intellij.testFramework.rules.TestNameExtension
import org.jetbrains.plugins.d2.lang.D2ParserDefinition
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.RegisterExtension

class D2ParsingTest : D2ParsingTestCase() {
  @Test
  fun parsing() {
    test()
  }
}

abstract class D2ParsingTestCase() {
  @RegisterExtension
  protected val testNameRule: TestNameExtension = TestNameExtension()

  @Suppress("JUnitMalformedDeclaration")
  @RegisterExtension
  private val testCase = object : ParsingTestCase("psi", "d2", D2ParserDefinition()), BeforeEachCallback, AfterEachCallback {
    override fun getTestDataPath() = "src/test/resources"

    override fun beforeEach(context: ExtensionContext?) {
      setUp()
    }

    override fun afterEach(context: ExtensionContext?) {
      tearDown()
    }

    fun test() {
      test(checkResult = true, ensureNoErrorElements = false)
    }

    fun test(checkResult: Boolean, ensureNoErrorElements: Boolean) {
      val name = testNameRule.methodName
      parseFile(name, loadFile("$name.$myFileExt"))
      if (checkResult) {
        checkResult(name, myFile)
        if (ensureNoErrorElements) {
          ensureNoErrorElements()
        }
      } else {
        toParseTreeText(myFile, skipSpaces(), includeRanges())
      }
    }
  }

  fun test() {
    testCase.test()
  }
}
