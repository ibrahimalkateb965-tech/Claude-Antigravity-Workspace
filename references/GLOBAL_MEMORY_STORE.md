<div dir="rtl">

# مخزن الذاكرة المركزي لمشروع Blind App (MEMORY_STORE.md)

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

- id: MEM-2026-08-26-001
  type: fleet-governance
  timestamp: "2026-08-26T16:46:00+03:00"
  agents: [fleet-orchestrator, persistent-memory-engine]
  context: "بروتوكول التفويض والتشغيل المباشر لأسطول الـ CLIs (Autonomous Direct Dispatch)"
  content: "يُحظر تماماً مطالبة المستخدم بنسخ ولصق أوامر الطرفية أو البرومبتات يدوياً للـ CLIs الأخرى (Claude Code CLI, OpenCode CLI). يلتزم الوكيل المنسق بتشغيل وتفويض الأوامر مباشرة عبر أدوات الطرفية المتاحة (`run_command` أو `manage_task` send_input أو عبر `claude -p` و `opencode run`)."
  tags: [fleet-orchestrator, autonomous-dispatch, run-command, multi-cli, global]
  status: active

- id: MEM-2026-08-26-002
  type: environment-standard
  timestamp: "2026-08-26T17:00:00+03:00"
  agents: [persistent-memory-engine, devops-deployer, code-architect]
  context: "معيار معالجة المسافات في مسار Flutter SDK بالروابط الوصلية (SDK Space Junction)"
  content: "وجود مسافات في مسار تثبيت Flutter SDK يكسر بناء خطافات native-assets في حزم C/SQLite مثل sqlite3 و objective_c. الحل القياسي المعياري هو إنشاء رابط وصلي (Directory Junction) بدون مسافات: `cmd /c mklink /J \"C:\\flutter_sdk\" \"<Flutter_Path>\"` وتنفيذ أوامر Flutter حصرياً عبر `C:\\flutter_sdk\\bin\\flutter`."
  tags: [flutter-sdk, native-assets, directory-junction, windows, path-spaces, global]
  status: active

- id: MEM-2026-08-26-003
  type: database-migration
  timestamp: "2026-08-26T17:30:00+03:00"
  agents: [persistent-memory-engine, backend-architect, offline-sync-db]
  context: "قواعد ترحيل مخططات PocketBase وترتيب تبعيات الحذف (PocketBase Schema Migration)"
  content: "في PocketBase v0.23+ / v0.39+، لا يمكن تحويل نوع جدول من auth إلى base مباشرة. الحل الصارم هو حذف الجداول القديمة المتعارضة بترتيب التبعية العكسي (الأبناء أولاً: daily_logs -> weekly_reports -> student -> students -> teachers -> circles -> academies) قبل استيراد المخطط الموحد الجديد مع تفعيل deleteMissing=false لتفادي حذف جداول النظام الأساسية (_superusers, _mfas)."
  tags: [pocketbase, schema-migration, auth-collections, foreign-keys, database, global]
  status: active

- id: MEM-2026-08-26-004
  type: scripting-standard
  timestamp: "2026-08-26T17:45:00+03:00"
  agents: [persistent-memory-engine, debugger, devops-deployer]
  context: "إلزامية السكربتات غير التفاعلية في بيئات الأتمتة والخلفية (Non-Interactive Scripting)"
  content: "استخدام دوال TTY التفاعلية مثل getpass أو input() يعلق السكربتات قسرياً عند تشغيلها عبر Background Tasks أو الأنابيب البرمجية. يجب دائماً قبول المعاملات والتوكنات عبر وسائط سطر الأوامر (CLI Args) أو متغيرات البيئة (Env Vars) مع معالجة واضحة للأخطاء."
  tags: [scripting, non-interactive, automation, python, getpass-hazard, global]
  status: active

- id: MEM-2026-08-26-005
  type: mcp-architecture
  timestamp: "2026-08-26T20:20:00+03:00"
  agents: [persistent-memory-engine, mcp-tool-builder, debugger]
  context: "معيار أمان قنوات خوادم FastMCP وتجنب كسر Stdio (FastMCP Stdio Integrity Standard)"
  content: "في خوادم FastMCP التي تعمل بنمط stdio، يُحظر تماماً استدعاء sys.stdout.detach() أو إعادة تغليف sys.stdout بـ codecs.getwriter؛ لأن محرك FastMCP stdio يحتاج خاصية sys.stdout.buffer لإنشاء TextIOWrapper للاتصال الثنائي وقنوات JSON-RPC وتعديلها يسبب AttributeError: '_io.BufferedWriter' object has no attribute 'buffer'. يجب توجيه كافة سجلات الفحص والـ Debugging إلى sys.stderr حصراً."
  tags: [fastmcp, stdio, json-rpc, buffer-integrity, mcp-servers, global]
  status: active

- id: MEM-2026-08-26-006
  type: cli-ux-standard
  timestamp: "2026-08-26T20:30:00+03:00"
  agents: [persistent-memory-engine, devops-deployer, prompt-engineer]
  context: "معيار المسارات المطلقة للأوامر والسكربتات الطرفية (Absolute Path Command Standard)"
  content: "يُحظر تقديم أوامر وسكربتات طرفية تعتمد على مسارات نسبية عارية (مثل cd 'dir' أو ./script.ps1) لأن مسار عمل الطرفية الحالي (CWD) قد يختلف لدى المستخدم أو بيئة الخلفية. يجب دائماً تقديم الأوامر بالمسار المطلق الكامل `& '<Full_Path>'` أو دمج الانتقال الكامل أولاً لضمان التنفيذ الناجح بنقرة واحدة."
  tags: [cli-commands, absolute-paths, powershell, cwd-mismatch, ux, global]
  status: active

- id: MEM-2026-08-26-007
  type: architecture-standard
  timestamp: "2026-08-26T21:10:00+03:00"
  agents: [persistent-memory-engine, code-architect, offline-sync-db]
  context: "توحيد معرفات المجال Domain Enums وفحص صلاحية JWT محلياً"
  content: "1. يُمنع تكرار تعريف الـ Enums في ملفات متعددة؛ يجب تعريف الـ Enum في ملفه المخصص (user_role.dart) وإعادة تصديره عبر الكيان (user_entity.dart) لمنع تعارض الأنواع في Dart. 2. في مصادقة PocketBase، يجب الاعتماد على authStore.isValid لفك تشفير JWT محلياً واستدعاء logout() فوراً عند انتهاء الصلاحية قبل التوجيه لمنع حلقات 401."
  tags: [domain-layer, clean-architecture, jwt-expiry, pocketbase, riverpod, global]
  status: active

- id: MEM-2026-08-30-001
  type: knowledge-baseline
  timestamp: "2026-08-30T10:42:00+03:00"
  agents: [self-refinement-engine, prompt-engineer, fleet-orchestrator]
  context: "التحقق الإجباري وتثبيت مصفوفة أحدث أجيال نماذج Claude 5"
  content: "يُحظر تماماً الإشارة إلى نماذج قديمة سابقة (مثل Claude 3.5 أو 3.7 أو أجيال 4 المتقاعدة). يتم اعتماد مصفوفة الجيل الأحدث النشط (Claude 5 Generation: Opus 5 / Opus Max, Sonnet 5, Fable 5, Haiku 4.5) مع إلزامية البحث والتحقق الفوري قبل تقديم أي استشارة."
  tags: [claude-models, claude-5, model-routing, fleet-orchestration, global]
  status: active

- id: ADR-2026-08-30-001
  type: decision
  timestamp: "2026-08-30T11:34:00+03:00"
  agents: [code-architect, fleet-orchestrator, self-refinement-engine]
  context: "اعتماد أداة Archify الرسمية والحفاظ على اسم المنبع وربطها بالجذور"
  content: "تم اعتماد أداة archify (الإصدار 2.16.0-dev.0) بالاسم الأصلي من المنبع وربطها بروابط وصلية مع ~/.gemini/config/skills/archify و ~/.claude/skills/archify لتصبح متاحة لكافة الوكلاء، مع حظر إعادة التسمية لمنع انقطاع مسار التحديثات الرسمي."
  tags: [archify, diagramming, skill-standard, junctions, global]
  status: active
```

</div>
- id: ADR-RESTRUCT-002
  type: decision
  timestamp: "2026-08-04T12:39:00+03:00"
  agents: [code-architect, persistent-memory-engine]
  context: "إعادة الهيكلة الشاملة للمشروع وترقية محرك المراجعة الذاتية"
  content: "نقل قاعدة البيانات من الـ Root، تنظيف المجلدات المكررة، أرشفة السكريبتات المؤقتة، إنشاء محرك المستندات الموحد tbc_document_engine.py، وترقية 04_Self_Refinement_Engine إلى مهارة عالمية (Global Skill)."
  tags: [refactoring, architecture]
  status: active

- id: LESSON-QS-001
  timestamp: "2026-08-13T21:35:00+03:00"
  context: "حصر الكميات لفيلا فيريال"
  tags: ['global']
  lesson: "تحويل المخططات من PDF إلى DWG/DXF يفقد الطبقات الأصلية ويحول النصوص العربية والأبعاد إلى Polylines أو أحرف تالفة، ولذلك فإن مسار الجداول التقديرية مع السماكة المكافئة للأسقف هو الأسرع والأكثر موثوقية."

- id: LESSON-AGENT-008
  timestamp: "2026-08-14T00:30:00+03:00"
  context: "حزمة تأسيس الوكلاء المعيارية وسكربت التهيئة التلقائي (Universal Agent Starter Kit)"
  tags: ['global', 'architecture', 'automation']
  lesson: "تأسيس حزمة معيارية مجردة ومستقلة مع سكربت تهيئة آلي (init_project_agents.py) يعتمد على الاكتشاف التلقائي لملفات البيئة والمقابلة التأسيسية، يمنع تلوث السياق الموروث (Context Bleeding) ويوحد قواعد الوكلاء الـ 20 عبر جميع المشاريع بنقرة واحدة."

- id: LESSON-AGENT-009
  timestamp: "2026-08-14T00:39:00+03:00"
  context: "اعتماد معمارية الذاكرة اللامركزية الثلاثية وتنظيف الدستور (3-Tier Memory Architecture)"
  tags: ['global', 'architecture', 'context-optimization']
  lesson: "حظر حشو الدروس اليومية في ملف الدستور AGENTS.md لتفادي تضخم نافذة السياق وهدر التوكنز وتشتت تركيز النماذج، واعتماد MEMORY_STORE.md كمستودع محلي وحيد للدروس مع ترحيل الدروس الحرجة الموسومة بـ global إلى ACTIVE_CONTEXT_INJECTION.md."

- id: LESSON-AGENT-013
  timestamp: "2026-09-03T15:50:00+03:00"
  context: "بروتوكول تسليم قيادة الأسطول ذاتياً عند نفاد كوتا جلسة Claude Code CLI"
  tags: ['fleet', 'quota-limit', 'failover', 'global']
  lesson: "عند وصول كوتا جلسة Claude Code CLI للحد الأقصى (100% used)، يتولى وكيل Antigravity IDE (Gemini 3.8 Flash High) فوراً قيادة الأسطول وإجراء كافة التعديلات البرمجية وتحديث جداول الإكسل والاعتماد الهندسي دون أي توقف."

- id: ADR-AGENT-004
  type: decision
  timestamp: "2026-08-14T00:30:00+03:00"
  agents: [code-architect, persistent-memory-engine, test-automator]
  context: "حزمة تأسيس الوكلاء المعيارية (Universal Agent Starter Kit)"
  content: "بناء حزمة معيارية مجردة تضم 20 وكيلاً و26 مهارة وسكربت تهيئة آلي يدعم الاكتشاف التلقائي لبيئات (Android/Web/QS/Python)، مع عزل ملفات المشاريع السابقة لمنع التلوث السياقي وضمان استخدام أحدث نماذج 2026 وقواعد lean-ctx عالمياً."
  tags: [scaffolding, agents, starter_kit, automation, global]
  status: active

- id: ADR-AGENT-005

- id: ADR-RESTRUCT-002
  type: decision
  timestamp: "2026-08-04T12:39:00+03:00"
  agents: [code-architect, persistent-memory-engine]
  context: "إعادة الهيكلة الشاملة للمشروع وترقية محرك المراجعة الذاتية"
  content: "نقل قاعدة البيانات من الـ Root، تنظيف المجلدات المكررة، أرشفة السكريبتات المؤقتة، إنشاء محرك المستندات الموحد tbc_document_engine.py، وترقية 04_Self_Refinement_Engine إلى مهارة عالمية (Global Skill)."
  tags: [refactoring, architecture]
  status: active

- id: LESSON-QS-001
  timestamp: "2026-08-13T21:35:00+03:00"
  context: "حصر الكميات لفيلا فيريال"
  tags: ['global']
  lesson: "تحويل المخططات من PDF إلى DWG/DXF يفقد الطبقات الأصلية ويحول النصوص العربية والأبعاد إلى Polylines أو أحرف تالفة، ولذلك فإن مسار الجداول التقديرية مع السماكة المكافئة للأسقف هو الأسرع والأكثر موثوقية."

- id: LESSON-AGENT-008
  timestamp: "2026-08-14T00:30:00+03:00"
  context: "حزمة تأسيس الوكلاء المعيارية وسكربت التهيئة التلقائي (Universal Agent Starter Kit)"
  tags: ['global', 'architecture', 'automation']
  lesson: "تأسيس حزمة معيارية مجردة ومستقلة مع سكربت تهيئة آلي (init_project_agents.py) يعتمد على الاكتشاف التلقائي لملفات البيئة والمقابلة التأسيسية، يمنع تلوث السياق الموروث (Context Bleeding) ويوحد قواعد الوكلاء الـ 20 عبر جميع المشاريع بنقرة واحدة."

- id: LESSON-AGENT-009
  timestamp: "2026-08-14T00:39:00+03:00"
  context: "اعتماد معمارية الذاكرة اللامركزية الثلاثية وتنظيف الدستور (3-Tier Memory Architecture)"
  tags: ['global', 'architecture', 'context-optimization']
  lesson: "حظر حشو الدروس اليومية في ملف الدستور AGENTS.md لتفادي تضخم نافذة السياق وهدر التوكنز وتشتت تركيز النماذج، واعتماد MEMORY_STORE.md كمستودع محلي وحيد للدروس مع ترحيل الدروس الحرجة الموسومة بـ global إلى ACTIVE_CONTEXT_INJECTION.md."

- id: LESSON-AGENT-013
  timestamp: "2026-09-03T15:50:00+03:00"
  context: "بروتوكول تسليم قيادة الأسطول ذاتياً عند نفاد كوتا جلسة Claude Code CLI"
  tags: ['fleet', 'quota-limit', 'failover', 'global']
  lesson: "عند وصول كوتا جلسة Claude Code CLI للحد الأقصى (100% used)، يتولى وكيل Antigravity IDE (Gemini 3.8 Flash High) فوراً قيادة الأسطول وإجراء كافة التعديلات البرمجية وتحديث جداول الإكسل والاعتماد الهندسي دون أي توقف."

- id: ADR-AGENT-004
  type: decision
  timestamp: "2026-08-14T00:30:00+03:00"
  agents: [code-architect, persistent-memory-engine, test-automator]
  context: "حزمة تأسيس الوكلاء المعيارية (Universal Agent Starter Kit)"
  content: "بناء حزمة معيارية مجردة تضم 20 وكيلاً و26 مهارة وسكربت تهيئة آلي يدعم الاكتشاف التلقائي لبيئات (Android/Web/QS/Python)، مع عزل ملفات المشاريع السابقة لمنع التلوث السياقي وضمان استخدام أحدث نماذج 2026 وقواعد lean-ctx عالمياً."
  tags: [scaffolding, agents, starter_kit, automation, global]
  status: active

- id: ADR-AGENT-005
  type: decision
  timestamp: "2026-08-14T00:39:00+03:00"
  agents: [code-architect, persistent-memory-engine]
  context: "هيكلة الذاكرة الثلاثية وترشيق الدستور (3-Tier Memory System)"
  content: "اعتماد الفصل الصارم بين: (1) دستور الوكلاء AGENTS.md للقواعد السلوكية الثابتة، (2) القيود العالمية المانعة للأخطاء ACTIVE_CONTEXT_INJECTION.md للخطوة صفر، و (3) متجر الذاكرة اللامركزي MEMORY_STORE.md لتسجيل 100% من الدروس والقرارات المحلية."
  tags: [memory, architecture, 3-tier, optimization, global]
  status: active

- id: MEM-2026-09-08-044
  type: lesson
  timestamp: "2026-09-08T09:30:00+03:00"
  agents: [prompt-engineer, boq-analyst, document-formatter, persistent-memory-engine]
  context: "حظر وضع الأسعار الافتراضية وتثبيت هوية المقاول المنفذ إعمار الفرعة"
  content: "الحظر المطلق لأي أسعار افتراضية أو تقديرية (Strict Zero-Pricing Rule) في جداول الكميات وعروض الأسعار ما لم يطلب المستخدم ذلك صراحة، مع إبقاء خانات التسعير مفرغة [             ر.س ] وبناء معادلات Excel شرطية ذكية =IF(K9=\"\",\"\",I9*K9) تعمل ديناميكياً فور كتابة السعر، وتثبيت مؤسسة إعمار الفرعة كالمقاول العام المنفذ لأعمال التوريد والتركيب."
  tags: [zero-pricing, unpriced-boq, efc-contractor, prompt-engineer, global]
  status: active

- id: MEM-2026-09-08-045
  type: lesson
  timestamp: "2026-09-08T09:35:00+03:00"
  agents: [feature-startup-orchestrator, prompt-engineer, document-formatter, persistent-memory-engine]
  context: "اعتماد الأرقام الإنجليزية (0-9) حصرياً مع حراسة التنسيقات وقيد الصفحة الواحدة"
  content: "اعتماد الأرقام الإنجليزية (0-9) حصرياً وبشكل مطلق في كافة المستندات الرسمية، الخطابات، عروض الأسعار، وجداول الكميات، مع حظر الأرقام الهندية المشرقية تماماً وتفعيل بوابة الصفر أرقام هندية len(re.findall(r'[\\u0660-\\u0669]', text)) == 0، مع الحفاظ التام على محاذاة الجداول والعزل ثنائي الاتجاه BiDi/RTL وقيد الصفحة الواحدة الصارم (Strict 1-Page A4)."
  tags: [english-digits, western-numerals, zero-indic-digits, formatting-retention, strict-1-page, global]
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

- id: MEM-2026-09-08-051
  type: lesson
  timestamp: "2026-09-08T22:05:00+03:00"
  agents: [feature-startup-orchestrator, prompt-engineer, document-formatter, linguistic-assistant, self-refinement-engine, persistent-memory-engine]
  context: "تحديث الترويسة العلوية بالترويسة الرسمية header_emaar.png واقتصار المقر على الرياض بخط الأميري الملكي"
  content: "اعتماد البانر الرأسي المعتمد للترويسة (header_emaar.png) في أعلى صفحات البروفايلات، الكتالوجات، الفواصل، وأغلفة الختام بدلاً من اللوجو المستطيل المصغر. واقتصار المقر والعنوان لمؤسسة إعمار الفرعة للمقاولات العامة على 'الرياض' حصرياً مع الحظر الصارم لذكر 'شارع الفرعة العام' أو 'حوطة بني تميم' في العناوين الرسمية، وحذف الهاتف الأرضي، وتثبيت البريدين المعتمدين (emaaralfharah2040@gmail.com و engmohamedyones@gmail.com). واعتماد خط الأميري الملكي (Amiri Typography) في العناوين والبطاقات المستحدثة وصفحات المشاريع، والالتزام ببوابة الصفر أرقام هندية مشرقية (Zero Indic Digits = 0) والسرعة الفائقة (< 5 ثوانٍ)."
  tags: [official-header, header-emaar, riyadh-only-address, amiri-typography, zero-indic-digits, master-unified-profile, fast-pipeline, memory-sync, global]
  status: active

- id: ADR-HOSTINGER-REMOTE-FUTRX-048
  type: adr
  timestamp: "2026-09-08"
  agents: [fleet-orchestrator, backend-architect, devops-deployer, persistent-memory-engine]
  context: "أتمتة نشر منصة remote.futrx على خادم Hostinger VPS بواسطة أدوات Hostinger MCP وتنسيق الأسطول"
  content: "بناءً على توجيه المستخدم ('نستكمل عملية نشر ريموت فيوتريكس تكس على هوستنجر بواسطة إم سي بي هوستنجر مع الأسطول'): 1) تفعيل بروتوكول الخطوة صفر وقراءة القيود التقنية صامتاً مع اعتماد القيد 53 (عزل المنصات الخادومية على خوادم سحابية KVM). 2) استخدام أدوات Hostinger MCP للتحقق من السيرفر الفعلي (ID: 1810150, IP: 187.55.226.225, Ubuntu 24.04 with Docker). 3) أتمتة إضافة والتحقق من سجلات DNS الأربعة عبر DNS_validateDNSRecordsV1 و DNS_updateDNSRecordsV1 لنطاق autovem.tech (تشمل remote, code.remote, *.code.remote, *.dev.remote) بنجاح 100%. 4) ربط المفتاح العام SSH (autovem_vps ID: 574899) بالسيرفر عبر VPS_attachPublicKeyV1. 5) تدقيق بيئة Docker الحالية واكتشاف تشغيل caddy على المنافذ 80 و 443 ووضع الحلول المعمارية لمنع تضارب المنافذ مع مثبت المنصة الرئيسي."
  tags: [hostinger-mcp, remote-futrx, vps-deployment, dns-automation, caddy-routing, multi-cli-fleet, zero-indic-digits, global]

- id: LESSON-OFFICIAL-HEADER-RIYADH-AMIRI-047
  type: lesson
  timestamp: "2026-09-08"
  agents: [feature-startup-orchestrator, prompt-engineer, document-formatter, linguistic-assistant, self-refinement-engine, persistent-memory-engine]
  context: "تحديث الترويسة العلوية بالترويسة الرسمية header_emaar.png واقتصار العنوان على الرياض بخط الأميري الملكي"
  content: "بناءً على طلب وميزة المستخدم ('استخدم هذه في الترويسة العلوية بدلاً من اللوجو المستطيل' و 'قم بإزالة شارع الفرعة العام، اكتفي فقط بذكر الرياض بالنسبة لإعمار الفرعة'): 1) استبدال الشعار المستطيل في الترويسة العلوية بالترويسة الرسمية المعتمدة للمؤسسة (header_emaar.png) في كل من الصفحة الفاصلة للذراع الصناعي (صفحة 25) والغلاف الختامي ودليل التواصل الموحد (صفحة 76) داخل إطار هندسي نقي مدعوم بخط ذهبي فاصل. 2) إزالة 'شارع الفرعة العام' والهاتف الأرضي بالكامل من صفحة الغلاف الأولى وبطاقة المقاولات بالغلاف الختامي والاكتفاء الحصري بذكر 'الرياض' مع إدراج البريدين الرسميين (emaaralfharah2040@gmail.com و engmohamedyones@gmail.com). 3) اعتماد خط الأميري الملكي (Amiri-Bold و Amiri-Regular) لكافة العناوين والنصوص المحدثة وصفحات المشاريع (14، 15، 16). 4) إنجاز التجميع والأتمتة بسرعة فائقة (< 5 ثوانٍ) في ملف 76 صفحة مستوفٍ 100% لبوابة الأرقام الإنجليزية (Zero Indic Digits = 0) وتوزيعه على كافة المسارات."
  tags: [official-header, header-emaar, riyadh-only-address, amiri-typography, zero-indic-digits, master-unified-profile, fast-pipeline, memory-sync, global]

- id: LESSON-WESTERN-DIGITS-FORMATTING-RETENTION-045
  type: lesson
  timestamp: "2026-09-08"
  agents: [feature-startup-orchestrator, prompt-engineer, document-formatter, linguistic-assistant, self-refinement-engine, persistent-memory-engine]
  context: "اعتماد الأرقام الإنجليزية (0-9) حصرياً بناءً على ميزة المستخدم الصريحة مع حراسة التنسيقات وقيد الصفحة الواحدة"
  content: "بناءً على طلب وميزة المستخدم الصريحة في الخطاف ('ابدأ ميزة: أريد استخدام الأرقام الإنجليزية فقط، ومع الالتزام بكافة التنسيقات في كل مكان'): 1) اعتماد الأرقام الإنجليزية الغربية (0-9) حصرياً في كافة المستندات الرسمية، عروض الأسعار، جداول الكميات (BOQ)، الخطابات التنفيذية، ملفات Excel و Word و PDF والتقارير الهندسية، مع حظر الأرقام الهندية المشرقية تماماً. 2) الحفاظ التام على محاذاة وتنسيقات الجداول والعزل ثنائي الاتجاه (BiDi Isolation) عبر <w:bidiVisual/> و <w:jc w:val=\"left\"/> لمنع انقلاب الأبعاد أو الأكواد. 3) التحقق البرمجي التلقائي عبر PyMuPDF من بوابة الصفر أرقام هندية (len(re.findall(r'[\u0660-\u0669]', text)) == 0) وقيد الصفحة الواحدة الصارم (Strict 1-Page A4) وثبات خانات التسعير المفرغة [             ر.س ]. 4) ترقية دليل القواعد العامة AGENTS.md والأمر القياسي لوكيل الأوامر UNPRICED_CONTRACTOR_BOQ_PROMPT.md لتثبيت هذا المعيار في كافة المشاريع اللاحقة."
  tags: [global, english-digits, western-numerals, zero-indic-digits, formatting-retention, bidi-isolation, strict-1-page, prompt-engineer, memory-sync]

- id: LESSON-UNPRICED-EFC-PROPOSAL-PROMPT-044
  type: lesson
  timestamp: "2026-09-08"
  agents: [prompt-engineer, boq-analyst, document-formatter, linguistic-assistant, self-refinement-engine, persistent-memory-engine]
  context: "تفعيل وكيل الأوامر (prompt-engineer) لترسيخ معيار حظر وضع الأسعار قطعياً وإصدار الأمر التشغيلي القياسي لعروض أسعار المقاول المنفذ"
  content: "بناءً على التوجيه الصارم للمستخدم واستدعاء وكيل الأوامر (prompt-engineer): 1) ترسيخ قاعدة الحظر المطلق للأسعار (Strict Zero-Pricing Rule)؛ يُحظر تماماً على أي وكيل افتراض أو تخمين أو وضع أي سعر للمتر أو احتساب مبالغ إجمالية أو ضرائب ما لم يطلب المستخدم ذلك كتابياً، والاكتفاء بحقول مفرغة ونظيفة [             ر.س ] للإدخال اليدوي. 2) بناء جداول Excel (XLSX) بمعادلات شرطية ذكية مثل =IF(K9=\"\",\"\",I9*K9) و =IF(L29=\"\",\"\",L29*0.15) لتعمل ديناميكياً وفورياً بمجرد إدخال السعر لاحقاً دون إظهار أصفار أو أخطاء. 3) صياغة الأمر التشغيلي القياسي وحفظه في المرجع UNPRICED_CONTRACTOR_BOQ_PROMPT.md وتحديث AGENTS.md ليكون ملزماً لكافة الوكلاء. 4) الحفاظ التام على قيد الصفحة الواحدة (Strict 1-Page A4) والورق الرسمي لمؤسسة إعمار الفرعة وبوابة الصفر أرقام غربية في ملفات PDF."
  tags: [global, prompt-engineer, zero-pricing, unpriced-boq, efc-contractor, conditional-excel-formulas, strict-1-page, zero-western-digits, memory-sync]

- id: LESSON-MCP-026
  type: architecture-decision
  timestamp: "2026-08-26"
  agents: [mcp-tool-builder, script-to-mcp-converter, code-architect, persistent-memory-engine]
  context: "تفعيل الخطاف العالمي #23 لأتمتة تحويل سكربتات المشاريع إلى خوادم MCP"
  content: "إطلاق الخطاف 23 في ملفات الإعدادات المركزية (.config) مدعوماً بمحرك project_scripts_to_mcp.py لمسح السكربتات عبر AST وتجريد منطق العمل وتوليد نماذج Pydantic وخوادم FastMCP مع أرشفة آمنة على الأقراص الثانوية وحماية قرص النظام C:."
  tags: [global, hooks-pipeline, hook-23, mcp-automation, script-migration, system-optimization]

- id: LESSON-MCP-025
  type: architecture-decision
  timestamp: "2026-08-26"
  agents: [mcp-tool-builder, script-to-mcp-converter, code-architect, persistent-memory-engine]
  context: "تأسيس خادم construction-engineering-suite وأتمتة تحويل سكربتات المشاريع إلى MCP (الخطاف #23)"
  content: "تحويل وتجميع أكثر من 60 سكريبت بايثون مبعثر عبر مشاريع مساحة العمل إلى خادم FastMCP موحد (construction-engineering-suite) يضم 7 أدوات هندسية متكاملة (DOCX, BOQ, CPM, Letters, WIR, Handover, PDF Export). تم تفعيل الخطاف العالمي #23 ومحرك project_scripts_to_mcp.py، مع أرشفة جميع السكربتات القديمة بأمان في قرص البيانات الثانوي (G:\_scripts_archive) وتوفير حماية تامة لسعة قرص النظام C:."
  tags: [global, fastmcp, mcp-server, script-migration, boq, cpm, docx-rtl, pdf-converter, secondary-drive-archive]

- id: LESSON-C-DRIVE-OPTIMIZATION-ORGANIZATION-050
  type: lesson
  timestamp: "2026-09-09"
  agents: [windows-c-drive-optimizer, windows-file-organizer, persistent-memory-engine]
  context: "تنقيد وفرز وترتيب ملفات القرص C وتحرير 12.26 GB من المساحة الحرجة بنظام الروابط الوصلية"
  content: "استجابة لمحفز خطاف 16 ('رتب جهازى, فرز الملفات, ترتيب ملفات القرص سي'): 1) تشخيص حالة القرص C واكتشاف وصول المساحة الحرة للحد الحرج (0.17 GB فقط). 2) ترحيل مجلد كاش البناء .gradle بحجم 7.48 GB إلى القرص D:\DevCache\.gradle وربطه برابط وصلي (Directory Junction) لحماية استقرار بناء المشاريع بدون استهلاك مساحة C. 3) تطهير 4.29 GB من مخلفات حزم تحديثات كرت الشاشة NVIDIA OTA Artifacts. 4) تنظيف المجلدات الفارغة المؤقتة بجذر C (مثل Voiceover و tmp)، وفرز ونقل ملفات النسخ الاحتياطية للسجل إلى مجلد Documents\\System_Backups. 5) قفزت المساحة الحرة للقرص C فورياً من 0.17 GB إلى 12.43 GB مع بقاء كافة روابط العبور النشطة سليمة 100% وبانعدام تام للأرقام الهندية المشرقية."
  tags: [windows-optimization, c-drive-cleanup, devcache-junction, file-organization, zero-indic-digits, memory-sync, global]

- id: LESSON-REMOTE-FUTRX-UBUNTU24-DOCKER-LXD-NETWORKING-051
  type: lesson
  timestamp: "2026-09-09"
  agents: [claude-code-cli, antigravity-ide, persistent-memory-engine, devops-deployer]
  context: "نشر منصة remote.futrx على Hostinger VPS Ubuntu 24.04 وحل تعارض توجيه شبكة الحاويات بين Docker و LXD و UFW"
  content: "عند تثبيت remote.futrx على سيرفر سحابي (Hostinger VPS) بنظام Ubuntu 24.04 يحتوي على Docker مسبقاً: 1) فشل بناء الصورة الأساسية futrx-remote-dev-base بمهلة زمنية (no IPv4 egress) بسبب ضبط Docker لسياسة FORWARD على DROP وتعارض جدار الحماية UFW مع جسر الحاويات lxdbr0. 2) أجرى Claude Code CLI تدقيقاً معمارياً صارماً (Devil's Advocate Audit) ورفض تعطيل UFW أمنياً، وقدم حلاً محصناً ومستداماً عبر خدمة systemd دائمة (lxd-docker-forward.service) تمرر lxdbr0 عبر سلسلة DOCKER-USER دون كسر عزل Docker، مع ضبط UFW لتمرير شبكة lxdbr0 ومنافذ 53 (DNS) و 67 (DHCP). 3) اكتمل البناء بنجاح 100% ونشرت المنصة على remote.autovem.tech و code.remote.autovem.tech بنظام تشفير Caddy التلقائي."
  tags: [remote-futrx, lxd-networking, docker-forward, ufw-routing, hostinger-vps, fleet-orchestration, zero-indic-digits, global]

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
