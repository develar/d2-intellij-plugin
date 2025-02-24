package org.jetbrains.plugins.d2

import com.intellij.codeInsight.completion.CompletionType
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.plugins.d2.completion.directions
import org.jetbrains.plugins.d2.completion.shapes
import org.jetbrains.plugins.d2.completion.varsAndClasses
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

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
        assertThat(fixture.lookupElementStrings).hasSameElementsAs((SIMPLE_RESERVED_KEYWORDS + KEYWORD_HOLDERS).toList())
    }

    @Test
    fun direction() {
        fixture.configureByText(
            "test.d2",
            "direction: <caret>"
        )
        fixture.complete(CompletionType.BASIC)
        assertThat(fixture.lookupElementStrings).hasSameElementsAs(directions.map { it.lookupString })
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
        assertThat(fixture.lookupElementStrings).hasSameElementsAs((SIMPLE_RESERVED_KEYWORDS + KEYWORD_HOLDERS).toList() + varsAndClasses.map { it.lookupString })
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
        assertThat(fixture.lookupElementStrings).hasSameElementsAs(listOf("--", "->", "<-", "<->"))
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
        assertThat(fixture.lookupElementStrings).hasSameElementsAs(allShapeStyleKeywords())
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
        assertThat(fixture.lookupElementStrings).hasSameElementsAs(allShapeStyleKeywords())
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
        assertThat(fixture.lookupElementStrings).hasSameElementsAs(allShapeStyleKeywords())
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
        assertThat(fixture.lookupElementStrings).hasSameElementsAs(
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
        assertThat(complete("logs.style.<caret>")).hasSameElementsAs(allShapeStyleKeywords())
    }

    @Test
    fun `style font`() {
        assertThat(complete("logs.style.font: <caret>")).hasSameElementsAs(listOf("mono"))
    }

    @Test
    fun `style font with valid prefix`() {
        assertThat(complete("logs.style.font: mo<caret>")).isNull()
    }

    @Test
    fun shape() {
        assertThat(complete("user: User {shape: <caret>}")).hasSameElementsAs(shapes.map { it.lookupString })
    }

    private fun complete(content: String): List<String>? {
        fixture.configureByText("test.d2", content)
        fixture.complete(CompletionType.BASIC)
        return fixture.lookupElementStrings
    }
}

private fun allShapeStyleKeywords() = ShapeStyles.values().map { it.keyword }