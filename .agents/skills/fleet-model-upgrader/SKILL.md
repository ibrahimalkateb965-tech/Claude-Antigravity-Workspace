---
name: fleet-model-upgrader
description: "Autonomous engine for auditing, upgrading, and synchronizing AI model tiers across the Multi-CLI fleet (Claude Code Opus 5.5 / Sonnet 5.5, Gemini 3.8 Flash High, GLM 5.3 Flash, Codex GPT-5.6 Soul), updating technical invariants [C-11], [C-21], [C-38], fleet_config.json, and interactive prompt libraries."
---

# Fleet Model Matrix Upgrade & Calibration Engine (`fleet-model-upgrader`)

## 1. Overview & Fleet Matrix Baseline

The `fleet-model-upgrader` skill autonomously audits, upgrades, and synchronizes the active model tiering matrix across the Antigravity Multi-CLI Fleet ecosystem. It enforces strict separation of concerns, token budget conservation, and exclusive testing monopolies.

### Current 5.5 Generation Active Matrix:

| Role / Component | Tool / Environment | Active Model (5.5 Generation) | Token Strategy & Boundaries |
| :--- | :--- | :--- | :--- |
| **Master Orchestrator & Testing Monopoly** | `Claude Code CLI` | **`Claude Opus 5.5 (Opus Max)`** | **Exclusive testing monopoly** (`flutter test`, unit tests). Architectural sign-offs and Devil's Advocate audits. Zero routine boilerplate writing. |
| **Feature Lead & Daily Dev** | `Claude Code CLI` / Headless | **`Claude Sonnet 5.5`** | Clean feature engineering, component refactoring, and domain logic. |
| **Headless Terminal & Data Plumbing** | `OpenCode CLI` | **`GLM 5.3 Flash`** & **`Meta Muse Spark 1.3`** | Free/unlimited headless terminal commands (`--auto`), database migrations (Drift/SQLite), DAOs, and API client plumbing. Preserves Claude quota. |
| **Visual Cockpit & Domain UI** | `Antigravity CLI / IDE` | **`Gemini 3.8 Flash (High)`** | Interactive visual workbench, UI scaffolding, and rapid component prototyping. |
| **Algorithmic Deep Refactoring** | `Codex CLI` | **`GPT-5.6 Soul`** | Algorithmic transforms, deep AST refactorings, and specialized math/performance modules. |

---

## 2. Upgrade Workflow & Invariants

When triggered via `"تحديث نماذج الأسطول"` or `"upgrade fleet models"`:

1. **Audit Active Files (الفحص والمطابقة):**
   - Scan all `fleet_config.json` files across active project workspaces.
   - Verify that Claude Code CLI is set to `Claude 5.5 Opus (Opus Max) / Sonnet 5.5`.
2. **Update Technical Invariants (تحديث القيود العالمية):**
   - Synchronize [C-11], [C-21], and [C-38] in `ACTIVE_CONTEXT_INJECTION.md`.
3. **Regenerate Dynamic Assets (تحديث أصول الويب والإكسيل):**
   - Run `python update_html.py` to sync all interactive HTML Prompt Libraries (`03_Dynamic_Prompt_Library/index.html`).
   - Run `python convert_hooks_to_sheets.py` to regenerate `HOOKS_GUIDE.xlsx`.
   - Curate and register corresponding prompts into `مكتبة الأوامر.xlsx`.
4. **Memory Ledger Entry (توثيق القرار المعماري):**
   - Record an Architectural Decision Record (ADR) in `MEMORY_STORE.md` detailing the upgraded generation.

---

## 3. Automation Script (`upgrade_fleet_models.py`)

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Fleet Model Matrix Upgrader Script
"""
import os
import json
import re

def upgrade_fleet_configs(search_paths):
    upgraded = 0
    for base in search_paths:
        if not os.path.exists(base):
            continue
        for root, dirs, files in os.walk(base):
            if "fleet_config.json" in files:
                p = os.path.join(root, "fleet_config.json")
                try:
                    with open(p, "r", encoding="utf-8") as f:
                        data = json.load(f)
                    changed = False
                    for agent in data.get("fleet_agents", []):
                        if agent.get("id") == "orchestrator":
                            agent["active_model"] = "Claude 5.5 Opus (Opus Max) / Sonnet 5.5"
                            changed = True
                    if changed:
                        with open(p, "w", encoding="utf-8") as f:
                            json.dump(data, f, ensure_ascii=False, indent=2)
                        upgraded += 1
                        print(f"✅ Upgraded: {p}")
                except Exception as e:
                    print(f"❌ Error in {p}: {e}")
    return upgraded
```

---

## 4. Verification & Guardrails

- [ ] Claude Code CLI testing monopoly remains 100% exclusive to Opus 5.5 Max.
- [ ] No automated background tasks delegated to Antigravity IDE workbench.
- [ ] OpenCode CLI models remain on GLM 5.3 Flash / Muse Spark 1.3 for terminal boilerplate conservation.
- [ ] All updated documentation strictly adheres to English numerals standard (0-9) [C-50].
