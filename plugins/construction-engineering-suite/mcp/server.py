#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
خادم FastMCP الموحد للمنظومة الهندسية والمقاولات
Construction Engineering Suite (FastMCP Server)
"""

import os
import sys
import asyncio
from pathlib import Path
from typing import List, Dict, Any, Optional
from pydantic import BaseModel, Field
from fastmcp import FastMCP

# استيراد الوحدات الهندسية
from modules.docx_engine import (
    build_arabic_docx_from_markdown,
    build_official_letter_docx,
    build_inspection_request_wir
)
from modules.excel_engine import (
    build_engineering_boq,
    build_cpm_schedule_excel
)
from modules.handover_engine import (
    generate_tbc_box_label,
    generate_handover_checklist_r09
)
from modules.pdf_converter import (
    convert_docx_to_pdf,
    convert_xlsx_to_pdf
)

mcp = FastMCP("construction-engineering-suite")


# -------------------------------------------------------------
# 1. أداة توليد مستندات الوورد من الماركداون (generate_arabic_docx)
# -------------------------------------------------------------
class GenerateArabicDocxRequest(BaseModel):
    markdown_content: str = Field(..., description="المحتوى بصيغة Markdown مع الجداول والعناوين والقوائم")
    output_path: str = Field(..., description="المسار المطلق لحفظ ملف Word (DOCX)")
    template_path: Optional[str] = Field(None, description="مسار اختياري لقالب DOCX مخصص")

@mcp.tool()
async def generate_arabic_docx(markdown_content: str, output_path: str, template_path: Optional[str] = None) -> str:
    """
    توليد مستند Microsoft Word عربي عالي الجودة من نص Markdown مع ضبط RTL والجداول والهوامش.
    """
    result_path = await asyncio.to_thread(
        build_arabic_docx_from_markdown,
        markdown_content,
        output_path,
        template_path
    )
    return f"تم بنجاح توليد مستند Word العربي: {result_path}"


# -------------------------------------------------------------
# 2. أداة جداول الكميات والتسعير (generate_engineering_boq)
# -------------------------------------------------------------
class BoqItem(BaseModel):
    item_no: Any = Field(..., description="رقم البند")
    description: str = Field(..., description="بيان ووصف الأعمال")
    unit: str = Field("مقطوعية", description="وحدة القياس (م2، م.ط، عدد، مقطوعية)")
    quantity: float = Field(1.0, description="الكمية")
    unit_price: float = Field(0.0, description="سعر الوحدة بالريال السعودي")
    notes: Optional[str] = Field("", description="ملاحظات إضافية")

class GenerateBoqRequest(BaseModel):
    project_name: str = Field(..., description="اسم المشروع بالعربية")
    items: List[Dict[str, Any]] = Field(..., description="قائمة بنود جدول الكميات")
    output_path: str = Field(..., description="المسار المطلق لحفظ ملف الإكسيل (XLSX)")
    vat_rate: Optional[float] = Field(0.15, description="نسبة ضريبة القيمة المضافة (الافتراضي 0.15)")
    template_path: Optional[str] = Field(None, description="مسار اختياري لقالب إكسيل")

@mcp.tool()
async def generate_engineering_boq(project_name: str, items: List[Dict[str, Any]], output_path: str, vat_rate: float = 0.15, template_path: Optional[str] = None) -> str:
    """
    توليد جدول كميات وتسعير هندسي (BOQ) بصيغة Excel (RTL) مع حساب الإجماليات والضريبة 15%.
    """
    res = await asyncio.to_thread(
        build_engineering_boq,
        project_name,
        items,
        output_path,
        vat_rate,
        template_path
    )
    return f"تم بنجاح توليد جدول الكميات: {res['output_path']} | الإجمالي قبل الضريبة: {res['subtotal']:,.2f} ريال | الصافي شاملاً الضريبة: {res['net_total']:,.2f} ريال"


# -------------------------------------------------------------
# 3. أداة حساب المسار الحرج والجدول الزمني (generate_cpm_schedule)
# -------------------------------------------------------------
class GenerateScheduleRequest(BaseModel):
    project_name: str = Field(..., description="اسم المشروع")
    start_date: str = Field(..., description="تاريخ بدء المشروع بصيغة YYYY-MM-DD")
    activities: List[Dict[str, Any]] = Field(..., description="قائمة الأنشطة الهندسية والاعتماديات")
    output_path: str = Field(..., description="المسار المطلق لحفظ ملف الإكسيل (XLSX)")
    template_path: Optional[str] = Field(None, description="مسار اختياري لقالب إكسيل")

@mcp.tool()
async def generate_cpm_schedule(project_name: str, start_date: str, activities: List[Dict[str, Any]], output_path: str, template_path: Optional[str] = None) -> str:
    """
    حساب المسار الحرج (CPM) وتوليد جدول الأنشطة الزمني في ملف Excel.
    """
    res = await asyncio.to_thread(
        build_cpm_schedule_excel,
        project_name,
        start_date,
        activities,
        output_path,
        template_path
    )
    return f"تم بنجاح توليد الجدول الزمني CPM: {res['output_path']} | مدة المشروع: {res['total_duration_days']} يوم عمل | الأنشطة الحرجة: {', '.join(res['critical_path_activities'])}"


# -------------------------------------------------------------
# 4. أداة الخطابات الرسمية المعتمدة (generate_official_letter)
# -------------------------------------------------------------
class GenerateLetterRequest(BaseModel):
    recipient_title: str = Field(..., description="الجهة أو الشخص المخاطب مع اللقب")
    subject: str = Field(..., description="موضوع الخطاب")
    letter_content: str = Field(..., description="نص ومتن الخطاب")
    project_name: str = Field(..., description="اسم المشروع")
    output_path: str = Field(..., description="المسار المطلق لحفظ ملف Word")
    letter_type: Optional[str] = Field("general", description="نوع الخطاب (guarantee, handover, general)")
    template_path: Optional[str] = Field(None, description="مسار اختياري لقالب مخصص")

@mcp.tool()
async def generate_official_letter(recipient_title: str, subject: str, letter_content: str, project_name: str, output_path: str, letter_type: str = "general", template_path: Optional[str] = None) -> str:
    """
    توليد خطاب رسمي معتمد بنمط صفحة واحدة A4 مع ضبط RTL وتنسيق الترويسة والتذييل.
    """
    res_path = await asyncio.to_thread(
        build_official_letter_docx,
        recipient_title,
        subject,
        letter_content,
        project_name,
        output_path,
        letter_type,
        template_path
    )
    return f"تم بنجاح توليد الخطاب الرسمي: {res_path}"


# -------------------------------------------------------------
# 5. أداة طلب فحص الأعمال WIR (generate_inspection_request_wir)
# -------------------------------------------------------------
class GenerateWirRequest(BaseModel):
    project_name: str = Field(..., description="اسم المشروع")
    wir_number: str = Field(..., description="رقم طلب الفحص مثل WIR-EL-001")
    discipline: str = Field(..., description="التخصص (كهرباء، مدني، ميكانيكا، معماري)")
    location: str = Field(..., description="موقع الفحص بالتفصيل داخل المشروع")
    description: str = Field(..., description="وصف الأعمال الخاضعة للاستلام")
    inspection_date: str = Field(..., description="تاريخ الفحص المطلوب")
    output_path: str = Field(..., description="المسار المطلق لحفظ الملف")
    submittal_ref: Optional[str] = Field(None, description="رقم اعتماد المواد أو المخططات ذات الصلة")
    template_path: Optional[str] = Field(None, description="مسار اختياري لقالب مخصص")

@mcp.tool()
async def generate_wir_request(project_name: str, wir_number: str, discipline: str, location: str, description: str, inspection_date: str, output_path: str, submittal_ref: Optional[str] = None, template_path: Optional[str] = None) -> str:
    """
    توليد نموذج طلب فحص أعمال (Work Inspection Request - WIR) منسق بصيغة DOCX.
    """
    res_path = await asyncio.to_thread(
        build_inspection_request_wir,
        project_name,
        wir_number,
        discipline,
        location,
        description,
        inspection_date,
        output_path,
        submittal_ref,
        template_path
    )
    return f"تم بنجاح توليد نموذج WIR: {res_path}"


# -------------------------------------------------------------
# 6. أداة نماذج تسليم الصندوق TBC (generate_tbc_handover_forms)
# -------------------------------------------------------------
class GenerateHandoverFormsRequest(BaseModel):
    project_info: Dict[str, Any] = Field(..., description="معلومات المشروع (الاسم، رقم العقد، الاستشاري، التاريخ)")
    output_dir: str = Field(..., description="مجلد حفظ النماذج المولدة")
    template_box_label: Optional[str] = Field(None, description="مسار قالب بطاقة الصندوق")
    template_checklist_r09: Optional[str] = Field(None, description="مسار قالب قائمة الفحص Form R09")

@mcp.tool()
async def generate_tbc_handover_forms(project_info: Dict[str, Any], output_dir: str, template_box_label: Optional[str] = None, template_checklist_r09: Optional[str] = None) -> str:
    """
    توليد وتعبئة نماذج استلام وتسليم الصندوق TBC (Box Label & Handover Checklist).
    """
    out_dir = Path(output_dir).resolve()
    out_dir.mkdir(parents=True, exist_ok=True)

    box_label_out = str(out_dir / "نموذج_تسمية_الصندوق.docx")
    checklist_out = str(out_dir / "HANDOVER_CHECKLIST_FORM_R09.docx")

    await asyncio.to_thread(generate_tbc_box_label, project_info, box_label_out, template_box_label)
    await asyncio.to_thread(generate_handover_checklist_r09, project_info, [], checklist_out, template_checklist_r09)

    return f"تم بنجاح توليد نماذج التسليم في: {out_dir}\n1. بطاقة الصندوق: {box_label_out}\n2. قائمة الفحص R09: {checklist_out}"


# -------------------------------------------------------------
# 7. أداة التحويل إلى PDF (export_to_pdf)
# -------------------------------------------------------------
class ExportToPdfRequest(BaseModel):
    input_file_path: str = Field(..., description="المسار المطلق لملف DOCX أو XLSX")
    output_pdf_path: Optional[str] = Field(None, description="مسار ملف PDF اختياري")

@mcp.tool()
async def export_to_pdf(input_file_path: str, output_pdf_path: Optional[str] = None) -> str:
    """
    تحويل أي ملف Word (DOCX) أو Excel (XLSX) إلى PDF بجودة طباعة عالية وخطوط عربية مدمجة.
    """
    in_ext = Path(input_file_path).suffix.lower()
    if in_ext in [".docx", ".doc"]:
        pdf_path = await asyncio.to_thread(convert_docx_to_pdf, input_file_path, output_pdf_path)
    elif in_ext in [".xlsx", ".xls"]:
        pdf_path = await asyncio.to_thread(convert_xlsx_to_pdf, input_file_path, output_pdf_path)
    else:
        raise ValueError(f"امتداد الملف غير مدعوم: {in_ext}. الصيغ المدعومة هي DOCX و XLSX فقط.")

    return f"تم بنجاح تصدير الملف إلى PDF: {pdf_path}"


if __name__ == "__main__":
    mcp.run()
