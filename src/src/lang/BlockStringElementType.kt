package org.jetbrains.plugins.d2.lang

import com.intellij.lang.ASTNode
import com.intellij.lang.LanguageParserDefinitions
import com.intellij.lang.PsiBuilderFactory
import com.intellij.openapi.application.ApplicationManager
import com.intellij.psi.PlainTextTokenTypes
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiPlainText
import com.intellij.psi.impl.source.tree.LazyParseablePsiElement
import com.intellij.psi.impl.source.tree.OwnBufferLeafPsiElement
import com.intellij.psi.tree.ILazyParseableElementType
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.plugins.markdown.injection.aliases.CodeFenceLanguageGuesser
import org.intellij.plugins.markdown.lang.MarkdownLanguage
import org.intellij.plugins.markdown.lang.parser.MarkdownParserAdapter
import org.intellij.plugins.markdown.lang.parser.MarkdownParserManager

private val gfmFlavourDescriptor = GFMFlavourDescriptor()

internal class BlockStringElementType(name: String) : ILazyParseableElementType(name, D2Language) {
    override fun parseContents(chameleon: ASTNode): ASTNode {
        val project = (chameleon.treeParent.psi ?: error("parent psi is null: $chameleon")).project

        val langNode = chameleon.treePrev?.takeIf { it.elementType == D2ElementTypes.BLOCK_STRING_LANG }
        val langId = langNode?.chars
        if (langId.isNullOrEmpty() || langId.contentEquals("md") || langId.contentEquals("markdown")) {
            val builder = PsiBuilderFactory.getInstance().createBuilder(
                project,
                chameleon,
                null,
                MarkdownLanguage.INSTANCE,
                chameleon.chars.subSequence(0, chameleon.textLength)
            )
            @Suppress("DEPRECATION")
            chameleon.psi.containingFile.putUserData(MarkdownParserManager.FLAVOUR_DESCRIPTION, gfmFlavourDescriptor)
            return MarkdownParserAdapter(gfmFlavourDescriptor).parse(this, builder)
        } else {
            val langIdString = langNode.text
            @Suppress("UnstableApiUsage") val languageForParser = runCatching {
                CodeFenceLanguageGuesser.guessLanguageForInjection(langIdString)
            }
                .onFailure {
                    // missing extension point in ParsingTestCase (a mock app)
                    if (!ApplicationManager.getApplication().isUnitTestMode) {
                        throw it
                    }
                }
                .getOrNull() ?: return UnknownLanguageBlockString(chameleon.chars)
            val builder = PsiBuilderFactory.getInstance()
                .createBuilder(project, chameleon, null, languageForParser, chameleon.chars)
            val parser = LanguageParserDefinitions.INSTANCE.forLanguage(languageForParser).createParser(project)
            val node = parser.parse(this, builder)
            return node.firstChildNode
        }
    }

    override fun getLanguageForParser(psi: PsiElement) = error("must not be called")

    override fun createNode(text: CharSequence?): ASTNode = LazyParseablePsiElement(this, text)
}

private class UnknownLanguageBlockString(text: CharSequence) :
    OwnBufferLeafPsiElement(PlainTextTokenTypes.PLAIN_TEXT, text), PsiPlainText {
    override fun accept(visitor: PsiElementVisitor) {
        visitor.visitPlainText(this)
    }

    override fun toString(): String = "PsiPlainText"
}