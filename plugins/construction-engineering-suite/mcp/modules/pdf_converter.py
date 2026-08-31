#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
محرك تحويل المستندات إلى PDF عبر COM Automation
PDF Converter Engine (Word & Excel to PDF)
"""

import os
import sys
import subprocess
from pathlib import Path
from typing import Optional


def convert_docx_to_pdf(input_path: str, output_path: Optional[str] = None) -> str:
    """
    تحويل ملف DOCX إلى PDF عبر Word COM Automation أو PowerShell
    """
    in_file = Path(input_path).resolve()
    if not in_file.exists():
        raise FileNotFoundError(f"الملف المصدر غير موجود: {in_file}")

    if output_path:
        out_file = Path(output_path).resolve()
    else:
        out_file = in_file.with_suffix(".pdf")

    out_file.parent.mkdir(parents=True, exist_ok=True)

    # 1. محاولة استخدام win32com المباشر
    try:
        import win32com.client
        word = win32com.client.Dispatch("Word.Application")
        word.Visible = False
        try:
            doc = word.Documents.Open(str(in_file))
            # 17 = wdExportFormatPDF
            doc.SaveAs(str(out_file), FileFormat=17)
            doc.Close()
            return str(out_file)
        finally:
            word.Quit()
    except Exception as e_com:
        # 2. خطة بديلة: استخدام سكريبت PowerShell معزول
        ps_cmd = f'''
        $word = New-Object -ComObject Word.Application
        $word.Visible = $false
        try {{
            $doc = $word.Documents.Open("{in_file}")
            $doc.SaveAs("{out_file}", 17)
            $doc.Close()
        }} finally {{
            $word.Quit()
        }}
        '''
        res = subprocess.run(["powershell", "-NoProfile", "-Command", ps_cmd], capture_output=True, text=True)
        if res.returncode == 0 and out_file.exists():
            return str(out_file)
        else:
            raise RuntimeError(f"فشل تحويل DOCX إلى PDF: {res.stderr or str(e_com)}")


def convert_xlsx_to_pdf(input_path: str, output_path: Optional[str] = None) -> str:
    """
    تحويل ملف Excel (XLSX) إلى PDF
    """
    in_file = Path(input_path).resolve()
    if not in_file.exists():
        raise FileNotFoundError(f"الملف المصدر غير موجود: {in_file}")

    if output_path:
        out_file = Path(output_path).resolve()
    else:
        out_file = in_file.with_suffix(".pdf")

    out_file.parent.mkdir(parents=True, exist_ok=True)

    try:
        import win32com.client
        excel = win32com.client.Dispatch("Excel.Application")
        excel.Visible = False
        try:
            wb = excel.Workbooks.Open(str(in_file))
            # 0 = xlTypePDF
            wb.ExportAsFixedFormat(0, str(out_file))
            wb.Close(False)
            return str(out_file)
        finally:
            excel.Quit()
    except Exception as e_com:
        ps_cmd = f'''
        $excel = New-Object -ComObject Excel.Application
        $excel.Visible = $false
        try {{
            $wb = $excel.Workbooks.Open("{in_file}")
            $wb.ExportAsFixedFormat(0, "{out_file}")
            $wb.Close($false)
        }} finally {{
            $excel.Quit()
        }}
        '''
        res = subprocess.run(["powershell", "-NoProfile", "-Command", ps_cmd], capture_output=True, text=True)
        if res.returncode == 0 and out_file.exists():
            return str(out_file)
        else:
            raise RuntimeError(f"فشل تحويل XLSX إلى PDF: {res.stderr or str(e_com)}")
