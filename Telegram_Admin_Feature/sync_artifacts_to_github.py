import os
import sys
import shutil
import glob
from pathlib import Path
import subprocess
from datetime import datetime
from dotenv import load_dotenv

load_dotenv(dotenv_path=Path(__file__).parent.parent / ".env")

# Safe utf-8 output
if hasattr(sys.stdout, 'reconfigure'):
    sys.stdout.reconfigure(encoding='utf-8')

def get_latest_conversation_dir(brain_dir):
    """Finds the most recently modified conversation directory in the brain folder."""
    convo_dirs = [os.path.join(brain_dir, d) for d in os.listdir(brain_dir) if os.path.isdir(os.path.join(brain_dir, d))]
    if not convo_dirs:
        return None
    # Sort by modification time
    convo_dirs.sort(key=lambda x: os.path.getmtime(x), reverse=True)
    return convo_dirs[0]

def main():
    project_root = Path(__file__).parent.parent.absolute()
    docs_dir = project_root / "docs"
    docs_dir.mkdir(exist_ok=True)
    
    # Ensure .nojekyll exists
    nojekyll_path = docs_dir / ".nojekyll"
    if not nojekyll_path.exists():
        nojekyll_path.touch()

    # مجلد "العقل" — قابل للتهيئة عبر .env مع مسار Antigravity الافتراضي
    brain_dir = os.getenv("ARTIFACTS_BRAIN_DIR", "").strip()
    if not brain_dir:
        brain_dir = os.path.join(
            os.path.expanduser("~"), ".gemini", "antigravity-ide", "brain"
        )
    
    if not os.path.exists(brain_dir):
        print(f"❌ خطأ: لم يتم العثور على مجلد العقل (Brain) في المسار {brain_dir}")
        print("   💡 الحل: أضِف ARTIFACTS_BRAIN_DIR=<المسار الصحيح> إلى ملف .env")
        sys.exit(1)

    latest_dir = get_latest_conversation_dir(brain_dir)
    if not latest_dir:
        print("❌ خطأ: لا توجد محادثات سابقة.")
        sys.exit(1)

    print(f"🔄 جاري سحب التقارير (Artifacts) من المحادثة: {os.path.basename(latest_dir)}")

    # Copy markdown files
    md_files = glob.glob(os.path.join(latest_dir, "*.md"))
    
    if not md_files:
        print("⚠️ لم يتم العثور على أي ملفات Markdown (Artifacts) في المحادثة الأخيرة.")
        sys.exit(1)

    copied_files = []
    for f in md_files:
        basename = os.path.basename(f)
        dest_path = docs_dir / basename
        shutil.copy2(f, dest_path)
        copied_files.append(basename)
        print(f"✅ تم نسخ: {basename}")

    # Generate an index.md
    index_path = docs_dir / "index.md"
    
    index_content = f"""<div dir="rtl">

# أرشيف التقارير (Artifacts Hub)
تاريخ المزامنة: {datetime.now().strftime("%Y-%m-%d %H:%M:%S")}

تم سحب التقارير التالية بنجاح من أحدث محادثة:

"""
    for file in copied_files:
        if file.lower() != "index.md":
            index_content += f"- [{file}]({file})\n"
            
    index_content += "\n</div>\n"
    
    with open(index_path, "w", encoding="utf-8") as idx:
        idx.write(index_content)
        
    print("✅ تم تحديث الفهرس (index.md)")

    # Git operations
    print("\n🚀 جاري الرفع إلى GitHub...")
    try:
        # Add docs folder
        subprocess.run(["git", "add", "docs/"], cwd=project_root, check=True)
        # Commit
        commit_msg = f"Auto-sync artifacts from conversation {os.path.basename(latest_dir)}"
        subprocess.run(["git", "commit", "-m", commit_msg], cwd=project_root)
        # Push
        subprocess.run(["git", "push"], cwd=project_root, check=True)
        print("🎉 تم الرفع إلى GitHub بنجاح! يمكنك الآن استعراضها عبر GitHub Pages.")
    except subprocess.CalledProcessError as e:
        print(f"⚠️ فشلت عملية رفع Git: {e}. قد يكون المستودع محدثاً بالفعل أو توجد مشكلة في الاتصال.")
        sys.exit(1)

if __name__ == "__main__":
    main()
