---
name: fleet-orchestrator
description: "Master Multi-CLI Fleet Orchestration Engine: coordinates cooperative agent fleets (Claude Code CLI Opus Max, OpenCode CLI Ox Alpha Unlimited / Local Ollama, Antigravity CLI, Antigravity IDE) with strict token economies, modular delegation templates, exclusive Claude Code testing monopoly, and mandatory phase code review quality gates."
---

# 🚀 Fleet Orchestrator Skill

Autonomous skill for establishing, configuring, and governing multi-CLI agent fleets across projects.

## 🧭 Multi-Agent Fleet Matrix

| CLI Agent | Typical Model | Primary Architectural Role | Token / Effort Strategy |
| :--- | :--- | :--- | :--- |
| **Claude Code CLI** | Claude 5 Opus (Opus Max) / Sonnet 5 | **Master Orchestrator, Sole Test Authority & Senior Reviewer** | Zero boilerplate generation; high-leverage architectural plans, diff auditing, and **exclusive test suite execution**. |
| **OpenCode CLI** | `qwen2.5-coder:7b` (Local Ollama) / Ox Alpha Unlimited / Meta Muse Spark 1.3 | **Terminal Executor & DB/Remote Layer (No Testing)** | High-speed terminal commands, ORM generation, builds, network clients, and CI workflows. |
| **Antigravity CLI (`agcli`)** | Gemini 3.8 Flash High / `hermes3:8b` (Local Ollama) | **Autonomous UI & Domain Code Generator (No Testing)** | Headless CLI direct invocation (`agcli run`). UI components, Compose/SwiftUI adaptation, and Domain logic without requiring manual chat copy/paste. |
| **Antigravity IDE** | Gemini 3.8 Flash High | **Interactive Developer Workbench & Session Cockpit** | Interactive human-AI pair programming, visual diffs, workspace intelligence, and persistent memory review. Not a target for programmatic CLI order assignment. |

### ⚡ Local Ollama Fleet Matrix (Hardware: GTX 1650 4GB VRAM + 16GB RAM | Storage: `D:\Ollama\models`)
* **`qwen2.5-coder:7b`**: Primary coding, tool calling, functions, and shell execution (Default for OpenCode CLI).
* **`deepseek-r1:7b`**: Lightweight logic reasoning, architectural sanity check, and local verification.
* **`hermes3:8b`**: Agentic workflows, structured instructions, and multi-step tool execution.
* **`qwen2.5-coder:1.5b`**: Ultra-fast terminal commands, log parsing, and lightweight text processing.

---

## 🔄 Mandatory Fleet Pipeline Workflow
 
Every feature phase follows a 4-step quality cycle:
1. **Task Delegation:** Issue structured Delegate Prompt (`02_TASK_DELEGATION.md`) or run direct CLI commands via headless mode:
   - For Claude Code CLI Code Review & Automated Tests: `claude -p "<review_and_test_prompt>"`
   - For OpenCode CLI Terminal Execution & DB/Client builds: `opencode run "<build_prompt>"`
   - For Antigravity CLI UI/Domain generation: `agcli run "<build_prompt>"` (or headless execution)
   <!-- Programmatic orders target CLI agents directly. Never assign CLI orders to Antigravity IDE which requires human copy-paste. -->
2. **Implementation & Static Verification:** Target CLI builds code and confirms static analysis passes (`flutter analyze = 0`). **No testing commands are run by executor CLIs.**
3. **Mandatory Test Execution & Quality Review:** Submit diffs to Claude Code (Opus Max). Claude Code executes all automated tests (`flutter test`) and performs Devil's Advocate Audit.
4. **Architectural Sign-off:** Claude Code issues the formal `[APPROVED]` verdict and logs ADR in `MEMORY_STORE.md` before unlocking the next Phase.

---

## 🛡️ Core Governing Principles
1. **Language Discipline:** 100% English only in all CLI terminal interactions, delegate prompts, and commit messages.
2. **Zero-Collision Boundaries:** Each CLI strictly modifies files within its assigned ownership tree.
3. **Exclusive Testing & Code Approval Monopoly:** Claude Code CLI (Opus Max) is the single, exclusive agent permitted to run automated test suites and approve/reject code written by other agents. All other agents are strictly forbidden from running tests.
4. **Direct Headless Execution:** When CLIs (`claude`, `opencode`) are installed on the OS PATH, leverage direct non-interactive CLI flags (`-p` / `run`) to execute multi-model reviews, tests, and tasks automatically.
5. **Session-Wide Fleet Mode Transformation (Hook 22 Activation):** When Hook 22 is invoked (triggers: "أسطول الـ CLIs", "تشغيل الأسطول", "تنسيق الأسطول", "fleet orchestrator", "multi cli fleet", "إدارة الأسطول"), all subsequent instructions in the conversation automatically transition into structured Multi-CLI fleet delegation prompts (Templates 02 & 03) in English without requiring re-triggering.
6. **Quota Failover Protocol:** If one CLI hits rate limits, other CLIs seamlessly assume acting leadership using `MEMORY_STORE.md` snapshots.
7. **Hardware Concurrency & Storage Guard:** On constrained local hardware (e.g. <= 4GB VRAM), execute models in `sequential_role_switching` mode to prevent memory thrashing, and enforce Directory Junctions (`mklink /J`) for local model caches to protect the OS root disk.
