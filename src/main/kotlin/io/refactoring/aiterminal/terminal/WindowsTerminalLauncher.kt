package io.refactoring.aiterminal.terminal

import com.intellij.execution.configurations.GeneralCommandLine

object WindowsTerminalLauncher {

    fun launch(directory: String) {
        when {
            isAvailable("wt") -> launchWindowsTerminal(directory)
            isAvailable("powershell") -> launchPowerShell(directory)
            else -> launchCmd(directory)
        }
    }

    private fun launchWindowsTerminal(directory: String) {
        GeneralCommandLine("wt", "-d", directory, "cmd", "/k", "claude")
            .createProcess()
    }

    private fun launchPowerShell(directory: String) {
        GeneralCommandLine(
            "powershell", "-NoExit", "-Command",
            "Set-Location '${directory}'; claude"
        ).createProcess()
    }

    private fun launchCmd(directory: String) {
        GeneralCommandLine(
            "cmd", "/c", "start", "cmd", "/k",
            "cd /d \"${directory}\" && claude"
        ).createProcess()
    }

    private fun isAvailable(command: String): Boolean {
        return try {
            GeneralCommandLine("where", command)
                .withRedirectErrorStream(true)
                .createProcess()
                .waitFor() == 0
        } catch (_: Exception) {
            false
        }
    }
}
