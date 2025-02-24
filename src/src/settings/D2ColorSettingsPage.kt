package org.jetbrains.plugins.d2.settings

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import org.jetbrains.plugins.d2.D2Bundle
import org.jetbrains.plugins.d2.D2Icons
import org.jetbrains.plugins.d2.lang.D2Language
import org.jetbrains.plugins.d2.lang.D2SyntaxHighlighter
import javax.swing.Icon

private class D2ColorSettingsPage : ColorSettingsPage {
    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    override fun getDisplayName(): String = D2Language.id

    override fun getIcon(): Icon = D2Icons.FILE

    override fun getHighlighter(): SyntaxHighlighter = D2SyntaxHighlighter()

    override fun getAdditionalHighlightingTagToDescriptorMap(): MutableMap<String, TextAttributesKey>? = null

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = attributeDescriptors {
        descriptor {
            displayName = D2Bundle.messagePointer("descriptor.comment")
            attrKey = D2SyntaxHighlighter.COMMENT
        }
        descriptors(D2Bundle.messagePointer("descriptors.braces_operators")) {
            descriptor {
                displayName = D2Bundle.messagePointer("descriptor.arrows")
                attrKey = D2SyntaxHighlighter.ARROWS
            }
            descriptor {
                displayName = D2Bundle.messagePointer("descriptor.dot")
                attrKey = D2SyntaxHighlighter.DOT
            }
            descriptor {
                displayName = D2Bundle.messagePointer("descriptor.colon")
                attrKey = D2SyntaxHighlighter.COLON
            }
            descriptor {
                displayName = D2Bundle.messagePointer("descriptor.semicolon")
                attrKey = D2SyntaxHighlighter.SEMICOLON
            }
            descriptor {
                displayName = D2Bundle.messagePointer("descriptor.braces")
                attrKey = D2SyntaxHighlighter.BRACES
            }
        }
        descriptors(D2Bundle.messagePointer("descriptors.identifiers")) {
            descriptor {
                displayName = D2Bundle.messagePointer("descriptor.identifier.default")
                attrKey = D2SyntaxHighlighter.IDENTIFIERS
            }
            descriptor {
                displayName = D2Bundle.messagePointer("descriptor.identifier.instance_field")
                attrKey = D2SyntaxHighlighter.FIELDS
            }
        }
        descriptor {
            displayName = D2Bundle.messagePointer("descriptor.keywords")
            attrKey = D2SyntaxHighlighter.KEYWORDS
        }
        descriptor {
            displayName = D2Bundle.messagePointer("descriptor.strings")
            attrKey = D2SyntaxHighlighter.STRING
        }
        descriptor {
            displayName = D2Bundle.messagePointer("descriptor.numbers")
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