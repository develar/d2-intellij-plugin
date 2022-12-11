package com.dvd.intellij.d2.ide.utils

import com.dvd.intellij.d2.ide.completion.BOOLEAN_COMPLETION
import com.dvd.intellij.d2.ide.completion.COLOR_COMPLETION
import com.dvd.intellij.d2.ide.completion.FONT_COMPLETION
import java.awt.Color

// https://github.com/terrastruct/d2/blob/eb55a49559fe933b3846094d8af01496ed55d037/d2graph/d2graph.go#L1061-L1087
enum class ShapeStyles(
  val keyword: String,
  val forShapes: Array<Shapes>,
  val validator: StyleValidator,
) {
  OPACITY("opacity", Shapes.values(), FloatRangeStyleValidator(0f, 1f)),
  STROKE("stroke", Shapes.values(), ColorStyleValidator),
  FILL("fill", Shapes.values(), ColorStyleValidator),
  STROKE_WIDTH("stroke-width", Shapes.values(), IntRangeStyleValidator(0, 15)),
  STROKE_DASH("stroke-dash", Shapes.values(), IntRangeStyleValidator(0, 10)),
  BORDER_RADIUS("border-radius", Shapes.values(), IntRangeStyleValidator(0, 20)),

  FONT("font", arrayOf(Shapes.TEXT), FontStyleValidator),
  FONT_SIZE("font-size", arrayOf(Shapes.TEXT), IntRangeStyleValidator(8, 100)),
  FONT_COLOR("font-color", arrayOf(Shapes.TEXT), ColorStyleValidator),
  BOLD("bold", arrayOf(Shapes.TEXT), BooleanStyleValidator),
  ITALIC("italic", arrayOf(Shapes.TEXT), BooleanStyleValidator),
  UNDERLINE("underline", arrayOf(Shapes.TEXT), BooleanStyleValidator),

  SHADOW("shadow", arrayOf(Shapes.SQUARE), BooleanStyleValidator),
  MULTIPLE("multiple", arrayOf(Shapes.SQUARE), BooleanStyleValidator),

  THREE_D("3d", arrayOf(Shapes.SQUARE), BooleanStyleValidator),

  // todo: check if edges = SQUARE, PARALLELOGRAM, DIAMOND, HEXAGON
  ANIMATED("animated", Shapes.EDGES, BooleanStyleValidator),
  FILLED("filled", Shapes.EDGES, BooleanStyleValidator),
  ;

  val completionElements = when (validator) {
    is BooleanStyleValidator -> BOOLEAN_COMPLETION
    is ColorStyleValidator -> COLOR_COMPLETION
    is FontStyleValidator -> FONT_COMPLETION
    else -> null
  }

  companion object {
    fun fromKeyword(keyword: String): ShapeStyles? = values().find { it.keyword == keyword }
  }
}

interface StyleValidator {
  val messageError: String

  fun accept(value: String): Boolean
}

class FloatRangeStyleValidator(
  private val from: Float,
  private val to: Float,
) : StyleValidator {
  override val messageError: String = "Expected %s to be a float between $from and $to"

  override fun accept(value: String): Boolean = value.toFloatOrNull()?.let { it in from..to } ?: false
}

class IntRangeStyleValidator(
  private val from: Int,
  private val to: Int,
) : StyleValidator {
  override val messageError: String = "Expected %s to be an integer between $from and $to"

  override fun accept(value: String): Boolean = value.toIntOrNull()?.let { it in from..to } ?: false
}

object ColorStyleValidator : StyleValidator {
  val COLOR_REGEX = "^#(([0-9a-fA-F]{2}){3}|([0-9a-fA-F]){3})\$".toRegex()

  @Suppress("SpellCheckingInspection")
  val NAMED_COLORS: Map<String, Color?> = mapOf(
    "transparent" to null,
    "aliceblue" to "#f0f8ff",
    "antiquewhite" to "#faebd7",
    "aqua" to "#00ffff",
    "aquamarine" to "#7fffd4",
    "azure" to "#f0ffff",
    "beige" to "#f5f5dc",
    "bisque" to "#ffe4c4",
    "black" to "#000000",
    "blanchedalmond" to "#ffebcd",
    "blue" to "#0000ff",
    "blueviolet" to "#8a2be2",
    "brown" to "#a52a2a",
    "burlywood" to "#deb887",
    "cadetblue" to "#5f9ea0",
    "chartreuse" to "#7fff00",
    "chocolate" to "#d2691e",
    "coral" to "#ff7f50",
    "cornflowerblue" to "#6495ed",
    "cornsilk" to "#fff8dc",
    "crimson" to "#dc143c",
    "cyan" to "#00ffff",
    "darkblue" to "#00008b",
    "darkcyan" to "#008b8b",
    "darkgoldenrod" to "#b8860b",
    "darkgray" to "#a9a9a9",
    "darkgrey" to "#a9a9a9",
    "darkgreen" to "#006400",
    "darkkhaki" to "#bdb76b",
    "darkmagenta" to "#8b008b",
    "darkolivegreen" to "#556b2f",
    "darkorange" to "#ff8c00",
    "darkorchid" to "#9932cc",
    "darkred" to "#8b0000",
    "darksalmon" to "#e9967a",
    "darkseagreen" to "#8fbc8f",
    "darkslateblue" to "#483d8b",
    "darkslategray" to "#2f4f4f",
    "darkslategrey" to "#2f4f4f",
    "darkturquoise" to "#00ced1",
    "darkviolet" to "#9400d3",
    "deeppink" to "#ff1493",
    "deepskyblue" to "#00bfff",
    "dimgray" to "#696969",
    "dimgrey" to "#696969",
    "dodgerblue" to "#1e90ff",
    "firebrick" to "#b22222",
    "floralwhite" to "#fffaf0",
    "forestgreen" to "#228b22",
    "fuchsia" to "#ff00ff",
    "gainsboro" to "#dcdcdc",
    "ghostwhite" to "#f8f8ff",
    "gold" to "#ffd700",
    "goldenrod" to "#daa520",
    "gray" to "#808080",
    "grey" to "#808080",
    "green" to "#008000",
    "greenyellow" to "#adff2f",
    "honeydew" to "#f0fff0",
    "hotpink" to "#ff69b4",
    "indianred" to "#cd5c5c",
    "indigo" to "#4b0082",
    "ivory" to "#fffff0",
    "khaki" to "#f0e68c",
    "lavender" to "#e6e6fa",
    "lavenderblush" to "#fff0f5",
    "lawngreen" to "#7cfc00",
    "lemonchiffon" to "#fffacd",
    "lightblue" to "#add8e6",
    "lightcoral" to "#f08080",
    "lightcyan" to "#e0ffff",
    "lightgoldenrodyellow" to "#fafad2",
    "lightgray" to "#d3d3d3",
    "lightgrey" to "#d3d3d3",
    "lightgreen" to "#90ee90",
    "lightpink" to "#ffb6c1",
    "lightsalmon" to "#ffa07a",
    "lightseagreen" to "#20b2aa",
    "lightskyblue" to "#87cefa",
    "lightslategray" to "#778899",
    "lightslategrey" to "#778899",
    "lightsteelblue" to "#b0c4de",
    "lightyellow" to "#ffffe0",
    "lime" to "#00ff00",
    "limegreen" to "#32cd32",
    "linen" to "#faf0e6",
    "magenta" to "#ff00ff",
    "maroon" to "#800000",
    "mediumaquamarine" to "#66cdaa",
    "mediumblue" to "#0000cd",
    "mediumorchid" to "#ba55d3",
    "mediumpurple" to "#9370db",
    "mediumseagreen" to "#3cb371",
    "mediumslateblue" to "#7b68ee",
    "mediumspringgreen" to "#00fa9a",
    "mediumturquoise" to "#48d1cc",
    "mediumvioletred" to "#c71585",
    "midnightblue" to "#191970",
    "mintcream" to "#f5fffa",
    "mistyrose" to "#ffe4e1",
    "moccasin" to "#ffe4b5",
    "navajowhite" to "#ffdead",
    "navy" to "#000080",
    "oldlace" to "#fdf5e6",
    "olive" to "#808000",
    "olivedrab" to "#6b8e23",
    "orange" to "#ffa500",
    "orangered" to "#ff4500",
    "orchid" to "#da70d6",
    "palegoldenrod" to "#eee8aa",
    "palegreen" to "#98fb98",
    "paleturquoise" to "#afeeee",
    "palevioletred" to "#db7093",
    "papayawhip" to "#ffefd5",
    "peachpuff" to "#ffdab9",
    "peru" to "#cd853f",
    "pink" to "#ffc0cb",
    "plum" to "#dda0dd",
    "powderblue" to "#b0e0e6",
    "purple" to "#800080",
    "rebeccapurple" to "#663399",
    "red" to "#ff0000",
    "rosybrown" to "#bc8f8f",
    "royalblue" to "#4169e1",
    "saddlebrown" to "#8b4513",
    "salmon" to "#fa8072",
    "sandybrown" to "#f4a460",
    "seagreen" to "#2e8b57",
    "seashell" to "#fff5ee",
    "sienna" to "#a0522d",
    "silver" to "#c0c0c0",
    "skyblue" to "#87ceeb",
    "slateblue" to "#6a5acd",
    "slategray" to "#708090",
    "slategrey" to "#708090",
    "snow" to "#fffafa",
    "springgreen" to "#00ff7f",
    "steelblue" to "#4682b4",
    "tan" to "#d2b48c",
    "teal" to "#008080",
    "thistle" to "#d8bfd8",
    "tomato" to "#ff6347",
    "turquoise" to "#40e0d0",
    "violet" to "#ee82ee",
    "wheat" to "#f5deb3",
    "white" to "#ffffff",
    "whitesmoke" to "#f5f5f5",
    "yellow" to "#ffff00",
    "yellowgreen" to "#9acd32",
  ).mapValues { (_, hex) ->
    if (hex != null) Color.decode(hex)
    else null
  }

  // todo: add all valid colors to string? maybe too long
  override val messageError: String = "Expected %s to be a valid named color or a HEX code"

  // todo: build color chooser
  override fun accept(value: String): Boolean = value in NAMED_COLORS.keys || value.matches(COLOR_REGEX)
}

object BooleanStyleValidator : StyleValidator {
  override val messageError: String = "Expected %s to be true or false"

  override fun accept(value: String): Boolean = value == "true" || value == "false"
}

object FontStyleValidator : StyleValidator {
  val SYSTEM_FONTS = arrayOf(
    "DEFAULT",
    "SERIOUS",
    "DIGITAL",
    "EDUCATIONAL",
    "NEWSPAPER",
    "MONO",
  )

  override val messageError: String = buildString {
    append("Expected %s to be a valid font: (")
    append(SYSTEM_FONTS.joinToString(", "))
    append(")")
  }

  override fun accept(value: String): Boolean = value in SYSTEM_FONTS
}
