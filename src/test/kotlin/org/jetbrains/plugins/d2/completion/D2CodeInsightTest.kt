package org.jetbrains.plugins.d2.completion

import com.intellij.codeInsight.completion.CompletionType
import com.intellij.testFramework.TestDataPath
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase5
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@TestDataPath("\$CONTENT_ROOT/resources/completion")
class D2CodeInsightTest : LightJavaCodeInsightFixtureTestCase5() {
  @Test
  fun `block completion`() {
    //language=d2
    fixture.configureByFiles(
      """
        s: {
          <caret>
        }
      """
    )
    fixture.complete(CompletionType.BASIC)
    val lookupElementStrings = fixture.lookupElementStrings
    assertThat(lookupElementStrings).isNotNull()
    assertThat(lookupElementStrings).containsExactlyInAnyOrder("")
  }
}