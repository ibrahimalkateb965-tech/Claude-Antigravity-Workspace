# Template 03 — Code Review Audit (Devil's Advocate)

Applied by Claude Code CLI to every diff returned by a fleet member,
**before** any test is run. A diff that fails Section A is rejected without testing.

---

```
## AUDIT-<ORDER id>
REVIEWER : claude-code-cli
DIFF SIZE: <files changed / lines +/->

### A. PROTOCOL COMPLIANCE  (fail = immediate reject)
[ ] Only files listed in "FILES YOU OWN" were modified
[ ] No test was executed by the delegate
[ ] No Arabic in code, comments, or commit message
[ ] No undeclared dependency or version change
[ ] No secret, key, keystore, or .p12 introduced or logged

### B. DEVIL'S ADVOCATE — attack the change
State the strongest case that this change is WRONG, then answer it.
[ ] What does this break on the platform the author did NOT test?
[ ] What does this cost a blind user? (VoiceOver focus order, announcement
    duplication, gesture conflict, audio ducking, interruption handling)
[ ] What happens on failure? (no network, audio interruption, phone call,
    backgrounded app, low memory, Kotlin/Native OOM)
[ ] Is this shared code that should have been expect/actual, or the reverse?
[ ] Does this introduce an Android idiom into commonMain?
[ ] Is there a simpler solution with fewer dependencies?

### C. ARCHITECTURE
[ ] Clean Architecture boundaries intact (domain has no platform imports)
[ ] No recomposition hazard (fast-changing state passed as () -> T, not T)
[ ] StateFlow remains the single source of truth
[ ] DI wiring is Koin-compatible in commonMain

### D. ACCESSIBILITY GATE  (blind-first, overrides all other results)
[ ] TalkBack behaviour unchanged or improved
[ ] VoiceOver behaviour explicitly reasoned about, not assumed
[ ] Custom actions map to rotor actions on iOS
[ ] No gesture handler swallows screen-reader semantics

### E. TEST EXECUTION  (Claude Code only)
COMMAND : <exact command>
RESULT  : <pass/fail counts, verbatim tail of output>

### F. VERDICT
  QUALITY GATE: <PASS | FAIL | PASS WITH CONDITIONS>
  REQUIRED FIXES:
    1. <...>
  RE-ISSUED AS: <ORDER id | none>
```

---

## Rejection reasons that require no further analysis

- The delegate ran a test.
- The delegate edited a path it does not own.
- The change makes shared code depend on an Android-only API.
- The change alters screen-reader semantics without an explicit VoiceOver/TalkBack rationale.
