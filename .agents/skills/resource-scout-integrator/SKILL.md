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

4. **Design Resources & UI/UX Curation Track (مسار مراجع التصميم والواجهات - Hook 31)**:
   - When scouting UI/UX platforms, design component galleries, or animation libraries:
     * Activate Hook 31 (`fleet_teamwork_orchestrator.py`) to scaffold parallel survey (M1), implementation (M2), adversarial review (M3), and forensic audit (M4).
     * Enforce physical `handoff.md` evidence files for all participating roles complying with `[C-100]` and `[C-102]`.
     * Audit Arabic BiDi/RTL reading flow (top-right orientation for Bento cards, mirrored CTA arrows, >= 48dp mobile touch targets).
     * Enforce GPU-only 60fps micro-interaction budgets (`transform`, `opacity`) and `prefers-reduced-motion` compliance (`[C-105]`).
     * Curate ready prompt formulas into `docs/DESIGN_RESOURCES_VAULT.md` and index them in `Brain/00_Design_Vault.md`.

5. **Hooks, Excel & HTML Web App Integration (تحديث الخطافات والإكسيل وصفحة الـ HTML)**:
   - Integrate new skills into relevant existing hooks in `HOOKS_GUIDE.md` (Local and Global).
   - If a new domain is introduced, propose and append a new hook.
   - Run `python .agents/convert_hooks_to_sheets.py` (or `F:\AI PROJECTS\Claude+Antigravity\convert_hooks_to_sheets.py`) to regenerate `HOOKS_GUIDE.xlsx`.
   - **MANDATORY**: Run `python update_html.py` to regenerate and sync the interactive Smart Prompt Library web app (`03_Dynamic_Prompt_Library/index.html`) across all active workspaces.
   - Curate and register corresponding prompts into `مكتبة الأوامر.xlsx` via `prompt_library_curator.py` or `curate_design_prompts.py` (Rule 17 / Hook 26).

6. **Autonomous Workspace Sync (المزامنة السحابية)**:
   - Run `python .agents/sync_global_ecosystem.py` to copy all skills, sub-agents, rules, and documentation to `Claude-Antigravity-Workspace` and push to GitHub.
