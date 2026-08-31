# -*- coding: utf-8 -*-
"""
سكريبت تحليل ملفات Excel (XLSX) واستخراج بنود الهيكل والأعمدة والترتيب تلقائياً
تأليف: وكيل reference-calibrator
"""

import sys
import os
from openpyxl import load_workbook

def analyze_xlsx(file_path, output_report_path):
    if not os.path.exists(file_path):
        print(f"Error: File not found at {file_path}")
        return False
        
    try:
        wb = load_workbook(file_path, read_only=True, data_only=True)
        report = []
        report.append(f"# تقرير تحليل ملف Excel مرجعي: {os.path.basename(file_path)}")
        report.append(f"- **المسار الكامل:** {file_path}")
        report.append(f"- **أسماء الأوراق (Sheets):** {', '.join(wb.sheetnames)}")
        report.append("\n---\n")
        
        for name in wb.sheetnames:
            ws = wb[name]
            report.append(f"## الورقة: {name}")
            
            # قراءة أول 10 صفوف لتحديد الترويسة والبيانات
            rows = []
            for row in ws.iter_rows(max_row=15, values_only=True):
                rows.append(row)
                
            if not rows:
                report.append("هذه الورقة فارغة.\n")
                continue
                
            report.append(f"- **إجمالي الصفوف المقروءة للتحليل:** {len(rows)}")
            
            # محاولة العثور على أول صف يحتوي على بيانات (الترويسة)
            header_row_idx = 0
            header_found = False
            for idx, r in enumerate(rows):
                # إذا كان الصف يحتوي على قيم غير فارغة متعددة، نعتبره الترويسة
                non_empty = [val for val in r if val is not None]
                if len(non_empty) >= 3:
                    header_row_idx = idx
                    header_found = True
                    break
                    
            if header_found:
                header_vals = [str(val).strip() if val is not None else "" for val in rows[header_row_idx]]
                # تصفية الأعمدة الفارغة في النهاية
                while header_vals and header_vals[-1] == "":
                    header_vals.pop()
                report.append(f"- **رقم صف الترويسة المكتشف:** {header_row_idx + 1}")
                report.append(f"- **أعمدة الجدول المكتشفة بالترتيب ({len(header_vals)} عمود):**")
                for col_num, val in enumerate(header_vals, 1):
                    report.append(f"  {col_num}. `{val}`")
                    
                # عينة من أول 3 صفوف بيانات بعد الترويسة
                report.append("- **عينة من صفوف البيانات التالية:**")
                data_rows_printed = 0
                for r_idx in range(header_row_idx + 1, len(rows)):
                    r_val = rows[r_idx]
                    if any(val is not None for val in r_val):
                        clean_val = [str(val).strip() if val is not None else "" for val in r_val[:len(header_vals)]]
                        report.append(f"  - صف {r_idx + 1}: { ' | '.join(clean_val) }")
                        data_rows_printed += 1
                        if data_rows_printed >= 4:
                            break
            else:
                report.append("- لم يتم تحديد ترويسة واضحة. عينة من الصفوف الأولى:")
                for idx, r in enumerate(rows[:5]):
                    clean_r = [str(val).strip() if val is not None else "" for val in r if val is not None]
                    report.append(f"  - صف {idx + 1}: { ' / '.join(clean_r) }")
            report.append("\n")
            
        # حفظ التقرير
        os.makedirs(os.path.dirname(output_report_path), exist_ok=True)
        with open(output_report_path, 'w', encoding='utf-8') as f:
            f.write('\n'.join(report))
            
        print(f"Success: Report generated at {output_report_path}")
        return True
    except Exception as e:
        print(f"Error during analysis: {str(e)}")
        return False

if __name__ == "__main__":
    if len(sys.argv) < 3:
        analyze_xlsx(
            r"G:\M.Yonis\حديقة الملك فهد الحلوة\A-1420.xlsx",
            r"g:\M.Yonis\.agents\skills\references\scripts\analysis_report_excel.txt"
        )
    else:
        analyze_xlsx(sys.argv[1], sys.argv[2])
