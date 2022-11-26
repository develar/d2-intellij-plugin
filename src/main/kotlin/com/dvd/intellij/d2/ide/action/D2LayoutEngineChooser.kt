package com.dvd.intellij.d2.ide.action

import com.dvd.intellij.d2.components.D2Layout
import com.dvd.intellij.d2.ide.editor.images.D2FileEditorImpl.Companion.D2_FILE_LAYOUT
import com.dvd.intellij.d2.ide.service.D2Service
import com.dvd.intellij.d2.ide.utils.D2Bundle
import com.dvd.intellij.d2.ide.utils.d2FileEditor
import com.dvd.intellij.d2.ide.utils.file
import com.intellij.icons.AllIcons
import com.intellij.ide.BrowserUtil
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.components.service

class D2LayoutEngineActionGroup : ActionGroup() {
  override fun getChildren(e: AnActionEvent?): Array<AnAction> {
    if (e == null) return emptyArray()
    val layouts = service<D2Service>().layoutEngines ?: return emptyArray()

    return arrayOf(
      *layouts.map(::D2LayoutEngineAction).toTypedArray(),
      Separator(),
      OpenLayoutEngineOverviewAction()
    )
  }
}

class D2LayoutEngineAction(private val layout: D2Layout) : ToggleAction(
  layout.name,
  buildString {
    append(layout.description)
    if (layout.bundled == true) {
      append(" (bundled)")
    }
  },
  null
) {

  override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

  override fun isSelected(e: AnActionEvent): Boolean =
    (e.d2FileEditor.getUserData(D2_FILE_LAYOUT) ?: D2Layout.DEFAULT) == layout

  override fun setSelected(e: AnActionEvent, state: Boolean) {
    e.d2FileEditor.putUserData(D2_FILE_LAYOUT, layout)
    e.d2FileEditor.refreshD2()
  }
}

class OpenLayoutEngineOverviewAction : AnAction(
  D2Bundle["d2.open.layout.documentation"],
  D2Bundle["d2.open.layout.documentation"],
  AllIcons.General.Web
) {
  companion object {
    private const val LAYOUT_DOCS = "https://d2lang.com/tour/layouts"
  }

  override fun actionPerformed(e: AnActionEvent) = BrowserUtil.browse(LAYOUT_DOCS)
}