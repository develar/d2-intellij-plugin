package org.jetbrains.plugins.d2.editor

import com.dvd.intellij.d2.components.D2Layout
import com.dvd.intellij.d2.components.D2Theme
import com.dvd.intellij.d2.ide.utils.D2Bundle
import com.dvd.intellij.d2.ide.utils.D2_EDITOR_NAME
import com.intellij.codeHighlighting.BackgroundEditorHighlighter
import com.intellij.ide.structureView.StructureViewBuilder
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.fileEditor.FileEditorStateLevel
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.jcef.JBCefApp
import com.intellij.ui.jcef.JBCefBrowser
import com.intellij.ui.jcef.JBCefBrowserBuilder
import com.intellij.util.EventDispatcher
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.beans.PropertyChangeListener
import javax.swing.JComponent
import javax.swing.JLabel

val D2_FILE_LAYOUT: Key<D2Layout> = Key<D2Layout>("d2_file_layout")
val D2_FILE_THEME: Key<D2Theme> = Key<D2Theme>("d2_file_theme")

// https://github.com/JetBrains/intellij-community/blob/master/images/src/org/intellij/images/editor/impl/ImageFileEditorImpl.java
class D2SvgViewer(
  val project: Project,
  private val file: VirtualFile
) : UserDataHolderBase(), FileEditor {
  private val dispatcher = EventDispatcher.create(PropertyChangeListener::class.java)

  private val component: JComponent
  private val browser: JBCefBrowser?

  init {
    if (JBCefApp.isSupported()) {
      browser = JBCefBrowserBuilder()
        .build()
      component = browser.component

      component.addComponentListener(object : ComponentAdapter() {
        override fun componentShown(e: ComponentEvent?) {
          // if preview was hidden initially (mode without preview)
          browser.cefBrowser.reload()
        }
      })
    } else {
      component = JLabel(D2Bundle.message("preview.requires.an.intellij.platform.ide.with.jcef.support"))
      browser = null
    }

    service<D2Service>().scheduleCompile(this, project)
  }

  fun refreshD2(port: Int) {
    browser?.loadURL("http://127.0.0.1:${port}")
  }

  override fun getComponent(): JComponent = component

  override fun getPreferredFocusedComponent(): JComponent = if (browser == null) component else browser.cefBrowser.uiComponent as JComponent

  override fun getName(): String = D2_EDITOR_NAME

  override fun getState(level: FileEditorStateLevel): FileEditorState {
//    return D2FileEditorState(
//      d2Editor.isTransparencyChessboardVisible,
//      d2Editor.isGridVisible,
//      d2Editor.zoomModel.zoomFactor,
//      d2Editor.zoomModel.isZoomLevelChanged,
//    )
    return FileEditorState.INSTANCE
  }

  override fun setState(state: FileEditorState) {
//    if (state is D2FileEditorState) {
//      val options = OptionsManager.getInstance().options
//      val zoomOptions = options.editorOptions.zoomOptions
//      val zoomModel = d2Editor.zoomModel
//      d2Editor.isTransparencyChessboardVisible = state.isBackgroundVisible
//      d2Editor.isGridVisible = state.isGridVisible
//      if (state.isZoomFactorChanged || !zoomOptions.isSmartZooming) {
//        zoomModel.zoomFactor = state.zoomFactor
//      }
//      zoomModel.isZoomLevelChanged = state.isZoomFactorChanged
//    }
  }

  override fun addPropertyChangeListener(listener: PropertyChangeListener) {
    dispatcher.addListener(listener)
  }

  override fun removePropertyChangeListener(listener: PropertyChangeListener) {
    dispatcher.removeListener(listener)
  }

//  override fun propertyChange(event: PropertyChangeEvent) {
//    val editorEvent = PropertyChangeEvent(this, event.propertyName, event.oldValue, event.newValue)
//    dispatcher.multicaster.propertyChange(editorEvent)
//  }

  override fun isModified(): Boolean = false

  override fun isValid(): Boolean = true

  override fun getBackgroundHighlighter(): BackgroundEditorHighlighter? = null

  override fun getCurrentLocation(): FileEditorLocation? = null

  override fun getStructureViewBuilder(): StructureViewBuilder? = null

  override fun dispose() {
    try {
      browser?.let {
        Disposer.dispose(it)
      }
    } finally {
      service<D2Service>().closeFile(this)
    }
  }

  override fun getFile(): VirtualFile = file
}