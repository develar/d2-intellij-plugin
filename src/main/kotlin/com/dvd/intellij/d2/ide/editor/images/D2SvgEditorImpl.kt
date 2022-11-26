package com.dvd.intellij.d2.ide.editor.images

import com.dvd.intellij.d2.ide.utils.*
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.*
import com.intellij.openapi.vfs.newvfs.RefreshQueue
import com.intellij.util.Alarm
import org.intellij.images.editor.ImageDocument
import org.intellij.images.editor.ImageEditor
import org.intellij.images.editor.ImageZoomModel
import org.intellij.images.thumbnail.actionSystem.ThumbnailViewActions
import org.intellij.images.vfs.IfsUtil
import java.awt.Color
import javax.swing.JComponent

// https://github.com/JetBrains/intellij-community/blob/master/images/src/org/intellij/images/editor/impl/ImageEditorImpl.java
@Suppress("DEPRECATION")
class D2SvgEditorImpl(
  private val project: Project,
  private val file: VirtualFile,
  private val fileEditor: D2FileEditorImpl,
) : ImageEditor, VirtualFileListener {
  private val isEmbedded = false
  private val isOpaque = false
  private val editorUI = D2SvgEditorUI(this, isEmbedded, isOpaque, fileEditor)
  private val updateAlarm = Alarm(Alarm.ThreadToUse.SWING_THREAD, this)

  private var disposed = false

  init {
    Disposer.register(this, editorUI)
    VirtualFileManager.getInstance().addVirtualFileListener(this, this)

    setValue(file)
  }

  fun setValue(file: VirtualFile?) {
    if (file == null) {
      editorUI.setImageProvider(null, null)
      return
    }
    try {
      editorUI.setImageProvider(IfsUtil.getImageProvider(file), IfsUtil.getFormat(file));
    } catch (e: Exception) {
      //Error loading image file
      editorUI.setImageProvider(null, null)
    }
  }

  override fun isValid(): Boolean = editorUI.getImageComponent().document.value != null
  override fun getComponent(): D2SvgEditorUI = editorUI
  override fun getContentComponent(): JComponent = editorUI.getImageComponent()
  override fun getFile(): VirtualFile = file
  override fun getProject(): Project = project
  override fun getDocument(): ImageDocument = editorUI.getImageComponent().document

  override fun setTransparencyChessboardVisible(visible: Boolean) {
    editorUI.getImageComponent().isTransparencyChessboardVisible = visible
    editorUI.repaint()
  }

  override fun isTransparencyChessboardVisible(): Boolean = editorUI.getImageComponent().isTransparencyChessboardVisible

  // Disable for thumbnails action
  override fun isEnabledForActionPlace(place: String): Boolean = ThumbnailViewActions.ACTION_PLACE != place

  override fun setGridVisible(visible: Boolean) {
    editorUI.getImageComponent().isGridVisible = visible
    editorUI.repaint()
  }

  override fun setEditorBackground(color: Color?) {
    editorUI.getImageComponent().parent.background = color
  }

  override fun setBorderVisible(visible: Boolean) {
    editorUI.getImageComponent().isBorderVisible = visible
  }

  override fun isGridVisible(): Boolean = editorUI.getImageComponent().isGridVisible
  override fun isDisposed(): Boolean = disposed
  override fun getZoomModel(): ImageZoomModel = editorUI.zoomModel

  override fun dispose() {
    disposed = true
  }

  override fun propertyChanged(event: VirtualFilePropertyEvent) {
    val svgFile = fileEditor.generatedFile

    if (file == event.file || svgFile == event.file) {
      // Change document
      file.refresh(true, false) {
        if (file.isD2) {
          setValue(file)
        } else {
          setValue(null)
          // Close editor
          val editorManager = FileEditorManager.getInstance(project)
          editorManager.closeFile(file)
        }
      }
    }
  }

  override fun contentsChanged(event: VirtualFileEvent) {
    val svgFile = fileEditor.generatedFile

    if (file == event.file || svgFile == event.file) {
      // Change document
      refreshFile()
    }
  }

  fun refreshFile() {
    val svgFile = fileEditor.generatedFile ?: return

    val postRunnable = Runnable { setValue(svgFile) }
    updateAlarm.cancelAllRequests()
    updateAlarm.addRequest({
      RefreshQueue.getInstance().refresh(false, false, postRunnable, ModalityState.current(), svgFile)
    }, 250)
  }
}