# Template 02 — Task Delegation Protocol

Every order issued by Claude Code to a fleet member MUST use this exact structure.
Orders are written in **English only**.

---

```
### ORDER-<PHASE>-<NNN>
ASSIGNED TO : <opencode-cli | antigravity-ide>
MODEL       : <declared model>
PRIORITY    : <P0 | P1 | P2>
DEPENDS ON  : <ORDER id(s) | none>

OBJECTIVE
  <One sentence. What must exist when this order is done.>

CONTEXT
  <Only the facts the agent needs. No history, no rationale it cannot act on.>

FILES YOU OWN FOR THIS ORDER (write access)
  - <exact path>
  - <exact path>

FILES YOU MUST NOT TOUCH
  - <exact path or glob>

STEPS
  1. <imperative, verifiable>
  2. <imperative, verifiable>

ACCEPTANCE CRITERIA
  [ ] <objectively checkable by Claude Code>
  [ ] <objectively checkable by Claude Code>

BUILD VERIFICATION YOU MAY RUN
  <compile-only commands. NEVER a test command.>

FORBIDDEN
  - Running any test (./gradlew test, allTests, connectedAndroidTest, maestro test).
    Test execution is the exclusive right of Claude Code CLI.
  - Editing any path outside "FILES YOU OWN FOR THIS ORDER".
  - Changing library versions not named in this order.
  - Writing Arabic in code, comments, commit messages, or terminal output.

REPORT BACK
  - Unified diff of every file changed.
  - Exact output of the build verification command.
  - Any deviation from STEPS, with the reason.
  - STOP after reporting. Do not proceed to the next order.
```

---

## Rules of issuance

1. **One owner per path.** If two orders need the same file, they are serialised, never parallelised.
2. **No test execution outside Claude Code.** This is absolute and is repeated inside every order.
3. **Compile-only verification.** Delegates may prove the code builds; they may not prove it works.
4. **Stop-and-report.** A delegate never chains orders on its own initiative.
5. **English discipline.** Any Arabic appearing in a delegate's output is a protocol violation and the diff is rejected unreviewed.
