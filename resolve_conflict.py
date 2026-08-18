import os
import shutil
import subprocess

src_agents = r"f:\AI PROJECTS\Blind App\.agents\AGENTS.md"
src_hooks = r"f:\AI PROJECTS\Blind App\.agents\HOOKS_GUIDE.md"
dst_dir = r"f:\AI PROJECTS\Claude+Antigravity\.agents"

shutil.copy2(src_agents, os.path.join(dst_dir, "AGENTS.md"))
shutil.copy2(src_hooks, os.path.join(dst_dir, "HOOKS_GUIDE.md"))

print("Resolved files by overwriting with latest master copies.")
