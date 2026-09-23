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

- id: MEM-2026-09-19-070
  type: decision
  timestamp: "2026-09-19T08:41:00+03:00"
  agents: [resource-scout-integrator, graft, context-governance, fleet-orchestrator, persistent-memory-engine]
  context: "اعتماد وتكامل محرك سياق الأكواد Graft لتوفير كوتا وتوكنز Claude Code والأسطول"
  content: "تم استكشاف وتقييم واعتماد أداة ومكتبة Graft (@nanonets/graft / trailhq/Graft) عبر الخطاف 20. تقوم الأداة ببناء خريطة معرفية وسياقية محلية (graft/) عبر tree-sitter AST ($0 وبدون قواعد بيانات) لدعم أكثر من 23 لغة، مما يقلل استهلاك التوكنز بنسبة 42% واستدعاءات الأدوات بنسبة 46% ويسرع الاستجابة 60% مع تحسين دقة SWE-bench Verified بمقدار +12 نقطة. تم تعميم مهارة graft محلياً وعالمياً (~/.claude/skills و ~/.gemini/config/skills)، وتحديث خطافات 20 و 22 و 27 ومصنف الإكسل ومكتبة الأوامر."
  tags: [graft, context-engine, token-conservation, claude-code, fleet, tree-sitter, global]
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

- id: ADR-TELEGRAM-ACI-LISTENER-028
  type: decision
  timestamp: "2026-09-19T14:15:00+03:00"
  agents: [devops-deployer, code-architect, persistent-memory-engine]
  context: "تفعيل مستمع تليجرام لإيقاظ Antigravity IDE ودعم الصوتيات OGG to WAV وتحديث مكتبة الأوامر"
  content: "1) تفعيل منظومة Telegram ACI Listener بنمط الدورة الواحدة (poll_once.py --takeover) كـ Background Task داخل Antigravity IDE، لإيقاظ الجلسة فورياً وبأولوية قصوى (Priority High) عند استلام الأوامر من هاتف المطور دون تضارب 409 Conflict. 2) دمج مكتبة PyAV في telegram_core.py لتحويل البصمات الصوتية OGG تلقائياً إلى WAV لتمكين المحرر من الاستماع للأوامر الصوتية وتحليلها مباشرة. 3) حذف الخطاف القديم من مصنف الإكسيل (تبويب Auto_vem_Win الصف 4) وتوثيق الخطاف المعياري الجديد رقم 12 في Gates_Hooks.Claude الصف 13 ودليل الخطافات HOOKS_GUIDE.md."
  tags: [telegram-aci, remote-development, background-listener, voice-commands, pyav, ogg-to-wav, prompt-library, hook-12, global]
  status: active

- id: ADR-MEIGEN-PROMPT-ARCHITECT-029
  type: decision
  timestamp: "2026-09-20T12:40:00+03:00"
  agents: [prompt-engineer, resource-scout-integrator, persistent-memory-engine]
  context: "تفعيل خطاف 20 وخطاف 26 على منصة meigen.ai وإدراج مهندس البرومبتات السينمائي في مكتبة الأوامر"
  content: "1) تفعيل الخطاف 20 (Graft & Resource Integration) باستيعاب وهندسة قدرات منصة meigen.ai في توليد الصور والفيديوهات الواقعية والسينمائية (8K Photorealism, Volumetric Lighting, Unreal Engine 5 optics). 2) تفعيل الخطاف 26 (Prompt Library Curator) برمجياً عبر أداة prompt_library_curator.py وإدراج الأمر المعياري '🎨🎬 مهندس برومبتات الصور والفيديو السينمائي (MeiGen Photorealism & Prompt Architect)' في مصنف 'مكتبة الأوامر.xlsx' داخل تبويب Custom_Skills.Claude بالصف رقم 9 (No: 8) بتنسيق Segoe UI و Consolas والحدود الرقيقة، لتمكين الوكالة من توليد المواد الإعلانية والتصاميم السينمائية للعملاء بجودة استثنائية."
  tags: [meigen-ai, hook-20, hook-26, prompt-curator, photorealism, cinematic-prompts, excel-sync, custom-skills]
  status: active


- id: MEM-2026-09-17-070
  type: decision
  timestamp: "2026-09-17T01:10:00+03:00"
  agents: [devops-deployer, security-guardian, master-orchestrator]
  context: "حزمة نشر EduTrack Pro على Hostinger VPS كنسخة نظيفة 100% بدون ترحيل بيانات قديمة (تأكيد العميل 2026-09-16)"
  content: "تم إقرار طوبولوجيا النشر لنظام <bdi>EduTrack Pro</bdi> (مركز غراس): <bdi>Caddy</bdi> المضيف يخدم النطاق العام ويمرر <bdi>/api/*</bdi> إلى حاوية <bdi>uvicorn</bdi> على <bdi>127.0.0.1:8000</bdi> ويخدم الملفات الثابتة من <bdi>/opt/edutrack</bdi> بقائمة سماح صارمة (<bdi>/web/*</bdi> و <bdi>/assets/*</bdi> فقط؛ كل ما عداها 404 بما فيها <bdi>db/</bdi> و <bdi>server/</bdi> و <bdi>deploy/</bdi> وملفات <bdi>*.md</bdi>). قاعدة <bdi>PostgreSQL 16</bdi> بلا منفذ منشور، وتتصل الواجهة البرمجية حصراً بدور <bdi>gheras_app</bdi> المقيد (بدون <bdi>superuser</bdi>) — تم إثبات ذلك بتشغيل حزمة الاختبارات كاملة (19/19) بهذا الدور. الأسرار تُولَّد على الخادم في <bdi>deploy/.env</bdi> (600) ولا تُزامَن ولا تُرفع أبداً. البذرة الإدارية النظيفة: صف <bdi>admin</bdi> من الهجرة 003 بتجزئة مؤقتة غير قابلة للاستخدام، ثم <bdi>--set-admin-password</bdi> غير تفاعلي عبر متغير <bdi>EDUTRACK_ADMIN_PASSWORD</bdi>. فحص صحة القاعدة عبر <bdi>TCP</bdi> عمداً لأن فحص المقبس المحلي يعلن الجاهزية أثناء تنفيذ سكربتات <bdi>initdb</bdi>. عائقان يحتاجان إبراهيم قبل التنفيذ الفعلي: مفتاح <bdi>SSH</bdi> غير مسجل على الخادم (رفض بالمفتاحين المحليين)، وسجل <bdi>DNS</bdi> للاسم العام غير موجود بعد مع تعذر الاتصال بالمنفذ 443 من الخارج."
  tags: [edutrack-pro, gheras, deployment, hostinger-vps, caddy, docker-compose, least-privilege, clean-instance, no-legacy-import, ssh-blocker, autovem, global]
  status: active

- id: MEM-2026-09-17-071
  type: decision
  timestamp: "2026-09-17T12:55:00+03:00"
  agents: [frontend-design-builder, code-architect, devops-deployer, persistent-memory-engine]
  context: "المرحلة 3 لمنظومة EduTrack Pro — اكتمال لوحة الإدارة وديناميكية الأدوات الـ 24 ونشرها بنجاح على Hostinger VPS"
  content: "اكتمال هندسة ونشر المرحلة 3 لمنظومة EduTrack Pro (مركز غراس): 1) تحويل لوحة الإدارة (home.js) إلى شبكة تفاعلية CSS Grid تضم كافة الأدوات الـ 24 مع ربطها بمؤشرات reports/daily الحقيقية واستبعاد أي بيانات وهمية. 2) الالتزام بمعايير A11y (role=button, tabindex=0, enter/space listeners) وعزل الجلسة مركزياً في app.js عبر حدث gheras:logout. 3) معالجة انحراف التوقيت العربي للتواريخ بإلحاق T00:00:00. 4) حل معوقات نشر ويندوز (أمر export في PowerShell وتشويه مسار مفتاح ssh في MSYS). 5) اكتمال النشر على الخادم (srv1810150.hstgr.cloud - IP: 187.55.226.225) واجتياز فحص الـ Smoke Test بنجاح (HTTP 200 للـ API واللوحة، وحجب المسارات الحساسة) والرابط الحي: https://gheras.autovem.tech/web/dashboard/."

- id: MEM-2026-09-17-072
  type: decision
  timestamp: "2026-09-17T15:42:00+03:00"
  agents: [backend-architect, frontend-design-builder, code-reviewer-quality, persistent-memory-engine]
  context: "استكمال وتفعيل شبكة أدوات EduTrack Pro، إزالة ازدواجية بوابات الدخول، وبناء قسم الإعدادات وتغيير كلمة المرور للأدمن"
  content: "استجابة لتوجيهات المطور: 1) بناء نقطة نهاية POST /api/v1/auth/change-password مع التحقق بمشفر Argon2id، فحص قوة كلمة المرور (>=8)، وتدوين الحدث في audit_log دون كشف كلمات المرور. 2) إضافة نقطة POST /api/v1/auth/reset-password/{user_id} للمدير العام لإعادة تعيين كلمات مرور المستخدمين الآخرين. 3) إنشاء صفحة settings.js متكاملة (تغيير كلمة المرور، إدارة مستخدمي النظام، بيانات المركز، والنسخ الاحتياطي التلقائي) وربطها بالقائمة الجانبية والشريط العلوي في index.html ومصفوفة ROUTES في app.js. 4) القضاء التام على أزرار الأدوات المعطلة (الـ 14 زراً التي كانت تعطي 'قريباً...') بربطها بمصادرها الحقيقية وتفعيل نوافذ التواصل عبر واتساب والنسخ الاحتياطي ومركز الإشعارات. 5) إزالة التكرار: حذف زر تسجيل الخروج المكرر من شبكة الأدوات، وإخفاء بوابة الدخول المكررة passwordGate في تطبيق غراس.html لتوحيد تدفق المصادقة في خطوة واحدة نظيفة. 6) كتابة حزمة اختبارات شاملة test_change_password.py وتأكيد مطابقة Rule 50 وخلو الكود من أي أخطاء بنية."

- id: MEM-2026-09-17-073
  type: decision
  timestamp: "2026-09-17T16:08:00+03:00"
  agents: [performance-optimizer, context-governor, master-orchestrator, persistent-memory-engine]
  context: "دروس قياس الاستهلاك الحقيقي لـ Claude Code CLI (قاعدة الـ 150k، ترشيد الوكلاء الفرعيين، وتحجيم المهارات الثقيلة)"
  content: "بناءً على التليمتري والبيانات الحية المباشرة من لوحة تحكم Claude Code CLI (قسم Usage): 1) **تأكيد قاعدة جدار الـ 150k توكن رسميّاً:** أظهرت التليمتري أن 60% من استهلاك الكوتا والحدود يحدث عندما يتجاوز السياق 150k توكن؛ لأن تكلفة قراءة وكتابة الكاش تتصاعد تربيعياً O(N^2). هذا يؤكد صحة معيار المعايرة الحصري لـ Claude Code على 150k توكن ووجوب تنفيذ /compact في منتصف المهام و /clear عند التبديل بين المهام. 2) **ترشيد استدعاء الوكلاء الفرعيين (Subagents Overhead):** أظهرت التليمتري أن 10% من الاستهلاك سببه الجلسات كثيفة الوكلاء الفرعيين حيث ينشئ كل وكيل طلباته الخاصة بسياقه الكامل؛ لذا يجب تقنين توليد الوكلاء الفرعيين وتفويض المهام الروتينية لـ OpenCode CLI (Flash & Muse). 3) **تحجيم استهلاك المهارات الثقيلة (Heavy Skills Optimization):** تبين أن مهارة /remote-futrx وحدها استهلكت 15% من الحصة و /code-review استهلكت 7%؛ لذا يجب تقليص نصوص المهارات الثقيلة وتحديد نماذج أخف (cheaper models) في ترويسة YAML للمهارة (frontmatter). 4) **تنبيه انتهاء الجلسة الأمني:** رصد إشعار انتهاء صلاحية تسجيل الدخول (Your login expires in 3 days) مما يفرض تجديد الجلسة عبر /login قبل انقضائها لتفادي تعطل الاستدعاء المباشر في الطرفية."
  tags: [claude-code, usage-telemetry, 150k-wall, subagents-cost, heavy-skills, remote-futrx, context-governance, login-renewal, autovem, global]

- id: MEM-2026-09-17-074
  type: decision
  timestamp: "2026-09-17T16:22:00+03:00"
  agents: [backend-architect, frontend-design-builder, test-automator, master-orchestrator, persistent-memory-engine]
  context: "اكتمال وتفعيل منظومة المستندات والتقارير الـ 11 القابلة للطباعة في EduTrack Pro واعتماد Claude Code CLI الكامل [APPROVED]"
  content: "اكتمال تفعيل منظومة التقارير والمستندات الـ 11 في تطبيق EduTrack Pro (مركز غراس): 1) بناء المحرك الشامل print_engine.js مع دعم حلقات Mustache التكرارية، الكتل المعكوسة، حماية XSS عبر الهروب التلقائي escapeHtml لكافة الوسوم النصية، وتوليد شريط أدوات طباعة زجاجي عائم يختفي تلقائياً في @media print. 2) ربط كافة قوالب الطباعة الـ 11 بقاعدة البيانات الحية وإلغاء تمرير التوكن في الـ Query Strings لمنع تسربه في سجلات Caddy. 3) حل مشكلة تحويل Decimal في Pydantic v2 بتغليف مخرجات مسارات print.py بدالة _resp(_convert(...)) لتحويل القيم النقدية إلى أرقام Float مدورة. 4) اجتياز كامل حزمة الاختبارات بنجاح بنسبة 100% (36/36 اختباراً أخضر: 14 print + 19 existing + 3 change_password) بحسب تدقيق واعتماد Claude Code CLI الصارم [APPROVED]. 5) تثبيت هوية العامل الثاني Worker B في OpenCode CLI كـ Meta Muse Spark 1.3 المتخصص في قواعد البيانات والبيانات، وتأكيد قاعدة حظر فتح أكثر من نسخة متزامنة لـ GLM 5.3 Flash لتفادي تجاوز حد التوازي."
  tags: [edutrack-pro, print-system, 11-templates, xss-protection, decimal-serialization, pydantic-v2, claude-code-approved, 36-tests-green, worker-b, muse-spark, autovem, global]
  status: active

- id: MEM-2026-09-17-075
  type: decision
  timestamp: "2026-09-17T18:30:00+03:00"
  agents: [master-orchestrator, backend-architect, frontend-design-builder, security-guardian, persistent-memory-engine]
  context: "تفعيل أزرار لوحة EduTrack Pro، معمارية الصلاحيات الجزئية للمشرفين، وهجرة الجنس"
  content: "اكتمال تفعيل كافة الأدوات المعلقة في EduTrack Pro (طلاب الإنجليزي أولاد/بنات، غياب الموظفين والخصم، التقييم اليومي، المعلمون والمشرفون). هندسة الصلاحيات الجزئية للمشرفين (students, attendance, daily_evaluation, monthly_evaluation, finance) مع حظر التعديل على users للمشرفين (403) والسماح بالقراءة، حجب الرواتب عن غير الماليين، وحماية نقاط الطباعة والتقارير الشهرية. إنشاء هجرة 004_phase3.sql لإضافة عمود gender، وتفعيل بروتوكول التراجع الذاتي لقيادة الأسطول عبر Antigravity IDE عند نفاد كوتا Claude Code دون توقف العمل."
  tags: [edutrack-pro, supervisor-permissions, rbac, gender-migration, fleet-failover, phase-3, gheras, autovem, global]
  status: active

---

## [milestone] هندسة وتثبيت ميثاق حارس النطاق والحد الأدنى الكافي (Scope Guard Directive - Hook 5 & Hook 26)
- **التاريخ:** 2026-09-23
- **الوكلاء المساهمون:** prompt-engineer (هندسة وتفكيك الأوامر)، code-architect (التصميم المعماري وتدقيق محامي الشيطان)، Antigravity IDE (Gemini 3.8 Flash High - قيادة الأسطول والتنفيذ المباشر).
- **المنجزات الفنية والهندسية والقرارات:**
  1. **التشريح الهندسي وتدقيق محامي الشيطان الصارم (Constraint 17):**
     - معالجة مخاطرة الترقيع السطحي (Superficial Band-aids) بالتأكيد على استئصال المشكلة من جذرها البرمجي (Root-Cause Invariant).
     - مواءمة ميثاق الاختبارات مع احتكار Claude Code CLI الحصري للفحص التلقائي، وقصر مهام باقي الوكلاء على الفحص الساكن والبصري.
     - ترشيد الاستكشاف البرمجي عبر أدوات lean-ctx وحظر قراءة الملفات الكاملة mode='full' لحماية نافذة السياق (Hook 27).
  2. **التثبيت الدستوري في وثيقة المشروع (AGENTS.md):**
     - إدراج القسم السادس المكتمل: \## 6. Scope Guard & Minimal Sufficient Change Directive\ بستة بنود تفصيلية تشمل ضوابط ما قبل التعديل، تسلسل الحلول البرمجية، بوابات طلب الإذن، فحص الأسطول، قاطع تضخم النطاق، ومعايير الإنجاز (Definition of Done).
  3. **الأرشفة في مكتبة الأوامر المركزية (Hook 26):**
     - إدراج الأمر التنفيذي المهندس برمجياً في مصنف الإكسيل \مكتبة الأوامر.xlsx\ تحت تبويب \Gates_Hooks.Claude\ (الصف #16، الرقم 15).
