package org.jetbrains.plugins.d2.editor

import com.intellij.openapi.fileEditor.*
import com.intellij.openapi.fileEditor.impl.text.TextEditorProvider
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jdom.Element
import org.jetbrains.plugins.d2.action.isD2
import org.jetbrains.plugins.d2.themeList

private class D2FileEditorProvider : FileEditorProvider, DumbAware {
  @OptIn(ExperimentalSerializationApi::class)
  private val json = Json {
    prettyPrint = true
    prettyPrintIndent = "  "
    ignoreUnknownKeys = true
  }

  override fun accept(project: Project, file: VirtualFile): Boolean = file.isD2

  // 2023.3
  @Suppress("unused")
  fun acceptRequiresReadAction(): Boolean {
    return false
  }

  override fun createEditor(project: Project, file: VirtualFile): FileEditor {
    val view = D2Viewer(project, file)
    val editor = TextEditorProvider.getInstance().createEditor(project, file) as TextEditor
    return TextEditorWithPreview(editor, view, D2_EDITOR_NAME, TextEditorWithPreview.Layout.SHOW_EDITOR_AND_PREVIEW)
  }

  override fun getEditorTypeId(): String = D2_EDITOR_NAME

  override fun getPolicy(): FileEditorPolicy = FileEditorPolicy.HIDE_DEFAULT_EDITOR

  override fun readState(sourceElement: Element, project: Project, file: VirtualFile): FileEditorState {
    val layout = sourceElement.getAttributeValue("layout")?.let {
      TextEditorWithPreview.Layout.fromId(it, null)
    }

    val firstState = sourceElement.getChild("text")?.let {
      TextEditorProvider.getInstance().readState(it, project, file)
    }
    val secondState: D2FileEditorState?  = sourceElement.getChild("d2")?.text?.let { dataString ->
      val result = json.decodeFromString<D2FileEditorState>(dataString) as? D2FileEditorState
      val theme = result?.theme
      if (theme == null) {
        result
      } else {
        // find a theme with the same id to set name
        themeList.firstOrNull { it.id == theme.id }?.let {
          D2FileEditorState(theme = theme, layout = result.layout, sketch = result.sketch)
        } ?: result
      }
    }
    return TextEditorWithPreview.MyFileEditorState(layout, firstState, secondState)
  }

  override fun writeState(state: FileEditorState, project: Project, targetElement: Element) {
    if (state is TextEditorWithPreview.MyFileEditorState) {
      state.firstState?.let {
        val text = Element("text")
        TextEditorProvider.getInstance().writeState(it, project, text)
        if (!text.isEmpty) {
          targetElement.addContent(text)
        }
      }

      state.secondState?.let {
        targetElement.addContent(Element("d2").addContent(json.encodeToString<D2FileEditorState>(it as D2FileEditorState)))
      }

      state.splitLayout?.name?.let {
        targetElement.setAttribute("layout", it)
      }
    }
  }
}