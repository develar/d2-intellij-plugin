package org.jetbrains.plugins.d2

import com.intellij.testFramework.ParsingTestCase
import com.intellij.testFramework.rules.TestNameExtension
import com.intellij.testFramework.runInEdtAndWait
import org.jetbrains.plugins.d2.lang.D2ParserDefinition
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.RegisterExtension

abstract class D2ParsingTestCase() {
  init {
//    System.setProperty("idea.tests.overwrite.data", "true")
  }

  @RegisterExtension
  protected val testNameRule: TestNameExtension = TestNameExtension()

  @Suppress("JUnitMalformedDeclaration")
  @RegisterExtension
  private val testCase = object : ParsingTestCase("psi", "d2", D2ParserDefinition()), BeforeEachCallback, AfterEachCallback {
    override fun getTestDataPath() = "src/test/resources"

    override fun beforeEach(context: ExtensionContext?) {
      runInEdtAndWait {
        setUp()
      }
    }

    override fun afterEach(context: ExtensionContext?) {
      runInEdtAndWait {
        tearDown()
      }
    }

    fun test(text: String? = null) {
      test(text = text, checkResult = true, ensureNoErrorElements = false)
    }

    fun test(text: String? = null, checkResult: Boolean, ensureNoErrorElements: Boolean) {
      val name = testNameRule.methodName
      parseFile(/* name = */ name, /* text = */ text ?: loadFile("$name.$myFileExt"))
      if (checkResult) {
        doCheckResult(
          /* testDataDir = */ myFullDataPath,
          /* file = */ myFile,
          /* checkAllPsiRoots = */ true,
          /* targetDataName = */ name,
          /* skipSpaces = */ false,
          /* printRanges = */ false,
          /* allTreesInSingleFile = */ false
        )
        if (ensureNoErrorElements) {
          ensureNoErrorElements()
        }
      } else {
        toParseTreeText(myFile, skipSpaces(), includeRanges())
      }
    }
  }

  fun test(text: String? = null) {
    testCase.test(text)
  }
}