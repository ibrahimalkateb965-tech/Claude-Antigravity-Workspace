#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
محرك توليد مستند PDF التفاعلي الفاخر (The Zero-to-Hero Journey PDF Generator)
يستخدم محرك Chromium / Microsoft Edge Headless لتصيير HTML إلى PDF بمتجهات عالية الدقة وروابط حية.
"""

import os
import sys
import subprocess
import time

# فرض ترميز UTF-8 لمنع أخطاء UnicodeEncodeError على Windows
if sys.platform == 'win32':
    try:
        sys.stdout.reconfigure(encoding='utf-8')
        sys.stderr.reconfigure(encoding='utf-8')
    except Exception:
        pass

def find_browser_executable():
    """البحث عن متصفح مبني على كروميوم (Edge أو Chrome) في المسارات القياسية لويندوز"""
    possible_paths = [
        # Microsoft Edge
        r"C:\Program Files (x86)\Microsoft\Edge\Application\msedge.exe",
        r"C:\Program Files\Microsoft\Edge\Application\msedge.exe",
        os.path.expandvars(r"%LOCALAPPDATA%\Microsoft\Edge\Application\msedge.exe"),
        # Google Chrome
        r"C:\Program Files\Google\Chrome\Application\chrome.exe",
        r"C:\Program Files (x86)\Google\Chrome\Application\chrome.exe",
        os.path.expandvars(r"%LOCALAPPDATA%\Google\Chrome\Application\chrome.exe"),
        # Brave
        r"C:\Program Files\BraveSoftware\Brave-Browser\Application\brave.exe",
    ]

    for path in possible_paths:
        if os.path.exists(path):
            return path
    
    # Try finding in PATH
    for name in ["msedge.exe", "chrome.exe", "msedge", "chrome"]:
        try:
            result = subprocess.run(["where", name], capture_output=True, text=True, check=False)
            if result.returncode == 0 and result.stdout.strip():
                return result.stdout.strip().splitlines()[0]
        except Exception:
            pass

    return None

def convert_html_to_pdf(html_path, output_pdf_path):
    """تحويل ملف HTML إلى PDF متجهات مع الحفاظ على الروابط والخطوط"""
    browser_exe = find_browser_executable()
    
    if not browser_exe:
        print("[!] لم يتم العثور على متصفح Microsoft Edge أو Google Chrome على الجهاز.")
        return False

    print(f"[*] تم اكتشاف المتصفح: {browser_exe}")
    
    # Normalize absolute paths
    abs_html = os.path.abspath(html_path)
    abs_pdf = os.path.abspath(output_pdf_path)
    
    # Convert file path to file:// URL
    file_url = f"file:///{abs_html.replace(os.sep, '/')}"
    
    os.makedirs(os.path.dirname(abs_pdf), exist_ok=True)
    
    # Command to render PDF using Chromium Headless
    cmd = [
        browser_exe,
        "--headless",
        "--disable-gpu",
        "--allow-file-access-from-files",
        "--run-all-compositor-stages-before-draw",
        f"--print-to-pdf={abs_pdf}",
        "--no-pdf-header-footer",
        file_url
    ]
    
    print(f"[*] جاري تصيير وتوليد ملف PDF: {abs_pdf} ...")
    try:
        proc = subprocess.run(cmd, capture_output=True, text=True, timeout=60, check=False)
        time.sleep(1) # wait for file lock release
        
        if os.path.exists(abs_pdf) and os.path.getsize(abs_pdf) > 0:
            size_kb = os.path.getsize(abs_pdf) / 1024
            print(f"[OK] تم توليد ملف PDF بنجاح تام!")
            print(f"   المسار: {abs_pdf}")
            print(f"   الحجم: {size_kb:.2f} KB")
            return True
        else:
            print("[!] فشل في إنشاء ملف PDF أو الملف الناتج فارغ.")
            if proc.stderr:
                print(f"تفاصيل الخطأ: {proc.stderr}")
            return False
    except Exception as e:
        print(f"[!] حدث استثناء أثناء التوليد: {e}")
        return False

if __name__ == "__main__":
    script_dir = os.path.dirname(os.path.abspath(__file__))
    input_html = os.path.join(script_dir, "docs", "zero_to_hero_guide.html")
    output_pdf = os.path.join(script_dir, "docs", "The_Zero_To_Hero_Journey_Autovem.pdf")
    
    if not os.path.exists(input_html):
        print(f"[!] لم يتم العثور على القالب: {input_html}")
        sys.exit(1)
        
    success = convert_html_to_pdf(input_html, output_pdf)
    if success:
        sys.exit(0)
    else:
        sys.exit(1)
