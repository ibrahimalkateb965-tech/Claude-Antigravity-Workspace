---
name: resource-scout-integrator
description: "Autonomous agent skill for evaluating external open-source repositories/resources, extracting high-value skills, deploying them globally to ~/.gemini/config/skills/, updating system hooks and Excel sheets, and syncing the entire ecosystem to Claude-Antigravity-Workspace."
---

# Resource Scout & Ecosystem Integrator Skill

When provided with any GitHub repository, tool URL, or resource package:

## Operational Workflow

1. **Evaluation & Value Audit (التحليل والتقييم)**:
   - Read the repository README, source code, and API architecture.
   - Determine whether it introduces a breakthrough ("نقلة نوعية"), duplicate capability, or dependency bloat.
   - Filter out platform-incompatible components (e.g. PHP/WordPress plugins when working on Mobile/Web).

2. **Extraction & Transformation (الاستخراج والتحويل للمواصفات)**:
   - Extract portable instructions, best practices, and guard rules.
   - Structure each skill with valid YAML frontmatter (`name`, `description`).
   - Define concrete imperatives, anti-patterns, and delivery self-checks.

3. **Global Deployment (التعميم العالمي)**:
   - Deploy skill to local workspace `.agents/skills/<skill-name>/SKILL.md`.
   - Deploy skill to Global Configuration Root `C:\Users\Kt\.gemini\config\skills\<skill-name>/SKILL.md`.

4. **Hooks & Workflow Integration (تحديث الخطافات والإكسيل)**:
   - Integrate new skills into relevant existing hooks in `HOOKS_GUIDE.md` (Local and Global).
   - If a new domain is introduced, propose and append a new hook.
   - Run `python .agents/convert_hooks_to_sheets.py` to regenerate `HOOKS_GUIDE.xlsx`.

5. **Autonomous Workspace Sync (المزامنة السحابية)**:
   - Run `python .agents/sync_global_ecosystem.py` to copy all skills, sub-agents, rules, and documentation to `Claude-Antigravity-Workspace` and push to GitHub.
