---
name: document-formatting
description: |
  تنسيق وإخراج وثائق المقاولات والخطابات والمواصفات الفنية
  بأعلى مستويات الجودة والاحترافية مع ضبط معمارية RTL وحل مفارقة BiDi في DOCX و PDF.
---

# مهارة تنسيق المستندات والإخراج النهائي (Document Formatting Skill)

## الأهداف
تطبيق القواعد الهندسية والمعمارية لضمان خروج كافة التقارير، المواصفات الفنية، الخطابات، والمنهجيات بمظهر احترافي وموحد، مع الالتزام التام باتجاه اليمين لليسار (RTL) الصارم وتفادي انكسار المحاذاة.

---

## القواعد الفنية الملزمة

### 1. المنظومة الخماسية لضبط اتجاه RTL وتفادي مفارقة BiDi (OpenXML BiDi Paradox):
عند التعامل مع ملفات Microsoft Word عبر البرمجة (`python-docx`):
- **مستوى المقطع (Section):** حقن `<w:bidi/>` داخل خصائص المقطع `<w:sectPr>` لضبط اتجاه الصفحة كاملاً.
- **مستوى الفقرة (Paragraph):** عند تفعيل `<w:bidi/>` داخل `<w:pPr>`، يُحظر تماماً استخدام المحاذاة لليمين `right` لأن محرك Word يعاملها كنهاية لسطر القراءة ويدفع النص لليسار البصري. يجب دائماً استخدام المحاذاة الموزونة `<w:jc w:val="both"/>` أو لليسار البرمجية `<w:jc w:val="left"/>`.
- **مستوى المقاطع النصية (Runs):** حقن `<w:rPr><w:rtl/></w:rPr>` في كافة كائنات Runs لمنع انقلاب المصطلحات الإنجليزية أو الأقواس أو الأرقام.
- **مستوى الجداول (Tables):** حقن `<w:tblPr><w:bidiVisual/></w:tblPr>` لضمان انطلاق الأعمدة من اليمين البصري (العمود الأول م على أقصى اليمين).
- **علامة اتجاه اليمين الخفية (RLM `\u200F`):** إدراج الرمز في بدايات الفقرات والنقاط التي تستهل بمصطلحات أو أكواد لاتينية (مثل `ASTM` أو `1.1`).

```python
from docx.oxml import parse_xml
from docx.oxml.ns import nsdecls

def apply_full_bidi_to_doc(doc):
    # 1. Section RTL
    for section in doc.sections:
        sectPr = section._sectPr
        if sectPr.find(qn('w:bidi')) is None:
            sectPr.append(parse_xml(f'<w:bidi {nsdecls("w")}/>'))

    # 2. Paragraph & Run RTL
    for p in doc.paragraphs:
        pPr = p._element.get_or_add_pPr()
        if pPr.find(qn('w:bidi')) is None:
            pPr.append(parse_xml(f'<w:bidi {nsdecls("w")}/>'))
        # Ensure left/both alignment
        jc = pPr.find(qn('w:jc'))
        if jc is not None:
            pPr.remove(jc)
        pPr.append(parse_xml(f'<w:jc {nsdecls("w")} w:val="both"/>'))
        
        for r in p.runs:
            rPr = r._element.get_or_add_rPr()
            if rPr.find(qn('w:rtl')) is None:
                rPr.append(parse_xml(f'<w:rtl {nsdecls("w")}/>'))

    # 3. Table RTL
    for table in doc.tables:
        tblPr = table._tbl.tblPr
        if tblPr.find(qn('w:bidiVisual')) is None:
            tblPr.append(parse_xml(f'<w:bidiVisual {nsdecls("w")}/>'))
```

---

### 2. الخطوط وهوية التصميم:
- **الخطوط المعتمدة:** `Traditional Arabic` أو `Sakkal Majalla` أو `Amiri` للنصوص العربية الرسمية.
- **الأحجام القياسية:**
  - العناوين الرئيسية (H1): 18-20pt Bold.
  - العناوين الفرعية (H2 / H3): 14-16pt Bold.
  - النصوص العادية والفقرات: 12-14pt Regular.
  - الجداول والخلايا: 10-11pt.
- **الهوامش:** هوامش متوازنة ومضغوطة (Top: 1.0" - 1.22", Bottom: 0.85", Left/Right: 0.8" - 1.0") لضمان استيعاب المحتوى ومنع التداخل مع الترويسات الرسمية.

---

### 3. منظومة الأرقام الإنجليزية المعتمدة (0-9) وبوابة الفحص الصارمة:
- **الأرقام الإنجليزية (0-9):** تُعتمد حصرياً وبشكل مطلق في كافة المستندات الرسمية، الجداول، المواصفات الفنية، والخطابات.
- **حظر الأرقام الهندية المشرقية (٠-٩):** يُحظر تماماً استخدام الأرقام الهندية المشرقية.
- **بوابة فحص الصفر أرقام هندية:**
  ```python
  import fitz, re
  doc = fitz.open("output.pdf")
  text = "".join(page.get_text() for page in doc)
  indic_count = len(re.findall(r'[\u0660-\u0669]', text))
  assert indic_count == 0, f"خطأ: تم اكتشاف {indic_count} رقماً هندياً مشرقياً!"
  ```

---

### 4. التصدير النظيف إلى PDF عبر Word COM:
- **تحويل المتجهات الأصلي (Vector Quality):** استخدام `win32com.client` مع محرك Microsoft Word للحصول على PDF بنظام المتجهات عالي الدقة.
- **معالجة قفل الملفات (File Locks):** قبل التصدير، يجب التأكد من إغلاق أي برامج تستحوذ على قفل ملف الـ PDF (مثل `FoxitPDFEditor.exe`) لتفادي توقف السكربت بخطأ `Command failed (-2147352567)`.
  ```python
  import subprocess, win32com.client
  # إغلاق العمليات التي قد تقفل ملف الـ PDF
  subprocess.run(["taskkill", "/F", "/IM", "FoxitPDFEditor.exe"], capture_output=True)
  
  word = win32com.client.Dispatch("Word.Application")
  word.Visible = False
  doc = word.Documents.Open(docx_path)
  doc.SaveAs(pdf_path, FileFormat=17)
  doc.Close(False)
  word.Quit()
  ```

---

### 5. بوابة فحص إحداثيات المحاذاة البصرية لليمين (PyMuPDF Visual RTL Gate):
- بعد توليد الـ PDF، يتم فحص إحداثيات الكتل النصية للتأكد من ارتكازها على الحافة اليمنى للورقة:
  ```python
  import fitz
  doc = fitz.open(pdf_path)
  page = doc[0]
  # الحافة اليمنى لورق A4 بعرض 595 pt يجب أن تصل إليها الكتل النصية (X1 >= 520 pt)
  blocks = page.get_text("blocks")
  right_aligned_blocks = [b for b in blocks if b[2] >= 520]
  assert len(right_aligned_blocks) > 0, "تحذير: النصوص لا ترتكز على اليمين البصري!"
  ```
