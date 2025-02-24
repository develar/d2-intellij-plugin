package org.jetbrains.plugins.d2

import com.intellij.psi.PsiFile
import com.intellij.psi.impl.DebugUtil
import com.intellij.testFramework.ParsingTestCase
import com.intellij.testFramework.ParsingTestUtil
import com.intellij.testFramework.rules.TestNameExtension
import com.intellij.testFramework.runInEdtAndWait
import org.assertj.core.api.Assertions.assertThat
import org.intellij.lang.annotations.Language
import org.intellij.plugins.markdown.lang.parser.MarkdownParserDefinition
import org.jetbrains.plugins.d2.lang.D2ParserDefinition
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.RegisterExtension
import org.opentest4j.AssertionFailedError
import org.opentest4j.FileInfo
import java.nio.file.Files
import java.nio.file.Path

abstract class D2ParsingTestCase {
    @RegisterExtension
    protected val testNameRule: TestNameExtension = TestNameExtension()

    @Suppress("JUnitMalformedDeclaration")
    @RegisterExtension
    private val testCase =
        object : ParsingTestCase("psi", "d2", D2ParserDefinition(), MarkdownParserDefinition()), BeforeEachCallback,
            AfterEachCallback {
            override fun getTestDataPath() = "src/testResources"

            private val testDataDir = Path.of(myFullDataPath)

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
                test(text = text, checkResult = true, ensureNoErrorElements = true)
            }

            fun test(text: String? = null, checkResult: Boolean, ensureNoErrorElements: Boolean) {
                val name = testNameRule.methodName
                parseFile(/* name = */ name, /* text = */ text ?: loadFile("$name.$myFileExt"))
                val file = myFile
                if (checkResult) {
                    checkParseResult(testDataDir = testDataDir, file = file, targetDataName = name)
                    if (ensureNoErrorElements) {
                        ParsingTestUtil.assertNoPsiErrorElements(file)
                    }
                } else {
                    toParseTreeText(file, skipSpaces(), includeRanges())
                }
            }
        }

    fun test(@Language("D2") text: String? = null, checkResult: Boolean = true, ensureNoErrorElements: Boolean = true) {
        testCase.test(text?.trimIndent(), checkResult, ensureNoErrorElements)
    }
}

private fun checkParseResult(testDataDir: Path, file: PsiFile, targetDataName: String) {
    val provider = file.viewProvider
    val languages = provider.languages

    if (languages.size == 1) {
        checkResult(testDataDir = testDataDir, targetDataName = "$targetDataName.txt", root = file)
        return
    }

    for (language in languages) {
        val root = provider.getPsi(language)
        assertThat(root).describedAs("FileViewProvider $provider didn't return PSI root for language ${language.id}")
            .isNotNull()
        val expectedName = "$targetDataName.${language.id}.txt"
        checkResult(testDataDir = testDataDir, targetDataName = expectedName, root = root)
    }
}

fun checkResult(testDataDir: Path, targetDataName: String, root: PsiFile) {
    val expectedFile = testDataDir.resolve(targetDataName).toAbsolutePath()
    val actual = psiToString(root)
    val isGoodTool = System.getenv("XPC_SERVICE_NAME")?.contains("intellij") ?: false
    val expectedString = Files.readString(expectedFile).trim()
    if (isGoodTool) {
        if (expectedString != actual) {
            if (System.getProperty("idea.tests.overwrite.data")?.toBoolean() == true) {
                Files.writeString(expectedFile, actual)
                return
            }

            throw AssertionFailedError(
                /* message = */ "$targetDataName is not equal to expected",
                /* expected = */ FileInfo(expectedFile.toString(), expectedString.encodeToByteArray()),
                /* actual = */ actual,
            )
        }
        return
    }

    // *** gradle doesn't support correct presentation of assertion in console, use diff as a message
    try {
        assertThat(expectedFile).hasContent(actual)
    } catch (e: AssertionError) {
        // AssertJ hasContent doesn't support FileInfo
        throw AssertionFailedError(
            /* message = */ e.message,
            /* expected = */ FileInfo(expectedFile.toString(), expectedString.encodeToByteArray()),
            /* actual = */ actual,
        )
    }
}

private fun psiToString(file: PsiFile): String {
    return DebugUtil.psiToString(/* root = */ file, /* showWhitespaces = */ true, /* showRanges = */ false).trim()
}