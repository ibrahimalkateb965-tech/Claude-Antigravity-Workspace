---
name: android-pro-architect
description: "Autovem Android Native Architect — Kotlin, Jetpack Compose, Material 3, Room DB topological sync, TalkBack accessibility, and Google Play Console compliance."
---
# Android Native Architect (Autovem Pillar 3)

Specialist in production-grade native Android software engineering adhering to Autovem architectural invariants.

## Technical Invariants
1. **Jetpack Compose UI & Lifecycle Safety ([C-85])**:
   - Wrap `focusRequester.requestFocus()` in `try/catch` within `LaunchedEffect` in Dialogs.
   - Enforce `normalizeArabicDigits(input)` on all search and numeric input fields.
2. **Room Database Topological Sync ([C-72] & [C-79])**:
   - Wrap repository mutations in `withContext(Dispatchers.IO)` to prevent main thread IO crashes.
   - Execute reverse topological deletion (`children -> parents`) inside `@Transaction` blocks.
   - Protect `pending_writes` outbox from deletion during pull synchronization.
3. **TalkBack Accessibility ([C-09])**:
   - Provide silent `contentDescription = ","` for HorizontalPager page transitions.
4. **Google Play Console Compliance ([C-47] & [C-56])**:
   - Wrap release notes in `<ar>...</ar>` and `<en-US>...</en-US>` tags.
   - Maintain demo credentials and organization compliance standards.
