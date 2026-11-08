---
name: verify-flowchart-studio
description: "Launch, check, drive, prove, and bug bash the Autovem Flowchart Studio web app with e2e. Use before claiming a Flowchart Studio change works, when asked to verify or screenshot a diagram, or to bug bash it."
---

# Verify Autovem Flowchart Studio

Read `.agents/skills/e2e/SKILL.md` first. This skill defines verification procedures for Autovem Flowchart Studio (`tools/flowchart_studio`). The runner config is `e2e.config.ts` (target `web`, tests `tests/**/*.e2e.ts`).

## Launch

The target's `app.command` runs `python -m http.server 3000 --directory tools/flowchart_studio`.
The e2e runner starts it automatically on port 3000, awaits readiness at `http://127.0.0.1:3000`, and terminates it when tests complete.

## Doctor

```bash
npx e2e list tests/flowchart-studio.e2e.ts       # loads config and lists test pairs; exit 0
npx e2e run tests/flowchart-studio.e2e.ts        # runs live studio verification suite; exit 0
```

## Drive

- **Semantic Locators**:
  - Editor: `#mermaidCode`
  - Template Selector: `#templateSelect`
  - Palette Selector: `#paletteSelect`
  - Render Button: `#btnRender`
  - SVG Diagram Stage: `#diagramSvgContainer`
  - Zoom Controls: `#btnZoomIn`, `#btnZoomOut`, `#btnZoomReset`, `#zoomBadge`
  - Sidebar Toggle: `#btnToggleSidebar`
  - Export Controls: `#btnExportPng`, `#btnExportSvg`, `#btnCopySvg`
- **Scripted**: Run `npx e2e run tests/flowchart-studio.e2e.ts`.

## Evidence

Every run produces `.e2e/report.json`.
Artifacts are stored in `.e2e/artifacts/web/`.
Trace summaries appear in `.e2e/results/`.

## Feature Map

Feature specifications and explore charters live in `features/`:
- `features/README.md`: Index of mapped features and verification status.
- `features/templates.md`: Architectural template switching and Mermaid syntax parsing.
- `features/palettes.md`: Contrast-compliant theme palettes (Candy, Indigo, Amber, Light).
- `features/export.md`: High-res 4K PNG rasterization and lossless SVG export.
