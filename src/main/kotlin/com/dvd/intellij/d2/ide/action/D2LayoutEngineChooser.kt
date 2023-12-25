package com.dvd.intellij.d2.ide.action

import com.dvd.intellij.d2.components.D2Layout
import com.dvd.intellij.d2.ide.utils.D2Bundle
import com.dvd.intellij.d2.ide.utils.d2FileEditor
import com.intellij.icons.AllIcons
import com.intellij.ide.BrowserUtil
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAware
import org.jetbrains.plugins.d2.editor.D2Service
import org.jetbrains.plugins.d2.editor.D2_FILE_LAYOUT

private class D2LayoutEngineActionGroup : ActionGroup(), DumbAware {
  override fun getChildren(e: AnActionEvent?): Array<AnAction> {
    if (e == null) {
      return emptyArray()
    }

    val layouts = service<D2Service>().getLayoutEngines() ?: return emptyArray()
    return arrayOf(
      *layouts.map(::D2LayoutEngineAction).toTypedArray(),
      Separator(),
      OpenLayoutEngineOverviewAction()
    )
  }

  override fun getActionUpdateThread() = ActionUpdateThread.BGT
}

private class D2LayoutEngineAction(private val layout: D2Layout) : ToggleAction(
  layout.name,
  @Suppress("HardCodedStringLiteral")
  buildString {
    append(layout.description)
    if (layout.bundled == true) {
      append(" (bundled)")
    }
  },
  null
), DumbAware {
  override fun isSelected(e: AnActionEvent): Boolean = (e.d2FileEditor.getUserData(D2_FILE_LAYOUT) ?: D2Layout.DEFAULT) == layout

  override fun setSelected(e: AnActionEvent, state: Boolean) {
    e.d2FileEditor.putUserData(D2_FILE_LAYOUT, layout)
    service<D2Service>().compile(e.d2FileEditor)
  }

  override fun getActionUpdateThread() = ActionUpdateThread.BGT
}

private const val LAYOUT_DOCS = "https://d2lang.com/tour/layouts"

private class OpenLayoutEngineOverviewAction : AnAction(
  D2Bundle.messagePointer("d2.open.layout.documentation"),
  AllIcons.General.Web
), DumbAware {
  override fun actionPerformed(e: AnActionEvent) = BrowserUtil.browse(LAYOUT_DOCS)
}