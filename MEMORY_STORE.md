<div dir="rtl">

# مخزن الذاكرة المركزي لمشروع Blind App (MEMORY_STORE.md)

> [!IMPORTANT]
> هذا الملف هو السجل المركزي للذاكرة المستدامة. يُحدّث تلقائياً بواسطة وكيل `persistent-memory-engine` بعد كل خطاف نجاح أو أمر تسجيل يدوي.

---

## سجل الدروس المستفادة (Lessons Learned)

```yaml
- id: MEM-FLEET-STRATEGIC-CLEAR-AGCLI-2026-09-12-007
  type: adr
  timestamp: "2026-09-12T10:40:00+03:00"
  agents: [fleet-orchestrator, prompt-engineer, persistent-memory-engine]
  context: "اعتماد بروتوكول التصفير الاستراتيجي وعزل تفويض Antigravity CLI عن بيئة Antigravity IDE"
  content: "1) اعتماد مصطلح وبروتوكول 'التصفير الاستراتيجي' (Strategic Clear) تحت الخطاف 25 والقالب 04 لضمان حسم المهام الحرجة وتثبيت الكود في حالة مستقرة خالية من أخطاء البناء وعمل Git Commit نظيف قبل مسح السياق أو التصفير. 2) الفصل المعماري التام بين الأدوات الطرفية (Headless CLIs) وبيئة العمل التفاعلية: حصر التكليفات البرمجية لأسطول الـ CLIs في الأدوات الطرفية المستقلة (Claude Code CLI, OpenCode CLI, Antigravity CLI agcli عبر agcli run)، وحظر التكليف لـ Antigravity IDE لحماية الأتمتة المباشرة ومنع الحاجة للنسخ واللصق اليدوي."
  tags: [fleet-orchestration, strategic-clear, hook-25, template-04, agcli-decoupling, antigravity-cli, autonomous-dispatch]
  status: active

- id: MEM-HOSTINGER-REMOTE-FUTRX-005
  type: adr
  timestamp: "2026-09-08T23:25:00+03:00"
  agents: [fleet-orchestrator, backend-architect, devops-deployer, persistent-memory-engine]
  context: "أتمتة نشر منصة remote.futrx على خادم Hostinger VPS بواسطة أدوات Hostinger MCP وتنسيق الأسطول"
  content: "بناءً على توجيه المستخدم واستدعاء أسطول الـ CLIs: 1) تفعيل بروتوكول الخطوة صفر والالتزام بالقيد 53 (عزل منصات الحاويات السحابية). 2) استخدام أدوات Hostinger MCP للتحقق من السيرفر (ID: 1810150, IP: 187.55.226.225). 3) أتمتة إضافة سجلات DNS الأربعة لنطاق autovem.tech عبر DNS_validateDNSRecordsV1 و DNS_updateDNSRecordsV1 بنجاح كامل. 4) ربط المفتاح العام SSH بالسيرفر عبر VPS_attachPublicKeyV1. 5) اكتشاف عمل caddy على الحاوية وحل تعارض المنافذ 80 و 443 مع التثبيت المباشر."
  tags: [hostinger-mcp, remote-futrx, vps-deployment, dns-automation, multi-cli-fleet, global]
  status: active

- id: MEM-FLEET-MODELS-2026-09-08-006
  type: adr
  timestamp: "2026-09-08T23:25:00+03:00"
  agents: [fleet-orchestrator, persistent-memory-engine]
  context: "اعتماد نموذج deepseek/deepseek-v4-flash لـ OpenCode CLI وترقية Antigravity إلى Gemini 3.8 Flash High عبر /learn"
  content: "تطبيقاً لأمر /learn وموافقة المستخدم الصريحة: 1) اعتماد نموذج deepseek/deepseek-v4-flash رسمياً كنموذج حصري لمنفذ الأوامر الطرفية وقواعد البيانات OpenCode CLI في القاعدة 13 وملفات fleet_config.json. 2) ترقية محرك Antigravity IDE إلى Gemini 3.8 Flash High للاستفادة من قدرات الاستدلال المتطورة وسرعة التوليد. 3) الحفاظ الصارم على احتكار الاختبارات والتدقيق المعماري النهائي لـ Claude Code CLI (Opus Max)."
  tags: [fleet-config, opencode-model, deepseek-v4-flash, gemini-3-8-flash, rule-13, memory-sync]
  status: active

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

- id: MEM-2026-08-09-001
  type: preference
  timestamp: "2026-08-09T19:27:00+03:00"
  agents: [persistent-memory-engine]
  context: "سلوك نسخ النصوص في واجهات الويب (Copy Action Behavior)"
  content: "عند استخدام زر النسخ في تطبيق الويب، يجب ألا ينسخ الوصف، بل المحفز (Trigger) فقط لمنع تداخل النصوص ونسخ معلومات إضافية غير مرغوبة."
  tags: [web-app, ux, copy-action, prompt-trigger]
  status: active

- id: MEM-2026-08-09-002
  type: lesson
  timestamp: "2026-08-09T21:05:00+03:00"
  agents: [agent-optimizer, code-reviewer-quality]
  context: "التدقيق المعماري الصارم (Devil's Advocate Audit)"
  content: "الاعتماد الأولي على النماذج قد يولد حلولاً سطحية (Band-aids) مثل الاعتماد على taskkill أو التحايل بالبايثون لحل مشاكل PowerShell. يجب دائماً تفعيل وكلاء الجودة (5، 6، 7) معاً تحت دور المدقق الصارم (محامي الشيطان) لتمزيق الحلول السطحية وفرض حلول هندسية جذرية مثل (Mutex Locks، وتصحيح ترميز الكونسول مباشرة)."
  tags: [quality-audit, best-practices, devil-advocate]
  status: active
- id: MEM-2026-08-10-001
  type: lesson
  timestamp: "2026-08-10T19:30:00+03:00"
  agents: [persistent-memory-engine, agent-optimizer]
  context: "تكرار أخطاء مسجلة مسبقاً (استخدام Fully Qualified Names لدوال الامتداد) بسبب تجاهل قراءة الذاكرة."
  content: "مجرد (تسجيل) الذكريات لا يكفي. يقع الوكيل أحياناً في (Shortcut Anti-Pattern) محاولاً اختصار الوقت. تم إقرار مبدأ (حقن السياق الإجباري Mandatory Context Injection): يجب أن تُمرر الدروس والأخطاء الشائعة ذات الصلة قسرياً للوكيل قبل بدء البرمجة، لضمان عدم استناده فقط لحفظه الخاطئ للغة المترجم."
  tags: [architecture, global, agent-behavior, context-injection, anti-pattern]
  status: active
- id: MEM-2026-08-10-002
  type: lesson
  timestamp: "2026-08-10T21:10:00+03:00"
  agents: [persistent-memory-engine, code-architect]
  context: "نجاح السحب الأفقي الصامت بإصبعين باستخدام TalkBack"
  content: "تم بنجاح استعادة ميزة السحب بإصبعين للتنقل بين الصفحات مع تشغيل TalkBack. تبين أن المشكلة كانت بسبب تغليف الـ Pager بمكونات مخصصة (SilentAccessiblePager) تمنع السلوك الافتراضي. الحل الأمثل: الاعتماد المطلق على (HorizontalPager) القياسي وتطبيق مبدأ المعمارية ثنائية الوضع (Dual-Mode Architecture). ولضمان الصمت التام للـ Pager أثناء السحب، تم إعطاؤه contentDescription بفاصلة فقط `,`."
  tags: [accessibility, talkback, horizontal-pager, dual-mode, bug-fix, success]
  status: active
- id: MEM-2026-08-10-003
  type: lesson
  timestamp: "2026-08-10T23:05:00+03:00"
  agents: [persistent-memory-engine, code-architect]
  context: "حماية الميزات المحذوفة مسبقاً بناءً على طلب العميل (Feature Deletion Memory)"
  content: "ميزة عالمية (Global Rule): يُمنع منعاً باتاً إعادة إضافة أي مكون واجهة مستخدم (UI Component) أو ميزة (Feature) تم حذفها مسبقاً (مثل شريط التشغيل PlayerControlPanel أو الأوامر الصوتية) إلا بطلب صريح ومباشر من المستخدم. يجب دائماً احترام حالة الكود كما هو موجود في آخر Commit وعدم افتراض أن اختفاء المكون هو خطأ برمجي يحتاج للاسترجاع."
  tags: [architecture, global, agent-behavior, ui-components]
  status: active
- id: MEM-2026-08-13-002
  type: lesson
  timestamp: "2026-08-13T12:15:00+03:00"
  agents: [persistent-memory-engine, code-architect]
  context: "الفرض الإجباري لبرومبت 'محامي الشيطان' (Devil's Advocate Persona) على المستوى العالمي"
  content: "لتجنب نسيان حلقة محامي الشيطان وتخطيها، يُفرض قسرياً على الوكيل عند بدء أي ميزة جديدة التوقف فور إنشاء الخطة المبدئية، واستدعاء شخصية 'Devil’s Advocate & Senior Staff Architect'. يُمنع كتابة الكود قبل توليد المخرجات الصارمة: [DEVIL'S ADVOCATE CRITIQUE], [ENGINEERING FIXES], و [MASTER REFINED PLAN CONSTRAINTS] باستخدام معايير التقييم الأربعة (الكمال المعماري، حالات الحافة، الآثار الجانبية، والسطحية). تم حقن هذا القيد في ACTIVE_CONTEXT_INJECTION."
  tags: [architecture, global, devil-advocate, context-injection]
  status: active
```

---

## سجل القرارات المعمارية (Architecture Decisions)

```yaml
- id: ADR-2026-08-01-001
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
  context: "تخصيص أعمدة لوحة القيادة (Dashboard Columns)"
  content: "لتفعيل دورة إضافات لتطبيق الويب، يجب الالتزام الصارم بتخصيص العمود الثالث ليكون (المحفزات - Triggers فقط) لضمان أن زر النسخ في التطبيق ينسخ المحفز فقط لتشغيل الوكيل، بينما يتم عزل (الوصف والتفاصيل) في عمود مستقل (الأخير) ليتم عرضه للمستخدم كمعلومات دون أن يتداخل مع النص المنسوخ."
  tags: [excel-export, ui-preference, prompt-library, copy-action]
  status: active

- id: ADR-2026-08-09-002
  type: decision
  timestamp: "2026-08-09T21:05:00+03:00"
  agents: [persistent-memory-engine, agent-optimizer]
  context: "اعتماد الخطاف الحارس (Watchdog Hook) لمنع فقدان الذاكرة"
  content: "اكتشفنا ظاهرة (المشاريع فارغة الذاكرة) في تطبيقي (تاج الوقار) و(تيجان النور) بسبب عدم تفعيل المستخدم لخطاف حفظ الذاكرة في نهاية الجلسة. كقرار معماري، تم تعديل قالب (AGENTS_SEED) لإلزام النظام مستقبلاً بتضمين خطاف حارس (Watchdog) يستخرج الـ Diffs آلياً ويحفظها في الذاكرة دون انتظار طلب مباشر."
  tags: [memory-architecture, compliance, watchdog-hook]
  status: active
```

---

## سجل الأخطاء المحلولة والقائمة (Resolved & Pending Bugs)

```yaml
- id: BUG-2026-08-01-001
  type: bug-fix
  timestamp: "2026-08-01T01:25:00+03:00"
  agents: [persistent-memory-engine, debugger]
  context: "مشكلة عدم توافق صوت الـ TTS الداخلي مع صوت الهاتف (صوت أنثوي بدلاً من المألوف)"
  content: "لم يتم حل المشكلة الجذرية المتعلقة بنوع الصوت الداخلي للتطبيق (لا يزال أنثوياً ومختلفاً عن صوت الهاتف الفعلي). يجب إجبار التطبيق على تبني محرك وصوت الـ TTS الافتراضي للنظام بالكامل."
  tags: [tts, accessibility, pending]
  status: pending_investigation

- id: BUG-2026-07-21-007
  type: bug-fix
  timestamp: "2026-07-21T21:23:00+03:00"
  agents: [persistent-memory-engine, debugger]
  context: "فشل إنشاء سجل جديد (Failed to create record) في PocketBase مع بيانات استجابة فارغة (data: {})"
  content: "عند إرسال طلب لإنشاء سجل يحتوي على حقول علاقات (Relations)، يجب التأكد أن قيمة الحقل المُرسلة هي الـ ID الخاص بالعنصر (وهو نص مكون من 15 حرفاً). استخدام الاسم كـ ID يؤدي لرفض السيرفر بـ 400 Bad Request مع رسالة فشل عامة فارغة data. تم تطبيق آلية لاستخراج الـ ID الصحيح، لكن المشكلة لا تزال قائمة (جاري التحقيق لاحقاً في احتمالية أن المشكلة في relation آخر مثل created_by_admin أو مشكلة في الـ Rules)."
  tags: [pocketbase, bug-fix, relations, api, pending]

- id: MEM-2026-08-01-001
  type: bug-fix
  timestamp: "2026-08-01T01:25:00+03:00"
  agents: [persistent-memory-engine, debugger]
  context: "مشكلة عدم توافق صوت الـ TTS الداخلي مع صوت الهاتف (صوت أنثوي بدلاً من المألوف)"
  content: "الدروس المستفادة من هذه المرحلة:\n1. منع الميكروفون من العمل أثناء النطق لتفادي تعارض Audio Focus.\n2. ضرورة إضافة `<queries>` لخدمة `RecognitionService` في أندرويد 11+.\n3. تطبيق تطبيع الحروف العربية للنصوص الملتقطة بالصوت (Normalization).\n\nالمشكلة المتبقية: لم يتم حل المشكلة الجذرية المتعلقة بنوع الصوت الداخلي للتطبيق (لا يزال أنثوياً ومختلفاً عن صوت الهاتف الفعلي). يجب في جلسة العمل القادمة إجبار التطبيق على تبني محرك وصوت الـ TTS الافتراضي للنظام بالكامل."
  tags: [tts, accessibility, speech-recognizer, pending]
  status: pending_investigation

- id: BUG-2026-08-01-002
  type: bug-fix
  timestamp: "2026-08-01T15:34:00+03:00"
  agents: [persistent-memory-engine, debugger, android-testing]
  context: "مشكلة عدم توافق صوت الـ TTS الداخلي مع صوت الهاتف وتعارض الميكروفون عند الاستماع، وفشل اختبارات VoiceCommandManagerTest."
  resolution: "إدارة الـ Audio Focus بشكل صارم في VoiceCommandManager (requestAudioFocus و abandonAudioFocus). تعديل VoiceCommandManager ليعيد النص الخام. وتجاوز اختبارات VoiceCommandManagerTest بعد عمل Mock لـ AudioManager لمنع ClassCastException."

- id: BUG-2026-08-02-001
  type: bug-fix
  timestamp: "2026-08-02T14:22:00+03:00"
  agents: [persistent-memory-engine, debugger]
  context: "إغلاق التطبيق (Crash) والدخول في حلقة لا نهائية عند تمرير واجهة Pager."
  content: "كان التطبيق ينهار أو يتخطى الآيات سريعاً بسبب التقاط حالة قديمة (Stale State) داخل بيئة LaunchedEffect في Compose. تم استبدال القراءة بحالة حية `viewModel.uiState.value.currentIndex` واستخدام أمر انتقال مباشر `goToAyah` لتفادي المشكلة."
  tags: [compose, launched-effect, bug-fix, ui]

- id: ADR-2026-08-09-002
  type: decision
  timestamp: "2026-08-09T21:20:00+03:00"
  agents: [agent-optimizer, persistent-memory-engine]
  context: "تكرار ظاهرة 'المشاريع فارغة الذاكرة' في المشاريع السابقة مثل تاج الوقار."
  content: "تقرر اعتماد مبدأ 'الخطاف الحارس' (Watchdog Hook) لمنع فقدان الذاكرة. لا يمكن الاعتماد على نوايا المستخدم لتفعيل وكيل الذاكرة يدوياً، بل يجب أن يقوم النظام بأخذ لقطة للمتغيرات وحفظها في MEMORY_STORE.md بشكل إجباري قبل الإغلاق."
  tags: [architecture, memory, watchdog, reliability, global]

- id: MEM-2026-08-09-003
  type: lesson
  timestamp: "2026-08-09T21:20:00+03:00"
  agents: [agent-optimizer, code-reviewer-quality]
  context: "مراجعة قوالب التأسيس العالمية واكتشاف ترقيعات (Band-Aids) متراكمة."
  content: "تم إرساء مبدأ دور 'محامي الشيطان' (Devil's Advocate) للتدقيق المعماري الصارم، والذي يمنع رفض أي ترقيع سطحي (مثل استخدام taskkill لمعالجة تعارض العمليات، أو سكربتات بايثون لحل مشكلة ترميز PowerShell) واستبدالها بحلول جذرية مستدامة."
  tags: [architecture, auditing, clean-code, global]

- id: ADR-2026-08-09-004
  type: decision
  timestamp: "2026-08-09T21:44:00+03:00"
  agents: [agent-optimizer, persistent-memory-engine, mcp-tool-builder]
  context: "الحاجة إلى مزامنة الدروس المستفادة عبر كافة المشاريع المحلية في بيئة التطوير (IDE)."
  content: "تم ابتكار وتصميم أول خادم (MCP Server) مخصص لبيئة المحرر باستخدام `FastMCP`. وظيفته فحص الذاكرة المحلية لأي مشروع ومزامنة الدروس العالمية إلى المستودع المركزي. وتم دمج السكربت كإضافة (Plugin) متكاملة في مجلد الإعدادات ليعمل بشكل مركزي مع أي مشروع."
  tags: [mcp, architecture, memory, automation, global]
  status: active

- id: ADR-2026-08-13-003
  type: decision
  timestamp: "2026-08-13T15:38:00+03:00"
  agents: [agent-optimizer, persistent-memory-engine, code-architect]
  context: "منع تضخم نافذة السياق (Context Window Bloat) في ملف القواعد العالمي AGENTS.md"
  content: "تطبيقاً لمبدأ فصل الاهتمامات، يُمنع حشو ملف AGENTS.md المركزي بالتفاصيل التقنية والبرومبتات الطويلة. بدلاً من ذلك، تُعزل هذه التفاصيل في ملف مستقل (ACTIVE_CONTEXT_INJECTION.md) داخل مجلد config العالمي، ويُضاف سطر واحد فقط في AGENTS.md يوجه الوكيل لقراءة هذا الملف قبل أي عملية برمجية. هذا يحافظ على تركيز النماذج ويقلل استهلاك الذاكرة."
  tags: [architecture, global, context-window, optimization]
  status: active
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


## الدروس المستفادة من مشروع تطبيق المكفوفين (TTS & STT)
1. **احترام تفضيلات النطق للمستخدم الكفيف (Accessibility UX)**
   - المشكلة: محاولة فرض محرك نطق محدد أو فرض أصوات سحابية عالية الجودة تشتت المستخدم.
   - الحل: ترك تهيئة TextToSpeech افتراضية تماماً بدون تمرير اسم المحرك ليعتمد التطبيق فوراً على تفضيلات النظام.
2. **منع تداخل النطق بين التطبيق و TalkBack**
   - المشكلة: المساعد الداخلي ينطق الوصف في نفس وقت TalkBack.
   - الحل: منع النطق الداخلي إذا كان isTalkBackEnabled صحيحاً. توفير contentDescription بدلاً من ذلك.
3. **أمان أندرويد 11+ في خدمة التعرف الصوتي (SpeechRecognizer)**
   - المشكلة: فشل التعرف الصوتي مباشرة بسبب حجب الخدمة أمنياً.
   - الحل: إضافة queries في AndroidManifest.xml للسماح بالوصول لخدمة جوجل.
4. **تجنب تعارض الـ TTS مع الميكروفون (Audio Focus Conflict)**
   - المشكلة: الميكروفون يلتقط صوت المساعد ويغلق.
   - الحل: عدم تشغيل TTS.speak قبل فتح الميكروفون مباشرة. الاعتماد على الرنة الافتراضية القصيرة (Beep).
5. **معالجة الاختلافات في تحويل الصوت إلى نص (Arabic STT Normalization)**
   - المشكلة: عدم التعرف على الأوامر بسبب اختلاف كتابة الهمزات والتاء المربوطة.
   - الحل: تطبيق نظام تطبيع (Normalization) لتوحيد الحروف قبل المقارنة.
6. **إدارة تنازع الصوت (Audio Focus Management) بشكل جذري**
   - المشكلة: تعارض مع قوارئ الشاشة رغم الإجراءات السابقة.
   - الحل: استدعاء requestAudioFocus مع AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE والتخلي عنه بـ abandonAudioFocus لضمان توجيه الصوت للتطبيق فقط أثناء الاستماع.
7. **محاكاة خدمات النظام في اختبارات الوحدة**
   - المشكلة: حقن Context يحمل getSystemService يسبب ClassCastException مع MockK.
   - الحل: عمل Mock صريح للخدمة وإرجاعها عند طلبها من الـ Context.
8. **تعارض التقاط الحالة القديمة في Compose (Stale State Capture in LaunchedEffect)**
   - المشكلة: الاعتماد على قيمة مقروءة من الـ `uiState` داخل كتلة `snapshotFlow` يؤدي لالتقاط قيمة قديمة (Stale Value)، مما قد يسبب حلقة لا نهائية (Infinite Loop) من التنقل العشوائي أو توقف التطبيق (Crash).
   - الحل: قراءة القيمة الحية مباشرة من المرجع داخل كتلة الـ `collect` مثل `viewModel.uiState.value.currentIndex` وتجنب استخدام المتغيرات الملتقطة، وكذلك الاعتماد على القفز المباشر `goToAyah(index)` بدلاً من حسابات الزيادة/النقصان النسبية.
9. **تطبيق مبدأ DRY (لا تكرر نفسك) على الروابط المركزية**
   - المشكلة: تكرار الرابط الأساسي (`https://verse.mp3quran.net/data/`) في كل عنصر ضمن قائمة القراء يرفع نسبة الخطأ ويجعل الصيانة صعبة.
   - الحل: استخراج الرابط كمتغير ثابت (`private const val BASE_URL`) وتمريره برمجياً لكل عنصر.
10. **توظيف أدوات Android Studio لحل قيود التعديل الخارجي**
   - المشكلة: الحاجة لقص وتعديل مقاسات الأيقونات (Image Processing) مع وجود قيد صارم يمنع تشغيل سكريبتات أو أوامر طرفية خارجية (No Shell Commands).
   - الحل: توجيه المستخدم لاستخدام الأداة المدمجة `Image Asset Studio` كخيار قياسي وأكثر أماناً لتوليد (Adaptive Icons) لجميع مقاسات الشاشات.
11. **التشغيل المتصل الديناميكي ومراعاة نمط المستخدم**
    - المشكلة: فرض قائمة تشغيل (Playlist) مستمرة يكسر النمط الافتراضي للتطبيق المبني على السحب اليدوي (Swipe-to-Read).
    - الحل: حقن قائمة الآيات المترابطة في `ExoPlayer` فقط عند تفعيل (الاستماع المتواصل)، واستخدام مستمع `onMediaItemTransition` لتحديث الواجهة، بينما يُترك النمط الفردي لتجربة السحب اليدوي الافتراضية.
12. **معالجة تعارض الصوت التلقائي عبر ContentType**
    - المشكلة: محاولة إدارة التركيز الصوتي (Audio Focus) يدوياً للإيقاف عند نطق TalkBack معقدة.
    - الحل: تغيير `ContentType` لمشغل `ExoPlayer` إلى `C.AUDIO_CONTENT_TYPE_SPEECH`. هذه الميزة المدمجة تجبر المشغل على الإيقاف المؤقت (Pause) تلقائياً عندما يتحدث المساعد (Ducking)، واستئناف التشغيل فور سكوته، دون الحاجة لمستمع صوتي يدوي.
13. **التدقيق المعماري الصارم ومنع الترقيعات السطحية (No Band-Aids)**
    - المشكلة: تراكم ديون تقنية بسبب استخدام حلول مؤقتة مثل `taskkill` لإنهاء العمليات المعلقة أو سكربتات خارجية لحل مشاكل ترميز.
    - الحل: تفعيل وكيل بدور "محامي الشيطان" لرفض هذه الترقيعات وفرض حلول هندسية صلبة (مثل Mutex Locks ووضع ترميز `UTF-8` محلي في `PowerShell`).
14. **ضمان استدامة الذاكرة عبر الخطاف الحارس (Watchdog Hook)**
    - المشكلة: مشاريع عملاقة (مثل تاج الوقار) لم تُسجل فيها أي ذكريات بسبب الاعتماد على تذكر المستخدم لاستدعاء وكيل الذاكرة.
    - الحل: بناء خطاف حارس مستقل يجبر النظام على استخراج الـ Diffs وتخزينها تلقائياً عند اقتراب انتهاء المهام.
15. **تنسيق عرض ماركداون للغة العربية في بيئة التطوير (IDE RTL Rendering)**
    - المشكلة: ملفات التوثيق العربية (`AGENTS.md` وغيرها) تظهر بشكل مشوه ومعكوس (الأرقام والرموز متداخلة) في واجهة العرض داخل المحرر.
    - الحل: تغليف جميع مستندات الماركداون والتقارير بـ `<div dir="rtl">` في بدايتها و `</div>` في نهايتها بشكل إجباري لضمان محاذاة صحيحة.
16. **معالجة انهيار التطبيق (NPE) داخل LazyColumn عند تغيير الحالة (Snapshot Capture)**
    - المشكلة: انهيار التطبيق بـ NullPointerException عند الضغط على زر الرجوع من قائمة الآيات، بسبب تعيين الحالة إلى null بينما لا يزال `LazyColumn` يحاول إعادة بناء عناصره بناءً على حالة قديمة. (تسريب مرجعي).
    - الحل: تطبيق نمط (Snapshot Capture) بالتقاط قيمة المتغير الثابتة قبل الـ if-condition (`val currentSurah = selectedSurahForAyahs`) واستخدامه لتهيئة الـ LazyColumn وتجنب أي استخدام لـ `!!` مع State متغيرة داخل دالة التكوين.

- id: MEM-2026-08-10-004
  type: lesson
  timestamp: "2026-08-10T20:30:00+03:00"
  agents: [persistent-memory-engine, code-architect]
  context: "إصلاحات قارئ الشاشة والسحب الأفقي"
  content: "تم إزالة تعديل الفاصلة (,) من AyahCard لمنع TalkBack من قراءة 'comma page'. كما تم إعادة الاعتماد على currentPage بدلاً من settledPage في مكون HorizontalPager مع إضافة متغير isProgrammaticScroll لتجنب تأخير تزامن الصوت عند التمرير الأفقي."
  tags: [accessibility, talkback, bug-fix, horizontal-pager]
  status: active

- id: MEM-2026-08-11-001
  type: lesson
  timestamp: "2026-08-11T00:05:00+03:00"
  agents: [persistent-memory-engine, debugger]
  context: "إصلاح انهيار التطبيق (NPE) داخل LazyColumn"
  content: "تم تطبيق حل Snapshot Capture لحماية القوائم الكسولة في Jetpack Compose من استثناءات NullPointer الناتجة عن التفريغ السريع للمتغيرات المشتركة."
  tags: [compose, lazycolumn, bug-fix, state]
  status: active

- id: MEM-2026-08-11-002
  type: lesson
  timestamp: "2026-08-11T00:25:00+03:00"
  agents: [persistent-memory-engine, code-architect]
  context: "تخصيص ترتيب البيانات الثابتة وتجاوز الفرز الافتراضي (Custom Sorting Override)"
  content: "لتطبيق فرز مخصص يطلبه المستخدم ويخالف الترتيب الأبجدي أو الافتراضي، الأفضل تطبيق نظام (أوزان مخصصة Custom Weights) باستخدام قائمة `indexOf` واسترجاع رقم أولوية (Priority). وللحفاظ على أي بيانات إضافية خارج القائمة المخصصة، يتم إعطاؤها وزناً كبيراً (مثل 999) لضمان إلحاقها دائماً في النهاية دون حذفها."
  tags: [kotlin, sorting, architecture, custom-logic]
  status: active
- id: MEM-2026-08-11-003
  type: lesson
  timestamp: "2026-08-11T12:00:00+03:00"
  agents: [persistent-memory-engine, code-architect]
  context: "إسكات حاويات التمرير (Pagers) تماماً مع الاحتفاظ بآلية السحب والنقر للمكفوفين (Stealth Box Pattern)"
  content: "إذا كان عنصر التمرير (مثل HorizontalPager) يطلق إعلانات تلقائية مزعجة (مثل 'Page...') ولا يمكن إسكاته بالطرق العادية، يجب استخدام نمط (Stealth Box). يتم تغليف العنصر بـ Box مع تطبيق modifier `clearAndSetSemantics` لإلغاء جميع الدلالات الأصلية. ثم يتم بناء الدلالات المطلوبة (مثل onClick و customActions و scrollBy) يدوياً داخل هذا الـ Box. هذا يوفر 'ثقباً أسود' يبتلع إعلانات المكون الأصلي ويسمح بالتحكم الكامل في الـ Accessibility دون كسر واجهة المستخدم أو التفرع الشجري (Dual-Mode Architecture)."
  tags: [accessibility, talkback, compose, stealth-box, dual-mode, bug-fix, global]
  status: active
- id: MEM-2026-08-11-004
  type: lesson
  timestamp: "2026-08-11T13:30:00+03:00"
  agents: [persistent-memory-engine, code-architect]
  context: "منع الاستئناف التلقائي للصوت عند تغير دورة الحياة (Screen Off/TalkBack)"
  content: "لا يجب بناء آلية لاستئناف الصوت التلقائي `resumePlayback` مرتبطة بأحداث دورة الحياة مثل `ON_START` في تطبيقات الصوتيات الموجهة للمكفوفين (أو بشكل عام). والسبب أن تفعيل TalkBack واستجابته لغلق الشاشة قد يُرسل دورة حياة سريعة لتطبيقات الخلفية مما يتسبب في تشغيل مفاجئ ومزعج للصوت. الاستئناف يجب أن يكون دائماً قراراً واعياً يتخذه المستخدم بضغط زر (التشغيل)."
  tags: [accessibility, talkback, lifecycle, media-playback, bug-fix]
  status: active
- id: MEM-2026-08-11-005
  type: lesson
  timestamp: "2026-08-11T13:46:00+03:00"
  agents: [persistent-memory-engine, debugger]
  context: "منع ExoPlayer من الاستئناف التلقائي المزعج بسبب تداخل TalkBack مع AudioFocus"
  content: "عند تفعيل `setHandleAudioBecomingNoisy(true)` و `setAudioAttributes(.., true)` في ExoPlayer، فإنه يقوم بإيقاف التشغيل مؤقتاً عند فقدان التركيز الصوتي (مثل نطق TalkBack 'شاشة مغلقة' عند إقفال الجهاز) ثم **يستأنف تلقائياً** عند عودة التركيز. المشكلة تحدث إذا حاول التطبيق إيقاف الصوت يدوياً (مثلاً في `ON_STOP`) باستخدام شرط `if (mediaController?.isPlaying == true)`؛ لأن حالة `isPlaying` ستكون `false` (بسبب فقدان التركيز المؤقت لـ TalkBack)، وبالتالي يتم تخطي أمر الإيقاف اليدوي، ويبقى `playWhenReady` صحيحاً، مما يسبب عودة الصوت فجأة والشاشة مغلقة! **الحل:** يجب أن يكون الإيقاف اليدوي `pause()` غير مشروط، أو يعتمد على `playWhenReady` بدلاً من `isPlaying`، لضمان تسجيل أمر الإيقاف بغض النظر عن التركيز الصوتي الحالي."
  tags: [accessibility, talkback, exoplayer, audio-focus, bug-fix, global]
  status: active

- id: MEM-2026-08-11-006
  type: tool-discovery
  timestamp: "2026-08-11T15:45:00+03:00"
  agents: [persistent-memory-engine, devops-deployer]
  context: "إنقاذ القرص C من الامتلاء (من 950MB إلى 6GB)"
  content: "عند معاناة المطورين من امتلاء القرص C الحرج بسبب حزم أندرويد، أفضل استراتيجية هي: 1. نقل مجلدات (gradle, avd, m2) إلى قرص آخر وربطها بروابط وهمية (Junctions - mklink /J) لتفادي كسر إعدادات Android Studio. 2. تنفيذ سكربت تنظيف متقدم لتعطيل السبات (powercfg -h off) وضغط النظام (compact /compactos:always) وتنظيف التحديثات بـ (DISM). يجب أن ينفذ السكربت كمسؤول يدوياً، ويجب إغلاق جميع عمليات java/gradle قبل النقل."
  tags: [windows-optimization, junctions, gradle, avd, disk-space, global]
  status: active

- id: MEM-2026-08-13-001
  type: lesson
  timestamp: "2026-08-13T10:17:00+03:00"
  agents: [persistent-memory-engine, code-architect, security-auditor]
  context: "تنفيذ التمديد المخفي (Backdoor) وتجاوز صلاحيات الفترة التجريبية"
  content: "عند بناء منافذ خلفية (Backdoors) للتطبيقات (مثل تمديد فترات تجريبية أو فتح مميزات نهائية للعملاء المباشرين)، يجب حماية الميزة بضمان الاستخدام لمرة واحدة (Single-Use). الاستراتيجية الأمثل محلياً: 1. تشفير الرموز بـ SHA-256 لتجنب الهندسة العكسية. 2. حفظ الرموز المستخدمة كـ Blacklist داخل EncryptedSharedPreferences. 3. ربط التفعيل بـ pointerInput للضغط المطول على عناصر غير متوقعة (كالأيقونات الجمالية). 4. توفير وصول موازي للمكفوفين عبر TalkBack باستخدام CustomAccessibilityAction بدلاً من حرمانهم أو تفعيلها بطريقة مرئية مكشوفة."
  tags: [security, backdoor, compose, talkback, trial-extension]
  status: active

- id: MEM-2026-08-14-001
  type: bug-fix
  timestamp: "2026-08-14T23:50:00+03:00"
  agents: [persistent-memory-engine, code-architect, code-reviewer-quality]
  context: "تجنب استخدام المسافات غير المرئية (\\u00A0) أو الفواصل في contentDescription"
  content: "عند محاولة إسكات عنصر في TalkBack، يُمنع وضع مسافات غير مرئية (مثل '\\u00A0' أو ' ') أو فواصل كـ contentDescription؛ لأن محركات النطق (Google TTS / Vocalizer) تقوم بنطقها حرفياً بصوت مسموع ('فاصلة' أو 'مسافة' أو 'Space'). الحل الصحيح: إما استخدام وصف دلالي واضح ومختصر (مثل 'الآية الحالية') أو الاعتماد على `clearAndSetSemantics { }` فارغة دون وضع أي محارف."
  tags: [accessibility, talkback, tts, semantics, bug-fix, global]
  status: active

- id: MEM-2026-08-15-001
  type: lesson
  timestamp: "2026-08-15T00:02:00+03:00"
  agents: [persistent-memory-engine, debugger, code-reviewer-quality]
  context: "حماية استيرادات الحزم (Imports) عند تعديل كتل الكود المتجاورة"
  content: "عند استبدال كتل الألوان أو السمات في ترويسة ملفات Kotlin، يجب الانتباه لعدم حذف استيرادات النماذج والواجهات الحيوية (مثل Reciter, Ayah, RoundedCornerShape). يُلزم الوكيل بفحص قائمة الاستيرادات في الملف بالكامل قبل حفظ التعديل.\n[ANTI-PATTERN AVOIDED]: التعديل السريع للترويسة دون مراجعة بقية الاستيرادات المعتمدة في الملف."
  tags: [kotlin, imports, build-error, refactoring, quality]
  status: active

- id: MEM-2026-08-15-002
  type: decision
  timestamp: "2026-08-15T00:03:00+03:00"
  agents: [persistent-memory-engine, code-architect, jetpack-compose-ui]
  context: "اعتماد النمط الدافئ البيج الترابي (#B38A5F) ونظام الألوان المتسق وتخصيص إعلانات TalkBack"
  content: "تم اعتماد نظام الألوان الدافئ الموحد: خلفية وكارت (#B38A5F)، نص الآية بالأسود الفحمي (#120C07) لوضوح التشكيل، إطارات (#8A653F) والأحمر الطوبي (#7C261E) للأرقام والتفعيل. مع تخصيص وصف كارت الآية لـ TalkBack ليعلن 'الآية [الرقم]' فقط، وحذف علامات (X) من كافة النوافذ، وإيقاف التلاوة فوراً عند فتح أي حوار.\n[ANTI-PATTERN AVOIDED]: استخدام ألوان عشوائية أو تشتيت الكفيف بتلاوة مستمرة أثناء فتح القوائم."
  tags: [ui, theme, accessibility, talkback, architecture]
  status: active
- id: MEM-2026-08-15-003
  type: bug-fix
  timestamp: "2026-08-15T03:22:00+03:00"
  agents: [persistent-memory-engine, code-architect, jetpack-compose-ui]
  context: "ظهور دائرة سوداء غريبة (⦿) فوق ألف التفريق في بعض الآيات"
  content: "سبب المشكلة الجذري لم يكن في النص، بل في ملف الخط (uthman_taha.ttf). كان الرمز (uni06DF) المخصص للصفر المستدير يشير بالخطأ إلى الوردة الكبيرة (uni0600). تم تصحيح الخط، وتم استبدال (uni06DF) بـ (uni06E0) وقائياً في دوال التعقيم (sanitizeUthmanicText). بالإضافة إلى ذلك، تم توحيد التصميم بإضافة كارت (SurahNameCard) مطابق لنمط كارت رقم الآية للحفاظ على التناسق البصري."
  tags: [typography, font-rendering, bug-fix, compose-ui, global]
  status: active
- id: MEM-2026-08-15-004
  type: lesson
  timestamp: "2026-08-15T03:25:00+03:00"
  agents: [persistent-memory-engine, code-architect]
  context: "إلغاء تلوين الكلمة المنفردة (Word-by-Word Highlighting) لعدم تزامنها مع القراء المتعددين"
  content: "عند دعم عدد كبير من القراء بسرعات تلاوة متفاوتة، الاعتماد على خوارزمية تلوين الكلمة المنفردة (عبر تقدير زمني ثابت أو مسافات متساوية) يؤدي حتماً إلى عدم تزامن مزعج ومشتت (Desync) مع الكلمة المنطوقة. الحل الهندسي الأمثل: إلغاء التلوين الجزئي للكلمات تماماً، والاعتماد على إبراز (كارت الآية بالكامل) باستخدام إطار دافئ وتلوين مخصص للآية النشطة. هذا النمط آمن، ومريح بصرياً، ولا يكسر تجربة المستخدم مهما اختلفت سرعة القارئ."
  tags: [ux, audio-sync, word-highlighting, global]
  status: active

- id: MEM-2026-08-15-005
  type: bug-fix
  timestamp: "2026-08-15T03:25:00+03:00"
  agents: [persistent-memory-engine, android-kotlin-pro]
  context: "التبديل الفوري للقارئ أثناء التلاوة (Instant Reciter Switching)"
  content: "عند اختيار قارئ جديد أثناء تشغيل التلاوة، بقاء القارئ القديم في طابور (ExoPlayer) يؤدي لتداخل الأصوات واستمرار التلاوة بالصوت القديم. يجب تفريغ المشغل فوراً عبر (mediaController.clearMediaItems()) قبل إرسال أمر تحميل السورة الجديدة وتمرير (autoPlay = true). هذا يضمن إسكات القارئ القديم في نفس اللحظة والبدء الفوري بالقارئ الجديد دون أعطال أو تزاحم في الطابور."
  tags: [exoplayer, media3, audio-playback, bug-fix, global]
  status: active

- id: MEM-2026-08-15-006
  type: bug-fix
  timestamp: "2026-08-15T23:05:00+03:00"
  agents: [persistent-memory-engine, code-architect, android-kotlin-pro]
  context: "منع الاستئناف التلقائي للصوت بعد انتهاء المكالمات الهاتفية أو عند توقف التطبيق في الخلفية"
  content: "عندما يفقد المشغل التركيز الصوتي مؤقتاً أثناء مكالمة هاتفية، أو عندما يرسل نظام الاتصال/البلوتوث حدث media button بعد إنهاء المكالمة، فإن مشغل ExoPlayer و MediaSession قد يستأنفان التشغيل تلقائياً. الحل المعماري الجذري: 1. إضافة مستمع لدورة حياة المشغل واستدعاء abandonAudioFocus صراحة عند توقف التشغيل (!playWhenReady). 2. تخصيص MediaSession.Callback لاعتراض أحداث أزرار الوسائط واستهلاكها بأمان دون تشغيل إذا كان المشغل متوقفاً عمداً.\n[ANTI-PATTERN AVOIDED]: الاكتفاء بـ pause() السطحي دون التخلي الصريح عن التركيز الصوتي مما يترك طابور النظام يعيد التشغيل عند إشعار AUDIOFOCUS_GAIN."
  tags: [exoplayer, audio-focus, phone-calls, media3, accessibility, bug-fix, global]
  status: active

- id: MEM-2026-08-15-007
  type: decision
  timestamp: "2026-08-15T23:05:00+03:00"
  agents: [persistent-memory-engine, jetpack-compose-ui, code-architect]
  context: "توحيد لون الكروت النشطة في شاشات السور والقراء مع فصيلة الخلفية الترابية (#B38A5F)"
  content: "تم إلغاء تحول الكروت النشطة إلى اللون الأسود (#201610)، واعتماد اللون الترابي الدافئ (#A27448) من نفس فصيلة الخلفية مع إطار أحمر طوبي دافئ (#7C261E) ونصوص عالية التباين، مما يوفر تجربة بصرية مريحة ومتناسقة مع الحفاظ على وضوح التحديد، وتعديل العبارة السفلية إلى 'انقر للتكرار' بحجم خط bodyMedium وإعلان 'اختيار الآية' فور فتح قائمة الآيات.\n[ANTI-PATTERN AVOIDED]: استخدام اللون الأسود الداكن كعنصر تحديد مما يكسر تناسق الهوية البصرية الترابية الدافئة."
  tags: [ui, theme, accessibility, talkback, warm-earth-theme, global]
  status: active
- id: MEM-2026-08-15-008
  type: bug-fix
  timestamp: "2026-08-15T23:55:00+03:00"
  agents: [persistent-memory-engine, debugger, code-reviewer-quality]
  context: "ظهور خطأ Unresolved reference 'unaryPlus' for operator '+' أثناء تجميع كود Kotlin"
  content: "عند استخدام الدالة الشرطية if/else في Kotlin لتعيين قيمة متغير (مثل الألوان في Jetpack Compose)، فإن وجود علامة زائد (+) بالخطأ قبل المتغير (مثل: +WarmAccentTerracotta) يجعل المترجم يبحث عن دالة unaryPlus() للمتغير. الحل: مراجعة دقيقة لعلامات الترقيم في التعبيرات الشرطية وإزالة علامة الزائد العشوائية التي قد تنتج أثناء تعديلات الكود السريعة.\n[ANTI-PATTERN AVOIDED]: تجاهل مراجعة دلالات (Syntax) السطور المعدلة يدوياً وترك رموز زائدة تسبب فشل بناء كامل للمشروع."
  tags: [kotlin, compose, syntax-error, debugging, global]
  status: active

- id: MEM-2026-08-15-009
  type: lesson
  timestamp: "2026-08-15T23:55:00+03:00"
  agents: [persistent-memory-engine, devops-deployer]
  context: "تشخيص وحل مشكلة عدم تعرف Android Studio على جهاز الهاتف (No Devices)"
  content: "عند ظهور رسالة 'No Devices' بجوار زر التشغيل رغم توصيل الهاتف، المشكلة عادة تنحصر في: 1. عدم قبول إذن (السماح بتصحيح أخطاء USB من هذا الكمبيوتر). 2. كابل الشحن لا ينقل البيانات. 3. تعليق خادم ADB. الحل الجذري الأسرع: تفعيل (Wireless Debugging) وإقران الجهاز عبر QR Code من داخل Android Studio لتجاوز جميع مشاكل الكابلات والتعريفات (Drivers).\n[ANTI-PATTERN AVOIDED]: إضاعة الوقت في إعادة تثبيت تعريفات الويندوز أو تغيير الكابلات قبل تجربة إعادة تشغيل خادم ADB أو الاتصال اللاسلكي السريع."
  tags: [android-studio, adb, wireless-debugging, hardware-connection, global]
  status: active
- id: MEM-2026-08-16-001
  type: design-decision
  timestamp: "2026-08-16T00:10:00+03:00"
  agents: [persistent-memory-engine, ui-ux-design-lead, taste-design-critic]
  context: "تنعيم التباين הלوني لإطار تحديد الكروت النشطة (Border Color)"
  content: "تم استبدال لون الإطار الأحمر الطوبي (WarmAccentTerracotta) حول الكروت النشطة بلون بني ترابي داكن (WarmCardActiveBorder - #6B4A2D) ليتناسب أكثر مع لون الكارت (WarmCardActive) والخلفية (WarmEarthBg). \n[ANTI-PATTERN AVOIDED]: استخدام ألوان حادة للتباين (مثل الأحمر) في مساحات واسعة حول الكروت مما قد يزعج العين أو يكسر تناغم الهوية البصرية الترابية الهادئة، وتم حصر الألوان الحادة (Accent) للأيقونات أو الأزرار الصغيرة."
  tags: [ui, theme, accessibility, warm-earth-theme, global]
  status: active
- id: MEM-2026-08-17-001
  type: design-decision
  timestamp: "2026-08-17T19:25:00+03:00"
  agents: [persistent-memory-engine, jetpack-compose-ui, android-kotlin-pro]
  context: "إتاحة تكرار الآية للمبصرين والمكفوفين وتكبير حجم الخط القرآني"
  content: "1. إظهار عبارة 'انقر للتكرار' في أسفل واجهة المشغل لجميع المستخدمين (المبصرين والمكفوفين معاً) بإلغاء شرط الحصر الخاص بـ TalkBack، مع ربطها بـ clickable لتفعيل إعادة تلاوة الآية الحالية مباشرة عند النقر عليها.\n2. زيادة حجم خط النص القرآني في AyahCard بمقدار درجتين (من 28sp إلى 32sp) وضبط تباعد الأسطر (lineHeight = 68sp) لأقصى وضوح للتشكيل وعلامات الضبط القرآني."
  tags: [ui, typography, compose, dual-mode, accessibility, ayah-card, global]
- id: MEM-2026-08-17-002
  type: architecture-decision
  timestamp: "2026-08-17T20:12:00+03:00"
  agents: [persistent-memory-engine, code-architect, frontend-design-builder]
  context: "بناء نسخة الويب والآيفون التقدمية (Mueen Web iOS PWA) في مجلد معزول"
  content: "تم بناء نسخة ويب تقدمية كاملة ومستقلة في مجلد `web_ios/` للعمل على الآيفون ومتصفح Safari مع الحفاظ المطلق على كود الأندرويد في `app/`. ركائز الإنجاز:\n1. كائن صوتي أحادي مع فتح قفل الصوت في Safari (Audio Unlock Priming) والربط الكامل بـ navigator.mediaSession.\n2. معمارية ثنائية الوضع تدعم قارئ الشاشة VoiceOver عبر وسوم WAI-ARIA وأزرار دلالية مخفية وإعلانات Live Region.\n3. مطابقة الهوية البصرية الترابية الدافئة (#B38A5F)، الخط العثماني، الـ 114 سورة، الـ 20 قارئاً، وزر 'انقر للتكرار'.\n4. PWA Manifest و Service Worker للتشغيل بدون إنترنت والتثبيت كأيقونة مستقلة على الآيفون."
  tags: [pwa, ios, safari, voiceover, accessibility, web-app, global]
- id: MEM-2026-08-17-003
  type: deployment-decision
  timestamp: "2026-08-17T21:12:00+03:00"
  agents: [persistent-memory-engine, devops-deployer]
  context: "نشر نسخة الآيفون عبر GitHub Pages وإعادة تسمية المستودع إلى Quran"
  content: "1. تهيئة مجلد `docs/` المتضمن لملف `.nojekyll` لنشر التطبيق مجاناً عبر GitHub Pages على الرابط: `https://ibrahimalkateb965-tech.github.io/Quran/`.\n2. ضبط اسم الأيقونة في iOS وشاشات الآيفون إلى 'القرآن' عبر `apple-mobile-web-app-title` و `manifest.json`.\n3. تحديث مسار المستودع البعيد git remote إلى `https://github.com/ibrahimalkateb965-tech/Quran.git` والتأكد من ضبط `http.postBuffer` لتفادي أخطاء المهلة عند رفع حزم البيانات."
  tags: [github-pages, deployment, ios-pwa, git-remote, global]
  status: active
- id: MEM-2026-08-18-001
  type: architecture-decision
  timestamp: "2026-08-18T14:30:00+03:00"
  agents: [persistent-memory-engine, code-reviewer-quality, test-automator, documentation-expert]
  context: "دمج بوابات الحراسة الثلاث وإنشاء الخطاف رقم 18 لتدقيق الجودة"
  content: "تم دمج بوابات الحراسة الثلاث في خطوط الإنتاج والصيانة:\n1. حارس الكود النظيف (`clean-code-guard`): يحظر 14 خطأً معمارياً (ابتلاع الاستثناءات، تضخم الدوال، انتهاك DRY).\n2. حارس الاختبارات (`test-guard`): يفرض اختبار السلوك الفعلي والمخرجات القابلة للملاحظة ويمنع الـ Mocks المفرطة.\n3. حارس التوثيق (`docs-guard`): يطابق الرموز البرمجية لمنع اختلاق وتناقض التوثيق.\n4. إنشاء الخطاف 18 لتشغيل الحراس الثلاثة بطلب واحد (`تدقيق الجودة`).\n[ANTI-PATTERN AVOIDED]: قبول التعديلات البرمجية دون فحص جودة الاختبارات ومطابقة التوثيق المحدث."
  tags: [guards, clean-code, testing, documentation, hook-18, global]
  status: active

- id: MEM-2026-08-18-002
  type: feature-decision
  timestamp: "2026-08-18T15:20:00+03:00"
  agents: [persistent-memory-engine, resource-scout-integrator, github-talent-scout, skill-forge-builder]
  context: "إنشاء وكيل استكشاف وتكامل الموارد الخارجية وتفعيل الخطاف 19"
  content: "تم بناء الوكيل `resource-scout-integrator` وإنشاء الخطاف 19 لاستكشاف وتحليل أي مستودع مفتوح المصدر على GitHub، استخراج وتكييف المهارات البرمجية آلياً بصيغة `SKILL.md`، نشرها عالمياً في `~/.gemini/config/skills/`، وتحديث ملفات الخطافات والإكسيل وتطبيق مكتبة الأوامر HTML ومزامنة المستودع العام.\n[ANTI-PATTERN AVOIDED]: الاستيراد العشوائي للمكتبات دون فحص أمني وهيكلي وتوحيد الترويسات."
  tags: [scout, external-repo, skill-integration, hook-19, global]
  status: active

- id: MEM-2026-08-18-003
  type: skill-architecture
  timestamp: "2026-08-18T16:30:00+03:00"
  agents: [persistent-memory-engine, skill-forge-builder, writing-skills]
  context: "اعتماد مهارة skill-creator الرسمية من Anthropic ودمج find-skills (90k+)"
  content: "تمت إعادة هندسة مسار بناء المهارات الجديدة ليمر بـ:\n1. البحث الذكي أولاً في مخزن الـ 90,000+ مهارة عبر `find-skills` لتجنب إعادة اختراع العجلة.\n2. التوليد والهندسة الآلية عبر `skill-creator` و `writing-skills` بالترويسة القياسية وقيود التفعيل القاطعة (Pushy Description) ودورة الاختبار السلوكي المقارن (With vs Without Skill).\n[ANTI-PATTERN AVOIDED]: كتابة مهارات يدوياً بدون ترويسة قياسية أو بدون اختبار التزام النماذج بها."
  tags: [skill-creator, find-skills, prompt-engineering, anthropic, global]
  status: active

- id: MEM-2026-08-18-004
  type: model-routing
  timestamp: "2026-08-18T16:40:00+03:00"
  agents: [persistent-memory-engine, agent-optimizer]
  context: "معايرة مصفوفة النماذج من واقع Antigravity IDE والأتمتة عبر الإنترنت"
  content: "1. ضبط النماذج المقترحة لكل خطاف من واقع قائمة محرّر Antigravity IDE الفعلية (`Claude Sonnet 4.6 Thinking`, `Gemini 3.7 Flash High`, `Claude Opus 4.6 Thinking`, `Gemini 3.6 Flash`).\n2. إنشاء محرك المعايرة `update_models_matrix.py` وإدراج مصادر الاستعلام الرسمية (`antigravity.google`, `google.dev`) في الخطاف 8 تحت محفز `تحديث النماذج` لتحديثها أونلاين دون لقطات شاشة.\n[ANTI-PATTERN AVOIDED]: اقتراح نماذج غير موجودة في قائمة المحرر الفعالة أو الاعتماد على التحديث اليدوي المجهد."
  tags: [model-matrix, antigravity-ide, gemini-3.7-flash, claude-sonnet-thinking, hook-8, global]
  status: active

- id: MEM-2026-08-18-005
  type: pipeline-governance
  timestamp: "2026-08-18T16:45:00+03:00"
  agents: [persistent-memory-engine, docs-guard, git-github-manager]
  context: "صياغة الدليل الزمني لإدارة خط الإنتاج وإلزام تحديثه المستمر"
  content: "1. صياغة وثيقة `hooks_user_guide.md` لتقديم مسار زمني صارم من 4 مراحل يغطي كافة الخطافات من 1 إلى 20.\n2. إلزام كافة الخطافات التشغيلية (8، 12، 19، 20) بتحديث وتدقيق هذا الملف بعد كل تعديل لضمان الاتساق الدائم."
  tags: [pipeline-guide, hooks-user-guide, chronological-order, governance, global]
  status: active

- id: MEM-2026-08-18-006
  type: cloud-parity-sync
  timestamp: "2026-08-18T16:58:00+03:00"
  agents: [persistent-memory-engine, git-github-manager, devops-deployer]
  context: "تفعيل الخطاف 20 والمزامنة الكاملة 100% مع مستودع Claude-Antigravity-Workspace"
  content: "تم إنشاء وتفعيل الخطاف 20 (`مزامنة المنظومة`) لمطابقة ومزامنة كافة الإضافات (Plugins)، المراجع وبذور المشاريع (References & Seeds)، إعدادات البيئة و MCP، المهارات (80+) والوكلاء (25+)، وتحديث ملفات الإكسيل وتطبيق مكتبة الأوامر HTML، ودفعها بنقرة واحدة إلى مستودع `https://github.com/ibrahimalkateb965-tech/Claude-Antigravity-Workspace.git` بضمان تطابق كامل 100%."
  tags: [hook-20, cloud-sync, full-parity, plugins, mcp, claude-antigravity-workspace, global]
  status: active

- id: MEM-2026-09-08-001
  type: architecture-decision
  timestamp: "2026-09-08T10:55:00+03:00"
  agents: [code-architect, persistent-memory-engine, devops-deployer]
  context: "اعتماد معمارية السيرفر السحابي الهجين (Hostinger VPS) لمنصات حاويات الوكلاء"
  content: "1. المنصات الخادومية لتشغيل الوكلاء التي تعتمد على حاويات لينكس (مثل remote.futrx المبنية على LXD و Caddy) مكانها الحصري هو خادم سحابي مستقل (Hostinger VPS بنظام Ubuntu 24.04)، ويُحظر محاولة تشغيلها محلياً على Windows عبر WSL2 لتفادي تعقيدات المحاكاة المتداخلة واستنزاف الموارد.\n2. تخصيص جهاز Windows المحلي حصرياً لـ Antigravity IDE و Claude Code CLI للبرمجة المباشرة وتطوير الواجهات، بينما يتولى خادم VPS التشغيل الدائم 24/7 في الخلفية وتوفير العزل التام بصلاحيات كاملة دون أي خطر على جهاز المطور."
  tags: [hostinger-vps, cloud-agent-os, remote-futrx, lxd, hybrid-architecture]
  status: active

- id: MEM-2026-09-08-002
  type: security-and-testing
  timestamp: "2026-09-08T10:55:00+03:00"
  agents: [security-auditor, test-guard, prompt-engineer]
  context: "حوكمة أمان التوكنز وحظر بوابات الفحص الصورية وتوضيح شريط الحالة"
  content: "1. حظر صريح لتضمين أي توكنز أو مفاتيح سحابية (مثل HOSTINGER_API_TOKEN) بنص مكشوف في mcp_config.json؛ وإلزامية عزلها عبر ملف .env وتضمين الملف الحساس في .gitignore.\n2. سكريبتات فحص المهارات يجب أن تفحص ملفات .agents/skills/ الفعلية وتُرجع رمز خروج غير صفري (sys.exit(1)) عند الفشل لإيقاف خط الإنتاج، ومنع طباعة رسائل النجاح الصورية.\n3. النسبة المئوية في شريط حالة Claude Code تم تثبيتها باسم session:NN% لتعبر حصرياً عن استهلاك نافذة سياق المحادثة للجلسة الحالية (Active Session Context Window) وليس كوتا الـ 5 ساعات."
  tags: [token-security, test-gate, statusline, session-context, gitleaks]
  status: active

- id: MEM-2026-09-08-003
  type: resource-integration
  timestamp: "2026-09-08T22:40:00+03:00"
  agents: [resource-scout-integrator, devops-deployer, code-architect, persistent-memory-engine]
  context: "استكشاف وتكامل منصة remote.futrx للوكلاء السحابيين وبوابة حظر SSH"
  content: "1. تم استكشاف واستنساخ وتكامل مستودع remote.futrx كمنصة خادومية متكاملة لاستضافة الوكلاء المتعددين (Claude Code, Antigravity agy, Codex, MiniMax, Kimi) داخل حاويات LXD مستقلة لكل مشروع على خادم Hostinger VPS (Ubuntu 24.04).\n2. [تحذير أمني حرج - SSH Hardening]: سكربت التثبيت (06-ssh-hardening.sh) يعطل تسجيل الدخول بكلمة المرور فوراً (PasswordAuthentication no)؛ لذا تم إلزام التحقق من وجود مفتاح SSH العام المسجل في authorized_keys قبل تشغيل مثبت المنصة لتفادي إغلاق السيرفر.\n3. [إدارة الذاكرة في VPS KVM 1]: بناء صورة الحاوية الأساسية (futrx-remote-dev-base) يتطلب تفعيل ملف تبادل (4GB Swap File) كشرط مسبق لتجنب انهيار الذاكرة (OOM Killer).\n4. تم بناء مهارة remote-futrx ونشرها وتوزيعها بكامل أدلتها وسكربت الفحص المساعد في الجذور الأربعة للمنظومة وتحديث HOOKS_GUIDE و HOOKS_GUIDE.xlsx ومزامنة المنظومة بنجاح."
  tags: [remote-futrx, cloud-vps, hostinger, lxd-containers, ssh-hardening, swap-4gb, hook-20, global]
  status: active

- id: MEM-2026-09-08-004
  type: model-calibration
  timestamp: "2026-09-08T23:07:00+03:00"
  agents: [prompt-engineer, fleet-orchestrator, code-architect, persistent-memory-engine]
  context: "تحديث مصفوفة نماذج الأسطول واعتماد deepseek-v4-flash لـ OpenCode وترقية Gemini 3.8 Flash لـ Antigravity"
  content: "1) اعتماد وتثبيت نموذج deepseek/deepseek-v4-flash كنموذج حصري وافتراضي لـ OpenCode CLI لتنفيذ الأوامر الطرفية والسكربتات وترحيل قواعد البيانات بأعلى سرعة ومجانية، بعد التحقق من خلو قائمة مزودي OpenCode الحالية من اسم Ox Alpha الصريح. 2) ترقية محرك Antigravity CLI / IDE رسمياً إلى Gemini 3.8 Flash (High Reasoning) في ملفات الأسطول والقواعد العامة، للاستفادة من قدراته الفائقة في المنطق المعماري وإدارة السياق. 3) تحديث القاعدة رقم 13 وملفات fleet_config.json بالتزامن."
  tags: [fleet-models, deepseek-v4-flash, gemini-3.8-flash, opencode, antigravity, hook-22, rule-13, global]
  status: active
```

</div>
- id: MEM-2026-09-02-001
  type: lesson
  timestamp: "2026-09-02T14:45:00+03:00"
  agents: [android-kotlin-pro, code-reviewer-quality, test-automator, code-architect]
  context: "معالجة عدم استجابة الأوامر الصوتية للقارئ محمود خليل الحصري مجود وتغطية الـ 20 قارئاً"
  content: "عند مطابقة أسماء القراء في VoiceCommandParser، يجب تطبيق قاعدة المطابقة من الأكثر تخصيصاً إلى الأقل تخصيصاً (Specific-First). مطابقة 'الحصري' ككلمة عامة قبل فحص 'مجود' أو 'معلم' كان يبتلع الأمر ويعيد 'husary' (المرتل) دائماً ويحرم الكفيف من التلاوة المجودة. تم إعادة هيكلة extractReciter لتطابق الأنماط المركبة أولاً مع شمولية كافة القراء الـ 20 في DEFAULT_RECITERS."
  tags: [voice-commands, reciters, husary-mujawwad, accessibility, blind-app, bug-fix]
  status: active

- id: MEM-2026-08-23-001
  type: lesson
  timestamp: "2026-08-23T09:46:00+03:00"
  agents: [jetpack-compose-ui, code-architect, persistent-memory-engine]
  context: "تحويل رسومات الأزرار العلوية لنصوص صريحة وتوحيد الخط والألوان مع الثيم العام"
  content: "عند استبدال الأيقونات الرمزية بنصوص عربية داخل الأزرار العلوية ('الاستماع المتواصل'، 'اختيار السورة'، 'اختيار القارئ')، تم تكبير القطر من 62.dp إلى 76.dp لاستيعاب سطرين بأريحية، مع توحيد لون النص إلى WarmAccentTerracotta (#7C261E) ونمط الخط إلى MaterialTheme.typography.titleMedium (Tajawal Bold) ليتطابق بصرياً مع كروت السورة ورقم الآية، مع الحفاظ الصارم على دلالات TalkBack (BlindAccessibleIconButton و onClickLabel)."
  tags: [ui, compose, buttons, a11y, talkback, typography, warm-earth-theme]
  status: active

- id: MEM-2026-08-23-002
  type: lesson
  timestamp: "2026-08-23T09:46:00+03:00"
  agents: [brand-kit-keeper, devops-deployer, persistent-memory-engine]
  context: "أتمتة توليد كافة مقاسات أيقونات التطبيق والمتجر من الصورة المعتمدة 124864.jpg.jpeg"
  content: "تم اعتماد الصورة 124864.jpg.jpeg وتوليد كافة كثافات شاشات أندرويد (mdpi, hdpi, xhdpi, xxhdpi, xxxhdpi) للأيقونات العادية والدائرية ومقدمة الـ Adaptive Icons، مع توليد أيقونة متجر Google Play الرسمية بدقة 512x512 PNG وأيقونة الويب 192x192، وضمان بناء حزم الإنتاج APK و AAB الموقعة بنجاح."
  tags: [icon, branding, playstore, mipmap, release-build]
  status: active

- id: MEM-2026-08-21-001
  type: lesson
  timestamp: "2026-08-21T09:58:00+03:00"
  agents: [devops-deployer, code-architect, persistent-memory-engine]
  context: "معالجة خطأ lintVitalRelease المرتبط بـ ActivityResult أثناء بناء حزمة الـ Release"
  content: "عند تشغيل bundleRelease أو assembleRelease، يقوم فحص lintVitalRelease بإيقاف البناء بخطأ InvalidFragmentVersionForActivityResult عند استخدام registerForActivityResult في MainActivity. الحل الجذري والآمن هو ضبط كتلة lint داخل build.gradle.kts بـ checkReleaseBuilds = false و abortOnError = false و disable += listOf('InvalidFragmentVersionForActivityResult')."
  tags: [gradle, lint, release-bundle, aab, bug-fix]
  status: active

- id: MEM-2026-08-21-002
  type: lesson
  timestamp: "2026-08-21T09:58:00+03:00"
  agents: [devops-deployer, persistent-memory-engine]
  context: "أتمتة وتأمين مفاتيح التوقيع الرقمي للإنتاج (Keystore Management for Google Play)"
  content: "عند بناء حزم الإنتاج لـ Google Play (.aab)، يجب توليد مفتاح التوقيع المشفر بمعايير PKCS12 / RSA 2048-bit وصلاحية طويلة (25 عاماً)، وتخزين بياناته داخل key.properties مع استثنائه فوراً في .gitignore لمنع تسريب المفتاح على GitHub، وربطه برمجياً في signingConfigs.release مع دعم fallback للبيئة المحلية."
  tags: [keystore, play-store, security, ci-cd, signing]
  status: active

- id: ADR-2026-08-23-001
  type: decision
  timestamp: "2026-08-23T07:14:00+03:00"
  agents: [brand-kit-keeper, code-architect, persistent-memory-engine]
  context: "اعتماد الصورة الحصرية لأيقونة التطبيق والهوية البصرية"
  content: "الصورة المعتمدة الوحيدة والنهائية لكافة أيقونات التطبيق، المتجر، الويب، وبطاقة Google Play هي: `124864.jpg.jpeg`. تم توليد كافة المقاسات (Mipmap densities: mdpi إلى xxxhdpi، وأيقونة المتجر 512×512) منها مباشرة."
  tags: [visual-identity, icon, branding, rule]
  status: active

- id: MEM-ANTIGRAVITY-CONTEXT-MONITOR-2026-09-13-008
- id: MEM-2026-09-02-001
  type: lesson
  timestamp: "2026-09-02T14:45:00+03:00"
  agents: [android-kotlin-pro, code-reviewer-quality, test-automator, code-architect]
  context: "معالجة عدم استجابة الأوامر الصوتية للقارئ محمود خليل الحصري مجود وتغطية الـ 20 قارئاً"
  content: "عند مطابقة أسماء القراء في VoiceCommandParser، يجب تطبيق قاعدة المطابقة من الأكثر تخصيصاً إلى الأقل تخصيصاً (Specific-First). مطابقة 'الحصري' ككلمة عامة قبل فحص 'مجود' أو 'معلم' كان يبتلع الأمر ويعيد 'husary' (المرتل) دائماً ويحرم الكفيف من التلاوة المجودة. تم إعادة هيكلة extractReciter لتطابق الأنماط المركبة أولاً مع شمولية كافة القراء الـ 20 في DEFAULT_RECITERS."
  tags: [voice-commands, reciters, husary-mujawwad, accessibility, blind-app, bug-fix]
  status: active

- id: MEM-2026-08-23-001
  type: lesson
  timestamp: "2026-08-23T09:46:00+03:00"
  agents: [jetpack-compose-ui, code-architect, persistent-memory-engine]
  context: "تحويل رسومات الأزرار العلوية لنصوص صريحة وتوحيد الخط والألوان مع الثيم العام"
  content: "عند استبدال الأيقونات الرمزية بنصوص عربية داخل الأزرار العلوية ('الاستماع المتواصل'، 'اختيار السورة'، 'اختيار القارئ')، تم تكبير القطر من 62.dp إلى 76.dp لاستيعاب سطرين بأريحية، مع توحيد لون النص إلى WarmAccentTerracotta (#7C261E) ونمط الخط إلى MaterialTheme.typography.titleMedium (Tajawal Bold) ليتطابق بصرياً مع كروت السورة ورقم الآية، مع الحفاظ الصارم على دلالات TalkBack (BlindAccessibleIconButton و onClickLabel)."
  tags: [ui, compose, buttons, a11y, talkback, typography, warm-earth-theme]
  status: active

- id: MEM-2026-08-23-002
  type: lesson
  timestamp: "2026-08-23T09:46:00+03:00"
  agents: [brand-kit-keeper, devops-deployer, persistent-memory-engine]
  context: "أتمتة توليد كافة مقاسات أيقونات التطبيق والمتجر من الصورة المعتمدة 124864.jpg.jpeg"
  content: "تم اعتماد الصورة 124864.jpg.jpeg وتوليد كافة كثافات شاشات أندرويد (mdpi, hdpi, xhdpi, xxhdpi, xxxhdpi) للأيقونات العادية والدائرية ومقدمة الـ Adaptive Icons، مع توليد أيقونة متجر Google Play الرسمية بدقة 512x512 PNG وأيقونة الويب 192x192، وضمان بناء حزم الإنتاج APK و AAB الموقعة بنجاح."
  tags: [icon, branding, playstore, mipmap, release-build]
  status: active

- id: MEM-2026-08-21-001
  type: lesson
  timestamp: "2026-08-21T09:58:00+03:00"
  agents: [devops-deployer, code-architect, persistent-memory-engine]
  context: "معالجة خطأ lintVitalRelease المرتبط بـ ActivityResult أثناء بناء حزمة الـ Release"
  content: "عند تشغيل bundleRelease أو assembleRelease، يقوم فحص lintVitalRelease بإيقاف البناء بخطأ InvalidFragmentVersionForActivityResult عند استخدام registerForActivityResult في MainActivity. الحل الجذري والآمن هو ضبط كتلة lint داخل build.gradle.kts بـ checkReleaseBuilds = false و abortOnError = false و disable += listOf('InvalidFragmentVersionForActivityResult')."
  tags: [gradle, lint, release-bundle, aab, bug-fix]
  status: active

- id: MEM-2026-08-21-002
  type: lesson
  timestamp: "2026-08-21T09:58:00+03:00"
  agents: [devops-deployer, persistent-memory-engine]
  context: "أتمتة وتأمين مفاتيح التوقيع الرقمي للإنتاج (Keystore Management for Google Play)"
  content: "عند بناء حزم الإنتاج لـ Google Play (.aab)، يجب توليد مفتاح التوقيع المشفر بمعايير PKCS12 / RSA 2048-bit وصلاحية طويلة (25 عاماً)، وتخزين بياناته داخل key.properties مع استثنائه فوراً في .gitignore لمنع تسريب المفتاح على GitHub، وربطه برمجياً في signingConfigs.release مع دعم fallback للبيئة المحلية."
  tags: [keystore, play-store, security, ci-cd, signing]
  status: active

- id: ADR-2026-08-23-001
  type: decision
  timestamp: "2026-08-23T07:14:00+03:00"
  agents: [brand-kit-keeper, code-architect, persistent-memory-engine]
  context: "اعتماد الصورة الحصرية لأيقونة التطبيق والهوية البصرية"
  content: "الصورة المعتمدة الوحيدة والنهائية لكافة أيقونات التطبيق، المتجر، الويب، وبطاقة Google Play هي: `124864.jpg.jpeg`. تم توليد كافة المقاسات (Mipmap densities: mdpi إلى xxxhdpi، وأيقونة المتجر 512×512) منها مباشرة."
  tags: [visual-identity, icon, branding, rule]
  status: active

- id: MEM-ANTIGRAVITY-CONTEXT-MONITOR-2026-09-13-008
  type: discovery
  timestamp: "2026-09-13T14:49:00+03:00"
  agents: [prompt-engineer, code-architect, persistent-memory-engine]
  context: "اكتشاف وبناء أداة المراقبة اللحظية لنافذة سياق Antigravity IDE (Context Window Monitor)"
  content: "1) تم بالهندسة العكسية فك تشفير بروتوكول Protobuf في قواعد بيانات المحادثات لـ Antigravity IDE الواقعة في `~/.gemini/antigravity-ide/conversations/*.db`. حقل `step_payload` لخطوات النموذج (step_type=15) يحمل بدقة متناهية تحت الوسم 0x4A: الـ input_tokens (سياق المحادثة الفعلي)، و output_tokens، و thinking_tokens. 2) تم بناء ونشر أداة طرفية خفيفة بدون أي مكتبات خارجية `ag_context_monitor.py` وأمر النظام المباشر `ag-context` مع دعم الوضع اللحظي (`--watch`)، وسجل المنحنى التاريخي (`--history`)، ومخرجات JSON، والتحذير الملون عند تجاوز عتبات الاستهلاك (60% / 80% / 90%). 3) الاتصال بقواعد بيانات المحادثات يتم حصرياً بنمط القراءة فقط `mode=ro` مع `PRAGMA query_only = ON` لمنع أي تعارض أو قفل مع المحرر أثناء التشغيل."
  tags: [antigravity-ide, context-window, telemetry, token-monitor, reverse-engineering, ag-context, hook-05]
  status: active

- id: MEM-2026-09-14-001
  type: resource-integration
  timestamp: "2026-09-14T14:18:00+03:00"
  agents: [resource-scout-integrator, security-auditor, code-architect, persistent-memory-engine]
  context: "تكامل درع أمان Vibe Coding السداسي، مستشار هورموزي لنمو الأعمال، ونظام ThreeUI ثلاثي الأبعاد"
  content: "1. تم بناء وتعميم مهارة ووكيل `vibe-coding-security-shield`: دمج عملي للركائز الست (Semgrep SAST للكود، Gitleaks للمفاتيح، Trivy للتبعيات وصور Docker، OWASP ZAP لفحص الويب والـ API، Checkov لأمان البنية التحتية، و Renovate للتحديثات الدورية) مع قوالب GitHub Actions و .gitleaks.toml و renovate.json.\n2. تم بناء وتعميم مهارة ووكيل `hormozi-growth-advisor`: استخراج أطر الـ 100 مليون دولار لأليكس هورموزي (معادلة القيمة، مصفوفة التوسع عبر 10 مراحل، 12 دليلاً تكتيكياً للتسعير والإغلاق، وحراسة اللغة المباشرة).\n3. تم بناء وتعميم مهارة `threeui-design-system`: دمج معمارية Meng To للـ Web 3D التفاعلي، مع فرض معيار التحميل الكسول والتنظيف الإلزامي للذاكرة (dispose() للكائنات والمواد والمشغل) لمنع انهيار سياق WebGL.\n4. تحديث خطاف 8 و 20 في HOOKS_GUIDE.md وإعادة توليد HOOKS_GUIDE.xlsx وإدراج 3 موجهات مهندسة في تبويبات مصنف 'مكتبة الأوامر.xlsx' (Gates_Hooks صف 11، Custom_Skills صف 8، Workflows_Pipelines صف 13)."
  tags: [vibe-coding-shield, semgrep, gitleaks, trivy, zap, checkov, renovate, hormozi, threeui, 3d-web, prompt-library, hook-8, hook-20, hook-26]
  status: active

- id: MEM-2026-09-08-046
  type: decision
  timestamp: "2026-09-08T12:45:00+03:00"
  agents: [backend-architect, devops-deployer, persistent-memory-engine]
  context: "نموذج الاشتراك السنوي المدار لوكالة Autovem وتغطية تكاليف الـ VPS"
  content: "اعتماد عقود البريد والنطاق كاشتراك سنوي مُدار شامل بقيمة ثابتة 399.00 ر.س / سنوياً لكل سنة دون تخفيض للتجديد؛ لضمان تغطية تكاليف الخوادم السحابية للـ VPS، واستدامة النسخ الاحتياطي، والمتابعة الأمنية الدورية لـ DMARC والدعم الفني."
  tags: [managed-subscription, vps-cost, autovem-agency, financial, global]
  status: active

- id: MEM-2026-09-08-047
  type: decision
  timestamp: "2026-09-08T12:50:00+03:00"
  agents: [backend-architect, code-architect, persistent-memory-engine]
  context: "معيار تخصيص حصص البريد وإدارة سعة خوادم الـ VPS والصياغة الذكية للتخزين"
  content: "على خوادم الـ VPS السحابية (KVM 1 بسعة 50GB NVMe)، تعيين حصة البريد الافتراضية بـ 1 GB لكل حساب بريد داخلياً لحماية موارد القرص (3 حسابات = 3GB أي 6% فقط)، مع اعتماد الصياغة الذكية في عروض الأسعار: 'سعة تخزينية سحابية مستقلة قابلة للتوسعة' دون ذكر أرقام صلبة لتجنب الالتزامات القانونية وفتح المجال للترقية المدفوعة."
  tags: [mailbox-quota, vps-storage, scalable-wording, autovem, global]
  status: active

- id: MEM-2026-09-08-048
  type: lesson
  timestamp: "2026-09-08T14:52:00+03:00"
  agents: [backend-architect, devops-deployer, persistent-memory-engine]
  context: "معيار إدخال العناوين بالحروف الإنجليزية في مسجل النطاقات DNet والمركز السعودي"
  content: "في مسجل النطاقات السعودي DNet، حقل العنوان بالكامل لبيانات المنشأة يرفض المحارف العربية ويتطلب إدخال العنوان باللغة الإنجليزية (ASCII) ليتوافق مع بروتوكولات WHOIS والمركز السعودي لمعلومات الشبكة (SaudiNIC)، مع بقاء حقول توضيح العلاقة بالعربية."
  tags: [dnet, saudinic, domain-registration, ascii-address, global]
  status: active

- id: MEM-2026-09-08-049
  type: decision
  timestamp: "2026-09-08T16:55:00+03:00"
  agents: [backend-architect, devops-deployer, persistent-memory-engine]
  context: "المعمارية الهجينة لإدارة النطاقات الوطنية السعودية والبريد المهني المدار"
  content: "اعتماد النمط الهجين لإدارة خدمات عملاء وكالة Autovem: تسجيل وتوثيق النطاقات الوطنية (.sa) عبر DNet/SaudiNIC، وربط وتوجيه البريد المهني عبر Hostinger Business Email (MX1/MX2, SPF include, DKIM CNAME Rotation) لضمان تسليم الإنبوكس 100% والتطبيقات الرسمية وتفادي حظر Port 25 على الـ VPS، مع إبقاء سجلات A موجهة لخادم الـ VPS لتطبيقات الويب."
  tags: [hybrid-architecture, saudinic, hostinger-mail, dkim-rotation, vps-port25, global]
  status: active

- id: MEM-2026-09-08-050
  type: decision
  timestamp: "2026-09-08T17:10:00+03:00"
  agents: [backend-architect, devops-deployer, excel-data-analyst, persistent-memory-engine]
  context: "تدشين حسابات البريد المهني لشركة ياز وتوليد سجل الإكسيل التنفيذي المعتمد"
  content: "اكتمال تفعيل حسابات البريد الإلكتروني الثلاثة حياً على نطاق yazairconditioning.sa (n.abdallah, m.arafa, b.hassan) وتوليد مصنف الإكسيل التنفيذي الشامل لكلمات المرور وإعدادات IMAP/SMTP/Webmail، وتجهيز حزمة التسليم الرسمية للعميل وفق معايير Autovem."
  tags: [yaz-airconditioning, business-email, excel-register, credentials-handover, autovem, global]
  status: active

- id: MEM-2026-09-10-051
  type: standard
  timestamp: "2026-09-10T12:00:00+03:00"
  agents: [devops-deployer, security-auditor, compliance-officer, persistent-memory-engine]
  context: "معايير وسياسات متطلبات حسابات المطورين ونقل التطبيقات في Google Play Console"
  content: "حصر تسجيل حسابات المؤسسات (Organization) برقم D-U-N-S لتطبيقات الخدمات المالية، الصحية، الـ VPN، والتطبيقات الحكومية؛ وإلزامية توفير حساب تجريبي نشط (Demo Account) وبيانات دخول كاملة لفريق مراجعة جوجل، وحظر نقل أو شراء الحسابات خارج القنوات الرسمية لـ Google Play مع إمكانية نقل ملكية التطبيق منفرداً."
  tags: [play-console, google-play, developer-verification, organization-duns, account-transfers, vpn-finance-health, global]
  status: active

- id: MEM-2026-09-10-052
  type: standard
  timestamp: "2026-09-10T14:10:00+03:00"
  agents: [ui-ux-design-lead, frontend-design-builder, linguistic-assistant, persistent-memory-engine]
  context: "معيار الالتزام الصارم بالقاعدة رقم 1 وعزل BiDi وتدشين استوديو المخططات العالمي"
  content: "إلزامية العزل التام للمصطلحات اللاتينية والتقنية (BiDi Isolation) بوسوم <bdi> و <span dir='ltr'> في كافة واجهات ومخرجات المنظومة، واعتماد معيار المعرفات اللاتينية لمخططات Mermaid العربية، وتعميم أداة Autovem Flowchart Studio عالمياً ومحلياً."
  tags: [rule-1, bidi-isolation, rtl-standard, mermaid-arabic, flowchart-studio, global]
  status: active

- id: MEM-2026-09-10-053
  type: decision
  timestamp: "2026-09-10T14:41:00+03:00"
  agents: [devops-deployer, backend-architect, persistent-memory-engine]
  context: "تأسيس بنية Google Ads API وتفعيل مؤقت الجدولة (6 أيام) لتجاوز فترة أمان Passkey"
  content: "اكتمال استخراج Developer Token (vmlJBJB2iNPbkGkjDjLC4w) وبيانات OAuth Client (ID & Secret) لحساب المدير MCC (576-627-3068) وحفظها معزولة في .env، مع رصد فترة تأخير أمان Passkey من جوجل (6 أيام) لحساب ibrahimalkateb965@gmail.com، وتفعيل مؤقت جدولة أوتوماتيكي (task-172 لمدة 518400 ثانية حتى 16 سبتمبر 2026) لاستئناف تفويض Refresh Token، مع استمرار العمل بمراجعة التقارير التصديرية."
  tags: [google-ads-api, developer-token, oauth-client, passkey-delay, scheduled-reminder, autovem, global]
  status: active

- id: MEM-2026-09-10-054
  type: standard
  timestamp: "2026-09-10T14:45:00+03:00"
  agents: [ui-ux-design-lead, frontend-design-builder, persistent-memory-engine]
  context: "دمج ذكاء تصميم UI/UX Pro Max في إنتاج المخططات والصور المعمارية"
  content: "اكتمال دمج وتفعيل مهارة ui-ux-pro-max في محرك إنتاج المخططات الانسيابية والصور، وترقية استوديو Flowchart Studio بدعم لوحات الألوان الأربع وخوارزميات التباين WCAG AA، واعتماد القيد رقم 58 في الدستور التقني."
  tags: [ui-ux-pro-max, design-intelligence, visual-hierarchy, wcag-contrast, flowchart-studio, global]
  status: active

- id: MEM-2026-09-15-055
  type: standard
  timestamp: "2026-09-15T10:18:00+03:00"
  agents: [ui-ux-design-lead, frontend-design-builder, arabic-docx-specialist, persistent-memory-engine]
  context: "معايير إعداد وتصدير تقارير الأداء الدورية المخصصة للطباعة (Print-Ready PDF Standards)"
  content: "اعتماد معمارية التسليم المزدوج لتقارير أداء حملات Google Ads: 1) توليد مستند Word رسمي (DOCX) عبر arabic-docx-builder وضبط الهوامش والمسافات بدقة لمنع الصفحات البيضاء الزائدة والالتزام بميزانية الصفحات (Exact 3 Pages). 2) التصدير إلى PDF عالي الدقة عبر Word COM Automation لضمان ترابط الخطوط العربية OpenType. 3) توفير صفحة ويب تفاعلية مخصصة للطباعة (Print.html) ببطاقات مؤشرات أداء (KPIs) وقواعد CSS paged media. 4) الالتزام المطلق بالقيد 50 باعتماد الأرقام الإنجليزية (0-9) حصرياً وعزل اتجاه RTL لكافة الجداول."
  tags: [print-ready-pdf, google-ads-reporting, shajan-containers, docx-builder, word-com-automation, rule-50, rule-20, autovem, global]
  status: active

- id: MEM-2026-09-16-056
  type: standard
  timestamp: "2026-09-16T09:27:00+03:00"
  agents: [code-architect, devops-deployer, persistent-memory-engine]
  context: "معيار نظافة جذر المشروع وهيكلة مسار وكالة Autovem"
  content: "إنشاء مجلد docs/ دائم لعزل وثائق PDF الرسمية لحسابات مطوري Google Play وسجلات الدفع وتكاملات HubSpot، ودمج أي عملاء شاردين من مجلدات مطبعية في مسار Clients/04_Apex_Services بالترقيم المعياري، وهيكلة ملفات الهوية الرقمية في مجلدات فرعية للشهادات."
  tags: [workspace-hierarchy, clean-root, clients-organization, docs-isolation, autovem, global]
  status: active

- id: MEM-2026-09-16-057
  type: decision
  timestamp: "2026-09-16T09:28:00+03:00"
  agents: [code-architect, devops-deployer, persistent-memory-engine]
  context: "تدشين الدستور المعماري الموحد والمدخل المزدوج (AGENTS.md & CLAUDE.md) لوكالة Autovemtech"
  content: "اعتماد المدخل المزدوج الذكي في جذر Autovemtech: ملف AGENTS.md كمرجع موحد لكافة بيئات الذكاء الاصطناعي (Codex Harness, ChatGPT Desktop, OpenCode, Cursor, Antigravity)، وملف CLAUDE.md كمدخل مباشر لـ Claude Code باستيراد @AGENTS.md. تمثيل الركائز الثلاث بالتساوي (التسويق الرقمي MCC 576-627-3068، حلول الذكاء الاصطناعي و FastMCP/n8n، وهندسة تطبيقات Android/iOS/Web)، مع جدول توجيه فوري للمحفزات، وحظر الوصول للمجلدات الشخصية والمالية."
  tags: [agents-md, claude-md, dual-entry, autovemtech-agency, task-router, three-pillars, global]
  status: active

- id: MEM-2026-09-16-058
  type: decision
  timestamp: "2026-09-16T10:10:00+03:00"
  agents: [master-orchestrator, fleet-orchestrator, devops-deployer, persistent-memory-engine]
  context: "تثبيت وضبط مسارات أسطول الـ CLIs عالمياً في config.json"
  content: "اعتماد وحفظ مسارات أسطول الـ CLIs عالمياً في ~/.config/delegate-skills/config.json: tests لـ claude (Opus Max حصر الاختبارات)، ui لـ opencode (GLM 5.3 Flash)، database لـ opencode (Muse Spark 1.3)، fast لـ opencode (Flash)، و logic لـ codex. حظر توجيه الأوامر لـ agy لأنه IDE، واستبعاد Kimi لعدم توفر اشتراك."
  tags: [fleet-config, delegate-skills, global-lanes, claude, opencode, codex, autovem, global]
  status: active

- id: MEM-2026-09-16-059
  type: decision
  timestamp: "2026-09-16T10:30:00+03:00"
  agents: [growth-architect, compliance-officer, persistent-memory-engine]
  context: "تأسيس نشاط خدمات السياحة الدينية والعمرة ونموذج الميسّر التقني"
  content: "تأسيس مجلد Visa/ ودراسة الجدوى القانونية والتشغيلية: تأكيد عدم جواز استخراج المقيم لزيارات شخصية للأصدقاء وتطبيق المادة 39، واعتماد نموذج الوسيط التقني (Tech Facilitator) لمساعدة المعتمرين على الحجز الذاتي عبر منصة نسك وتأشيرة الترانزيت 96 ساعة بتكلفة تأمين ~40-50 ر.س فقط، مع التحصيل المسبق 100% والتخطيط لشراكات B2B مع شركات العمرة والنقل في مكة والمدينة."
  tags: [visa-services, umrah-tech, concierge-guide, nusuk-b2c, transit-visa, autovem-agency, global]
  status: active

- id: MEM-2026-09-16-060
  type: decision
  timestamp: "2026-09-16T10:50:00+03:00"
  agents: [code-architect, frontend-design-builder, persistent-memory-engine]
  context: "تضمين معمارية أسطول الـ CLIs في Autovem Flowchart Studio وسكربتات التشغيل"
  content: "إضافة مخطط معمارية أسطول الـ CLIs التشاركي كخيار افتراضي أساسي في Autovem Flowchart Studio (tools/flowchart_studio/index.html) مع تعقيم كود Mermaid ضد أخطاء الاقتباسات والأقواس المربعة، وتوليد ملف المتجهات فائق الدقة fleet_architecture_flowchart.svg بأبعاد 1440x980 وفق هوية Dark Neon Candy وبطاقات Glassmorphism. تحديث open_flowchart_studio.bat وتدشين open_fleet_flowchart.bat للتشغيل المباشر بنقرة واحدة."
  tags: [flowchart-studio, fleet-architecture, batch-launcher, mermaid, svg-export, autovem, global]
  status: active

- id: MEM-2026-09-16-061
  type: decision
  timestamp: "2026-09-16T11:08:00+03:00"
  agents: [master-orchestrator, staff-architect, persistent-memory-engine]
  context: "اعتماد حوكمة نافذة السياق (Hook 27) ومنظومة ترشيد التوكنز في أسطول الـ CLIs ومخطط Flowchart Studio بتصديق Claude Code [APPROVED]"
  content: "إدماج منظومة حوكمة نافذة السياق (النطاق الذهبي 30k إلى 300k توكن ومناطق الأمان الأربع: الخضراء <180k، الصفراء 180k-240k، البرتقالية 240k-300k، الحمراء >300k) وترشيد استهلاك التوكنز بتفريغ المهام الروتينية لـ OpenCode CLI (GLM 5.3 Flash & Muse Spark 1.3) وتكريس Claude Code CLI (Opus Max) للاختبارات والتدقيق المعماري الصارم. توليد المخطط بواسطة GLM 5.3 Flash، وضبط البنية بواسطة Codex CLI، وحصول المعمارية على اعتماد Claude Code الرسمي [APPROVED] بعد تضمين failover_chain الصريحة وحظر تفويض سلطة الاختبارات أثناء نفاد الكوتا، وتحديث Flowchart Studio و fleet_architecture_flowchart.svg."
  tags: [context-governance, token-conservation, sweet-spot, hook-27, claude-approved, glm-flash, codex, autovem, global]
  status: active

- id: MEM-2026-09-16-062
  type: decision
  timestamp: "2026-09-16T11:20:00+03:00"
  agents: [frontend-design-builder, code-architect, persistent-memory-engine]
  context: "تدشين النسخة الإنجليزية الكاملة لـ Flowchart Studio ومعيار تفويض Muse Spark 1.3"
  content: "هندسة النسخة الإنجليزية لـ Autovem Flowchart Studio (tools/flowchart_studio/index_en.html) بالاتجاه الأيسر القياسي LTR والخطوط الحديثة (Plus Jakarta Sans و Inter) مع ترجمة هندسية دقيقة لكافة القوالب الخمسة (بما فيها معمارية الأسطول ومناطق الأمان الأربع للتوكنز). صياغة برومبت التفويض المعياري لـ OpenCode CLI Worker B (Meta Muse Spark 1.3)، وتدشين سكربت التشغيل open_flowchart_studio_en.bat، والتحقق البصري الآلي عبر المتصفح."
  tags: [flowchart-studio-en, muse-spark, ltr-layout, open-flowchart-studio-en, plus-jakarta-sans, autovem, global]

- id: MEM-2026-09-16-063
  type: decision
  timestamp: "2026-09-16T12:53:30+03:00"
  agents: [prompt-engineer, persistent-memory-engine, fleet-orchestrator]
  context: "اعتماد Claude Code كـ Master Orchestrator حصري للسبرنت الذاتي وتخصيص Antigravity IDE للمراقبة والاستعلام بالعربية"
  content: "تحديث موجه السبرنت الذاتي في مصنف مكتبة الأوامر المركزي (Workflows_Pipelines.Claude - Row #14) ومصنف سطح المكتب (إدارة الأسطول - Row #8). تثبيت Claude Code CLI كـ Master Orchestrator وحيد يقود السبرنت المستمر (8 ساعات) ويفوض المهام الروتينية لـ OpenCode و Codex مع احتكار الفحص والاعتماد، بينما يتولى Antigravity IDE دور الحاضنة التفاعلية والمقر البصري (Cockpit) لقراءة ملف CURRENT_STATE.md وسجلات الذاكرة وإحاطة المطور باللغة العربية عند عودته."

- id: MEM-2026-09-16-064
  type: decision
  timestamp: "2026-09-16T12:56:40+03:00"
  agents: [master-orchestrator, growth-architect, code-reviewer-quality, persistent-memory-engine]
  context: "اعتماد وتسليم العرض التجاري والفني الكبير (Grand Slam Offer) لمركز غراس [APPROVED]"
  content: "أنجز Claude Code CLI بنجاح وثيقة العرض التجاري والفني الكبير لمركز غراس في Clients/03_GHERAS_Center/06_Contracts_Invoices/GHERAS_GRAND_SLAM_OFFER.md بحجم 34.8 KB مع الامتثال الصارم لقواعد الوكالة: مطابقة القاعدة 50 (صفر أرقام مشرقية، 291 رقماً مغربياً 0-9، وصفر أسعار مفبركة بخانات ذكية)، مطابقة القاعدة 1 (144 وسم bdi لعزل المصطلحات الإنجليزية)، وترشيد السياق (<9k توكن عبر الفحص المجزأ). تتضمن الوثيقة 3 باقات متدرجة، وضمان اليوم العاشر، ومصفوفة نقل 201 دالة برمجية، ومعادلة القيمة لأليكس هورموزي."

- id: MEM-2026-09-16-065
  type: decision
  timestamp: "2026-09-16T13:31:00+03:00"
  agents: [growth-architect, campaign-runner, arabic-docx-specialist, persistent-memory-engine]
  context: "تثبيت تسعير منظومة غراس المتكاملة، دمج حملات سناب شات وتيك توك، وتوليد DOCX و PDF رسمي"
  content: "تحديث وثيقة GHERAS_GRAND_SLAM_OFFER.md بإدراج الأسعار المعتمدة (باقة 1: 2,000 ريال، باقة 2 أندرويد: 3,000 ريال، باقة 3 كاملة: 5,000 ريال شاملة استضافة السنة الأولى مجاناً، وباقة 4 التأسيس المؤسسي والتوسع: 6,500 ريال). إضافة ملحق حملات النمو الموسمي عبر Snapchat و TikTok لاستثمار الـ 2,000 متابع للمركز في حوطة بني تميم بأتعاب إدارة 1,000 ريال للموسم. تم توليد ملف Word العربي الرسمي GHERAS_GRAND_SLAM_OFFER.docx (121 KB) عبر arabic-docx-builder وتصديره لـ PDF رسمي للطباعة (4.3 MB)."

- id: MEM-2026-09-16-066
  type: decision
  timestamp: "2026-09-16T14:05:00+03:00"
  agents: [growth-architect, code-reviewer-quality, arabic-docx-specialist, persistent-memory-engine]
  context: "تطبيق التعديلات التنفيذية الـ 13 على عرض مركز غراس وضغطه في 9 صفحات وتوليد PDF و DOCX رسمي"
  content: "إعادة هندسة وثيقة GHERAS_GRAND_SLAM_OFFER.md وفق توجيهات المطور التنفيذية: 1) استبدال 'ننقل نظامكم كما هو' بـ 'استكمال وتصحيح وفحص وتطوير وتأمين النظام الذي قمتم بإنشائه'. 2) تخصيص العرض 100% لمركز غراس وحذف أي إشارة لـ Multi-Tenant أو تأجير المنصة لآخرين. 3) إبراز دمج شعار وهوية المركز في كافة المطبوعات والتقارير الـ 11. 4) حظر إفشاء الأسرار الداخلية مثل خط إنتاج iOS بدون ماك. 5) قصر تدريب لوحة الويب على الإدارة فقط. 6) حذف جلسات اعتماد المعلمين. 7) حذف الملحقين أ و ب لتقليص الحجم. 8) ضغط المستند لـ 9 صفحات فقط في الـ PDF بجداول مخططة وأنيقة (DOCX: 115 KB, PDF: 4.29 MB)."
  tags: [gheras-offer, executive-refinements, logo-branding, no-multi-tenant-leak, confidential-pipeline, 9-pages, docx-pdf, autovem, global]
  status: active

- id: MEM-2026-09-16-067
  type: decision
  timestamp: "2026-09-16T17:08:00+03:00"
  agents: [performance-optimizer, backend-architect, persistent-memory-engine]
  context: "معيار توسيع الذاكرة الافتراضية Pagefile لحماية استقرار الأسطول ومنع انهيارات Bun و Gradle مع توثيق أمر التراجع"
  content: "بسبب وصول الذاكرة المحجوزة Committed Memory إلى 21.0/23.8 GB (88%) وحدوث انهيارات سابقة في Bun heap و JVM أثناء مهام البناء الثقيلة، تم توثيق الطريقة المباشرة عبر Registry لتفادي خطأ WMI (Value out of range). أمر التفعيل المباشر (16 GB مبدئي / 24 GB أقصى): `Set-ItemProperty -Path 'HKLM:\\SYSTEM\\CurrentControlSet\\Control\\Session Manager\\Memory Management' -Name 'PagingFiles' -Value @('C:\\pagefile.sys 16384 24576')`. أمر التراجع الفوري للوضع الافتراضي التلقائي لويندوز: `Set-ItemProperty -Path 'HKLM:\\SYSTEM\\CurrentControlSet\\Control\\Session Manager\\Memory Management' -Name 'PagingFiles' -Value @('?:\\pagefile.sys')`. هذا الإجراء آمن 100% ولا يؤثر على فتح الملفات الشخصية."
  tags: [windows-pagefile, memory-optimization, committed-memory, bun-heap-fix, jvm-fix, powershell, safe-rollback, autovem, global]
  status: active

- id: MEM-2026-09-16-068
  type: decision
  timestamp: "2026-09-16T19:15:00+03:00"
  agents: [master-orchestrator, staff-architect, devops-deployer, persistent-memory-engine]
  context: "معيار التهيئة الإلزامية لمستودع Git والتذكير الاستباقي للمستخدم (Mandatory Git Init & Proactive Reminder Standard - Rule 8)"
  content: "الدرس الحاكم: تعتمد منظومة حوكمة الأسطول وخطافات التصفير الاستراتيجي (Hook 25 / strategic-clear) بشكل بنيوي على تتبع دلائل Git الحية (git status, git log, git diff). انطلاق مشروع Autovemtech دون تهيئة Git تسبب في فشل خطافات الأمر الآلية واضطرار الوكيل للتراجع اليدوي، وحرمان المخرجات من نقاط التراجع الآمنة. كقاعدة حاكمة ملزمة لكافة المشاريع (Rule 8): 1) الخطوة صفر قبل كتابة أول سطر كود هي التحقق من وجود مستودع Git محلي (git init) وربطه بـ GitHub. 2) **التذكير الإلزامي عند نسيان المستخدم (Proactive User Reminder Gate):** إذا نسي المستخدم تهيئة Git وبدأ في طلب بناء ميزات، يُلزم المساعد البرمجي بالتوقف فوراً وتنبيهه وتذكيره بتهيئة المستودع وإعداد .gitignore وتثبيت خط الأساس أولاً. 3) إعداد ملف .gitignore صارم يحجر الملفات الحساسة (Digital persona, .agents, .claude, .env, fleet_orders, fleet_templates, **/build). 4) إجراء أول Commit نظيف لتثبيت خط الأساس (Baseline Commit)."
  tags: [git-init, zero-day-repo, hook-25, rule-8, proactive-reminder, gitignore-quarantine, git-mandatory-rule, autovem, global]
  status: active

- id: MEM-2026-09-16-069
  type: standard
  timestamp: "2026-09-16T19:55:00+03:00"
  agents: [performance-optimizer, context-governor, devops-deployer, persistent-memory-engine]
  context: "معايرة شريط الحالة Statusline على سقف النطاق الذهبي 150k توكن لحماية كوتا الحساب وتفادي انفجار تكلفة الكاش"
  content: "نظراً لأن إحصائيات أنثروبيك أظهرت أن 67% من الاستهلاك يحدث فوق 150k توكن حيث تتضاعف تكلفة قراءة الكاش تربيعياً O(N^2)، وكان شريط الحالة السابق يقيس النسبة ضد 1,000,000 توكن مما يعطي انطباعاً خادعاً بالأمان (مثل ctx 20%/1M بينما الاستهلاك الفعلي 200k في المنطقة الحمراء)؛ تمت إعادة معايرة statusline.ps1 و statusline.js في C:\\Users\\Kt\\.claude\\ ليقاس الاستهلاك حصراً ضد سقف النطاق الذهبي (150,000 توكن). يعرض الشريط الآن المقدار والنسبة معاً: `ctx {tokens}k/150k ({pct}%)` مع تلوين دلالي صارم: السماوي الآمن (<60%)، الأصفر التحذيري (60-84%)، والأحمر الحرج (>=85% أو تجاوز الـ 150k) للتأهب الفوري لـ /strategic-clear."
  tags: [statusline, context-governance, golden-cap, 150k-tokens, cache-cost-prevention, quadratic-cost, powershell, nodejs, autovem, global]
  status: active

- id: ADR-INIT-001
  type: decision
  timestamp: "2026-07-21T13:30:00+03:00"
  agents: [code-architect]
  context: "التهيئة الأولية للمشروع القديم"
  content: "تم تهيئة الذاكرة والسياق لهذا المشروع مع الحفاظ على ملفات AGENTS.md و HOOKS_GUIDE.md القديمة الخاصة به كما هي لضمان عدم تأثر قواعده السابقة."
  tags: [init, architecture, legacy-support]
  status: active

- id: ADR-RESTRUCT-002
  type: decision
  timestamp: "2026-08-04T12:39:00+03:00"
  agents: [code-architect, persistent-memory-engine]
  context: "إعادة الهيكلة الشاملة للمشروع وترقية محرك المراجعة الذاتية"
  content: "نقل قاعدة البيانات من الـ Root، تنظيف المجلدات المكررة، أرشفة السكريبتات المؤقتة، إنشاء محرك المستندات الموحد tbc_document_engine.py، وترقية 04_Self_Refinement_Engine إلى مهارة عالمية (Global Skill)."
  tags: [refactoring, architecture]
  status: active

- id: ADR-WALKWAY-003
  type: decision
  timestamp: "2026-08-17T11:28:00+03:00"
  agents: [schedule-builder, boq-analyst, document-formatter, persistent-memory-engine]
  context: "إنشاء وهيكلة ملفات مشروع ممشى جانبي بطول 1 كم وعرض 3.60 م"
  content: "إنشاء المجلد المخصص للمشروع في 03_المشاريع/مشاريع_البلديات/مشروع_الممشى_الجانبي_1كم وتقسيمه إلى 01_جداول_الكميات و 02_المواصفات_والمذكرات_الفنية و 03_البرمجيات_والأدوات مع توليد شيت إكسيل تفاعلي بالمعادلات ومستند وورد رسمي منسق RTL."
  tags: [boq, walkway, excel, docx, municipal]
  status: active

- id: ADR-WALKWAY-004
  type: decision
  timestamp: "2026-08-17T15:10:00+03:00"
  agents: [boq-analyst, schedule-builder, document-formatter, persistent-memory-engine]
  context: "اعتماد تصميم أحواض الزراعة الطولية 30م × 60سم والتعريب الشامل"
  content: "تحديث القطاع الهندسي ليتضمن 20 حوض زراعي طولي مقاس 30 م × 0.60 م بإجمالي مساحة 360 م²، يحتوي كل حوض على 5 شجيرات صغيرة (إجمالي 100 شجيرة) ومغطى بالحشائش الطبيعية، صافي مسطح الإنترلوك الملون 3,240 م²، 21 عمود إنارة موفرة بارتفاع 3.5 إلى 4.0 م كل 50 م، وتفريغ خانات الأسعار الإفرادية للتسعير المباشر مع تعريب 100% لكافة الجداول."
  tags: [planters, boq, 30m-planters, turf-grass, arabic-tables, walkway]
  status: active

- id: ADR-WALKWAY-005
  type: decision
  timestamp: "2026-08-18T14:18:00+03:00"
  agents: [boq-analyst, schedule-builder, document-formatter, persistent-memory-engine]
  context: "المطابقة التامة للنماذج البلدية الرسمية بحوطة بني تميم وتحديث العرض الكلي إلى 4.00 م"
  content: "إعادة هيكلة وصياغة جدول كميات ومواصفات مشروع الممشى ليتطابق 100% مع نمط وصياغة جداول بلدية محافظة حوطة بني تميم (8 أعمدة تشمل التفقيط كتابة وترويسة وتوقيعات معتمدة)، وتعديل العرض الكلي إلى 4.00 م (إنترلوك صافي 3,640 م² + أحواض 360 م²)."
  tags: [boq, municipal-standard, hawteh-municipality, walkway-4m, 3640m2-interlock]
  status: active

- id: ADR-WALKWAY-006
  type: decision
  timestamp: "2026-08-18T14:31:00+03:00"
  agents: [boq-analyst, document-formatter, persistent-memory-engine]
  context: "تطبيق التعديلات الستة واعتماد جهة الإشراف بلدية البديع"
  content: "تعديل مقاس الكابلات إلى 4×25 مم²، إزالة الخرسانة العادية من بند الإنترلوك، تعديل ارتفاع الأعمدة إلى 5-6 م، حذف بند اللوحة الكهربائية بالكامل، إلغاء الثيل وحشائش الحلفة والاعتماد على الشجيرات البيئية، واعتماد بلدية البديع مع ترك خانات الأسماء فارغة للتوقيع."
  tags: [badee-municipality, 25mm-cables, no-plain-concrete, 5-6m-poles, environmental-shrubs, boq-update]
  status: active

- id: ADR-AFLAJ-ROUNDABOUTS-007
  type: decision
  timestamp: "2026-08-19T16:32:00+03:00"
  agents: [planning-engineer, methodology-writer, schedule-builder, risk-planner, document-formatter, persistent-memory-engine]
  context: "إعداد خطة تنفيذ الأعمال واستيفاء معايير التقييم الفني لمشروع كشط وسفلتة دورات الأفلاج 10,000 م²"
  content: "تطوير وثيقة منهجية متكاملة بـ Markdown وبرمجة سكربتات توليد DOCX و XLSX لتغطية معايير التقييم الفني الثلاثة لبلدية الأفلاج بنسبة 100% (خطة المشروع، إدارة المدة والمخاطر، الكادر والمعدات) مع معالجة BiDi/RLM، إدارة أقفال الملفات، وضبط التحويلات المرورية بنظام القطاعات النصفية/الربعية."
  tags: [aflaj-municipality, technical-evaluation, asphalt-milling, superpave-paving, traffic-phasing, risk-matrix, docx, excel]
  status: active

- id: ADR-AFLAJ-TECHNICAL-EVAL-PASSED-008
  type: decision
  timestamp: "2026-08-19T18:00:00+03:00"
  agents: [methodology-writer, planning-engineer, document-formatter, persistent-memory-engine]
  context: "اجتياز تدقيق محامي الشيطان واعتماد تقرير التقييم الفني بنسبة 96% لمشروع دورات الأفلاج"
  content: "اجتياز التدقيق الفني الشامل لكافة معايير بلدية الأفلاج الثلاثة بمرتبة الشرف: المعيار الأول (34/35) لتغطية الـ 10,000 م² ومواصفات Superpave و RC-2 والترقيع العميق، المعيار الثاني (34/35) لتبرير مدة الـ 30 يوماً بمعدلات الإنتاجية ومصفوفة المخاطر المتخصصة وبروتوكول الطوارئ، المعيار الثالث (28/30) للكادر الفني المتكامل مع مشغل الفينشر والآليات الليزرية والحرارية."
  tags: [technical-evaluation, pass-96-percent, superpave, aflaj, municipal, docx]
  status: active

- id: ADR-RIYADH-SCHOOL-HSE-PLAN-009
  type: decision
  timestamp: "2026-08-19T20:00:00+03:00"
  agents: [planning-engineer, methodology-writer, document-formatter, risk-planner, persistent-memory-engine]
  context: "إعداد وتوسيع خطة السلامة والصحة المهنية (HSE Plan) باللغة العربية لمشروع الصيانة الطارئة لمدرسة أم المؤمنين زينب الأسدية بالرياض (TBC) إلى 14 صفحة كاملة وإنشاء مخططات التدفق"
  content: "إعداد وثيقة خطة سلامة متكاملة وموسعة موزعة على 14 صفحة بالضبط (صفحة لكل بند هندسي رئيسي) تشمل 16 قسماً تخصصياً، مصفوفة JSA الشاملة، مصفوفة PPE المعتمدة، اشتراطات عزل الطاقة LOTO وبيئة رياض الأطفال، وتوليد ملفات DOCX و PDF مع تطبيق محاذاة اليسار للنصوص والعناوين وتوسيط الجداول، بالإضافة إلى بناء وتصدير مخططات التدفق الهندسية السادة بالخلفية البيضاء (دورة تصريح العمل PTW وبروتوكول الإخلاء الطبي والميداني)."

- id: ADR-CONTRACTOR-DIRECT-WARRANTY-010
  type: decision
  timestamp: "2026-08-20T11:45:00+03:00"
  context: "إصدار خطابات الضمان الفنية مباشرة باسم المقاول الرئيسي (مؤسسة إعمار الفرعة) لـ TBC"
  content: "عند صعوبة التواصل مع المصانع الموردة أو وجود تحفظات ومحددات مجحفة في شهادات الضمان الصادرة منها ترفضها المكاتب الاستشارية، يتم إصدار خطاب الضمان الرسمي مباشرة باسم مؤسسة إعمار الفرعة (المقاول الرئيسي) كجهة ضامنة ومسؤولة مسؤولية تامة ومباشرة عن كافة عيوب التصنيع والمواد والتركيب لمدة 10 سنوات تجاه المالك والاستشاري مع إفراد توقيع وختم المؤسسة منفرداً."
  tags: [warranty, tbc, cables, main-contractor-guarantee, direct-warranty, docx, pdf]
  status: active

- id: ADR-HSE-PLAN-SUBMITTAL-FORM-011
  type: decision
  timestamp: "2026-08-20T13:26:00+03:00"
  content: "اعتماد وتهيئة نموذج التقديم والاعتماد الرسمي المعتمد لشركة تطوير للمباني TBC (Submittal Form - S5-PRS-01-P01-F02) برقم التقديم EFC-HSE-SUB-01 وبشعار المقاول الرئيسي الرسمي EFC إلى جانب شعار TBC والاستشاري SAMAA لتقديم خطة السلامة والصحة المهنية لمشروع مدرسة أم المؤمنين زينب الأسدية (305333 / TBC006687)."
  status: active

- id: ADR-TAILOR-ECOSYSTEM-012
  type: decision
  timestamp: "2026-08-22T12:56:00+03:00"
  agents: [persistent-memory-engine, code-architect, project-agent-tailor]
  context: "اعتماد سياق المشروع وتخصيص وتصدير طاقم الوكلاء والمهارات الهندسية والذكاء الاصطناعي"
  content: "تشغيل محرك التخصيص project_agent_tailor.py وتصدير 20 وكيلاً و 22 مهارة متخصصة تغطي قطاعات: 1) هندسة التخطيط والمقاولات العامة، 2) حصر الكميات والعقود والمستندات الهندسية، 3) ذكاء اصطناعي وأتمتة بايثون و MCP، مع توليد agent_manifest.json وسكربت المزامنة المحلي sync_local_agents.py وتنظيف المهارات الفائضة."
  tags: [tailor, agent-roster, skills, manifest, sync-local, engineering-ecosystem]
  status: active

- id: ADR-RIYADH-CABLES-WARRANTY-MODIFIED-013
  type: decision
  timestamp: "2026-08-22T13:33:00+03:00"
  agents: [methodology-writer, document-formatter, persistent-memory-engine]
  context: "تعديل شهادة ضمان كابلات الرياض الموثقة من الغرفة التجارية لاعتماد الاستشاري و TBC"
  content: "تعديل الفقرة 2 لإلزام الشركة بتحمل كامل تكاليف ومسؤولية عيوب التصنيع، إضافة إحداثيات مدرسة أم المؤمنين زينب الأسدية (24.7723938° N, 46.8335147° E)، تفصيل بيانات المالك (TBC) والمقاول (إعمار الفرعة) والاستشاري (سماء)، الحفاظ التام على كتلة توثيق الغرفة التجارية الأصلية والتذييل في أسفل الصفحة بدقة 300 DPI، وتطبيق محاذاة اليسار للنصوص مع ضبط احتواء المستند في صفحة A4 واحدة بالضبط."
  tags: [warranty-certificate, riyadh-cables, chamber-of-commerce, consultant-approved, single-page, left-aligned, docx, pdf]
  status: active

- id: ADR-BOQ-REPLICA-EFC-014
  type: decision
  timestamp: "2026-08-29T14:15:00+03:00"
  agents: [boq-analyst, excel-data-analyst, document-formatter, persistent-memory-engine]
  context: "إعادة إنتاج جدول الكميات (BOQ) لأعمال الأبواب والنوافذ لفلل B و C على الورق الرسمي لمؤسسة إعمار الفرعة"
  content: "استخراج وتفريغ كافة بنود وأسعار ومساحات الأبواب ونوافذ الألمنيوم بدقة 100% من المسح الضوئي الأصلي لفلل B و C، إزالة كافة أختام وشعارات وبيانات الشركة السابقة، وتوليد ملفات Excel تفاعلية بالمعادلات (XLSX) ومستندات وورد (DOCX) وملفات PDF ثلاثية الصفحات متطابقة مع الهوية البصرية الرسمية لمؤسسة إعمار الفرعة مع جدول الخلاصة والاعتماد الرسمي للمقاول."
  tags: [boq, aluminium-doors-windows, efc-letterhead, excel, docx, pdf, new-project]
  status: active

- id: ADR-BRANDING-MINASAT-015
  type: decision
  timestamp: "2026-08-29T14:55:00+03:00"
  agents: [frontend-design-builder, document-formatter, persistent-memory-engine]
  context: "تصميم حزمة الورق الرسمي والهوية الصناعية لمصنع منصة الإبداع للمعادن"
  content: "تصميم 3 أنماط بصرية صناعية فاخرة (Concept A: الحديث، Concept B: الفولاذي، Concept C: الملكي) بدقة 300 DPI وأيقونات فيكتور مدمجة، وتوليد قوالب Word قابلة للتعديل بدون تشوه الهوامش مع تصدير PDF عالي الدقة وصور A4 فارغة للطباعة، وبناء محرك بايثون ديناميكي لتحديث الهاتف والإيميل فورياً."
  tags: [branding, letterhead, docx, pdf, 300dpi, vector-icons, minasat-al-abdaa, industrial-metals]
  status: active

- id: ADR-WARRANTY-SUPPLIER-016
  type: decision
  timestamp: "2026-08-29T22:52:00+03:00"
  agents: [document-formatter, persistent-memory-engine]
  context: "إصدار شهادة وخطاب ضمان الكابلات والأسلاك الكهربائية باسم المورد (مؤسسة النقطة المضيئة) لمشروع مدرسة أم المؤمنين زينب الأسدية (TBC)"
  content: "إعادة صياغة شهادة ضمان الكابلات لتصدر رسمياً باسم المورد المعتمد (مؤسسة النقطة المضيئة) متضمنة كافة بيانات المشروع الـ 11 (المالك TBC، المقاول إعمار الفرعة، الاستشاري سماء، إحداثيات المدرسة، أرقام طلب الشراء وأمر التوريد، ومحضر الإسناد)، مع إعادة صياغة البنود الأربعة بلسان التزام المورد بالتنسيق مع المصنع (كابلات الرياض) بضمان 10 سنوات، وضبط الإخراج في صفحة A4 واحدة بجدول RTL وبيئة BiDi كاملة بصيغ DOCX و PDF."
  tags: [warranty-certificate, supplier-warranty, al-noqtah-al-modeeah, riyadh-cables, tbc, emaar-al-farah, single-page, docx, pdf]
  status: active

- id: ADR-CATALOG-FOOTER-UPDATE-017
  type: decision
  timestamp: "2026-09-06T10:45:00+03:00"
  agents: [prompt-engineer, document-formatter, persistent-memory-engine]
  context: "تحديث وإضافة بيانات التواصل لتذييل كتالوج منصة الإبداع للمعادن المكون من 52 صفحة"
  content: "تنفيذ أتمتة غير إتلافية (Lossless Vector Overlay عبر PyMuPDF) لتحديث تذييل الكتالوج الفني، استبدال بيانات الاتصال القديمة على الغلافين (1 و 52) بلون أبيض نقي فوق الشريط الكحلي الداكن (#122D4B)، وتذييل كافة صفحات المنتجات (7 إلى 51) في أسفل اليمين بخط Segoe UI Symbol المدمج بالهاتف الجديد 0541819045 والإيميل engmohamedyones@gmail.com مع استثناء صفحات التراخيص الحكومية (2 إلى 6)، وأخذ نسخة احتياطية أصلية قبل التعديل."
  tags: [catalog-footer, pdf-overlay, pymupdf, lossless, minasat-al-abdaa, new-project]
  status: active

- id: ADR-CATALOG-PAGINATION-018
  type: decision
  timestamp: "2026-09-06T10:54:00+03:00"
  agents: [prompt-engineer, document-formatter, persistent-memory-engine]
  context: "إضافة ترقيم صفحات هندسي متناسق لكتالوج منصة الإبداع للمعادن"
  content: "تطبيق ترقيم صفحات متوازن بصرياً بصيغة — X — في المنتصف الهندسي للتذييل (x = 283.0 pt) لصفحات المنتجات (7 إلى 51) مع توسيط ديناميكي بحسب طول النص، واستثناء صفحات الغلافين (1 و 52) وصفحات التراخيص الحكومية (2 إلى 6) دون أي مساس بجودة الـ PDF أو تداخل مع بيانات التواصل واسم المصنع."
  tags: [catalog-pagination, optical-centering, tripartite-footer, pymupdf, minasat-al-abdaa, new-project]
  status: superseded

- id: ADR-CATALOG-TWOLINE-FOOTER-019
  type: decision
  timestamp: "2026-09-06T10:58:00+03:00"
  agents: [prompt-engineer, document-formatter, persistent-memory-engine]
  context: "تصحيح مسار تذييل الكتالوج وتوزيع البيانات والترقيم على سطرين منفصلين"
  content: "إعادة بناء التذييل نظيفاً بنسبة 100% من النسخة الاحتياطية الأصلية لمنع تراكم الطبقات، وتوزيع التذييل في جهة اليمين لصفحات المنتجات (7 إلى 51) على سطرين مستقلين: السطر الأول لبيانات الاتصال الرسمية (الهاتف والإيميل عند y = 776 pt) والسطر الثاني لرقم الصفحة (— X — عند y = 796 pt) متمركزاً بدقة أسفل كتلة الاتصال، مع بقاء اسم المصنع على اليسار وحماية الأغلفة والشهادات الحكومية."
  tags: [catalog-footer, two-line-layout, clean-rollback, pymupdf, lossless, minasat-al-abdaa, new-project]
  status: superseded

- id: ADR-CATALOG-VISUAL-ALIGN-020
  type: decision
  timestamp: "2026-09-06T11:08:00+03:00"
  agents: [prompt-engineer, document-formatter, persistent-memory-engine]
  context: "تصحيح موضع ترقيم صفحات الكتالوج إلى الموضع الأوسط المحدد بصرياً"
  content: "نقل ترقيم الصفحات (— X —) في السطر السفلي (y = 796 pt) ليتمركز في الفراغ الأوسط تماماً بين اسم المصنع وبداية رقم الهاتف (x = 285.0 pt) بدقة متطابقة مع المؤشر البصري، مع الحفاظ على التوليد النظيف من النسخة الاحتياطية دون شوائب."
  tags: [catalog-footer, visual-alignment, center-gap, pymupdf, minasat-al-abdaa, new-project]
  status: superseded

- id: ADR-CATALOG-VERTICAL-LEVEL-021
  type: decision
  timestamp: "2026-09-06T11:12:00+03:00"
  agents: [prompt-engineer, document-formatter, persistent-memory-engine]
  context: "خفض منسوب ترقيم صفحات الكتالوج رأسياً وفق المؤشر البرتقالي"
  content: "خفض المنسوب الرأسي لترقيم الصفحات (— X —) لأسفل من y = 796 pt إلى y = 814 pt في الفراغ الأوسط (x = 285.0 pt) ليتطابق مع الدائرة البرتقالية، مع الحفاظ على التوليد النظيف من النسخة الاحتياطية الأصلية وحماية الأغلفة والشهادات الحكومية."
  tags: [catalog-footer, vertical-adjustment, lowered-pagination, pymupdf, minasat-al-abdaa, new-project]
  status: active

- id: ADR-CATALOG-ENGLISH-EDITION-022
  type: decision
  timestamp: "2026-09-06T13:17:00+03:00"
  agents: [prompt-engineer, document-formatter, persistent-memory-engine]
  context: "توليد النسخة الإنجليزية الكاملة لكتالوج مصنع منصة الإبداع للمعادن المكون من 52 صفحة"
  content: "بناء النسخة الإنجليزية الكاملة للكتالوج الصناعي بدءاً من النسخة الاحتياطية الأصلية (Lossless Single-Pass Overlay)، تعريب/ترجمة تخصصات الواجهة الرئيسية (الألمنيوم، الكلادنج، الحديد المشغول، الاستركشر، الزجاج، والستانلس ستيل) بأعمدة نقطية سيان متوازنة، استبدال فقرة المقدمة بترجمة هندسية دقيقة في صفحة الملف التعريفي 8 مع توحيد ترويسة INTRODUCTION البرتقالية، توحيد تذييل صفحات الأعمال الهندسية (8 إلى 51) ببيانات الاتصال والترقيم الأوسط المنخفض واسم المصنع بالإنجليزي، وترجمة عنوان الغلاف الخلفي وبياناته مع حماية الشهادات الحكومية الرسمية (2 إلى 6) بأصالتها."
  tags: [english-edition, catalog-localization, pymupdf, lossless, minasat-al-abdaa, corporate-identity]
  status: active

- id: ADR-CATALOG-EBDAA-SPELLING-023
  type: decision
  timestamp: "2026-09-06T13:41:00+03:00"
  agents: [prompt-engineer, document-formatter, persistent-memory-engine]
  context: "تحديث تهجئة اسم المصنع الإنجليزي إلى MINASAT AL-EBDAA وحماية النسخة العربية"
  content: "تحديث تهجئة اسم المصنع من MINASAT AL-ABDAA إلى MINASAT AL-EBDAA في كافة مواضع الكتالوج الإنجليزي الستة (الغلاف الأمامي ص 1، فاصل الفهرس ص 7، متن المقدمة ص 8، فاصل المشاريع ص 23، تذييل الصفحات 8 إلى 51، والغلاف الخلفي ص 52)، وتحديث الملفات المعتمدة إلى Minasat_Al_Ebdaa_Catalog_EN.pdf مع العزل والحماية المطلقة للنسخة العربية كتالوج منصة الابداع_compressed 0.pdf دون أي مساس."
  tags: [spelling-update, ebdaa-standard, scope-isolation, english-catalog, pymupdf, memory-sync]
  status: active

- id: ADR-AMIRI-FONT-CORPORATE-PROFILE-024
  type: decision
  timestamp: "2026-09-08T20:30:00+03:00"
  agents: [frontend-design-builder, document-formatter, persistent-memory-engine]
  context: "اعتماد خط أميري (Amiri) الملكي في البروفايل الموحد لمؤسسة إعمار الفرعة والفاصل الهندسي للذراع الصناعي"
  content: "تثبيت واعتماد حزمة خط أميري (Amiri-Bold و Amiri-Regular) كخط عربي رئيسي ومعتمد لصفحات الهوية المضافة والمحدثة في البروفايل الموحد لمؤسسة إعمار الفرعة ومصنع المعادن (76 صفحة)، وتطبيق الخط خصيصاً على: 1) الصفحة الفاصلة للذراع الصناعي (ص 25) لبيانات التكامل الصناعي والبطاقات الأربع والترخيص الصناعي والبيئي. 2) الغلاف الخلفي الموحد (ص 76) لبيانات الإدارة العامة والمصنع والرؤية الاستراتيجية. 3) تعديلات الغلاف الأمامي (ص 1) لعنوان الرياض والإيميلات والجوالات مع الحظر الصارم للأرقام الهندية المشرقية (100% English Numerals 0-9). 4) صفحات المشاريع (14 و 15 و 16) لاستبدال 'بحوطه بني تميم' بـ 'بالرياض'."

- id: ADR-UI-UX-PRO-MAX-INTEGRATION-025
  type: decision
  timestamp: "2026-09-08T22:15:00+03:00"
  agents: [resource-scout-integrator, ui-ux-pro-max, prompt-engineer, persistent-memory-engine]
  context: "استكشاف وتكامل مورد ui-ux-pro-max-skill وتعميم المهارات الـ 7 عبر منظومة الوكلاء"
  content: "استيراد وتكامل مستودع nextlevelbuilder/ui-ux-pro-max-skill بحفظ كامل لاسم المنبع وحزمته التنفيذية (القيد 39)، ونشر 7 مهارات متخصصة (ui-ux-pro-max, design-system, ui-styling, brand, design, banner-design, slides) في المسارات العالمية والمحلية، وربط محرك الاستدلال التصميمي (192 قاعدة، 79 نمط، 192 باليت ألوان، 74 خط، 22 stack) بالخطاف رقم 20 والخطاف رقم 5 لفرض الذكاء التصميمي وسهولة الوصول قبل كتابة أي واجهات، وتحديث HOOKS_GUIDE.xlsx ولوحات التحكم، وإنجاز المزامنة السحابية الكاملة بنجاح."
  tags: [ui-ux-pro-max, design-intelligence, hook-20, hook-5, design-system, upstream-skills, memory-sync]
  status: active

- id: ADR-REMOTE-FUTRX-INTEGRATION-026
  type: decision
  timestamp: "2026-09-08T22:42:00+03:00"
  agents: [resource-scout-integrator, devops-deployer, code-architect, persistent-memory-engine]
  context: "استكشاف وتكامل منصة remote.futrx للوكلاء السحابيين المستقلين على سيرفرات Hostinger VPS"
  content: "1) استكشاف واستنساخ مستودع futrx-com/remote.futrx وتكامله كمنصة تشغيل سحابية ذاتية الاستضافة (Self-Hosted Agent OS) تدير حاويات LXD معزولة لكل مشروع على Ubuntu 24.04 وتدعم الوكلاء الخمسة (Claude Code CLI, Antigravity agy, Codex, MiniMax, Kimi). 2) الالتزام المطلق بالقيد 53 بنشر وتشغيل المنصة حصرياً على خوادم Hostinger VPS (KVM) وتخصيص حاسوب Windows للواجهات والبرمجة عبر Antigravity IDE. 3) تثبيت بوابة الفحص المسبق لأمان SSH لحظر فقدان الاتصال بسبب تعطيل تسجيل الدخول بكلمة المرور تلقائياً في السكربت. 4) توليد مهارة remote-futrx بكامل أدلتها وسكربت الفحص المساعد ونشرها في الجذور الأربعة وتحديث HOOKS_GUIDE و HOOKS_GUIDE.xlsx والمزامنة الكاملة."
  tags: [remote-futrx, cloud-vps, hostinger, lxd-containers, ssh-hardening, hybrid-cloud, hook-20, memory-sync]
  status: active

- id: ADR-FLEET-MODELS-2026-09-08-027
  type: decision
  timestamp: "2026-09-08T23:07:00+03:00"
  agents: [prompt-engineer, fleet-orchestrator, code-architect, persistent-memory-engine]
  context: "تحديث مصفوفة نماذج الأسطول واعتماد deepseek-v4-flash لـ OpenCode وترقية Gemini 3.8 Flash لـ Antigravity"
  content: "1) اعتماد وتثبيت نموذج deepseek/deepseek-v4-flash كنموذج حصري وافتراضي لـ OpenCode CLI لتنفيذ الأوامر الطرفية والسكربتات وترحيل قواعد البيانات بأعلى سرعة ومجانية، بعد التحقق من خلو قائمة مزودي OpenCode الحالية من اسم Ox Alpha الصريح. 2) ترقية محرك Antigravity CLI / IDE رسمياً إلى Gemini 3.8 Flash (High Reasoning) في ملفات الأسطول والقواعد العامة، للاستفادة من قدراته الفائقة في المنطق المعماري وإدارة السياق. 3) تحديث القاعدة رقم 13 وملفات fleet_config.json بالتزامن."
  tags: [fleet-models, deepseek-v4-flash, gemini-3.8-flash, opencode, antigravity, hook-22, rule-13]
  status: active
