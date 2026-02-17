package io.refactoring.aiterminal

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import io.refactoring.aiterminal.terminal.TerminalLauncher

class OpenInClaudeAction : AnAction() {

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun update(e: AnActionEvent) {
        val selectedFiles = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY)
        if (selectedFiles != null && selectedFiles.size > 1) {
            e.presentation.isEnabledAndVisible = false
            return
        }

        e.presentation.isEnabledAndVisible = e.getData(CommonDataKeys.VIRTUAL_FILE) != null
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
        val directory = if (file.isDirectory) file.path else file.parent?.path ?: return

        try {
            TerminalLauncher.launch(directory)
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
