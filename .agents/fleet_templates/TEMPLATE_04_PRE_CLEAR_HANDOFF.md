# Template 04 — Pre-Clear Session Handoff Protocol (Hook 25)

Used by Claude Code CLI and Fleet Commander when preparing to clear the session (`/clear` or `/compact`) to prevent context loss.

---

```
### PRE-CLEAR HANDOFF CHECKLIST

1. STATE FREEZE:
   - Active Branch & HEAD commit: `git status --short --branch`
   - Last completed milestone / order & Quality Gate status (test pass counts, approval status)
   - Uncommitted changes (must be 0 or committed/stashed)
   - Update `CURRENT_STATE.md` (§1 "THE ONE THING TO DO NEXT" & Phase log updated with exact standing)

2. MEMORY & GOTCHAS FLUSH:
   - Any runtime lessons, OOM traps, or quirks flushed to `.agents/MEMORY_STORE.md`
   - Blockers list in CURRENT_STATE.md updated

3. DECISION POINTS & HANDOFF SUMMARY:
   Print a formatted block for Ibrahim:
   - Summary of completed work in this closing session
   - Current clean git commit hash
   - Next available choices (e.g. Choice A, Choice B)
   
4. RESUME PROMPT (Ready to copy-paste):
   Provide a concise 1-2 line prompt for Ibrahim to paste immediately after `/clear`:
   ```
   Read CLAUDE.md, AGENTS.md, and CURRENT_STATE.md. State where we are and present the choices for the next task.
   ```

5. GREEN LIGHT:
   Print:
   "✅ State frozen and documented. You can now safely run /clear in this terminal."
```
