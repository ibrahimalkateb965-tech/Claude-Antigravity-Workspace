#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Prompt Library Curator & Sync Engine
Author: Antigravity Multi-Agent Core
Description: Programmatically formats, categorizes, and appends engineered prompts 
into 'مكتبة الأوامر.xlsx' with precise cell styling, fonts, borders, and auto-row detection.
"""

import os
import sys
import json
import argparse
import openpyxl
from openpyxl.styles import Font, Alignment, Border, Side, PatternFill

if hasattr(sys.stdout, 'reconfigure'):
    try:
        sys.stdout.reconfigure(encoding='utf-8')
    except Exception:
        pass

DEFAULT_EXCEL_PATH = r"G:\أرشيف_ونسخ_احتياطية\ملفات_مكررة_وعامة\AI\مكتبة الأوامر.xlsx"

# Standard Cell Styles matching 'مكتبة الأوامر.xlsx'
THIN_SIDE = Side(border_style="thin", color="000000")
CELL_BORDER = Border(left=THIN_SIDE, right=THIN_SIDE, top=THIN_SIDE, bottom=THIN_SIDE)

FONT_NO = Font(name="Segoe UI", size=10, bold=False, color="000000")
FONT_NAME = Font(name="Segoe UI", size=10, bold=True, color="000000")
FONT_PROMPT = Font(name="Consolas", size=10, bold=True, color="000000")
FONT_DESC = Font(name="Segoe UI", size=10, bold=False, color="000000")

ALIGN_NO = Alignment(horizontal="center", vertical="center", wrap_text=True)
ALIGN_NAME = Alignment(horizontal="right", vertical="center", wrap_text=True)
ALIGN_PROMPT = Alignment(horizontal="left", vertical="center", wrap_text=True)
ALIGN_DESC = Alignment(horizontal="right", vertical="center", wrap_text=True)

# Sheet keywords for intelligent routing
SHEET_ROUTING = {
    "Gates_Hooks.Antigravity": ["teamwork-preview", "teamwork", "victory auditor", "أنتي جرافيتي فحص", "بوابة انتي جرافيتي"],
    "CLI_Core.Antigravity": ["agcli", "antigravity", "gemini", "جيميناي", "أنتي جرافيتي"],
    "Workflows_Pipelines.Antigravity": ["antigravity workflow", "سير عمل أنتي جرافيتي"],
    "CLI_Core.Claude": ["cli", "claude", "fleet", "clear", "compact", "context", "orchestrator", "commander", "advisor", "كلود"],
    "Workflows_Pipelines.Claude": ["workflow", "pipeline", "ci", "cd", "testflight", "release", "github actions", "build", "automation"],
    "Gates_Hooks.Claude": ["hook", "gate", "quality gate", "خطاف", "بوابة", "audit", "مراجعة", "فحص"],
    "Custom_Skills.Claude": ["skill", "مهارة", "agent", "وكيل", "subagent", "role", "تخصيص"],
    "Config_Environments.Claude": ["config", "environment", "settings", "بيئة", "إعدادات", "gradle", "sdk", "path", "memory", "ram"],
    "Construction_MCP_Suite": ["boq", "مخطط", "عقد", "مقاولات", "هندسة", "كميات", "tbc", "مستخلص", "docx", "إنشاءات"]
}

def detect_best_sheet(name, prompt, desc, available_sheets):
    text = f"{name} {prompt} {desc}".lower()
    scores = {}
    for sheet, keywords in SHEET_ROUTING.items():
        if sheet in available_sheets:
            score = sum(1 for kw in keywords if kw in text)
            scores[sheet] = score
            
    best_sheet = max(scores, key=scores.get)
    if scores[best_sheet] > 0:
        return best_sheet
    return "CLI_Core.Claude" if "CLI_Core.Claude" in available_sheets else "prompts"

def add_prompt_to_library(name, prompt, desc="", sheet_name=None, excel_path=None):
    excel_path = excel_path or DEFAULT_EXCEL_PATH
    if not os.path.exists(excel_path):
        raise FileNotFoundError(f"Excel file not found at: {excel_path}")

    # Open workbook
    wb = openpyxl.load_workbook(excel_path)
    
    if not sheet_name or sheet_name not in wb.sheetnames:
        sheet_name = detect_best_sheet(name, prompt, desc, wb.sheetnames)
        
    ws = wb[sheet_name]

    # Find the next clean row
    # Scan from bottom to find last populated row in Col B or Col C
    target_row = 1
    max_no = 0
    for r in range(ws.max_row, 0, -1):
        v_b = ws.cell(r, 2).value
        v_c = ws.cell(r, 3).value
        v_no = ws.cell(r, 1).value
        if v_b or v_c:
            target_row = r + 1
            break

    # Determine next 'No'
    for r in range(1, target_row):
        val = ws.cell(r, 1).value
        if isinstance(val, int):
            if val > max_no:
                max_no = val
        elif isinstance(val, str) and val.strip().isdigit():
            num = int(val.strip())
            if num > max_no:
                max_no = num

    next_no = max_no + 1

    # Insert cells
    c_no = ws.cell(row=target_row, column=1, value=next_no)
    c_name = ws.cell(row=target_row, column=2, value=name)
    c_prompt = ws.cell(row=target_row, column=3, value=prompt)
    c_desc = ws.cell(row=target_row, column=4, value=desc if desc else None)

    # Apply precise styling
    c_no.font = FONT_NO
    c_no.alignment = ALIGN_NO
    c_no.border = CELL_BORDER

    c_name.font = FONT_NAME
    c_name.alignment = ALIGN_NAME
    c_name.border = CELL_BORDER

    c_prompt.font = FONT_PROMPT
    c_prompt.alignment = ALIGN_PROMPT
    c_prompt.border = CELL_BORDER

    c_desc.font = FONT_DESC
    c_desc.alignment = ALIGN_DESC
    c_desc.border = CELL_BORDER

    # Set row height if needed
    ws.row_dimensions[target_row].height = 45

    # Attempt save with fallback
    try:
        wb.save(excel_path)
    except PermissionError:
        # File is locked in Excel - try COM automation
        try:
            import win32com.client
            xl = win32com.client.GetObject(Class="Excel.Application")
            target_wb = None
            for w in xl.Workbooks:
                if "مكتبة الأوامر" in w.Name:
                    target_wb = w
                    break
            if target_wb:
                target_sheet = target_wb.Sheets(sheet_name)
                target_sheet.Cells(target_row, 1).Value = next_no
                target_sheet.Cells(target_row, 2).Value = name
                target_sheet.Cells(target_row, 3).Value = prompt
                target_sheet.Cells(target_row, 4).Value = desc if desc else ""
                target_wb.Save()
            else:
                raise PermissionError("File locked by Excel and workbook not found in running instances.")
        except Exception as e:
            raise PermissionError(f"Cannot save Excel file because it is open in Microsoft Excel. Please save/close it and retry. Error: {e}")

    # Auto Dual-Sync to Interactive Web App
    synced_targets = sync_to_web_apps(excel_path=excel_path, target_sheet=sheet_name)

    return {
        "status": "success",
        "excel_path": excel_path,
        "sheet_name": sheet_name,
        "row_number": target_row,
        "no": next_no,
        "name": name,
        "prompt": prompt,
        "desc": desc,
        "synced_web_apps": synced_targets
    }

def sync_to_web_apps(excel_path=DEFAULT_EXCEL_PATH, target_sheet=None):
    """
    Syncs the full workbook sheets from Excel into the interactive HTML prompt library index.html files.
    """
    if not os.path.exists(excel_path):
        return []

    try:
        wb = openpyxl.load_workbook(excel_path, data_only=True)
    except Exception:
        return []

    library_data = {}

    for sheet_name in wb.sheetnames:
        ws = wb[sheet_name]
        if ws.max_row < 2:
            continue

        rows_data = []
        for r in range(1, ws.max_row + 1):
            row_vals = [ws.cell(r, c).value for c in range(1, ws.max_column + 1)]
            if any(v is not None and str(v).strip() != "" for v in row_vals):
                rows_data.append(row_vals)

        if len(rows_data) < 2:
            continue

        headers = [str(h) if h is not None else "" for h in rows_data[0]]
        colNo = next((i for i, h in enumerate(headers) if 'م' in h or 'no' in h.lower()), 0)
        colName = next((i for i, h in enumerate(headers) if 'اسم' in h or 'name' in h.lower()), 1 if len(headers) > 1 else 0)
        colPrompt = next((i for i, h in enumerate(headers) if 'محفز' in h or 'prompt' in h.lower() or 'أمر' in h), 2 if len(headers) > 2 else 0)
        colNotes = next((i for i, h in enumerate(headers) if 'نموذج' in h or 'note' in h.lower() or 'وصف' in h or 'desc' in h.lower()), 3 if len(headers) > 3 else 0)
        colAgents = next((i for i, h in enumerate(headers) if 'وكلاء' in h or 'agent' in h.lower()), -1)
        colGoal = next((i for i, h in enumerate(headers) if 'هدف' in h or 'goal' in h.lower()), -1)
        colDetails = next((i for i, h in enumerate(headers) if 'تفاصيل' in h or 'detail' in h.lower()), -1)

        sheet_rows = []
        for r_idx, row in enumerate(rows_data[1:], start=1):
            def get_val(idx):
                if idx is not None and idx >= 0 and idx < len(row):
                    v = row[idx]
                    return str(v).strip() if v is not None else ""
                return ""
            no_val = get_val(colNo)
            name_val = get_val(colName)
            prompt_val = get_val(colPrompt)
            notes_val = get_val(colNotes)
            agents_val = get_val(colAgents)
            goal_val = get_val(colGoal)
            details_val = get_val(colDetails)

            if not prompt_val and not name_val:
                continue

            sheet_rows.append({
                "No": int(no_val) if no_val.isdigit() else (no_val or r_idx),
                "Name": name_val or f"أمر #{r_idx}",
                "Prompt": prompt_val,
                "Notes": notes_val,
                "Agents": agents_val,
                "Goal": goal_val,
                "Details": details_val
            })

        if sheet_rows:
            library_data[sheet_name.strip()] = sheet_rows

    json_str = json.dumps(library_data, ensure_ascii=False, indent=4)
    active_tab_val = f'"{target_sheet}"' if target_sheet and target_sheet in library_data else '"الكل (All)"'

    html_targets = [
        r"C:\Users\Kt\.gemini\config\tools\prompt_library\index.html",
        r"F:\AI PROJECTS\Claude+Antigravity\03_Dynamic_Prompt_Library\index.html",
        r"F:\AI PROJECTS\Quran_Records\with antigravity\New Crew\03_Dynamic_Prompt_Library\index.html",
        r"F:\AI PROJECTS\Claude+Antigravity -production\03_Dynamic_Prompt_Library\index.html"
    ]

    # Google Drive (Autovem Cloud Suite) Dynamic Integration
    gdrive_candidates = [
        r"H:\My Drive\Autovem\03_Dynamic_Prompt_Library\index.html",
        r"I:\My Drive\Autovem\03_Dynamic_Prompt_Library\index.html",
        os.path.expandvars(r"%USERPROFILE%\Google Drive\Autovem\03_Dynamic_Prompt_Library\index.html")
    ]
    for g_cand in gdrive_candidates:
        g_drive_root = os.path.dirname(os.path.dirname(g_cand))
        if os.path.exists(g_drive_root) and g_cand not in html_targets:
            html_targets.append(g_cand)

    master_template = r"F:\AI PROJECTS\Claude+Antigravity\03_Dynamic_Prompt_Library\index.html"
    synced_targets = []
    for html_path in html_targets:
        if not os.path.exists(html_path):
            if os.path.exists(master_template) and html_path != master_template:
                try:
                    os.makedirs(os.path.dirname(html_path), exist_ok=True)
                    import shutil
                    shutil.copy2(master_template, html_path)
                except Exception:
                    continue
            else:
                continue
        try:
            with open(html_path, 'r', encoding='utf-8') as f:
                content = f.read()

            start_marker = "let libraryData = {"
            start_idx = content.find(start_marker)
            if start_idx != -1:
                end_marker = "let activeTab ="
                end_idx = content.find(end_marker, start_idx)
                if end_idx != -1:
                    new_content = content[:start_idx] + f"let libraryData = {json_str};\n        let activeTab = {active_tab_val};\n"
                    semicolon_idx = content.find(";", end_idx)
                    if semicolon_idx != -1:
                        new_content += content[semicolon_idx + 1:]
                    else:
                        new_content += content[end_idx:]

                    with open(html_path, 'w', encoding='utf-8') as f:
                        f.write(new_content)
                    synced_targets.append(html_path)
        except Exception:
            pass

    return synced_targets

def main():
    parser = argparse.ArgumentParser(description="Prompt Library Curator & Sync Engine")
    parser.add_argument("--name", "-n", default=None, help="Command / Prompt Name (Arabic + English)")
    parser.add_argument("--prompt", "-p", default=None, help="The prompt text or command trigger")
    parser.add_argument("--desc", "-d", default="", help="Description & Usage Notes")
    parser.add_argument("--sheet", "-s", default=None, help="Target Sheet / Tab name")
    parser.add_argument("--file", "-f", default=DEFAULT_EXCEL_PATH, help="Path to Excel workbook")
    parser.add_argument("--list-sheets", action="store_true", help="List all available sheets in workbook")
    parser.add_argument("--sync-only", action="store_true", help="Sync existing Excel workbook to web apps without adding new prompt")
    parser.add_argument("--json", action="store_true", help="Output result as JSON")
    args = parser.parse_args()

    try:
        excel_path = args.file or DEFAULT_EXCEL_PATH
        if args.sync_only:
            synced = sync_to_web_apps(excel_path=excel_path, target_sheet=args.sheet)
            if args.json:
                print(json.dumps({"status": "success", "synced_web_apps": synced}, ensure_ascii=False, indent=2))
            else:
                print(f"🌐 Successfully synced Excel library to {len(synced)} interactive web app interfaces!")
                for t in synced:
                    print(f"   ✓ {t}")
            return

        if args.list_sheets:
            if not os.path.exists(excel_path):
                raise FileNotFoundError(f"Excel file not found at: {excel_path}")
            wb = openpyxl.load_workbook(excel_path, read_only=True)
            sheets = wb.sheetnames
            if args.json:
                print(json.dumps({"sheets": sheets}, ensure_ascii=False, indent=2))
            else:
                print(f"📑 Available Sheets in '{os.path.basename(excel_path)}' ({len(sheets)} sheets):")
                for i, s in enumerate(sheets, 1):
                    print(f"  {i}. {s}")
            return

        if not args.name or not args.prompt:
            parser.error("the following arguments are required: --name/-n, --prompt/-p (unless using --list-sheets or --sync-only)")

        res = add_prompt_to_library(
            name=args.name,
            prompt=args.prompt,
            desc=args.desc,
            sheet_name=args.sheet,
            excel_path=args.file
        )
        if args.json:
            print(json.dumps(res, ensure_ascii=False, indent=2))
        else:
            print(f"✅ Successfully added to Prompts Library!")
            print(f"📑 Sheet: {res['sheet_name']}")
            print(f"📍 Location: Row #{res['row_number']} (No: {res['no']})")
            print(f"🏷️ Name: {res['name']}")
            if res.get("synced_web_apps"):
                print(f"🌐 Dual-Sync: Updated {len(res['synced_web_apps'])} interactive web app pages automatically!")
    except Exception as e:
        if args.json:
            print(json.dumps({"status": "error", "error": str(e)}, ensure_ascii=False))
        else:
            print(f"❌ Error: {e}", file=sys.stderr)
        sys.exit(1)

if __name__ == "__main__":
    main()
