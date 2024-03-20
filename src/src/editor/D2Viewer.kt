package org.jetbrains.plugins.d2.editor

import com.intellij.codeHighlighting.BackgroundEditorHighlighter
import com.intellij.ide.structureView.StructureViewBuilder
import com.intellij.ide.ui.LafManagerListener
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.fileEditor.FileEditorStateLevel
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.jcef.JBCefBrowser
import com.intellij.ui.jcef.JBCefBrowserBuilder
import com.intellij.util.EventDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.job
import kotlinx.serialization.Serializable
import org.jetbrains.plugins.d2.D2Layout
import org.jetbrains.plugins.d2.D2Theme
import java.awt.BorderLayout
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.beans.PropertyChangeListener
import javax.swing.JComponent
import javax.swing.JPanel
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

internal data class D2Info(@JvmField val version: String, @JvmField val layouts: List<D2Layout>)

internal val D2_INFO_DATA_KEY: DataKey<D2Info> = DataKey.create("LAYOUT_ENGINES")

@Service(Service.Level.PROJECT)
private class ProjectLevelCoroutineScopeHolder(val coroutineScope: CoroutineScope)

internal const val D2_EDITOR_NAME = "D2FileEditor"

@Serializable
internal data class D2FileEditorState(@JvmField var theme: D2Theme?,
                                      @JvmField val layout: D2Layout?,
                                      @JvmField val sketch: Boolean = false) : FileEditorState {
  override fun canBeMergedWith(otherState: FileEditorState, level: FileEditorStateLevel): Boolean = otherState is D2FileEditorState

  //override fun getEditorId() = "D2Viewer"
  //
  //override fun getTransferableOptions(): Map<String, String?> {
  //  return mapOf("theme" to theme)
  //}
  //
  //override fun setTransferableOptions(options: Map<String, String?>) {
  //  theme = options.get("theme")
  //}
  //
  //override fun setCopiedFromMasterEditor() {
  //}
}

internal class D2Viewer(
  val project: Project,
  private val file: VirtualFile
) : UserDataHolderBase(), FileEditor, DumbAware {
  private val dispatcher = EventDispatcher.create(PropertyChangeListener::class.java)

  val renderManager: RenderManager
  val coroutineScope: CoroutineScope

  var theme: D2Theme? = null
    set(value) {
      field = value
      requestRender()
    }

  var layout: D2Layout? = null
    set(value) {
      field = value
      requestRender()
    }

  var sketch: Boolean = false
    set(value) {
      field = value
      requestRender()
    }

  private val component: JComponent
  private val browser: JBCefBrowser?

  init {
    browser = JBCefBrowserBuilder()
      .build()

    coroutineScope = project.service<ProjectLevelCoroutineScopeHolder>().coroutineScope.childScope()
    renderManager = RenderManager(coroutineScope = coroutineScope, project = project, file = file) {
      browser.loadURL("http://127.0.0.1:$it")
    }

    component = object : JPanel(BorderLayout()), DataProvider {
      override fun getData(dataId: String): Any? {
        return when {
          CommonDataKeys.PROJECT.`is`(dataId) -> project
          D2_INFO_DATA_KEY.`is`(dataId) -> renderManager.d2Info.value.takeIf { it.version.isNotEmpty() }
          else -> null
        }
      }
    }

    val actionManager = ActionManager.getInstance()
    val actionGroup = actionManager.getAction("D2.EditorToolbar") as ActionGroup
    val actionToolbar = actionManager.createActionToolbar("D2.D2Viewer", actionGroup, true)
    actionToolbar.targetComponent = component

    component.add(actionToolbar.component, BorderLayout.PAGE_START)
    component.add(browser.component, BorderLayout.CENTER)

    component.addComponentListener(object : ComponentAdapter() {
      override fun componentShown(e: ComponentEvent?) {
        // if preview was hidden initially (mode without preview)
        browser.cefBrowser.reload()
      }
    })

    ApplicationManager.getApplication().messageBus.connect(coroutineScope).subscribe(LafManagerListener.TOPIC, LafManagerListener {
      requestRender()
    })

    requestRender()
  }

  private fun requestRender() {
    renderManager.request(RenderRequest(theme = theme, layout = layout, sketch = sketch))
  }

  override fun getComponent(): JComponent = component

  override fun getPreferredFocusedComponent(): JComponent = if (browser == null) component else browser.cefBrowser.uiComponent as JComponent

  override fun getName(): String = D2_EDITOR_NAME

  override fun getState(level: FileEditorStateLevel): FileEditorState {
    return D2FileEditorState(theme = theme, layout = layout, sketch = sketch)
  }

  override fun setState(state: FileEditorState) {
    if (state !is D2FileEditorState) {
      return
    }

    theme = state.theme
    layout = state.layout
    sketch = state.sketch
  }

  override fun addPropertyChangeListener(listener: PropertyChangeListener) {
    dispatcher.addListener(listener)
  }

  override fun removePropertyChangeListener(listener: PropertyChangeListener) {
    dispatcher.removeListener(listener)
  }

  override fun isModified(): Boolean = false

  override fun isValid(): Boolean = true

  override fun getBackgroundHighlighter(): BackgroundEditorHighlighter? = null

  override fun getCurrentLocation(): FileEditorLocation? = null

  override fun getStructureViewBuilder(): StructureViewBuilder? = null

  override fun dispose() {
    coroutineScope.cancel()
    browser?.let {
      Disposer.dispose(it)
    }
  }

  override fun getFile(): VirtualFile = file
}

private fun CoroutineScope.childScope(context: CoroutineContext = EmptyCoroutineContext): CoroutineScope {
  val parentContext = coroutineContext
  return CoroutineScope(parentContext + SupervisorJob(parent = parentContext.job) + context)
}