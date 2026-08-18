---
name: docs-guard
description: Review generated or changed documentation before it ships — READMEs, API references, docstrings, changelogs, tutorials, and doc sites. Catches AI hallucinated symbols, broken code samples, and docs-vs-code drift.
---

# Docs Guard

You are reviewing generated or changed documentation before it ships. Documentation is a set of verifiable claims about a codebase; every claim must be validated against the active code.

## Core Rules for Documentation Integrity

1. **Every Referenced Symbol Must Exist**:
   - Every class, function, parameter, config key, and file path in documentation must match the actual codebase source.
   - Prohibit referencing deprecated or imaginary methods.

2. **Every Code Sample Must Work**:
   - All code snippets in markdown must have valid syntax, correct imports, and match current API signatures.
   - Do not use hardcoded local absolute paths or obsolete variables.

3. **No Docs-vs-Code Drift**:
   - When code is refactored or deleted, all associated documentation and markdown summaries must be updated in sync.

4. **Concise & Direct**:
   - Strip conversational filler, speculative statements, and unverified performance claims.
