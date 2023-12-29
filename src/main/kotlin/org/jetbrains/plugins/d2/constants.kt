package org.jetbrains.plugins.d2

internal const val D2_EDITOR_NAME = "D2FileEditor"
internal const val D2_NOTIFICATION_GROUP = "D2_INFO"

val SIMPLE_RESERVED_KEYWORDS: Array<String> = arrayOf(
  "label",
  "desc",
  "shape",
  "icon",
  "constraint",
  "tooltip",
  "link",
  "near",
  "width",
  "height",
  "direction",
  "top",
  "left",
  "grid-rows",
  "grid-columns",
  "grid-gap",
  "vertical-gap",
  "horizontal-gap",
  "class",
)

// https://github.com/terrastruct/d2/blob/eb55a49559fe933b3846094d8af01496ed55d037/d2graph/d2graph.go#L1054-L1057
internal val KEYWORD_HOLDERS = arrayOf(
  "style",
  "source-arrowhead",
  "target-arrowhead",
)

enum class Shapes(val value: String) {
  RECTANGLE("rectangle"),
  SQUARE("square"),
  PAGE("page"),
  PARALLELOGRAM("parallelogram"),
  DOCUMENT("document"),
  CYLINDER("cylinder"),
  QUEUE("queue"),
  PACKAGE("package"),
  STEP("step"),
  CALLOUT("callout"),
  STORED_DATA("stored_data"),
  PERSON("person"),
  DIAMOND("diamond"),
  OVAL("oval"),
  CIRCLE("circle"),
  HEXAGON("hexagon"),
  CLOUD("cloud"),
  TEXT("text"),
  CODE("code"),
  CLASS("class"),
  SQL_TABLE("sql_table"),
  IMAGE("image"),
  ;

  val prettyName = value.replace("_", " ")
}

internal val ARROWHEADS = arrayOf(
  "none",
  "arrow",
  "triangle",
  "diamond",
  "filled-diamond"
// line ??
)
