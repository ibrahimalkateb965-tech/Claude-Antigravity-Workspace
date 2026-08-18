import os
import shutil
import subprocess

SOURCE_BLIND_ROOT = r"f:\AI PROJECTS\Blind App"
SOURCE_BLIND_AGENTS = r"f:\AI PROJECTS\Blind App\.agents"
SOURCE_GLOBAL_SKILLS = r"C:\Users\Kt\.gemini\config\skills"
SOURCE_GLOBAL_SUBAGENTS = r"C:\Users\Kt\.gemini\config\Sub_Agent"
TARGET_WORKSPACE = r"f:\AI PROJECTS\Claude+Antigravity"
TARGET_AGENTS = os.path.join(TARGET_WORKSPACE, ".agents")
TARGET_SKILLS = os.path.join(TARGET_AGENTS, "skills")
TARGET_SUBAGENTS = os.path.join(TARGET_AGENTS, "Sub_Agent")

os.makedirs(TARGET_SKILLS, exist_ok=True)
os.makedirs(TARGET_SUBAGENTS, exist_ok=True)

print(">>> 1. Updating Excel & Prompt Library Application...")
# 1. Update Excel
conv_script = os.path.join(SOURCE_BLIND_AGENTS, "convert_hooks_to_sheets.py")
if os.path.exists(conv_script):
    subprocess.run(["python", conv_script], cwd=SOURCE_BLIND_AGENTS, check=False)

# 2. Update Prompt Library HTML
html_script = os.path.join(SOURCE_BLIND_AGENTS, "update_html.py")
if os.path.exists(html_script):
    subprocess.run(["python", html_script], cwd=SOURCE_BLIND_AGENTS, check=False)

print(">>> 2. Syncing Global & Local Skills to Claude-Antigravity-Workspace...")
# Copy from global skills
if os.path.exists(SOURCE_GLOBAL_SKILLS):
    for item in os.listdir(SOURCE_GLOBAL_SKILLS):
        s_path = os.path.join(SOURCE_GLOBAL_SKILLS, item)
        d_path = os.path.join(TARGET_SKILLS, item)
        if os.path.isdir(s_path):
            shutil.copytree(s_path, d_path, dirs_exist_ok=True)
        else:
            shutil.copy2(s_path, d_path)

# Copy from Blind App skills
local_skills = os.path.join(SOURCE_BLIND_AGENTS, "skills")
if os.path.exists(local_skills):
    for item in os.listdir(local_skills):
        s_path = os.path.join(local_skills, item)
        d_path = os.path.join(TARGET_SKILLS, item)
        if os.path.isdir(s_path):
            shutil.copytree(s_path, d_path, dirs_exist_ok=True)
        else:
            shutil.copy2(s_path, d_path)

print(">>> 3. Syncing Sub-Agents...")
if os.path.exists(os.path.join(SOURCE_BLIND_AGENTS, "Sub_Agent")):
    for item in os.listdir(os.path.join(SOURCE_BLIND_AGENTS, "Sub_Agent")):
        s_path = os.path.join(SOURCE_BLIND_AGENTS, "Sub_Agent", item)
        d_path = os.path.join(TARGET_SUBAGENTS, item)
        if os.path.isdir(s_path):
            shutil.copytree(s_path, d_path, dirs_exist_ok=True)
        else:
            shutil.copy2(s_path, d_path)

print(">>> 4. Syncing Core Guides & Scripts...")
core_files = [
    "AGENTS.md",
    "HOOKS_GUIDE.md",
    "HOOKS_GUIDE.xlsx",
    "convert_hooks_to_sheets.py",
    "update_html.py",
    "update_models_matrix.py",
    "sync_global_ecosystem.py",
    "ACTIVE_CONTEXT_INJECTION.md",
    "MEMORY_EXPORT_PROTOCOL.md"
]

for f in core_files:
    src = os.path.join(SOURCE_BLIND_AGENTS, f)
    if os.path.exists(src):
        dst = os.path.join(TARGET_AGENTS, f)
        shutil.copy2(src, dst)

# Root-level docs
for root_doc in ["AGENTS.md", "HOOKS_GUIDE.xlsx", "sync_global_ecosystem.py"]:
    shutil.copy2(os.path.join(SOURCE_BLIND_AGENTS, root_doc), os.path.join(TARGET_WORKSPACE, root_doc))

if os.path.exists(os.path.join(SOURCE_BLIND_ROOT, "hooks_user_guide.md")):
    shutil.copy2(os.path.join(SOURCE_BLIND_ROOT, "hooks_user_guide.md"), os.path.join(TARGET_WORKSPACE, "hooks_user_guide.md"))

print(">>> 5. Ecosystem Sync Completed Successfully!")
