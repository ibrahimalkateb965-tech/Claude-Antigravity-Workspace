"""
سكربت المزامنة المحلي لبيئة وكلاء ومهارات المشروع
Local Project Agent & Skill Sync Script
"""
import os
import shutil
import json

# المسار المركزي للمنظومة
CENTRAL_WORKSPACE = r"F:\AI PROJECTS\Claude+Antigravity"
CENTRAL_AGENTS = os.path.join(CENTRAL_WORKSPACE, ".agents")
CENTRAL_SKILLS = os.path.join(CENTRAL_AGENTS, "skills")
CENTRAL_SUBAGENTS = os.path.join(CENTRAL_AGENTS, "Sub_Agent")

# مسار المشروع الحالي
PROJECT_ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), ".."))
LOCAL_AGENTS = os.path.join(PROJECT_ROOT, ".agents")
LOCAL_SKILLS = os.path.join(LOCAL_AGENTS, "skills")
LOCAL_SUBAGENTS = os.path.join(LOCAL_AGENTS, "Sub_Agent")
MANIFEST_FILE = os.path.join(LOCAL_AGENTS, "agent_manifest.json")

def sync_project_agents():
    print(f"🔄 جاري مزامنة وكلاء ومهارات المشروع: {os.path.basename(PROJECT_ROOT)}")
    
    if not os.path.exists(MANIFEST_FILE):
        print("⚠️ لم يتم العثور على ملف تعريف الوكلاء (agent_manifest.json). جاري استدعاء محرك التخصيص العام...")
        try:
            import sys
            sys.path.insert(0, CENTRAL_WORKSPACE)
            from project_agent_tailor import tailor_project_environment
            tailor_project_environment(PROJECT_ROOT)
            return
        except Exception as e:
            print(f"❌ تعذر استدعاء محرك التخصيص: {e}")
            return

    with open(MANIFEST_FILE, "r", encoding="utf-8") as f:
        manifest = json.load(f)

    target_agents = set(manifest.get("agents", []))
    target_skills = set(manifest.get("skills", []))

    os.makedirs(LOCAL_SKILLS, exist_ok=True)
    os.makedirs(LOCAL_SUBAGENTS, exist_ok=True)

    # 1. مزامنة الوكلاء
    synced_agents = 0
    for agent in target_agents:
        src_yaml = os.path.join(CENTRAL_SUBAGENTS, f"{agent}.yaml")
        dst_yaml = os.path.join(LOCAL_SUBAGENTS, f"{agent}.yaml")
        if os.path.exists(src_yaml):
            shutil.copy2(src_yaml, dst_yaml)
            synced_agents += 1
            
    # نسخ الملفات المساندة للوكلاء إن وجدت
    for helper_file in ["sub_agents.yaml", "system_prompt.md"]:
        src = os.path.join(CENTRAL_SUBAGENTS, helper_file)
        if os.path.exists(src):
            shutil.copy2(src, os.path.join(LOCAL_SUBAGENTS, helper_file))

    # 2. مزامنة المهارات
    synced_skills = 0
    for skill in target_skills:
        src_dir = os.path.join(CENTRAL_SKILLS, skill)
        dst_dir = os.path.join(LOCAL_SKILLS, skill)
        if os.path.exists(src_dir) and os.path.isdir(src_dir):
            shutil.copytree(src_dir, dst_dir, dirs_exist_ok=True)
            synced_skills += 1

    # 3. تنظيف أي مهارات أو وكلاء فائضين
    pruned_skills = 0
    if os.path.exists(LOCAL_SKILLS):
        for existing in os.listdir(LOCAL_SKILLS):
            skill_path = os.path.join(LOCAL_SKILLS, existing)
            if os.path.isdir(skill_path) and existing not in target_skills and not existing.startswith("custom-"):
                shutil.rmtree(skill_path)
                pruned_skills += 1

    pruned_agents = 0
    if os.path.exists(LOCAL_SUBAGENTS):
        for existing in os.listdir(LOCAL_SUBAGENTS):
            if existing.endswith(".yaml") and existing not in ["sub_agents.yaml"]:
                agent_name = existing[:-5]
                if agent_name not in target_agents and not agent_name.startswith("custom-"):
                    os.remove(os.path.join(LOCAL_SUBAGENTS, existing))
                    pruned_agents += 1

    print(f"✅ تمت المزامنة بنجاح!")
    print(f"   - تم تحديث {synced_agents} وكيلاً، و {synced_skills} مهارة.")
    if pruned_skills > 0 or pruned_agents > 0:
        print(f"   - تم تنظيف {pruned_agents} وكيلاً زائداً و {pruned_skills} مهارة زائدة.")

if __name__ == "__main__":
    sync_project_agents()
