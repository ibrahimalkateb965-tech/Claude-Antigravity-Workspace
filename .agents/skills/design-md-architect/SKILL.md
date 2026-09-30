---
name: design-md-architect
description: "Autovem DESIGN.md & Visual System Architect — creates, analyzes, and standardizes AI-readable DESIGN.md files according to Google Stitch, Refero Styles, and VoltAgent specifications. Bridges the gap between AGENTS.md (how to build) and DESIGN.md (how it looks and feels), defining color tokens, typography scales, spacing units, elevation, and component aesthetics with strict Arabic/RTL compliance."
---

# DESIGN.md Architect & Visual System Specialist

The `DESIGN.md` file is the canonical design specification read by AI coding agents (<bdi>Claude Code, Cursor, Codex, Gemini CLI, Antigravity</bdi>) to guarantee visual consistency and aesthetic excellence.

```
┌─────────────────────────────────────────────────────────────┐
│                       PROJECT ROOT                          │
├──────────────────────────────┬──────────────────────────────┤
│          AGENTS.md           │          DESIGN.md           │
│     (Read by Coding Agents)   │     (Read by Design Agents)  │
│      How to build logic      │   How it should look & feel  │
└──────────────────────────────┴──────────────────────────────┘
```

---

## 1. When to Use

- When starting a new web or mobile project and establishing its visual identity.
- When extracting design tokens from an existing website or reference brand (<bdi>Refero Styles</bdi> / <bdi>Awesome DESIGN.md</bdi>).
- When standardizing color palettes, typography scales, spacing grids, and component styles across a team.
- When an AI agent generates generic, bland, or inconsistent UI ("AI Slop").

---

## 2. Standard DESIGN.md Specification Structure

Every generated `DESIGN.md` MUST contain these core sections:

```markdown
# DESIGN.md — [Project Name] Visual System & Design Tokens

## 1. Aesthetic Identity & Design Direction
- **Brand Archetype**: (e.g. Modern Developer Tool, Cinematic Dark, Warm Editorial, Clean FinTech)
- **Visual Mood**: (e.g. Sharp typography, deep void-black canvas, neon cyber accents, glassmorphic sheen)
- **Reference Inspirations**: (e.g. Linear, Vercel, Apple, Superhuman, Teenage Engineering)

## 2. Color Palette & Semantic Tokens
- **Backgrounds**: Canvas (`--bg-canvas`), Surface (`--bg-surface`), Elevated (`--bg-elevated`)
- **Foregrounds**: Primary text (`--text-primary`), Secondary (`--text-secondary`), Muted (`--text-muted`)
- **Brand / Accent**: Primary (`--accent-primary`), Hover (`--accent-hover`), Subtle glow (`--accent-glow`)
- **Status Colors**: Success, Warning, Danger, Info
- **Border Tokens**: Hairline borders (`--border-subtle`: 1px solid rgba(255,255,255,0.08))

## 3. Typography Hierarchy & Arabic Font Pairing
- **Header Family**: (e.g. Outfit / Cairo / Inter Display)
- **Body Family**: (e.g. Inter / Tajawal / IBM Plex Sans Arabic)
- **Monospace Family**: (e.g. JetBrains Mono / Fira Code)
- **Scale Tokens**:
  - `display`: clamp(2.5rem, 5vw, 4rem) | line-height: 1.1 | tracking: -0.02em
  - `h1`: 2rem (32px) | line-height: 1.2
  - `h2`: 1.5rem (24px) | line-height: 1.3
  - `body`: 1rem (16px) | line-height: 1.6
  - `caption`: 0.8125rem (13px) | line-height: 1.4

## 4. Spacing Scale & Layout Grid
- Base unit: 4px / 8px grid (`4px`, `8px`, `12px`, `16px`, `24px`, `32px`, `48px`, `64px`)
- Container constraints: max-width `1200px` for landing, `1440px` for dashboards.
- Safe print margins: >= 25pt.

## 5. Surfaces, Elevation & Borders
- Border radius tokens: `--radius-sm: 6px`, `--radius-md: 10px`, `--radius-lg: 16px`, `--radius-pill: 9999px`
- Shadows: Multi-layered soft ambient shadows, subtle inner highlights (`box-shadow: inset 0 1px 0 0 rgba(255,255,255,0.1)`).

## 6. Motion & Interactive States
- Hover transitions: `cubic-bezier(0.16, 1, 0.3, 1)` in `180ms`
- Focus states: High-visibility 2px focus ring with 2px offset (`outline: 2px solid var(--accent-primary)`)

## 7. Arabic & BiDi / RTL Invariants
- All layout properties MUST use logical properties: `margin-inline-start`, `padding-inline-end`.
- Numbers MUST use Western Arabic numerals (0-9) strictly (Rule 50).
- Wrap Latin brands and code symbols in `<bdi>` or `<span dir="ltr">`.
```

---

## 3. Reference Palettes (from Refero & Awesome DESIGN.md)

### A. Dark Modern Tech (Linear / Vercel Aesthetic)
```css
:root {
  --bg-canvas: #090a0f;
  --bg-surface: #12141c;
  --bg-elevated: #1a1d29;
  --border-subtle: rgba(255, 255, 255, 0.08);
  --border-active: rgba(255, 255, 255, 0.16);
  --accent-primary: #6366f1;
  --accent-glow: rgba(99, 102, 241, 0.25);
  --text-primary: #f8fafc;
  --text-secondary: #94a3b8;
  --text-muted: #64748b;
}
```

### B. Warm Cream Editorial (ElevenLabs / Perplexity Aesthetic)
```css
:root {
  --bg-canvas: #fcfbf9;
  --bg-surface: #f5f3ef;
  --bg-elevated: #ffffff;
  --border-subtle: #e6e2db;
  --accent-primary: #b45309;
  --text-primary: #1c1917;
  --text-secondary: #57534e;
  --text-muted: #8c857b;
}
```

---

## 4. Verification & Quality Checklist

Before finalizing any `DESIGN.md`:
- [ ] Are semantic tokens named for their role (`--bg-surface`), not literal color (`--dark-blue`)?
- [ ] Is contrast ratio verified against WCAG AA standards (>= 4.5:1 for text)?
- [ ] Are Arabic fonts paired correctly (Cairo/Tajawal or IBM Plex Sans Arabic)?
- [ ] Are directional rules expressed via CSS logical properties?
