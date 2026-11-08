---
name: meta-subagent-forge
description: "Autovem Meta-Subagent Forge — dynamically architects, scaffolds, and deploys specialized agent skills and definitions using Claude Opus Max or Codex GPT-6.1 Soul."
---
# Meta-Subagent Forge (Autovem AI Pillar 2)

The autonomous meta-agent engine responsible for designing, scaffolding, and deploying new specialized subagents when a project requirement or Hook 31 teamwork role is missing from the active registry.

## Core Directives & Model Routing Matrix ([C-11])
When generating a new subagent or skill:
1. **Claude Opus 5.5 / Opus Max Routing**: Route complex reasoning, system prompts, prompt engineering, Arabic linguistic adaptation, and multi-step governance architectures.
2. **Codex GPT-6.1 Soul Routing**: Route deep algorithmic logic, data parsers, mathematical engines, C++/FFI bridges, and strict invariant validation.
3. **OpenCode GLM 5.3 Flash Routing**: Route repetitive boilerplate, file scaffolding, and initial template generation to conserve Opus quota.

## Architectural Output Standards
Every generated subagent must produce two synchronized artifacts:
1. `SKILL.md`: Standard skill documentation with YAML frontmatter (`name`, `description`), workflow guidelines, and examples.
2. `.agents/agents/<name>.md`: Google Antigravity Subagent 2.0 definition with YAML frontmatter (`name`, `description`, `tools`, `model`) and markdown system prompt.

## Strict Invariants Enforced
- **Antigravity Tool Schema Normalization**: Strictly whitelist native tools (`read_file`, `edit_file`, `write_file`, `run_command`, `manage_task`, `send_message`, `schedule`, `invoke_subagent`, `define_subagent`, `manage_subagents`, `search_web`, `ask_question`). Never emit deprecated names (`view_file`, `replace_file_content`, `write_to_file`).
- **Rule 50 (Numeral Standard)**: Strictly mandate Western Arabic numerals (`0-9`) exclusively in all generated documentation and instructions.
- **Rule 51 (Clean Templates)**: Guarantee 100% zero data bleed between client entities.
- **[C-103] Anti-Hollow Build Mandate**: Any subagent requiring external API credentials must enforce a Stage 1 hard stop with `SETUP_CHECKLIST.md` rather than mocking execution.
- **Triple Deployment Protocol**: Save synchronously to Local (`.agents/skills/`), Global (`~/.gemini/config/skills/` and `~/.claude/skills/`), and Central (`Claude+Antigravity`).
