package com.dvd.intellij.d2.ide.action

import com.dvd.intellij.d2.ide.service.D2Service
import com.dvd.intellij.d2.ide.utils.D2Bundle
import com.intellij.execution.impl.ConsoleViewImpl
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.ToolWindowId
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.content.MessageView
import java.awt.BorderLayout
import javax.swing.JPanel

private class D2MessagesAction : AnAction(), DumbAware {
  override fun actionPerformed(e: AnActionEvent) {
    val project = e.project ?: return

    val messageView = MessageView.getInstance(project)
    val displayName = D2Bundle.message("d2")

    var content = messageView.contentManager.findContent(displayName)
    if (content == null) {
      val panel = JPanel(BorderLayout())
      val console = ConsoleViewImpl(project, true)
      panel.add(console.component, BorderLayout.CENTER)

      content = ContentFactory.getInstance().createContent(panel, displayName, true)
      messageView.contentManager.addContent(content)
      Disposer.register(content, console)
    }

    val console = content.component as ConsoleViewImpl
    console.clear()
    messageView.contentManager.setSelectedContent(content)
    for (command in service<D2Service>().map.values) {
      console.print(command.log, ConsoleViewContentType.LOG_INFO_OUTPUT)
    }

    val toolWindow = ToolWindowManager.getInstance(project).getToolWindow(ToolWindowId.MESSAGES_WINDOW)
    if (toolWindow != null && !toolWindow.isActive) {
      toolWindow.activate(null, false)
    }
  }
}