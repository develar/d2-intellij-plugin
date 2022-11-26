package com.dvd.intellij.d2.ide.settings

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.options.colors.AttributesDescriptor

fun attributesDescriptors(block: AttributesDescriptorsBuilder.() -> Unit): Array<AttributesDescriptor> =
  AttributesDescriptorsBuilder().apply(block).build()

@DslMarker
annotation class DescriptorDsl

@DescriptorDsl
class AttributesDescriptorsBuilder {
  private val attrs = mutableListOf<AttributesDescriptor>()

  fun descriptor(block: DescriptorBuilder.() -> Unit) = attrs.add(DescriptorBuilder().apply(block).build())
  fun descriptors(displayName: String, block: DESCRIPTORS.() -> Unit) =
    attrs.addAll(DESCRIPTORS(displayName).apply(block))

  fun build(): Array<AttributesDescriptor> = attrs.toTypedArray()
}

@DescriptorDsl
class DESCRIPTORS(private val parentDisplayName: String) : ArrayList<AttributesDescriptor>() {
  fun descriptor(block: DescriptorBuilder.() -> Unit) = add(DescriptorBuilder(parentDisplayName).apply(block).build())
}

@DescriptorDsl
class DescriptorBuilder(private val parentDisplayName: String = "") {
  lateinit var displayName: String
  lateinit var attrKey: TextAttributesKey

  fun build() = AttributesDescriptor(
    if (parentDisplayName.isEmpty()) displayName else "$parentDisplayName//$displayName",
    attrKey
  )
}