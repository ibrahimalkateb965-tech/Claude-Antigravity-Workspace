#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
محرك المزامنة والتصدير الشامل لأدوات الذكاء الاصطناعي للمستودع المركزي
Universal AI Tools & Ecosystem Export Engine

يقوم هذا المحرك بالتقاط وتصدير كافة تحديثات أدوات الذكاء الاصطناعي، الوكلاء، المهارات،
الذاكرة، وسياق الحقن من أي مشروع محلي (أو من كافة المشاريع) إلى المستودع المركزي:
F:\\AI PROJECTS\\Claude+Antigravity
"""

import os
import sys
import shutil
import subprocess
import argparse
import codecs
import re
from typing import List, Dict, Set

# فرض ترميز UTF-8 لمخرجات الكونسول في ويندوز
if hasattr(sys.stdout, 'encoding') and sys.stdout.encoding.lower() != 'utf-8':
    sys.stdout = codecs.getwriter('utf-8')(sys.stdout.detach())

# ------------------------------------------------------------------------------
# المسارات الثابتة
# ------------------------------------------------------------------------------
CENTRAL_WORKSPACE = r"F:\AI PROJECTS\Claude+Antigravity"
CENTRAL_AGENTS = os.path.join(CENTRAL_WORKSPACE, ".agents")
CENTRAL_SKILLS = os.path.join(CENTRAL_AGENTS, "skills")
CENTRAL_SUBAGENTS = os.path.join(CENTRAL_AGENTS, "Sub_Agent")
CENTRAL_PLUGINS = os.path.join(CENTRAL_WORKSPACE, "plugins")
CENTRAL_REFERENCES = os.path.join(CENTRAL_WORKSPACE, "references")
CENTRAL_TEMPLATES = os.path.join(CENTRAL_WORKSPACE, "fleet_templates")

GLOBAL_CONFIG_ROOT = r"C:\Users\Kt\.gemini\config"
GLOBAL_SKILLS = os.path.join(GLOBAL_CONFIG_ROOT, "skills")
GLOBAL_SUBAGENTS = os.path.join(GLOBAL_CONFIG_ROOT, "Sub_Agent")
GLOBAL_PLUGINS = os.path.join(GLOBAL_CONFIG_ROOT, "plugins")
GLOBAL_REFERENCES = os.path.join(GLOBAL_CONFIG_ROOT, "references")
GLOBAL_MEMORY_STORE = os.path.join(GLOBAL_REFERENCES, "GLOBAL_MEMORY_STORE.md")


def ensure_central_dirs():
    """التأكد من وجود كافة المجلدات الأساسية في المستودع المركزي"""
    for d in [CENTRAL_WORKSPACE, CENTRAL_AGENTS, CENTRAL_SKILLS, CENTRAL_SUBAGENTS, 
             CENTRAL_PLUGINS, CENTRAL_REFERENCES, CENTRAL_TEMPLATES]:
        os.makedirs(d, exist_ok=True)


def merge_memory_files(src_memory_path: str, dst_memory_path: str):
    """دمج الذكريات والدروس المستفادة بذكاء دون تكرار"""
    if not os.path.exists(src_memory_path):
        return 0

    with open(src_memory_path, 'r', encoding='utf-8') as f:
        src_content = f.read()

    existing_dst_content = ""
    if os.path.exists(dst_memory_path):
        with open(dst_memory_path, 'r', encoding='utf-8') as f:
            existing_dst_content = f.read()

    yaml_blocks = re.findall(r'```yaml(.*?)```', src_content, re.DOTALL)
    entries_to_add = []

    for yblock in yaml_blocks:
        parts = yblock.split('- id:')
        for p in parts[1:]:
            entry_text = "- id:" + p
            id_match = re.search(r'(MEM|BUG|ADR|LRN|P[0-9])-[A-Za-z0-9_-]+', entry_text)
            if id_match:
                mem_id = id_match.group(0)
                if mem_id not in existing_dst_content:
                    entries_to_add.append(entry_text.strip())

    if entries_to_add:
        with open(dst_memory_path, 'a', encoding='utf-8') as f:
            if not existing_dst_content.strip():
                f.write("<div dir=\"rtl\">\n\n# 🧠 سجل الذاكرة والدروس المستفادة\n\n```yaml\n")
            for entry in entries_to_add:
                f.write("\n" + entry + "\n")
            if not existing_dst_content.strip():
                f.write("```\n\n</div>\n")
        return len(entries_to_add)
    return 0


def copy_tree_overwrite(src: str, dst: str):
    """نسخ مجلد مع استبدال وتحديث الملفات الموجودة فقط"""
    if not os.path.exists(src):
        return 0
    os.makedirs(dst, exist_ok=True)
    count = 0
    for root, dirs, files in os.walk(src):
        rel_path = os.path.relpath(root, src)
        dest_dir = os.path.join(dst, rel_path)
        os.makedirs(dest_dir, exist_ok=True)
        for file in files:
            s_file = os.path.join(root, file)
            d_file = os.path.join(dest_dir, file)
            if not os.path.exists(d_file) or os.path.getmtime(s_file) > os.path.getmtime(d_file):
                shutil.copy2(s_file, d_file)
                count += 1
    return count


def sync_project_ai_to_central(project_root: str, is_verbose: bool = True):
    """تصدير ومزامنة أدوات الذكاء الاصطناعي من مشروع محدد إلى المستودع المركزي"""
    project_root = os.path.abspath(project_root)
    project_name = os.path.basename(project_root)
    project_agents_dir = os.path.join(project_root, ".agents")

    if not os.path.exists(project_agents_dir) and not os.path.exists(os.path.join(project_root, "opencode.json")):
        if is_verbose:
            print(f"⏩ تخطي المشروع (لا يحتوي على أدوات ذكاء اصطناعي): {project_name}")
        return

    print(f"\n========================================================")
    print(f"🚀 بدء تصدير أدوات الذكاء الاصطناعي من: {project_name}")
    print(f"📁 المسار: {project_root}")
    print(f"========================================================")

    ensure_central_dirs()

    conv_script = os.path.join(project_agents_dir, "convert_hooks_to_sheets.py")
    if os.path.exists(conv_script):
        print("📊 تحديث ملف الإكسيل ومكتبة الأوامر...")
        subprocess.run([sys.executable, conv_script], cwd=project_agents_dir, check=False)

    html_script = os.path.join(project_agents_dir, "update_html.py")
    if os.path.exists(html_script):
        subprocess.run([sys.executable, html_script], cwd=project_agents_dir, check=False)

    local_skills_dir = os.path.join(project_agents_dir, "skills")
    if os.path.exists(local_skills_dir):
        updated_skills = 0
        for skill_name in os.listdir(local_skills_dir):
            s_skill = os.path.join(local_skills_dir, skill_name)
            d_skill = os.path.join(CENTRAL_SKILLS, skill_name)
            if os.path.isdir(s_skill):
                updated_skills += copy_tree_overwrite(s_skill, d_skill)
        print(f"🧩 تم تحديث/مزامنة ملفات المهارات: {updated_skills} ملف.")

    local_subagents_dir = os.path.join(project_agents_dir, "Sub_Agent")
    if os.path.exists(local_subagents_dir):
        updated_agents = 0
        for agent_file in os.listdir(local_subagents_dir):
            s_agent = os.path.join(local_subagents_dir, agent_file)
            d_agent = os.path.join(CENTRAL_SUBAGENTS, agent_file)
            if os.path.isfile(s_agent):
                if not os.path.exists(d_agent) or os.path.getmtime(s_agent) > os.path.getmtime(d_agent):
                    shutil.copy2(s_agent, d_agent)
                    updated_agents += 1
        print(f"🤖 تم تحديث/مزامنة بطاقات الوكلاء: {updated_agents} بطاقة.")

    local_memory = os.path.join(project_agents_dir, "MEMORY_STORE.md")
    if not os.path.exists(local_memory):
        local_memory = os.path.join(project_root, "MEMORY_STORE.md")
    
    if os.path.exists(local_memory):
        central_memory = os.path.join(CENTRAL_WORKSPACE, "MEMORY_STORE.md")
        central_agents_memory = os.path.join(CENTRAL_AGENTS, "MEMORY_STORE.md")
        
        added_central = merge_memory_files(local_memory, central_memory)
        merge_memory_files(local_memory, central_agents_memory)
        
        os.makedirs(GLOBAL_REFERENCES, exist_ok=True)
        added_global = merge_memory_files(local_memory, GLOBAL_MEMORY_STORE)
        
        print(f"🧠 تم دمج وتحديث الذاكرة: {added_central} درس جديد في المستودع المركزي ({added_global} في الذاكرة العالمية).")

    for rule_file in ["ACTIVE_CONTEXT_INJECTION.md", "AGENTS.md", "HOOKS_GUIDE.md", "HOOKS_GUIDE.xlsx"]:
        src_f = os.path.join(project_agents_dir, rule_file)
        if not os.path.exists(src_f):
            src_f = os.path.join(project_root, rule_file)
        if os.path.exists(src_f):
            shutil.copy2(src_f, os.path.join(CENTRAL_AGENTS, rule_file))
            shutil.copy2(src_f, os.path.join(CENTRAL_WORKSPACE, rule_file))

    fleet_templates_src = os.path.join(project_root, "fleet_templates")
    if os.path.exists(fleet_templates_src):
        copy_tree_overwrite(fleet_templates_src, CENTRAL_TEMPLATES)
        copy_tree_overwrite(fleet_templates_src, os.path.join(CENTRAL_AGENTS, "fleet_templates"))

    for fleet_file in ["fleet_config.json", "opencode.json", "mcp_config.json"]:
        src_cf = os.path.join(project_root, fleet_file)
        if os.path.exists(src_cf):
            target_dst = os.path.join(CENTRAL_WORKSPACE, fleet_file)
            if not os.path.exists(target_dst) or os.path.getmtime(src_cf) > os.path.getmtime(target_dst):
                shutil.copy2(src_cf, target_dst)

    for sync_scr in ["sync_global_ecosystem.py", "convert_hooks_to_sheets.py", "update_html.py", "update_models_matrix.py"]:
        src_s = os.path.join(project_agents_dir, sync_scr)
        if os.path.exists(src_s):
            shutil.copy2(src_s, os.path.join(CENTRAL_AGENTS, sync_scr))
            shutil.copy2(src_s, os.path.join(CENTRAL_WORKSPACE, sync_scr))

    print(f"✅ اكتمل تصدير وتحديث أدوات الذكاء الاصطناعي من [{project_name}] بنجاح إلى المركز!")


def sync_from_global_config():
    """مزامنة الإضافات من الإعدادات العامة للنظام (~/.gemini/config)"""
    print("\n🌐 مزامنة الأدوات والمهارات من الإعدادات العالمية للنظام...")
    ensure_central_dirs()
    
    if os.path.exists(GLOBAL_SKILLS):
        c = copy_tree_overwrite(GLOBAL_SKILLS, CENTRAL_SKILLS)
        print(f"   - مهارات النظام العالمية: تم تحديث {c} ملف.")

    if os.path.exists(GLOBAL_SUBAGENTS):
        c = copy_tree_overwrite(GLOBAL_SUBAGENTS, CENTRAL_SUBAGENTS)
        print(f"   - وكلاء النظام العالميون: تم تحديث {c} ملف.")

    if os.path.exists(GLOBAL_PLUGINS):
        c = copy_tree_overwrite(GLOBAL_PLUGINS, CENTRAL_PLUGINS)
        print(f"   - ملحقات النظام (Plugins): تم تحديث {c} ملف.")

    if os.path.exists(GLOBAL_REFERENCES):
        c = copy_tree_overwrite(GLOBAL_REFERENCES, CENTRAL_REFERENCES)
        print(f"   - المراجع المركزية: تم تحديث {c} ملف.")

    for g_file in ["ACTIVE_CONTEXT_INJECTION.md", "mcp_config.json", "config.json"]:
        g_src = os.path.join(GLOBAL_CONFIG_ROOT, g_file)
        if os.path.exists(g_src):
            shutil.copy2(g_src, os.path.join(CENTRAL_WORKSPACE, g_file))


def main():
    parser = argparse.ArgumentParser(description="محرك المزامنة والتصدير الشامل لأدوات الذكاء الاصطناعي")
    parser.add_argument("--from", dest="source_path", help="مسار المشروع المصدر لتصدير أدواته إلى المستودع المركزي")
    parser.add_argument("--all", action="store_true", help="فحص وتصدير أدوات كافة المشاريع الموجودة في F:\\AI PROJECTS\\")
    args = parser.parse_args()

    print("====================================================================")
    print("🌟 محرك المزامنة والتصدير الشامل لأدوات الذكاء الاصطناعي (Hub Exporter)")
    print(f"🎯 المستودع المركزي المستهدف: {CENTRAL_WORKSPACE}")
    print("====================================================================")

    sync_from_global_config()

    if args.all:
        parent_dir = r"F:\AI PROJECTS"
        if os.path.exists(parent_dir):
            for item in os.listdir(parent_dir):
                full_p = os.path.join(parent_dir, item)
                if os.path.isdir(full_p) and full_p.lower() != CENTRAL_WORKSPACE.lower():
                    sync_project_ai_to_central(full_p, is_verbose=False)
    elif args.source_path:
        sync_project_ai_to_central(args.source_path)
    else:
        current_cwd = os.getcwd()
        if os.path.exists(os.path.join(current_cwd, ".agents")) and current_cwd.lower() != CENTRAL_WORKSPACE.lower():
            sync_project_ai_to_central(current_cwd)
        else:
            blind_app_path = r"F:\AI PROJECTS\Blind App"
            if os.path.exists(blind_app_path):
                sync_project_ai_to_central(blind_app_path)

    print("\n🎉 تم اكتمال كافة عمليات التصدير والمزامنة بنجاح! المستودع المركزي محدث بالكامل.")
    print("====================================================================")


if __name__ == "__main__":
    main()
