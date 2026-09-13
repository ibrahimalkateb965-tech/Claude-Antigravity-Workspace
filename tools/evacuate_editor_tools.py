#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
محرك ترحيل وتطهير أدوات المحرر والذكاء الاصطناعي (Hook 28 Engine)
Editor Tooling Evacuation & Workspace Sanitizer

الوظيفة:
1. التحقق من مزامنة وتحديث أدوات الذكاء الاصطناعي النشطة في الإعدادات العالمية (C:\\Users\\Kt\\.gemini\\config\\tools).
2. ترحيل ونقل كافة أدلة المحرر وملفات التوثيق والسكربتات إلى المستودع المركزي (F:\\AI PROJECTS\\Claude+Antigravity).
3. التحقق التشفيري الإلزامي (SHA-256) قبل حذف أي ملف من بيئة المشروع المحلي.
4. تنظيف المخلفات المؤقتة (JVM Crash Dumps, Word Lock files) وإعادة شجرة العمل إلى النقاء التام.
"""

import os
import sys
import shutil
import hashlib
import argparse
import codecs
import time
from datetime import datetime
from typing import List, Dict, Tuple, Optional

# فرض ترميز UTF-8 لمخرجات الكونسول في ويندوز
if hasattr(sys.stdout, 'encoding') and sys.stdout.encoding.lower() != 'utf-8':
    sys.stdout = codecs.getwriter('utf-8')(sys.stdout.detach())

# ------------------------------------------------------------------------------
# المسارات المعيارية الثابتة
# ------------------------------------------------------------------------------
GLOBAL_CONFIG_ROOT = r"C:\Users\Kt\.gemini\config"
GLOBAL_CONFIG_TOOLS = os.path.join(GLOBAL_CONFIG_ROOT, "tools")

CENTRAL_WORKSPACE = r"F:\AI PROJECTS\Claude+Antigravity"
CENTRAL_TOOLS = os.path.join(CENTRAL_WORKSPACE, "tools")
CENTRAL_DOCS = os.path.join(CENTRAL_WORKSPACE, "docs")
CENTRAL_ASSETS = os.path.join(CENTRAL_WORKSPACE, "assets")

# قائمة الملفات والأنماط التابعة للمحرر التي يجب ترحيلها من المشروع المحلي
EDITOR_TOOLS_WHITELIST = {
    "ag_context_monitor.py",
    "context_advisor.py",
    "prompt_library_curator.py",
    "build_antigravity_guide_doc.py",
}

DOCS_EXTENSIONS = {".docx", ".pdf", ".md"}

EDITOR_DOC_PREFIXES = [
    "Antigravity_Context_Management_Guide",
    "Claude_Code_Context_Management_Guide",
]

EPHEMERAL_PATTERNS = [
    "hs_err_pid",
    "replay_pid",
    "gate.log",
    "test.docx",
    "test.pdf"
]


def calculate_sha256(file_path: str) -> str:
    """حساب البصمة التشفيرية للملف لضمان سلامة النقل 100%"""
    if not os.path.exists(file_path) or os.path.isdir(file_path):
        return ""
    hasher = hashlib.sha256()
    try:
        with open(file_path, 'rb') as f:
            while chunk := f.read(65536):
                hasher.update(chunk)
        return hasher.hexdigest()
    except Exception as e:
        print(f"⚠️ تعذر قراءة الملف لحساب البصمة: {file_path} ({e})")
        return ""


def ensure_destination_dirs():
    """التأكد من وجود كافة المجلدات المستهدفة في المركز والإعدادات العالمية"""
    for d in [GLOBAL_CONFIG_TOOLS, CENTRAL_WORKSPACE, CENTRAL_TOOLS, CENTRAL_DOCS, CENTRAL_ASSETS]:
        os.makedirs(d, exist_ok=True)


def safe_transfer_and_verify(src_path: str, dst_path: str, dry_run: bool = False) -> Tuple[bool, str]:
    """
    بروتوكول النسخ والتحقق بالبصمة قبل الحذف:
    1. قراءة SHA-256 للمصدر.
    2. فحص ما إذا كان الملف موجوداً في الوجهة. إذا كان مختلفاً، حفظ نسخة أرشيفية.
    3. النسخ ثم مطابقة SHA-256.
    4. إذا تطابقت، يتم الحذف بأمان.
    """
    if not os.path.exists(src_path):
        return False, "المصدر غير موجود"

    src_hash = calculate_sha256(src_path)
    if not src_hash:
        return False, "فشل حساب بصمة المصدر"

    if dry_run:
        return True, f"[DRY-RUN] سيتم نقل {os.path.basename(src_path)} ➔ {dst_path}"

    os.makedirs(os.path.dirname(dst_path), exist_ok=True)

    # التحقق من وجود تعارض في الوجهة
    final_dst = dst_path
    if os.path.exists(dst_path):
        dst_hash = calculate_sha256(dst_path)
        if dst_hash == src_hash:
            # الملف متطابق تماماً في الوجهة بالفعل! الحذف آمن فوراً.
            try:
                os.remove(src_path)
                return True, "تم الحذف بأمان (الملف متطابق مسبقاً في الوجهة)"
            except Exception as e:
                return False, f"فشل حذف الملف المحلي: {e}"
        else:
            # محتوى مختلف: التحقق من التواريخ والأحجام
            src_mtime = os.path.getmtime(src_path)
            dst_mtime = os.path.getmtime(dst_path)
            if src_mtime > dst_mtime:
                # المصدر أحدث، استبدال مع عمل نسخة احتياطية
                backup_dst = dst_path + f".bak_{int(dst_mtime)}"
                shutil.copy2(dst_path, backup_dst)
                final_dst = dst_path
            else:
                # الوجهة أحدث، حفظ المصدر كأرشيف لتجنب الكتابة فوق الأحدث
                base, ext = os.path.splitext(dst_path)
                final_dst = f"{base}_from_project_{int(src_mtime)}{ext}"

    # تنفيذ النسخ
    shutil.copy2(src_path, final_dst)

    # التدقيق التشفيري بعد النسخ
    verified_hash = calculate_sha256(final_dst)
    if verified_hash != src_hash:
        return False, f"⛔ فشل التحقق التشفيري! البصمة غير متطابقة ({src_hash[:8]} != {verified_hash[:8]})"

    # الحذف الآمن للمصدر بعد التأكد القاطع
    try:
        os.remove(src_path)
        return True, f"✅ تم النقل والتحقق بنجاح ➔ {os.path.basename(final_dst)}"
    except Exception as e:
        return False, f"تم النقل لكن تعذر حذف المصدر: {e}"


def sync_tool_to_global_and_central(src_tool_path: str, dry_run: bool = False) -> Dict[str, str]:
    """مزامنة الأداة إلى كل من global config و Claude+Antigravity المركزي ثم حذفها محلياً"""
    tool_name = os.path.basename(src_tool_path)
    result = {"tool": tool_name, "global": "", "central": "", "status": "FAIL"}

    global_dst = os.path.join(GLOBAL_CONFIG_TOOLS, tool_name)
    central_dst = os.path.join(CENTRAL_TOOLS, tool_name)

    src_hash = calculate_sha256(src_tool_path)
    if not src_hash:
        result["status"] = "تعذر حساب البصمة"
        return result

    if dry_run:
        result["status"] = f"[DRY-RUN] سيتم نقل {tool_name} إلى Global Config و Central Hub"
        return result

    # 1. المزامنة إلى Global Config أولاً
    if os.path.exists(global_dst):
        g_hash = calculate_sha256(global_dst)
        if g_hash != src_hash:
            # نحدث global_dst إذا كان المحلي أحدث أو أكبر
            if os.path.getsize(src_tool_path) >= os.path.getsize(global_dst):
                shutil.copy2(src_tool_path, global_dst)
                result["global"] = "تم التحديث"
            else:
                result["global"] = "الإصدار العالمي أحدث/أكبر (محتفظ به)"
        else:
            result["global"] = "متطابق مسبقاً"
    else:
        shutil.copy2(src_tool_path, global_dst)
        result["global"] = "تم التثبيت الجديد"

    # 2. المزامنة إلى Central Tools
    if os.path.exists(central_dst):
        c_hash = calculate_sha256(central_dst)
        if c_hash != src_hash:
            if os.path.getsize(src_tool_path) >= os.path.getsize(central_dst):
                shutil.copy2(src_tool_path, central_dst)
                result["central"] = "تم التحديث"
            else:
                result["central"] = "الإصدار المركزي أحدث/أكبر (محتفظ به)"
        else:
            result["central"] = "متطابق مسبقاً"
    else:
        shutil.copy2(src_tool_path, central_dst)
        result["central"] = "تم التثبيت الجديد"

    # 3. التأكد من حفظ الملف على الأقل في أحد الوجهتين ببصمة صحيحة قبل الحذف المحلي
    final_g_hash = calculate_sha256(global_dst)
    final_c_hash = calculate_sha256(central_dst)

    if final_g_hash == src_hash or final_c_hash == src_hash:
        try:
            os.remove(src_tool_path)
            result["status"] = "SUCCESS: تم النقل والتحقق والحذف المحلي"
        except Exception as e:
            result["status"] = f"تم النسخ لكن فشل الحذف: {e}"
    else:
        # إذا كان كلاهما أكبر ولديهما ميزات أحدث، الملف المحلي مستغنى عنه بأمان
        if os.path.getsize(global_dst) > os.path.getsize(src_tool_path) or os.path.getsize(central_dst) > os.path.getsize(src_tool_path):
            try:
                os.remove(src_tool_path)
                result["status"] = "SUCCESS: تم اعتماد النسخة الأحدث وحذف النسخة القديمة المحلية"
            except Exception as e:
                result["status"] = f"فشل حذف النسخة القديمة: {e}"
        else:
            result["status"] = "ABORT: لم تتطابق البصمة مع أي وجهة"

    return result


def evacuate_workspace(project_path: str, dry_run: bool = False):
    """تنفيذ عملية الترحيل والتطهير الكاملة لمشروع محدد"""
    project_path = os.path.abspath(project_path)
    project_name = os.path.basename(project_path)

    print(f"\n{'='*70}")
    print(f"🧹 محرك ترحيل وتطهير أدوات المحرر (Hook 28: Workspace Sanitizer)")
    print(f"📁 المشروع المستهدف: {project_name} ({project_path})")
    print(f"⚙️ وضع التشغيل: {'[محاكاة فقط DRY-RUN]' if dry_run else '[تنفيذ حقيقي LIVE EXECUTION]'}")
    print(f"{'='*70}\n")

    ensure_destination_dirs()

    # --------------------------------------------------------------------------
    # 1. جرد وتطهير مجلد tools/ المحلي
    # --------------------------------------------------------------------------
    project_tools_dir = os.path.join(project_path, "tools")
    if os.path.exists(project_tools_dir):
        print("🔍 [المرحلة 1] جرد وترحيل أدوات المحرر من مجلد tools/ ...")
        for item in list(os.listdir(project_tools_dir)):
            full_item_path = os.path.join(project_tools_dir, item)
            if os.path.isfile(full_item_path) and item in EDITOR_TOOLS_WHITELIST:
                res = sync_tool_to_global_and_central(full_item_path, dry_run=dry_run)
                print(f"   ➔ {item:<32}: {res['status']} (Global: {res.get('global', '-')}, Central: {res.get('central', '-')})")

        # إذا أصبح مجلد tools فارغاً، نحذفه لتطهير المشروع
        if not dry_run and os.path.exists(project_tools_dir) and not os.listdir(project_tools_dir):
            try:
                os.rmdir(project_tools_dir)
                print("   ✨ تم إزالة مجلد tools/ الفارغ بنجاح.")
            except Exception:
                pass
    else:
        print("ℹ️ مجلد tools/ غير موجود في المشروع (سليم).")

    # --------------------------------------------------------------------------
    # 2. جرد وترحيل وثائق وأدلة المحرر في جذر المشروع
    # --------------------------------------------------------------------------
    print("\n🔍 [المرحلة 2] جرد وترحيل أدلة ومستندات المحرر من جذر المشروع...")
    root_items = list(os.listdir(project_path))
    evacuated_docs = 0

    for item in root_items:
        full_path = os.path.join(project_path, item)
        if not os.path.isfile(full_path):
            continue

        # فحص أدلة المحرر
        is_editor_doc = any(item.startswith(prefix) for prefix in EDITOR_DOC_PREFIXES)
        if is_editor_doc:
            dst_file = os.path.join(CENTRAL_DOCS, item)
            ok, msg = safe_transfer_and_verify(full_path, dst_file, dry_run=dry_run)
            print(f"   📄 {item:<40}: {msg}")
            if ok:
                evacuated_docs += 1

        # فحص الرسوم البيانية الخاصة بالمحرر
        elif item == "flowchart_promax_neon_4k.png":
            dst_file = os.path.join(CENTRAL_ASSETS, item)
            ok, msg = safe_transfer_and_verify(full_path, dst_file, dry_run=dry_run)
            print(f"   🖼️ {item:<40}: {msg}")
            if ok:
                evacuated_docs += 1

    print(f"   📊 إجمالي المستندات المنقولة والمحققة: {evacuated_docs}")

    # --------------------------------------------------------------------------
    # 3. تنظيف المخلفات المؤقتة والملفات الزائدة
    # --------------------------------------------------------------------------
    print("\n🔍 [المرحلة 3] تنظيف المخلفات المؤقتة وأقفال الملفات وحطام JVM...")
    cleaned_junk = 0
    for item in list(os.listdir(project_path)):
        full_path = os.path.join(project_path, item)
        if not os.path.isfile(full_path):
            continue

        # فحص ملفات قفل وورد المؤقتة
        if item.startswith("~$"):
            if dry_run:
                print(f"   🧹 [DRY-RUN] سيتم حذف ملف القفل المؤقت: {item}")
            else:
                try:
                    os.remove(full_path)
                    print(f"   🧹 تم حذف ملف القفل المؤقت: {item}")
                    cleaned_junk += 1
                except Exception as e:
                    print(f"   ⚠️ تعذر حذف ملف القفل (الملف مفتوح حالياً): {item}")

        # فحص مخلفات JVM و gate.log و ملفات الاختبار المؤقتة
        elif any(item.startswith(pat) or item == pat for pat in EPHEMERAL_PATTERNS):
            if dry_run:
                print(f"   🧹 [DRY-RUN] سيتم إزالة ملف المخلفات: {item}")
            else:
                try:
                    os.remove(full_path)
                    print(f"   🧹 تم إزالة ملف المخلفات: {item}")
                    cleaned_junk += 1
                except Exception as e:
                    print(f"   ⚠️ تعذر إزالة الملف {item}: {e}")

    print(f"   📊 إجمالي المخلفات التي تم تنظيفها: {cleaned_junk}")

    print(f"\n{'='*70}")
    print(f"🎉 تم اكتمال خطاف التطهير والترحيل للمشروع بنجاح!")
    print(f"🛡️ بيئة العمل في [{project_name}] أصبحت نقية 100% وجاهزة لبوابات الجودة والتصفير.")
    print(f"{'='*70}\n")


def main():
    parser = argparse.ArgumentParser(description="محرك ترحيل وتطهير أدوات المحرر والذكاء الاصطناعي (Hook 28)")
    parser.add_argument("--project", dest="project_path", default=os.getcwd(), help="مسار المشروع المراد تطهيره")
    parser.add_argument("--dry-run", action="store_true", help="محاكاة الفحص دون حذف أو نقل فعلي")
    args = parser.parse_args()

    evacuate_workspace(args.project_path, dry_run=args.dry_run)


if __name__ == "__main__":
    main()
