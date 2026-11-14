import os
import re
import json

def update_prompt_library():
    # Paths
    base_dir = os.path.dirname(os.path.abspath(__file__))
    md_paths = [
        os.path.join(base_dir, "HOOKS_GUIDE.md"),
        r"F:\AI PROJECTS\Blind App\.agents\HOOKS_GUIDE.md",
        r"C:\Users\Kt\.gemini\config\HOOKS_GUIDE.md"
    ]
    
    md_path = None
    for p in md_paths:
        if os.path.exists(p):
            md_path = p
            break
            
    if not md_path:
        print("Error: HOOKS_GUIDE.md not found.")
        return

    html_targets = [
        r"F:\AI PROJECTS\Claude+Antigravity\03_Dynamic_Prompt_Library\index.html",
        r"F:\AI PROJECTS\Claude+Antigravity\03_Dynamic_Hooks_Library\index.html",
        r"F:\AI PROJECTS\Claude+Antigravity -production\03_Dynamic_Prompt_Library\index.html",
        r"F:\AI PROJECTS\Claude+Antigravity -production\03_Dynamic_Hooks_Library\index.html",
        r"F:\AI PROJECTS\Quran_Records\with antigravity\New Crew\03_Dynamic_Prompt_Library\index.html"
    ]

    # Google Drive (Autovem Cloud Suite) Dynamic Integration
    gdrive_roots = [
        r"H:\My Drive\Autovem",
        r"I:\My Drive\Autovem",
        os.path.expandvars(r"%USERPROFILE%\Google Drive\Autovem"),
        os.path.expandvars(r"%USERPROFILE%\Google Drive\My Drive\Autovem")
    ]
    for g_root in gdrive_roots:
        parent = os.path.dirname(g_root)
        if os.path.exists(parent):
            g_prompt = os.path.join(g_root, "03_Dynamic_Prompt_Library", "index.html")
            g_hooks = os.path.join(g_root, "03_Dynamic_Hooks_Library", "index.html")
            hub_target = os.path.join(g_root, "index.html")
            master_prompt = r"F:\AI PROJECTS\Claude+Antigravity\03_Dynamic_Prompt_Library\index.html"
            master_hooks = r"F:\AI PROJECTS\Claude+Antigravity\03_Dynamic_Hooks_Library\index.html"
            master_hub = r"F:\AI PROJECTS\Claude+Antigravity\hub_portal.html"

            import shutil
            try:
                os.makedirs(os.path.dirname(g_prompt), exist_ok=True)
                os.makedirs(os.path.dirname(g_hooks), exist_ok=True)
                if not os.path.exists(g_prompt) and os.path.exists(master_prompt):
                    shutil.copy2(master_prompt, g_prompt)
                if not os.path.exists(g_hooks) and os.path.exists(master_hooks):
                    shutil.copy2(master_hooks, g_hooks)
                if os.path.exists(master_hub):
                    shutil.copy2(master_hub, hub_target)
            except Exception as e:
                print(f"Warning initializing Google Drive Autovem folder: {e}")

            if g_prompt not in html_targets:
                html_targets.append(g_prompt)
            if g_hooks not in html_targets:
                html_targets.append(g_hooks)

    with open(md_path, 'r', encoding='utf-8') as f:
        content = f.read()

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

    detail_blocks = re.findall(r'###\s*(\d+)\.\s*(.*?)\n(.*?)(?=(?:###\s*\d+|\Z))', content, re.DOTALL)
    details_dict = {}
    for num_str, title, block_text in detail_blocks:
        num = int(num_str)
        details_dict[num] = block_text.strip()

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

    for html_path in html_targets:
        if not os.path.exists(html_path):
            continue
            
        with open(html_path, 'r', encoding='utf-8') as f:
            html_content = f.read()

        start_marker = "let libraryData = {"
        start_idx = html_content.find(start_marker)
        if start_idx != -1:
            end_marker = "let activeTab ="
            end_idx = html_content.find(end_marker, start_idx)
            if end_idx != -1:
                new_html = html_content[:start_idx] + f"let libraryData = {json_str};\n        " + html_content[end_idx:]
                with open(html_path, 'w', encoding='utf-8') as f:
                    f.write(new_html)
                print(f"SUCCESS: Updated Dynamic Prompt Library at {html_path}")
            else:
                print(f"Warning: end_marker not found in {html_path}")
        else:
            print(f"Warning: start_marker not found in {html_path}")

if __name__ == "__main__":
    update_prompt_library()
