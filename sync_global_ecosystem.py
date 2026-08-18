import os
import shutil
import subprocess

SOURCE_BLIND_AGENTS = r"f:\AI PROJECTS\Blind App\.agents"
SOURCE_GLOBAL_SKILLS = r"C:\Users\Kt\.gemini\config\skills"
SOURCE_GLOBAL_SUBAGENTS = r"C:\Users\Kt\.gemini\config\Sub_Agent"
TARGET_WORKSPACE = os.path.dirname(os.path.abspath(__file__))
TARGET_AGENTS = os.path.join(TARGET_WORKSPACE, ".agents")
TARGET_SKILLS = os.path.join(TARGET_AGENTS, "skills")
TARGET_SUBAGENTS = os.path.join(TARGET_AGENTS, "Sub_Agent")

os.makedirs(TARGET_SKILLS, exist_ok=True)
os.makedirs(TARGET_SUBAGENTS, exist_ok=True)

print(">>> 1. Syncing Global & Local Skills to Claude-Antigravity-Workspace...")
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

print(">>> 2. Syncing Sub-Agents...")
if os.path.exists(os.path.join(SOURCE_BLIND_AGENTS, "Sub_Agent")):
    for item in os.listdir(os.path.join(SOURCE_BLIND_AGENTS, "Sub_Agent")):
        s_path = os.path.join(SOURCE_BLIND_AGENTS, "Sub_Agent", item)
        d_path = os.path.join(TARGET_SUBAGENTS, item)
        if os.path.isdir(s_path):
            shutil.copytree(s_path, d_path, dirs_exist_ok=True)
        else:
            shutil.copy2(s_path, d_path)

print(">>> 3. Syncing Core Guides & Scripts...")
core_files = [
    "AGENTS.md",
    "HOOKS_GUIDE.md",
    "HOOKS_GUIDE.xlsx",
    "convert_hooks_to_sheets.py",
    "ACTIVE_CONTEXT_INJECTION.md",
    "MEMORY_EXPORT_PROTOCOL.md"
]

for f in core_files:
    src = os.path.join(SOURCE_BLIND_AGENTS, f)
    if os.path.exists(src):
        dst = os.path.join(TARGET_AGENTS, f)
        shutil.copy2(src, dst)
        print(f"  Copied {f} to {dst}")

# Also copy AGENTS.md and HOOKS_GUIDE.xlsx to root of TARGET_WORKSPACE for direct accessibility
shutil.copy2(os.path.join(SOURCE_BLIND_AGENTS, "AGENTS.md"), os.path.join(TARGET_WORKSPACE, "AGENTS.md"))
shutil.copy2(os.path.join(SOURCE_BLIND_AGENTS, "HOOKS_GUIDE.xlsx"), os.path.join(TARGET_WORKSPACE, "HOOKS_GUIDE.xlsx"))

print(">>> 4. Ecosystem Sync Completed Successfully!")
