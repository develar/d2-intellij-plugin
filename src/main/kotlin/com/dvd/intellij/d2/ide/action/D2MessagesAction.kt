package com.dvd.intellij.d2.ide.action

import com.dvd.intellij.d2.ide.toolWindow.D2ToolWindowService
import com.dvd.intellij.d2.ide.utils.d2FileEditor
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service

class D2MessagesAction : AnAction() {
  override fun actionPerformed(e: AnActionEvent) {
    e.project?.service<D2ToolWindowService>()?.show(e.d2FileEditor)
  }
}