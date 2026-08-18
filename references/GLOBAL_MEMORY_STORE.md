<div dir="rtl">

# 🧠 الذاكرة العالمية المجمعة (Global Memory Store)

> **يحتوي هذا الملف على الذكريات والدروس المستفادة التي تم تصديرها أوتوماتيكياً من مختلف المشاريع.**

```yaml
- id: ADR-2026-08-09-002
  type: decision
  timestamp: '2026-08-09T21:20:00+03:00'
  agents:
  - agent-optimizer
  - persistent-memory-engine
  context: تكرار ظاهرة 'المشاريع فارغة الذاكرة' في المشاريع السابقة مثل تاج الوقار.
  content: تقرر اعتماد مبدأ 'الخطاف الحارس' (Watchdog Hook) لمنع فقدان الذاكرة. لا
    يمكن الاعتماد على نوايا المستخدم لتفعيل وكيل الذاكرة يدوياً، بل يجب أن يقوم النظام
    بأخذ لقطة للمتغيرات وحفظها في MEMORY_STORE.md بشكل إجباري قبل الإغلاق.
  tags:
  - architecture
  - memory
  - watchdog
  - reliability
  - global
- id: MEM-2026-08-09-003
  type: lesson
  timestamp: '2026-08-09T21:20:00+03:00'
  agents:
  - agent-optimizer
  - code-reviewer-quality
  context: مراجعة قوالب التأسيس العالمية واكتشاف ترقيعات (Band-Aids) متراكمة.
  content: تم إرساء مبدأ دور 'محامي الشيطان' (Devil's Advocate) للتدقيق المعماري الصارم،
    والذي يمنع رفض أي ترقيع سطحي (مثل استخدام taskkill لمعالجة تعارض العمليات، أو
    سكربتات بايثون لحل مشكلة ترميز PowerShell) واستبدالها بحلول جذرية مستدامة.
  tags:
  - architecture
  - auditing
  - clean-code
  - global
- id: LESSON-QS-001
  timestamp: '2026-08-13T21:35:00+03:00'
  context: حصر الكميات لفيلا فيريال
  tags:
  - global
  lesson: تحويل المخططات من PDF إلى DWG/DXF يفقد الطبقات الأصلية ويحول النصوص العربية
    والأبعاد إلى Polylines أو أحرف تالفة، ولذلك فإن مسار الجداول التقديرية مع السماكة
    المكافئة للأسقف هو الأسرع والأكثر موثوقية.
- id: LESSON-AGENT-008
  timestamp: '2026-08-14T00:30:00+03:00'
  context: حزمة تأسيس الوكلاء المعيارية وسكربت التهيئة التلقائي (Universal Agent Starter
    Kit)
  tags:
  - global
  - architecture
  - automation
  lesson: تأسيس حزمة معيارية مجردة ومستقلة مع سكربت تهيئة آلي (init_project_agents.py)
    يعتمد على الاكتشاف التلقائي لملفات البيئة والمقابلة التأسيسية، يمنع تلوث السياق
    الموروث (Context Bleeding) ويوحد قواعد الوكلاء الـ 20 عبر جميع المشاريع بنقرة
    واحدة.
- id: ADR-AGENT-004
  type: decision
  timestamp: '2026-08-14T00:30:00+03:00'
  agents:
  - code-architect
  - persistent-memory-engine
  - test-automator
  context: حزمة تأسيس الوكلاء المعيارية (Universal Agent Starter Kit)
  content: بناء حزمة معيارية مجردة تضم 20 وكيلاً و26 مهارة وسكربت تهيئة آلي يدعم الاكتشاف
    التلقائي لبيئات (Android/Web/QS/Python)، مع عزل ملفات المشاريع السابقة لمنع التلوث
    السياقي وضمان استخدام أحدث نماذج 2026 وقواعد lean-ctx عالمياً.
  tags:
  - scaffolding
  - agents
  - starter_kit
  - automation
  - global
  status: active
- id: LESSON-AGENT-009
  timestamp: '2026-08-14T00:39:00+03:00'
  context: اعتماد معمارية الذاكرة اللامركزية الثلاثية وتنظيف الدستور (3-Tier Memory
    Architecture)
  tags:
  - global
  - architecture
  - context-optimization
  lesson: حظر حشو الدروس اليومية في ملف الدستور AGENTS.md لتفادي تضخم نافذة السياق
    وهدر التوكنز وتشتت تركيز النماذج، واعتماد MEMORY_STORE.md كمستودع محلي وحيد للدروس
    مع ترحيل الدروس الحرجة الموسومة بـ global إلى ACTIVE_CONTEXT_INJECTION.md.
- id: ADR-AGENT-005
  type: decision
  timestamp: '2026-08-14T00:39:00+03:00'
  agents:
  - code-architect
  - persistent-memory-engine
  context: هيكلة الذاكرة الثلاثية وترشيق الدستور (3-Tier Memory System)
  content: 'اعتماد الفصل الصارم بين: (1) دستور الوكلاء AGENTS.md للقواعد السلوكية
    الثابتة، (2) القيود العالمية المانعة للأخطاء ACTIVE_CONTEXT_INJECTION.md للخطوة
    صفر، و (3) متجر الذاكرة اللامركزي MEMORY_STORE.md لتسجيل 100% من الدروس والقرارات
    المحلية.'
  tags:
  - memory
  - architecture
  - 3-tier
  - optimization
  - global
  status: active
```

</div>
- id: MEM-2026-08-10-001
  type: lesson
  timestamp: "2026-08-10T19:30:00+03:00"
  agents: [persistent-memory-engine, agent-optimizer]
  context: "تكرار أخطاء مسجلة مسبقاً (استخدام Fully Qualified Names لدوال الامتداد) بسبب تجاهل قراءة الذاكرة."
  content: "مجرد (تسجيل) الذكريات لا يكفي. يقع الوكيل أحياناً في (Shortcut Anti-Pattern) محاولاً اختصار الوقت. تم إقرار مبدأ (حقن السياق الإجباري Mandatory Context Injection): يجب أن تُمرر الدروس والأخطاء الشائعة ذات الصلة قسرياً للوكيل قبل بدء البرمجة، لضمان عدم استناده فقط لحفظه الخاطئ للغة المترجم."
  tags: [architecture, global, agent-behavior, context-injection, anti-pattern]
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

