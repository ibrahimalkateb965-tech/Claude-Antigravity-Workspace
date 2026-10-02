# 🛡️ GLOBAL ACTIVE CONTEXT INJECTION (MANDATORY TECHNICAL INVARIANTS)
> **STEP ZERO PROTOCOL:** Every agent MUST read this file at Step Zero before writing any code or plans.
> All 77 architectural decisions, past incident post-mortems, and engineering constraints are organized by functional domain.

---

## 🏛️ DOMAIN 1: MULTI-CLI FLEET, SUBAGENTS & CONTEXT GOVERNANCE

### [C-11] Model Tiering Matrix (استراتيجية النماذج)
- **Flash (`Gemini 3.8 Flash High` / `OpenCode Muse Spark`):** Routine scripting, terminals, boilerplate, content, data layers, Git commands.
- **Pro (`Claude Sonnet 5.5`):** General programming, feature development, component refactoring.
- **Thinking (`Claude Opus 5.5 / Opus Max`):** Architecture planning, Devil's Advocate audits, invariant verification.

### [C-12] System Autonomy (استقلالية النظام)
- Systems and skills must remain 100% model-agnostic and neutral, free from hardcoded vendor-specific lock-in.

### [C-19] Smart Agent Tailoring & Pruning (التخصيص والتصدير الذكي)
- Use Hook 3 (`project_agent_tailor.py`) to analyze `PROJECT_CONTEXT.md` and export only required domain agents to `.agents/skills`.
- Prune all unused skills to eliminate system prompt token bloat by 60%+. Generate `sync_local_agents.py` for project sync.

### [C-21] Collaborative Multi-CLI Fleet Architecture (معمارية أسطول الـ CLIs)
- **Roles:**
  - `Claude Code CLI (Opus 5.5 Max)`: Master Orchestrator, architectural auditing, and **EXCLUSIVE TESTING MONOPOLY**.
  - `OpenCode CLI`: Headless terminal executor, database migrations, DAOs, and data plumbing. Models: **GLM 5.3 Flash** (`-m opencode-go/glm-5.3-flash`) and **Meta Muse Spark 1.3** (`-m opencode/muse-spark-1.3-contributor`).
  - `Antigravity CLI (agcli)`: Headless terminal executor for UI/domain contracts (`agcli run`).
  - `Codex CLI`: Algorithmic translations, complex data transforms, and deep refactoring.
  - `Antigravity IDE`: Visual Cockpit & Workbench for the developer. **NEVER delegate automated background tasks to the IDE.**
- **Standards:** Central `fleet_config.json`; all CLI communications in English; Phase Code Review Gate before transitioning phases.

### [C-25] Autonomous Direct Dispatch (بروتوكول التفويض المباشر)
- **NEVER** ask the user to manually copy/paste prompts between CLIs. Dispatch commands directly via headless flags (`claude -p "..."`, `opencode run "..."`, `agcli run "..."`).

### [C-31] Active OpenCode Token Conservation (تفعيل أوبن كود لترشيد التوكنز)
- Offload routine CLI tasks, boilerplate, and database migrations to OpenCode CLI (`GLM 5.3 Flash` / `Muse Spark 1.3`) to preserve Opus Max quota.

### [C-32] Dynamic Multi-Provider Cascading Failover (استراتيجية التراجع الديناميكي)
- When a model service fails (500/503/rate-limit), cascade dynamically:
  1. **Tier 1 (OpenCode Go):** `opencode-go/glm-5.3-flash` & `opencode-go/muse-spark-1.3-contributor`.
  2. **Tier 2 (Cloud Fast):** `google/gemini-2.5-flash` or `deepseek/deepseek-chat` (never prefix with `gemini/`, use `google/`).
  3. **Tier 3 (OpenRouter Free):** `openrouter/meta-llama/llama-3.3-70b-instruct:free` -> `openrouter/qwen/qwen-2.5-coder-32b-instruct:free`.
  4. **Tier 4 (Offline Fallback):** `ollama/qwen2.5-coder:1.5b`.

### [C-33] AI Model Directory Junctions & VRAM Protection (حماية مسارات نماذج Ollama)
- Create hard Directory Junction: `cmd /c mklink /J "C:\Users\<User>\.ollama\models" "<Secondary_Drive>\Ollama\models"`.
- On 4GB VRAM GPUs (GTX 1650), run models sequentially (`sequential_role_switching`) to prevent VRAM allocation crashes.

### [C-34] Local Ollama Hardware Matrix (مصفوفة نماذج Ollama)
- **4GB VRAM:** Default to `qwen2.5-coder:1.5b` (100% GPU VRAM allocation, 0 RAM spillover).
- **8GB+ VRAM:** Use `qwen2.5-coder:7b` (code), `deepseek-r1:7b` (reasoning), `hermes3:8b` (agents).
- Await `success` digest checksum verification before triggering API calls.

### [C-35] System-Installed Claude Code CLI Baseline (اعتماد كلود كود المثبت)
- `Claude Code CLI` is system-installed and ready on `PATH`. Never attempt reinstallation; invoke directly (`claude -p`).

### [C-36] Claude Code Extension Webview Recovery (استكشاف أخطاء واجهة كلود كود)
- On `Error loading webview: Could not register service worker`:
  1. Run `Developer: Reload Webviews` / `Developer: Reload Window`.
  2. Toggle `Claude Code: Use Terminal` in settings.
  3. Rollback via extension gear ⚙️ -> `Install Another Version`.

### [C-38] Claude 5.5 Generation Baseline (اعتماد الجيل 5.5 لكلود)
- Reference active 5.5 gen matrix: `Claude Opus 5.5 / Opus Max` (audits & tests), `Claude Sonnet 5.5` (daily dev), `Claude Fable 5` (long-horizon agents), `Claude Haiku 4.5` (micro-tasks).

### [C-45] Fleet Quota Limit Failover (التراجع الذاتي لقيادة الأسطول)
- If `Claude Code CLI` session quota is exhausted: `Antigravity IDE (Gemini 3.8 Flash High)` immediately assumes fleet leadership, code edits, and quality gates. Routine tasks continue on `OpenCode CLI`. Claude resumes test monopoly once quota resets.

### [C-60] Strategic Clear & Headless agcli Delegation (التصفير الاستراتيجي وعزل agcli)
- Apply 3-stage Strategic Clear: (1) Green build & clean Git commit, (2) Snapshot to `CURRENT_STATE.md` and `MEMORY_STORE.md`, (3) Resume prompt generation before `/clear`.
- Headless code generation targets `agcli run`, never `Antigravity IDE`.

### [C-62] Golden Productivity & Context Governance (النطاق الذهبي للإنتاجية 30k-300k)
- Gauge context telemetry via `ag-context` or `context-advisor` (SQLite WAL telemetry).
- **Sweet Spot Zones (30,000 - 300,000 tokens):**
  - **Green (< 180k | < 60%):** 100% attention fidelity. Standard progressive disclosure (`mode=signatures`).
  - **Yellow (180k - 240k | 60-80%):** Early warning. Ban full file reads (`mode='full'`); slice line ranges <= 50 lines.
  - **Orange (240k - 300k | 80-100%):** Pre-handoff planning. Finalize active micro-task, clean `git commit`, save lessons to `MEMORY_STORE.md`.
  - **Red (> 300k | > 100%):** Attention degradation. Block new tasks; execute Hook 25 (Strategic Clear), generate resume prompt, and run `/clear`.

### [C-64] IDE vs CLI & Subscription-Aware Fleet Routing (التوجيه المدرك للاشتراكات)
- Never target `Antigravity IDE` with background automated task dispatches. Failover UI tasks to `OpenCode GLM 5.3 Flash`.
- Route only to user-active subscriptions (Claude Code for tests, OpenCode Go for terminal/plumbing, Codex for algorithms).

### [C-67] Claude Code 150k Ceiling Calibration (معايرة شريط حالة كلود على 150k)
- The 150k token ceiling applies **EXCLUSIVELY to Claude Code CLI** to avoid quadratic $O(N^2)$ cache costs (67% of cost waste happens >150k).
- Calibrate statusline (`statusline.ps1` / `statusline.js`) against 150,000 tokens: `ctx {k}/150k ({pct}%)`.

### [C-76] Subagent Governance & Progressive Disclosure (حوكمة الوكلاء وترشيد التوكنز)
- **Targeted Dispatch vs Swarm Anti-Pattern:** Ban concurrent multi-agent background swarms reading same files. Dispatch single-responsibility ephemeral agents. Parallelism allowed only for read-only analytical agents on Flash models.
- **Progressive Disclosure:** Ban reading full files (`mode='full'`). Read headers/signatures (`mode=signatures` via lean-ctx). When inspecting logic, restrict slice to <= 50 lines.
- **Artifact-Driven Handoff:** Transfer context via lightweight intermediate files (`implementation_plan.md`, `CURRENT_STATE.md`), never verbose chat dumps.

### [C-77] Teamwork-Preview Governance & Sheet Separation (حوكمة teamwork-preview)
- **DO NOT** use `teamwork-preview` for daily/routine tasks; it spawns 7 cascade agents (Orchestrator -> Implementer -> R1/R2/R3 Reviewers -> Sentinel -> Victory Auditor), multiplying token burn 7x. Use single-agent Targeted Dispatch.
- Restrict `teamwork-preview` strictly to comprehensive pre-release audits before app store submissions.
- Isolate Antigravity/Gemini commands in dedicated `.Antigravity` sheets in `مكتبة الأوامر.xlsx`.

### [C-78] OpenCode CLI Headless Automation & Batch Sizing Standard (معيار تشغيل أوبن كود غير التفاعلي)
- عند تشغيل `opencode run` في الخلفية أو عبر أوامر برمجية (مثل تفويض Claude Code لـ GLM):
  1. **إلزامية علم `--auto`:** يجب دائماً تضمين `--auto` للسماح التلقائي بالصلاحيات غير المحظورة؛ تشغيل الأمر بدونها يتسبب في تعليق الطرفية لانتظار إدخال TTY تفاعلي.
  2. **قاعدة الدُفعات الرباعية (Max 4 Files Per Batch):** يُحظر تكليف GLM أو OpenCode بتعديل أكثر من 4 ملفات في جلسة `opencode run` واحدة لتفادي انقطاع السياق أو اقتطاع الكود، مع تجزئة المهام الكبيرة لدُفعات متتابعة.
  3. **أمر بناء Gradle في ويندوز:** في بيئة PowerShell على Windows، يُنفذ أمر البناء بالصيغة: `& .\gradlew.bat assembleDebug` وتجنب استدعاء `cmd /c "gradlew.bat"` العاري لتفادي أخطاء مسار العمل.

---

## 📦 DOMAIN 2: REPOSITORY, GIT INVARIANTS & CENTRAL WORKSPACE

### [C-04] GitHub Pages Markdown Rendering (.nojekyll)
- Always place an empty `.nojekyll` file in repo root to disable Jekyll processing and allow direct raw Markdown rendering.

### [C-13] Monorepo & Multi-Project Memory Architecture (هيكلية الذاكرة اللامركزية)
- Maintain decentralized memory: Each project must have its own `.agents/` directory containing local `MEMORY_STORE.md` and `PROJECT_CONTEXT.md` to prevent cross-context bleed.

### [C-14] Legacy Project Backward Compatibility (توافقية المشاريع القديمة)
- In legacy projects with existing `.agents` and `AGENTS.md`, apply **add-only** policy. Inject `MEMORY_STORE.md` and `PROJECT_CONTEXT.md` without replacing existing rules.

### [C-16] Watchdog Session Hook (الخطاف الحارس لمنع فقدان الذاكرة)
- Auto-extract git diffs and persist lessons to `MEMORY_STORE.md` prior to closing sessions, without waiting for manual user trigger.

### [C-59] Universal Project-to-Central AI Tooling Export (التصدير الإلزامي للمستودع المركزي)
- Because individual project `.gitignore` files quarantine AI configs, all AI tools, skills, rules, and memory updates MUST be exported to the central repo:
  - Local Path: `F:\AI PROJECTS\Claude+Antigravity`
  - Command: `python .agents/sync_local_agents.py --export` or `& "F:\AI PROJECTS\Claude+Antigravity\export_ai_tools.ps1"`.

### [C-63] Workspace Tooling Evacuation & Sanitization (تطهير أدوات المحرر - Hook 28)
- Central tooling lives exclusively in `F:\AI PROJECTS\Claude+Antigravity`.
- **SHA-256 Verified Transfer:** Never delete a local tool file until SHA-256 hashes match 100% with the central destination.
- Purge JVM crash dumps (`hs_err_*.log`, `replay_*.log`) and Word lockfiles to maintain a pristine Git working tree.

### [C-66] Mandatory Zero-Day Git Init (التهيئة الإلزامية لمستودع Git)
- Every coding project MUST have a local Git repo initialized (`git init`) before writing code.
- Strict `.gitignore` quarantine for: `Digital persona/`, `.agents/`, `.claude/`, `fleet_orders/`, `fleet_templates/`, `.env`, `**/build/`, `*.log`.
- **Proactive Reminder Gate:** If user starts coding in an uninitialized directory, immediately halt and prompt Git initialization.

### [C-73] Central Repo AI Commit Invariant (التوجيه الإلزامي لعمليات Git Commit)
- **NEVER** commit AI governance files (`AGENTS.md`, `HOOKS_GUIDE.md`, `MEMORY_STORE.md`, skills, prompt libraries) inside individual app repos.
- Always commit AI governance changes exclusively to:
  - Path: `F:\AI PROJECTS\Claude+Antigravity`
  - Remote: `https://github.com/ibrahimalkateb965-tech/Claude-Antigravity-Workspace`.

---

## 📱 DOMAIN 3: ANDROID, FLUTTER, AUDIO & MOBILE RUNTIME

### [C-09] TalkBack Two-Finger Horizontal Pager (السحب الأفقي الصامت)
- Avoid wrapping Pager in custom blocking containers. Use standard `HorizontalPager` adhering to Dual-Mode Architecture.
- Provide a silent contentDescription containing a single comma `,` to silence TalkBack chatter during 2-finger page swipes.

### [C-10] Feature Deletion Memory (حماية الميزات المحذوفة)
- **NEVER** re-introduce UI components or features deleted in previous commits (e.g., `PlayerControlPanel`, voice controls) unless explicitly requested.

### [C-26] Flutter SDK Space Junction Standard (معالجة مسافات مسار Flutter SDK)
- Spaces in Flutter SDK path break C/C++/SQLite `native-assets` compilation.
- Create junction without spaces: `cmd /c mklink /J "C:\flutter_sdk" "<Flutter_Path>"`. Execute Flutter commands exclusively through `C:\flutter_sdk`.

### [C-41] Post-Edit Automatic Device Deployment (التشغيل والتثبيت التلقائي على الهاتف)
- After completing any feature/fix, auto-install and launch the latest build on connected ADB device:
  `& "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe"` or `& "C:\flutter_sdk\bin\flutter.bat" run -d <device_id>`.
- If device is `unauthorized`, prompt user to accept USB Debugging dialog on phone.

### [C-42] Flutter RTL Visual Overflow Prevention (تحصين واجهات Flutter ضد التجاوز البصري)
- **NEVER** place long text headers adjacent to action buttons inside an unconstrained `Row`.
- Wrap headers in `Wrap(alignment: WrapAlignment.spaceBetween, spacing: 8, runSpacing: 8)` or `Expanded(child: Text(..., maxLines: 1, overflow: TextOverflow.ellipsis))` to prevent `RIGHT OVERFLOWED BY X PIXELS` bars.

### [C-44] ADB Package Launch Standard (معيار إطلاق تطبيقات Flutter عبر ADB)
- Extract actual `applicationId` from `build.gradle.kts` (never guess package name).
- Launch stably via: `& "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe" shell monkey -p <pkg> -c android.intent.category.LAUNCHER 1` (wake screen with `keyevent 26/82` if sleeping).

### [C-46] MediaButton Event Interception on Call Termination (منع استئناف الصوت بعد المكالمات)
- In `MediaSessionService` / `ExoPlayer`: Intercept and consume (`return true`) media button events (`ACTION_DOWN` & `ACTION_UP` for `KEYCODE_MEDIA_PLAY`, `KEYCODE_MEDIA_PLAY_PAUSE`, `KEYCODE_HEADSETHOOK`) whenever player is not actively playing (`!player.playWhenReady || STATE_IDLE || STATE_ENDED`).
- Override and disable `onPlaybackResumption` to prevent Android Bluetooth stack from resuming audio after phone calls.

### [C-47] Google Play Release Notes Language Tagging (وسوم لغة ملاحظات الإصدار)
- Release notes in Google Play Console MUST be wrapped in language tags: `<ar>...</ar>` and `<en-US>...</en-US>`. Bare text triggers `Line 1: text outside language tags` validation error.

### [C-56] Google Play Developer Account Policy (متطلبات حسابات المطورين)
- Organization account with D-U-N-S number mandatory for financial, health, `VpnService`, and government apps.
- Provide active demo credentials/QR for Google review teams. Prohibit third-party account leasing; use official app transfer tools.

### [C-72] Room Topological Sync & Outbox Protection (الترتيب الطوبولوجي لمزامنة Room)
- **Reverse Topological Deletion:** When replacing local Room DB scopes with foreign keys enabled (`PRAGMA foreign_keys = ON`), delete child records before parent records (logs -> reports -> students -> circles) to avoid `SQLiteConstraintException`.
- **Outbox Invariant:** The `pending_writes` table MUST NOT have foreign keys and MUST NEVER be cleared or wiped during pull sync.
- Execute delete-and-reinsert atomically inside `@Transaction` on `RoomDatabase` to eliminate UI Flow flickering.

### [C-74] PowerShell JVM Arguments & gradle.properties Standard (معاملات JVM في PowerShell)
- Never pass bare `-Dorg.gradle.jvmargs=...` in PowerShell; pwsh parses `-Dorg` as task name.
- Define JVM heap limits in `gradle.properties` (`org.gradle.jvmargs=-Xmx2048m`). If passed via CLI, wrap in quotes: `"-Dorg.gradle.jvmargs=..."`.

### [C-79] Android Room Repository IO Dispatching & Triple-Trigger Auto-Sync (معيار حماية مستودعات Room والمزامنة التلقائية)
- **عزل خيوط المعالجة في المستودعات (Repository IO Dispatching):**
  - في طبقة الـ Repository، يجب دائماً تغليف عمليات قاعدة بيانات Room (`upsert`, `replaceScope`, `insert`) بـ `withContext(Dispatchers.IO)`.
  - عودة استجابات Retrofit إلى خيط المعالجة الرئيسي (Main Thread) تتسبب في انهيار التطبيق بـ `IllegalStateException: Cannot access database on the main thread` فور محاولة الحفظ في Room حتى لو نجح طلب الشبكة (200 OK).
- **نمط المزامنة التلقائية الثلاثي (Triple-Trigger Auto-Sync Pattern):**
  1. **المزامنة عند إنشاء النموذج (`ViewModel.init { refresh() }`):** إلزامية إطلاق المزامنة في كتلة `init` لجميع شاشات العرض الرئيسية لضمان تحديث البيانات فور فتح الشاشة دون انتظار سحب الشاشة يدوياً (Pull-to-Refresh).
  2. **المزامنة عند الرجوع للتطبيق (`LifecycleResumeEffect`):** ربط الشاشة بدورة حياة أندرويد لإعادة المزامنة تلقائياً عند عودة التطبيق من الخلفية.
  3. **المزامنة الاستباقية عند الإقلاع البارد (`RootNavHost`):** إطلاق `pullSync.requestFull()` في خلفية Coroutine فور استعادة الجلسة النشطة (`SessionState.Active`).
  4. **حراسة وميض الشاشة الفارغة (Anti-Flash Guard):** في شاشات العرض، يجب ربط حالة `isLoading` بحالة المزامنة السحابية الجارية لمنع ظهور رسائل خلو البيانات (مثل "لا يوجد أبناء") قبل اكتمال وصول بيانات الشبكة.

---

## ⚡ DOMAIN 4: BACKEND, CLOUD, FASTMCP & SCRIPTING

### [C-03] PowerShell Large File Downloads (BITS Transfer)
- Use TLS 1.2 and BITS transfer (`Start-BitsTransfer`) rather than `Invoke-WebRequest` to prevent connection drops on large binaries.

### [C-05] Windows UTF-8 Python Console Encoding (ترميز بايثون على ويندوز)
- Use `sys.stdout.reconfigure(encoding='utf-8')` to prevent `UnicodeEncodeError (cp1252)` on Arabic/emojis in Windows console.

### [C-06] Telegram Bot Background Process Conflicts (تعارض عمليات البوت)
- HTTP 409 Conflict indicates dangling background `python.exe`/`pythonw.exe`. Kill orphan processes via `taskkill /F /PID <PID>` (run shell as Admin if elevated).

### [C-22] DevCache Junctions for C: Drive Space Protection (ترحيل كاش التطوير)
- Prevent C: drive exhaustion by junctioning heavy caches to secondary disk (`D:\DevCache\`):
  - `.gradle`, `.cargo`, `.rustup`, Docker disks, and Claude Desktop VM bundles (`AppData\Roaming\Claude\vm_bundles`).
  - Clean Python uv cache regularly: `uv cache clean`.

### [C-27] PocketBase Schema Migration Dependency Order (ترحيل مخططات PocketBase)
- PocketBase does not allow mutating `auth` collections to `base` if referenced by foreign keys.
- Delete conflicting collections in reverse dependency order (children first: `daily_logs` -> `weekly_reports` -> `student` -> `students` -> `teachers` -> `circles` -> `academies`) before re-importing unified schema with `deleteMissing=false`.

### [C-28] Non-Interactive Scripting Standard (إلزامية السكربتات غير التفاعلية)
- Scripts executed in background/pipes MUST NOT use interactive TTY inputs (`getpass`, `input()`). Accept credentials via CLI flags (`--password`) or environment variables.

### [C-29] FastMCP Stdio Buffer Integrity Standard (أمان قنوات FastMCP)
- In FastMCP / stdio MCP servers: **NEVER** detach or wrap `sys.stdout` with `codecs.getwriter`. FastMCP requires binary `sys.stdout.buffer` for JSON-RPC. Direct all logging and debug statements to `sys.stderr` exclusively.

### [C-30] Absolute Path Terminal Command Standard (المسارات المطلقة للأوامر)
- **NEVER** propose commands with naked relative paths (`cd "dir"` or `./script.ps1`). Always provide full absolute paths wrapped in quotes: `& "<Full_Absolute_Path>"`.

### [C-37] MCP 64-Character Identifier Limit (قيد طول أسماء أدوات MCP)
- Tool names `mcp_<server_name>_<tool_name>` must strictly match `^[a-zA-Z0-9_-]{1,64}$`. Names >64 chars crash tool loading in the IDE.

### [C-40] Cloud Relational Sync & Fallback (ربط العلاقات والمزامنة السحابية)
- Explicitly pass `teacherId` from active session (`currentUser.id`). Fallback to Auth Store ID in `SyncEngine` to prevent `400 (teacher_id: validation_required)`.

### [C-52] Autovem VPS Mailbox Quota Standard (حصص البريد على VPS)
- Default professional mailbox quota is **1 GB per mailbox** on 50GB NVMe VPS instances. State in commercial proposals as *"scalable cloud storage"* without contractual over-commitments.

### [C-53] Cloud VPS Multi-Agent Container Hosting (منصات حاويات الوكلاء السحابية)
- Deploy Linux containerized multi-agent engines (LXD / Docker) on Hostinger Ubuntu 24.04 VPS for 24/7 uptime. Windows desktop is reserved for Antigravity IDE and interactive development.

### [C-54] Token Isolation & Real Verification (عزل التوكنز ومنع الفحص الصوري)
- Store API tokens in `.env` (git-quarantined). Testing scripts must exit with code `sys.exit(1)` on failure. `session:NN%` in Claude statusline denotes active context window (out of 200k), not 5-hour rate limits.

### [C-70] SPA Anti-Cache & Non-Hanging Base64 Deploy (منع احتباس الكاش والنشر عبر Base64)
- In Nginx/Caddy for SPAs, enforce `header Cache-Control "no-cache, must-revalidate"` on static views. Append version query params `?v=X.X` to dynamic view imports.
- Deploy files via SSH without stdin hangs: `echo '<base64_data>' | base64 -d > '<target_file>'`.

### [C-71] Telegram ACI Mobile Wakeup & Audio Handling (واجهة تليجرام لإيقاظ المحرر)
- Run listener in one-shot mode (`poll_once.py --takeover`) as a Background Task in Antigravity. When a command arrives, exit cleanly with high priority to wake the IDE.
- Convert voice notes from OGG to WAV (`pcm_s16le`) via PyAV to bypass MIME type limits. Sanitize bare underscores `_` to prevent `400 Bad Request: can't parse entities`.

### [C-80] OTP Gateway Graceful Sandbox Fallback Standard (معيار وضع المحاكاة الثنائي لرموز التحقق)
- عند غياب إعدادات بوابة الإرسال الخارجية (SMS أو WhatsApp) في بيئة الإنتاج أو التطوير (`WHATSAPP_API_URL` و `WHATSAPP_API_TOKEN` غير مضبوطين):
  1. **إعادة الرمز برمجياً:** يُلزم خادم الـ API بإرجاع حقل `"sandbox_code": code` ضمن استجابة JSON لنقطة النهاية `/auth/request-otp` لإعلام العميل بالرمز دون تسريبه للمستخدمين غير المصرح لهم.
  2. **قبول رمز التجاوز الموحد (`1234`):** يجب أن تقبل نقطة النهاية `/auth/verify-otp` الرمز `1234` في وضع المحاكاة مع استمرار تطبيق كافة قيود الجلسة (الصلاحية، عدد المحاولات، عدم الاستخدام المسبق) لضمان عدم تعليق عمليات الفحص والتسليم.
  3. **التعبئة التلقائية الآمنة:** في تطبيقات الهاتف، يُلتقط `sandbox_code` تلقائياً لتعبئة حقل الرمز مع عرض إشعار تنبيهي لطيف يتيح الدخول بنقرة واحدة.

---

## 🎨 DOMAIN 5: UI/UX, RTL/BIDI, TYPOGRAPHY & DOCUMENT ENGINES

### [C-01] Skill Frontmatter Standard (هيكل المهارات الموحد)
- Every skill (`SKILL.md`) must include valid YAML frontmatter with `name` and `description`, registered in `sub_agents.yaml`.

### [C-20] PDF & Written Document Design Identity (تصميم وتوليد ملفات PDF)
- Use Dark Neon Candy palette (#0a0a14 with Neon Purple/Cyan accents). Enforce zero blank pages, balanced grid typography (Tajawal/Outfit/Cairo), and tight visual density.

### [C-23] TBC Handover Box & Checklist Automation (أتمتة نماذج تسليم TBC)
- Preserve merged table cell structures. Replace header logo in `word/media/image2.jpg` maintaining aspect ratio. Export to PDF via Word COM Automation.

### [C-48] Clean PDF Rebuild & Visual Marker Standard (إعادة البناء النظيف لملفات PDF)
- Re-generate modified PDFs from clean backup (`_backup.pdf`) using single-pass lossless overlay.
- Auto-decode English-typed Arabic keystrokes (`hvd]` -> `أريد`). Enforce safe print margins (>= 25pt).

### [C-49] Multi-Edition PDF Scope Isolation (حماية النسخ المعتمدة)
- Never overwrite approved master PDFs (e.g. Arabic original). Branch clean English versions to dedicated filenames (`_EN.pdf`). Verify file integrity via `stat/mtime`.

### [C-50] Emaar Al-Farah English Numerals & Zero-Pricing (الأرقام الإنجليزية وحظر وضع الأسعار)
- Enforce standard English numerals (0-9) strictly across all documents; zero Indic digits (`[٠-٩] == 0`).
- **Strict Zero-Pricing Rule:** Never invent prices/totals unless explicitly supplied. Format blank price slots as `[             ر.س ]` with conditional Excel formulas `=IF(K9="","",I9*K9)`. Single-page A4 invariant.

### [C-51] Clean Template & Zero Cross-Project Bleed (نقاء قوالب التوليد)
- Templates used in MCP document tools (`arabic-docx-builder`, `boq-generation`) must be pure structural skeletons containing only styles, margins, and `{{CONTENT_HERE}}`. No past client data.

### [C-55] Emaar Al-Farah Corporate Profile Standard (ترويسة البروفايل الرسمية)
- Use full banner header `header_emaar.png`. Restrict corporate headquarters to **«الرياض»** (Riyadh) only; ban previous local addresses. Apply Amiri font family (`Amiri-Bold` / `Amiri-Regular`).

### [C-57] Rule 1 BiDi Isolation & Flowchart Studio (عزل BiDi واستوديو المخططات)
- Isolate Latin technical terms in Arabic text via `<bdi>`, `<span dir="ltr">`, or code blocks.
- Mermaid diagrams: Use clean Latin node IDs (`node_1`), enclose Arabic labels in quotes `["..."]`. Use `flowchart_studio` for 4K PNG / SVG renders.

### [C-58] UI/UX Pro Max Visual Production Standard (معايير التصميم البصري)
- Enforce WCAG AA contrast (>= 4.5:1). Emphasize central core engine node with color glow. Apply typography pairing (Cairo headers, Tajawal descriptions, Fira Code for symbols).

### [C-68] Egyptian Phone Number Prefix & Zero Duplication (صياغة أرقام الهواتف)
- When prefixing Egyptian phone numbers with `+20`, strip the trunk zero: format as `+20 10xxxxxxxx` or `+20 11xxxxxxxx` (never `+20 010...`).

### [C-69] OpenXML BiDi Paradox & DOCX RTL Standard (مفارقة محاذاة OpenXML في Word)
- In Word DOCX `<w:bidi/>`, **NEVER** use right alignment `<w:jc w:val="right"/>` (pushes text to extreme visual left). Use `<w:jc w:val="both"/>` or `<w:jc w:val="left"/>` inside `<w:pPr>` to align right visually (`block[2] >= 520 pt`).
- Inject RLM (`\u200F`) at lines starting with Latin words or numbers. Check PyMuPDF bounding boxes and close PDF locks (`FoxitPDFEditor.exe`) before COM export.

### [C-75] Industrial Quotations & Full-Page Density Standard (عروض الأسعار الصناعية)
- Use official 300 DPI high-res letterhead for Creative Metal Platform Factory (unified no. 7038153537) as page underlay.
- Zero-terms policy: place overall total table, Saudi Building Code declaration, and stamp immediately after final item. Zero blank pages; auto-sync copies to Google Drive (`H:\My Drive\اعمار الفرعة`).

---

## 📚 DOMAIN 6: PROMPT LIBRARY & SUSTAINED MEMORY ECOSYSTEM

### [C-02] Generative Engine Optimization (تحسين محركات البحث التوليدية GEO)
- Boost fact density, adopt authoritative expert tone, structure content into self-contained quote-friendly paragraphs for AI RAG engines.

### [C-07] Mandatory Devil's Advocate Audit (التدقيق المعماري الصارم)
- Reject superficial band-aid solutions. Activate rigorous adversarial review covering: architectural completeness, edge cases, lifecycle hazards, and non-superficiality.

### [C-08] Mandatory Context Injection (السياق المحقون الإجباري)
- Prevent shortcut anti-patterns by programmatically injecting past failure modes and lessons into the agent's active reasoning before implementation.

### [C-15] Prompt Library Dashboard Column Separation (تخصيص أعمدة لوحة القيادة)
- In prompt library spreadsheets, Column 3 is reserved strictly for Command/Trigger text (for one-click copying). Column 4 contains description and notes.

### [C-17] Devil's Advocate Persona Gate (الفرض الإجباري لبرومبت محامي الشيطان)
- For every new feature, pause before implementation and generate: `[DEVIL'S ADVOCATE CRITIQUE]`, `[ENGINEERING FIXES]`, `[MASTER REFINED PLAN CONSTRAINTS]`, and `[UNINTENDED SIDE-EFFECTS & LIFECYCLE HAZARDS]`.

### [C-39] Upstream Skill Directory Identity Standard (الحفاظ على اسم المنبع للمهارات)
- Maintain upstream directory names and bundle structures (`bin/`, `scripts/`). Symlink (`mklink /J`) across agent roots (`~/.claude/skills` and `~/.gemini/config/skills`).

### [C-43] SaaS Tiering & Free Trial Standard (نموذج اشتراكات SaaS للحلقات)
- Individual teacher account is 100% free. Academy Tier offers 7-day card-upfront free trial, auto-billing on day 8 with cancellation from settings.

### [C-61] Smart Prompt Library Curator & Automation (أتمتة مكتبة الأوامر الذكية)
- Capture engineered prompts in three-part format: (1) Emoji + Descriptive Name, (2) Prompt Command, (3) Usage Notes.
- Auto-route to `مكتبة الأوامر.xlsx` using `prompt_library_curator.py` with Segoe UI and Consolas typography, falling back to Excel COM if locked.

### [C-65] Digital Concierge & Upfront Payment Standard (التيسير الرقمي والتأشيرات)
- Agency acts as technical facilitator for government portals (Nusuk / 96h Transit). Enforce 100% upfront payment via isolated digital card (STC Pay). B2B partnerships only.\n