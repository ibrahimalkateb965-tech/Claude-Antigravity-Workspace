<div dir="rtl">

# دليل منظومة أسطول الـ CLIs العالمية (Global Fleet Orchestration Suite)

> [!IMPORTANT]
> هذه المنظومة تتيح لك إدارة وضبط أسطول الوكلاء المساعدين عبر سطر الأوامر (**Claude Code CLI**, **OpenCode CLI**, **Antigravity CLI / IDE**) في أي مشروع برمجي على هذا الجهاز، مع ترشيد استهلاك التوكنز وتحديد النماذج والجهد وبوابات المراجعة الإلزامية عبر ملف التكوين المركزي `fleet_config.json`.

---

## 🎛️ 1. ملف الضبط المركزي (`fleet_config.json` / `fleet_config_seed.json`)

يمكن نسخ قالب `fleet_config_seed.json` إلى جذر أي مشروع جديد وتعديل:
* **النماذج النشطة (active_model):** (`Claude 5 Opus`, `DeepSeek V4`, `Gemini 3.7 Flash`... إلخ).
* **مستوى الجهد (effort_level):** (`Max Reasoning`, `High Speed`, `High Reasoning`).
* **خط الإنتاج الإلزامي (fleet_pipeline_workflow):** لفرض مراجعة الكود بنهاية كل مرحلة.

---

## 📂 2. فهرس القوالب الجاهزة للاستخدام (Prompt Templates Index)

| رقم القالب | اسم القالب وملفه | متى يُستخدم؟ | الهدف الأساسي |
| :--- | :--- | :--- | :--- |
| **01** | `01_FLEET_KICKOFF.md` | عند بدء أو استئناف الجلسة مع Claude Code | يوجه Claude لدور الموجه المعماري ويطلب منه فوراً تقرير حالة الأسطول والجهد. |
| **02** | `02_TASK_DELEGATION.md` | لتفويض مهمة إلى OpenCode أو Antigravity | إنشاء أمر إنجليزي محكم مع تحديد شجرة الملفات والمهارات المفوّضة. |
| **03** | `03_CODE_REVIEW_AUDIT.md` | بعد إنهاء كل مرحلة برمجية (Mandatory Gate) | إرسال الـ Diffs لـ Claude (Opus Max) للمراجعة المعمارية والتدقيق الصارم. |
| **04** | `04_STATUS_AND_HEALTH_CHECK.md` | في أي وقت خلال العمل | طلب تقرير فوري بنسبة إنجاز المراحل وصحة الأسطول ومستوى الجهد. |
| **05** | `05_QUOTA_FAILOVER_HANDOFF.md` | عند امتلاء الحصة (Rate Limit) أو التبديل | تسليم واستلام القيادة المعمارية بسلاسة دون فقدان سطر كود أو قرار. |

</div>
