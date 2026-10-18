#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
محرك التحديث الآلي لصفحات تطبيقات الويب لمكتبة الخطافات والأوامر الذكية
(Dynamic Hooks & Prompt Library Synchronizer)
Author: Antigravity Multi-Agent Fleet Core
Description: يقرأ ملف HOOKS_GUIDE.md المعتمد ويستخرج بيانات كافة الخطافات ويحقنها برمجياً
في مصفوفة libraryData داخل كافة صفحات index.html لتطبيقي Hooks Library و Prompt Library
سواء في المستودع الرئيسي أو النسخ الإنتاجية أو أدوات النظام العالمية عبر Windows و WSL.
"""

import os
import sys
import re
import json

# فرض ترميز UTF-8 لمخرجات الكونسول في ويندوز
if hasattr(sys.stdout, 'reconfigure'):
    try:
        sys.stdout.reconfigure(encoding='utf-8')
    except Exception:
        pass


def resolve_path(p):
    """تحويل المسارات تلقائياً بين نظامي Windows و WSL لضمان العمل العابر للبيئات."""
    if os.path.exists(p):
        return p
    if os.name != 'nt':
        # التحويل من Windows إلى WSL
        m = re.match(r'^([a-zA-Z]):[\\/](.*)$', p)
        if m:
            drive = m.group(1).lower()
            rest = m.group(2).replace('\\', '/')
            wsl_p = f"/mnt/{drive}/{rest}"
            if os.path.exists(wsl_p):
                return wsl_p
    else:
        # التحويل من WSL إلى Windows
        m = re.match(r'^/mnt/([a-zA-Z])/(.*)$', p)
        if m:
            drive = m.group(1).upper()
            rest = m.group(2).replace('/', '\\')
            win_p = f"{drive}:\\{rest}"
            if os.path.exists(win_p):
                return win_p
    return p


def update_prompt_library():
    print("====================================================================")
    print("🌐 بدء التحديث الآلي لصفحات تطبيقات الويب للخطافات والأوامر (HTML Sync Engine)")
    print("====================================================================")

    # 1. تحديد ملف HOOKS_GUIDE.md المعتمد
    base_dir = os.path.dirname(os.path.abspath(__file__))
    md_paths = [
        "/home/t/.gemini/config/HOOKS_GUIDE.md",
        r"C:\Users\Kt\.gemini\config\HOOKS_GUIDE.md",
        os.path.join(base_dir, "HOOKS_GUIDE.md"),
        r"F:\AI PROJECTS\Claude+Antigravity\HOOKS_GUIDE.md",
        r"F:\AI PROJECTS\Autovemtech\HOOKS_GUIDE.md",
        r"F:\AI PROJECTS\Blind App\.agents\HOOKS_GUIDE.md"
    ]

    md_path = None
    for p in md_paths:
        resolved = resolve_path(p)
        if os.path.exists(resolved):
            md_path = resolved
            break

    if not md_path:
        print("❌ خطأ: لم يتم العثور على ملف HOOKS_GUIDE.md في أي من المسارات المعتمدة.")
        return

    print(f"📖 قراءة دليل الخطافات من: {md_path}")

    # 2. حصر كافة صفحات HTML المستهدفة (مكتبة الخطافات ومكتبة الأوامر)
    html_targets = [
        # تطبيقات مكتبة الخطافات (Dynamic Hooks Library)
        r"F:\AI PROJECTS\Claude+Antigravity\03_Dynamic_Hooks_Library\index.html",
        r"F:\AI PROJECTS\Claude+Antigravity -production\03_Dynamic_Hooks_Library\index.html",
        r"C:\Users\Kt\.gemini\config\tools\hooks_library\index.html",
        r"/home/t/.gemini/config/tools/hooks_library/index.html",
        r"F:\AI PROJECTS\Quran_Records\with antigravity\New Crew\03_Dynamic_Hooks_Library\index.html",

        # تطبيقات مكتبة الأوامر (Dynamic Prompt Library)
        r"F:\AI PROJECTS\Claude+Antigravity\03_Dynamic_Prompt_Library\index.html",
        r"F:\AI PROJECTS\Claude+Antigravity -production\03_Dynamic_Prompt_Library\index.html",
        r"C:\Users\Kt\.gemini\config\tools\prompt_library\index.html",
        r"/home/t/.gemini/config/tools/prompt_library/index.html",
        r"F:\AI PROJECTS\Quran_Records\with antigravity\New Crew\03_Dynamic_Prompt_Library\index.html"
    ]

    # اكتشاف ديناميكي لأي صفحات إضافية عبر مسار المشاريع الرئيسي
    search_roots = [r"F:\AI PROJECTS", "/mnt/f/AI PROJECTS"]
    for s_root in search_roots:
        if os.path.exists(s_root):
            try:
                for item in os.listdir(s_root):
                    proj_dir = os.path.join(s_root, item)
                    if os.path.isdir(proj_dir):
                        for lib_dir in ["03_Dynamic_Hooks_Library", "03_Dynamic_Prompt_Library"]:
                            cand = os.path.join(proj_dir, lib_dir, "index.html")
                            if os.path.exists(cand) and cand not in html_targets:
                                html_targets.append(cand)
            except Exception:
                pass

    with open(md_path, 'r', encoding='utf-8') as f:
        content = f.read()

    # 3. تحليل جدول الخطافات
    hooks_data = []
    for line in content.split('\n'):
        if line.strip().startswith('| **'):
            parts = [p.strip() for p in line.split('|')[1:-1]]
            if len(parts) >= 5:
                name = parts[0].replace('**', '')
                agents = parts[1].replace('`', '')
                model_level = parts[2].replace('<br>', '\n')
                triggers = parts[3].replace('"', '').replace('`', '')
                goal = parts[4]

                m = re.match(r'(\d+)\.', name)
                idx = int(m.group(1)) if m else len(hooks_data) + 1

                hooks_data.append({
                    'id': idx,
                    'name': name,
                    'agents': agents,
                    'model_level': model_level,
                    'triggers': triggers,
                    'goal': goal
                })

    # 4. تحليل كتل التفاصيل لكل خطاف
    detail_blocks = re.findall(r'###\s*(\d+)\.\s*(.*?)\n(.*?)(?=(?:###\s*\d+|\Z))', content, re.DOTALL)
    details_dict = {}
    for num_str, title, block_text in detail_blocks:
        num = int(num_str)
        details_dict[num] = block_text.strip()

    # 5. بناء كائن libraryData المتوافق مع JavaScript
    libraryData = {
        'الملخص العام': []
    }

    for hook in hooks_data:
        detail_text = details_dict.get(hook['id'], '')

        obj = {
            'No': hook['id'],
            'Name': hook['name'],
            'Prompt': hook['triggers'],
            'Notes': hook['model_level'],
            'Agents': hook['agents'],
            'Goal': hook['goal'],
            'Details': detail_text
        }

        libraryData['الملخص العام'].append(obj)

        clean_name = re.sub(r'^\d+\.\s*', '', hook['name'])
        if len(clean_name) > 25:
            clean_name = clean_name[:25]
        sheet_title = f"{hook['id']}. {clean_name}"

        libraryData[sheet_title] = [obj]

    json_str = json.dumps(libraryData, ensure_ascii=False, indent=4)

    # 6. حقن البيانات في ملفات HTML المستهدفة
    updated_count = 0
    seen_targets = set()
    for raw_path in html_targets:
        resolved = resolve_path(raw_path)
        if not os.path.exists(resolved) or resolved in seen_targets:
            continue
        seen_targets.add(resolved)

        try:
            with open(resolved, 'r', encoding='utf-8') as f:
                html_content = f.read()

            start_marker = "let libraryData = {"
            start_idx = html_content.find(start_marker)
            if start_idx != -1:
                end_marker = "let activeTab ="
                end_idx = html_content.find(end_marker, start_idx)
                if end_idx != -1:
                    new_html = html_content[:start_idx] + f"let libraryData = {json_str};\n        " + html_content[end_idx:]
                    with open(resolved, 'w', encoding='utf-8') as f:
                        f.write(new_html)
                    print(f"✅ تم تحديث تطبيق الويب بنجاح: {resolved}")
                    updated_count += 1
                else:
                    print(f"⚠️ تحذير: لم يتم العثور على نهاية المحدد (end_marker) في: {resolved}")
            else:
                print(f"⚠️ تحذير: لم يتم العثور على بداية المحدد (start_marker) في: {resolved}")
        except Exception as e:
            print(f"❌ خطأ أثناء معالجة الملف {resolved}: {e}")

    print("--------------------------------------------------------------------")
    print(f"🎉 اكتمل تحديث كافة صفحات HTML بنجاح! تم تحديث {updated_count} واجهة تفاعلية ({len(hooks_data)} خطاف).")
    print("====================================================================")


if __name__ == "__main__":
    update_prompt_library()
