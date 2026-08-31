# 📦 TEMPLATE 02: Task Delegation & Sub-Agent Handoff

> **Usage:** Used by Claude Code (or generated for other CLIs) to assign modular tasks with explicit boundaries.

```markdown
# 🤖 DELEGATE SKILLS PROTOCOL: Task Assignment

**Target CLI Agent:** [OpenCode CLI | Antigravity CLI | Antigravity IDE]  
**Assigned Skill Roles:** [e.g. [offline-sync-db], [flutter-ui-pro], [domain-contracts]]  
**Exclusive Tree Ownership:** `[e.g. lib/core/remote/]`  
**Governing Blueprints:** `fleet_config.json`, project blueprints, and `PROJECT_CONTEXT.md`

---

### 🎯 Objective:
[Detailed 1-2 sentence description of the goal]

---

### 🛠️ Execution Blueprint & File Specifications:
1. **`[File Path 1]`**:
   - [Specification details, method signatures, constraints]
2. **`[File Path 2]`**:
   - [Specification details, method signatures, constraints]

---

### ⚠️ Strict Architectural Constraints:
- Do NOT touch files outside your exclusive tree ownership.
- **NO TESTING EXECUTION:** Do NOT run test suites or modify test files. Claude Code CLI holds the exclusive monopoly on running tests and approving code.
- Run static verification / analyzer only and confirm **0 errors**.

Execute this implementation task, output the summary diffs, and report static verification status!
```
