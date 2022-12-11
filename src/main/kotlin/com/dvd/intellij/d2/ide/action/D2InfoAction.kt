package com.dvd.intellij.d2.ide.action

import com.dvd.intellij.d2.ide.service.D2Service
import com.dvd.intellij.d2.ide.utils.D2Bundle
import com.dvd.intellij.d2.ide.utils.NOTIFICATION_GROUP
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service

class D2InfoAction : AnAction() {
  override fun actionPerformed(e: AnActionEvent) {
    val version = service<D2Service>().compilerVersion ?: return

    NotificationGroupManager.getInstance()
      .getNotificationGroup(NOTIFICATION_GROUP)
      .createNotification(
        D2Bundle["d2"],
        D2Bundle["d2.compiler.info"].replace("%version%", version),
        NotificationType.INFORMATION
      )
      .notify(e.project)
  }
}