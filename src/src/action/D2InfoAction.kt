package org.jetbrains.plugins.d2.action

import com.intellij.notification.BrowseNotificationAction
import com.intellij.notification.Notification
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware
import org.jetbrains.plugins.d2.D2Bundle
import org.jetbrains.plugins.d2.D2_NOTIFICATION_GROUP
import org.jetbrains.plugins.d2.editor.D2_INFO_DATA_KEY
import java.lang.ref.WeakReference

private class D2InfoAction : AnAction(), DumbAware {
    private var prevNotification: WeakReference<Notification?>? = null

    override fun actionPerformed(e: AnActionEvent) {
        prevNotification?.get()?.expire()

        val version = e.dataContext.getData(D2_INFO_DATA_KEY)?.version ?: return
        val notification = NotificationGroupManager.getInstance().getNotificationGroup(D2_NOTIFICATION_GROUP)
            .createNotification(
                D2Bundle.message("d2"),
                content = D2Bundle.message("d2.compiler.info", version),
                type = NotificationType.INFORMATION
            )
            .addAction(
                BrowseNotificationAction(
                    D2Bundle.message("notification.content.d2.docs"),
                    "https://d2lang.com/tour/intro/"
                )
            )
            .addAction(
                BrowseNotificationAction(
                    D2Bundle.message("notification.content.d2.plugin"),
                    "https://github.com/develar/d2-intellij-plugin"
                )
            )

        prevNotification = WeakReference(notification)
        notification.notify(e.project)
    }

    override fun getActionUpdateThread() = ActionUpdateThread.BGT
}