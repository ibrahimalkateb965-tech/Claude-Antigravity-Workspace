# -*- coding: utf-8 -*-
"""
سكريبت توليد الجداول الزمنية بصيغة Excel
تأليف: وكيل schedule-builder
"""

import os
import datetime
from openpyxl import Workbook
from openpyxl.styles import Font, Alignment, PatternFill, Border, Side
from openpyxl.utils import get_column_letter

# استيراد خوارزمية المسار الحرج
try:
    from cpm_calculator import CPMCalculator
except ImportError:
    from .cpm_calculator import CPMCalculator

def parse_date(date_str):
    """تحويل النص إلى تاريخ"""
    for fmt in ('%Y-%m-%d', '%d-%m-%Y', '%Y/%m/%d', '%d/%m/%Y'):
        try:
            return datetime.datetime.strptime(date_str, fmt).date()
        except ValueError:
            pass
    return datetime.date.today()

def add_days(start_date, days):
    """إضافة أيام عمل للبداية للحصول على تاريخ النهاية"""
    # الأيام المدخلة تبدأ من اليوم الأول (المدة 1 تعني نفس يوم البداية)
    return start_date + datetime.timedelta(days=max(0, days - 1))

def generate_excel_schedule(project_name, start_date_str, activities_data, output_path):
    """
    توليد ملف الجدول الزمني
    activities_data: قائمة بقواميس الأنشطة:
    [
        {"id": "A", "wbs": "1.1", "name": "النشاط 1", "duration": 5, "predecessors": []},
        ...
    ]
    """
    start_date = parse_date(start_date_str)
    
    # 1. حساب المسار الحرج
    calc = CPMCalculator()
    for act in activities_data:
        calc.add_activity(act["id"], act["name"], act["duration"], act.get("predecessors", []))
        
    results = calc.calculate()
    
    # 2. إنشاء ملف Excel
    wb = Workbook()
    ws = wb.active
    ws.title = "الجدول الزمني"
    ws.views.sheetView[0].showGridLines = True
    
    # ضبط الاتجاه من اليمين إلى اليسار RTL
    ws.sheet_properties.pageSetUpPr.fitToPage = True
    ws.sheet_view.rightToLeft = True
    
    # 3. التنسيقات والألوان (SLEEK BLUE PALETTE)
    font_family = "Traditional Arabic"
    title_font = Font(name=font_family, size=16, bold=True, color="1F497D")
    header_font = Font(name=font_family, size=12, bold=True, color="FFFFFF")
    data_font = Font(name=font_family, size=11, bold=False)
    bold_data_font = Font(name=font_family, size=11, bold=True)
    critical_font = Font(name=font_family, size=11, bold=True, color="C00000")
    
    header_fill = PatternFill(start_color="1F497D", end_color="1F497D", fill_type="solid")
    stripe_fill = PatternFill(start_color="F2F5F8", end_color="F2F5F8", fill_type="solid")
    critical_fill = PatternFill(start_color="FFCCCC", end_color="FFCCCC", fill_type="solid")
    
    align_center = Alignment(horizontal="center", vertical="center", wrap_text=True)
    align_right = Alignment(horizontal="right", vertical="center", wrap_text=True)
    
    thin_border_side = Side(border_style="thin", color="D3D3D3")
    thin_border = Border(left=thin_border_side, right=thin_border_side, top=thin_border_side, bottom=thin_border_side)
    thick_bottom = Border(bottom=Side(border_style="medium", color="1F497D"))
    
    # 4. ترويسة العنوان
    ws.merge_cells("A1:H1")
    ws["A1"] = f"الجدول الزمني لمشروع: {project_name}"
    ws["A1"].font = title_font
    ws["A1"].alignment = align_center
    ws.row_dimensions[1].height = 40
    
    ws.merge_cells("A2:H2")
    ws["A2"] = f"تاريخ البدء المعتمد: {start_date.strftime('%Y-%m-%d')}م"
    ws["A2"].font = Font(name=font_family, size=12, italic=True)
    ws["A2"].alignment = align_center
    ws.row_dimensions[2].height = 20
    
    # 5. عناوين الجدول
    headers = [
        "رمز WBS", "اسم النشاط", "المدة (يوم)", 
        "تاريخ البدء المتوقع", "تاريخ الانتهاء المتوقع", 
        "الأنشطة السابقة", "فترة السماح (يوم)", "حالة النشاط"
    ]
    
    for col_idx, header in enumerate(headers, 1):
        cell = ws.cell(row=4, column=col_idx, value=header)
        cell.font = header_font
        cell.fill = header_fill
        cell.alignment = align_center
        cell.border = thin_border
        
    ws.row_dimensions[4].height = 28
    
    # 6. إدخال البيانات والأنشطة
    row_idx = 5
    for act_info in activities_data:
        act_id = act_info["id"]
        wbs = act_info.get("wbs", "")
        
        # جلب الحسابات من نتائج المسار الحرج
        cpm_result = results.get(act_id)
        if cpm_result:
            es_days = cpm_result.es
            ef_days = cpm_result.ef
            tf = cpm_result.tf
            is_critical = cpm_result.is_critical
            
            # حساب التواريخ الفعلية
            act_start = start_date + datetime.timedelta(days=es_days - 1)
            act_finish = start_date + datetime.timedelta(days=ef_days - 1)
            
            status_text = "حرج" if is_critical else "اعتيادي"
        else:
            act_start = start_date
            act_finish = add_days(start_date, act_info["duration"])
            tf = 0
            is_critical = False
            status_text = "غير محدد"
            
        # كتابة البيانات في الخلايا
        ws.cell(row=row_idx, column=1, value=wbs).alignment = align_center
        ws.cell(row=row_idx, column=2, value=act_info["name"]).alignment = align_right
        ws.cell(row=row_idx, column=3, value=act_info["duration"]).alignment = align_center
        ws.cell(row=row_idx, column=4, value=act_start.strftime("%Y-%m-%d")).alignment = align_center
        ws.cell(row=row_idx, column=5, value=act_finish.strftime("%Y-%m-%d")).alignment = align_center
        
        preds_str = ",".join(act_info.get("predecessors", []))
        ws.cell(row=row_idx, column=6, value=preds_str).alignment = align_center
        ws.cell(row=row_idx, column=7, value=tf).alignment = align_center
        ws.cell(row=row_idx, column=8, value=status_text).alignment = align_center
        
        # تطبيق التنسيق والنمط على الصف
        is_stripe = (row_idx % 2 == 0)
        for col_idx in range(1, 9):
            cell = ws.cell(row=row_idx, column=col_idx)
            cell.border = thin_border
            
            # خط النشاط الحرج
            if is_critical:
                cell.font = critical_font
                cell.fill = critical_fill
            else:
                cell.font = data_font
                if is_stripe:
                    cell.fill = stripe_fill
                    
        ws.row_dimensions[row_idx].height = 22
        row_idx += 1
        
    # 7. ملاءمة عرض الأعمدة تلقائياً
    for col in ws.columns:
        max_len = 0
        col_letter = get_column_letter(col[0].column)
        for cell in col:
            if cell.row < 4:  # تجاوز صفوف العنوان في الملاءمة
                continue
            if cell.value:
                # حساب الطول التقريبي مع مراعاة النصوص العربية
                val_str = str(cell.value)
                max_len = max(max_len, len(val_str))
        ws.column_dimensions[col_letter].width = max(max_len + 4, 12)
        
    # تعديل خاص لعمود اسم النشاط ليتسع بشكل مريح
    ws.column_dimensions["B"].width = 40
    
    # حفظ الملف
    os.makedirs(os.path.dirname(output_path), exist_ok=True)
    wb.save(output_path)
    return output_path

if __name__ == "__main__":
    test_acts = [
        {"id": "A", "wbs": "1", "name": "أعمال التحضير واعتماد المواد", "duration": 7, "predecessors": []},
        {"id": "B", "wbs": "2", "name": "أعمال الحفر وتجهيز الموقع", "duration": 10, "predecessors": ["A"]},
        {"id": "C", "wbs": "3", "name": "توريد وصب الخرسانة العادية", "duration": 5, "predecessors": ["B"]},
        {"id": "D", "wbs": "4", "name": "أعمال المباني والعزل", "duration": 8, "predecessors": ["B"]},
        {"id": "E", "wbs": "5", "name": "أعمال اللياسة والدهانات", "duration": 15, "predecessors": ["C", "D"]},
        {"id": "F", "wbs": "6", "name": "التسليم الابتدائي والتنظيف", "duration": 3, "predecessors": ["E"]}
    ]
    generate_excel_schedule("مشروع إنشاء سور المقبرة", "2026-08-01", test_acts, "g:/M.Yonis/.agents/skills/references/scripts/test_schedule.xlsx")
    print("تم توليد ملف التجربة بنجاح في: test_schedule.xlsx")
