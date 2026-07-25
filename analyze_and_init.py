import os

base_dir = r"F:\AI PROJECTS\Quran_Records\with antigravity"
skip_projects = ["New Crew"]

def get_project_type(project_path):
    try:
        files = os.listdir(project_path)
    except Exception:
        return "غير معروف", "مشروع عام"
        
    files_lower = [f.lower() for f in files]
    
    # Android / Kotlin
    if 'build.gradle' in files_lower or 'build.gradle.kts' in files_lower or 'settings.gradle' in files_lower:
        return "Android/Kotlin", "تطبيق أندرويد"
    # Web / Node
    if 'package.json' in files_lower:
        return "Web/Node.js", "تطبيق ويب / خادم"
    # Python
    if 'requirements.txt' in files_lower or any(f.endswith('.py') for f in files_lower):
        return "Python", "نظام ذكاء اصطناعي / سكربتات"
    # Odoo
    if 'odoo' in files_lower or '__manifest__.py' in files_lower:
        return "Odoo ERP", "وحدة أودو (Odoo Module)"
    
    return "متعدد التقنيات", "مشروع متكامل"

memory_template = """<div dir="rtl">

# مخزن الذاكرة المركزي للمشروع (MEMORY_STORE.md)

> [!IMPORTANT]
> هذا الملف هو السجل المركزي للذاكرة المستدامة الخاص بهذا المشروع تحديداً لتجنب التداخل مع المشاريع الأخرى. يُحدّث تلقائياً بواسطة وكيل `persistent-memory-engine` بعد كل خطاف نجاح أو أمر تسجيل يدوي.

---

## سجل الدروس المستفادة (Lessons Learned)

```yaml
```

---

## سجل القرارات المعمارية (Architecture Decisions)

```yaml
- id: ADR-INIT-001
  type: decision
  timestamp: "2026-07-21T12:30:00+03:00"
  agents: [code-architect]
  context: "التهيئة الأولية للمشروع"
  content: "تم تحليل وتهيئة هذا المشروع برمجياً ليُدار كـ ({stack}) وتم فصل السياق والذاكرة الخاصة به ليعمل ككيان مستقل ضمن بنية Autovem المركزية."
  tags: [init, architecture, auto-detected]
  status: active
```

---

## سجل تفضيلات المستخدم (User Preferences)

```yaml
```

---

## سجل الأخطاء المحلولة (Resolved Bugs)

> سيتم تعبئته تلقائياً عند حل أخطاء مستقبلية.

</div>
"""

context_template = """<div dir="rtl">

# سياق المشروع (PROJECT_CONTEXT.md)

> [!NOTE]
> يحتوي هذا الملف على الملخص المعماري والهدف الأساسي من هذا المشروع.

## نبذة عن المشروع
- **اسم المشروع:** {project_name}
- **الهدف الرئيسي:** {purpose}
- **الحالة الحالية:** قيد التطوير والصيانة
- **التقنيات الأساسية (التي تم اكتشافها):** {stack}

## الهيكلية العامة
- المشروع يعمل ببيئة معزولة بسياق خاص به ضمن بيئة العمل المتعددة (Monorepo).
- يتم إدارة المهام المعقدة عبر وكلاء Autovem المتخصصين المركزية.

</div>
"""

def main():
    projects = sorted([d for d in os.listdir(base_dir) if os.path.isdir(os.path.join(base_dir, d))])
    
    for project_name in projects:
        if project_name in skip_projects:
            continue
            
        project_path = os.path.join(base_dir, project_name)
        agents_dir = os.path.join(project_path, ".agents")
        
        # Determine stack
        stack, purpose = get_project_type(project_path)
        
        # Ensure .agents exists
        os.makedirs(agents_dir, exist_ok=True)
        
        mem_path = os.path.join(agents_dir, "MEMORY_STORE.md")
        ctx_path = os.path.join(agents_dir, "PROJECT_CONTEXT.md")
        
        # Write Context
        with open(ctx_path, 'w', encoding='utf-8') as f:
            f.write(context_template.format(project_name=project_name, purpose=purpose, stack=stack))
            
        # Write Memory
        with open(mem_path, 'w', encoding='utf-8') as f:
            f.write(memory_template.format(stack=stack))
            
        print(f"Initialized: {project_name} | Type: {stack}")

if __name__ == "__main__":
    main()
