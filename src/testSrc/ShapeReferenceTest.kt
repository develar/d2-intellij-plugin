package org.jetbrains.plugins.d2

import com.intellij.openapi.application.readAction
import com.intellij.psi.PsiReference
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.plugins.d2.lang.psi.ShapeDeclaration
import org.jetbrains.plugins.d2.lang.psi.ShapeId
import org.jetbrains.plugins.d2.lang.psi.buildFQN
import org.junit.jupiter.api.Test

class ShapeReferenceTest : D2LightCodeInsightFixtureTestCase() {
    @Test
    fun `no ref for declaration`() {
        test(
            """
      christmas: {
        presents
      }
      birthdays: {
        <caret>presents
        _.christmas.presents -> presents: regift
        _.christmas.style.fill: "#ACE1AF"
      }      
    """
        ) {
            assertNoRef()
        }
    }

    @Test
    fun `ref from qualified reference`() {
        test(
            """
      christmas: {
        presents
      }
      birthdays: {
        presents
        _.<caret>christmas.presents -> presents: regift
        _.christmas.style.fill: "#ACE1AF"
      }      
    """
        ) {
            assertRefToShapeWithLabel("christmas")
        }
    }

    @Test
    fun `ref to local ambiguous`() {
        test(
            """
      christmas: {
        presents
      }
      birthdays: {
        presents
        _.christmas.presents -> <caret>presents: regift
      }      
    """
        ) {
            assertRefToShapeWithFQN(listOf("birthdays", "presents"))
        }
    }

    @Test
    fun `ref to nonlocal ambiguous`() {
        test(
            """
      christmas: {
        presents
      }
      birthdays: {
        presents
        _.christmas.<caret>presents -> presents: regift
      }      
    """
        ) {
            assertRefToShapeWithFQN(listOf("christmas", "presents"))
        }
    }

    @Test
    fun `parent ref level 1`() {
        test(
            """
        christmas: {
          presents
        }
        
        birthdays: {
          presents
          _<caret>.christmas.presents -> presents: regift
        }
    """
        ) {
            assertNoRef()
        }
    }

    @Test
    fun `parent ref level 2`() {
        test(
            """
        christmas: {
          presents
        }
        
        birthdays: {
          presents
          _.christmas.presents -> presents: regift
        
          level2: {
            <caret>_.presents.style.fill: "#296e2d"
          }
        }
    """
        ) {
            assertRefToShapeWithLabel("birthdays")
        }
    }
}

private class RefAssertions(private val fixture: CodeInsightTestFixture) {
    fun assertNoRef() {
        assertThat(fixture.file.findReferenceAt(fixture.editor.caretModel.offset)).isNull()
    }

    fun assertRefToShapeWithLabel(label: String) {
        val ref = fixture.file.findReferenceAt(fixture.editor.caretModel.offset)
        val shapeId = (ref as PsiReference).resolve() as ShapeId
        assertThat(shapeId.text).isEqualTo(label)
        assertThat((shapeId.parent as ShapeDeclaration).findId()!!.text).isEqualTo(label)
    }

    fun assertRefToShapeWithFQN(fqn: List<String>) {
        val ref = fixture.file.findReferenceAt(fixture.editor.caretModel.offset)
        val shapeId = (ref as PsiReference).resolve() as ShapeId
        assertThat(shapeId.text).isEqualTo(fqn.last())
        assertThat(buildFQN(shapeId).map { it.name }.asReversed()).isEqualTo(fqn)
    }
}

private fun D2LightCodeInsightFixtureTestCase.test(content: String, test: RefAssertions.() -> Unit) {
    fixture.configureByText("test.d2", content.trimIndent())
    runBlocking {
        readAction {
            test(RefAssertions(fixture))
        }
    }
}