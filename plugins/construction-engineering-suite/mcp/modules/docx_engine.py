#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
محرك وثائق الوورد الهندسية والتنسيق العربي (DOCX Engine)
يدعم تحويل الماركداون، خطابات الضمان بنمط الصفحة الواحدة، وطلبات الفحص WIR
"""

import os
import sys
import datetime
from pathlib import Path
from typing import List, Dict, Any, Optional

import docx
from docx import Document
from docx.shared import Inches, Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT, WD_ALIGN_VERTICAL
from docx.oxml import parse_xml, OxmlElement
from docx.oxml.ns import nsdecls, qn

FONT_PRIMARY = "Traditional Arabic"
FONT_SECONDARY = "Calibri"
COLOR_PRIMARY = RGBColor(31, 73, 125)  # Dark Blue #1F497D
COLOR_SECONDARY = RGBColor(89, 89, 89)
COLOR_TEXT = RGBColor(0, 0, 0)


def set_rtl_paragraph(paragraph):
    """ضبط اتجاه الفقرة إلى RTL"""
    pPr = paragraph._element.get_or_add_pPr()
    bidi = parse_xml(f'<w:bidi {nsdecls("w")}/>')
    pPr.append(bidi)


def set_cell_background(cell, fill_hex: str):
    """تلوين خلفية الخلية"""
    tcPr = cell._tc.get_or_add_tcPr()
    shd = parse_xml(f'<w:shd {nsdecls("w")} w:fill="{fill_hex}"/>')
    tcPr.append(shd)


def set_cell_margins(cell, top=100, bottom=100, left=120, right=120):
    """ضبط هوامش الخلية بالـ dxa"""
    tcPr = cell._tc.get_or_add_tcPr()
    tcMar = parse_xml(f'<w:tcMar {nsdecls("w")}><w:top w:w="{top}" w:type="dxa"/><w:bottom w:w="{bottom}" w:type="dxa"/><w:left w:w="{left}" w:type="dxa"/><w:right w:w="{right}" w:type="dxa"/></w:tcMar>')
    tcPr.append(tcMar)


def build_arabic_docx_from_markdown(markdown_content: str, output_path: str, template_path: Optional[str] = None) -> str:
    """
    تحويل محتوى Markdown إلى مستند Word عربي منسق بدقة RTL
    """
    out_file = Path(output_path).resolve()
    out_file.parent.mkdir(parents=True, exist_ok=True)

    if template_path and os.path.exists(template_path):
        doc = Document(template_path)
    else:
        doc = Document()
        # هوامش الصفحة 2.5 سم
        for section in doc.sections:
            section.top_margin = Inches(0.8)
            section.bottom_margin = Inches(0.8)
            section.left_margin = Inches(0.8)
            section.right_margin = Inches(0.8)

    lines = markdown_content.splitlines()
    in_table = False
    table_rows = []

    def flush_table():
        nonlocal in_table, table_rows
        if not table_rows:
            in_table = False
            return
        
        # تحليل بيانات الجدول
        header = table_rows[0]
        data_rows = table_rows[1:]
        
        cols_count = len(header)
        table = doc.add_table(rows=len(table_rows), cols=cols_count)
        table.alignment = WD_TABLE_ALIGNMENT.CENTER
        
        # ترويسة الجدول
        for col_idx, text in enumerate(header):
            cell = table.cell(0, col_idx)
            set_cell_background(cell, "1F497D")
            set_cell_margins(cell, 120, 120, 150, 150)
            p = cell.paragraphs[0]
            p.alignment = WD_ALIGN_PARAGRAPH.CENTER
            set_rtl_paragraph(p)
            run = p.add_run(text.strip())
            run.font.name = FONT_PRIMARY
            run.font.size = Pt(11)
            run.font.bold = True
            run.font.color.rgb = RGBColor(255, 255, 255)

        for row_idx, r_data in enumerate(data_rows, 1):
            fill = "F2F5F8" if row_idx % 2 == 0 else "FFFFFF"
            for col_idx, text in enumerate(r_data):
                if col_idx >= cols_count:
                    break
                cell = table.cell(row_idx, col_idx)
                set_cell_background(cell, fill)
                set_cell_margins(cell, 100, 100, 120, 120)
                p = cell.paragraphs[0]
                p.alignment = WD_ALIGN_PARAGRAPH.RIGHT
                set_rtl_paragraph(p)
                run = p.add_run(text.strip())
                run.font.name = FONT_PRIMARY
                run.font.size = Pt(10.5)

        doc.add_paragraph()  # فاصل
        table_rows = []
        in_table = False

    for line in lines:
        stripped = line.strip()
        if not stripped:
            if in_table:
                flush_table()
            continue

        # جداول Markdown
        if stripped.startswith("|") and stripped.endswith("|"):
            cells = [c.strip() for c in stripped.split("|")[1:-1]]
            # تجاهل سطور الفواصل |:---|:---|
            if cells and all(set(c).issubset({'-', ':', ' '}) for c in cells):
                continue
            table_rows.append(cells)
            in_table = True
            continue
        elif in_table:
            flush_table()

        # عناوين H1
        if stripped.startswith("# "):
            p = doc.add_paragraph()
            p.alignment = WD_ALIGN_PARAGRAPH.CENTER
            set_rtl_paragraph(p)
            run = p.add_run(stripped[2:])
            run.font.name = FONT_PRIMARY
            run.font.size = Pt(18)
            run.font.bold = True
            run.font.color.rgb = COLOR_PRIMARY
            continue

        # عناوين H2
        if stripped.startswith("## "):
            p = doc.add_paragraph()
            p.alignment = WD_ALIGN_PARAGRAPH.RIGHT
            set_rtl_paragraph(p)
            run = p.add_run(stripped[3:])
            run.font.name = FONT_PRIMARY
            run.font.size = Pt(14)
            run.font.bold = True
            run.font.color.rgb = COLOR_PRIMARY
            continue

        # عناوين H3
        if stripped.startswith("### "):
            p = doc.add_paragraph()
            p.alignment = WD_ALIGN_PARAGRAPH.RIGHT
            set_rtl_paragraph(p)
            run = p.add_run(stripped[4:])
            run.font.name = FONT_PRIMARY
            run.font.size = Pt(12.5)
            run.font.bold = True
            run.font.color.rgb = RGBColor(40, 40, 40)
            continue

        # قوائم نقطية
        if stripped.startswith("- ") or stripped.startswith("* "):
            p = doc.add_paragraph(style='List Bullet')
            p.alignment = WD_ALIGN_PARAGRAPH.RIGHT
            set_rtl_paragraph(p)
            run = p.add_run(stripped[2:])
            run.font.name = FONT_PRIMARY
            run.font.size = Pt(11.5)
            continue

        # فقرة عادية
        p = doc.add_paragraph()
        p.alignment = WD_ALIGN_PARAGRAPH.JUSTIFY
        set_rtl_paragraph(p)
        run = p.add_run(stripped)
        run.font.name = FONT_PRIMARY
        run.font.size = Pt(12)

    if in_table:
        flush_table()

    doc.save(str(out_file))
    return str(out_file)


def build_official_letter_docx(recipient_title: str, subject: str, letter_content: str, project_name: str, output_path: str, letter_type: str = "general", template_path: Optional[str] = None) -> str:
    """
    توليد خطاب رسمي معتمد (صفحة واحدة A4) مع ضبط RTL والهوامش والتحية والخاتمة
    """
    out_file = Path(output_path).resolve()
    out_file.parent.mkdir(parents=True, exist_ok=True)

    if template_path and os.path.exists(template_path):
        doc = Document(template_path)
    else:
        doc = Document()
        for section in doc.sections:
            section.top_margin = Inches(0.7)
            section.bottom_margin = Inches(0.7)
            section.left_margin = Inches(0.8)
            section.right_margin = Inches(0.8)

    # 1. التاريخ والرقم الإشاري
    date_p = doc.add_paragraph()
    date_p.alignment = WD_ALIGN_PARAGRAPH.LEFT
    set_rtl_paragraph(date_p)
    run_date = date_p.add_run(f"التاريخ: {datetime.date.today().strftime('%Y/%m/%d')}م\nالموافق: {datetime.date.today().strftime('%d-%m-%Y')}")
    run_date.font.name = FONT_PRIMARY
    run_date.font.size = Pt(10)
    run_date.font.color.rgb = COLOR_SECONDARY

    # 2. المخاطب
    to_p = doc.add_paragraph()
    to_p.alignment = WD_ALIGN_PARAGRAPH.RIGHT
    set_rtl_paragraph(to_p)
    run_to = to_p.add_run(f"{recipient_title}\nالمحترمين")
    run_to.font.name = FONT_PRIMARY
    run_to.font.size = Pt(13)
    run_to.font.bold = True

    # تحية
    salut_p = doc.add_paragraph()
    salut_p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    set_rtl_paragraph(salut_p)
    run_salut = salut_p.add_run("السلام عليكم ورحمة الله وبركاته،،، وبعد:")
    run_salut.font.name = FONT_PRIMARY
    run_salut.font.size = Pt(12.5)
    run_salut.font.bold = True

    # 3. الموضوع
    subj_p = doc.add_paragraph()
    subj_p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    set_rtl_paragraph(subj_p)
    run_subj = subj_p.add_run(f"الموضوع: {subject} — مشروع: {project_name}")
    run_subj.font.name = FONT_PRIMARY
    run_subj.font.size = Pt(13)
    run_subj.font.bold = True
    run_subj.font.underline = True
    run_subj.font.color.rgb = COLOR_PRIMARY

    # 4. متن الخطاب
    for paragraph_text in letter_content.split("\n\n"):
        if not paragraph_text.strip():
            continue
        p = doc.add_paragraph()
        p.alignment = WD_ALIGN_PARAGRAPH.JUSTIFY
        set_rtl_paragraph(p)
        r = p.add_run(paragraph_text.strip())
        r.font.name = FONT_PRIMARY
        r.font.size = Pt(12.5)

    # 5. الخاتمة والتوقيع
    close_p = doc.add_paragraph()
    close_p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    set_rtl_paragraph(close_p)
    run_close = close_p.add_run("\nشاكرين ومقدرين حسن تعاونكم الدائم معنا،،،\nوتقبلوا وافر التحية والتقدير،،،")
    run_close.font.name = FONT_PRIMARY
    run_close.font.size = Pt(12)
    run_close.font.bold = True

    sig_p = doc.add_paragraph()
    sig_p.alignment = WD_ALIGN_PARAGRAPH.LEFT
    set_rtl_paragraph(sig_p)
    run_sig = sig_p.add_run("المقاول المنفذ:\nمؤسسة إعمار الفرعة للمقاولات العامة\nالمدير العام / المهندس المسؤول\n_____________________")
    run_sig.font.name = FONT_PRIMARY
    run_sig.font.size = Pt(11.5)
    run_sig.font.bold = True

    doc.save(str(out_file))
    return str(out_file)


def build_inspection_request_wir(project_name: str, wir_number: str, discipline: str, location: str, description: str, inspection_date: str, output_path: str, submittal_ref: Optional[str] = None, template_path: Optional[str] = None) -> str:
    """
    توليد طلب فحص أعمال (Work Inspection Request - WIR) بصيغة Word
    """
    out_file = Path(output_path).resolve()
    out_file.parent.mkdir(parents=True, exist_ok=True)

    if template_path and os.path.exists(template_path):
        doc = Document(template_path)
    else:
        doc = Document()
        for section in doc.sections:
            section.top_margin = Inches(0.6)
            section.bottom_margin = Inches(0.6)
            section.left_margin = Inches(0.6)
            section.right_margin = Inches(0.6)

    # العنوان
    title_p = doc.add_paragraph()
    title_p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    set_rtl_paragraph(title_p)
    r = title_p.add_run(f"طلب استلام وفحص أعمال (WIR)\nWORK INSPECTION REQUEST\n{project_name}")
    r.font.name = FONT_PRIMARY
    r.font.size = Pt(14)
    r.font.bold = True
    r.font.color.rgb = COLOR_PRIMARY

    # جدول بيانات الفحص
    table = doc.add_table(rows=6, cols=2)
    table.alignment = WD_TABLE_ALIGNMENT.CENTER

    data = [
        ("رقم طلب الفحص (WIR No.):", wir_number),
        ("التخصص الهندسي (Discipline):", discipline),
        ("تاريخ الفحص المطلوب (Date):", inspection_date),
        ("موقع العمل بالتفصيل (Location):", location),
        ("المرجع الفني / الاعتماد (Submittal Ref):", submittal_ref or "حسب المواصفات المعتمدة"),
        ("بيان الأعمال المطلوب فحصها (Description):", description),
    ]

    for row_idx, (label, val) in enumerate(data):
        cell_lbl = table.cell(row_idx, 0)
        cell_val = table.cell(row_idx, 1)
        
        set_cell_background(cell_lbl, "D9E1F2")
        set_cell_margins(cell_lbl, 100, 100, 120, 120)
        set_cell_margins(cell_val, 100, 100, 120, 120)

        p_lbl = cell_lbl.paragraphs[0]
        p_lbl.alignment = WD_ALIGN_PARAGRAPH.RIGHT
        set_rtl_paragraph(p_lbl)
        r_lbl = p_lbl.add_run(label)
        r_lbl.font.name = FONT_PRIMARY
        r_lbl.font.size = Pt(11)
        r_lbl.font.bold = True

        p_val = cell_val.paragraphs[0]
        p_val.alignment = WD_ALIGN_PARAGRAPH.RIGHT
        set_rtl_paragraph(p_val)
        r_val = p_val.add_run(val)
        r_val.font.name = FONT_PRIMARY
        r_val.font.size = Pt(11)

    # قسم الاستشاري والاعتماد
    doc.add_paragraph()
    status_p = doc.add_paragraph()
    status_p.alignment = WD_ALIGN_PARAGRAPH.RIGHT
    set_rtl_paragraph(status_p)
    r_stat = status_p.add_run("قرار المهندس المشرف / الاستشاري:\n[  ] مقبول (Approved)       [  ] مقبول مع ملاحظات (Approved as Noted)       [  ] مرفوض ويُعاد التقديم (Revise & Resubmit)")
    r_stat.font.name = FONT_PRIMARY
    r_stat.font.size = Pt(11)
    r_stat.font.bold = True

    doc.save(str(out_file))
    return str(out_file)
