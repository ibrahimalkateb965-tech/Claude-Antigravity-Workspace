# -*- coding: utf-8 -*-
"""
سكريبت توليد وثائق منهجيات إنجاز الأعمال بصيغة Word
تأليف: وكيل methodology-writer
"""

import os
from docx import Document
from docx.shared import Pt, Inches, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.oxml import OxmlElement
from docx.oxml.ns import qn

def set_cell_background(cell, hex_color):
    """تعيين لون خلفية خلية في جدول"""
    shading_elm = OxmlElement('w:shd')
    shading_elm.set(qn('w:val'), 'clear')
    shading_elm.set(qn('w:color'), 'auto')
    shading_elm.set(qn('w:fill'), hex_color)
    cell._tc.get_or_add_tcPr().append(shading_elm)

def set_cell_margins(cell, top=100, bottom=100, left=150, right=150):
    """ضبط الهوامش الداخلية لخلية الجدول (بالنقاط الإضافية)"""
    tcPr = cell._tc.get_or_add_tcPr()
    tcMar = OxmlElement('w:tcMar')
    for m, val in [('top', top), ('bottom', bottom), ('left', left), ('right', right)]:
        node = OxmlElement(f'w:{m}')
        node.set(qn('w:w'), str(val))
        node.set(qn('w:type'), 'dxa')
        tcMar.append(node)
    tcPr.append(tcMar)

def set_rtl(paragraph):
    """ضبط اتجاه الفقرة من اليمين إلى اليسار ولغة النص للعربية"""
    pPr = paragraph._p.get_or_add_pPr()
    bidi = OxmlElement('w:bidi')
    bidi.set(qn('w:val'), '1')
    pPr.append(bidi)

def set_run_font(run, name="Traditional Arabic", size=14, bold=False, italic=False, color_rgb=None):
    """ضبط خط وتنسيق جزء من النص"""
    run.font.name = name
    run.font.size = Pt(size)
    run.bold = bold
    run.italic = italic
    if color_rgb:
        run.font.color.rgb = color_rgb
        
    # ضبط ترميز اللغة العربية في XML
    rPr = run._r.get_or_add_rPr()
    rFonts = OxmlElement('w:rFonts')
    rFonts.set(qn('w:ascii'), name)
    rFonts.set(qn('w:hAnsi'), name)
    rFonts.set(qn('w:eastAsia'), name)
    rFonts.set(qn('w:cs'), name)
    rPr.append(rFonts)

def create_styled_paragraph(doc, text="", style_type="normal", align=WD_ALIGN_PARAGRAPH.RIGHT):
    """إنشاء فقرة بتنسيق عربي موحد"""
    p = doc.add_paragraph()
    p.alignment = align
    set_rtl(p)
    
    if text:
        run = p.add_run(text)
        if style_type == "title":
            set_run_font(run, name="Traditional Arabic", size=20, bold=True, color_rgb=RGBColor(31, 73, 125))
        elif style_type == "subtitle":
            set_run_font(run, name="Traditional Arabic", size=16, bold=True, color_rgb=RGBColor(31, 73, 125))
        elif style_type == "h1":
            set_run_font(run, name="Traditional Arabic", size=16, bold=True, color_rgb=RGBColor(79, 129, 189))
        elif style_type == "h2":
            set_run_font(run, name="Traditional Arabic", size=14, bold=True, color_rgb=RGBColor(79, 129, 189))
        else:
            set_run_font(run, name="Traditional Arabic", size=14, bold=False)
            
    return p

def generate_methodology_doc(project_metadata, phases, personnel, equipment, safety_items, output_path):
    """
    توليد مستند منهجية إنجاز الأعمال بصيغة DOCX
    """
    doc = Document()
    
    # 1. ضبط هوامش الصفحة
    sections = doc.sections
    for section in sections:
        section.top_margin = Inches(1)
        section.bottom_margin = Inches(1)
        section.left_margin = Inches(1)
        section.right_margin = Inches(1)
        
    # 2. رأس وتذييل الصفحة (Header & Footer)
    # ملاحظة: سنبسط الرأس والتذييل تجنباً للأخطاء المعقدة في docx
    header = doc.sections[0].header
    hp = header.paragraphs[0]
    hp.alignment = WD_ALIGN_PARAGRAPH.LEFT
    hrun = hp.add_run("مؤسسة إعمار الفرعة للمقاولات العامة")
    set_run_font(hrun, name="Sakkal Majalla", size=10, bold=True, color_rgb=RGBColor(128, 128, 128))
    
    footer = doc.sections[0].footer
    fp = footer.paragraphs[0]
    fp.alignment = WD_ALIGN_PARAGRAPH.CENTER
    frun = fp.add_run("منهجية إنجاز الأعمال فنية ومعتمدة  |  صفحة 1")
    set_run_font(frun, name="Sakkal Majalla", size=10, italic=True, color_rgb=RGBColor(128, 128, 128))

    # 3. العنوان الرئيسي
    p_title = create_styled_paragraph(doc, f"منهجية تنفيذ وإنجاز الأعمال فنية ومعتمدة", "title", WD_ALIGN_PARAGRAPH.CENTER)
    p_proj = create_styled_paragraph(doc, f"مشروع: {project_metadata['name']}", "subtitle", WD_ALIGN_PARAGRAPH.CENTER)
    
    # سطر فارغ
    doc.add_paragraph()

    # 4. القسم 1: مقدمة ومعلومات عامة
    create_styled_paragraph(doc, "1. مقدمة ومعلومات عامة عن المشروع", "h1")
    p_intro = create_styled_paragraph(doc)
    r_intro = p_intro.add_run(
        f"تقدم مؤسسة إعمار الفرعة للمقاولات العامة هذه المنهجية الفنية لتنفيذ أعمال مشروع "
        f"({project_metadata['name']}) لصالح ({project_metadata['client']})، "
        f"خلال مدة زمنية وقدرها ({project_metadata['duration']}) يوماً تقويمياً. "
        f"وتلتزم المؤسسة بإنهاء جميع الأعمال المسندة إليها طبقاً للمواصفات الفنية المعتمده، "
        f"واشتراطات الكود السعودي للبناء (SBC)، وتوجيهات المهندسين المشرفين."
    )
    set_run_font(r_intro, size=14)

    # 5. القسم 2: خطة وجدول التنفيذ المرحلي
    create_styled_paragraph(doc, "2. خطة ومراحل تنفيذ الأعمال", "h1")
    p_phase_intro = create_styled_paragraph(doc)
    r_phase_intro = p_phase_intro.add_run("تم تقسيم خطة سير العمل بالموقع إلى عدة مراحل متزامنة ومتتابعة لضمان الجودة والالتزام بالجدول الزمني كالتالي:")
    set_run_font(r_phase_intro, size=14)
    
    # سرد المراحل
    for idx, phase in enumerate(phases, 1):
        p_phase = create_styled_paragraph(doc)
        # تنسيق رقم البند بولد
        r_num = p_phase.add_run(f"المرحلة {idx}: {phase['title']} ({phase['duration']} أيام) \n")
        set_run_font(r_num, size=14, bold=True)
        r_desc = p_phase.add_run(f"• وصف الأعمال: {phase['desc']}")
        set_run_font(r_desc, size=13)

    # 6. القسم 3: الكادر الفني والإداري (العمالة)
    create_styled_paragraph(doc, "3. الهيكل الإداري والفني المقترح (العمالة)", "h1")
    
    # إنشاء جدول العمالة
    table_pers = doc.add_table(rows=len(personnel) + 1, cols=3)
    table_pers.autofit = True
    
    # عناوين الجدول
    headers_pers = ["م", "المسمى الوظيفي", "العدد المقترح"]
    hdr_cells = table_pers.rows[0].cells
    for i, title in enumerate(headers_pers):
        hdr_cells[i].text = title
        set_cell_background(hdr_cells[i], "1F497D")
        set_cell_margins(hdr_cells[i])
        p = hdr_cells[i].paragraphs[0]
        p.alignment = WD_ALIGN_PARAGRAPH.CENTER
        set_rtl(p)
        set_run_font(p.runs[0], name="Traditional Arabic", size=12, bold=True, color_rgb=RGBColor(255, 255, 255))
        
    # إدخال بيانات العمالة
    for r_idx, pers in enumerate(personnel, 1):
        row_cells = table_pers.rows[r_idx].cells
        row_cells[0].text = str(r_idx)
        row_cells[1].text = pers["role"]
        row_cells[2].text = str(pers["count"])
        
        # تنسيق وحشو الخلايا
        for c_idx in range(3):
            cell = row_cells[c_idx]
            set_cell_margins(cell)
            p = cell.paragraphs[0]
            p.alignment = WD_ALIGN_PARAGRAPH.CENTER if c_idx != 1 else WD_ALIGN_PARAGRAPH.RIGHT
            set_rtl(p)
            if p.runs:
                set_run_font(p.runs[0], name="Traditional Arabic", size=12)
                
    # مسافة بعد الجدول
    doc.add_paragraph()

    # 7. القسم 4: المعدات والآليات
    create_styled_paragraph(doc, "4. المعدات والآليات المقترح تأمينها للموقع", "h1")
    
    # إنشاء جدول المعدات
    table_equip = doc.add_table(rows=len(equipment) + 1, cols=3)
    table_equip.autofit = True
    
    headers_equip = ["م", "اسم المعدة / الآلية", "العدد المطلوب"]
    hdr_cells = table_equip.rows[0].cells
    for i, title in enumerate(headers_equip):
        hdr_cells[i].text = title
        set_cell_background(hdr_cells[i], "1F497D")
        set_cell_margins(hdr_cells[i])
        p = hdr_cells[i].paragraphs[0]
        p.alignment = WD_ALIGN_PARAGRAPH.CENTER
        set_rtl(p)
        set_run_font(p.runs[0], name="Traditional Arabic", size=12, bold=True, color_rgb=RGBColor(255, 255, 255))
        
    for r_idx, equip in enumerate(equipment, 1):
        row_cells = table_equip.rows[r_idx].cells
        row_cells[0].text = str(r_idx)
        row_cells[1].text = equip["name"]
        row_cells[2].text = str(equip["count"])
        
        for c_idx in range(3):
            cell = row_cells[c_idx]
            set_cell_margins(cell)
            p = cell.paragraphs[0]
            p.alignment = WD_ALIGN_PARAGRAPH.CENTER if c_idx != 1 else WD_ALIGN_PARAGRAPH.RIGHT
            set_rtl(p)
            if p.runs:
                set_run_font(p.runs[0], name="Traditional Arabic", size=12)
                
    # مسافة بعد الجدول
    doc.add_paragraph()

    # 8. القسم 5: إجراءات السلامة والصحة المهنية
    create_styled_paragraph(doc, "5. خطة إجراءات السلامة والصحة المهنية بالموقع", "h1")
    p_safety_intro = create_styled_paragraph(doc)
    r_safety_intro = p_safety_intro.add_run(
        "تضع مؤسسة إعمار الفرعة سلامة العاملين وزوار الموقع في مقدمة أولوياتها، "
        "ولذلك يتم تطبيق إجراءات السلامة الصارمة والوقاية من المخاطر كالتالي:"
    )
    set_run_font(r_safety_intro, size=14)
    
    for s_idx, item in enumerate(safety_items, 1):
        p_safety = create_styled_paragraph(doc)
        r_bullet = p_safety.add_run(f" {s_idx}. {item}")
        set_run_font(r_bullet, size=13)
        
    # حفظ المستند
    os.makedirs(os.path.dirname(output_path), exist_ok=True)
    doc.save(output_path)
    return output_path

if __name__ == "__main__":
    test_meta = {
        "name": "صيانة حديقة الملك فهد ببلدية الحلوة",
        "client": "بلدية الحلوة - أمانة منطقة الرياض",
        "duration": 60
    }
    test_phases = [
        {"title": "التحضير والتجهيز واعتماد المواد", "duration": 10, "desc": "تقديم عينات المواد والكتالوجات والحصول على الموافقات الرسمية قبل التوريد."},
        {"title": "التوريدات والأعمال الإنشائية التمهيدية", "duration": 15, "desc": "توريد شبكات الري، الثيل الصناعي، تجهيز وتسوية المسطحات بالموقع."},
        {"title": "تنفيذ الأعمال الفنية والتركيبات", "duration": 25, "desc": "تركيب شبكات ري متكاملة وفرد الثيل الصناعي وتثبيته ميكانيكياً ورش رمل السليكا والمطاط."},
        {"title": "الاختبارات والتنظيف والتسليم", "duration": 10, "desc": "اختبار ضغط شبكات ري الحديقة، وتخطيط الساحات وتنظيف الموقع بالكامل والتسليم الابتدائي."}
    ]
    test_pers = [
        {"role": "مهندس مشروع مدني/زراعي مقيم", "count": 1},
        {"role": "مراقب سلامة وصحة مهنية", "count": 1},
        {"role": "فني تركيب شبكات ري", "count": 2},
        {"role": "عمالة متخصصة تركيب ثيل", "count": 4},
        {"role": "سائقين ومشغلي معدات", "count": 2}
    ]
    test_equip = [
        {"name": "سيارة نقل شاحنة متوسطة", "count": 1},
        {"name": "ببكات لزوم التسوية والفرد", "count": 1},
        {"name": "معدة فرد حبيبات الرمل والمطاط ميكانيكياً", "count": 1},
        {"name": "أدوات قياس واختبار ضغط شبكات ري", "count": 1}
    ]
    test_safety = [
        "إلزام جميع العاملين بارتداء معدات الوقاية الشخصية (خوذة، حذاء سلامة، سترة عاكسة).",
        "تأمين وتطويق الموقع بلوحات تحذيرية وحواجز أمان لمنع دخول غير المختصين.",
        "توفير حقائب إسعافات أولية متكاملة في الموقع وتدريب الكادر الفني على الاستخدام.",
        "الفحص الدوري اليومي للمعدات والآليات قبل بدء العمل للتأكد من خلوها من الأعطال."
    ]
    generate_methodology_doc(test_meta, test_phases, test_pers, test_equip, test_safety, "g:/M.Yonis/.agents/skills/references/scripts/test_methodology.docx")
    print("تم توليد منهجية التجربة بنجاح في: test_methodology.docx")
