package com.dvd.intellij.d2.ide.action

import com.dvd.intellij.d2.ide.utils.D2Bundle
import com.dvd.intellij.d2.ide.utils.NOTIFICATION_GROUP
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import org.jetbrains.plugins.d2.editor.D2Service

private class D2InfoAction : AnAction() {
  override fun actionPerformed(e: AnActionEvent) {
    val version = service<D2Service>().getCompilerVersion() ?: return
    NotificationGroupManager.getInstance()
      .getNotificationGroup(NOTIFICATION_GROUP)
      .createNotification(
        D2Bundle.message("d2"),
        D2Bundle.message("d2.compiler.info", version),
    NotificationType.INFORMATION
    )
    .notify(e.project)
  }
}