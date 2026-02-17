package io.refactoring.aiterminal

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import io.refactoring.aiterminal.terminal.TerminalLauncher

class OpenInClaudeToolbarAction : AnAction() {

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = e.project != null
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val basePath = project.basePath ?: return

        try {
            TerminalLauncher.launch(basePath)
        } catch (ex: Exception) {
            NotificationGroupManager.getInstance()
                .getNotificationGroup("OpenInClaude")
                .createNotification(
                    "Open in Claude",
                    ex.message ?: "终端启动失败",
                    NotificationType.ERROR
                )
                .notify(project)
        }
    }
}
