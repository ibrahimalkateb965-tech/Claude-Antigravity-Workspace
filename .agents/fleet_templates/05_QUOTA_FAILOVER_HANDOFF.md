# 🔄 TEMPLATE 05: Quota Failover & Role Hand-off

> **Usage:** Used when Claude Code CLI or any CLI hits a rate limit / token quota, or when re-entering after quota reset.

```markdown
# 🔀 MULTI-CLI FAILOVER & STATE RE-ENTRY PROTOCOL

**Event:** [Quota Exhaustion / Rate Limit | Session Re-Entry after Reset]  
**Handing Off From:** [Claude Code CLI | Antigravity]  
**Assuming Orchestration:** [Antigravity | Claude Code CLI (Opus Max)]  
**State Memory:** `MEMORY_STORE.md` and `PROJECT_CONTEXT.md`

---

### 📌 Current Session State & Snapshot:
- **Latest Delivered Feature / Phase:** [Summary of last verified module]
- **Active In-Progress Task:** [Summary of current task in flight]
- **Verification Logs:** Linter status = [0 errors]

---

### 🎯 Directives:
1. Sync state seamlessly from `MEMORY_STORE.md`.
2. Maintain zero disruption to ongoing tasks.
3. Keep strict tree ownership boundaries intact.
4. Continue project execution without rewriting existing code.
```
