package com.dvd.intellij.d2.ide.editor.images

import com.dvd.intellij.d2.components.D2Layout
import com.dvd.intellij.d2.components.D2Theme
import com.dvd.intellij.d2.ide.utils.D2_EDITOR_ID
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.fileEditor.FileEditorStateLevel
import com.intellij.openapi.fileEditor.TransferableFileEditorState
import java.io.Serializable

private const val BACKGROUND_VISIBLE_OPTION = "backgroundVisible"
private const val GRID_VISIBLE_OPTION = "gridVisible"
private const val ZOOM_FACTOR_OPTION = "zoomFactor"
private const val ZOOM_FACTOR_CHANGED_OPTION = "zoomFactorChanged"

data class D2FileEditorState(
  var isBackgroundVisible: Boolean,
  var isGridVisible: Boolean,
  var zoomFactor: Double,
  var isZoomFactorChanged: Boolean,
) : TransferableFileEditorState, Serializable {

  override fun canBeMergedWith(
    otherState: FileEditorState,
    level: FileEditorStateLevel
  ): Boolean = otherState is D2FileEditorState

  override fun setCopiedFromMasterEditor() {
    isZoomFactorChanged = true
  }

  override fun getEditorId(): String = D2_EDITOR_ID

  override fun getTransferableOptions(): Map<String, String> = buildMap {
    put(BACKGROUND_VISIBLE_OPTION, isBackgroundVisible.toString())
    put(GRID_VISIBLE_OPTION, isGridVisible.toString())
    put(ZOOM_FACTOR_OPTION, zoomFactor.toString())
    put(ZOOM_FACTOR_CHANGED_OPTION, isZoomFactorChanged.toString())
  }

  override fun setTransferableOptions(options: Map<String?, String?>) {
    var o = options[BACKGROUND_VISIBLE_OPTION]
    if (o != null) {
      isBackgroundVisible = java.lang.Boolean.parseBoolean(o)
    }
    o = options[GRID_VISIBLE_OPTION]
    if (o != null) {
      isGridVisible = java.lang.Boolean.parseBoolean(o)
    }
    o = options[ZOOM_FACTOR_OPTION]
    if (o != null) {
      zoomFactor = o.toDouble()
    }
    o = options[ZOOM_FACTOR_CHANGED_OPTION]
    if (o != null) {
      isZoomFactorChanged = java.lang.Boolean.parseBoolean(o)
    }
  }
}