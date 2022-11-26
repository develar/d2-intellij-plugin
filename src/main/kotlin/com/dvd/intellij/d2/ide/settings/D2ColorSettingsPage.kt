package com.dvd.intellij.d2.ide.settings

import com.dvd.intellij.d2.ide.lang.D2Language
import com.dvd.intellij.d2.ide.lang.D2SyntaxHighlighter
import com.dvd.intellij.d2.ide.utils.D2Bundle
import com.dvd.intellij.d2.ide.utils.D2Icons
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import javax.swing.Icon

class D2ColorSettingsPage : ColorSettingsPage {
  override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY
  override fun getDisplayName(): String = D2Language.id
  override fun getIcon(): Icon = D2Icons.FILE
  override fun getHighlighter(): SyntaxHighlighter = D2SyntaxHighlighter()
  override fun getAdditionalHighlightingTagToDescriptorMap(): MutableMap<String, TextAttributesKey>? = null

  override fun getAttributeDescriptors(): Array<AttributesDescriptor> = attributesDescriptors {
    descriptor {
      displayName = D2Bundle["descriptor.comment"]
      attrKey = D2SyntaxHighlighter.COMMENT
    }
    descriptors(D2Bundle["descriptors.braces_operators"]) {
      descriptor {
        displayName = D2Bundle["descriptor.arrows"]
        attrKey = D2SyntaxHighlighter.ARROWS
      }
      descriptor {
        displayName = D2Bundle["descriptor.dot"]
        attrKey = D2SyntaxHighlighter.DOT
      }
      descriptor {
        displayName = D2Bundle["descriptor.colon"]
        attrKey = D2SyntaxHighlighter.COLON
      }
      descriptor {
        displayName = D2Bundle["descriptor.semicolon"]
        attrKey = D2SyntaxHighlighter.SEMICOLON
      }
      descriptor {
        displayName = D2Bundle["descriptor.braces"]
        attrKey = D2SyntaxHighlighter.BRACES
      }
    }
    descriptors(D2Bundle["descriptors.identifiers"]) {
      descriptor {
        displayName = D2Bundle["descriptor.identifier.default"]
        attrKey = D2SyntaxHighlighter.IDENTIFIERS
      }
      descriptor {
        displayName = D2Bundle["descriptor.identifier.instance_field"]
        attrKey = D2SyntaxHighlighter.FIELDS
      }
    }
    descriptor {
      displayName = D2Bundle["descriptor.keywords"]
      attrKey = D2SyntaxHighlighter.KEYWORDS
    }
    descriptor {
      displayName = D2Bundle["descriptor.strings"]
      attrKey = D2SyntaxHighlighter.STRING
    }
    descriptor {
      displayName = D2Bundle["descriptor.numbers"]
      attrKey = D2SyntaxHighlighter.NUMBERS
    }
  }

  // todo proper formatting
  override fun getDemoText(): String = """
    # This is a comment
    a -> b: connection
    
    a.shape: circle
    
    b: * {
        source-arrowhead: 1
        target-arrowhead: * {
            shape: diamond
            style.filled: true
        }
    }
    
    c: label
    d: "another label"
    
    c -> d <- e: triple connection
  """.trimIndent()
}