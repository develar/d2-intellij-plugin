package org.jetbrains.plugins.d2.action

import com.intellij.icons.AllIcons
import com.intellij.ide.BrowserUtil
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.project.DumbAware
import org.jetbrains.plugins.d2.D2Bundle
import org.jetbrains.plugins.d2.D2Layout
import org.jetbrains.plugins.d2.editor.D2Info
import org.jetbrains.plugins.d2.editor.D2_INFO_DATA_KEY

private class D2LayoutEngineActionGroup : ActionGroup(), DumbAware {
    private var cachedChildren: Array<AnAction> = AnAction.EMPTY_ARRAY
    private var lastInfo: D2Info? = null

    override fun getChildren(e: AnActionEvent?): Array<AnAction> {
        if (e == null) {
            return AnAction.EMPTY_ARRAY
        }

        val info = e.dataContext.getData(D2_INFO_DATA_KEY)
        if (lastInfo === info) {
            return cachedChildren
        }

        val layouts = info?.layouts ?: return emptyArray()
        cachedChildren = arrayOf(
            *layouts.map(::D2LayoutEngineAction).toTypedArray<D2LayoutEngineAction>(),
            Separator(),
            OpenLayoutEngineOverviewAction()
        )
        return cachedChildren
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
    override fun isSelected(e: AnActionEvent): Boolean = (e.d2FileEditor?.layout ?: D2Layout.DEFAULT) == layout

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        e.d2FileEditor?.layout = layout
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