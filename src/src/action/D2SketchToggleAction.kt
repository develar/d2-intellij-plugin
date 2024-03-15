package org.jetbrains.plugins.d2.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction
import com.intellij.openapi.project.DumbAware

private class D2SketchToggleAction() : ToggleAction(), DumbAware {

  override fun isSelected(e: AnActionEvent): Boolean {
    return e.d2FileEditor?.sketch == true
  }

  override fun setSelected(e: AnActionEvent, state: Boolean) {
    e.d2FileEditor?.sketch = state
  }
}