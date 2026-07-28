<div dir="rtl">

# مخزن الذاكرة المركزي لنظام Autovem (MEMORY_STORE.md)

> [!IMPORTANT]
> هذا الملف هو السجل المركزي للذاكرة المستدامة. يُحدّث تلقائياً بواسطة وكيل `persistent-memory-engine` بعد كل خطاف نجاح أو أمر تسجيل يدوي.

---

## سجل الدروس المستفادة (Lessons Learned)

```yaml
- id: MEM-2026-07-21-001
  type: lesson
  timestamp: "2026-07-21T11:00:00+03:00"
  agents: [agent-optimizer, prompt-engineer]
  context: "تأسيس نظام Autovem وبناء 37 مهارة جديدة"
  content: "عند بناء مهارات جديدة، يجب دائماً اتباع هيكل YAML frontmatter موحد (name + description) والتأكد من تسجيل كل مهارة في sub_agents.yaml"
  tags: [skill-creation, standardization]
  status: active

- id: MEM-2026-07-21-002
  type: lesson
  timestamp: "2026-07-21T11:50:00+03:00"
  agents: [ai-geo-seo-optimizer]
  context: "تحسين محركات البحث التوليدية GEO"
  content: "تحسين المحتوى للظهور في محركات بحث الذكاء الاصطناعي (Perplexity, SearchGPT, Gemini) يتطلب زيادة كثافة الحقائق، الاعتماد على نبرة الخبير، وتقسيم المحتوى لفقرات مستقلة قابلة للاقتباس والاسترجاع عبر RAG."
  tags: [geo, ai-search, seo]
  status: active

- id: MEM-2026-07-21-003
  type: lesson
  timestamp: "2026-07-21T12:15:00+03:00"
  agents: [persistent-memory-engine, code-architect]
  context: "تحميل الملفات الكبيرة برمجياً عبر PowerShell"
  content: "عند أتمتة تحميل الملفات الكبيرة (مثل MSI) من GitHub، استخدام Invoke-WebRequest قد يؤدي إلى انقطاع الاتصال (Connection Forcibly Closed). الحل المستقر هو تفعيل TLS 1.2 واستخدام وحدة BITS (Start-BitsTransfer) لضمان التحميل المستقر ودعم الاستئناف."
  tags: [powershell, automation, bits-transfer, bug-fix]
  status: active

- id: MEM-2026-07-21-004
  type: bug-fix
  timestamp: "2026-07-21T16:50:00+03:00"
  agents: [devops-deployer, persistent-memory-engine]
  context: "نشر ملفات ماركداون ولوحة التحكم على GitHub Pages"
  content: "موقع GitHub Pages يبني المواقع بشكل افتراضي باستخدام Jekyll. هذا يمنع قراءة الملفات التي تحتوي على رموز معينة أو ملفات البنية. لمنع ذلك والحصول على استعراض مباشر وسليم لملفات Markdown الخام، يجب إنشاء ملف فارغ باسم `.nojekyll` في الجذر الرئيسي للمستودع."
  tags: [github-pages, jekyll, static-site]
  status: active

- id: MEM-2026-07-21-005
  type: bug-fix
  timestamp: "2026-07-21T16:50:00+03:00"
  agents: [debugger, persistent-memory-engine]
  context: "معالجة أخطاء الترميز للمخرجات العربية على ويندوز"
  content: "تشغيل سكربتات بايثون في الخلفية على ويندوز تطبع نصوصاً باللغة العربية أو رموزاً تعبيرية (Emojis) يسبب توقف السكربت بخطأ UnicodeEncodeError (ترميز cp1252). الحل هو فرض ترميز UTF-8 لمخرجات الكونسول بإضافة `sys.stdout = codecs.getwriter('utf-8')(sys.stdout.detach())` في بداية السكربت، مع إزالة الرموز غير المدعومة من الطباعة الافتراضية."
  tags: [windows, python, encoding, unicode, bug-fix]
  status: active

- id: MEM-2026-07-21-006
  type: bug-fix
  timestamp: "2026-07-21T16:50:00+03:00"
  agents: [devops-deployer, persistent-memory-engine]
  context: "حل تعارض عمليات البوت النشطة في الخلفية"
  content: "عند تشغيل بوت تليجرام باستخدام getUpdates، فإن أي نسخة قديمة نشطة في الخلفية ستسبب خطأ Conflict (رمز 409). لحلها يجب تحديد معرّف العملية (PID) لـ pythonw.exe أو python.exe وإنهاؤها قسراً. وإذا كانت العملية تعمل بصلاحيات مدير (Elevated) فيجب فتح Terminal كمسؤول (Run as Admin) لتنفيذ أمر `taskkill /F /PID <PID>` بنجاح."
  tags: [process-management, taskkill, telegram-bot, conflict, windows]
  status: active

- id: MEM-2026-07-25-001
  type: bug-fix
  timestamp: "2026-07-25T18:25:00+03:00"
  agents: [persistent-memory-engine, debugger]
  context: "تجمد طرفية PowerShell عند نقل الملفات (Move-Item) بسبب الترميز"
  content: "عند استخدام PowerShell `Move-Item` لنقل كميات كبيرة من الملفات، قد تتجمد العملية بالكامل في الخلفية (Hang) إذا تضمنت أسماء الملفات رموز إيموجي أو أحرف عربية غير متوافقة مع ترميز الطرفية الافتراضي مما يتسبب بانتظار استجابة المستخدم أو فشل صامت. الحل هو استخدام بايثون `shutil.move` مع تمرير `pass` داخل `except Exception` لتخطي أي أخطاء طباعة (Print/Encoding) وإنجاز النقل بنجاح وبسرعة هائلة."
  tags: [powershell, bug-fix, move-item, encoding, python, shutil]
  status: active

- id: MEM-2026-07-28-001
  type: lesson
  timestamp: "2026-07-28T20:00:00+03:00"
  agents: [github-talent-scout, performance-optimizer]
  context: "بيئة Google Colab (Python 3.12 / NumPy 2.0)"
  content: "تجنب ترقية المكتبات الأساسية (Zero-Over-Upgrade) لتفادي التعارض مع torchvision المثبت مسبقاً، استخدام الخيار --no-deps عند تثبيت النماذج. بالنسبة للنماذج المقيدة (Gated Models) يجب تسجيل الدخول عبر huggingface_hub.login قبل الاستدعاء، وتثبيت kornia كشرط مسبق لـ BiRefNet/RMBG-2.0. لتفادي VRAM OOM يجب تفريغ الذاكرة المؤقتة للـ GPU بعد كل صورة معالجة عبر torch.cuda.empty_cache() و gc.collect()."
  tags: [colab, pytorch, hf-hub, oom, memory-leak]
  status: active

- id: MEM-2026-07-28-002
  type: lesson
  timestamp: "2026-07-28T20:00:00+03:00"
  agents: [code-architect, persistent-memory-engine]
  context: "توليد الأكواد وحقن النصوص بـ Python re.sub"
  content: "عند دمج JSON أو كود يحتوي على مسافات أسطر \\n داخل ملفات HTML/JS باستخدام سكريبتات بايثون و re.sub، يجب تمرير النص كدالة مجهولة lambda _: new_content لمنع التعبير النمطي من ترجمة الـ Escape Sequences إلى أسطر فعلية تتسبب بخطأ صياغي SyntaxError في الجافاسكربت."
  tags: [python, regex, js-injection, syntax-error, bug-fix]
  status: active

- id: MEM-2026-07-28-003
  type: lesson
  timestamp: "2026-07-28T20:00:00+03:00"
  agents: [devops-deployer, backend-architect]
  context: "ربط Supabase و Paddle والتوزيع على سيرفر VPS"
  content: "في Supabase Auth يجب ضبط Site URL ودومين الـ VPS في Redirect URLs لمنع توجيه التأكيد إلى localhost. لتسهيل التطوير يمكن تعطيل Confirm Email من إعدادات الحسابات. لتفادي أخطاء Hydration في Next.js بسبب إضافات المتصفح يتم إضافة suppressHydrationWarning لوسم html. لنقل التحديثات بسرعة للسيرفر يفضل ضغط الملفات بـ tar -cf ونقلها بـ scp."
  tags: [supabase, paddle, nextjs, hydration, vps, deployment]
  status: active

- id: MEM-2026-07-28-004
  type: lesson
  timestamp: "2026-07-28T20:00:00+03:00"
  agents: [windows-c-drive-optimizer, security-auditor]
  context: "تنظيف وترتيب القرص C وحماية قواعد البيانات"
  content: "يُمنع تعديل صلاحيات أو مسح مجلدات قواعد البيانات أثناء التنظيف. تم إلزام سكريبتات التنظيف بوضع استثناءات صريحة (Exclusions) لمجلد C:\\Program Files\\Microsoft SQL Server وأي مجلدات قواعد بيانات لمنع فقدان خدمة SQLEXPRESS للصلاحيات (OS error 5 Access is denied)."
  tags: [windows, c-drive, sql-server, database-protection, exclusions]
  status: active

- id: MEM-2026-07-28-005
  type: lesson
  timestamp: "2026-07-28T20:00:00+03:00"
  agents: [offline-sync-db, debugger]
  context: "صيانة وحماية قواعد بيانات Primavera P6"
  content: "عند اختفاء مشاريع Primavera، السبب ليس حذف قواعد البيانات بل اختيار Database Alias خاطئ في شاشة الدخول أو نقل ملف الـ .db يدوياً. يمكن قراءة prmbootstrapV2.xml للوصول للمسار الأصلي واستخدام PowerShell Get-ChildItem -Filter *.db -Recurse لإيجاده. لحماية البيانات يتم استخدام سكريبت Backup-PrimaveraDB.ps1 لعمل نسخ احتياطية مؤرخة طردياً إلى GoogleDrive_Backups."
  tags: [primavera-p6, sqlite, backup, database-troubleshooting]
  status: active

- id: MEM-2026-07-28-006
  type: bug-fix
  timestamp: "2026-07-28T22:45:00+03:00"
  agents: [offline-sync-db, debugger]
  context: "حل مشكلة تعطل تسجيل الطالب المستقل/غير المتزامن"
  content: "في PocketBaseAuthClient، الاعتماد المسبق على تسجيل وهمي (Dummy Login) قبل تسجيل الطالب قد يوقف التسجيل كلياً إذا لم يكن الطالب متزامناً من قِبل المعلم مسبقاً. الحل وضع عملية Dummy Login في try-catch لاستكمال التسجيل كطالب جديد في حال الفشل."
  tags: [pocketbase, authentication, sync, fallback, bug-fix]
  status: active
```
```

---

## سجل القرارات المعمارية (Architecture Decisions)

```yaml
- id: ADR-2026-07-21-001
  type: decision
  timestamp: "2026-07-21T11:00:00+03:00"
  agents: [code-architect, agent-optimizer]
  context: "اختيار استراتيجية النماذج لنظام Autovem"
  content: "تم اعتماد النظام الثلاثي: flash للمحتوى والتسويق والمالية والقانون، pro للبرمجة والمراجعة والبناء، thinking للمعمارية والتخطيط والذاكرة"
  tags: [model-strategy, autovem-core]
  status: active

- id: ADR-2026-07-21-002
  type: decision
  timestamp: "2026-07-21T11:00:00+03:00"
  agents: [prompt-engineer]
  context: "استقلالية نظام Autovem"
  content: "حذف جميع الإشارات لأنظمة خارجية من ملفات المهارات والوكلاء. النظام مستقل تماماً ومحايد للنماذج (Model-Agnostic)"
  tags: [independence, cleanup]
  status: active

- id: ADR-2026-07-21-003
  type: decision
  timestamp: "2026-07-21T12:15:00+03:00"
  agents: [code-architect, persistent-memory-engine]
  context: "هيكلية الذاكرة للمشاريع المتعددة (Monorepo Workspace)"
  content: "لمنع تداخل السياق في بيئات العمل متعددة المشاريع، تم اعتماد هيكلية لامركزية للذاكرة عبر تهيئة مجلد `.agents` محلي داخل كل مشروع فرعي يحتوي على ملفات `MEMORY_STORE.md` و `PROJECT_CONTEXT.md` الخاصة به، مع ربطها جميعاً بأدلة الخطافات والمهارات المركزية."
  tags: [memory-architecture, monorepo, context-isolation]
  status: active

- id: ADR-2026-07-21-004
  type: decision
  timestamp: "2026-07-21T13:30:00+03:00"
  agents: [persistent-memory-engine, code-architect]
  context: "توافقية النظام مع المشاريع القديمة (Legacy Support)"
  content: "لتهيئة مشاريع قديمة تمتلك بالفعل مجلدات `.agents` وقواعد `AGENTS.md` منفصلة، نعتمد قاعدة (الإضافة فقط دون استبدال). نضيف ملفي `MEMORY_STORE.md` و `PROJECT_CONTEXT.md` حصرياً، ونبقي قواعد الخطافات القديمة للمشروع سليمة كما هي لضمان عدم تأثر السلوك السابق للمشروع القديم."
  tags: [legacy-support, backward-compatibility, architecture]
  status: active

- id: ADR-2026-07-21-005
  type: decision
  timestamp: "2026-07-21T16:50:00+03:00"
  agents: [persistent-memory-engine, code-architect]
  context: "نمط الإيقاظ الفردي لبوتات التطوير (One-Shot Polling Pattern)"
  content: "لتفعيل دورة اتصال ثنائية الاتجاه بين بوت تليجرام والمحرر (IDE) دون استهلاك المعالج ودون نوم الوكيل، تم اعتماد نمط (One-Shot Polling). يقوم السكربت `poll_once.py` بالدوران في الخلفية حتى يستقبل رسالة واحدة من المستخدم، يكتبها في `telegram_request.json` ثم يغلق نفسه (exit 0). هذا الإغلاق يوقظ بيئة التطوير تلقائياً لتقوم بمعالجة الطلب، إرسال الرد، ومن ثم إعادة تشغيل السكربت مجدداً."
  tags: [one-shot-polling, lifecycle, background-tasks, process-wakeup]
  status: active

- id: ADR-2026-07-25-001
  type: decision
  timestamp: "2026-07-25T16:40:00+03:00"
  agents: [code-architect, persistent-memory-engine]
  context: "إدارة التكلفة وتبني معمارية الوكيل المزدوج"
  content: "لترشيد استهلاك رصيد Prepaid للنماذج المتقدمة، تم اعتماد إطار العمل المزدوج (Dual-Agent). يعمل (Claude 5) كمستشار ومهندس يقرأ الملفات ويستكشف الأخطاء ويضع الخطط، بينما يعمل (Gemini Pro/Antigravity) كمنفذ لكتابة الكود والـ Refactoring بناءً على هذه الخطط."
  tags: [dual-agent, architecture, cost-optimization, claude, gemini]
  status: active

- id: ADR-2026-07-25-002
  type: decision
  timestamp: "2026-07-25T18:25:00+03:00"
  agents: [code-architect, persistent-memory-engine]
  context: "تفعيل معمارية فرق العمل المتوازية (Agent Teams)"
  content: "لزيادة سرعة الإنجاز، تم تفعيل ميزة فرق وكلاء كلود التجريبية `CLAUDE_CODE_EXPERIMENTAL_AGENT_TEAMS=1` في إعدادات المحرر. هذا يسمح بتشغيل عدة وكلاء يعملون بالتوازي وفي مساحات منفصلة في سطر الأوامر والتواصل فيما بينهم. يتم اللجوء لهذه الميزة بدلاً من `Subagents` عندما يكون العمل يتطلب استكشاف موازي ومستقل مثل دراسة الكود من عدة زوايا، أو تشغيل فريق بحث استراتيجي."
  tags: [agent-teams, parallel-execution, architecture, claude-code]
  status: active
```

---

## سجل تفضيلات المستخدم (User Preferences)

```yaml
- id: PREF-2026-07-21-001
  type: preference
  timestamp: "2026-07-21T11:00:00+03:00"
  agents: [linguistic-assistant]
  context: "تفضيلات اللغة والتنسيق"
  content: "جميع الردود باللغة العربية الفصحى مع تنسيق RTL. مهارات متخصصة فعلياً بدون دمج. اسم النظام: Autovem"
  tags: [language, rtl, naming]
  status: active

- id: PREF-2026-07-21-002
  type: preference
  timestamp: "2026-07-21T11:50:00+03:00"
  agents: [persistent-memory-engine]
  context: "ترتيب أعمدة جداول الخطافات وتسهيل النسخ"
  content: "تفضيل وضع المحفزات (Triggers) قبل أسماء الوكلاء المسئولين في جداول وملفات الخطافات لسهولة النسخ المباشر للمحفز في واجهات المستخدم والبطاقات."
  tags: [ui-preference, hooks, triggers-order]
  status: active

- id: PREF-2026-07-24-001
  type: preference
  timestamp: "2026-07-24T15:20:00+03:00"
  agents: [persistent-memory-engine, frontend-design-builder]
  context: "تصدير ملف الإكسيل لتطبيق مكتبة الأوامر (Prompt Library)"
  content: "عند بناء ملف الإكسيل الخاص بالخطافات لتطبيق الويب، يجب الالتزام الصارم بتخصيص العمود الثالث ليكون (المحفزات - Triggers فقط) لضمان أن زر النسخ في التطبيق ينسخ المحفز فقط لتشغيل الوكيل، بينما يتم عزل (الوصف والتفاصيل) في عمود مستقل (الأخير) ليتم عرضه للمستخدم كمعلومات دون أن يتداخل مع النص المنسوخ."
  tags: [excel-export, ui-preference, prompt-library, copy-action]
  status: active

- id: PREF-2026-07-28-001
  type: preference
  timestamp: "2026-07-28T22:00:00+03:00"
  agents: [agent-optimizer, performance-optimizer]
  context: "ترشيد استهلاك التوكنز في عمليات البناء (Build Process)"
  content: "يُمنع تنفيذ أوامر بناء أندرويد (مثل ./gradlew assembleDebug) عبر طرفية الوكيل لأن مخرجاتها الكثيفة تستهلك توكنز عالية بلا داعٍ. يجب دائماً تفويض عملية البناء للمستخدم ليقوم بها يدوياً عبر (Android Studio)، ويقتصر دور الوكيل على التوجيه وكتابة الكود."
  tags: [token-optimization, build, android-studio, cost-saving]
  status: active

- id: PREF-2026-07-28-002
  type: preference
  timestamp: "2026-07-28T22:45:00+03:00"
  agents: [agent-optimizer]
  context: "سرعة الإنجاز والعمل بالتوازي واستقلالية التنفيذ"
  content: "تم تحديث دستور المشروع (AGENTS.md) لتفويض التنفيذ المباشر والعمل المتوازي دون انتظار إذن صريح للمهام العادية. تم تفعيل سياسة 'تقليص الاستهلاك' لجعل الردود مقتضبة جداً مع التركيز على التقارير النهائية والأكواد."
  tags: [autonomy, parallel-execution, lean-communication, token-optimization]
  status: active
```

---

## سجل الأخطاء المحلولة (Resolved Bugs)

```yaml
- id: MEM-2026-07-21-007
  type: bug-fix
  timestamp: "2026-07-21T21:23:00+03:00"
  agents: [persistent-memory-engine, debugger]
  context: "فشل إنشاء سجل جديد (Failed to create record) في PocketBase مع بيانات استجابة فارغة (data: {})"
  content: "عند إرسال طلب لإنشاء سجل يحتوي على حقول علاقات (Relations)، يجب التأكد أن قيمة الحقل المُرسلة هي الـ ID الخاص بالعنصر (وهو نص مكون من 15 حرفاً). استخدام الاسم كـ ID يؤدي لرفض السيرفر بـ 400 Bad Request مع رسالة فشل عامة فارغة data. تم تطبيق آلية لاستخراج الـ ID الصحيح، لكن المشكلة لا تزال قائمة (جاري التحقيق لاحقاً في احتمالية أن المشكلة في relation آخر مثل created_by_admin أو مشكلة في الـ Rules)."
  tags: [pocketbase, bug-fix, relations, api, pending]
  status: pending_investigation
```

---

## فهرس المهارات والقدرات (Skill Capability Index)

| القسم | عدد المهارات | المهارات |
|:------|:---:|:---------|
| **النواة والذاكرة** | 1 | persistent-memory-engine |
| **أدوات التطوير** | 4 | skill-forge-builder, docs-fetcher-context, mcp-tool-builder, webapp-qa-tester |
| **التصميم والعلامة** | 6 | ui-ux-design-lead, taste-design-critic, motion-transitions-pro, frontend-design-builder, web-artifacts-prototyper, brand-kit-keeper |
| **التسويق والنمو** | 5 | copywriting-lead, ai-geo-seo-optimizer, cro-conversion-lead, ad-creative-maker, customer-research-voice |
| **صناعة المحتوى** | 3 | post-content-writer, script-hook-generator, profile-optimizer |
| **المالية** | 6 | financial-statements-builder, journal-entry-keeper, reconciliation-auditor, variance-analyst, audit-support-prep, close-management-lead |
| **الأعمال** | 6 | cash-flow-watcher, invoice-chaser, payroll-planner, margin-analyst, tax-prepper, campaign-runner |
| **القانون** | 6 | contract-reviewer, nda-triage, compliance-officer, legal-risk-assessor, vendor-vetter, signature-wrangler |
| **الوكلاء الأصليون** | 23 | (راجع sub_agents.yaml للقائمة الكاملة) |

</div>
