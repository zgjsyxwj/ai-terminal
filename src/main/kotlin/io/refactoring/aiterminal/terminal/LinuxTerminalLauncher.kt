package io.refactoring.aiterminal.terminal

import com.intellij.execution.configurations.GeneralCommandLine

object LinuxTerminalLauncher {

    private data class TerminalCandidate(
        val name: String,
        val buildCommand: (directory: String) -> List<String>
    )

    private val candidates: List<TerminalCandidate>
        get() = listOf(
            TerminalCandidate("x-terminal-emulator") { dir ->
                listOf("x-terminal-emulator", "--working-directory=$dir", "-e", "claude")
            },
            TerminalCandidate(System.getenv("TERMINAL") ?: "") { dir ->
                listOf(System.getenv("TERMINAL") ?: "", "--working-directory=$dir", "-e", "claude")
            },
            TerminalCandidate("gnome-terminal") { dir ->
                listOf("gnome-terminal", "--working-directory=$dir", "--", "claude")
            },
            TerminalCandidate("konsole") { dir ->
                listOf("konsole", "--workdir", dir, "-e", "claude")
            },
            TerminalCandidate("xterm") { dir ->
                listOf("xterm", "-e", "cd '${dir}' && claude")
            }
        )

    fun launch(directory: String) {
        for (candidate in candidates) {
            if (candidate.name.isNotEmpty() && isAvailable(candidate.name)) {
                GeneralCommandLine(candidate.buildCommand(directory)).createProcess()
                return
            }
        }
        throw RuntimeException("未找到可用的终端模拟器，请安装 gnome-terminal、konsole 或 xterm")
    }

    private fun isAvailable(command: String): Boolean {
        return try {
            GeneralCommandLine("which", command)
                .withRedirectErrorStream(true)
                .createProcess()
                .waitFor() == 0
        } catch (_: Exception) {
            false
        }
    }
}
