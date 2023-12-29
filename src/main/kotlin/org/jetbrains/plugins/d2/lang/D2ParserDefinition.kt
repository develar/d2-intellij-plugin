package org.jetbrains.plugins.d2.lang

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IFileElementType
import org.jetbrains.plugins.d2.file.D2File
import org.jetbrains.plugins.d2.lang.D2ElementTypes.*
import org.jetbrains.plugins.d2.lang.psi.*

private val D2_FILE = IFileElementType(D2Language)

class D2ParserDefinition : ParserDefinition {
  override fun createLexer(p: Project?) = createD2Lexer()

  override fun getCommentTokens() = D2TokenSets.COMMENT

  override fun getStringLiteralElements() = D2TokenSets.STRING

  override fun createParser(p: Project?) = D2Parser()

  override fun getFileNodeType() = D2_FILE

  override fun createFile(provider: FileViewProvider) = D2File(provider)

  override fun createElement(node: ASTNode): PsiElement {
    return when (node.elementType.index) {
      BLOCK_DEFINITION.index -> D2BlockDefinition(node)
      BLOCK_STRING.index -> D2BlockString(node)
      CONNECTOR.index -> D2Connector(node)
      PROPERTY.index -> D2Property(node)
      PROPERTY_KEY.index -> D2PropertyKey(node)

      SHAPE_LABEL.index -> ShapeLabel(node)
      SHAPE_CONNECTION.index -> D2ShapeConnection(node)

      SHAPE_ID.index -> ShapeId(node)

      UNQUOTED_STRING_VALUE.index -> UnquotedStringValue(node)
      STRING_VALUE.index -> StringValue(node)
      COLOR_VALUE.index -> ColorValue(node)
      OTHER_VALUE.index -> OtherValue(node)

      else -> throw AssertionError("Unknown element type: ${node.elementType}")
    }
  }
}
