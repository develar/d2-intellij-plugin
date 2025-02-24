@file:Suppress("ReplaceJavaStaticMethodWithKotlinAnalog")

package org.jetbrains.plugins.d2

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.tree.IElementType
import org.jetbrains.plugins.d2.lang.D2ElementTypes

// https://github.com/terrastruct/d2/blob/eb55a49559fe933b3846094d8af01496ed55d037/d2graph/d2graph.go#L1061-L1087
internal enum class ShapeStyles(
    val keyword: String,
    val validator: StyleValidator,
) {
    OPACITY("opacity", FloatRangeStyleValidator(0f, 1f)),

    FILL("fill", ColorStyleValidator),
    FILL_PATTERN(
        "fill-pattern",
        TextStyleValidator(java.util.Set.of("dots", "lines", "grain"))
    ),

    STROKE("stroke", ColorStyleValidator),
    STROKE_WIDTH("stroke-width", IntRangeStyleValidator(0, 15)),
    STROKE_DASH("stroke-dash", IntRangeStyleValidator(0, 10)),

    BORDER_RADIUS("border-radius", IntRangeStyleValidator(0, 20)),

    // only for text
    FONT("font", FontStyleValidator),
    FONT_SIZE("font-size", IntRangeStyleValidator(8, 100)),
    FONT_COLOR("font-color", ColorStyleValidator),
    BOLD("bold", BooleanStyleValidator),
    ITALIC("italic", BooleanStyleValidator),
    UNDERLINE("underline", BooleanStyleValidator),
    TEXT_TRANSFORM(
        "text-transform",
        TextStyleValidator(java.util.Set.of("uppercase", "lowercase", "title", "none"))
    ),

    SHADOW("shadow", BooleanStyleValidator),
    MULTIPLE("multiple", BooleanStyleValidator),
    DOUBLE_BORDER("double-border", BooleanStyleValidator),

    THREE_D("3d", BooleanStyleValidator),

    // todo: only for connections
    ANIMATED("animated", BooleanStyleValidator),
    FILLED("filled", BooleanStyleValidator),
    ;

    val completionElements: List<LookupElementBuilder>?
        get() = validator.completionElements

    companion object {
        fun fromKeyword(keyword: String): ShapeStyles? = entries.find { it.keyword == keyword }
    }
}

sealed interface StyleValidator {
    val completionElements: List<LookupElementBuilder>?

    val messageError: String

    fun accept(value: String): Boolean

    // todo not used (see comment in styleCompletion)
    fun isAcceptableValueType(elementType: IElementType): Boolean
}

class FloatRangeStyleValidator(
    private val from: Float,
    private val to: Float,
) : StyleValidator {
    override val completionElements: List<LookupElementBuilder>?
        get() = null

    override val messageError: String = "Expected %s to be a float between $from and $to"

    override fun accept(value: String): Boolean = value.toFloatOrNull()?.let { it in from..to } ?: false

    override fun isAcceptableValueType(elementType: IElementType) =
        elementType == D2ElementTypes.FLOAT || elementType == D2ElementTypes.INT
}

class IntRangeStyleValidator(
    private val from: Int,
    private val to: Int,
) : StyleValidator {
    override val completionElements: List<LookupElementBuilder>?
        get() = null

    override val messageError: String
        get() = "Expected %s to be an integer between $from and $to"

    override fun accept(value: String): Boolean = value.toIntOrNull()?.let { it in from..to } ?: false

    override fun isAcceptableValueType(elementType: IElementType) = elementType == D2ElementTypes.INT
}

private data object BooleanStyleValidator : StyleValidator {
    override val completionElements = sequenceOf(true, false).map { LookupElementBuilder.create(it).bold() }.toList()

    override val messageError: String = "Expected %s to be true or false"

    override fun accept(value: String): Boolean = value == "true" || value == "false"

    override fun isAcceptableValueType(elementType: IElementType) =
        elementType == D2ElementTypes.TRUE || elementType == D2ElementTypes.FALSE
}

private data object FontStyleValidator : StyleValidator {
    private val SYSTEM_FONTS = arrayOf(
        "mono",
    )

    override val completionElements = SYSTEM_FONTS.map { LookupElementBuilder.create(it) }

    override val messageError: String = buildString {
        append("Expected %s to be a valid font: (")
        append(SYSTEM_FONTS.joinToString(", "))
        append(")")
    }

    override fun accept(value: String): Boolean = value in SYSTEM_FONTS

    override fun isAcceptableValueType(elementType: IElementType) =
        elementType == D2ElementTypes.STRING || elementType == D2ElementTypes.UNQUOTED_STRING
}

private class TextStyleValidator(private val variants: Set<String>) : StyleValidator {
    override val completionElements = variants.map { LookupElementBuilder.create(it) }

    override val messageError: String
        get() = "Expected %s to be one of (${variants.joinToString(", ")})"

    override fun accept(value: String): Boolean = variants.contains(value)

    override fun isAcceptableValueType(elementType: IElementType) =
        elementType == D2ElementTypes.STRING || elementType == D2ElementTypes.UNQUOTED_STRING
}
