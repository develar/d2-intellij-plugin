package com.dvd.intellij.d2.ide.utils

internal const val D2_TOOLWINDOW_ID = "D2ToolWindow"
internal const val D2_EDITOR_NAME = "D2FileEditor"
internal const val D2_EDITOR_ID = "D2Editor"

internal const val D2_GROUP_TOOLBAR = "D2.EditorToolbar"
internal const val D2_GROUP_POPUP = "D2.EditorPopupMenu"
internal const val D2_ACTION_PLACE = "D2.Editor"

// https://github.com/terrastruct/d2/blob/eb55a49559fe933b3846094d8af01496ed55d037/d2graph/d2graph.go#L1040-L1050
internal val RESERVED_KEYWORDS = arrayOf(
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

  companion object {
    val EDGES = arrayOf(SQUARE, RECTANGLE, PARALLELOGRAM, DIAMOND, HEXAGON)
  }

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
