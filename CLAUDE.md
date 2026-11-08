# CLAUDE.md — Blind App (تطبيق القرآن للمكفوفين)

Project-level operating rules for Claude Code CLI in this repository.
These rules are **permanent** and apply to every session, without needing to be restated.

---

## 1. LANGUAGE RULE (PERMANENT — HIGHEST PRIORITY)

> **Claude Code MUST always reply to the maintainer (Ibrahim) in Arabic.**
> **All technical terms MUST remain in English** — untranslated and unmodified.

Scope and exceptions:

| Channel | Language |
| :--- | :--- |
| Replies to Ibrahim in chat | **Arabic** (technical terms in English) |
| Task Delegation orders to OpenCode CLI / Antigravity CLI (agcli) | **English only** |
| Code, identifiers, comments, commit messages, PR bodies | **English only** |
| CI logs, CLI terminal commands | **English only** |
| Generated `.md` orders and audits under `fleet_orders/` | **English only** |

Technical terms that stay in English include (non-exhaustive):
`Kotlin Multiplatform`, `Compose Multiplatform`, `expect/actual`, `Koin`, `Hilt`,
`ExoPlayer`, `AVPlayer`, `AVAudioSession`, `VoiceOver`, `TalkBack`, `Ktor`,
`SQLDelight`, `Room`, `XCFramework`, `Gradle`, `Quality Gate`, `Devil's Advocate Audit`.

Do **not** transliterate these into Arabic script.

---

## 2. FLEET COMMAND MODE (Hook 22)

Once Hook 22 is invoked, this session stays in Fleet Command Mode permanently.
Every subsequent request is converted into structured delegation using:
- `fleet_templates/TEMPLATE_02_TASK_DELEGATION.md`
- `fleet_templates/TEMPLATE_03_CODE_REVIEW_AUDIT.md`
- `fleet_templates/TEMPLATE_04_PRE_CLEAR_HANDOFF.md` (Hook 25: invoked when session capacity is high, e.g. session:90%, or Ibrahim requests pre-clear handoff).


### Exclusive responsibilities

| Agent | Owns | Absolutely forbidden |
| :--- | :--- | :--- |
| **Claude Code CLI** (`Opus 5`) | Planning, architectural review, Devil's Advocate Audit, **exclusive execution of ALL automated tests**, final approval / Quality Gates | Writing bulk boilerplate that a cheaper model can produce |
| **OpenCode CLI** (`Meta Muse Spark 1.3` / Fallback: `Dynamic Multi-Provider`) | Terminal execution, build files, Gradle / version catalog, database layer, network clients, CI workflows | **Running any test.** Touching UI composables or accessibility semantics |
| **Antigravity CLI** (`agcli` [Gemini 3.8 Flash High / Local]) | Headless UI building, iOS adaptation, Domain Logic, `expect/actual` platform abstractions, persistent memory via direct CLI invocation | **Running any test.** Editing Gradle files or CI workflows |
| **Antigravity IDE** (`Gemini 3.8 Flash High`) | Interactive Developer Workbench & Cockpit (human pair programming, visual diffs, workspace intelligence) | **Programmatic CLI order target** (All CLI delegation routes to `agcli` to avoid manual copy-paste) |

### Testing Monopoly
No code is merged until Claude Code has executed the tests itself and issued
an explicit `QUALITY GATE: PASS`. OpenCode and Antigravity **write and build only**.

### Local 4-Account Gemini Pro Fleet Matrix & Headless Invocation Protocol
Claude Code CLI orchestrates the 4 dedicated local Google AI Pro accounts (`D:\FleetProfiles\Worker_{1..4}`) and OpenCode Zen via **direct headless dispatch (`[C-25]`)**:

| Worker | Google Account / Role | Primary Scope | Headless Invocation Command |
| :--- | :--- | :--- | :--- |
| **Worker 1** | `ja7563110275@gmail.com` | UI, Jetpack Compose, Components, Design Tokens, Dialogs | Win: `& "D:\FleetProfiles\agy1.bat" "<prompt>"`<br>WSL: `/mnt/d/FleetProfiles/agy1.sh "<prompt>"` |
| **Worker 2** | `ibrahimalkateb224@gmail.com` | Domain Layer, UseCases, Repositories, ViewModels, StateFlows | Win: `& "D:\FleetProfiles\agy2.bat" "<prompt>"`<br>WSL: `/mnt/d/FleetProfiles/agy2.sh "<prompt>"` |
| **Worker 3** | `ibrahimalkateb9220@gmail.com` | 2M Context Ingestion, Docs, TalkBack / VoiceOver A11y, Audio Service | Win: `& "D:\FleetProfiles\agy3.bat" "<prompt>"`<br>WSL: `/mnt/d/FleetProfiles/agy3.sh "<prompt>"` |
| **Worker 4** | `ayahowiedy123@gmail.com` | Secondary Parallelism, Refactoring, Overflow & Failover Pool | Win: `& "D:\FleetProfiles\agy4.bat" "<prompt>"`<br>WSL: `/mnt/d/FleetProfiles/agy4.sh "<prompt>"` |
| **Auto Failover** | Round-Robin across 1-4 | Cascades automatically if a worker exhausts quota or hits 429 | Win: `& "D:\FleetProfiles\agy-fleet.ps1" -Prompt "<prompt>"`<br>WSL: `/mnt/d/FleetProfiles/agy-fleet.sh "<prompt>"` |
| **Worker Zen** | OpenCode Go (`GLM 5.3 Flash`) | DB Migrations, Schemas, CLI Plumbing, Gradle Builds | `opencode run --auto -m opencode-go/glm-5.3-flash "<prompt>"` |

**Rules of Engagement:**
1. **Zero Manual Copy-Paste:** Claude Code issues commands directly in terminal using the invocation strings above.
2. **Strict Testing Monopoly:** Delegates write and build only. Claude Code audits diffs and runs `./gradlew test` / E2E suites exclusively.
3. **Drive C: Zero-Write (`[C-82]`):** All profiles and cached artifacts remain quarantined in `D:\FleetProfiles\`.

### Multi-Worker Parallel Concurrency Standard (GLM 5.3 Flash)
As authorized and commanded by agency leadership, **Claude Code CLI is actively commanded to dispatch multiple GLM 5.3 Flash workers simultaneously in parallel** on non-colliding task scopes:
- **Worker A (`opencode-go/glm-5.3-flash`)**: Shell commands, CLI build tasks, migrations, DAOs, backend logic.
- **Worker B (`cline` with `glm-5.3-flash` or parallel headless `opencode`)**: UI scaffolding, view components, frontend layout.
- **Rule**: Dispatch both workers concurrently across disjoint file sets to maximize speed and conserve Opus Max token budget.

---

## 3. TARGET STACK (as of 2026-09-10)

- **Shared:** Kotlin Multiplatform (`:shared` only — **no Compose Multiplatform**, per ADR-004, 2026-09-11)
- **Modules:** `:shared` (domain + data + platform abstractions), `:app` (Android, Jetpack Compose — not renamed, per ADR-003), `iosApp/` (XcodeGen, **SwiftUI** UI over the `:shared` XCFramework). `:composeApp` is never created.
- **DI:** Koin (Hilt is Android/JVM-only and **cannot** cross to iOS)
- **Network:** Ktor Client (`OkHttp` engine on Android, `Darwin` engine on iOS) + `kotlinx.serialization`
- **Audio:** `expect class AudioEngine` → `Media3/ExoPlayer` (Android) / `AVPlayer` + `AVAudioSession` (iOS)
- **Accessibility:** `expect` platform layer → `TalkBack` (Android) / `VoiceOver` (iOS). **Never share the interaction model.**

## 4. NON-NEGOTIABLE PRODUCT CONSTRAINTS
 
1. **Blind-first.** Any change that degrades screen-reader behaviour is an automatic Quality Gate failure, regardless of test results.
2. **No paid unlock outside the platform billing system.** The app is fully free; `TrialManager` and PIN gating were removed deliberately.
3. **No data collection.** The Play Store Data Safety declaration and the iOS Privacy Manifest must both stay at "no data collected".
4. **Uthmanic text integrity.** `sanitizeUthmanicText` must be ported byte-for-byte; any regression in diacritic rendering is a P0 defect.

---

## 5. GIT & REPOSITORY HYGIENE RULE (PERMANENT — STRICT AI QUARANTINE)

> **NEVER stage, commit, or push AI assistant configurations, internal memories, fleet orders, or local tool state to Git/GitHub.**

1. **Quarantined Paths (Strictly Forbidden in Git):**
   - **Claude Code:** `.claude/`, `CLAUDE.md`, `Claude outputs/`
   - **Antigravity & Agents:** `.agents/`, `.gemini/`, `.antigravity/`, `mcp_config.json`, `MEMORY_STORE.md`
   - **OpenCode & Multi-CLI Fleet:** `opencode.json`, `.opencode/`, `fleet_config.json`, `fleet_orders/`, `fleet_templates/`
   - **JVM / Gradle Logs:** `hs_err_pid*.log`, `replay_pid*.log`, `**/hs_err_pid*.log`, `**/replay_pid*.log`
2. **Operational Rules:**
   - All the above paths are excluded via `.gitignore` and enforced by `.git/hooks/pre-commit`.
   - Never use `git add -f` to bypass the ignore rules.
   - When staging and committing files, stage ONLY application code, resources, tests, and official product docs.
   - If an order or report touches these files locally for tool operations, they must remain untracked and must never be included in any commit.

---

## 6. ORCHESTRATOR PLAYBOOK

Shared protocols live in `AGENTS.md` §16. Claude-specific practice:
1. **Fan-Out / Fan-In:** Send broad discovery to low-cost lanes (a long-context worker, Haiku subagents) and have them return a compact summary. Decide from that summary, opening raw sources only to verify a claim the decision rests on.
2. **Meta-Prompting:** For a new SOP, new fleet template, or high-token dispatch, ask the maintainer about audience, quality bar, and a reference example, then draft the prompt in the `AGENTS.md` §16.3 contract format. Run §16.4 if it will be reused.
3. **Retrospective:** After a skill build or multi-phase job, answer "How could that have been done faster with fewer tokens?" and record the durable answer in `MEMORY_STORE.md` (per `AGENTS.md` §8).
4. **Session Hygiene:** Run `/context` at the start of a heavy session and act on the context zones. Use `/btw` for side questions during long CLI runs. Hand off through Hook 25 (`TEMPLATE_04_PRE_CLEAR_HANDOFF.md`) before `/clear`.
5. **Repo Setup:** Use `/init` in new product repos. Maintain this root file by hand.
6. **Extensions:** Prototype with an MCP, then move stable read-only workflows to a script or skill and disable the connector. Keep MCP servers whose guards live in code.

