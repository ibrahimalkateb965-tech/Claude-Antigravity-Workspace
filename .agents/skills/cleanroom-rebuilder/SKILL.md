---
name: cleanroom-rebuilder
description: "Autovem Clean-Room Rebuilder & Software Reconstruction Engine — implements the clean-room methodology (Study → Build → Check → Ready) from Craft Anything, enabling agents to study any website, app, or UI, inspect layouts and behavior with evidence, and direct coding agents to recreate production-grade clones with zero IP infringement."
---

# Clean-Room Rebuilder & Software Reconstruction Engine

Based on the clean-room reverse engineering architecture pioneered by **Craft Anything** (`storytold/craft-anything`).

This skill guides agents through understanding how any existing web app, mobile app, desktop tool, or UI component operates, and guiding the Multi-CLI fleet to engineer a clean, high-performance, and IP-safe reproduction from verifiable evidence.

---

## 🏛️ The 4-Stage Reconstruction Pipeline

```
┌─────────────────┐       ┌─────────────────┐       ┌─────────────────┐       ┌─────────────────┐
│     1. STUDY    │  ───► │     2. BUILD    │  ───► │     3. CHECK    │  ───► │    4. READY     │
│ Evidence & Spec │       │ Code Generation │       │ Multi-View Diff │       │ Polish & Refine │
└─────────────────┘       └─────────────────┘       └─────────────────┘       └─────────────────┘
```

### Stage 1: Study (استقصاء الأصل بالأدلة)
- **Target Analysis:** Deconstruct the target (URL, app window, design mockups, or source assets).
- **Evidence Ledger:** Never guess or hallucinate. Record concrete findings:
  - Responsive layout breakpoints (Mobile: 375px/390px, Tablet: 768px, Desktop: 1440px).
  - DOM structure, hierarchy, and semantic HTML elements.
  - Computed design tokens: exact hex/oklch colors, typography scale, spacing units, and border radiuses.
  - Event listeners, interactive states (hover, active, focus, disabled), and animation curves.
- **Output:** Formulate a structured `RECONSTRUCTION_SPEC.md` capturing all observed behaviors and assets.

### Stage 2: Build (البناء النظيف)
- **Zero Cross-Contamination (Clean Room):**
  - Do not copy proprietary vendor assets, logos, copyrighted copy, or obfuscated internal code.
  - Re-author business logic, state machines, and styling cleanly using modern stacks (Tailwind CSS, React/Next.js, Jetpack Compose, or Flutter).
  - Division of Labor:
    - **OpenCode CLI (GLM 5.3 Flash / Spark):** Scaffolding, HTML boilerplate, component shells, and database schemas.
    - **Antigravity CLI (agcli):** Jetpack Compose, reactive UI states, and responsive styling.
    - **Claude Code CLI (Opus Max):** Core domain logic, complex math/algorithms, and architectural oversight.

### Stage 3: Check (الفحص والمطابقة الصارمة)
- **Deterministic Comparison:**
  - **Pixel & Layout Verification:** Test the build side-by-side at 3 screen widths against the original or spec.
  - **Behavioral Walkthrough:** Verify interactive flows (modals, dropdowns, form validation, error states).
  - **Console & Network Cleanliness:** Zero uncaught exceptions, zero console warnings, and proper API error handling.
- **Evidence Gap List:** Generate an explicit delta matrix listing remaining discrepancies before proceeding.

### Stage 4: Ready & Refine (التسليم والتحسين المستمر)
- Fine-tune micro-interactions and transitions using `impeccable-design-guard` and `motion-transitions-pro`.
- Deliver ready-to-run code with clear setup instructions and automated tests.

---

## 🛡️ Clean Room vs. Deep Mode Policy

| Dimension | Clean Room Mode (Default) | Deep Mode (Authorized Private Use) |
| :--- | :--- | :--- |
| **Input Source** | External observation (DOM, visual layout, public behavior) | Disassembly, decompilation, binary/APK bytecodes |
| **IP Protection** | 100% clean-room compliant; zero copyrighted asset reuse | Private security auditing and legacy interoperability |
| **DRM / Protection** | Strictly preserves and respects access controls & DRM | Strictly for owned/authorized security analysis |
| **Tooling** | Browser DevTools, `webapp-qa-tester`, screenshots | Ghidra, Rizin, JEB, ILSpy, APKTool, Frida |

---

## 🛠️ CLI & MCP Server Integration

When `craft-anything-cli` is installed locally:
- **Expose MCP Server:**
  ```bash
  craft-anything-cli mcp --clean-room
  ```
- **Autonomous CLI Rebuild:**
  ```bash
  craft-anything-cli rebuild <target_url> "<rebuild_instructions>"
  ```
- **Fallback Engine:**
  If `craft-anything-cli` is absent, utilize the native Autovem fleet tools:
  - Inspection: `webapp-qa-tester` (Playwright / Chrome DevTools) + `impeccable-design-guard`.
  - Architecture: `code-architect` + `ui-ux-pro-max`.
  - Sign-off: Claude Code CLI testing monopoly ([C-21] / [C-96]).
