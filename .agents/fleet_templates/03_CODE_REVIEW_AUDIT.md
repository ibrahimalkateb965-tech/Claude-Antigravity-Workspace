# 🔍 TEMPLATE 03: Code Review, Testing & Quality Audit (Opus Max)

> **Usage:** Provide this prompt along with the execution diffs or files to Claude Code (Opus Max) for strict quality review, test execution, and code sign-off at the end of each Phase.

```markdown
# 🔬 CODE REVIEW, TESTING & DEVIL'S ADVOCATE AUDIT REQUEST

**Executor Agent:** [OpenCode CLI | Antigravity]  
**Implemented Scope / Phase:** [e.g. Phase 2 / Phase 3]  
**Static Verification Status:** Linter / Analyzer = [0 errors / status log]

---

### 📝 Implemented Code / Diffs for Review:
```dart
// [Paste key implementation snippets or diff summary here]
```

---

### 🎯 Audit & Test Execution Requirements for Claude Code (Opus Max):
As the **Sole Testing and Code Approval Authority**, please perform the following:
1. **Architectural Compliance:** Verify adherence to clean architecture and project constraints.
2. **Edge Cases & Race Conditions:** Check for concurrent access bugs, unhandled nullability, or infinite loops.
3. **Execute Automated Tests:** Run the full project automated test suite and confirm all tests pass green.
4. **Final Sign-off:** Provide explicit approval (`[APPROVED]`) or bulleted required fixes (`[REQUIRED REVISIONS]`). No code is approved or merged without Claude's sign-off.
```
