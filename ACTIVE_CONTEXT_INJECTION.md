# 🛡️ GLOBAL ACTIVE CONTEXT INJECTION (MANDATORY TECHNICAL INVARIANTS)
> **STEP ZERO PROTOCOL:** Every agent MUST read this file at Step Zero before writing any code or plans.
> All 89 architectural decisions, past incident post-mortems, and engineering constraints are organized by functional domain.

---

## 🏛️ DOMAIN 1: MULTI-CLI FLEET, SUBAGENTS & CONTEXT GOVERNANCE

### [C-11] Model Tiering Matrix (استراتيجية النماذج والترقية التلقائية للنماذج الأحدث)
- **Flash (`Gemini 3.8 Flash High` / `OpenCode Muse Spark` / `GLM 5.3 Flash`):** Routine scripting, terminals, boilerplate, content, data layers, Git commands.
- **Pro (`Claude Sonnet 5.5` / `OpenCode DeepSeek V4`):** General programming, feature development, component refactoring.
- **Thinking & Deep Algorithmic (`Claude Opus 5.5 / Opus Max` & `Codex GPT-6.1 Soul`):** Architecture planning, Devil's Advocate audits, invariant verification, deep algorithmic optimization, and heavy mathematical parsers.
- **معيار الترقية التلقائية لنماذج البرمجة الأحدث (Frontier Coding Model Auto-Binding):**
  - عند صدور أجيال ونماذج ذكاء اصطناعي أحدث متخصصة في البرمجة من أي من المطورين (OpenAI / Codex, Anthropic / Claude Code, Google / Antigravity, OpenCode Go):
    1. يرتبط وكيل Codex CLI تلقائياً بالجيل الأحدث لنموذج **GPT-6.1 Soul** ونماذج الكود الرائدة من OpenAI.
    2. يرتبط Claude Code CLI بالجيل الأحدث لـ Claude Opus/Sonnet (مع احتكار الفحص والاختبارات الحصري [C-21]).
    3. يرتبط Antigravity CLI بالجيل الأحدث لـ Gemini Flash High / Gemini Pro للكود والواجهات.
    4. يرتبط OpenCode CLI بأحدث نماذج الأكواد السريعة (GLM / DeepSeek / Muse Spark).
    5. تنفذ الترقية تلقائياً دون الإخلال بحدود الأدوار المعمارية أو احتكار الاختبارات.

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
  - `Codex CLI`: Algorithmic translations, complex data transforms, heavy refactoring, and math engines. Model: **GPT-6.1 Soul** (`gpt-6.1-soul` / `gpt-6-astra`) with continuous auto-binding to newest frontier coding releases.
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
### [C-83] Two-Machine Distributed Fleet Architecture (معمارية الأسطول الموزع الهجين)
- **Machine 1 (Master Cockpit):**
  - Claude Code CLI (Opus Max): قائد الأسطول والمحتكر الحصري لكافة الاختبارات والاعتماد [C-21].
  - Antigravity IDE (Gemini 3.8 Flash High): قمرة القيادة التفاعلية والاستشارات.
  - OpenCode Go: مزودو الطرفية محلياً (GLM 5.3 Flash, DeepSeek V4, Meta Muse Spark).
  - Fleet Cockpit: لوحة التحكم وبوابة الاعتماد البصري (:8088).
- **Dedicated Fleet Worker Accounts (Google AI Pro 4x Subscriptions):**
  - Worker 1 (agy CLI - ja7563110275@gmail.com - Google AI Pro): واجهات المستخدم والـ UI (Compose).
  - Worker 2 (agy CLI - ibrahimalkateb224@gmail.com - Google AI Pro): منطق الأعمال والـ Backend.
  - Worker 3 (agy CLI - ibrahimalkateb9220@gmail.com - Google AI Pro): السياق الضخم والأتمتة والتوثيق.
  - Worker 4 (agy CLI - ayahowiedy123@gmail.com - Google AI Pro): المهام الثانوية والتحويل الاحتياطي للكوتا (Failover).
  - Worker Zen (OpenCode CLI - OpenCode Zen - GLM 5.3 Flash): قواعد البيانات والمخططات.
- **Local Profile Isolation:** عزل الجلسات محلياً في `D:\FleetProfiles\Worker_{1..4}` عبر `--user-data-dir` لحماية قرص C: وتطبيق [C-82].
- **Testing Prohibition:** يُحظر تماماً تشغيل أي اختبارات آلية على عمال agy أو OpenCode؛ احتكار الاختبارات لـ Claude Code CLI حصراً [C-21].

### [C-84] Mandatory Claude Code Pre-Dispatch Targeting Gate (بوابة اعتماد التوجيه لـ Claude Code)
- في الخطاف 30 (Hook 30) وكافة أدوات التوجيه البرمجي لمساحات العمل:
  - يُحظر تماماً إرسال أو مزامنة أي توجيه مع اللابتوب الثاني (Machine 2) إلا بعد تدقيق ومراجعة معلمات التوجيه من قِبل Claude Code CLI.
  - يُشترط صدور ختم الاعتماد المعماري الصريح: `[APPROVED FOR REMOTE DISPATCH]` وتوثيقه في `fleet_target.json`.
  - في حال غياب الاعتماد، تظل حالة التوجيه `DRAFT_PENDING_CLAUDE_APPROVAL` وتُقفل قنوات الإرسال الشبكي للابتوب الثاني برمجياً.

### [C-89] Claude Code Hooks Directory Junction & CJS Module Isolation Standard (معيار وصلات الربط وعزل CommonJS لخطافات Claude Code)
- لضمان الامتثال الصارم للقيد [C-82] وحماية القرص C: من الكتابة العشوائية:
  1. **وصلة الربط الإلزامية (Directory Junction):** يتم توجيه مسار خطافات كلود `C:\Users\<User>\.claude\hooks` حصرياً عبر وصلة ربط مجلدات صلبة (`cmd /c mklink /J "C:\Users\<User>\.claude\hooks" "<Secondary_Drive>\tools\claude_hooks"`) لحفظ الأكواد تحت تتبع Git في القرص الثانوي.
  2. **عزل بيئة الحزم (CJS Isolation):** في مشاريع Node التي تفرض نمط `"type": "module"` في جذر المشروع، يجب إنشاء ملف `package.json` مستقل ومخصص داخل مجلد `tools/claude_hooks` بمحتوى `{"type": "commonjs"}` لعزل الخطافات ومنع انهيار فحوصات `node:test`.

### [C-91] Multi-CLI Headless Delegation via Stdin Streaming Standard (معيار التفويض الطرفي غير التفاعلي عبر تدفق stdin)
- عند تفويض مهام برمجية أو استشارات معمارية مؤتمتة لـ Claude Code CLI أو OpenCode عبر سكربتات بايثون أو الطرفية:
  1. **حظر تمرير البرومبت كوسيط سطر أوامر نصوصي عارٍ:** يُمنع تمرير البرومبتات الطويلة والمتعددة الأسطر كمعامل مباشر `claude -p "..."` في Windows CMD لتجنب مشاكل اقتطاع علامات التنصيص وحذف الأسطر.
  2. **التمرير الإلزامي عبر تدفق stdin:** يتم استدعاء الأداة عبر نمط التدفق `cmd.exe /c "claude -p -"` مع ضخ البرومبت عبر `stdin=subprocess.PIPE` بترميز UTF-8 حصراً.

### [C-93] Anthropic Frontier Safeguard Downgrade & Cyber Terminology Standard (معيار التعامل مع حراسة Anthropic والتراجع السيبراني)
- **آلية التراجع السيبراني (Safeguard Downgrade):** عند التعامل مع أدوات الهندسة العكسية، تفكيك الملفات الثنائية (APKs, Binaries)، ومحركات التحليل (Ghidra, JADX, REA)، تطلق حراسة Anthropic لنموذج Opus 5.5 وسم `[ cyber ]` وتقوم بالتحويل التلقائي الآمن إلى `Opus 4.8`.
- **بروتوكول الاستجابة:**
  1. يُحظر على الوكلاء الذعر أو إيقاف الجلسة أو إعادة طرح الأسئلة عند حدوث التراجع؛ نموذج Opus 4.8 مؤهل تماماً لإكمال المهام المعمارية والتطويرية بأعلى دقة.
  2. توثيق كافة إجراءات الهندسة العكسية كإجراءات تشغيلية قياسية (SOP) داخل `Brain/03_Jobs/Reverse_Engineering_SOP.md` دون إقحام المصطلحات الحساسة في محادثات التوجيه اليومية لتفادي إثارة الفلاتر دون حاجة.

### [C-95] Remote Mobile Fleet Autonomous Handoff Standard (معيار الإعفاء الطرفي للتحكم عن بُعد)
- **حظر تكليف المستخدم على الهاتف بأوامر الطرفية:** عند تشغيل الجلسات بنمط التحكم عن بُعد (Claude Remote Control) ووجود المستخدم خارج بيئة سطح المكتب:
  1. يُحظر تماماً توجيه المستخدم لتنفيذ أوامر طرفية يدوية (`git push`, `git commit`, `restart`, `adb`).
  2. يتولى وكيل محرر Antigravity المحلي في الخلفية فوراً وبشكل ذاتي تنفيذ كافة عمليات الـ Git، تشغيل الاختبارات المؤتمتة، والتحقق الميداني بمجرد جاهزية المخرجات، مع إخطار المستخدم بالنتيجة النهائية بأسلوب مقتضب ومباشر.

### [C-96] Hybrid Autonomous E2E Browser Testing & Claude Sign-Off Pipeline (منظومة الفحص التفاعلي المزدوج والاعتماد المعماري)
- **فصل مسار التنفيذ الميداني عن مسار الاعتماد (Separation of Test Execution from Architectural Sign-Off):**
  1. **مسار التنفيذ التفاعلي الشاق (Execution Lane):** يتولى الثنائي `Antigravity CLI (agcli / Gemini 3.8 Flash High)` مع مهارة `webapp-qa-tester` (المدعومة بـ Playwright و Chrome DevTools) كافة عمليات الفحص التفاعلي للأزرار والحقول في المتصفح الحقيقي (نقر، تعبئة، محاكاة أحداث، لقطات شاشة، وفحص DOM و CSS LTR)، بتكلفة $0.00 وبلا أي استنزاف لكوتا كلود.
  2. **توليد مصفوفة الأدلة (Execution Evidence & Matrix Generation):** يقوم الثنائي بتجميع كافة الأدلة وسجلات النجاح الخضراء 100% في تقرير مصفوفة مدمج ومنظم.
  3. **بوابة الاعتماد المعماري الصارم (Claude Code Architectural Sign-Off Gate):** يُرفع تقرير الأدلة المنجزة إلى `Claude Code CLI (Opus Max)` في استدعاء خفيف وموجز (< 5,000 توكنز) لإجراء التدقيق المعماري النهائي (Devil's Advocate Audit) وإصدار الختم الرسمي `[APPROVED]`، مما يوفر أكثر من 90% من كوتا كلود للمهام المعمارية والقرارات التصميمية الكبرى.

### [C-98] WSL Stdin / TTY Decoupling for Windows CLI Harnesses (عزل قنوات الدخل لأدوات ويندوز من WSL)
- **عزل طرفية الدخل (Stdin Isolation):** عند تشغيل أوامر وأدوات أسطول الـ CLIs المثبتة في بيئة ويندوز (مثل `opencode`, `claude`, `codex`) من داخل حاويات أو طرفيات Linux / WSL بنمط غير تفاعلي (headless):
  1. **إلزامية إعادة توجيه الدخل:** يجب دائماً وبشكل إلزامي عزل طرفية الدخل عبر `< /dev/null` أو تمرير المحتوى الموجه عبر الملفات والأنابيب (`cat prompt.txt | ...`).
  2. **حظر الاستدعاء المباشر دون توجيه:** يُمنع استدعاء الأوامر دون إعادة توجيه الدخل لتفادي تجمد العمليات في انتظار موجه تفاعلي (Interactive TTY Hang).

### [C-100] Multi-CLI Fleet Teamwork Preview Standard (معيار العمل الجماعي لأسطول الـ CLIs - الخطاف 31)
- **فلسفة التوزيع بدلاً من الاستنزاف الأحادي:**
  - تحويل وتوزيع نمط `teamwork-preview` الأحادي في محرر Antigravity (المقيد بالقيد [C-77]) إلى أسطول الـ CLIs الموزع لحماية نافذة السياق وترشيد الكوتا بنسبة 85%+.
  - **توزيع الأدوار:**
    1. **Master Orchestrator:** `Claude Code CLI (Opus Max)` بالتعاون مع `Antigravity IDE` (Cockpit).
    2. **Parallel Explorers:** `Antigravity CLI (agcli)` للواجهات والأصول، `OpenCode GLM 5.3 Flash` لقواعد البيانات والخلفية، `OpenCode Spark` لمنطق النطاق والتوافق.
    3. **Implementation Workers:** `OpenCode GLM 5.3 Flash` (Worker A - ماكس 4 ملفات بالدفعة [C-78])، `Cline CLI` و `agcli` (Worker B - واجهات وسقالات).
    4. **Adversarial Reviewers & Challengers:** `OpenCode DeepSeek V4` (مراجعة معمارية وتحدي البيانات)، `agcli` مع `webapp-qa-tester` (فحص المتصفح التفاعلي المجاني [C-96]).
    5. **Exclusive Testing Monopoly & Forensic Auditor:** `Claude Code CLI (Opus Max)` حصرياً لتشغيل الاختبارات وإصدار الاعتماد `[APPROVED]`.
- **قاعدة نقاء مساحة العمل (Metadata-Only Rule):**
  - يُحظر تماماً وضع أي أكواد مصدرية أو ملفات بيانات أو نسخ ثنائية داخل `.agents/fleet_teamwork/`؛ تقتصر هذه المجلدات حصرياً على سجلات المتابعة والأدلة الوصفية (`DISPATCH.md`, `BRIEFING.md`, `handoff.md`, `GATE_STATUS.md`).

### [C-102] Teamwork Physical Artifact Evidence Standard (معيار أدلة التسليم الفيزيائية لخطاف 31)
- **إلزامية ملفات الأدلة الحقيقية:** في خطاف 31 (`fleet_teamwork_orchestrator.py`)، يُحظر تماماً الاكتفاء بالإشارة التلقائية إلى `handoff.md` كقيمة شكلية في سجل البوابات دون وجود أصل فيزيائي.
- **التوثيق الإلزامي لكل دور:** يجب على كل وكيل مشغل لدور في المراحل M1 (المسح والاستكشاف)، M2 (التنفيذ والتطوير)، و M3 (المراجعة والتحدي) كتابة ملف `handoff.md` فعلي وموثق داخل مجلد دوره المخصص (مثل `.agents/fleet_teamwork/<role>/handoff.md`) يوضح بدقة ما تم إنجازه أو مراجعته قبل تسجيل أحكام `DONE` أو `CLEAN`.

### [C-103] Two-Stage Phased Dispatch & Anti-Hollow Build Standard (معيار التفويض ثنائي الأطوار ومنع البناء الهيكلي الوهمي في الوضع الذاتي Turbo)
- **حظر البناء الهيكلي الوهمي (Anti-Hollow Build Mandate):** عند تكليف أي وكيل بمهمة برمجية تتطلب خدمات أو تكاملات خارجية يملك مفاتيحها المستخدم حصراً (مثل: Discord Bots, Sentry DSN, GitHub Tokens, Stripe, Temporal Cloud, Firebase):
  1. **الطور الأول (Autonomous Scaffolding in Background - Turbo Safe):** يُلزم الوكيل بقصر عمله الذاتي في الخلفية على: التصميم المعماري، كتابة السقالات والكود، بناء سير العمل، توليد ملف المتغيرات النموذجية `.env.example`، وإعداد دليل إعدادات بشرية دقيق (`SETUP_CHECKLIST.md`).
  2. **بوابة التوقف الإجبارية (Mandatory Hard Stop Gate):** يُحظر تماماً على الوكيل محاكاة أو تزييف المفاتيح الخارجية أو إعلان انتهاء المهمة زيفاً دون اكتمالها. يجب أن يتوقف فور الانتهاء من السقالات البرمجية، ويسلم تقرير النواقص البشرية للمطور.
  3. **الطور الثاني (Interactive Handshake & Shadow Run):** لا يبدأ الوكيل في تفعيل أو تشغيل أو اختبار التكاملات الحية إلا بعد تأكيد المستخدم لإدخال المفاتيح الحقيقية في بيئة التشغيل، مع البدء دائماً بوضع المراقبة (Shadow Mode).

### [C-104] Dual Dynamic Web Apps Synchronization & Cross-Platform Path Normalization Standard (معيار المزامنة المزدوجة لتطبيقات الويب وتطبيع المسارات العابرة للبيئات)
- **المزامنة المتزامنة لتطبيقي الويب (Dual Web Apps Invariant):**
  - في كافة عمليات تحديث أو إضافة أي خطاف جديد (من 1 إلى 31+):
    1. يُلزم محرك التحديث (`update_html.py`) باستهداف وتحديث كلاً من **مكتبة الخطافات (`03_Dynamic_Hooks_Library`)** و **مكتبة الأوامر (`03_Dynamic_Prompt_Library`)** في آن واحد.
    2. يُحظر تماماً تحديث تطبيق وتجاهل الآخر، مع شمول النسخ الإنتاجية (`-production`) والنسخ التشغيلية للنظام (`C:\Users\<User>\.gemini\config\tools\`).
- **التطبيع الإلزامي للمسارات العابرة للبيئات (Bidirectional Path Normalization):**
  - عند كتابة سكربتات أتمتة تعمل بين بيئات هجينة (Windows PowerShell / WSL2 / Linux Containers):
    1. يجب دائماً تضمين دالة تطبيع مسارات معيارية (`resolve_path()`) تترجم المسارات تلقائياً بين نمط أحرف المحركات في ويندوز (`F:\...`) ونمط نقاط التثبيت في لينكس (`/mnt/f/...`).
    2. يُمنع الاعتماد على مسارات صلبة أحادية النظام تفشل فور تشغيل السكربت من داخل بيئة مغايرة.
- **ثنائية التوليد (Workbook & HTML Parity):**
  - يُلزم استدعاء أداة الإكسيل `convert_hooks_to_sheets.py` بالتوازي مع أداة الويب `update_html.py` لضمان التطابق الحسابي التام بنسبة 100% بين ملفات `HOOKS_GUIDE.md` و `HOOKS_GUIDE.xlsx` وصفحات `index.html`.

### [C-105] Design Resources & Modern UI/UX Architecture Standard (معيار مستودع مراجع التصميم وهندسة الواجهات - Hook 31)
- **اعتماد مصفوفة المنصات الـ 8 كمرجع هندسي إلزامي:**
  - في كافة مهام بناء وتطوير الواجهات لركائز الوكالة الثلاث، تُعتمد المنصات الـ 8 الموثقة في `docs/DESIGN_RESOURCES_VAULT.md`:
    1. `jiro.build`: هندسة برومبتات الذكاء الاصطناعي لتوليد الواجهات (1,200+ Prompts).
    2. `navbar.gallery`: أشرطة التنقل العلوية والجزر العائمة (Floating Pills).
    3. `footer.design`: تذييلات الصفحات الموثوقة خماسية الأعمدة لدعم الثقة والأرشفة.
    4. `cta.gallery`: دعوات الإجراء عالية التحويل (CRO) وأشرطة الهواتف العائمة.
    5. `404s.design`: صفحات الخطأ التفاعلية ومسارات استعادة الزوار وتفادي الارتداد.
    6. `unsection.com`: الكتل النمطية الجاهزة (الهيرو، الأسعار، الشركاء، الأسئلة الشائعة).
    7. `bentogrids.com`: شبكات البنتو غير المتماثلة للوحات التحكم والميزات.
    8. `60fps.design`: التفاعلات الدقيقة والحركات الفيزيائية السلسة.
- **الضوابط الصارمة للواجهات العربية وحركات الـ 60fps:**
  1. **انسيابية الـ RTL الطبيعية:** تبدأ العين العربية القراءة من الركن العلوي الأيمن؛ لذا توضع أهم بطاقة في شبكة البنتو في أعلى اليمين (`top-right`)، وتعكس أزرار الـ CTA اتجاه الأسهم، ويوضع الشعار يميناً وأزرار الإجراء يساراً.
  2. **مساحات لمس الهواتف:** أشرطة الـ CTA العائمة وأزرار التنقل يجب ألا تقل مساحة اللمس فيها عن `48x48dp` لمنع النقرات العشوائية الخاطئة.
  3. **ميزانية حركة الـ 60fps:** حظر تحريك خصائص الحجم والتخطيط (`width/height/margin`)؛ وتقتصر الحركات حصرياً على معالج الرسوميات (`transform: translate3d/scale` و `opacity`) مع احترام خيار تقليل الحركة (`prefers-reduced-motion`).
  4. **الامتثال الصارم للقاعدتين 50 و 51:** اعتماد الأرقام الإنجليزية (`0-9`) حصرياً، وتجريد القوالب البرمجية من أي بيانات عملاء خاصة.

### [C-106] Google Business Profile & Authenticated Browser Automation Standard (معيار أتمتة وإدارة ملفات النشاط التجاري)
- **اختراق الأطر المضمنة عبر CDP (Cross-Origin Iframe Penetration):**
  1. تعمل واجهات إدارة الأنشطة التجارية لجوجل (Google NMX) داخل أطر مضمنة (Iframes) تابعة لنطاقات فرعية؛ يُحظر محاولة قراءتها أو التفاعل معها عبر JavaScript DOM المباشر لتفادي أخطاء `SecurityError: Blocked cross-origin frame`.
  2. يُلزم الاعتماد حصرياً على أدوات `Chrome DevTools MCP` أو `webapp-qa-tester` المرتبطة بمنفذ تصحيح كروم (`9222`)، حيث تخترق أدوات `take_snapshot`, `fill`, `click` شجرة الوصولية (a11y tree) لكافة الأطر دون قيود أمنية.
- **تثبيت حقول Material Design:**
  - تتطلب الحقول النصية والقوائم إطلاق حدث `Enter` أو `blur` ومطابقة حالة التركيز قبل النقر على زر "حفظ" لضمان تثبيت الإرسال الشبكي.
- **بروتوكول التهدئة الإلزامي بعد التعديل (Post-Edit Cooling Window):**
  - عند حفظ تعديلات حيوية (الهواتف، ساعات العمل، الفئات)، تدخل البيانات في مراجعة آلية لدى جوجل (10 إلى 90 دقيقة).
  - يُحظر تماماً إجراء أي نقرات أو تعديلات إضافية متلاحقة خلال هذه النافذة لتفادي تعليق الحساب (Suspension) أو إثارة شرط إعادة التحقق بالفيديو (Video Re-verification). يتم الفحص عبر إعادة قراءة لوحة المعرفة العامة فقط.

### [C-107] Autovem Brain Vault Pilot Governance & Arabic Note Sizing Standard (معيار حوكمة ذاكرة Brain وقياس المحتوى العربي)
- **معيار تقييم الملاحظات الكبيرة (Line Count vs KB Rule):**
  - في مستودع المعرفة والذاكرة (`Brain/` و `Autovem_brain`)، يتم تقييم تضخم الملاحظات (`Oversize Notes`) **حصرياً بعدد الأسطر ($\le 300$ سطر)** وليس بالحجم بالكيلوبايت.
  - تعليل: ترميز UTF-8 للمحارف العربية يستهلك بايتين لكل حرف، مما يضخم الحجم بالبايت رغم قصر الملاحظة وسهولة استيعابها في نافذة الانتباه.
  - عند تجاوز أي ملف حاجز 300 سطر، يُلزم تفكيكه فوراً إلى ملف فهرس رئيسي ($\le 100$ سطر) ومذكرات موضوعية فرعية ($\le 250$ سطر لكل مذكرة) مع ترويسة معيارية كاملة (`type`, `owner`, `updated`, `summary`).
- **إلزامية تسجيل جلسات الـ PILOT لكافة المنسقين (PILOT Session Logging):**
  - يُلزم كافة منسقي الأسطول (`Claude Code CLI`, `Antigravity IDE`, `agcli`, `OpenCode CLI`) بتسجيل سطر واحد بصيغة الـ PILOT تحت ترويسة `## Pilot` في ملف اليومية النشط (`Brain/06_Daily/YYYY-MM-DD.md`) عند اختتام كل جلسة عمل:
    `- PILOT HH:MM [Agent] job=<job-or-none> boot_files=<n> boot_kb=<n> hit=<1|0> misses=<n> note=<short text>`
  - حظر إنهاء الجلسات دون توثيق هذا السطر لضمان احتساب مقاييس `boot_kb <= 25` و `Hit rate >= 80%` لبوابة اليوم 30 (2026-11-12).
- **تحصين محارف الألوان وروابط الويكي (Wiki-Link Sanitization):**
  - يُحظر كتابة محارف ألوان الطرفية ANSI بصيغة `^[[` داخل نصوص ومقتطفات Markdown لتفادي إثارة إنذارات الروابط المكسورة الخاطئة؛ وتُكتب دائماً بصيغة `\x1b[` أو `ESC[`.

### [C-108] Antigravity CLI Quad-Fleet Global Shim & Worker Routing Standard (معيار موجهات الأوامر وتوجيه عمال أسطول Antigravity الأربعة)
- **موجهات الأوامر الموحدة (Global Shim Invariant):**
  - يتم استدعاء أداة Antigravity CLI حصرياً عبر الموجهات الموحدة في مسار NPM العام (`C:\Users\Kt\AppData\Roaming\npm` المشترك بين Windows و WSL):
    1. `agcli` و `agy` لبيئة Linux/WSL مع تطبيق عزل طرفية الدخل الإلزامي (`< /dev/null`) لمنع تجمد الطرفية [C-98].
    2. `agcli.cmd` و `agy.cmd` لبيئة Windows CMD / PowerShell.
  - ترتبط الموجهات مباشرة بالملف الثنائي `D:\FleetProfiles\bin\agy.exe` (v1.4.0) وعزل البروفايلات في `D:\FleetProfiles\Agy_{1..4}`.
- **التوجيه التخصصي للحسابات الأربعة (Role-Based Worker Routing):**
  - في كافة مهام الأسطول والخطاف 31، يتم توجيه المهام إلى الحساب المناسب عبر وسيط `--worker <1..4>` أو `FLEET_WORKER`:
    * **Worker 1 (`ja7563110275@gmail.com`):** واجهات المستخدم والـ UI (Compose & Web).
    * **Worker 2 (`ibrahimalkateb224@gmail.com`):** منطق الأعمال والـ Backend وقواعد البيانات.
    * **Worker 3 (`ibrahimalkateb9220@gmail.com`):** السياق الضخم (2M Tokens)، الوصولية، والتوثيق الشامل.
    * **Worker 4 (`ayahowiedy123@gmail.com`):** المهام المتوازية والتحويل الاحتياطي للكوتا.
- **حظر الاختبارات:** يُحظر تماماً تشغيل أي اختبارات آلية على عمال agy؛ احتكار الفحص والاختبار لـ Claude Code CLI حصراً [C-21].

### [C-109] Loka Context Orchestrator & Surgical Build OS Standard (معيار حوكمة وهندسة سياق البرمجة الجراحية - Hook 32)
- **حوكمة السياق داخل مساحة العمل النشطة (In-Workspace Context Governance):**
  - في كافة مهام البرمجة الجراحية وإصلاح الأعطال والمهام المستقلة الموجهة لمشاريع العملاء (مثل EduTrack Pro في `Clients/03_GHERAS_Center/`):
    1. **حظر فتح مستودع Loka كمشروع مستقل:** يُحظر تماماً فتح مستودع المهارات والقوالب (`loka-context-harness`) كمساحة عمل مستقلة في المحرر لتعديل مشاريع أخرى؛ لتفادي عمى السياق (Context Blindness) وتلف مسارات Git النسبية.
    2. **الاستدعاء كوسيط حوكمة (Middleware Invariant):** يُستدعى وكيل `[loka-orchestrator]` وإطار Loka كطبقة وسيطة داخل مساحة عمل المشروع النشط (`Autovemtech`)، مع استدعاء المهارات المنشورة في `.agents/skills/`.
- **التوجيه الذاتي الإلزامي للمسار المناسب (Autonomous Routing Engine):**
  - يقوم الوكيل تلقائياً بربط طبيعة المهمة بالمسار المطابق:
    * **Loka ACE / ACE Lite:** لصياغة الأفكار العامة، أبحاث التقصي المعمق (Deep Research)، ومواصفات المنتجات.
    * **Loka V10 / V10 Lite:** للتخطيط المعماري، تدقيق مساحات التهديد، وفحص الأنظمة دون لمس الكود.
    * **Loka V11 Lite (Build OS):** لتطوير الميزات وإصلاح العيوب بنمط التعديل الجراحي الأدنى (Surgical Fix Mode / Minimal Blast Radius).
    * **Loka V12 Supervised:** لجلسات البرمجة التفاعلية التي تتطلب بوابات مراجعة بشرية مرحلية (Checkpoints).
    * **Loka V12 Autonomous Edition:** للمهام المفوّضة ذات الصلاحيات المستقلة وشروط التوقف الإجبارية (Stop Conditions).
- **بروتوكول ملفات الحالة الثلاثية والقواعد الـ 15:**
  - إلزامية إنشاء وقفل مواصفات `SPEC.md` في مجلد الميزة قبل كتابة أي كود، وتتبع الحالة اللحظية في `STATE.md`، وتسجيل القرارات في `MEMORY.md`.
  - تطبيق القواعد الـ 15 (منع اختلاق الأدوات R1، عزل الأسرار R2، قراءة الملفات العامة قبل التعديل R4، أصغر تعديل آمن R7، وحظر خفض المعايير R8).
- **التكامل مع أسطول الـ CLIs واحتكار الاختبارات [C-21]:**
  - توجيه المنفذين (OpenCode CLI للبناء وقواعد البيانات، و agcli للواجهات)، مع حظر إعلان الاكتمال دون توليد أدلة مادية مثبتة، وتفويض الفحص والاعتماد المعماري النهائي حصرياً لـ `Claude Code CLI (Opus Max)`.

### [C-114] Codex CLI Sandbox Parity & PATH Registration Standard (معيار تكامل وتطابق البيئة الرملية لـ Codex CLI وتسجيل مساره)
- في إدارة وتكامل أسطول الـ CLIs مع وكيل Codex:
  1. **تثبيت مسار البيئة الرملية في PATH:** يجب التأكد دائماً من تسجيل المجلد `C:\Users\<User>\.codex\.sandbox-bin` ضمن متغير البيئة `User PATH` لضمان استدعاء `codex` بسلاسة عبر كافة موجهات الأوامر.
  2. **التطابق الصارم لإصدارات الـ IPC:** يجب أن تتطابق أرقام إصدارات الملفات التنفيذية الأربعة للبيئة الرملية (`codex.exe`، `codex-code-mode-host.exe`، `codex-command-runner.exe`، `codex-windows-sandbox-setup.exe`) تطابقاً تاماً لمنع أخطاء قنوات الاتصال `failed to decode code-mode IPC frame`.
  3. **تحديث متغيرات الطرفيات النشطة:** عند إضافة مسارات جديدة لمتغير البيئة في بيئة PowerShell النشطة، يتم تزويد الطرفية بأمر الإنعاش اللحظي:
     `$env:Path = [Environment]::GetEnvironmentVariable("Path", "Machine") + ";" + [Environment]::GetEnvironmentVariable("Path", "User")`.

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

### [C-94] Autovemtech Master Tri-Pillar Scope Invariant (نطاق المستودع الشامل لركائز الوكالة الثلاث)
- **الهوية المعمارية للمستودع:** مستودع `Autovemtech` هو الحاضنة المركزية الشاملة لكافة أعمال وكالة Autovemtech بكافة ركائزها الثلاث:
  1. ركيزة التسويق والنمو (`Clients/` متضمنة كافة العملاء والقوالب).
  2. ركيزة حلول الذكاء الاصطناعي وأنظمة الوكلاء ومستودع المعرفة (`.agents`, `tools`, `Brain/`).
  3. ركيزة هندسة البرمجيات والتطبيقات (`Android`, `iOS`, `Web`).
- **حظر الانحصار في عميل واحد:** يُحظر تماماً على أي وكيل (Claude Code أو غيره) التعامل مع المستودع كنطاق محصور لمشروع عميل واحد (مثل EduTrack Pro). كافة تقارير، أدوات، وقوالب الوكالة هي أصول أصيلة في شجرة العمل ويجب معاملتها وفق حوكمة الوكالة الكاملة.

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


### [C-85] Jetpack Compose Dialog Focus Lifecycle & Digits Normalization Standard (معيار حوارات Compose وتطبيع الأرقام والوصولية)
- **أمان دورة حياة التركيز (Focus Requester Safety):** عند استدعاء `focusRequester.requestFocus()` داخل `LaunchedEffect` في حوارات `BasicAlertDialog` أو `Dialog`، يجب دائماً تغليفه داخل كتلة `try { focusRequester.requestFocus() } catch (_: Exception) {}` لحماية التطبيق من أخطاء عدم اكتمال تثبيت الحوار في شجرة التركيز (Composition Lifecycle).
- **التطبيع الإلزامي للأرقام (Dual-Alphabet Digit Normalization):** في كافة حقول البحث والإدخال الرقمية، يجب تمرير النص عبر دالة تطبيع معيارية `normalizeArabicDigits(input)` لتحويل الأرقام المشرقية (`٠-٩`) والفارسية (`۰-۹`) آلياً إلى الأرقام الغربية (`0-9`) وتجريد المحارف غير الرقمية قبل إجراء أي فحص نطاقي أو تحويل رقمي.
- **إعلانات الوصولية المتزامنة (A11y Sound & Visual Feedback):** يجب أن يرافق كل خطأ تحقق عددي عرض بصري واضح بالتوازي مع تنبيه صوتي صريح لقارئات الشاشة (TalkBack / TTS)، مع ضمان مساحات لمس للأزرار لا تقل عن 48dp.

### [C-86] Mobile E2E Testing Headless Degradation & Runtime Baseline (معيار تشغيل وعزل اختبارات E2E للأجهزة المحمولة)
- **العزل بين الفحص المعزول والفحص الميداني:** في خطوط الإنتاج والتحقق التلقائي، تُعتمد اختبارات الوحدة و Robolectric و Compose Unit Tests كبوابة جودة أولى تعمل بصورة مستقلة ومجردة (Headless) دون الحاجة لجهاز ADB متصل.
- **إدارة تبعية أجهزة ADB:** اختبارات المحاكاة الحية `@e2e-dev/mobile` تتطلب جهازاً فيزيائياً متصلاً أو محاكياً (AVD) مشغلاً بنشاط. عند رصد غياب أجهزة ADB، يجب الإبلاغ الشفاف عن سبب تعذر الإطلاق دون اعتبار ذلك عطلاً في الكود المصدري.
- **توافقية بيئة Node في أسطول ويندوز:** في بيئات Windows المثبت عليها Node 22.20، يتم اعتماد ضبط الحد الأدنى في `node-version.js` لمحرك e2e لتفادي حجب الفحص التلقائي.

### [C-92] Cross-Platform Android Non-Interactive Build & JDK Isolation Standard (معيار بناء أندرويد وتثبيت JDK في البيئات المشتركة)
- عند بناء حزم أندرويد (APK / AAB) من بيئات التطوير المزدوجة (WSL2 / Windows):
  1. **التثبيت الصريح لمسار JDK:** يجب تحديد مسار Adoptium JDK 17 المعتمد صراحة عبر `$env:JAVA_HOME` داخل سكربت البناء غير التفاعلي لمنع التداخل مع إصدارات JVM الأخرى.
  2. **السلاسل النصية الخام (Raw Strings):** يجب تمرير أوامر PowerShell من بايثون باستخدام السلاسل الخام `r'''...'''` لضمان عدم تلف المحارف الخاصة أو تلف محارف الخطوط المائلة العكسية.

### [C-115] Android Semantic VersionCode Formula & Google Play Release Lifecycle Standard (معيار الترقيم الدلالي لـ versionCode وإدارة مسودات جوجل بلاي)
- **حظر الترقيم التخميني العشوائي (Strict Ban on Manual Integer Guessing):**
  - يُحظر تماماً في كافة مشاريع Android و KMP كتابة `versionCode` كرقم صحيح يدوي اعتباطي (مثل 1, 2, 3...) عند تجهيز حزم الإنتاج لمتجر Google Play لتفادي خطأ الرفض الأبدي: `Version code X has already been used`.
- **اعتماد معادلة الترقيم الدلالي المعمارية (Mandatory Semantic VersionCode Formula):**
  - يتم اشتقاق `versionCode` حسابياً في `app/build.gradle.kts` من أرقام الإصدار المعيارية (SemVer) وفق المعادلة الإنتاجية:
    $$\text{versionCode} = (\text{Major} \times 1,000,000) + (\text{Minor} \times 10,000) + (\text{Patch} \times 100) + \text{Build}$$
  - **مثال للإصدار 1.1.0 Build 0:** ينتج تلقائياً `1010000`، وهو رقم يتجاوز حتماً أي أرقام قديمة محجوزة، ويوفر 100 خانة بناء فرعية (`verBuild: 0..99`) لنفس الإصدار عند الحاجة دون المساس بـ `versionName` المرئي للمستخدمين.
- **حوكمة مسودات كونسول جوجل بلاي (Play Console Draft Governance):**
  - عند ظهور زر `Create new release` باللون الرمادي (معطل)، فإن السبب الحتمي هو وجود مسودة نشطة سابقة (`Draft Release`).
  - يتم فك القفل بالدخول إلى تبويب `Releases` واختيار:
    1. `Edit release`: لاستبدال الحزمة المرفوضة بالحزمة الجديدة ومتابعة النشر.
    2. `Discard release`: لحذف المسودة العالقة وإعادة تفعيل زر `Create new release` فوراً.
- **طبيعة تحذير Debug Symbols:**
  - تحذير `This App Bundle contains native code, and you've not uploaded debug symbols` في مشاريع KMP يعتبر تحذيراً إرشادياً بحتاً (Advisory Warning) ولا يشكل خطأ مانعاً للنشر؛ يتم المتابعة مباشرة عبر `Save ➔ Start rollout to Production`.

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

### [C-87 / C-90] Playwright Headless Container Permissions & Arabic PDF Generation Standard (معيار تشغيل متصفح Playwright بالحاويات وتوليد PDF العربي)
- **مسار وتصاريح متصفحات Playwright في Docker:**
  - في كافة حاويات Docker التي تعمل بمستخدم معزول غير جذري (Non-root user مثل `edutrack` أو `node`):
    1. **المسار المشترك الإلزامي:** يجب دائماً ضبط متغير البيئة `ENV PLAYWRIGHT_BROWSERS_PATH=/opt/ms-playwright` في الـ Dockerfile وملف Compose.
    2. **منح التصاريح الشاملة:** يجب إنشاء المجلد ومنح صلاحيات القراءة والتنفيذ `RUN mkdir -p /opt/ms-playwright && playwright install --with-deps --only-shell chromium && chmod -R 755 /opt/ms-playwright` لمنع أخطاء الحجب الأمني (Permission Denied) التي تحدث عند التثبيت الافتراضي في `/root/.cache`.
- **حظر مكتبات الكانفاس لتوليد المستندات العربية (Canvas Banned for Arabic PDFs):**
  - يُحظر تماماً استخدام مكتبات الكانفاس للعميل (`html2canvas`, `html2pdf.js`) لتوليد المستندات الرسمية العربية؛ نظراً لفشلها الجذري في معالجة مسافات الكلمات والتشكيل (الالتصاق وعكس النقطتين).
  - يجب اعتماد التوليد السحابي المتجهي عبر متصفح خفي حقيقي (`Headless Chromium`) أو نافذة الطباعة الأصلية للمتصفح (`window.print()`).
- **نمط الروابط الموقعة لتجاوز اعتراض برامج التنزيل (IDM-Resilient Signed URLs):**
  - عند تنزيل ملفات الـ PDF عبر المتصفح، يُحظر إرسال جسم الملف الثنائي مباشرة عبر `fetch()` إذا كانت الواجهة تتطلب توثيق Bearer؛ لتفادي إنتاج ملفات 0-byte عند اعتراض برامج التنزيل المسرّعة (IDM).
  - يُعتمد نمط الرابط الموقوت (`POST /pdf/...` يعيد رابط تحميل موقع موقوت بصلاحية دقيقتين يتبعه المتصفح مباشرة كرابط ملف تنفيذي).
- **التضمين المحلي لخطوط الطباعة (Local Font Offline-First):**
  - يجب تضمين خطوط الطباعة (مثل Cairo WOFF2) محلياً داخل خادم الواجهة لمنع الاعتماد على خوادم الخطوط الخارجية وضمان إنتاج PDF متجهي عالي الدقة بحجم منخفض (~124KB).

### [C-88] psycopg3 SQL Percentage Wildcard Escaping Standard (معيار الهروب الصارم لمحرف النسبة المئوية في psycopg3)
- عند صياغة استعلامات SQL مع بايثون باستخدام مكتبة `psycopg` (الإصدار 3+):
  1. **إلزامية مضاعفة الرمز (`%%`):** يُحظر استخدام رمز النسبة المئوية المنفرد `%` داخل المعاملات النصية الثابتة في نصوص الاستعلامات (مثل شروط `LIKE '/api/v1/students/%'`)؛ لأن محرك psycopg3 يعتبره محرف استبدال ناقص ويفشل بخطأ خادم `500 ProgrammingError`.
  2. **الصيغة الإلزامية:** يجب كتابة الاستعلام دائماً بصيغة `LIKE '/api/v1/students/%%'` لضمان هروبه وتحويله لرمز `%` واحد أثناء التنفيذ بأمان تام.

### [C-97] WhatsApp Web Binary Attachment Sandbox & Public Capability URL Standard (معيار مشاركة الوثائق وتجاوز عزل واتساب ويب)
- **عزل متصفح الويب عن المرفقات الثنائية (WhatsApp Web Sandbox):**
  1. يُحظر الاعتماد على بروتوكولات روابط واتساب (`wa.me` أو `web.whatsapp.com/send`) أو افتراض قدرتها على إرفاق ملفات ثنائية (PDF / Documents) مباشرة، كما يُحظر الاعتماد على تمرير `text` مع `files` في `navigator.share` (يتجاهل تطبيق واتساب الملف ويرسل النص فقط).
  2. **نمط روابط القدرة العامة (Capability URL Endpoint):** عند مشاركة أي مستند رسمي (سند، فاتورة، تقرير) عبر واتساب من تطبيقات الويب، يجب إنشاء نقطة نهاية عامة ومؤمّنة برمز قدرة أو معرف فريد غير قابل للتخمين (مثل `/api/v1/public/.../pdf`) لخدمة ملف الـ PDF مباشرة للمستلم وتضمين الرابط داخل نص الرسالة.
  3. **التنزيل المتوازي والتحذير المرئي:** تفعيل التنزيل المتوازي للملف محلياً على جهاز المستخدم المرسل مع تنبيه مرئي (Toast) لإرشاده.

### [C-107] EduTrack Pro Production Deployment, Auth Token Key & Role Route Governance Standard (معيار النشر والتحقق ومفاتيح المصادقة لنظام EduTrack Pro)
- **قاعدة استخراج توكن المصادقة (Auth Token Key Invariant):**
  - استجابة تسجيل الدخول في `/api/v1/auth/login` تعيد حمولة JSON تحتوي على `token` وليس `access_token`:
    `{"token": "...", "user": {"id": "...", "role": "..."}}`.
  - في كافة سكربتات الاختبار المؤتمت والفحص الميداني، يُحظر استخراج `access_token`؛ بل يجب دائماً قراءة `token = res["token"]` لمنع إرسال `Bearer None` وانهيار الطلبات بخطأ `401 Unauthorized`.
- **كلمة مرور الحسابات التجريبية (Seeded Demo Accounts Password):**
  - الحسابات التجريبية الثلاثة المزروعة في `013_demo_accounts_seed.sql`:
    1. المعلم: `teacher.demo` (هاتف: `0550000001`)
    2. ولي الأمر: `guardian.demo` (هاتف: `0550000002`)
    3. الطالب: `student.demo` (هوية: `1100000001`)
  - تشترك جميعها بكلمة المرور الموحدة المشفرة بـ Argon2id: `Password123!` (يُمنع تخمين كلمات مرور مشتقة من اسم الدور).
- **حوكمة مسارات الميزات وصلاحيات الأدوار (Route & Scope Governance):**
  1. **المراسلة الفورية:** بادئة المسار هي `/threads` حصراً (وليس `/messaging/threads`).
  2. **أعذار الحضور:** استدعاء `GET /attendance/excuses` محصور حصراً بالإدارة والمشرفين (`manager`, `supervisor`)؛ وتجربة استدعائه بأدوار المعلمين أو أولياء الأمور تُرجع `403 Forbidden`. يقدم أولياء الأمور الأعذار حصراً عبر `POST /attendance/excuses`.
  3. **استلام الطلاب:** مسار جلب النداءات النشطة هو `GET /pickup/requests/active` (طلب `GET /pickup/requests` العاري يرجع `405 Method Not Allowed`).
- **بروتوكول النشر السحابي لهوستنجر (VPS Production Deploy Protocol):**
  - النشر الرسمي على خادم VPS (`187.55.226.225`) يتم حصراً عبر تشغيل:
    `bash deploy/push.sh root@187.55.226.225 gheras.autovem.tech -i <SSH_KEY>`
  - يضمن السكربت عدم استبدال ملف الأسرار `/opt/edutrack/deploy/.env`، ويطبق الهجرات الجديدة من `003` حتى `021` تلقائياً عبر `psql -f /docker-entrypoint-initdb.d/$m` بنمط Idempotent، ويعيد بناء حاوية `edutrack-api:2` ويحقق 65 جدولاً عاماً مع تفعيل فحص النسخ الاحتياطي اليومي.

### [C-110] Digital Storefront Zero-Trust & Atomic Fulfillment Standard (معيار المتاجر الرقمية الآمنة والتسليم الذري الفوري)
- **حدود الوصول الصفرية للعملاء (Zero-Trust Customer Boundary):**
  1. **حظر قراءة المتصفح لقاعدة البيانات:** يُحظر تماماً منح المتصفح صلاحية القراءة المباشرة لمجموعات الطلبات (`orders`) أو المخزون (`accounts_inventory`) في قواعد أمان Firestore (`allow read, write: if false;`).
  2. **كوكيز الجلسات المشفرة الإلزامية:** ترتبط كل عملية شراء بكوكي جلسة مشفر (`HttpOnly`, `SameSite=Strict`, `Secure`, `__Host-`) يحمل رمز وصول عشوائي (32 بايت)، وتتم المصادقة في الخادم حصراً بمقارنة تجزئة SHA-256 باستخدام `timingSafeEqual` لمنع هجمات التوقيت الجانبية.
  3. **استطلاع البيانات عبر مسارات الخادم المحمية:** يتم جلب حالة الطلب حصراً عبر `GET /api/orders/[id]` مع رأس `Cache-Control: private, no-store`، مع التوقف التلقائي عن الاستطلاع عند الحالات النهائية (`completed`, `rejected`, `expired`).
- **حماية إيصالات الحوالات البنكية وبث الذاكرة (Direct Buffer Receipt Streaming):**
  1. يُحظر حفظ إيصالات الحوالات البنكية في حاويات تخزين عامة أو روابط مكشوفة؛ وتُبث الصورة مباشرة من ذاكرة الخادم (`Buffer`) كطلب `multipart/form-data` إلى بوت تيليجرام الإداري.
  2. فحص البايتات السحرية للصور في الذاكرة (PNG `89 50 4E 47...` و JPEG `FF D8 FF`) وحظر أي ملفات تتجاوز 4 ميجابايت أو تحمل توقيعاً مجهولاً.
  3. التحقق الإلزامي من ترويسة أمان تيليجرام `x-telegram-bot-api-secret-token` عبر `timingSafeEqual`.
- **التوريد الحتمي ومنع البيع المزدوج (Deterministic Ingestion & Atomic FSM):**
  1. **معرّف التجزئة الحتمي:** يُخزن كل رابط أو مفتاح رقمي في Firestore بمعرّف مستند يساوي `SHA256(cleanUrl)` لمنع تكرار إدراجه عند إعادة توجيه الفواتير.
  2. **التسليم الذري المحصن:** يتم تخصيص المنتجات الرقمية واعتماد الطلبات حصراً داخل معاملة ذرية متزامنة (`adminDb.runTransaction`)؛ تُحظر إعادة تشغيل الاعتماد لمنع استهلاك حسابات إضافية، ويُحظر إلغاء أو استرجاع الطلبات المسلمة مسبقاً.
- **درع التوعية الأمنية للعميل (Account Login Shield):**
  - إلزامية عرض تنبيه أمني بارز في واجهة التسليم يوجه العميل بضرورة التأكد من تسجيل الدخول بحسابه الشخصي في المتصفح قبل النقر على رابط التفعيل لمنع استهلاك الرابط على حسابات العمل أو الحسابات المؤقتة.
- **المحاسبة الدقيقة ومطابقة الأرباح (Exact Decimal Precision):**
  - حساب التكاليف وهوامش الأرباح حصراً بمكتبة Python `Decimal` مع التقريب `ROUND_HALF_UP`، وتجنب حسابات الفاصلة العائمة (Float)، مع خروج سكربتات المزامنة برمز خطأ غير صفري عند حدوث أي إخفاق لمنع إعطاء نتائج مضللة.
- **انسيابية واجهة المستخدم وتفادي المسدودات (Hook 32 Navigation):**
  - توفير أزرار تراجع وإلغاء واضحة في كافة النوافذ المشروطة وشاشات الطلب لتجنب احتجاز المستخدم في شاشات مغلقة.

### [C-111] Local Web Tools Dynamic Port Allocation & Launcher Resilience Standard (معيار حوكمة تخصيص المنافذ الديناميكية والمشغلات المرنة لأدوات الويب المحلية)
- **حظر المنافذ الصلبة الشائعة (Strict Prohibition of Hardcoded Common Ports):**
  - في كافة أدوات الويب المحلية ولوحات التحكم الطرفية (FastAPI, Flask, Streamlit, Vite):
    1. يُحظر تماماً حجز المنفذ `8000` أو `8080` كمنفذ صلب وحيد بدون بدائل، لتفادي التعارض مع خدمات النظام أو الحاويات.
    2. يُلزم بدء التخصيص الافتراضي من نطاق المنافذ الآمنة غير الشائعة (`8555+` أو `8888+`).
- **إلزامية الكشف التلقائي عن المنافذ الشاغرة (Dynamic Auto-Port Probing):**
  - يجب دائماً تضمين دالة فحص اتصال في سكربت التشغيل الوسيط (`run.py` عبر `is_port_in_use()` و `find_available_port()`):
    - إذا كان المنفذ المطلوب محجوزاً أو معترضاً، ينتقل النظام تلقائياً وبشكل صامت للمنفذ المتاح التالي ويفتح المتصفح على الرابط المحدّث.
- **حظر الإقلاع الأعمى في الخلفية (Ban on Silent Background Launches):**
  - في ملفات الدُفعات (`start.bat` و `start.ps1`):
    - يُمنع إطلاق الخادم عبر `start /b` الصامت الذي يخفي انهيارات الإقلاع واستثناءات الاستيراد.
    - يُطلق الخادم في واجهة الطرفية المرئية عبر `python run.py` لعرض السجلات وتسهيل التشخيص السريع.
- **تحصين مسارات الأصول الثابتة (StaticFiles Guard):**
  - عند ربط مجلدات الأصول الثابتة في خوادم Starlette / FastAPI (`app.mount("/assets", ...)`):
    - يجب دائماً التحقق المسبق من وجود المجلد فيزيائياً على القرص (`if assets_dir.exists():`) قبل الربط لمنع إطلاق استثناء `RuntimeError` فجائي عند بدء التشغيل.
- **التطبيع الشامل لـ CORS للمنافذ الديناميكية:**
  - يجب ضبط إعدادات CORS باستخدام التعبير النمطي `allow_origin_regex=r"^http://(127\.0\.0\.1|localhost)(:\d+)?$"` لضمان قبول الاتصال من أي منفذ شاغر يتم تخصيصه تلقائياً دون إثارة أخطاء CORS.

### [C-112] Windows Unquoted Registry Paths & Headless Dialog Suppression Standard (معيار تحصين مسارات الريجستري ومنع النوافذ الحوارية المعطلة)
- عند تشغيل سكربتات الصيانة، إعداد نقاط استعادة النظام (`Checkpoint-Computer`)، أو إطلاق عمليات ويندوز المرتفعة الصلاحيات (UAC / Elevated Processes):
  1. **التطهير الإلزامي للمسارات غير المقتبسة:** يجب فحص مفاتيح التشغيل التلقائي (`HKCU` و `HKLM` تحت `CurrentVersion\Run` و `WOW6432Node`) والتأكد من تغليف أي مسار تنفيذي يحوي مسافات بعلامات اقتباس مزدوجة `""` لمنع خطأ `Windows cannot find 'C:\Program'`.
  2. **حظر النوافذ الحوارية المتزامنة:** عند استخدام `ShellExecuteExW` أو ما يعادلها في ويندوز، يجب دائماً تضمين العلم `SEE_MASK_FLAG_NO_UI (0x00000400)` في `sei.fMask` لمنع ويندوز نهائياً من إظهار أي نوافذ خطأ حوارية متزامنة تحظر استجابة الخوادم والوكلاء.

### [C-113] Heavy System Maintenance Non-Blocking Execution Standard (معيار العمليات الخلفية غير الحاجبة لصيانة الأقراص والنظام)
- في تطبيقات الويب والخوادم الخلفية غير التفاعلية:
  1. **حظر التنفيذ المتزامن لأوامر الصيانة الطويلة:** يُمنع تماماً تنفيذ الأوامر المستغرقة لعدة دقائق (مثل ضغط النظام `compact /compactos:always` أو إلغاء التجزئة `defrag`) بشكل متزامن داخل خيط طلب الـ HTTP.
  2. **التنفيذ الخلفي المستقل:** يجب تشغيل هذه العمليات في الخلفية عبر `Start-Process -WindowStyle Hidden` وإرجاع استجابة فورية للواجهة بحالة `Started` دون تجاوز 5 ثوانٍ، مع تسجيل العمليات ومتابعتها عبر سجلات تدقيق مستقلة.

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

### [C-81] Universal Windows File Explorer Discovery & Desktop Output Hub Standard (المعيار العالمي لإظهار وتصدير الملفات المولدة)
- **حظر المسارات المنعزلة (No Raw /mnt/ Paths):** يُحظر تماماً على أي وكيل في Antigravity أو الأسطول تقديم روابط أو مسارات ملفات مولدة تبدأ بـ `/mnt/...` دون ترجمتها الصريحة لمسار ويندوز المقابل (`D:\...` أو `F:\...`).
- **حظر التصدير للقرص C: وحفظ الملفات في مسارها الأصلي:** فور توليد أي ملف مخرج نهائي (PDF, DOCX, XLSX, HTML, Zip)، يُحظر منعاً باتاً نسخه أو تصديره إلى القرص `C:\`. يبقى الملف في مساره على قرص العمل (`D:\` أو `F:\`)، أو يُنسخ اختيارياً إلى مجمع مخرجات على القرص `D:\المستندات_المولدة\` فقط دون لمس القرص `C:`.
- **الإظهار الفوري المباشر (Direct In-Place Explorer Reveal):** يتم استدعاء `reveal "<file>"` لإظهار الملف في موضعه الأصلي على القرص `D:\` أو `F:\` مباشرة عبر مستكشف الملفات (Detached `explorer.exe /select,"<win_path>"`) دون إنشاء أي نسخ على `C:`.

### [C-82] Strict Windows Drive C: Zero-Write Quarantine Standard (معيار الحظر الصارم للكتابة على القرص C:)
- **حظر الكتابة التام (Zero-Write on Drive C:):** يُحظر منعاً باتاً على أي وكيل، سكربت، أو أداة في Antigravity أو الأسطول إنشاء، كتابة، أو نسخ أي ملفات نهائية أو مؤقتة على قرص النظام Windows Drive C: (`C:\` أو `/mnt/c/`).
- **حصر العمل على أقراص البيانات:** تقتصر كافة عمليات الإنشاء، التوليد، التصدير، وتحديث مكتبة الأوامر حصرياً على أقراص المشاريع والبيانات (`D:\` و `F:\`) أو داخل نظام ملفات لينكس المحلي (`/home/t/`).
- **تحديث مكتبة الأوامر:** تُحدّث النسخة الرسمية حصراً في `F:\AI PROJECTS\Claude+Antigravity\مكتبة الأوامر.xlsx`، ويُمنع المساس بأي نسخة على القرص `C:`.

### [C-99] Executive Non-Technical Client Reporting & Direct Public URL Standard (معيار التقارير التنفيذية للعملاء غير التقنيين)
- **حظر المصطلحات البرمجية تماماً:** عند إعداد أي وثيقة أو تقرير موجه لعميل إداري غير تقني، يُمنع ذكر أسماء الخوادم، أو قواعد البيانات، أو التقنيات الداخلية (Docker, Playwright, API, CRUD, JSON).
- **الهيكلة بالقيمة والأثر:** التركيز حصرياً على الأثر اليومي (توفير الوقت، راحة أولياء الأمور، الأمان المالي)، مع شرح كل ميزة في بطاقة بصرية تتضمن خطوات الاستخدام البسيطة (خطوتان فقط).
- **الاستضافة والنشر الفوري:** توليد التقرير بصيغة PDF ونشره فوراً في المسار العام لخادم الوكالة مع توفير رابط HTTPS مباشر يمكن فتحه فوراً من الجوال.

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

### [C-101] Master Workbook Git-Diff Integrity Standard (معيار فحص تكامل الإكسيل ضد Git HEAD)
- **حظر التحقق السطحي عند إضافة الأوامر:** عند إضافة أو تعديل أي موجهات في `مكتبة الأوامر.xlsx` (الخطاف 26)، يُمنع منعاً باتاً على مراجعي الجودة والمهندسين الحكم بسلامة المصنف (`sheet integrity intact`) بمجرد فحص الأسطر المضافة حديثاً فقط.
- **إلزامية الفحص الجنائي المقارن:** يجب برمجياً إجراء مقارنة تفريعية شاملة لكافة أوراق المصنف مقابل آخر التزام معتمد في Git عبر `git show HEAD:مكتبة الأوامر.xlsx` للتحقق من عدم حدوث أي انحراف (Drift) أو فقدان لأي صفوف أو أوامر تاريخية سابقة (`TOTAL MISSING ROWS: 0`)، واستعادة أي صفوف مفقودة برمجياً عبر `tools/restore_missing_prompts.py` قبل طلب الاعتماد المعماري M4.

### [C-65] Digital Concierge & Upfront Payment Standard (التيسير الرقمي والتأشيرات)
- Agency acts as technical facilitator for government portals (Nusuk / 96h Transit). Enforce 100% upfront payment via isolated digital card (STC Pay). B2B partnerships only.