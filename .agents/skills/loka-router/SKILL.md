---
name: loka-router
description: Select the correct original Loka framework version for an Agent task without mixing frameworks that solve different environments.
---

# Loka Framework Router

Answer the first matching question and stop. Do not load multiple versions by
default. The sole designed stack is **LokaV12 on top of LokaV11**.

Each original environment route also has a **Lite** form. Use its Lite form when
the user requests a compact, one-page, copy-paste prompt or has limited context.
Lite replaces the matching Full form for that task; it is not an extra overlay.

1. Is the task vague, strategic, research/content/workflow-oriented, or in need
   of a structured brief before execution? Use **Loka ACE**.
2. Is it prompt architecture, app/SaaS planning, implementation planning, or a
   technical audit without repository/shell execution? Use **LokaV10**.
3. Can the Agent inspect files, edit a repository, run commands, or use coding
   tools? Use **LokaV11**. Load **Loka Guard** first; use **Loka Build** for
   implementation after the task is understood. Use V11 Surgical Fix Mode for
   a targeted existing-repo change.
4. Is that V11 work a supervised 1–3 hour session with periodic human review?
   Add **LokaV12 Supervised** or its original **V12 Lite** form on top of V11.
5. Is it a deliberately bounded autonomous mission under a documented
   authority contract? Add the separate **LokaV12 Autonomous Edition** or its
   compact **Autonomous Edition Lite** on top of V11. It is distinct from
   supervised V12; choose one V12 edition only.
6. Is the work happening in a prompt-driven visual builder or agentic visual
   IDE? Use **LokaV13**. After export to a standard repo/IDE, stop V13 and
   switch to V11.

Use **Loka Failures** as a planning or review reference when relevant; it is a
failure catalogue, not a competing execution version. Do not use Build to
override a project architecture contract; do not use Guard to override host
platform policy or the user's current instruction.

See `LOKA_INDEX.md` for the complete Full, Lite, and overlay mapping.
