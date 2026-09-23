# AGENTS.md — Autovemtech Master Architecture & Fleet Directive

Welcome to **Autovemtech** (Autovem) — a hybrid technology and growth agency.
This document is the canonical entry point for all AI agents, coding assistants, and CLI harnesses (including **Codex Harness / ChatGPT Desktop**, **Claude Code CLI**, **Antigravity IDE**, **OpenCode CLI**, and **Cursor**).

---

## 1. Agency Identity & Core Infrastructure

- **Entity**: Autovemtech (Brand: `Autovem` — do not transliterate into Arabic in official headings).
- **Core Mission**: Delivering end-to-end digital growth, bespoke AI systems, and cross-platform mobile/web software.
- **Official Domain**: `www.autovemtech.com` | **Official Inquiries**: `ibrahimalkateb965@gmail.com`.
- **Infrastructure Registry** *(Pointers only — zero secrets in this file; credentials live in `.env`)*:
  - **Google Ads MCC (Manager Account)**: `576-627-3068` (`AutovemTech Agency`).
  - **Cloud Infrastructure**: Hostinger VPS (`srv1810150.hstgr.cloud` — IP: `187.55.226.225`, KVM 1: 50 GB NVMe, 4 GB RAM).
  - **Hybrid Domain & Business Email Architecture**:
    - **Sovereign TLDs (`.sa` / `.com.sa`)**: Registered & managed via local accredited registrars (**DNet / SaudiNIC**).
    - **Enterprise Business Email**: Managed via Hostinger Business Mail Cluster (MX1/MX2, SPF, DKIM CNAME Rotation, DMARC `p=quarantine`).
    - **Web & Application Hosting**: Directed to VPS via A-Records managed by Caddy & Docker containers.

---

## 2. The Three Operational Pillars

```
                     ┌──────────────────────────────────────────────┐
                     │          AUTOVEMTECH AGENCY ROOT             │
                     └──────────────────────┬───────────────────────┘
                                            │
         ┌──────────────────────────────────┼──────────────────────────────────┐
         ▼                                  ▼                                  ▼
┌──────────────────┐               ┌──────────────────┐               ┌──────────────────┐
│     PILLAR 1     │               │     PILLAR 2     │               │     PILLAR 3     │
│ Digital Marketing│               │   AI Solutions   │               │ App Engineering  │
│   & Growth Ops   │               │ & Agent Systems  │               │ (Mobile & Web)   │
└────────┬─────────┘               └────────┬─────────┘               └────────┬─────────┘
         │                                  │                                  │
  Clients/                          .agents/                          Android (Kotlin/Compose)
  ├── 01_Shajan_Containers          tools/                            iOS (Remote Playbook)
  ├── 02_YAZ_Air_Conditioning       scripts/                          Web (Modern Vanilla/Next)
  ├── 03_GHERAS_Center              Flowchart Studio                  Play Console & Store Ops
  ├── 04_Apex_Services
  └── _Agency_Templates
```

### Pillar 1: Digital Marketing & Client Growth Operations
- **Root Directory**: `Clients/`
- **Standard 8-Folder Structure per Client**:
  1. `00_Client_Profile`: Client identity, geographic targeting, and access credentials.
  2. `01_Market_Research`: Market analysis, competitor audits, and keyword intent clustering.
  3. `02_Google_Ads_Campaigns`: Campaign architecture (Search, Call-Only, PMax), budgets, and negative keyword lists.
  4. `03_Ad_Creatives_Copy`: High-converting Responsive Search Ads (RSA), call ads, and assets.
  5. `04_Landing_Pages_CRO`: Mobile-first CRO, GTM/GA4 dataLayer events, WhatsApp/call conversion hooks.
  6. `05_Analytics_Reports`: Weekly KPI dashboards, monthly audits, and print-ready executive summaries.
  7. `06_Contracts_Invoices`: Scope of Work (SOW), managed subscriptions, and payment logs.
  8. `07_Old_Data_Archive`: Historical audits, past account benchmarks, and migration logs.
- **Client Onboarding Rule**: Always duplicate `Clients/_Agency_Templates/` to create a new client folder (`Clients/NN_ClientName`).

### Pillar 2: AI Solutions & Autonomous Agentic Systems
- **Root Directories**: `.agents/`, `tools/`, `scripts/`
- **Capabilities**:
  - Autonomous multi-agent pipelines (Fleet Orchestrator, Sub-agents under `.agents/Sub_Agent/`).
  - Production-grade **FastMCP** server construction and tool exposure.
  - **n8n** automation workflows, webhook processors, and LLM router architectures.
  - Visualization & System Architecture: **Autovem Flowchart Studio** (`tools/flowchart_studio/`) for 4K architectural diagramming with WCAG AA compliance.
- **Memory & Decisions**: Long-term Architectural Decision Records (ADRs) live in `.agents/MEMORY_STORE.md`.

### Pillar 3: Application Engineering (Mobile & Web)
- **Android Engineering**: Kotlin, Jetpack Compose, Material 3, Room DB, WorkManager, Offline-first sync, and Google Play Console compliance (D-U-N-S, Organization account standards).
- **iOS Remote Engineering**: Zero-Mac CI/CD pipeline governed strictly by [REMOTE_IOS_DEV_PLAYBOOK.md](file:///f:/AI%20PROJECTS/Autovemtech/REMOTE_IOS_DEV_PLAYBOOK.md).
  - Fast inner loop: Linux container (`flutter analyze`, test, web preview).
  - Slow outer loop: GitHub Actions `macos-15` runner for `xcodebuild`, code signing, and TestFlight deployment.
  - Strict VoiceOver accessibility standards.
- **Web Engineering**: High-performance modern web interfaces, semantic HTML5, zero-bloat vanilla CSS, responsive layouts, and print-ready media styling (`@media print`).

---

## 3. Instant Task Router (Shorthand Intent Mapping)

When the user provides a brief or conversational prompt, immediately resolve and route the task using this lookup table:

| User Trigger / Keyword | Target Directory / Document | Standard Operating Action |
| :--- | :--- | :--- |
| **"عميل جديد" / "new client"** | `Clients/_Agency_Templates` → `Clients/NN_Name` | Clone template folder, initialize `00_Client_Profile/CLIENT_OVERVIEW.md`. |
| **"شجن" / "حاويات" / "Shajan"** | `Clients/01_Shajan_Containers/` | Check `02_Google_Ads_Campaigns` or `05_Analytics_Reports`. Focus: Search + Call-Only, Riyadh waste/dumpsters. |
| **"ياز" / "تكييف" / "YAZ"** | `Clients/02_YAZ_Air_Conditioning/` | Check campaigns or email credentials. Enforce negative list for automotive AC bleed; isolate emergency maintenance from project installs. |
| **"غراس" / "Gheras"** | `Clients/03_GHERAS_Center/` | Educational center campaigns, landing page lead-gen, local community targeting. |
| **"تقرير أداء" / "Google Ads report"** | `Clients/<client>/05_Analytics_Reports/` | Build 3-page print-ready report (DOCX via `arabic-docx-builder` + PDF export + `print.html`). Apply Rule 50 (0-9 numerals). |
| **"صفحة هبوط" / "CRO" / "landing page"** | `Clients/<client>/04_Landing_Pages_CRO/` | Mobile-first CRO, WhatsApp/Call sticky buttons, GTM click-to-call triggers. |
| **"فاتورة" / "عرض سعر" / "quote"** | `Clients/<client>/06_Contracts_Invoices/` | Managed subscription standard (399 SAR/yr for domain+mail). Strict 1-Page A4, Rule 50 zero-pricing. |
| **"نطاق" / "بريد مهني" / "DNS"** | `.agents/MEMORY_STORE.md` (Decisions 10–13) | Hybrid architecture: DNet/SaudiNIC for `.sa` + Hostinger Enterprise Mail (MX/SPF/DKIM/DMARC). |
| **"Google Ads API" / "OAuth" / "token"** | `GOOGLE_ADS_API_RESUMPTION.md`, `scripts/` | Follow Passkey resumption protocol for MCC `576-627-3068`. Run `authenticate_google_ads.py`. |
| **"Android" / "APK" / "Play Console"** | Play Console PDFs at root + mobile specs | Review Organization D-U-N-S rules and demo account requirements before submission. |
| **"iOS" / "TestFlight" / "build ipa"** | `REMOTE_IOS_DEV_PLAYBOOK.md` | Execute fast loop on Linux container; push to GitHub Actions `macos-15` for `.ipa`. |
| **"مخطط" / "flowchart" / "architecture"** | `tools/flowchart_studio/` | Open studio via `open_flowchart_studio.bat`. Use valid Mermaid syntax and Dark Neon Candy palette. |
| **"وكيل جديد" / "MCP" / "n8n"** | `.agents/skills/`, `.agents/Sub_Agent/` | Follow YAML frontmatter standard; ensure zero-leak stdio for FastMCP. |

---

## 4. Multi-CLI Fleet Matrix & Division of Labor

Autovemtech operates an autonomous multi-CLI fleet governed by [fleet_config.json](file:///f:/AI%20PROJECTS/Autovemtech/fleet_config.json):

```
┌────────────────────────────────────────────────────────────────────────┐
│              CLAUDE CODE CLI (Opus Max / Sonnet 5)                     │
│   Master Orchestrator • Staff Architect • Sole Test & Audit Authority  │
└──────────────────┬──────────────────────────────────┬──────────────────┘
                   │                                  │
                   ▼                                  ▼
┌──────────────────────────────────────┐  ┌─────────────────────────────────────────────────────────────┐
│     CODEX CLI / CHATGPT DESKTOP      │  │                   OPENCODE CLI (DUAL WORKERS)               │
│           (GPT-5.6 Soul)             │  │ ┌─────────────────────────────┬───────────────────────────┐ │
│ Algorithmic Logic • Complex Refactor │  │ │ Worker A: GLM 5.3 Flash     │ Worker B: Meta Muse Spark │ │
│ Deep Optimizations • Data Parsers    │  │ │ Fast Terminal & Scaffolding │ DB, Migrations & DAOs     │ │
└──────────────────────────────────────┘  │ └─────────────────────────────┴───────────────────────────┘ │
                   │                      └─────────────────────────────────────────────────────────────┘
                   │                      ┌─────────────────────────────────────────────────────────────┐
                   │                      │                     CLINE CLI (DUAL WORKERS)                │
                   │                      │ ┌─────────────────────────────┬───────────────────────────┐ │
                   │                      │ │ Worker C: GLM 5.3 Flash     │ Worker D: Meta Muse Spark │ │
                   │                      │ │ UI Scaffolding & Terminal   │ Data & API Controllers    │ │
                   │                      │ └─────────────────────────────┴───────────────────────────┘ │
                   │                      └──────────────────────────────┬──────────────────────────────┘
                   │                                                     │
                   └──────────────────┬──────────────────────────────────┘
                                      ▼
┌────────────────────────────────────────────────────────────────────────┐
│               ANTIGRAVITY CLI - agcli (Gemini 3.8 Flash High)          │
│       Terminal Executor: UI Architecture, Compose/Web & Domain Logic    │
└────────────────────────────────────────────────────────────────────────┘
                                      ▲
                                      │ (Refined prompts & plans)
┌────────────────────────────────────────────────────────────────────────┐
│                     ANTIGRAVITY IDE (Workbench & Cockpit)              │
│  Interactive Control Room • Direct Collaboration with User • Guidance  │
│  Visual Inspection • Prompt Optimization BEFORE Dispatch to Fleet CLIs │
└────────────────────────────────────────────────────────────────────────┘
```

### Fleet Roles & Separation of Concerns

| Agent Entity | Tool / Harness | Model / Invocation | Core Operational Role |
| :--- | :--- | :--- | :--- |
| **Claude Code CLI** | `claude` | Opus Max / Sonnet 5 | Master Orchestrator, Staff Architect, **Sole Testing Authority**, Quality Gatekeeper. |
| **Codex CLI / Desktop** | `codex` / ChatGPT Desktop | GPT-5.6 Soul / gpt-6-astra | Algorithmic logic, complex math engines, heavy refactoring, data pipelines. |
| **OpenCode CLI (Worker A)** | `opencode` (Flash) | `-m opencode-go/glm-5.3-flash` | **Ultra-Fast Terminal Executor**: Shell commands, CLI build tasks, scaffolding, rapid scripts (runs in parallel with Worker B; **MAX 1 instance of GLM concurrently**). |
| **OpenCode CLI (Worker B)** | `opencode` (Muse) | `-m opencode-go/muse-spark-1.3-contributor` | **Database & Data Architect**: Database schemas, migrations, DAOs, repositories, data transformers (runs in parallel). |
| **Cline CLI (Worker C)** | `cline` (Flash) | `glm-5.3-flash (medium)` | **Parallel UI Scaffolding**: Runs concurrently with Worker A. Rapid terminal tasks, defaults to Act mode with Auto-approve. ($0.00) |
| **Cline CLI (Worker D)** | `cline` (Muse) | `muse-spark-1.3-contributor` | **Parallel Data Architect**: Redundancy for Worker B. Data logic, API routes, defaults to Act mode with Auto-approve. ($0.00) |
| **Antigravity CLI (`agcli`)** | `agcli` | Gemini 3.8 Flash High | **Autonomous Terminal Executor**: Headless generation of UI components, Jetpack Compose, Web views, and domain logic. |
| **Antigravity IDE** | Editor Workbench | Gemini 3.8 Flash High | **Interactive Human-AI Cockpit**: High-level strategy, consultation, visual inspection, monitoring real-time outputs, and manually refining/optimizing commands with the user *before* routing them to the CLI fleet. **Never target Antigravity IDE with automated CLI delegate prompts.** |

### Dual-Harness Concurrency Invariants
> [!IMPORTANT]
> **OpenCode Parallel Execution Rule**: Worker A (`GLM 5.3 Flash`) and Worker B (`Meta Muse Spark 1.3`) are designed to operate concurrently in parallel on separate tasks (e.g. terminal execution + database modeling).
> However, **NEVER spawn more than ONE instance of `opencode-go/glm-5.3-flash` simultaneously**. Exceeding 1 concurrent GLM Flash instance triggers API provider concurrency lockouts (HTTP 429).
> **Cline Parallel Execution Rule**: Worker C (`GLM 5.3 Flash`) and Worker D (`Meta Muse Spark 1.3`) operate in a separate harness (`cline`). Worker C can run safely in parallel with Worker A without triggering lockouts, providing 100% capacity boost.

### Mandatory Testing & Verification Monopoly
> [!CAUTION]
> **Exclusive Testing Rule**: `Claude Code CLI` is the **ONLY** agent authorized to execute test suites (`test`, `analyze`, lint audits) and grant final approval (`[APPROVED]`).
> Neither Codex, OpenCode, Antigravity CLI (`agcli`), nor Antigravity IDE may run test commands or sign off on feature quality gates.

### Token Conservation & Context Window Sweet Spot Governance (Hook 27 & Hook 25)
Governed by [.agents/CONTEXT_GOVERNANCE.md](file:///f:/AI%20PROJECTS/Autovemtech/.agents/CONTEXT_GOVERNANCE.md):
- **Golden Sweet Spot (30,000 – 300,000 tokens):** Peak attention fidelity is strictly confined to this range.
- **4 Operational Zones:**
  1. **🟢 Green Zone (< 180k tokens):** 100% attention; normal operations with `lean-ctx` signatures.
  2. **🟡 Yellow Zone (180k – 240k tokens):** Strict ban on `mode='full'` reads; line range inspection only.
  3. **🟠 Orange Zone (240k – 300k tokens):** Scope freeze, clean git commit, log ADR in `MEMORY_STORE.md`, generate resumption prompt.
- **Active Offloading Engine:** Route all boilerplate, shell commands, and DB migrations to OpenCode CLI (GLM 5.3 Flash & Meta Muse Spark) to conserve expensive Claude Opus Max token budget.
- **Claude-Exclusive Statusline Golden Safety Calibration (150,000 tokens):**
  The 150,000 token safety cap applies **EXCLUSIVELY to Claude Code CLI** (and not to other agents like Antigravity IDE / Gemini or OpenCode, which operate up to their 300,000 golden sweet spot or full model limits). Claude statusline scripts (`statusline.ps1` / `statusline.js`) must strictly calculate context consumption against this 150,000 cap to safeguard against Claude's quadratic cache cost escalation $O(N^2)$.
  Display format: `ctx {tokens}k/150k ({pct}%)` with calibrated thresholds: Cyan (<60%), Yellow (60–84%), Red (>=85% / strategic clear trigger).

---

## 5. Non-Negotiable Boundaries & Security Perimeter

1. **Quarantine / Strict No-Touch Areas**:
   - **`Digital persona/`**, raw personal passport scans/documents, and root personal payment/tax PDFs are strictly confidential.
   - AI agents must **NEVER** read, modify, index, or transmit raw personal identity files from these directories.
2. **Secrets & Environment Variables**:
   - **NEVER** hardcode credentials or tokens in markdown, scripts, or git commits.
   - All secrets must be loaded from `.env`.
3. **Language Policy**:
   - **Terminals, CLIs, Git Commits, and Code**: English strictly.
   - **Client Deliverables, Proposals, and Ad Copy**: Flawless, professional Arabic (RTL compliant), unless specifically requested otherwise in the client profile.
4. **Rule 50 (English Numerals & Zero-Pricing Standard)**:
   - Always use Western Arabic numerals (`0-9`) exclusively in all official documents, reports, proposals, and Excel sheets. Never use Eastern Arabic numerals (٠-٩).
   - Never invent or assume unconfirmed prices. Use smart formulas (e.g. `=IF(K9="","",I9*K9)`) leaving blank cells until confirmed.
5. **Rule 51 (Clean Templates & Zero Cross-Project Bleed)**:
   - Templates in `_Agency_Templates/` and MCP docx generators must be 100% generic structural templates.
   - Never allow data from one client (e.g., Shajan) to bleed into another (e.g., YAZ or Apex).
   - **Rule 51+ (Data Source Independence — Standalone App Invariant)**: When building any financial or management app from an external data source (Excel, CSV, JSON):
     1. **Phase 1 (Active):** Build with real client data for verification (`initialData_[client].js`).
     2. **Phase 2 (Purge):** `initialData.js` (the template) MUST be purged to generic professional roles (e.g., `treasurer`, `site_engineer`) and all balances zeroed (`0.00`).
     3. **Phase 3 (Dual Output):** The build script MUST produce TWO standalone HTML files: one with live client data (`_النشطة_البيانات_الحالية.html`) and one blank template (`_نسخة_جديدة_فارغة.html`).
     4. **Absolute Prohibitions in template files:** No personal names, no Excel cell references (e.g., `C92`, `C94`), no employee-named tabs or labels anywhere in UI, documentation, or export headers.
6. **Rule 1 (BiDi & RTL Isolation & Artifact Pre-Flight Gate)**:
   - **Mandatory `<div dir="rtl">` Wrapper:** Every Arabic markdown artifact (`implementation_plan.md`, `walkthrough.md`, `learning_proposal.md`, reports, client summaries) MUST strictly begin with `<div dir="rtl">` on Line 1 and end with `</div>` on the final line.
   - **Strict BiDi Isolation:** In all Arabic markdown, HTML, and SVG files, isolate all English terms, acronyms, model names, CLI commands, code symbols, and file paths using `<bdi>` (e.g. `<bdi>OpenCode CLI</bdi>` or `<bdi>`web/print/`</bdi>`) or `<span dir="ltr">` to prevent punctuation and line-flow inversion.
7. **Rule 7 (Digital Concierge & Government Visa Services Standard)**:
   - **Role Definition:** Act strictly as a "Tech Facilitator & Digital Concierge" assisting non-tech-savvy clients with official government self-service portals (e.g. Nusuk B2C at `umrah.nusuk.sa`, 96-hour Transit Visa via Saudia/Flynas).
   - **100% Upfront Collection Rule:** Always collect the full government fees + agency facilitation fees from the client upfront before initiating payment. Use a dedicated prepaid digital card (STC Pay / Urpay) loaded only with the exact transaction amount to ensure 100% financial security.
   - **No Unlicensed Brokering:** Never practice commercial visa brokerage as an individual (violates Saudi Labor Law Article 39 and Anti-Concealment). All commercial expansions must route through official B2B partnerships with licensed Saudi Umrah operators and transportation companies in Makkah and Madinah.
8. **Rule 8 (Zero-Day Git Init & Proactive User Reminder Standard)**:
   - **Mandatory Repository Foundation:** Every software engineering project must have an active local Git repository (`.git`) linked to GitHub from Day 0 before writing application code.
   - **Proactive AI Reminder Gate:** If the user starts a project or requests feature development in a workspace that lacks `.git`, the AI assistant MUST proactively pause, alert, and remind the user: *"⚠️ تنبيه حوكمة: مساحة العمل لا تحتوي على مستودع Git مهيأ (`.git`). يجب إنشاء المستودع وضبط الحجر الصحي في `.gitignore` وتثبيت أول commit لحماية المخرجات وتفعيل بروتوكول التصفير الاستراتيجي (Hook 25)."*
   - **Strict AI Quarantine in `.gitignore`:** The repository's `.gitignore` must immediately quarantine: `Digital persona/`, `.agents/`, `.claude/`, `fleet_orders/`, `fleet_templates/`, `.env`, and build caches (`**/build/`, `**/.gradle/`, `*.log`).
9. **Rule 9 (Decimal-to-Float JSON Invariant for API Endpoints)**:
   - In FastAPI/Python endpoints querying raw SQL monetary amounts or database aggregates (`sum(amount)`), **NEVER** return raw `Decimal` objects in response payloads.
   - Pydantic v2 automatically serializes raw `Decimal` objects as JSON strings (`"500.00"`), breaking client arithmetic and test comparisons.
   - All response dictionaries containing monetary/numeric fields must be routed through the recursive `_convert()` serializer (e.g. `return _resp(...)`) to guarantee all `Decimal` values are converted to rounded `float` numbers (`round(float(val), 2)`).
10. **Rule 10 (Room Topological Sync & Outbox Protection Invariant)**:
    - In offline-first Android apps using Room with foreign keys enabled (`PRAGMA foreign_keys = ON`), local cache replacement/invalidation must strictly follow reverse topological order (leaf children ➔ parent roots) inside a single `@Transaction` method.
    - The mutation outbox (`pending_writes`) must NEVER declare foreign keys and must NEVER be cleared or touched during pull sync routines.

---

## 6. Scope Guard & Minimal Sufficient Change Directive

All agents and CLI harnesses must complete tasks with the **smallest sufficient change** while rigorously addressing the **root cause**. Explicit user instructions for a given task override default behaviors below.

### 6.1. Pre-Edit Discovery & Context Restraint
- **Targeted Inspection**: Read only immediately relevant code, call paths, and conventions using `lean-ctx` (`mode='signatures'`) or line-range slices. Do **NEVER** read entire repositories or large files (`mode='full'`) for small modifications.
- **Skill Parsimony**: Load only skills strictly required for the immediate task; do not load expansive multi-agent workflows for casual keyword triggers.
- **Direct Execution**: Make clear, small fixes directly. Draft a formal `implementation_plan.md` only when the approach is ambiguous, architecture-altering, or high-impact.
- **Autonomous Routine Decisions**: Resolve routine implementation details independently. Escalate only when conflicting interpretations lead to materially divergent outcomes.

### 6.2. Implementation Hierarchy & Anti-Band-Aid Mandate
- **Implementation Precedence**: Before writing any new code, evaluate solutions in this strict order:
  1. Existing patterns, utilities, and helper functions within the repository.
  2. Standard library and built-in runtime/platform capabilities.
  3. Already installed dependencies and frameworks.
  4. Only then, minimal new code strictly necessary for the active scope.
- **Root-Cause Invariant**: Address root causes directly. Never deploy superficial band-aids (e.g. suppression blocks, blind `try/except: pass`, or brittle hacks). Do not introduce speculative abstractions, factories, or compatibility layers for hypothetical future needs.
- **Dependency Justification**: Never introduce new external libraries or packages without explicit technical justification explaining why existing installed dependencies are insufficient.
- **Scope Discipline**: Fix nearby issues only if they directly block the active task; otherwise, flag them as technical debt for follow-up. Avoid unrelated refactoring or full-file rewrites.
- **Dead Code Cleanup**: Purge replaced code paths immediately. Retain legacy shims only when explicit backward compatibility is mandated.
- **Non-Negotiables**: Never compromise validation, error handling, security, or accessibility (WCAG AA) in the name of brevity.

### 6.3. Escalation & Authorization Gates
- **Continuous Execution**: Keep implementing, verifying, and fixing within authorized scope without repeatedly pausing to ask routine confirmation.
- **Mandatory Escalation Triggers**: Halt and request explicit user confirmation before:
  - Material scope expansion beyond original intent.
  - Adding unapproved recurring costs or changing production permissions.
  - Executing irreversible or destructive system actions.
- **Analysis-Only Scope**: If requested only to analyze, inspect, or audit, report structured findings—do not modify code.

### 6.4. Fleet-Calibrated Verification & Testing Invariant
- **Fleet Testing Monopoly**: All automated test execution (`test`, `analyze`, lint gates) remains the **exclusive monopoly of Claude Code CLI (Opus Max)**. Other agents (`OpenCode`, `agcli`, `Codex`) must perform static code inspection and rely on Claude Code for automated test execution and final `[APPROVED]` status.
- **Verification Parsimony**: Reuse existing tests first. Add new tests only for real behavior and genuine regression risks—never mechanical tests that mirror implementation details.
- **Ephemeral Verification**: Keep scratch scripts in temporary folders; do not leave temporary verification scripts as permanent repository tests.
- **Check Restraint**: Once checks pass, repeat them only if subsequent edits are made, failures emerge, or unresolved edge cases remain.

### 6.5. Scope Growth Circuit Breaker
If the implementation plan begins to demand future-only abstractions, unrelated refactoring, peripheral features, or redundant validation loops:
1. Immediately pause and prune the excess work.
2. Re-anchor strictly to the core requirement.
3. Finish execution strictly within the originally authorized boundary.

### 6.6. Definition of Done (DoD)
A task is considered **DONE** only when:
1. The requested functionality functions completely and verification evidence is established.
2. Every modified line directly serves the task; all scratch files, debug logs, and dead code have been purged.
3. A concise summary is delivered detailing the result, verification evidence, and any unresolved items or follow-ups.
