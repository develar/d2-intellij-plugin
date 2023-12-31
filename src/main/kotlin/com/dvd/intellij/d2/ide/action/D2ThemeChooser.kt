package com.dvd.intellij.d2.ide.action

import com.dvd.intellij.d2.components.D2Theme
import com.intellij.icons.AllIcons
import com.intellij.ide.BrowserUtil
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAware
import org.jetbrains.plugins.d2.D2Bundle
import org.jetbrains.plugins.d2.d2FileEditor
import org.jetbrains.plugins.d2.editor.D2Service
import org.jetbrains.plugins.d2.editor.D2_FILE_THEME

private class D2ThemesActionGroup : ActionGroup() {
  override fun getChildren(e: AnActionEvent?): Array<AnAction> = arrayOf(
    *D2Theme.values().map(::D2ThemeAction).toTypedArray(),
    Separator(),
    OpenThemeOverviewAction()
  )
}

private class D2ThemeAction(private val theme: D2Theme) : ToggleAction(theme.tName), DumbAware {
  override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

  override fun isSelected(e: AnActionEvent): Boolean {
    return (e.d2FileEditor.getUserData(D2_FILE_THEME) ?: D2Theme.DEFAULT) == theme
  }

  override fun setSelected(e: AnActionEvent, state: Boolean) {
    e.d2FileEditor.putUserData(D2_FILE_THEME, theme)
    service<D2Service>().compileAndWatch(e.d2FileEditor)
  }
}

private const val THEME_DOCS = "https://d2lang.com/tour/themes"

private class OpenThemeOverviewAction : AnAction(
  D2Bundle.messagePointer("d2.open.theme.documentation"),
  AllIcons.General.Web
), DumbAware {
  override fun actionPerformed(e: AnActionEvent) = BrowserUtil.browse(THEME_DOCS)
}