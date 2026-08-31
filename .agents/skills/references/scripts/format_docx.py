# -*- coding: utf-8 -*-
"""
سكريبت تنسيق وإخراج وثائق Word طبقاً لمعايير الجودة (AGENTS.md)
تأليف: وكيل document-formatter
"""

import os
from docx import Document
from docx.shared import Pt, Inches, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.oxml import OxmlElement
from docx.oxml.ns import qn

def set_rtl(paragraph):
    """ضبط الفقرة لتكون من اليمين إلى اليسار ولغة عربية"""
    pPr = paragraph._p.get_or_add_pPr()
    bidi = OxmlElement('w:bidi')
    bidi.set(qn('w:val'), '1')
    pPr.append(bidi)

def format_run(run, font_name="Traditional Arabic", font_size=14, color_rgb=None, bold=None, italic=None):
    """تطبيق تنسيق الخط والترميز العربي على run"""
    run.font.name = font_name
    run.font.size = Pt(font_size)
    if color_rgb:
        run.font.color.rgb = color_rgb
    if bold is not None:
        run.bold = bold
    if italic is not None:
        run.italic = italic
        
    rPr = run._r.get_or_add_rPr()
    rFonts = OxmlElement('w:rFonts')
    rFonts.set(qn('w:ascii'), font_name)
    rFonts.set(qn('w:hAnsi'), font_name)
    rFonts.set(qn('w:eastAsia'), font_name)
    rFonts.set(qn('w:cs'), font_name)
    rPr.append(rFonts)

def format_document(file_path, output_path=None):
    """
    تنسيق كامل المستند وإخراجه بمظهر فني ومطابق للمعايير
    """
    if not os.path.exists(file_path):
        raise FileNotFoundError(f"الملف غير موجود: {file_path}")
        
    doc = Document(file_path)
    primary_color = RGBColor(31, 73, 125)  # Navy Blue
    secondary_color = RGBColor(79, 129, 189)  # Medium Blue
    
    # 1. ضبط الهوامش (2.5 سم من جميع الجهات)
    margin_size = Inches(0.98) # 2.5 cm تقريباً
    for section in doc.sections:
        section.top_margin = margin_size
        section.bottom_margin = margin_size
        section.left_margin = margin_size
        section.right_margin = margin_size
        
        # التأكد من تفعيل الرأس والتذييل
        section.different_first_page_header_footer = False

    # 2. تنسيق الفقرات والنصوص
    for paragraph in doc.paragraphs:
        # تحديد نمط الفقرة استناداً إلى المحتوى أو النمط الأصلي
        text = paragraph.text.strip()
        if not text:
            continue
            
        set_rtl(paragraph)
        
        # كشف العناوين وتنسيقها
        is_heading = False
        font_size = 14
        bold = False
        color = None
        
        # فحص إن كانت الفقرة عنوان رئيسي أو فرعي
        if paragraph.style.name.startswith('Heading 1') or (text.startswith(tuple(f"{i}." for i in range(1, 20))) and len(text) < 100):
            is_heading = True
            font_size = 16
            bold = True
            color = primary_color
            paragraph.alignment = WD_ALIGN_PARAGRAPH.RIGHT
        elif paragraph.style.name.startswith('Heading 2') or (text.startswith(tuple(f"{i}.{j}" for i in range(1, 20) for j in range(1, 20))) and len(text) < 100):
            is_heading = True
            font_size = 14
            bold = True
            color = secondary_color
            paragraph.alignment = WD_ALIGN_PARAGRAPH.RIGHT
        elif paragraph.style.name.startswith('Title') or paragraph.style.name.startswith('Subtitle'):
            is_heading = True
            font_size = 20 if 'Title' in paragraph.style.name else 16
            bold = True
            color = primary_color
            paragraph.alignment = WD_ALIGN_PARAGRAPH.CENTER
        else:
            # الفقرة العادية
            paragraph.alignment = WD_ALIGN_PARAGRAPH.RIGHT
            
        # تطبيق التنسيق على جميع مقاطع النص داخل الفقرة
        for run in paragraph.runs:
            format_run(run, font_name="Traditional Arabic", font_size=font_size, color_rgb=color, bold=bold)

    # 3. تنسيق الجداول المضمنة
    for table in doc.tables:
        # محاذاة الجدول في المنتصف
        table.alignment = WD_ALIGN_PARAGRAPH.CENTER
        
        for r_idx, row in enumerate(table.rows):
            is_header = (r_idx == 0)
            for cell in row.cells:
                # تعيين خلفية العناوين
                if is_header:
                    shading_elm = OxmlElement('w:shd')
                    shading_elm.set(qn('w:val'), 'clear')
                    shading_elm.set(qn('w:color'), 'auto')
                    shading_elm.set(qn('w:fill'), '1F497D')
                    cell._tc.get_or_add_tcPr().append(shading_elm)
                    
                # ضبط الفقرات داخل الخلايا
                for paragraph in cell.paragraphs:
                    set_rtl(paragraph)
                    paragraph.alignment = WD_ALIGN_PARAGRAPH.CENTER if is_header else WD_ALIGN_PARAGRAPH.RIGHT
                    for run in paragraph.runs:
                        font_size = 12
                        bold = True if is_header else False
                        color = RGBColor(255, 255, 255) if is_header else None
                        format_run(run, font_name="Traditional Arabic", font_size=font_size, color_rgb=color, bold=bold)

    # 4. حفظ الملف المنسق
    final_output_path = output_path if output_path else file_path
    doc.save(final_output_path)
    return final_output_path

if __name__ == "__main__":
    # تجربة التنسيق على ملف تجريبي
    import shutil
    src = "g:/M.Yonis/.agents/skills/references/scripts/test_methodology.docx"
    dest = "g:/M.Yonis/.agents/skills/references/scripts/formatted_test_methodology.docx"
    if os.path.exists(src):
        shutil.copy(src, dest)
        format_document(dest)
        print("تمت عملية تنسيق وتدقيق مستند Word بنجاح!")
