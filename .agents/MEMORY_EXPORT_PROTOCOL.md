<div dir="rtl">

# بروتوكول تصدير الذاكرة لنظام Autovem (MEMORY_EXPORT_PROTOCOL.md)

> [!IMPORTANT]
> يحدد هذا البروتوكول كيفية تصدير السياق المناسب لكل وكيل عند تفعيله، لضمان أن كل وكيل يعمل بأقصى كفاءة مع أقل استهلاك للتوكنز.

---

## 1. مبدأ التصدير الانتقائي (Selective Context Injection)

**لا يتلقى كل وكيل كل الذاكرة.** بل يتم تصفية الذاكرة وتصدير الحزمة المناسبة فقط بناءً على:
- **تخصص الوكيل** (Agent Specialization).
- **الوسوم المطابقة** (Tag Matching).
- **حالة الذاكرة** (Status = active فقط).

---

## 2. جدول التصدير حسب التخصص

### وكلاء المعمارية والتخطيط (thinking)
**يتلقون:** `decision` + `pattern` + `preference` + `tool-discovery`
**وسوم:** `architecture`, `structure`, `modules`, `naming`, `patterns`
**الوكلاء:** `code-architect`, `monorepo-architect`, `prompt-engineer`, `agent-optimizer`, `persistent-memory-engine`

### وكلاء البرمجة والتنفيذ (pro)
**يتلقون:** `lesson` + `pattern` + `bug-fix`
**وسوم:** `kotlin`, `compose`, `android`, `coroutines`, `room`, `hilt`, `flow`
**الوكلاء:** `android-kotlin-pro`, `jetpack-compose-ui`, `offline-sync-db`, `backend-architect`

### وكلاء المراجعة والاختبار (pro)
**يتلقون:** `bug-fix` + `pattern` + `lesson`
**وسوم:** `testing`, `junit`, `mockk`, `ci`, `security`, `owasp`, `quality`
**الوكلاء:** `code-reviewer-quality`, `code-reviewer-feature-dev`, `android-testing`, `test-automator`, `security-auditor`

### وكلاء التصميم (pro/flash)
**يتلقون:** `preference` + `decision` + `pattern`
**وسوم:** `design`, `colors`, `typography`, `rtl`, `animation`, `brand`
**الوكلاء:** `ui-ux-design-lead`, `taste-design-critic`, `motion-transitions-pro`, `frontend-design-builder`, `web-artifacts-prototyper`, `brand-kit-keeper`

### وكلاء المحتوى والتسويق (flash)
**يتلقون:** `preference` + `pattern`
**وسوم:** `content`, `tone`, `style`, `arabic`, `seo`, `marketing`
**الوكلاء:** `copywriting-lead`, `ai-seo-optimizer`, `cro-conversion-lead`, `ad-creative-maker`, `customer-research-voice`, `post-content-writer`, `script-hook-generator`, `profile-optimizer`

### وكلاء المالية والأعمال (flash)
**يتلقون:** `pattern` + `preference` + `decision`
**وسوم:** `finance`, `accounting`, `reporting`, `business`, `payroll`, `tax`
**الوكلاء:** `financial-statements-builder`, `journal-entry-keeper`, `reconciliation-auditor`, `variance-analyst`, `audit-support-prep`, `close-management-lead`, `cash-flow-watcher`, `invoice-chaser`, `payroll-planner`, `margin-analyst`, `tax-prepper`, `campaign-runner`

### وكلاء القانون والامتثال (flash)
**يتلقون:** `pattern` + `decision` + `lesson`
**وسوم:** `legal`, `compliance`, `license`, `nda`, `contract`, `risk`
**الوكلاء:** `contract-reviewer`, `nda-triage`, `compliance-officer`, `legal-risk-assessor`, `vendor-vetter`, `signature-wrangler`

---

## 3. آلية الحقن (Injection Mechanism)

عند تفعيل أي وكيل، يقوم الوكيل الرئيسي بالخطوات التالية:

```
1. قراءة MEMORY_STORE.md
2. تصفية الذكريات حسب جدول التصدير (النوع + الوسوم + الحالة)
3. ترتيبها بالأحدث أولاً (max 10 ذكريات)
4. حقنها في سياق الوكيل الفرعي كجزء من التفعيل
```

---

## 4. قواعد الحد (Rate Limiting)

- **الحد الأقصى لكل تصدير:** 10 ذكريات أحدث.
- **حجم كل ذاكرة:** لا يتجاوز 200 كلمة.
- **ذكريات `superseded`:** لا تُصدّر أبداً.
- **ذكريات `archived`:** لا تُصدّر إلا عند الطلب الصريح.

---

## 5. تحديث تلقائي بعد الخطافات

| الخطاف | الإجراء |
|:-------|:--------|
| خطاف النجاح (`تم بنجاح`) | إضافة دروس مستفادة جديدة |
| خطاف هندسة السياق (`سياق المشروع`) | تحديث القرارات المعمارية |
| خطاف الذاكرة (`حفظ ذاكرة`) | تسجيل أي نوع ذاكرة مباشرة |
| خطاف الأخطاء (`حدث خطأ`) | تسجيل bug-fix بعد الحل |

</div>
