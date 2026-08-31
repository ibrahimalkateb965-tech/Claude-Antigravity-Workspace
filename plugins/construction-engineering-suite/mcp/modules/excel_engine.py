#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
محرك توليد ملفات الإكسيل الهندسية (BOQ, CPM Schedule, Daily Reports)
Excel Engineering Engine
"""

import os
import datetime
from pathlib import Path
from typing import List, Dict, Any, Optional
from openpyxl import Workbook, load_workbook
from openpyxl.styles import Font, Alignment, PatternFill, Border, Side
from openpyxl.utils import get_column_letter

from .cpm_core import CPMCalculator


def parse_date(date_str: str) -> datetime.date:
    """تحويل النص إلى تاريخ"""
    for fmt in ('%Y-%m-%d', '%d-%m-%Y', '%Y/%m/%d', '%d/%m/%Y'):
        try:
            return datetime.datetime.strptime(str(date_str).strip(), fmt).date()
        except ValueError:
            pass
    return datetime.date.today()


def build_engineering_boq(project_name: str, items: List[Dict[str, Any]], output_path: str, vat_rate: float = 0.15, template_path: Optional[str] = None) -> Dict[str, Any]:
    """
    توليد ملف جدول الكميات والتسعير (BOQ) بصيغة Excel بتنسيق عربي RTL احترافي
    """
    out_file = Path(output_path).resolve()
    out_file.parent.mkdir(parents=True, exist_ok=True)

    if template_path and os.path.exists(template_path):
        wb = load_workbook(template_path)
        ws = wb.active
    else:
        wb = Workbook()
        ws = wb.active
        ws.title = "جدول الكميات والتسعير"

    ws.views.sheetView[0].showGridLines = True
    ws.sheet_view.rightToLeft = True

    font_family = "Traditional Arabic"
    title_font = Font(name=font_family, size=16, bold=True, color="1F497D")
    header_font = Font(name=font_family, size=12, bold=True, color="FFFFFF")
    data_font = Font(name=font_family, size=11)
    bold_data_font = Font(name=font_family, size=11, bold=True)
    
    header_fill = PatternFill(start_color="1F497D", end_color="1F497D", fill_type="solid")
    stripe_fill = PatternFill(start_color="F2F5F8", end_color="F2F5F8", fill_type="solid")
    total_fill = PatternFill(start_color="D9E1F2", end_color="D9E1F2", fill_type="solid")
    
    align_center = Alignment(horizontal="center", vertical="center", wrap_text=True)
    align_right = Alignment(horizontal="right", vertical="center", wrap_text=True)
    align_left = Alignment(horizontal="left", vertical="center")
    
    thin_border_side = Side(border_style="thin", color="D3D3D3")
    thin_border = Border(left=thin_border_side, right=thin_border_side, top=thin_border_side, bottom=thin_border_side)
    thick_bottom = Border(bottom=Side(border_style="medium", color="1F497D"), top=thin_border_side, left=thin_border_side, right=thin_border_side)

    # 1. الترويسة والعنوان
    ws.merge_cells("A1:G1")
    ws["A1"] = f"جدول الكميات والتسعير الهندسي — مشروع: {project_name}"
    ws["A1"].font = title_font
    ws["A1"].alignment = align_center
    ws.row_dimensions[1].height = 40

    ws.merge_cells("A2:G2")
    ws["A2"] = f"تاريخ الإصدار: {datetime.date.today().strftime('%Y-%m-%d')} | نسبة ضريبة القيمة المضافة: {int(vat_rate * 100)}%"
    ws["A2"].font = Font(name=font_family, size=10, italic=True, color="595959")
    ws["A2"].alignment = align_center
    ws.row_dimensions[2].height = 20

    # 2. عناوين الأعمدة
    headers = ["رقم البند", "بيان الأعمال (وصف البند)", "الوحدة", "الكمية", "سعر الوحدة (SAR)", "السعر الإجمالي (SAR)", "ملاحظات"]
    for col_idx, header in enumerate(headers, 1):
        cell = ws.cell(row=4, column=col_idx, value=header)
        cell.font = header_font
        cell.fill = header_fill
        cell.alignment = align_center
        cell.border = thin_border
    ws.row_dimensions[4].height = 30

    # 3. إدراج البيانات
    current_row = 5
    subtotal_before_vat = 0.0

    for idx, item in enumerate(items, 1):
        item_no = item.get("item_no", idx)
        desc = item.get("description", "")
        unit = item.get("unit", "مقطوعية")
        qty = float(item.get("quantity", 1))
        unit_price = float(item.get("unit_price", 0))
        total_price = qty * unit_price
        notes = item.get("notes", "")
        subtotal_before_vat += total_price

        is_even = (idx % 2 == 0)
        row_fill = stripe_fill if is_even else None

        row_data = [
            (1, item_no, align_center, data_font, None),
            (2, desc, align_right, data_font, None),
            (3, unit, align_center, data_font, None),
            (4, qty, align_center, data_font, "#,##0.00"),
            (5, unit_price, align_left, data_font, "#,##0.00"),
            (6, total_price, align_left, bold_data_font, "#,##0.00"),
            (7, notes, align_right, data_font, None)
        ]

        for col_idx, val, alignment, font, num_fmt in row_data:
            cell = ws.cell(row=current_row, column=col_idx, value=val)
            cell.font = font
            cell.alignment = alignment
            cell.border = thin_border
            if row_fill:
                cell.fill = row_fill
            if num_fmt:
                cell.number_format = num_fmt

        ws.row_dimensions[current_row].height = 25
        current_row += 1

    # 4. ملخص الإجماليات والضريبة
    ws.cell(row=current_row, column=1).border = thin_border
    ws.merge_cells(start_row=current_row, start_column=2, end_row=current_row, end_column=5)
    sum_cell = ws.cell(row=current_row, column=2, value="المجموع الإجمالي (قبل الضريبة):")
    sum_cell.font = bold_data_font
    sum_cell.alignment = align_right
    sum_cell.fill = total_fill
    
    val_cell = ws.cell(row=current_row, column=6, value=subtotal_before_vat)
    val_cell.font = Font(name=font_family, size=11, bold=True, color="1F497D")
    val_cell.alignment = align_left
    val_cell.fill = total_fill
    val_cell.number_format = "#,##0.00"
    val_cell.border = thin_border
    ws.row_dimensions[current_row].height = 25
    current_row += 1

    # الضريبة
    vat_amount = subtotal_before_vat * vat_rate
    ws.merge_cells(start_row=current_row, start_column=2, end_row=current_row, end_column=5)
    vat_cell = ws.cell(row=current_row, column=2, value=f"ضريبة القيمة المضافة ({int(vat_rate*100)}%):")
    vat_cell.font = bold_data_font
    vat_cell.alignment = align_right
    vat_cell.fill = total_fill

    vat_val_cell = ws.cell(row=current_row, column=6, value=vat_amount)
    vat_val_cell.font = bold_data_font
    vat_val_cell.alignment = align_left
    vat_val_cell.fill = total_fill
    vat_val_cell.number_format = "#,##0.00"
    vat_val_cell.border = thin_border
    ws.row_dimensions[current_row].height = 25
    current_row += 1

    # الإجمالي الصافي
    net_total = subtotal_before_vat + vat_amount
    ws.merge_cells(start_row=current_row, start_column=2, end_row=current_row, end_column=5)
    net_cell = ws.cell(row=current_row, column=2, value="الإجمالي النهائي شامل الضريبة (SAR):")
    net_cell.font = Font(name=font_family, size=12, bold=True, color="C00000")
    net_cell.alignment = align_right
    net_cell.fill = PatternFill(start_color="FCE4D6", end_color="FCE4D6", fill_type="solid")

    net_val_cell = ws.cell(row=current_row, column=6, value=net_total)
    net_val_cell.font = Font(name=font_family, size=12, bold=True, color="C00000")
    net_val_cell.alignment = align_left
    net_val_cell.fill = PatternFill(start_color="FCE4D6", end_color="FCE4D6", fill_type="solid")
    net_val_cell.number_format = "#,##0.00"
    net_val_cell.border = thick_bottom
    ws.row_dimensions[current_row].height = 28

    # ضبط عروض الأعمدة
    column_widths = {1: 12, 2: 45, 3: 12, 4: 15, 5: 18, 6: 22, 7: 20}
    for col_idx, width in column_widths.items():
        ws.column_dimensions[get_column_letter(col_idx)].width = width

    wb.save(str(out_file))
    return {
        "status": "success",
        "output_path": str(out_file),
        "items_count": len(items),
        "subtotal": subtotal_before_vat,
        "vat_amount": vat_amount,
        "net_total": net_total
    }


def build_cpm_schedule_excel(project_name: str, start_date_str: str, activities: List[Dict[str, Any]], output_path: str, template_path: Optional[str] = None) -> Dict[str, Any]:
    """
    توليد جدول الأنشطة وحساب المسار الحرج (CPM) وتصديره لإكسيل
    """
    out_file = Path(output_path).resolve()
    out_file.parent.mkdir(parents=True, exist_ok=True)

    start_date = parse_date(start_date_str)
    calc = CPMCalculator()

    for act in activities:
        calc.add_activity(
            activity_id=act.get("id"),
            name=act.get("name", ""),
            duration=int(act.get("duration", 1)),
            predecessors=act.get("predecessors", []),
            wbs=act.get("wbs", "")
        )

    cpm_results = calc.calculate()

    if template_path and os.path.exists(template_path):
        wb = load_workbook(template_path)
        ws = wb.active
    else:
        wb = Workbook()
        ws = wb.active
        ws.title = "الجدول الزمني CPM"

    ws.views.sheetView[0].showGridLines = True
    ws.sheet_view.rightToLeft = True

    font_family = "Traditional Arabic"
    title_font = Font(name=font_family, size=16, bold=True, color="1F497D")
    header_font = Font(name=font_family, size=11, bold=True, color="FFFFFF")
    data_font = Font(name=font_family, size=10)
    critical_font = Font(name=font_family, size=10, bold=True, color="C00000")
    
    header_fill = PatternFill(start_color="1F497D", end_color="1F497D", fill_type="solid")
    crit_fill = PatternFill(start_color="FCE4D6", end_color="FCE4D6", fill_type="solid")
    normal_fill = PatternFill(start_color="FFFFFF", end_color="FFFFFF", fill_type="solid")
    
    align_center = Alignment(horizontal="center", vertical="center")
    align_right = Alignment(horizontal="right", vertical="center")
    thin_border_side = Side(border_style="thin", color="D3D3D3")
    thin_border = Border(left=thin_border_side, right=thin_border_side, top=thin_border_side, bottom=thin_border_side)

    # 1. ترويسة
    ws.merge_cells("A1:K1")
    ws["A1"] = f"الجدول الزمني الهندسي وحساب المسار الحرج (CPM) — مشروع: {project_name}"
    ws["A1"].font = title_font
    ws["A1"].alignment = align_center
    ws.row_dimensions[1].height = 40

    ws.merge_cells("A2:K2")
    ws["A2"] = f"تاريخ البداية: {start_date} | إجمالي المدة الزمنية: {cpm_results['total_duration']} يوم عمل | الأنشطة الحرجة: {len(cpm_results['critical_path'])}"
    ws["A2"].font = Font(name=font_family, size=10, italic=True, color="595959")
    ws["A2"].alignment = align_center
    ws.row_dimensions[2].height = 20

    headers = [
        "معرف النشاط", "WBS", "اسم النشاط", "المدة (أيام)", "الاعتماديات",
        "البداية المبكرة (ES)", "النهاية المبكرة (EF)", "البداية المتأخرة (LS)",
        "النهاية المتأخرة (LF)", "السماحية الكلية (TF)", "المسار الحرج"
    ]

    for col_idx, h in enumerate(headers, 1):
        cell = ws.cell(row=4, column=col_idx, value=h)
        cell.font = header_font
        cell.fill = header_fill
        cell.alignment = align_center
        cell.border = thin_border
    ws.row_dimensions[4].height = 28

    current_row = 5
    for act in cpm_results["activities"]:
        is_crit = act["is_critical"]
        row_font = critical_font if is_crit else data_font
        row_fill = crit_fill if is_crit else normal_fill
        crit_label = "حرج (Critical)" if is_crit else "عادي"

        es_date = start_date + datetime.timedelta(days=act["es"])
        ef_date = start_date + datetime.timedelta(days=max(0, act["ef"] - 1))

        row_vals = [
            (1, act["id"], align_center),
            (2, act["wbs"], align_center),
            (3, act["name"], align_right),
            (4, act["duration"], align_center),
            (5, ", ".join(act["predecessors"]) if act["predecessors"] else "-", align_center),
            (6, f"{act['es']} ({es_date})", align_center),
            (7, f"{act['ef']} ({ef_date})", align_center),
            (8, act["ls"], align_center),
            (9, act["lf"], align_center),
            (10, act["tf"], align_center),
            (11, crit_label, align_center)
        ]

        for col_idx, val, alignment in row_vals:
            cell = ws.cell(row=current_row, column=col_idx, value=val)
            cell.font = row_font
            cell.alignment = alignment
            cell.border = thin_border
            cell.fill = row_fill

        ws.row_dimensions[current_row].height = 24
        current_row += 1

    widths = {1: 12, 2: 10, 3: 35, 4: 12, 5: 15, 6: 20, 7: 20, 8: 15, 9: 15, 10: 15, 11: 18}
    for col_idx, width in widths.items():
        ws.column_dimensions[get_column_letter(col_idx)].width = width

    wb.save(str(out_file))
    return {
        "status": "success",
        "output_path": str(out_file),
        "total_duration_days": cpm_results["total_duration"],
        "critical_path_activities": cpm_results["critical_path"],
        "activities_count": len(activities)
    }
