---
name: hyperframes-motion-engine
description: "Autovem HyperFrames Motion & Video-as-Code Specialist — leverages spring physics from Kinetics (stiffness, damping, mass), deterministic HTML/CSS/GSAP video-as-code compilation from HyperFrames, and SaaS product launch pacing from WhatShips. Generates fluid micro-interactions and programmatic product demo videos without manual editing suites."
---

# HyperFrames Motion & Video-as-Code Specialist

This skill bridges the gap between static web code, fluid physical motion design, and programmatic video generation. It combines:
1. **[Kinetics](https://kinetics.colorion.co)**: Natural spring physics replacing linear easing.
2. **[HyperFrames](https://hyperframes.dev)**: Deterministic Video-as-Code rendering (HTML/CSS/GSAP to MP4).
3. **[WhatShips](https://whatships.com)**: Silicon Valley SaaS launch pacing, demo reveals, and conversion storyboarding.

---

## 1. Spring Physics Standard (Kinetics Principles)

Traditional CSS `ease-in-out` curves feel robotic and lifeless. Natural UI motion requires **Spring Physics**:

### Key Parameters:
- **Stiffness ($k$)**: Controls how rapidly the spring pulls back to rest. (High = snappy; Low = gentle).
- **Damping ($c$)**: Friction reducing oscillation. (Under-damped = bounciness; Critically damped = zero bounce, maximum speed).
- **Mass ($m$)**: Perceived visual weight of the element.

### Calibrated Physics Profiles:

```css
/* Snappy Micro-interaction (Buttons, Toggles, Switches) */
/* Mass: 1, Stiffness: 300, Damping: 24 */
--spring-snappy: linear(
  0, 0.009, 0.035 2.1%, 0.141 4.4%, 0.723 12.9%, 0.938 16.7%, 1.017 20.2%,
  1.042 23%, 1.043 26.4%, 1.026 30.8%, 1.006 35.6%, 0.998 40.8%, 1 50%
);

/* Smooth Modal / Dialog Reveal (Sheet, Popover, Card Expand) */
/* Mass: 1, Stiffness: 180, Damping: 22 */
--spring-smooth: linear(
  0, 0.006, 0.024 1.8%, 0.098 3.8%, 0.495 10.8%, 0.785 16.2%, 0.942 21.6%,
  1.013 27.2%, 1.028 32.8%, 1.017 38.6%, 1.004 44.8%, 0.999 51.4%, 1 65%
);

/* Gentle Fluid Fade / Background Parallax */
/* Mass: 1.2, Stiffness: 100, Damping: 26 */
--spring-gentle: linear(
  0, 0.003, 0.012 1.5%, 0.05 3.1%, 0.28 9.2%, 0.58 16.2%, 0.82 23.8%,
  0.95 32.4%, 1.005 42%, 1.012 51.2%, 1.004 62%, 1 80%
);
```

---

## 2. Video-as-Code Generation (HyperFrames Workflow)

HyperFrames enables AI agents to write declarative video sequences using HTML/CSS and GSAP timelines:

### Core Architecture:
1. **Composition Specs**: Define resolution (`1920x1080` for Desktop, `1080x1920` for Mobile Reels), frame rate (`60fps`), and total duration.
2. **Declarative Timelines**: Sequence elements on a deterministic timeline (`gsap.timeline({ paused: true })`).
3. **Deterministic Rendering**: Headless Puppeteer crawls frame-by-frame (`seek(t)`), ensuring zero dropped frames or audio de-sync.

### Sample Programmatic SaaS Demo Composition:
```html
<div class="video-stage" style="width: 1920px; height: 1080px; background: #090a0f; overflow: hidden; position: relative;">
  <!-- Scene 1: Problem Hook (0.0s - 2.5s) -->
  <div id="hook-scene" class="scene">
    <h1 class="headline gradient-text">Ship Production Apps 10x Faster</h1>
    <p class="subheadline">Without the manual boilerplate.</p>
  </div>

  <!-- Scene 2: Feature Reveal with Spring Motion (2.5s - 6.0s) -->
  <div id="feature-card" class="card" style="transform: scale(0.8) translateY(100px); opacity: 0;">
    <div class="terminal-header">Fleet Orchestration Engine</div>
    <div class="code-stream">/* Multi-CLI Agent Dispatch */</div>
  </div>
</div>

<script>
  const tl = gsap.timeline({ paused: true });
  
  // Scene 1 In
  tl.from("#hook-scene", { opacity: 0, y: 40, duration: 0.8, ease: "power3.out" })
    .to("#hook-scene", { opacity: 0, y: -40, duration: 0.5, delay: 1.2 })
    
  // Scene 2 Spring In (WhatShips Style)
    .to("#feature-card", {
      opacity: 1,
      scale: 1,
      y: 0,
      duration: 1.1,
      ease: "elastic.out(1, 0.75)"
    }, "-=0.2");
</script>
```

---

## 3. WhatShips Storyboard Pacing Rubric

When engineering product launch videos and demos, follow the **WhatShips 4-Beat Structure**:

| Beat | Timestamp | Visual Objective | Audio / Motion Cue |
| :--- | :--- | :--- | :--- |
| **1. The Hook** | `0.0s – 2.0s` | State the acute customer pain point in <= 8 words | Immediate rapid zoom, stark contrasting headline |
| **2. The Shift** | `2.0s – 5.0s` | Introduce the product interface with physical momentum | Spring-damped 3D card tilt, cursor hover trigger |
| **3. The Crux** | `5.0s – 12.0s` | Showcase the core "magic moment" in real-time execution | Fast-forward progress indicator, crisp UI state change |
| **4. The Payoff** | `12.0s – 15.0s` | Call to Action (CTA) and clear next step | Clean badge reveal, subtle radiant glow background |

---

## 4. Operational Invariants

- **Performance First**: All animations in live web applications must strictly animate compositor-only properties (`transform`, `opacity`). Never animate `width`, `height`, `top`, or `margin` to prevent layout thrashing (60fps guaranteed).
- **Reduced Motion Respect**: Always include `@media (prefers-reduced-motion: reduce)` fallbacks that instantly snap properties without motion.
