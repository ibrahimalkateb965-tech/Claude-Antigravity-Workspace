<div dir="rtl">

# 🚀 منظومة Claude & Antigravity - بيئة التطوير متعددة الوكلاء (Multi-Agent Operating System)

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Architecture: Dual-Agent Pipeline](https://img.shields.io/badge/Architecture-Dual--Agent%20Pipeline-emerald.svg)]()
[![Sub-Agents: 25+](https://img.shields.io/badge/Sub--Agents-25%2B-purple.svg)]()
[![Global Skills: 80+](https://img.shields.io/badge/Global%20Skills-80%2B-gold.svg)]()
[![Hooks: 21 Triggers](https://img.shields.io/badge/Hooks-21%20Triggers-cyan.svg)]()

مرحباً بك في المستودع المرجعي الشامل لمنظومة التطوير المتقدمة **Claude & Antigravity Multi-Agent Ecosystem**.  
تم تصميم هذه المنظومة لتمكين المطورين من تشغيل طاقم هندسي متكامل من وكلاء الذكاء الاصطناعي (AI Crew) بأعلى درجات الانضباط المعماري، والجودة البرمجية، وترشيد استهلاك التوكنز.

---

## 🌟 المعمارية الهندسية ونموذج العمل (Dual-Agent Pipeline)

تعتمد المنظومة على مبدأ **فصل المسؤوليات والتكامل المزدوج**:
* **المهندس المخطط والمشرف (Claude / Thinking Tier):** يتولى التخطيط المعماري، تحليل المسارات، التدقيق الصارم، وهندسة السياق والذاكرة.
* **المهندس المنفذ والمراجع (Antigravity / Execution Tier):** يتولى العمليات البرمجية الدقيقة، بناء الواجهات (Compose / Web)، كتابة الاختبارات، ومراقبة الكود النظيف.

```mermaid
flowchart TD
    User["المطور / المستخدم"] -->|إرسال محفز الخطاف مثل 'ابدأ ميزة'| HookSystem["نظام الخطافات التلقائي (Auto-Hooks Engine - 21 خطافاً)"]
    HookSystem -->|تفعيل الدور| Architect["Code Architect (التصميم الهيكلي)"]
    Architect -->|إعداد الخطة| Devil["محامي الشيطان (Devil's Advocate Audit)"]
    Devil -->|اعتماد الخطة بنجاح| Coders["فريق البرمجة (Kotlin / Compose / Backend)"]
    Coders -->|فحص الكود النظيف| CleanGuard["Clean Code Guard (مكافحة أخطاء AI)"]
    CleanGuard -->|فحص واختبار السلوك| TestGuard["Test Guard (منع الـ Mock المفرط)"]
    TestGuard -->|مطابقة التوثيق| DocsGuard["Docs Guard (منع تباين التوثيق)"]
    DocsGuard -->|تخزين الدروس والمزامنة| Memory["Persistent Memory & Git Sync"]
```

---

## ⚡ التشغيل السريع على أي جهاز (Quickstart Guide)

لتثبيت وتفعيل هذه المنظومة على جهازك الخاص في دقائق معدودة:

### 1. استنساخ المستودع
```bash
git clone https://github.com/ibrahimalkateb965-tech/Claude-Antigravity-Workspace.git
cd Claude-Antigravity-Workspace
```

### 2. تفعيل المهارات والوكلاء عالمياً (Global Deployment)
لجعل جميع المهارات والوكلاء متاحة لكافة مشاريعك في بيئة Antigravity:
* **لمستخدمي Windows:**
  قم بنسخ مجلد المهارات والوكلاء إلى مسار الإعدادات العالمي للمحرر:
  ```powershell
  # إنشاء المجلدات إن لم تكن موجودة
  New-Item -ItemType Directory -Force -Path "$HOME\.gemini\config\skills"
  New-Item -ItemType Directory -Force -Path "$HOME\.gemini\config\Sub_Agent"

  # نسخ المهارات والوكلاء
  Copy-Item -Recurse -Force .agents\skills\* "$HOME\.gemini\config\skills\"
  Copy-Item -Recurse -Force .agents\Sub_Agent\* "$HOME\.gemini\config\Sub_Agent\"
  Copy-Item -Force .agents\HOOKS_GUIDE.md "$HOME\.gemini\config\HOOKS_GUIDE.md"
  Copy-Item -Force .agents\AGENTS.md "$HOME\.gemini\config\AGENTS.md"
  ```

---

## 🧭 جدول الخطافات التلقائية الـ 21 (Master Hooks Summary)

بمجرد كتابة أي من هذه الكلمات المحفزة في بداية رسالتك، يستجيب النظام فورياً بتفعيل الوكلاء والإجراءات المحددة:

| م | اسم الخطاف | المحفزات (Triggers للنسخ المباشر) | الوكلاء المسؤولون | الهدف الأساسي |
| :-: | :--- | :--- | :--- | :--- |
| **1** | **خطاف البدء والافتتاح** | `"بسم الله"`, `"بسم الله الرحمن الرحيم"` | `[prompt-engineer]`, `[agent-optimizer]` | تهيئة الجلسة، التحقق من القواعد الفنية واللغوية، وترشيد الاستهلاك. |
| **2** | **خطاف هندسة السياق** | `"سياق المشروع"`, `"هندسة السياق"` | `[prompt-engineer]`, `[agent-optimizer]` | إنشاء وتحديث ملف `PROJECT_CONTEXT.md` لضمان التوجيه وتفادي الانحراف. |
| **3** | **خطاف التخصيص والتصدير الذكي لطاقم المشروع** | `"اعتمد سياق المشروع"`, `"تخصيص وكلاء المشروع"`, `"تصدير بيئة المشروع"`, `"تخصيص الوكلاء"` | `[code-architect]`, `[agent-optimizer]`, `[prompt-engineer]` | تحليل سياق المشروع وتصدير حزم الوكلاء محلياً مع التنظيف التلقائي (Pruning) وتوليد سكربت المزامنة. |
| **4** | **خطاف الإشراف العام وتصحيح المسار** | `"تصحيح مسار"`, `"تعديل البرومبت"`, `"خطأ بالخطاف"` | `[prompt-engineer]`, `[agent-optimizer]` | تشريح سبب الانحراف وإعادة توجيه الوكيل للمسار الصحيح. |
| **5** | **خطاف خط الإنتاج البرمجي** | `"ابدأ ميزة"`, `"تطوير ميزة"`, `"اصنع ميزة"` | `[code-architect]`, `[clean-code-guard]`, `[test-guard]` | دورة التطوير الكاملة (معمارية ← تدقيق ← برمجة ← حراسة الكود ← اختبار). |
| **6** | **خطاف التكامل الفني** | `"ربط API"`, `"تكامل خارجي"` | `[devops-deployer]`, `[backend-architect]` | ربط الخدمات الخارجية بعد تحصين انقطاع الشبكة وتأمين المفاتيح. |
| **7** | **خطاف تدقيق الصيانة** | `"تدقيق صيانة"`, `"مراجعة DRY"`, `"كود نظيف"` | `[code-reviewer-quality]`, `[clean-code-guard]` | مراجعة الكود ومنع التكرار وتطبيق معايير الكود النظيف. |
| **8** | **خطاف الفحص الأمني** | `"فحص أمني"`, `"مراجعة أمان"` | `[security-auditor]`, `[code-reviewer-quality]` | مسح الثغرات وفحص تشفير البيانات وصلاحيات الوصول. |
| **9** | **خطاف تحديث المكتبات والنماذج** | `"تحديث الاعتماديات"`, `"تحديث المكتبات"`, `"تحديث النماذج"`, `"معايرة النماذج"` | `[github-talent-scout]`, `[code-architect]`, `[agent-optimizer]` | فحص Gradle واستكشاف ومعايرة النماذج الذكية عبر الإنترنت. |
| **10** | **خطاف الفحص والاختبار التلقائي** | `"قم بالاختبار"`, `"جاهز للتجربة"` | `[android-testing]`, `[test-guard]` | تشغيل اختبارات السلوك الفعلي بدون Mock مفرط وضمان التغطية. |
| **11** | **خطاف معالجة الأخطاء والأعطال** | `"حدث خطأ"`, `"التطبيق توقف"`, `"Crash"` | `[debugger]` | تحليل الـ Stack Trace وعزل المشكلة وتطبيق الحل الأدنى الآمن. |
| **12** | **خطاف إدارة النسخ والمستودعات** | `"نظم جيت"`, `"ارفع لجيتهاب"`, `"git commit"` | `[git-github-manager]` | تنظيم الفروع، صياغة رسائل الالتزام الاحترافية، والرفع لـ GitHub. |
| **13** | **خطاف تحديث الإنجازات** | `"حدث الإنجازات"`, `"تحديث README"` | `[git-github-manager]`, `[docs-guard]` | مطابقة التوثيق برمجياً وصياغة الإنجازات في README ورفعها. |
| **14** | **خطاف الذاكرة المستدامة** | `"حفظ ذاكرة"`, `"تحديث السياق"`, `"سجّل هذا"` | `[persistent-memory-engine]` | التقاط وتخزين الدروس والقرارات المعمارية في `MEMORY_STORE.md`. |
| **15** | **خطاف النجاح والتحليل البعدي** | `"تم بنجاح"`, `"انتهى المشروع بنجاح"` | `[agent-optimizer]` | إجراء تحليل بعدي وتحديث سجل التعلم وسد الثغرات البرمجية. |
| **16** | **خطاف تنظيم وترتيب الأجهزة الشامل** | `"رتب جهازى"`, `"فرز الملفات"`, `"ترتيب ملفات"` | `[windows-c-drive-optimizer]`, `[windows-file-organizer]` | فرز ملفات الأقراص وتنظيف قرص C وتحديث الفهارس. |
| **17** | **خطاف ترتيب الملفات لمسار محدد** | `"رتب المسار"`, `"نظم المجلد"`, `"ترتيب مسار"` | `[windows-file-organizer]` | تصنيف وترتيب الملفات داخل مجلد محدد بذكاء ودقة. |
| **18** | **خطاف إدارة تليجرام** | `"شغل البوت"`, `"تفعيل تليجرام"`, `"Telegram Bot"` | `[devops-deployer]`, `[persistent-memory-engine]` | تشغيل بوت تليجرام بنمط الاستماع للطلبات عن بعد. |
| **19** | **خطاف التدقيق البرمجي الشامل وحراسة الجودة** | `"تدقيق الجودة"`, `"فحص الكود النظيف"`, `"clean code audit"` | `[code-reviewer-quality]`, `[clean-code-guard]`, `[test-guard]` | فحص شامل للكود النظيف، سلامة الاختبارات، ومطابقة التوثيق. |
| **20** | **خطاف استكشاف وتكامل الموارد العالمية** | `"استكشف المورد"`, `"حلل المستودع"`, `"integrate repo"` | `[resource-scout-integrator]`, `[github-talent-scout]`, `[skill-forge-builder]` | استيراد المهارات من المستودعات الخارجية وتعميمها وتحديث المنظومة تلقائياً. |
| **21** | **خطاف المزامنة السحابية الشاملة ومطابقة المنظومة** | `"مزامنة المنظومة"`, `"sync ecosystem"`, `"sync workspace"` | `[git-github-manager]`, `[persistent-memory-engine]`, `[agent-optimizer]` | مطابقة ومزامنة كافة الإضافات والمهارات وتطبيق الأوامر ورفعها لـ GitHub بتطابق 100%. |

---

## 👥 فهرس الوكلاء المتخصصين (Sub-Agents Directory)

تمتلك المنظومة أكثر من 25 وكيلاً متخصصاً موزعين عبر تخصصات دقيقة:

* 🏛️ **طبقة المعمارية والتخطيط:**
  - `[code-architect]`: تخطيط المعمارية النظيفة (Clean Architecture) ونمط MVVM/MVI.
  - `[monorepo-architect]`: إدارة وحدات وحزم الـ Monorepo.
  - `[prompt-engineer]`: صياغة وهندسة قوالب التوجيه وأنظمة الخطافات.
  - `[agent-optimizer]`: قياس كفاءة الوكلاء والحد من تكلفة التوكنز.
  - `[persistent-memory-engine]`: إدارة الذاكرة المعرفية وتصدير السياق.

* 💻 **طبقة البرمجة والتنفيذ:**
  - `[android-kotlin-pro]`: خبير Kotlin، Coroutines، Flow، وإدارة دورة الحياة.
  - `[jetpack-compose-ui]`: خبير واجهات Jetpack Compose ودعم RTL وإعادة الرسم.
  - `[frontend-design-builder]`: خبير واجهات الويب التفاعلية (HTML/CSS/JS/PWA).
  - `[offline-sync-db]`: خبير قواعد البيانات المحلية (Room DB) والمزامنة دون اتصال.
  - `[backend-architect]`: خبير الـ APIs وقواعد البيانات السحابية.

* 🛡️ **طبقة الحراسة وضمان الجودة:**
  - `[clean-code-guard]`: حارس الكود النظيف ومكافحة الأخطاء الـ 14 الشائعة لـ AI.
  - `[test-guard]`: حارس الاختبارات الهادفة ومنع Mocks الوهمية.
  - `[docs-guard]`: حارس دقة التوثيق ومطابقة الرموز البرمجية.
  - `[security-auditor]`: مراجع الثغرات الأمنية وتشفير البيانات.
  - `[debugger]`: محلل السجلات والـ Crash ومعالج المشكلات المنهجي.

* 🌐 **طبقة الاستكشاف والتكامل:**
  - `[resource-scout-integrator]`: فحص وتكامل المستودعات والموارد الخارجية وتعميمها.
  - `[github-talent-scout]`: البحث عن المكتبات المفتوحة لسد الفجوات البرمجية.
  - `[skill-forge-builder]`: بناء وهيكلة المهارات البرمجية آلياً.
  - `[git-github-manager]`: إدارة الإصدارات والـ Commits والتوثيق الآلي.

---

## 📂 هيكلية مجلدات المنظومة (Ecosystem Structure)

```text
Claude-Antigravity-Workspace/
├── .agents/
│   ├── Sub_Agent/               # إعدادات وتعاريف كافة الوكلاء الـ 25+
│   ├── skills/                  # مجلد المهارات البرمجية الـ 80+
│   ├── AGENTS.md                # الدستور المعماري وقواعد التطوير الصارمة
│   ├── HOOKS_GUIDE.md           # الدليل المفصل للخطافات التلقائية الـ 21
│   ├── HOOKS_GUIDE.xlsx         # جدول الإكسيل التفاعلي المولد تلقائياً (22 شيت)
│   ├── ACTIVE_CONTEXT_INJECTION.md # قيود بروتوكول الخطوة صفر الإلزامية
│   └── convert_hooks_to_sheets.py # سكربت أتمتة تحويل الخطافات لملف إكسيل
├── 03_Dynamic_Prompt_Library/   # تطبيق مكتبة الأوامر التفاعلية (HTML/CSS/JS)
├── PROJECT_CONTEXT.md           # عقل وسياق المشروع المعتمد
├── USER_GUIDE.md                # دليل الاستخدام والممارسة التفصيلي
├── README.md                    # هذا الملف التعريفي الشامل
├── project_agent_tailor.py      # محرك التخصيص والتصدير الذكي للمشاريع والتنظيف
├── sync_local_agents_template.py # قالب سكربت المزامنة المحلي للمشاريع
└── sync_global_ecosystem.py     # محرك المزامنة التلقائية للمنظومة مع GitHub
```

---

## 🤝 المشاركة والمساهمة (Contributing)

نرحب بكافة المساهمات لتطوير وتوسيع مكتبة المهارات والوكلاء:
1. قم بعمل **Fork** للمستودع.
2. أضف مهاراتك أو وكيلك الجديد تحت مجلد `.agents/skills/` أو `.agents/Sub_Agent/`.
3. شغل سكربت `python .agents/convert_hooks_to_sheets.py` لتحديث الجداول.
4. أرسل **Pull Request** لمراجعة التعديلات واعتمادها.

---

<div align="center">
  <b>تم البناء والتطوير بواسطة إبراهيم الكاتب وطاقم الوكلاء المساعدين</b> 🚀
</div>

</div>
