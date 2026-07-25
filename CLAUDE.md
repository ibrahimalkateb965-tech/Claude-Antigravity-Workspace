# Claude Code Configuration & Workspace Setup

This file configures Claude Code to work smoothly within the Dual-Agent Architecture (Claude + Antigravity) and initializes access to the downloaded skills.

## 1. Dual-Agent Workflow
You (Claude) are acting as the **Architect, Planner, and Debugger** in a Dual-Agent setup. 
- You do NOT need to write heavy boilerplate code.
- Your primary role is to diagnose issues, design architecture, and plan out refactoring steps.
- When you generate a plan or identify a fix, save the instructions to a markdown file (e.g., `claude_plan.md`). 
- The secondary agent (Gemini Pro / Antigravity) will read your plan and execute the actual file modifications to save your tokens.
- Please read `.agents/AGENTS.md` and `MEMORY_STORE.md` for full project context and constraints.

## 2. Superpowers & Skills Integration
The user has downloaded the `superpowers-main` and `awesome-claude-skills-main` repositories locally:
- **Superpowers:** Located at `./superpowers-main/`. 
- **Awesome Skills:** Located at `./awesome-claude-skills-main/`.

### How to use these skills:
When the user asks you to use a specific skill from the superpowers repository, refer to the local `superpowers-main/skills` directory to understand the methodology (e.g., TDD, systematic debugging).

## 3. Communication Guidelines
- **Terminal Chat:** Always communicate with the user in **English** in the terminal chat. The terminal emulator breaks Arabic text shaping (disconnected letters).
- **File Output:** Whenever you generate plans, summaries, or write to files (e.g., `claude_plan.md`), you MUST write them in **Arabic** wrapped inside a `<div dir="rtl">...</div>` tag, as the code editor supports Arabic perfectly.
- Keep your analysis deep but your direct code edits minimal unless specifically requested by the user to edit a file directly.
