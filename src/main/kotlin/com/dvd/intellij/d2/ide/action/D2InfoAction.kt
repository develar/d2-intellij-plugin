package com.dvd.intellij.d2.ide.action

import com.dvd.intellij.d2.ide.service.D2Service
import com.dvd.intellij.d2.ide.utils.file
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service

class D2InfoAction : AnAction() {
  override fun actionPerformed(e: AnActionEvent) {
    val version = service<D2Service>().compilerVersion ?: return

    NotificationGroupManager.getInstance()
      .getNotificationGroup("D2_INFO")
      .createNotification(
        "D2", """
        Compiler version: $version
        <br>
        <a href="https://d2lang.com">Website</a><br>
        <a href="https://github.com/dvdandroid/d2-intellij">Plugin</a>
      """.trimIndent(), NotificationType.INFORMATION
      )
      .notify(e.project)
  }
}