---
name: context-governance
description: Strategic context window management, telemetry auditing with ag-context, health zone diagnostics, and controlled session compaction/handoff execution in Antigravity IDE and agcli.
---

# Context Governance & Telemetry HUD Skill

## Overview
This skill governs the context window lifecycle in **Antigravity IDE** and **agcli**, ensuring zero context overflows, high attention fidelity, and clear architectural decisions (Continue, Prune, Compact, or Strategic Clear).

## Primary Telemetry Commands

```bash
# 1. Quick context snapshot (tokens, percentage, headroom)
ag-context

# 2. Strategic decision & health report
context-advisor
# or
ag-context -a

# 3. Live watch mode (refreshes every N seconds)
ag-context -w 3

# 4. Telemetry history across last N steps
ag-context -H 5

# 5. Programmatic JSON export
ag-context -j
context-advisor -j
```

## The 4 Health Zones & Golden Sweet Spot Matrix [30k - 300k]

| Zone | Threshold (of 300k Sweet Spot) | Cognitive Status | Tactical Decision | Action |
| :--- | :--- | :--- | :--- | :--- |
| 🟢 **Green** | `< 180k` (< 60%) | Peak Accuracy & Zero Fatigue | Normal Development | Keep using `lean-ctx` (signatures mode). Stay in sweet spot. |
| 🟡 **Yellow** | `180k - 240k` (60% - 80%) | Sweet Spot Warning | Selective Reading | Avoid `mode='full'` for large files. Avoid dumping terminal logs. |
| 🟠 **Orange** | `240k - 300k` (80% - 100%) | Pre-Handoff Planning | Stabilize & Commit | Finish current sub-task, commit to git, save lessons in `MEMORY_STORE.md`. |
| 🔴 **Red** | `> 300k` (> 100%) | Exceeded Sweet Spot | Immediate Handoff | **Mandatory Hook 25**: Freeze state, generate resume prompt, execute `/clear`. |

## Integration with Hook 25 (Strategic Clear)
When in **Orange** or **Red** zones:
1. Ensure the workspace compiles cleanly (`100% Green`).
2. Commit all staged work: `git commit -m "..."`.
3. Update `fleet_orders/CURRENT_STATE.md` or `PROJECT_CONTEXT.md`.
4. Generate the exact one-line resume prompt for the user.
5. Give the explicit green light for `/clear`.
