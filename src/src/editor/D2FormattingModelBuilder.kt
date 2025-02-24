package org.jetbrains.plugins.d2.editor

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.formatter.FormatterUtil.isWhitespaceOrEmpty
import org.jetbrains.plugins.d2.lang.D2ElementTypes
import org.jetbrains.plugins.d2.lang.D2Language
import org.jetbrains.plugins.d2.lang.D2TokenSets

private class D2FormattingModelBuilder : FormattingModelBuilder {
    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val settings = formattingContext.codeStyleSettings
        val spacingBuilder = createSpacingBuilder(settings)
        val block = D2Block(
            node = formattingContext.node,
            alignment = null,
            indent = Indent.getSmartIndent(Indent.Type.CONTINUATION),
            wrap = null,
            spacingBuilder = spacingBuilder,
        )
        return FormattingModelProvider.createFormattingModelForPsiFile(
            formattingContext.containingFile,
            block,
            settings
        )
    }
}

private fun createSpacingBuilder(settings: CodeStyleSettings): SpacingBuilder {
    val commonSettings = settings.getCommonSettings(D2Language)

    val spaceCountBeforeColon = 0
    val spaceCountAfterColon = 1

    return SpacingBuilder(settings, D2Language)
        .before(D2ElementTypes.COLON).spacing(spaceCountBeforeColon, spaceCountBeforeColon, 0, false, 0)
        .after(D2ElementTypes.COLON).spacing(spaceCountAfterColon, spaceCountAfterColon, 0, false, 0)
        .withinPair(D2ElementTypes.LBRACE, D2ElementTypes.RBRACE).spaceIf(commonSettings.SPACE_WITHIN_BRACKETS, true)
        .before(D2ElementTypes.SEMICOLON).spacing(0, 0, 0, false, 0)
        .after(D2ElementTypes.SEMICOLON).spacing(1, 1, 0, false, 0)
}

private class D2Block(
    private val node: ASTNode,
    private val alignment: Alignment?,
    private val indent: Indent,
    private val wrap: Wrap?,
    private val spacingBuilder: SpacingBuilder
) : ASTBlock {
    // lazy initialized on first call to #getSubBlocks()
    private var subBlocks: MutableList<Block>? = null

    private var childWrap: Wrap? = null

    init {
        childWrap = if (node.elementType == D2ElementTypes.BLOCK_DEFINITION) {
            Wrap.createWrap(CommonCodeStyleSettings.WRAP_ALWAYS, true)
        } else {
            null
        }
    }

    override fun getNode() = node

    override fun getTextRange(): TextRange = node.textRange

    override fun getSubBlocks(): List<Block> {
        if (subBlocks == null) {
            val children = node.getChildren(null)
            subBlocks = ArrayList(children.size)
            for (child in children) {
                if (isWhitespaceOrEmpty(child)) {
                    continue
                }
                subBlocks!!.add(makeSubBlock(child))
            }
        }
        return subBlocks!!
    }

    private fun makeSubBlock(childNode: ASTNode): Block {
        var indent = Indent.getNoneIndent()
        var wrap: Wrap? = null
        // not-null means that this block node is D2 block
        if (childWrap != null) {
            val childElementType = childNode.elementType
            if (childElementType == D2ElementTypes.SEMICOLON) {
                wrap = Wrap.createWrap(WrapType.NONE, true)
            } else if (childElementType != D2ElementTypes.LBRACE && childElementType != D2ElementTypes.RBRACE) {
                wrap = childWrap
                indent = Indent.getNormalIndent()
            }
        }
        return D2Block(
            node = childNode,
            alignment = alignment,
            indent = indent,
            wrap = wrap,
            spacingBuilder = spacingBuilder
        )
    }

    override fun getWrap(): Wrap? = wrap

    override fun getIndent() = indent

    override fun getAlignment(): Alignment? = alignment

    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        return spacingBuilder.getSpacing(/* parent = */ this, /* child1 = */ child1, /* child2 = */ child2)
    }

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
        if (node.elementType == D2ElementTypes.BLOCK_DEFINITION) {
            return ChildAttributes(Indent.getNormalIndent(), null)
        } else if (node.psi is PsiFile) {
            return ChildAttributes(Indent.getNoneIndent(), null)
        }
        return ChildAttributes(null, null)
    }

    override fun isIncomplete(): Boolean {
        val lastChildNode = node.lastChildNode
        val elementType = node.elementType
        when (elementType) {
            D2ElementTypes.BLOCK_DEFINITION -> {
                return lastChildNode != null && lastChildNode.elementType !== D2ElementTypes.RBRACE
            }

            D2ElementTypes.SHAPE_PROPERTY -> {
                var child: ASTNode? = node.firstChildNode
                while (child != null) {
                    if (D2TokenSets.PROPERTY_VALUE.contains(child.elementType)) {
                        return false
                    }
                    child = child.treeNext
                }
                return true
            }

            else -> {
                return false
            }
        }
    }

    override fun isLeaf(): Boolean = node.firstChildNode == null
}