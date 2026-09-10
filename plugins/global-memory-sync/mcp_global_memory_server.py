import os
import re
import sys
from pathlib import Path
from typing import Optional
from fastmcp import FastMCP

# تهيئة سيرفر MCP
mcp = FastMCP("GlobalMemorySync")

GLOBAL_REFERENCES_DIR = r"C:\Users\Kt\.gemini\config\references"
GLOBAL_MEMORY_PATH = os.path.join(GLOBAL_REFERENCES_DIR, "GLOBAL_MEMORY_STORE.md")

def extract_global_memories(filepath: str):
    if not os.path.exists(filepath):
        return []
    with open(filepath, 'r', encoding='utf-8', errors='replace') as f:
        content = f.read()

    blocks = content.split('- id:')
    global_entries = []
    for block in blocks[1:]:
        if 'tags:' in block:
            tags_match = re.search(r'tags:\s*\[(.*?)\]', block, re.IGNORECASE)
            if tags_match and 'global' in tags_match.group(1).lower():
                clean_block = re.split(r'\n(?=[#\-]{3,}|\s*##|\s*```)', block)[0]
                entry = "- id:" + clean_block
                global_entries.append(entry.strip())
            
    return global_entries

@mcp.tool()
def sync_global_memory(local_memory_path: Optional[str] = None) -> str:
    """Syncs the 'global' tagged memory entries from the local MEMORY_STORE.md to the Global References directory."""
    target_path = local_memory_path
    if not target_path or not os.path.exists(target_path):
        candidates = [
            os.path.join(os.getcwd(), ".agents", "MEMORY_STORE.md"),
            r"g:\M.Yonis\.agents\MEMORY_STORE.md",
            r"f:\AI PROJECTS\Blind App\.agents\MEMORY_STORE.md"
        ]
        for c in candidates:
            if os.path.exists(c):
                target_path = c
                break

    if not target_path or not os.path.exists(target_path):
        return f"❌ لم يتم العثور على ملف ذاكرة محلي في: {target_path or 'المسارات الافتراضية'}"
        
    entries = extract_global_memories(target_path)
    if not entries:
        return f"⚠️ لم يتم العثور على ذكريات مصنفة كعالمية (global) في {target_path}."
        
    os.makedirs(GLOBAL_REFERENCES_DIR, exist_ok=True)
    existing_content = ""
    if os.path.exists(GLOBAL_MEMORY_PATH):
        with open(GLOBAL_MEMORY_PATH, 'r', encoding='utf-8', errors='replace') as f:
            existing_content = f.read()
            
    new_entries_count = 0
    entries_to_add = []
    
    for entry in entries:
        # البحث عن أي معرّف ID متاح
        id_match = re.search(r'- id:\s*([A-Za-z0-9_-]+)', entry)
        entry_id = id_match.group(1) if id_match else None
        
        if entry_id and entry_id not in existing_content:
            entries_to_add.append(entry)
            new_entries_count += 1
            
    if new_entries_count == 0:
        return f"ℹ️ جميع الذكريات العالمية من ({Path(target_path).name}) موجودة بالفعل في المركز العالمي."

    with open(GLOBAL_MEMORY_PATH, 'a', encoding='utf-8') as f:
        for entry in entries_to_add:
            f.write("\n" + entry + "\n")
            
    return f"🚀 تم تصدير {new_entries_count} ذكريات جديدة بنجاح من {Path(target_path).parent.parent.name} إلى المركز العالمي!"

if __name__ == "__main__":
    mcp.run()
