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
    return when (node.elementType) {
      BLOCK_DEFINITION -> BlockDefinition(node)
      BLOCK_STRING -> BlockString(node)
      CONNECTOR -> Connector(node)
      PROPERTY -> Property(node)
      PROPERTY_KEY -> PropertyKey(node)
      PROPERTY_MAP -> PropertyMap(node)
      ARRAY -> D2Array(node)

      SHAPE_DECLARATION -> ShapeDeclaration(node)
      SHAPE_CONNECTION -> ShapeConnection(node)

      SHAPE_ID -> ShapeId(node)

      UNQUOTED_STRING_VALUE -> UnquotedStringValue(node)
      STRING_VALUE -> StringValue(node)
      COLOR_VALUE -> ColorValue(node)
      OTHER_VALUE -> OtherValue(node)

      else -> throw AssertionError("Unknown element type: ${node.elementType}")
    }
  }
}
