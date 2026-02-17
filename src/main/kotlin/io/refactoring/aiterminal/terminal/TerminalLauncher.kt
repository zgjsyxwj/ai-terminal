package io.refactoring.aiterminal.terminal

import com.intellij.openapi.util.SystemInfo

object TerminalLauncher {

    fun launch(directory: String) {
        when {
            SystemInfo.isWindows -> WindowsTerminalLauncher.launch(directory)
            SystemInfo.isMac -> MacTerminalLauncher.launch(directory)
            SystemInfo.isLinux -> LinuxTerminalLauncher.launch(directory)
            else -> throw RuntimeException("不支持的操作系统")
        }
    }
}
