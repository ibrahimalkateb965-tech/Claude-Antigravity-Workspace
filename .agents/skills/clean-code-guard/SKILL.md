---
name: clean-code-guard
description: Review generated or changed production code before it ships, using Clean Code, SOLID, DRY, KISS, YAGNI, and LLM-specific failure-mode checks in any programming language. Best used reactively after an agent writes, edits, refactors, or fixes code, before presenting, committing, or merging the result. Use when the user asks "review this PR", "is this safe to merge?", "make this cleaner", "audit this code", "refactor this", "fix this bug", or after a coding agent produced implementation code.
---

# Clean Code Guard

You are reviewing generated or changed code before it ships. Apply the rules below as a guard pass after the first implementation pass to catch the systematic failure modes of AI-generated code.

## The 14 AI-Specific Failure Modes to Eliminate

1. **Broad Exception Swallowing**: Never wrap risky operations in empty catch blocks `catch (Exception e) {}` or log-only catches without rethrowing or user-facing error handling.
2. **Declaration of Success Despite Failures**: Never return hardcoded fixture/mock data to make an unverified path appear working.
3. **Bloated Monolithic Functions**: Target ≤ 20-30 lines per function with single responsibility.
4. **Vague Naming**: Prohibit names like `data`, `item`, `temp`, `res`, `helper`, `utils` without domain qualifiers.
5. **Over-Abstraction / Premature Architecture**: Do not invent generic interfaces, wrappers, or factory layers for single-use logic (YAGNI).
6. **Code Duplication**: Never copy-paste similar logic blocks across files; extract shared pure utilities.
7. **Flag Arguments**: Avoid boolean parameters that branch function behavior into two separate functions; split them cleanly.
8. **Parameter Ceiling**: Never exceed 4 arguments in a method; bundle them into a typed data class or options object.
9. **Dead Code & Zombie Comments**: Remove commented-out code, unused imports, and obsolete TODOs.
10. **State Leaks & Side Effects**: Ensure UI components do not mutate global state outside designated state management (StateFlow/MVI).
11. **Hallucinated APIs**: Always verify that standard library and dependency methods actually exist before invoking them.
12. **Inconsistent Naming & Code Style**: Match surrounding file idioms and architectural patterns.
13. **Silent Fallbacks**: Fallbacks must be intentional and documented, not masked errors.
14. **Missing Lifecycle Cleanup**: Ensure coroutines, observers, and listeners are properly canceled in ViewModel / Lifecycle scopes.

## Delivery Self-Check
- [ ] No broad or empty catch blocks.
- [ ] Functions are short, focused, and explicitly named.
- [ ] No hardcoded bypasses or fake implementations.
- [ ] Single level of abstraction per function.
