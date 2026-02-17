# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

IntelliJ Platform plugin (`io.refactoring.ai-terminal`) built with Kotlin, targeting IntelliJ IDEA 2025.2.4+.

## Tech Stack

- **Language**: Kotlin 2.1.20, Java 21
- **Build**: Gradle 9.0.0 with Kotlin DSL
- **Platform**: IntelliJ Platform Plugin SDK (plugin gradle plugin 2.10.2)
- **Min IDE Build**: 252.25557

## Build Commands

```bash
# Build the plugin
./gradlew build

# Run IntelliJ IDEA with the plugin loaded (for manual testing)
./gradlew runIde

# Run tests
./gradlew test

# Verify plugin compatibility
./gradlew verifyPlugin

# Publish to JetBrains Marketplace
./gradlew publishPlugin
```

## Architecture

插件功能：在 IntelliJ Project 视图右键菜单中添加 "Open in Claude" 选项（快捷键 `Alt+Shift+C`），在选中目录打开系统终端并自动执行 `claude` 命令。

**包结构** (`io.refactoring.aiterminal`):

- `OpenInClaudeAction` — IntelliJ Action 入口，注册在 `ProjectViewPopupMenu` 组。负责解析选中文件/目录路径，调用 TerminalLauncher，异常时通过 IntelliJ 通知系统反馈。`update()` 方法控制菜单可见性（多选时隐藏）。
- `ClaudeIcons` — 图标常量，`@JvmField` 注解使 plugin.xml 可通过反射访问。
- `terminal/TerminalLauncher` — 根据 `SystemInfo` API 分发到对应平台的启动器。
- `terminal/WindowsTerminalLauncher` — 优先级：Windows Terminal → PowerShell → CMD。使用 `where` 命令检测。
- `terminal/MacTerminalLauncher` — 优先级：iTerm2 → Terminal.app。使用 AppleScript 启动。
- `terminal/LinuxTerminalLauncher` — 候选列表：x-terminal-emulator → $TERMINAL → gnome-terminal → konsole → xterm。使用 `which` 检测。

**关键模式**：各平台启动器均为 `object` 单例，内部按优先级依次检测可用终端（责任链），统一使用 `GeneralCommandLine` 启动进程。

**插件配置**：`src/main/resources/META-INF/plugin.xml` 声明 Action 注册、通知组（`OpenInClaude`，BALLOON 类型）和平台依赖。

**设计文档**：`docs/v1/` 下有需求规格(spec.md)、技术方案(plan.md)和任务清单(tasks.md)。

## Other
- 我的母语是中文，尽量使用中文回答
- 不要过度设计