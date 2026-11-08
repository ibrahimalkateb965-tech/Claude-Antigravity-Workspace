import os
import shutil
import subprocess

SOURCE_BLIND_ROOT = r"f:\AI PROJECTS\Blind App"
SOURCE_BLIND_AGENTS = r"f:\AI PROJECTS\Blind App\.agents"
SOURCE_GLOBAL_ROOT = r"C:\Users\Kt\.gemini\config"
SOURCE_GLOBAL_SKILLS = os.path.join(SOURCE_GLOBAL_ROOT, "skills")
SOURCE_GLOBAL_SUBAGENTS = os.path.join(SOURCE_GLOBAL_ROOT, "Sub_Agent")
SOURCE_GLOBAL_PLUGINS = os.path.join(SOURCE_GLOBAL_ROOT, "plugins")
SOURCE_GLOBAL_REFERENCES = os.path.join(SOURCE_GLOBAL_ROOT, "references")

TARGET_WORKSPACE = r"f:\AI PROJECTS\Claude+Antigravity"
TARGET_AGENTS = os.path.join(TARGET_WORKSPACE, ".agents")
TARGET_SKILLS = os.path.join(TARGET_AGENTS, "skills")
TARGET_SUBAGENTS = os.path.join(TARGET_AGENTS, "Sub_Agent")
TARGET_PLUGINS = os.path.join(TARGET_WORKSPACE, "plugins")
TARGET_REFERENCES = os.path.join(TARGET_WORKSPACE, "references")

os.makedirs(TARGET_SKILLS, exist_ok=True)
os.makedirs(TARGET_SUBAGENTS, exist_ok=True)
os.makedirs(TARGET_PLUGINS, exist_ok=True)
os.makedirs(TARGET_REFERENCES, exist_ok=True)

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

if os.path.exists(SOURCE_GLOBAL_SUBAGENTS):
    for item in os.listdir(SOURCE_GLOBAL_SUBAGENTS):
        s_path = os.path.join(SOURCE_GLOBAL_SUBAGENTS, item)
        d_path = os.path.join(TARGET_SUBAGENTS, item)
        if os.path.isdir(s_path):
            shutil.copytree(s_path, d_path, dirs_exist_ok=True)
        else:
            shutil.copy2(s_path, d_path)

print(">>> 4. Syncing Global Plugins & Tools Ecosystem...")
if os.path.exists(SOURCE_GLOBAL_PLUGINS):
    for item in os.listdir(SOURCE_GLOBAL_PLUGINS):
        s_path = os.path.join(SOURCE_GLOBAL_PLUGINS, item)
        d_path = os.path.join(TARGET_PLUGINS, item)
        if os.path.isdir(s_path):
            shutil.copytree(s_path, d_path, dirs_exist_ok=True)
        else:
            shutil.copy2(s_path, d_path)

print(">>> 5. Syncing Global References & Seeds...")
if os.path.exists(SOURCE_GLOBAL_REFERENCES):
    for item in os.listdir(SOURCE_GLOBAL_REFERENCES):
        s_path = os.path.join(SOURCE_GLOBAL_REFERENCES, item)
        d_path = os.path.join(TARGET_REFERENCES, item)
        if os.path.isdir(s_path):
            shutil.copytree(s_path, d_path, dirs_exist_ok=True)
        else:
            shutil.copy2(s_path, d_path)

print(">>> 6. Syncing Core Guides & Global Configs...")
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

# Root-level docs & configs in TARGET_WORKSPACE
for root_doc in ["AGENTS.md", "HOOKS_GUIDE.xlsx", "sync_global_ecosystem.py"]:
    shutil.copy2(os.path.join(SOURCE_BLIND_AGENTS, root_doc), os.path.join(TARGET_WORKSPACE, root_doc))

if os.path.exists(os.path.join(SOURCE_BLIND_ROOT, "hooks_user_guide.md")):
    shutil.copy2(os.path.join(SOURCE_BLIND_ROOT, "hooks_user_guide.md"), os.path.join(TARGET_WORKSPACE, "hooks_user_guide.md"))

for global_cfg in ["mcp_config.json", "config.json", "ACTIVE_CONTEXT_INJECTION.md"]:
    src = os.path.join(SOURCE_GLOBAL_ROOT, global_cfg)
    if os.path.exists(src):
        shutil.copy2(src, os.path.join(TARGET_WORKSPACE, global_cfg))

print(">>> 7. Global Ecosystem Full Parity Sync Completed Successfully!")
