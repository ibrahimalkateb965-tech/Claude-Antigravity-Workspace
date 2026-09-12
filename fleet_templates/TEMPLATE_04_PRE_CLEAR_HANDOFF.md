# Template 04 — Strategic Clear & Session Handoff Protocol

Used by Claude Code CLI and Fleet Commander when preparing to execute a Strategic Clear (`/clear` or `/compact`) to finish in-flight priority tasks, prevent half-baked commits, and preserve complete context.

---

```
### STRATEGIC CLEAR CHECKLIST (التصفير الاستراتيجي)

1. IN-FLIGHT TASK ASSESSMENT & WRAP-UP (إنهاء المهام العالقة ذات الأولوية):
   - Check if any immediate priority task or active order is currently in-progress.
   - If it can be wrapped up cleanly within the remaining context: compile, verify Quality Gate, and commit it cleanly now.
   - If it is too large to finish: set a clean stopping milestone, ensure code compiles, or isolate/stash uncompleted experiments so the branch is left in a 100% green/stable state.

2. STATE FREEZE & GIT VERIFICATION:
   - Branch status: `git status --short --branch` (Must be 100% clean, 0 uncommitted unhandled changes)
   - Last completed order & Quality Gate evidence (tests passed, APK byte-size verification)
   - Update `fleet_orders/CURRENT_STATE.md` (§1 "THE ONE THING TO DO NEXT" updated with exact standing and choices)

3. MEMORY & GOTCHAS FLUSH:
   - Any runtime lessons, OOM traps (e.g. B-17 idle compiler daemon kills), or quirks flushed to `MEMORY_STORE.md`
   - B-issues list in CURRENT_STATE.md updated

4. DECISION POINTS & HANDOFF SUMMARY:
   Print a formatted block for Ibrahim:
   - What high-priority work was closed before clearing
   - Current clean git commit hash and branch
   - Clear statement of available next choices (e.g. Option A, Option B)

5. RESUME PROMPT (Ready to copy-paste):
   Provide a concise 1-2 line prompt for Ibrahim to paste immediately after `/clear`:
   ```
   Read CLAUDE.md and fleet_orders/CURRENT_STATE.md. State where we are and present the choices for the next order.
   ```

6. GREEN LIGHT FOR /clear:
   Print:
   "✅ Strategic Clear preparation complete: In-flight tasks wrapped up, state frozen, branch clean. You can now safely run /clear in this terminal."
```
