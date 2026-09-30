---
name: impeccable-design-guard
description: "Autovem Impeccable Design Quality & Anti-Slop Guard — based on Paul Bakaus's Impeccable framework and The Component Gallery taxonomy. Enforces human-grade visual hierarchy, typography tension, contrast ratios (WCAG AA), accessible component states, and purges generic AI slop from web and mobile interfaces."
---

# Impeccable Design Quality & Anti-Slop Guard

This skill arms AI agents with the rigorous design critique and auditing vocabulary necessary to eradicate generic "AI Slop" and elevate interfaces to human-crafted, premium-grade execution.

---

## 1. What is "AI Slop" in Frontend UI?

AI coding models have distinct aesthetic failure modes:
1. **Generic Gray Syndrome**: Flat, lifeless gray backgrounds (`#f3f4f6`, `#1f2937`) lacking tint or brand warmth.
2. **Hierarchy Flattening**: Giving every button, badge, and card equal visual weight (everything is bold, everything has the same shadow).
3. **Floating Island Cards**: Nesting 4 layers of cards with 16px padding inside cards with no clear structural purpose.
4. **Broken Spatial Rhythm**: Inconsistent spacing (mixing 13px, 19px, 27px randomly instead of adhering to a strict 4/8px scale).
5. **Absence of Micro-details**: Missing active states, keyboard focus rings, skeleton loaders, and tactile hover feedback.

---

## 2. The Impeccable 4-Step Auditing Protocol

### Step 1: `/impeccable init` (Direction & Point of View)
Before generating code, define the **Point of View (POV)**:
- Who is this for? What emotion should it evoke? (e.g. "Precision technical workstation" vs "Friendly consumer app").
- Pick ONE dominant visual idea (e.g. sharp geometric borders, soft ambient glow, or dense editorial grid).

### Step 2: `/impeccable audit` (Structural & Contrast Check)
Run a ruthless pass against the implementation:
- **Contrast Check**: Background-to-foreground contrast must meet **WCAG AA** (>= 4.5:1 for normal text, >= 3:1 for large text).
- **CTA Hierarchy**: Exactly ONE Primary CTA per viewport. Secondary actions must be ghost, subtle, or text-only.
- **Form Controls**: Labels MUST be persistently visible (never rely solely on placeholders). Input fields must have distinct focus, hover, disabled, and error states.

### Step 3: `/impeccable critique` (Design Tension & Typography)
Evaluate layout tension:
- **Typography Contrast**: Ensure sharp contrast between heading sizes and body sizes (e.g., Display 36px/Bold vs Body 15px/Regular).
- **Line Length**: Paragraph text must be clamped between 50 to 75 characters (`max-width: 65ch`) for optimal readability.
- **RTL Integrity**: Confirm bidirectional layout symmetry, correct chevron arrow orientations, and logical text alignment.

### Step 4: `/impeccable polish` (Micro-Craft & Tactile Feedback)
Add the finishing touches that differentiate world-class products:
- Inner border highlights (`box-shadow: inset 0 1px 0 rgba(255,255,255,0.1)` on dark themes).
- Smooth spring or cubic-bezier hover transitions (`transition: all 180ms cubic-bezier(0.16, 1, 0.3, 1)`).
- Custom subtle scrollbars and active press transformations (`transform: translateY(1px)` on button click).

---

## 3. The Component Gallery Taxonomy Standards

When reviewing or building UI components, adhere strictly to the established taxonomy of [component.gallery](https://component.gallery):

| Component Pattern | Anti-Pattern (Slop) | Impeccable Standard |
| :--- | :--- | :--- |
| **Buttons** | Multiple filled colored buttons in a single row | 1 Solid Primary, rest Outlined / Ghost with semantic variant tokens |
| **Modal / Dialog** | Unconstrained full-screen popups with hidden close button | Backdrop blur (`backdrop-filter: blur(8px)`), trapped focus, ESC key handling, clear header & footer |
| **Breadcrumbs** | Hardcoded text slashes with broken RTL mirroring | Semantic `<nav aria-label="Breadcrumb">` with accessible separators (`aria-hidden="true"`) |
| **Toasts / Alerts** | Raw browser `alert()` or abrupt blocking banners | Non-blocking floating stacked notifications with dismiss countdown and pause-on-hover |
| **Data Tables** | Text-heavy unpaginated tables with zero column alignment | Numeric data right-aligned, text left/start-aligned, sticky headers, subtle zebra rows |

---

## 4. Self-Review Anti-Slop Rubric

Every code modification touching UI must satisfy this rubric before sign-off:
- [ ] Are all font families loaded from designated design tokens?
- [ ] Is every interactive element keyboard navigable with a distinct focus ring?
- [ ] Are colors bound to CSS variables (`var(--accent-primary)`), zero naked hex codes in components?
- [ ] Does the UI scale gracefully down to 320px mobile screens without horizontal scroll bleed?
