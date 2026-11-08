# Template 02 — Task Delegation Protocol

Every order issued by Claude Code to a fleet member MUST use this exact structure.
Orders are written in **English only**.

---

```
### ORDER-<PHASE>-<NNN>
ASSIGNED TO : <opencode-cli | antigravity-cli>
MODEL       : <declared model>
PRIORITY    : <P0 | P1 | P2>
DEPENDS ON  : <ORDER id(s) | none>
<!-- Programmatic CLI delegation targets headless agents (opencode-cli, antigravity-cli / agcli). Antigravity IDE is the human workbench. -->

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

---

## Parallel Multi-Worker Delegation Standard (GLM 5.3 Flash Concurrency)

When tasks can be decoupled into disjoint file sets or independent modules (e.g. Worker A for DB/Repositories, Worker B for UI Components), Claude Code issues **paired parallel orders** and launches both workers simultaneously:

```
### ORDER-<PHASE>-<NNN>-A (Worker A: GLM 5.3 Flash)
ASSIGNED TO : opencode-cli
MODEL       : opencode-go/glm-5.3-flash
INVOCATION  : opencode run -m opencode-go/glm-5.3-flash --auto "<prompt>"
SCOPE       : <backend / db / terminal plumbing files>

### ORDER-<PHASE>-<NNN>-B (Worker B: GLM 5.3 Flash)
ASSIGNED TO : cline-cli (or parallel opencode-cli instance)
MODEL       : glm-5.3-flash (medium)
INVOCATION  : cline "<prompt>" OR opencode run -m opencode-go/glm-5.3-flash --auto "<prompt>"
SCOPE       : <frontend / ui scaffolding / markup files>
```

---

## Local 4-Account Gemini Pro Delegation Protocol (D:\FleetProfiles\)

When assigning tasks to the local flat-rate Google AI Pro subscription pool ($0 token cost), Claude Code invokes:

```
### ORDER-<PHASE>-<NNN>-G1 (UI / Presentation)
ASSIGNED TO : agcli_worker_1 (Google AI Pro: ja7563110275@gmail.com)
WIN COMMAND : powershell.exe -NoProfile -Command "& 'D:\FleetProfiles\agy1.bat' '[prompt]'"
WSL COMMAND : /mnt/d/FleetProfiles/agy1.sh "[prompt]"
SCOPE       : Compose UI, Screens, Components, Sheets, Dialogs, Design Tokens

### ORDER-<PHASE>-<NNN>-G2 (Domain / Logic)
ASSIGNED TO : agcli_worker_2 (Google AI Pro: ibrahimalkateb224@gmail.com)
WIN COMMAND : powershell.exe -NoProfile -Command "& 'D:\FleetProfiles\agy2.bat' '[prompt]'"
WSL COMMAND : /mnt/d/FleetProfiles/agy2.sh "[prompt]"
SCOPE       : Domain Layer, UseCases, Repositories, ViewModels, StateFlows

### ORDER-<PHASE>-<NNN>-G3 (Context / Docs / A11y)
ASSIGNED TO : agcli_worker_3 (Google AI Pro: ibrahimalkateb9220@gmail.com)
WIN COMMAND : powershell.exe -NoProfile -Command "& 'D:\FleetProfiles\agy3.bat' '[prompt]'"
WSL COMMAND : /mnt/d/FleetProfiles/agy3.sh "[prompt]"
SCOPE       : TalkBack/VoiceOver Semantics, Audio Engine, 2M Context Ingestion, Docs

### ORDER-<PHASE>-<NNN>-G4 (Parallelism / Failover)
ASSIGNED TO : agcli_worker_4 (Google AI Pro: ayahowiedy123@gmail.com)
WIN COMMAND : powershell.exe -NoProfile -Command "& 'D:\FleetProfiles\agy4.bat' '[prompt]'"
WSL COMMAND : /mnt/d/FleetProfiles/agy4.sh "[prompt]"
SCOPE       : Secondary Tasks, Fast Refactoring, Overflow & Dynamic Failover

### ORDER-<PHASE>-<NNN>-FAILOVER (Smart Auto-Cascade)
WIN COMMAND : powershell.exe -NoProfile -File "D:\FleetProfiles\agy-fleet.ps1" -Prompt "[prompt]"
WSL COMMAND : /mnt/d/FleetProfiles/agy-fleet.sh "[prompt]"
SCOPE       : Automatically rotates across Workers 1-4 if any worker hits a 429 quota limit.
```

---

## Rules of issuance

1. **One owner per path.** If two orders need the same file, they are serialised, never parallelised.
2. **Parallel GLM Workers.** Disjoint files MUST be parallelised across GLM 5.3 Flash workers to maximize throughput.
3. **No test execution outside Claude Code.** This is absolute and is repeated inside every order.
4. **Compile-only verification.** Delegates may prove the code builds; they may not prove it works.
5. **Stop-and-report.** A delegate never chains orders on its own initiative.
6. **English discipline.** Any Arabic appearing in a delegate's output is a protocol violation and the diff is rejected unreviewed.
