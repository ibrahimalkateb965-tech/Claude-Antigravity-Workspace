import os
import re
import sys
import codecs
from fastmcp import FastMCP


# إعداد المسارات الأساسية
LOCAL_MEMORY_PATH = r"f:\AI PROJECTS\Blind App\.agents\MEMORY_STORE.md"
GLOBAL_REFERENCES_DIR = r"C:\Users\Kt\.gemini\config\references"
GLOBAL_MEMORY_PATH = os.path.join(GLOBAL_REFERENCES_DIR, "GLOBAL_MEMORY_STORE.md")

# تهيئة سيرفر MCP
mcp = FastMCP("GlobalMemorySync")

def extract_global_memories(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()

    # البحث عن جميع كتل YAML
    yaml_matches = re.findall(r'```yaml(.*?)```', content, re.DOTALL)
    if not yaml_matches:
        return []
    
    global_entries = []
    
    for yaml_content in yaml_matches:
        blocks = yaml_content.split('- id:')
        
        for block in blocks[1:]:
            if 'global' in block.lower():  
                entry = "- id:" + block
                global_entries.append(entry.strip())
            
    return global_entries

@mcp.tool()
def sync_global_memory() -> str:
    """Syncs the 'global' tagged memory entries from the local MEMORY_STORE.md to the Global References directory."""
    if not os.path.exists(LOCAL_MEMORY_PATH):
        return "❌ ملف الذاكرة المحلية غير موجود!"
        
    entries = extract_global_memories(LOCAL_MEMORY_PATH)
    
    if not entries:
        return "⚠️ لم يتم العثور على ذكريات مصنفة كعالمية (global) للتصدير."
        
    os.makedirs(GLOBAL_REFERENCES_DIR, exist_ok=True)
    
    existing_content = ""
    if os.path.exists(GLOBAL_MEMORY_PATH):
        with open(GLOBAL_MEMORY_PATH, 'r', encoding='utf-8') as f:
            existing_content = f.read()
            
    new_entries_count = 0
    entries_to_add = []
    
    for entry in entries:
        id_match = re.search(r'(MEM|BUG|ADR)-\d{4}-\d{2}-\d{2}-\d+', entry)
        entry_id = id_match.group(0) if id_match else None
        
        if entry_id and entry_id not in existing_content:
            entries_to_add.append(entry)
            new_entries_count += 1
            
    if new_entries_count == 0:
        return "ℹ️ جميع الذكريات العالمية موجودة بالفعل في المركز العالمي (لم تتم إضافة أي جديد)."

    is_new_file = not existing_content.strip()
    
    with open(GLOBAL_MEMORY_PATH, 'a', encoding='utf-8') as f:
        if is_new_file:
            f.write("<div dir=\"rtl\">\n\n# 🧠 الذاكرة العالمية المجمعة (Global Memory Store)\n\n")
            f.write("> **يحتوي هذا الملف على الذكريات والدروس المستفادة التي تم تصديرها أوتوماتيكياً عبر أداة MCP.**\n\n")
            f.write("```yaml\n")
            
        for entry in entries_to_add:
            f.write(entry + "\n\n")
            
        if is_new_file:
            f.write("```\n\n</div>\n")
            
    return f"🚀 تم تصدير {new_entries_count} ذكريات جديدة بنجاح إلى المركز العالمي!"

if __name__ == "__main__":
    # تشغيل سيرفر MCP عبر stdio
    mcp.run()
