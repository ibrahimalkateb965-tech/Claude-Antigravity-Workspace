#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Generator for Antigravity_Context_Management_Guide.docx and .pdf
Produces a production-grade, beautifully formatted strict RTL Arabic document.
Enforces w:bidi on all paragraphs, w:rtl on all runs, and w:bidiVisual on all tables.
"""

import os
import sys
import docx
from docx.shared import Inches, Pt, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT
from docx.oxml import parse_xml
from docx.oxml.ns import nsdecls

if hasattr(sys.stdout, 'reconfigure'):
    sys.stdout.reconfigure(encoding='utf-8')

def set_cell_background(cell, fill_hex):
    tcPr = cell._tc.get_or_add_tcPr()
    tcPr.append(parse_xml(f'<w:shd {nsdecls("w")} w:fill="{fill_hex}"/>'))

def set_cell_margins(cell, top=140, bottom=140, left=180, right=180):
    tcPr = cell._tc.get_or_add_tcPr()
    tcMar = parse_xml(f'<w:tcMar {nsdecls("w")}><w:top w:w="{top}" w:type="dxa"/><w:bottom w:w="{bottom}" w:type="dxa"/><w:left w:w="{left}" w:type="dxa"/><w:right w:w="{right}" w:type="dxa"/></w:tcMar>')
    tcPr.append(tcMar)

def set_table_rtl(table):
    tblPr = table._tbl.tblPr
    tblPr.append(parse_xml(f'<w:bidiVisual {nsdecls("w")} w:val="1"/>'))

def apply_p_rtl(p, align=WD_ALIGN_PARAGRAPH.RIGHT):
    pPr = p._p.get_or_add_pPr()
    pPr.append(parse_xml(f'<w:bidi {nsdecls("w")} w:val="1"/>'))
    p.alignment = align

def apply_run_rtl(run, font_name='Arial', size_pt=10.5, bold=False, color=None):
    run.font.name = font_name
    run.font.size = Pt(size_pt)
    run.bold = bold
    if color:
        run.font.color.rgb = color
        
    rPr = run._r.get_or_add_rPr()
    sz_half_pts = int(size_pt * 2)
    rPr.append(parse_xml(f'<w:rFonts {nsdecls("w")} w:ascii="{font_name}" w:hAnsi="{font_name}" w:cs="{font_name}"/>'))
    rPr.append(parse_xml(f'<w:szCs {nsdecls("w")} w:val="{sz_half_pts}"/>'))
    rPr.append(parse_xml(f'<w:rtl {nsdecls("w")} w:val="1"/>'))

def add_styled_heading(doc, text, level=1):
    p = doc.add_paragraph()
    apply_p_rtl(p, align=WD_ALIGN_PARAGRAPH.RIGHT)
    p.paragraph_format.space_before = Pt(14 if level == 1 else 10)
    p.paragraph_format.space_after = Pt(4)
    p.paragraph_format.keep_with_next = True
    
    run = p.add_run(text)
    if level == 1:
        apply_run_rtl(run, font_name='Arial', size_pt=17, bold=True, color=RGBColor(0x0F, 0x17, 0x2A))
    elif level == 2:
        apply_run_rtl(run, font_name='Arial', size_pt=14, bold=True, color=RGBColor(0x1E, 0x3A, 0x8A))
    elif level == 3:
        apply_run_rtl(run, font_name='Arial', size_pt=12, bold=True, color=RGBColor(0x0E, 0x74, 0x90))
    return p

def add_body_p(doc, text="", bold_prefix=None, space_after=5):
    p = doc.add_paragraph()
    apply_p_rtl(p, align=WD_ALIGN_PARAGRAPH.RIGHT)
    p.paragraph_format.space_after = Pt(space_after)
    p.paragraph_format.line_spacing = 1.2
    
    if bold_prefix:
        r_pre = p.add_run(bold_prefix)
        apply_run_rtl(r_pre, font_name='Arial', size_pt=10.5, bold=True, color=RGBColor(0x1F, 0x29, 0x37))
        
    if text:
        r_body = p.add_run(text)
        apply_run_rtl(r_body, font_name='Arial', size_pt=10.5, bold=False, color=RGBColor(0x37, 0x41, 0x51))
    return p

def add_callout_box(doc, text, title=None, border_hex="1E3A8A", bg_hex="F0F4F8"):
    tbl = doc.add_table(rows=1, cols=1)
    tbl.alignment = WD_TABLE_ALIGNMENT.CENTER
    set_table_rtl(tbl)
    cell = tbl.cell(0, 0)
    set_cell_background(cell, bg_hex)
    set_cell_margins(cell, top=160, bottom=160, left=200, right=200)
    
    tcPr = cell._tc.get_or_add_tcPr()
    tcBorders = parse_xml(
        f'<w:tcBorders {nsdecls("w")}>'
        f'<w:top w:val="none"/>'
        f'<w:left w:val="none"/>'
        f'<w:bottom w:val="none"/>'
        f'<w:right w:val="single" w:sz="36" w:space="0" w:color="{border_hex}"/>'
        f'</w:tcBorders>'
    )
    tcPr.append(tcBorders)
    
    p = cell.paragraphs[0]
    apply_p_rtl(p, align=WD_ALIGN_PARAGRAPH.RIGHT)
    p.paragraph_format.space_after = Pt(2)
    p.paragraph_format.line_spacing = 1.2
    
    if title:
        rt = p.add_run(title + "\n")
        apply_run_rtl(rt, font_name='Arial', size_pt=11, bold=True, color=RGBColor(0x1E, 0x3A, 0x8A))
        
    rb = p.add_run(text)
    apply_run_rtl(rb, font_name='Arial', size_pt=10, bold=False, color=RGBColor(0x1F, 0x29, 0x37))
    
    p_after = doc.add_paragraph()
    apply_p_rtl(p_after)
    p_after.paragraph_format.space_after = Pt(4)

def build_docx(output_path):
    doc = docx.Document()
    
    for s in doc.sections:
        s.top_margin = Inches(0.8)
        s.bottom_margin = Inches(0.8)
        s.left_margin = Inches(0.8)
        s.right_margin = Inches(0.8)
        
    # Title Header
    p_title = doc.add_paragraph()
    apply_p_rtl(p_title)
    p_title.paragraph_format.space_before = Pt(0)
    p_title.paragraph_format.space_after = Pt(2)
    r_t = p_title.add_run("الدليل الشامل لإدارة نافذة السياق وترشيد التوكنز في Antigravity")
    apply_run_rtl(r_t, font_name='Arial', size_pt=20, bold=True, color=RGBColor(0x0F, 0x17, 0x2A))
    
    p_sub = doc.add_paragraph()
    apply_p_rtl(p_sub)
    p_sub.paragraph_format.space_after = Pt(10)
    r_s = p_sub.add_run("الدليل العملي لحوكمة الجلسات، كبح تدهور التركيز، وخفض خط الأساس في بيئة Antigravity IDE و CLI")
    apply_run_rtl(r_s, font_name='Arial', size_pt=12, bold=False, color=RGBColor(0x25, 0x63, 0xEB))
    
    add_callout_box(
        doc,
        "يقدم هذا المستند استراتيجية هندسية متكاملة لإدارة نافذة السياق (Context Window) في Antigravity IDE والأداة الطرفية agcli، مع تصحيح المفاهيم الخاطئة حول 'أمان المليون توكن'، وتوضيح الركائز الأربع لحماية الذاكرة ومنع تشتت النماذج وتسريع الأداء البرمجي.",
        title="📌 الخلاصة التنفيذية والهدف المعماري",
        border_hex="2563EB",
        bg_hex="F8FAFC"
    )
    
    # Section 1
    add_styled_heading(doc, "أولاً: تشريح المشكلة — وهم الأمان في 'المليون توكن' (The 1M Illusion)", level=2)
    add_body_p(doc, "يعتمد محرك Antigravity على نموذج Gemini 3.8 Flash (High) بنافذة سياق ضخمة تتسع لـ 1,048,576 توكن (1M Tokens). هذه السعة الهائلة (5 أضعاف Claude Code) توفر مساحة رحبة، لكنها توقع المطورين في فخين معماريين خطيرين:")
    
    add_body_p(doc, "كلما تجاوزت الجلسة 400k إلى 500k توكن، تبدأ قدرة النموذج اللغوي على استرجاع التفاصيل الدقيقة بالانخفاض، مما يؤدي لنسيان القيود المعمارية الصارمة واللجوء لحلول سطحية وتكرار أخطاء برمجية سابقة.", bold_prefix="1. ظاهرة تدهور التركيز وضياع التفاصيل (Context Rot): ")
    add_body_p(doc, "يسجل المحرر كل خطوة واستدعاء أداة في قاعدة بيانات SQLite محلية (~/.gemini/antigravity-ide/conversations/*.db). انتفاخ الجلسة يضخم قاعدة البيانات إلى أكثر من 40 ميغابايت ويرفع استهلاك الذاكرة العشوائية إلى 2.5GB، مما يبطئ واجهة المحرر ويسبب أخطاء الذاكرة الافتراضية للويندوز (errno=1455).", bold_prefix="2. تضخم الذاكرة العشوائية للمحرر (RAM & SQLite Bloat): ")
    
    # Section 2
    add_styled_heading(doc, "ثانياً: الحقيقة الهندسية — مضاعف استدعاء الأدوات (Tool Call Multiplier)", level=2)
    add_body_p(doc, "الخطر الأكبر في إدارة السياق ليس الوصول لسقف المليون، بل التكلفة التراكمية في كل دورة عمل:")
    add_body_p(doc, "في كل خطوة تفكير أو استدعاء لأداة برمجية (Tool Call)، يعيد المحرك إرسال كامل سياق المحادثة السابق من البداية.", bold_prefix="• بنية استدعاء النماذج: ")
    add_body_p(doc, "إذا كانت جلستك منتفخة بـ 200,000 توكن، ونفذ الوكيل 15 أداة برمجية لإنجاز ميزة معينة، فإن إجمالي التوكنز المعالجة والمحسوبة على كوتا الحساب هو: 200,000 × 15 = 3 ملايين توكن تم حرقها في مهمة واحدة! بينما إبقاء الجلسة عند 80,000 توكن يستهلك 1.2M توكن فقط، محققاً وفراً قدره 1.8 مليون توكن.", bold_prefix="• الحسبة الرياضية الصادمة: ")
    
    # Section 3
    add_styled_heading(doc, "ثالثاً: الركيزة الأولى — خفض خط الأساس في Antigravity (The Baseline)", level=2)
    add_body_p(doc, "عند فتح أي جلسة جديدة، يبدأ مؤشر الاستهلاك فعلياً من 110,000 إلى 135,000 توكن (ما يعادل 11% إلى 13% من المليون) محجوزة للتعليمات الأساسية والأدوات والمهارات المحقونة. يوضح الجدول التالي المكونات المهدرة وكيفية خفضها:")
    
    # Table 1: Baseline Components
    table1 = doc.add_table(rows=5, cols=3)
    table1.alignment = WD_TABLE_ALIGNMENT.CENTER
    set_table_rtl(table1)
    
    headers = ["المكون المهدر", "الحجم التقريبي", "الإجراء والتوصية الهندسية"]
    col_widths = [Inches(2.2), Inches(1.3), Inches(3.3)]
    
    hdr_cells = table1.rows[0].cells
    for i, title in enumerate(headers):
        hdr_cells[i].text = title
        hdr_cells[i].width = col_widths[i]
        set_cell_background(hdr_cells[i], "1E3A8A")
        set_cell_margins(hdr_cells[i], top=120, bottom=120, left=140, right=140)
        p = hdr_cells[i].paragraphs[0]
        apply_p_rtl(p, align=WD_ALIGN_PARAGRAPH.RIGHT)
        for r in p.runs:
            apply_run_rtl(r, font_name='Arial', size_pt=10, bold=True, color=RGBColor(0xFF, 0xFF, 0xFF))
            
    t1_data = [
        ("سيرفرات MCP غير المستخدمة (مثل hostinger بـ 120+ أداة)", "25k - 35k توكن", "تعطيلها من ملف mcp_config.json وحصرها في بيئات الويب فقط."),
        ("المهارات الفائضة (حزم الكيمياء والأحياء وووردبريس)", "20k - 30k توكن", "تشغيل خطاف التخصيص الذكي (Hook 3) لحذف المهارات الزائدة تلقائياً."),
        ("قواعد الدستور المتضخمة داخل AGENTS.md", "15k - 20k توكن", "عزل الدروس المستفادة وحصرها في MEMORY_STORE.md اللامركزي."),
        ("ملفات السياق المحقون غير المفلترة", "10k - 15k توكن", "تنقيح ACTIVE_CONTEXT_INJECTION.md وأرشفة القيود المنتهية.")
    ]
    
    for row_idx, data in enumerate(t1_data, start=1):
        row_cells = table1.rows[row_idx].cells
        bg_color = "F9FAFB" if row_idx % 2 == 1 else "FFFFFF"
        for col_idx, text in enumerate(data):
            row_cells[col_idx].text = text
            row_cells[col_idx].width = col_widths[col_idx]
            set_cell_background(row_cells[col_idx], bg_color)
            set_cell_margins(row_cells[col_idx], top=100, bottom=100, left=120, right=120)
            p = row_cells[col_idx].paragraphs[0]
            apply_p_rtl(p, align=WD_ALIGN_PARAGRAPH.RIGHT)
            for r in p.runs:
                if col_idx == 1:
                    apply_run_rtl(r, font_name='Arial', size_pt=9.5, bold=True, color=RGBColor(0x0E, 0x74, 0x90))
                else:
                    apply_run_rtl(r, font_name='Arial', size_pt=9.5, bold=False, color=RGBColor(0x1F, 0x29, 0x37))

    p_space = doc.add_paragraph()
    apply_p_rtl(p_space)
    p_space.paragraph_format.space_after = Pt(4)
    add_body_p(doc, "ينخفض خط الأساس فوراً من ~135k إلى نحو 65k-70k توكن، موفراً أكثر من 65,000 توكن في كل دورة طوال اليوم!", bold_prefix="الأثر المباشر: ")

    # Section 4
    add_styled_heading(doc, "رابعاً: الركيزة الثانية — المقارنة المعمارية بين Antigravity IDE و CLI (agcli)", level=2)
    add_body_p(doc, "في إطار حوكمة أسطول الـ CLIs (Hook 22)، يمتلك Antigravity وجهين تشغيليين يجب الفصل التام بينهما:")
    
    # Table 2: IDE vs CLI
    table2 = doc.add_table(rows=6, cols=3)
    table2.alignment = WD_TABLE_ALIGNMENT.CENTER
    set_table_rtl(table2)
    
    t2_headers = ["وجه المقارنة", "Antigravity IDE (Workbench)", "Antigravity CLI (agcli run)"]
    t2_widths = [Inches(1.8), Inches(2.5), Inches(2.5)]
    
    for i, title in enumerate(t2_headers):
        cell = table2.rows[0].cells[i]
        cell.text = title
        cell.width = t2_widths[i]
        set_cell_background(cell, "0F766E")
        set_cell_margins(cell, top=120, bottom=120, left=140, right=140)
        p = cell.paragraphs[0]
        apply_p_rtl(p, align=WD_ALIGN_PARAGRAPH.RIGHT)
        for r in p.runs:
            apply_run_rtl(r, font_name='Arial', size_pt=10, bold=True, color=RGBColor(0xFF, 0xFF, 0xFF))
            
    t2_data = [
        ("طبيعة العمل", "بيئة التطوير الرسومية التفاعلية للمطور.", "المنفذ الطرفي الذاتي غير التفاعلي (Headless)."),
        ("الاستخدام الأمثل", "التخطيط المعماري ومراجعة الـ Diffs والـ Artifacts.", "بناء شاشات الـ UI، توليد الأكواد، وتحديث الذاكرة."),
        ("استهلاك الذاكرة", "مرتفع (2.0GB – 2.5GB RAM) بسبب Electron.", "منخفض جداً ومعزول ويحرر الذاكرة فور الانتهاء."),
        ("إدارة السياق", "محادثة ممتدة تتراكم في قاعدة بيانات SQLite.", "سياق مؤقت ينتهي مع انتهاء الأمر."),
        ("القاعدة المعمارية", "يُحظر توجيه أوامر الـ CLI الآلية إليها منعاً للنسخ اليدوي.", "المنفذ المعتمد للتفويض المباشر الذاتي من الأسطول.")
    ]
    
    for row_idx, data in enumerate(t2_data, start=1):
        bg = "F0FDFA" if row_idx % 2 == 1 else "FFFFFF"
        for col_idx, text in enumerate(data):
            c = table2.rows[row_idx].cells[col_idx]
            c.text = text
            c.width = t2_widths[col_idx]
            set_cell_background(c, bg)
            set_cell_margins(c, top=100, bottom=100, left=120, right=120)
            p = c.paragraphs[0]
            apply_p_rtl(p, align=WD_ALIGN_PARAGRAPH.RIGHT)
            for r in p.runs:
                apply_run_rtl(r, font_name='Arial', size_pt=9.5, bold=(col_idx == 0), color=RGBColor(0x1F, 0x29, 0x37))

    # Section 5
    add_styled_heading(doc, "خامساً: الركيزة الثالثة — عتبات الاستهلاك والتصفير في نافذة الـ 1M", level=2)
    add_body_p(doc, "بناءً على منحنى تركيز النماذج، يتم تقسيم نافذة المليون إلى أربعة نطاقات تشغيلية دقيقة:")
    
    add_body_p(doc, "أقصى درجات الذكاء والتركيز وانعدام تام للهلوسة. هذه هي المنطقة المثالية للتصميم الهيكلي وحل المعضلات المعمارية الكبرى.", bold_prefix="• 🟢 النطاق الأخضر (0 – 300k توكن / 0% – 30%): منطقة الأداء الفائق (Sweet Spot) — ")
    add_body_p(doc, "استقرار عالي في الأداء مع ضرورة الالتزام بالقراءة المقتضبة للملفات وتجنب طباعة سجلات البناء.", bold_prefix="• 🟡 النطاق الأصفر (300k – 600k توكن / 30% – 60%): منطقة الإنتاجية المستقرة — ")
    add_body_p(doc, "بداية تشتت التركيز. التوجيه الهندسي: إنهاء الميزة الحالية فوراً، وعمل Git Commit، وحفظ الذاكرة في MEMORY_STORE.md.", bold_prefix="• 🟠 النطاق البرتقالي (600k – 800k توكن / 60% – 80%): منطقة التحذير — ")
    add_body_p(doc, "يُحظر بدء أي ميزة جديدة. يتم تطبيق خطاف التصفير الاستراتيجي (Hook 25) وتجميد الحالة في CURRENT_STATE.md وبدء جلسة جديدة نظيفة.", bold_prefix="• 🔴 النطاق الأحمر (> 800k توكن / > 80%): منطقة الخطر والتصفير الإلزامي — ")
    
    # Section 6
    add_styled_heading(doc, "سادساً: الركيزة الرابعة — المراقبة اللحظية باستخدام أداة ag-context", level=2)
    add_body_p(doc, "تم ابتكار وبناء أداة طرفية متخصصة ومدمجة في النظام لقراءة استهلاك التوكنز لحظياً مباشرة من محرك Antigravity:")
    
    add_callout_box(
        doc,
        "1. عرض الحالة الفورية: ag-context\n"
        "2. شاشة المراقبة الحية في الوقت الفعلي (تحديث تلقائي كل 3 ثوانٍ): ag-context -w\n"
        "3. فحص تاريخ استهلاك التوكنز لآخر 5 خطوات: ag-context -H 5\n"
        "4. مخرجات JSON للتكامل مع السكربتات والوكلاء: ag-context -j",
        title="⚡ الأوامر التشغيلية اليومية لأداة ag-context",
        border_hex="059669",
        bg_hex="ECFDF5"
    )
    
    # Section 7
    add_styled_heading(doc, "سابعاً: الركيزة الخامسة — قواعد النظافة اليومية لكبح انتفاخ السياق", level=2)
    add_body_p(doc, "يُحظر استدعاء view_file لقراءة ملفات كاملة تتجاوز 100 سطر. يجب دائماً استخدام معاملي StartLine و EndLine لقراءة المقطع المستهدف فقط.", bold_prefix="1. القراءة المجزأة للملفات (Slice Notation): ")
    add_body_p(doc, "اعتماد replace_file_content لتعديل الأسطر المحددة وتجنب write_to_file لإعادة كتابة ملفات كاملة.", bold_prefix="2. الاستبدال الموضعي: ")
    add_body_p(doc, "مخرجات Gradle أو NPM تلتهم من 10k إلى 30k توكن عند طباعتها في الكونسول. القاعدة: توجيه المخرجات لملف خارجي (./gradlew ... > build.log) وقراءة سطور الخطأ فقط.", bold_prefix="3. حظر طباعة مخرجات البناء الخام: ")
    add_body_p(doc, "تجميع الاستفسارات والمهام في رسالة واحدة منسقة يوفر دورات كاملة من إعادة إرسال سياق المحادثة السابق.", bold_prefix="4. تجميع الطلبات (Batching): ")
    
    # Section 8
    add_styled_heading(doc, "ثامناً: الخلاصة التنفيذية وقائمة الإجراءات الفورية", level=2)
    add_body_p(doc, "1. تعطيل خوادم MCP الزائدة في mcp_config.json (يوفر 25k-35k توكن).")
    add_body_p(doc, "2. تشغيل خطاف التخصيص الذكي (Hook 3) لحذف المهارات غير المستخدمة (يوفر 20k-30k توكن).")
    add_body_p(doc, "3. المتابعة اليومية لنسبة الامتلاء عبر أمر ag-context.")
    add_body_p(doc, "4. توجيه مخرجات البناء للملفات لتفادي قفزات التوكنز المفاجئة.")
    add_body_p(doc, "5. التصفير الاستراتيجي عند 60% إلى 70% للبقاء دائماً في منطقة الأداء الفائق (Sweet Spot).")
    
    doc.save(output_path)
    print(f"DOCX successfully created at: {output_path}")

if __name__ == "__main__":
    out_docx = r"f:\AI PROJECTS\Blind App\Antigravity_Context_Management_Guide.docx"
    build_docx(out_docx)
