package org.jetbrains.plugins.d2.settings

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.options.colors.AttributesDescriptor
import java.util.function.Supplier

fun attributeDescriptors(block: AttributeDescriptorsBuilder.() -> Unit): Array<AttributesDescriptor> {
    return AttributeDescriptorsBuilder().apply(block).build()
}

@DslMarker
annotation class DescriptorDsl

@DescriptorDsl
class AttributeDescriptorsBuilder {
    private val attrs = mutableListOf<AttributesDescriptor>()

    fun descriptor(block: DescriptorBuilder.() -> Unit) = attrs.add(DescriptorBuilder().apply(block).build())
    fun descriptors(displayName: Supplier<String>, block: DESCRIPTORS.() -> Unit): Boolean {
        return attrs.addAll(DESCRIPTORS(displayName).apply(block))
    }

    fun build(): Array<AttributesDescriptor> = attrs.toTypedArray()
}

@DescriptorDsl
class DESCRIPTORS(private val parentDisplayName: Supplier<String>) : ArrayList<AttributesDescriptor>() {
    fun descriptor(block: DescriptorBuilder.() -> Unit) = add(DescriptorBuilder(parentDisplayName).apply(block).build())
}

@DescriptorDsl
class DescriptorBuilder(private val parentDisplayName: Supplier<String> = Supplier { "" }) {
    lateinit var displayName: Supplier<String>
    lateinit var attrKey: TextAttributesKey

    fun build(): AttributesDescriptor {
        return AttributesDescriptor(
            {
                val parentDisplayName = parentDisplayName.get()
                if (parentDisplayName.isEmpty()) displayName.get() else "$parentDisplayName//$displayName"
            },
            attrKey
        )
    }
}