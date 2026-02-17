package io.refactoring.aiterminal.terminal

import com.intellij.execution.configurations.GeneralCommandLine
import java.io.File

object MacTerminalLauncher {

    fun launch(directory: String) {
        if (File("/Applications/iTerm.app").exists()) {
            launchITerm2(directory)
        } else {
            launchTerminalApp(directory)
        }
    }

    private fun launchITerm2(directory: String) {
        val escapedDir = directory.replace("'", "'\\''")
        val script = """
            tell application "iTerm2"
                create window with default profile
                tell current session of current window
                    write text "cd '${escapedDir}' && claude"
                end tell
            end tell
        """.trimIndent()

        GeneralCommandLine("osascript", "-e", script).createProcess()
    }

    private fun launchTerminalApp(directory: String) {
        val escapedDir = directory.replace("'", "'\\''")
        val script = """
            tell application "Terminal"
                do script "cd '${escapedDir}' && claude"
                activate
            end tell
        """.trimIndent()

        GeneralCommandLine("osascript", "-e", script).createProcess()
    }
}
