---
name: standalone-html-builder
description: >
  نمط بناء وتوزيع تطبيقات HTML المستقلة (Standalone) للعملاء — خاصةً تطبيقات العهدة
  والمالية. يُولَّد ملفان دائماً: نسخة تشغيلية بالبيانات الفعلية + نسخة فارغة للمشاريع الجديدة.
  يُفعَّل بالمحفّزات: "ابنِ الحزمة", "اصنع نسخة فارغة", "standalone html", "build package".
triggers:
  - "ابنِ الحزمة"
  - "اصنع نسخة فارغة"
  - "standalone html"
  - "build package"
  - "حزمة العميل"
---

# مهارة: بناء وتوزيع تطبيقات HTML المستقلة

## الغرض
بناء تطبيق HTML واحد مضغوط (كل CSS + JS + HTML في ملف واحد) وتوزيعه كنسختين.

## بنية المخرجات القياسية
```
output/
├── [اسم_المشروع]_النشطة_البيانات_الحالية.html   ← للعميل الحالي (بياناته الفعلية)
├── [اسم_المشروع]_نسخة_جديدة_فارغة.html          ← قالب عام مصفّر (0.00 لكل الأرصدة)
├── دليل_تشغيل_التطبيق.html                       ← دليل المستخدم النهائي (RTL)
└── نسخة_احتياطية_[تاريخ].json                   ← نسخة احتياطية من البيانات الأولية
```

## سكريبت البناء القياسي (Python)
```python
# build_[project]_package.py
import re, pathlib

SRC_DIR = pathlib.Path("src")
OUT_DIR = pathlib.Path("output_[client]")
OUT_DIR.mkdir(exist_ok=True)

def build(initial_data_js: str, out_name: str):
    """دمج كل الملفات في HTML واحد مضغوط"""
    template = (SRC_DIR / "index.template.html").read_text(encoding="utf-8")
    
    # حقن CSS
    css = (SRC_DIR / "style.css").read_text(encoding="utf-8")
    template = template.replace("/* __STYLE_INJECTION__ */", css)
    
    # حقن JS (بالترتيب: البيانات الأولية → المحرك → التطبيق)
    template = template.replace("// __INITIAL_DATA__", initial_data_js)
    template = template.replace(
        "// __ENGINE__",
        (SRC_DIR / "engine" / "accountingEngine.js").read_text(encoding="utf-8")
    )
    template = template.replace(
        "// __APP__",
        (SRC_DIR / "app.js").read_text(encoding="utf-8")
    )
    
    (OUT_DIR / out_name).write_text(template, encoding="utf-8")
    print(f"✅ تم بناء: {out_name}")

# النسخة الفعّالة (بيانات العميل الحالي)
build(
    (SRC_DIR / "data" / "initialData_active.js").read_text(encoding="utf-8"),
    "[اسم]_النشطة_البيانات_الحالية.html"
)

# النسخة الفارغة (قالب مصفّر)
build(
    (SRC_DIR / "data" / "initialData.js").read_text(encoding="utf-8"),  # ← الأرصدة = 0
    "[اسم]_نسخة_جديدة_فارغة.html"
)
```

## قواعد صارمة لـ initialData.js (القالب الفارغ)
1. **أدوار وظيفية فقط** — لا أسماء شخصية أبداً:
   ```js
   // ✅ صحيح
   { id: 'treasurer', name: 'أمين الصندوق والعهدة' }
   { id: 'site_engineer', name: 'مهندس الموقع' }
   
   // ❌ خطأ
   { id: 'eng_ibrahim', name: 'م. إبراهيم الكاتب' }
   ```
2. **تصفير الأرصدة الافتراضية** — `amount: 0`, `balance: 0.00`
3. **لا مراجع لخلايا إكسيل** — يُمنع ذكر `C92`, `C94`, أو أي مرجع خلية في الواجهة أو التوثيق
4. **لا وسوم شخصية** — إزالة أي tab أو label يحمل اسم موظف محدد

## قاعدة التطهير من مصادر البيانات (Data Source Independence — Rule 51+)
عند بناء أي تطبيق من ملف إكسيل أو مصدر خارجي:
- **المرحلة 1:** بناء النسخة الأولى من البيانات المستوردة (للتحقق والاختبار)
- **المرحلة 2:** تطهير `initialData.js` وتصفيره ليكون قالباً عاماً
- **المرحلة 3:** نسخة العميل الفعلية تُخزَّن في `initialData_[client].js` منفصلة
- **المرحلة 4:** ملف البناء يُولّد النسختين (الفعّالة + الفارغة) آلياً

## مثال مُثبَت: Autovem Custody Pro
- **المستودع:** `Clients/06_ِAccounting/custody_web_app/`
- **سكريبت البناء:** `build_standalone.py` + `build_abdullah_package.py`
- **المخرجات:** `نسخة_التطبيق_عبدالله/`
- **GitHub Pages:** `https://ibrahimalkateb965-tech.github.io/Acounnting_App/`
