package com.dvd.intellij.d2.ide.toolWindow

import com.dvd.intellij.d2.ide.service.D2Service
import com.dvd.intellij.d2.ide.utils.D2_TOOLWINDOW_ID
import com.dvd.intellij.d2.ide.utils.isD2
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.ui.ConsoleView
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory

interface D2ToolWindowService {
  fun editorOpened(fileEditor: FileEditor)

  fun editorClosed(fileEditor: FileEditor)

  fun show(fileEditor: FileEditor)

  fun update(fileEditor: FileEditor)
}

private class D2ToolWindowServiceImpl(private val project: Project) : D2ToolWindowService {
  private fun getD2ToolWindow(): ToolWindow? {
    return ToolWindowManager.getInstance(project).getToolWindow(D2_TOOLWINDOW_ID)
  }

  private val tcb = TextConsoleBuilderFactory.getInstance().createBuilder(project)
  private val fileEditorKey = Key<Int>("d2_content_fileEditor")

  override fun editorOpened(fileEditor: FileEditor) {
    if (!fileEditor.file.isD2) {
      return
    }

    val toolWindow = getD2ToolWindow()
    if (toolWindow == null) {
      return
    }

    if (!service<D2Service>().isCompilerInstalled()) {
      return
    }

    val content = ContentFactory.getInstance().createContent(tcb.console.component, fileEditor.file.name, false).apply {
      putUserData(fileEditorKey, fileEditor.hashCode())
    }
    toolWindow.contentManager.addContent(content)
    update(fileEditor)
    toolWindow.isAvailable = true
    // if show() not called, toolwindow is not attached
    toolWindow.show {
      toolWindow.hide()
    }
  }

  override fun editorClosed(fileEditor: FileEditor) {
    if (!fileEditor.file.isD2) {
      return
    }

    val toolWindow = getD2ToolWindow()
    if (toolWindow == null) {
      return
    }

    if (!service<D2Service>().isCompilerInstalled()) {
      return
    }

    getRelativeContent(fileEditor, toolWindow)?.let {
      toolWindow.contentManager.removeContent(it, true)
    }

    if (toolWindow.contentManager.contentCount == 0) {
      toolWindow.isAvailable = false
    }
  }

  override fun show(fileEditor: FileEditor) {
    val toolWindow = getD2ToolWindow()
    if (toolWindow == null) {
      return
    }

    getRelativeContent(fileEditor, toolWindow)?.let {
      toolWindow.contentManager.setSelectedContent(it)
      toolWindow.show()
    }
  }

  override fun update(fileEditor: FileEditor) {
    getRelativeContent(fileEditor, getD2ToolWindow() ?: return)?.let {
      val view = it.component as ConsoleView
      // only process output
//      val cmd = service<D2Service>().map[fileEditor]?.cmd ?: return
//      cmd.process?.let { p -> view.attachToProcess(p) }

      val log = service<D2Service>().map[fileEditor]?.log ?: return
      view.updateText(log)
    }
  }

  // plugin + process output
  private fun ConsoleView.updateText(log: String) {
    // "jumping" text
    clear()

    log.lines().forEach {
      // even with ColoredProcessHandler, output is not colored

//      val type = when {
//        "success: " in it -> ConsoleViewContentType("green", ConsoleHighlighter.GREEN)
//        "warn: " in it -> ConsoleViewContentType("yellow", ConsoleHighlighter.YELLOW)
//        "err: " in it -> ConsoleViewContentType("red", ConsoleHighlighter.RED)
//        "info: " in it -> ConsoleViewContentType("blue", ConsoleHighlighter.BLUE)
//        "[process ]" in it || "[plugin ]" in it -> ConsoleViewContentType("magenta", ConsoleHighlighter.MAGENTA)
//        else -> ConsoleViewContentType.NORMAL_OUTPUT
//      }

      print("$it\n", ConsoleViewContentType.NORMAL_OUTPUT)
    }
  }

  private fun getRelativeContent(fileEditor: FileEditor, toolWindow: ToolWindow): Content? {
    return toolWindow.contentManager.contents.firstOrNull { it.getUserData(fileEditorKey) == fileEditor.hashCode() }
  }
}


private class D2ToolWindow : ToolWindowFactory, DumbAware {
  override fun isApplicable(project: Project): Boolean = service<D2Service>().isCompilerInstalled()

  override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) = Unit
}