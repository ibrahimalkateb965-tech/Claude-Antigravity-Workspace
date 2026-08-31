#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
محرك نماذج واستلامات تسليم الصندوق TBC (Handover Engine)
يدعم تعبئة بطاقة الصندوق (Box Label)، قائمة الفحص (Form R09)، وفهرس المحتويات
"""

import os
import shutil
import zipfile
from pathlib import Path
from typing import List, Dict, Any, Optional

import docx
from docx import Document
from docx.shared import Inches, Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_ALIGN_VERTICAL, WD_TABLE_ALIGNMENT
from docx.oxml import parse_xml
from docx.oxml.ns import nsdecls


def set_cell_background(cell, fill_hex: str):
    tcPr = cell._tc.get_or_add_tcPr()
    shd = parse_xml(f'<w:shd {nsdecls("w")} w:fill="{fill_hex}"/>')
    tcPr.append(shd)


def set_cell_margins(cell, top=80, bottom=80, left=100, right=100):
    tcPr = cell._tc.get_or_add_tcPr()
    tcMar = parse_xml(f'<w:tcMar {nsdecls("w")}><w:top w:w="{top}" w:type="dxa"/><w:bottom w:w="{bottom}" w:type="dxa"/><w:left w:w="{left}" w:type="dxa"/><w:right w:w="{right}" w:type="dxa"/></w:tcMar>')
    tcPr.append(tcMar)


def format_cell_text(cell, text: str, font_name="Calibri", font_size=9.5, bold=False, color_rgb=(0,0,0), align=WD_ALIGN_PARAGRAPH.CENTER, is_rtl=False):
    cell.text = ""
    p = cell.paragraphs[0]
    p.alignment = align
    if is_rtl:
        pPr = p._element.get_or_add_pPr()
        pPr.append(parse_xml(f'<w:bidi {nsdecls("w")}/>'))
    run = p.add_run(str(text))
    run.font.name = font_name
    run.font.size = Pt(font_size)
    run.font.bold = bold
    run.font.color.rgb = RGBColor(*color_rgb)


def generate_tbc_box_label(project_info: Dict[str, Any], output_path: str, template_path: Optional[str] = None) -> str:
    """
    توليد بطاقة تعريف صندوق الاستلام (Handover Box Label)
    """
    out_file = Path(output_path).resolve()
    out_file.parent.mkdir(parents=True, exist_ok=True)

    if template_path and os.path.exists(template_path):
        shutil.copy2(template_path, str(out_file))
        doc = Document(str(out_file))
        
        # استبدال النصوص في الجداول
        replacements = {
            "PROJECT_NAME": project_info.get("project_name", "مدرسة الرياض"),
            "CONTRACT_NO": project_info.get("contract_no", ""),
            "BOX_NO": project_info.get("box_no", "1 / 1"),
            "CONSULTANT": project_info.get("consultant_name", "دار الرؤية للاستشارات الهندسية"),
            "CONTRACTOR": project_info.get("contractor_name", "مؤسسة إعمار الفرعة للمقاولات العامة"),
            "DATE": project_info.get("submission_date", "")
        }

        for table in doc.tables:
            for row in table.rows:
                for cell in row.cells:
                    for k, v in replacements.items():
                        if k in cell.text:
                            cell.text = cell.text.replace(k, v)
        doc.save(str(out_file))
    else:
        # إنشاء بطاقة معيارية جديدة
        doc = Document()
        table = doc.add_table(rows=6, cols=2)
        table.alignment = WD_TABLE_ALIGNMENT.CENTER
        
        fields = [
            ("اسم المشروع (Project Name):", project_info.get("project_name", "")),
            ("رقم العقد (Contract No.):", project_info.get("contract_no", "")),
            ("رقم الصندوق (Box Number):", project_info.get("box_no", "Box 1 of 1")),
            ("الاستشاري المشرف (Consultant):", project_info.get("consultant_name", "")),
            ("المقاول المنفذ (Contractor):", project_info.get("contractor_name", "مؤسسة إعمار الفرعة للمقاولات")),
            ("تاريخ التسليم (Date of Submission):", project_info.get("submission_date", ""))
        ]

        for idx, (label, val) in enumerate(fields):
            cell_lbl = table.cell(idx, 0)
            cell_val = table.cell(idx, 1)
            set_cell_background(cell_lbl, "1F497D")
            format_cell_text(cell_lbl, label, font_name="Traditional Arabic", font_size=12, bold=True, color_rgb=(255,255,255), is_rtl=True)
            format_cell_text(cell_val, val, font_name="Traditional Arabic", font_size=12, bold=True, is_rtl=True)

        doc.save(str(out_file))

    return str(out_file)


def generate_handover_checklist_r09(project_info: Dict[str, Any], items_status: List[Dict[str, Any]], output_path: str, template_path: Optional[str] = None) -> str:
    """
    تعبئة نموذج قائمة استلام وثائق المشروع TBC Handover Check List Form R09
    """
    out_file = Path(output_path).resolve()
    out_file.parent.mkdir(parents=True, exist_ok=True)

    if template_path and os.path.exists(template_path):
        shutil.copy2(template_path, str(out_file))
        doc = Document(str(out_file))
    else:
        doc = Document()
        title_p = doc.add_paragraph()
        title_p.alignment = WD_ALIGN_PARAGRAPH.CENTER
        run = title_p.add_run(f"HANDOVER CHECK LIST FORM - R09\n{project_info.get('project_name', '')}")
        run.font.name = "Calibri"
        run.font.size = Pt(14)
        run.font.bold = True

    # حفظ الملف
    doc.save(str(out_file))
    return str(out_file)
