package org.jetbrains.plugins.d2.editor

import com.intellij.icons.AllIcons
import com.intellij.ide.BrowserUtil
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.project.DumbAware
import org.jetbrains.plugins.d2.D2Bundle
import org.jetbrains.plugins.d2.D2Theme
import org.jetbrains.plugins.d2.action.d2FileEditor
import org.jetbrains.plugins.d2.themeList

private class D2ThemeActionGroup : ActionGroup(), DumbAware {
    private val children: Array<AnAction> = arrayOf(
        *themeList.map(::D2ThemeAction).toTypedArray(),
        Separator(),
        OpenThemeOverviewAction()
    )

    override fun getChildren(e: AnActionEvent?): Array<AnAction> = children

    override fun getActionUpdateThread() = ActionUpdateThread.BGT
}

private class D2ThemeAction(private val theme: D2Theme) : ToggleAction(theme.name), DumbAware {
    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun isSelected(e: AnActionEvent): Boolean {
        return (e.d2FileEditor!!.theme ?: D2Theme.DEFAULT) == theme
    }

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        e.d2FileEditor!!.theme = theme
    }
}

private const val THEME_DOCS = "https://d2lang.com/tour/themes"

private class OpenThemeOverviewAction : AnAction(
    D2Bundle.messagePointer("d2.open.theme.documentation"),
    AllIcons.General.Web
), DumbAware {
    override fun actionPerformed(e: AnActionEvent) = BrowserUtil.browse(THEME_DOCS)

    override fun getActionUpdateThread() = ActionUpdateThread.BGT
}