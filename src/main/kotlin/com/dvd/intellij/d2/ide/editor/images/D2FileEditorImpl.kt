package com.dvd.intellij.d2.ide.editor.images

import com.dvd.intellij.d2.components.D2Layout
import com.dvd.intellij.d2.components.D2Theme
import com.dvd.intellij.d2.ide.service.D2Service
import com.dvd.intellij.d2.ide.utils.D2_EDITOR_NAME
import com.intellij.codeHighlighting.BackgroundEditorHighlighter
import com.intellij.ide.structureView.StructureViewBuilder
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.fileEditor.FileEditorStateLevel
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.EventDispatcher
import org.intellij.images.editor.ImageEditor
import org.intellij.images.editor.ImageFileEditor
import org.intellij.images.options.EditorOptions
import org.intellij.images.options.OptionsManager
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import javax.swing.JComponent

// https://github.com/JetBrains/intellij-community/blob/master/images/src/org/intellij/images/editor/impl/ImageFileEditorImpl.java
class D2FileEditorImpl(
  val project: Project,
  file: VirtualFile
) : UserDataHolderBase(), ImageFileEditor, PropertyChangeListener {
  companion object {
    val D2_FILE_LAYOUT = Key<D2Layout>("d2_file_layout")
    val D2_FILE_THEME = Key<D2Theme>("d2_file_theme")
  }

  private val d2Editor = D2SvgEditorImpl(project, file, this)
  private val dispatcher = EventDispatcher.create(PropertyChangeListener::class.java)

  init {
    Disposer.register(this, d2Editor)

    val options = OptionsManager.getInstance().options
    val editorOptions: EditorOptions = options.editorOptions
    val gridOptions = editorOptions.gridOptions
    val transparencyChessboardOptions = editorOptions.transparencyChessboardOptions
    imageEditor.isGridVisible = gridOptions.isShowDefault
    imageEditor.isTransparencyChessboardVisible = transparencyChessboardOptions.isShowDefault

    (imageEditor as D2SvgEditorImpl).component.getImageComponent().addPropertyChangeListener(this)
  }

  fun refreshD2() {
    d2Editor.zoomModel.fitZoomToWindow()
    service<D2Service>().compile(this)
    d2Editor.refreshFile()
  }

  override fun getComponent(): JComponent = d2Editor.component
  override fun getPreferredFocusedComponent(): JComponent = d2Editor.contentComponent
  override fun getName(): String = D2_EDITOR_NAME

  override fun getState(level: FileEditorStateLevel): FileEditorState = D2FileEditorState(
    d2Editor.isTransparencyChessboardVisible,
    d2Editor.isGridVisible,
    d2Editor.zoomModel.zoomFactor,
    d2Editor.zoomModel.isZoomLevelChanged,
  )

  override fun setState(state: FileEditorState) {
    if (state is D2FileEditorState) {
      val options = OptionsManager.getInstance().options
      val zoomOptions = options.editorOptions.zoomOptions
      val zoomModel = d2Editor.zoomModel
      d2Editor.isTransparencyChessboardVisible = state.isBackgroundVisible
      d2Editor.isGridVisible = state.isGridVisible
      if (state.isZoomFactorChanged || !zoomOptions.isSmartZooming) {
        zoomModel.zoomFactor = state.zoomFactor
      }
      zoomModel.isZoomLevelChanged = state.isZoomFactorChanged
    }
  }

  override fun addPropertyChangeListener(listener: PropertyChangeListener) {
    dispatcher.addListener(listener)
  }

  override fun removePropertyChangeListener(listener: PropertyChangeListener) {
    dispatcher.removeListener(listener)
  }

  override fun propertyChange(event: PropertyChangeEvent) {
    val editorEvent = PropertyChangeEvent(this, event.propertyName, event.oldValue, event.newValue)
    dispatcher.multicaster.propertyChange(editorEvent)
  }

  override fun isModified(): Boolean = false
  override fun isValid(): Boolean = true
  override fun getBackgroundHighlighter(): BackgroundEditorHighlighter? = null
  override fun getCurrentLocation(): FileEditorLocation? = null
  override fun getStructureViewBuilder(): StructureViewBuilder? = null
  override fun dispose() {}
  override fun getImageEditor(): ImageEditor = d2Editor
  override fun getFile(): VirtualFile = d2Editor.file
}