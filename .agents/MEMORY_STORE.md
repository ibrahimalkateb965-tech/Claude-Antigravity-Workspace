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

- id: MEM-2026-08-20-001
  type: lesson
  timestamp: "2026-08-20T18:28:00+03:00"
  agents: [persistent-memory-engine, frontend-design-builder, agent-optimizer]
  context: "تحديث وتزامن كائن libraryData في تطبيق مكتبة الأوامر HTML وشيتات الإكسيل للـ 21 خطافاً"
  content: "عند إضافة أو تعديل أو إعادة ترقيم أي خطاف في `HOOKS_GUIDE.md`، يجب تشغيل سكربت `convert_hooks_to_sheets.py` لتحديث مصنف الإكسيل بـ N+1 ورقة عمل (22 شيت)، وتشغيل `update_html.py` لتحديث كائن `libraryData` داخل تطبيق الويب `03_Dynamic_Prompt_Library/index.html` لضمان ظهور التبويبات والبطاقات وأزرار النسخ الفوري بشكل متطابق ومتزامن."
  anti_pattern_avoided: "عدم تزامن واجهة الويب ومصنف الإكسيل مع الدليل الفني المحدث للخطافات (Docs & UI Drift)."
  tags: [dynamic-prompt-library, excel-sync, html-ui, full-parity, automation]
  status: active
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

- id: ADR-2026-08-20-001
  type: decision
  timestamp: "2026-08-20T18:28:00+03:00"
  agents: [code-architect, persistent-memory-engine, agent-optimizer]
  context: "تخصيص الخطاف رقم 3 مستقلاً للتخصيص والتصدير الذكي لطاقم المشروع وترقية المنظومة لـ 21 خطافاً"
  content: "تم فصل عملية تخصيص الوكلاء والمهارات إلى خطاف مستقل بذاته (الخطاف رقم 3: خطاف التخصيص والتصدير الذكي لطاقم المشروع) بدلاً من دمجه ضمن خطاف هندسة السياق، لتوسيع المنظومة لتصبح 21 خطافاً تخصصياً. يتولى هذا الخطاف استدعاء محرك `project_agent_tailor.py` لتحليل سياق المشروع المعتمد، وتصدير حزم الوكلاء المطلوبة فقط، وإجراء تنظيف تلقائي (Pruning) لأي مهارات فائضة، وتوليد سكربت المزامنة المحلي `sync_local_agents.py` لترشيد نافذة السياق 100%."
  anti_pattern_avoided: "تضخم المهارات غير المستخدمة داخل مجلدات المشاريع المحلية (Context Window Bloat) وتشتت الوكلاء واستنزاف التوكنز."
  tags: [hooks-architecture, project-agent-tailor, 21-hooks, pruning, context-window-optimization]
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

- id: PREF-2026-08-20-001
  type: preference
  timestamp: "2026-08-20T18:28:00+03:00"
  agents: [persistent-memory-engine, code-architect]
  context: "تنظيف وحذف المهارات والوكلاء الفائضين آلياً في المشاريع المستهدفة"
  content: "اعتماد المسح والتنظيف التلقائي الصارم (Pruning) للمهارات والوكلاء الفائضين في بيئات المشاريع المحلية فور اعتماد سياق المشروع، مع الحفاظ فقط على حزمة النواة والحزم التخصصية المعتمدة لحماية نافذة السياق من الاستنزاف."
  tags: [user-preference, pruning, context-budget]
  status: active

- id: PREF-2026-08-20-002
  type: preference
  timestamp: "2026-08-20T19:10:00+03:00"
  agents: [persistent-memory-engine, ui-ux-design-lead, frontend-design-builder]
  context: "معايير تصميم وتوليد الوثائق المكتوبة وملفات PDF لكافة المشاريع"
  content: "في كافة المشاريع التي تحتوي على وثائق مكتوبة أو ملفات PDF: 1) استخدام نفس لوحة ألوان وهوية مكتبة الأوامر التفاعلية (Dark Neon Candy Palette: #0a0a14 مع لمسات Neon Purple, Cyan, Pink, Green وبطاقات زجاجية Glassmorphism). 2) منع المسافات الفارغة الكبيرة والصفحات البيضاء/الفارغة تماماً. 3) ضغط وتنسيق المحتوى ليحتل أقل عدد ممكن من الصفحات مع مراعاة راحة العين البصرية وعدم تكدس الكلام عبر التوزيع الشبكي والخطوط المتناسقة (Tajawal / Outfit / Cairo)."
  anti_pattern_avoided: "المسافات البيضاء المهدرة، الصفحات الفارغة العالقة، تباين الهوية البصرية، وتكدس النصوص غير المريح للعين."
  tags: [pdf-design, prompt-library-palette, minimal-pages, ergonomic-layout, no-blank-pages, universal-rule]
  status: active

- id: ARCH-2026-08-20-003
  type: architectural_decision
  timestamp: "2026-08-20T23:54:00+03:00"
  agents: [code-architect, prompt-engineer, persistent-memory-engine]
  context: "توحيد مصطلح Hook ومبدأ الأسطول الهندسي القابل للتوسع اللانهائي عبر المنظومة"
  content: "1) اعتماد مصطلح 'Hook' كمعيار تقني موحد بدلاً من 'خطاف' في كافة الوثائق والواجهات، مع الحفاظ على المحفزات التلقائية باللغة العربية. 2) ترسيخ مبدأ أن أسطول الوكلاء الـ 25+ والـ 80+ مهارة هو نواة مرنة تتوسع وتتمدد ديناميكياً لاستيراد وبناء مهارات جديدة فورياً عبر Hook 20 و Hook 9 ومحرك بناء المهارات حسب متطلبات كل مشروع دون أي جمود."
  tags: [hook-nomenclature, dynamic-scaling, extensible-fleet, hook-20, standardization]
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
| **أدوات التطوير والتخصيص** | 5 | skill-forge-builder, docs-fetcher-context, mcp-tool-builder, webapp-qa-tester, project-agent-tailor |
| **التصميم والعلامة** | 6 | ui-ux-design-lead, taste-design-critic, motion-transitions-pro, frontend-design-builder, web-artifacts-prototyper, brand-kit-keeper |
| **التسويق والنمو** | 5 | copywriting-lead, ai-geo-seo-optimizer, cro-conversion-lead, ad-creative-maker, customer-research-voice |
| **صناعة المحتوى** | 3 | post-content-writer, script-hook-generator, profile-optimizer |
| **المالية** | 6 | financial-statements-builder, journal-entry-keeper, reconciliation-auditor, variance-analyst, audit-support-prep, close-management-lead |
| **الأعمال** | 6 | cash-flow-watcher, invoice-chaser, payroll-planner, margin-analyst, tax-prepper, campaign-runner |
| **القانون** | 6 | contract-reviewer, nda-triage, compliance-officer, legal-risk-assessor, vendor-vetter, signature-wrangler |
| **حراس الجودة** | 4 | clean-code-guard, test-guard, docs-guard, security-auditor |
| **الوكلاء المتخصصون** | 25+ | (راجع sub_agents.yaml للقائمة الكاملة) |

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

- id: MEM-2026-08-18-001
  type: lesson
  timestamp: "2026-08-18T20:16:00+03:00"
  agents: [persistent-memory-engine, code-architect, android-kotlin-pro]
  context: "استمرار تشغيل التلاوة في الخلفية عند انطفاء الشاشة في وضع الاستماع المتواصل"
  content: "عند تفعيل وضع الاستماع المتواصل، يجب ألا تقوم واجهة المستخدم (Activity) بإيقاف التلاوة قسرياً عند أحداث دورة الحياة (ON_PAUSE / ON_STOP). الحل المعماري: فحص حالة isContinuousPlayEnabled في LifecycleEventObserver والسماح لخدمة QuranAudioService (MediaSessionService) بمواصلة البث وتمرير الآيات تلقائياً في الخلفية أثناء إغلاق الشاشة.\n[ANTI-PATTERN AVOIDED]: الإيقاف الشامل غير المشروط للتلاوة في ON_PAUSE مما يحرم المستخدم الكفيف من الاستماع عند إقفال الهاتف."

- id: MEM-2026-08-18-002
  type: bug-fix
  timestamp: "2026-08-18T20:38:00+03:00"
  agents: [persistent-memory-engine, debugger, jetpack-compose-ui]
  context: "منع إعادة تعيين موضع التلاوة للآية القديمة عند إعادة فتح التطبيق من الخلفية (Pager Resume Desync)"
  content: "عندما تتقدم التلاوة في الخلفية (عبر ExoPlayer)، تتغير حالة ViewModel.currentAyahIndex، لكن PagerState في واجهة المستخدم المتوقفة يظل محتفظاً بالصفحة القديمة. عند فتح التطبيق، كان snapshotFlow يرى اختلافاً بين PagerState (القديم) و ViewModel (الجديد) فيظن أن المستخدم سحب الشاشة ويطلب goToAyah للصفحة القديمة مما يعيد التلاوة للبداية! الحل الجذري: تتبع سحب المستخدم صراحة (userScrolled) بحيث لا يقوم PagerState بإرسال أمر goToAyah إلا إذا كان التغيير ناتجاً عن سحب يدوي فعلي من المستخدم، ومزامنة PagerState فورا لصفحة ViewModel الحالية عند الاستئناف.\n[ANTI-PATTERN AVOIDED]: ربط snapshotFlow في PagerState بتغيير ViewModel دون التحقق من كون الحدث ناتجاً عن سحب المستخدم (User-Initiated Scroll)."
  tags: [compose, pager, snapshotflow, lifecycle, background-playback, bug-fix]
  status: active

- id: ADR-2026-08-01-001
  type: decision
  timestamp: "2026-07-21T11:00:00+03:00"
  agents: [code-architect, agent-optimizer]
  context: "اختيار استراتيجية النماذج لنظام Autovem"
  content: "تم اعتماد النظام الثلاثي: flash للمحتوى والتسويق والمالية والقانون، pro للبرمجة والمراجعة والبناء، thinking للمعمارية والتخطيط والذاكرة"
  tags: [model-strategy, autovem-core]
  status: active

- id: ADR-2026-08-23-001
  type: decision
  timestamp: "2026-08-23T07:14:00+03:00"
  agents: [brand-kit-keeper, code-architect, persistent-memory-engine]
  context: "اعتماد الصورة الحصرية لأيقونة التطبيق والهوية البصرية"
  content: "الصورة المعتمدة الوحيدة والنهائية لكافة أيقونات التطبيق، المتجر، الويب، وبطاقة Google Play هي: `124864.jpg.jpeg`. تم توليد كافة المقاسات (Mipmap densities: mdpi إلى xxxhdpi، وأيقونة المتجر 512×512) منها مباشرة."
  tags: [visual-identity, icon, branding, rule]
  status: active

- id: ADR-2026-08-09-002
  type: decision
  timestamp: "2026-08-09T21:05:00+03:00"
  agents: [persistent-memory-engine, agent-optimizer]
  context: "اعتماد الخطاف الحارس (Watchdog Hook) لمنع فقدان الذاكرة"
  content: "اكتشفنا ظاهرة (المشاريع فارغة الذاكرة) في تطبيقي (تاج الوقار) و(تيجان النور) بسبب عدم تفعيل المستخدم لخطاف حفظ الذاكرة في نهاية الجلسة. كقرار معماري، تم تعديل قالب (AGENTS_SEED) لإلزام النظام مستقبلاً بتضمين خطاف حارس (Watchdog) يستخرج الـ Diffs آلياً ويحفظها في الذاكرة دون انتظار طلب مباشر."
  tags: [memory-architecture, compliance, watchdog-hook]
  status: active

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

- id: MEM-ANTIGRAVITY-CONTEXT-MONITOR-2026-09-13-008
  type: discovery
  timestamp: "2026-09-13T14:49:00+03:00"
  agents: [prompt-engineer, code-architect, persistent-memory-engine]
  context: "اكتشاف وبناء أداة المراقبة اللحظية لنافذة سياق Antigravity IDE (Context Window Monitor)"
  content: "1) تم بالهندسة العكسية فك تشفير بروتوكول Protobuf في قواعد بيانات المحادثات لـ Antigravity IDE الواقعة في `~/.gemini/antigravity-ide/conversations/*.db`. حقل `step_payload` لخطوات النموذج (step_type=15) يحمل بدقة متناهية تحت الوسم 0x4A: الـ input_tokens (سياق المحادثة الفعلي)، و output_tokens، و thinking_tokens. 2) تم بناء ونشر أداة طرفية خفيفة بدون أي مكتبات خارجية `ag_context_monitor.py` وأمر النظام المباشر `ag-context` مع دعم الوضع اللحظي (`--watch`)، وسجل المنحنى التاريخي (`--history`)، ومخرجات JSON، والتحذير الملون عند تجاوز عتبات الاستهلاك (60% / 80% / 90%). 3) الاتصال بقواعد بيانات المحادثات يتم حصرياً بنمط القراءة فقط `mode=ro` مع `PRAGMA query_only = ON` لمنع أي تعارض أو قفل مع المحرر أثناء التشغيل."
  tags: [antigravity-ide, context-window, telemetry, token-monitor, reverse-engineering, ag-context, hook-05]
  status: active

- id: MEM-FLEET-STRATEGIC-CLEAR-AGCLI-2026-09-12-007
  type: adr
  timestamp: "2026-09-12T10:40:00+03:00"
  agents: [fleet-orchestrator, prompt-engineer, persistent-memory-engine]
  context: "اعتماد بروتوكول التصفير الاستراتيجي وعزل تفويض Antigravity CLI عن بيئة Antigravity IDE"
  content: "1) اعتماد مصطلح وبروتوكول 'التصفير الاستراتيجي' (Strategic Clear) تحت الخطاف 25 والقالب 04 لضمان حسم المهام الحرجة وتثبيت الكود في حالة مستقرة خالية من أخطاء البناء وعمل Git Commit نظيف قبل مسح السياق أو التصفير. 2) الفصل المعماري التام بين الأدوات الطرفية (Headless CLIs) وبيئة العمل التفاعلية: حصر التكليفات البرمجية لأسطول الـ CLIs في الأدوات الطرفية المستقلة (Claude Code CLI, OpenCode CLI, Antigravity CLI agcli عبر agcli run)، وحظر التكليف لـ Antigravity IDE لحماية الأتمتة المباشرة ومنع الحاجة للنسخ واللصق اليدوي."
  tags: [fleet-orchestration, strategic-clear, hook-25, template-04, agcli-decoupling, antigravity-cli, autonomous-dispatch]
  status: active
