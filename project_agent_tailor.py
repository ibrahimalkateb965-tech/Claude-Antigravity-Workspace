"""
محرك التخصيص والتصدير الذكي للوكلاء والمهارات للمشاريع المحلية
Project Agent & Skill Tailor Engine

يقوم هذا المحرك بتحليل سياق المشروع المعتمد (PROJECT_CONTEXT.md) والملفات المحيطة،
وتحديد وتصدير طاقم الوكلاء والمهارات المتخصصة فقط إلى مجلد المشروع (.agents/)،
مع تنظيف أي مهارات أو وكلاء فائضين (Pruning)، وتوفير سكربت المزامنة المحلي.
"""

import os
import shutil
import json
import re
import argparse
import sys
import codecs
from typing import Dict, List, Set, Tuple

if hasattr(sys.stdout, 'encoding') and sys.stdout.encoding.lower() != 'utf-8':
    sys.stdout = codecs.getwriter('utf-8')(sys.stdout.detach())


# المسارات المركزية
CENTRAL_WORKSPACE = r"F:\AI PROJECTS\Claude+Antigravity"
CENTRAL_AGENTS = os.path.join(CENTRAL_WORKSPACE, ".agents")
CENTRAL_SKILLS = os.path.join(CENTRAL_AGENTS, "skills")
CENTRAL_SUBAGENTS = os.path.join(CENTRAL_AGENTS, "Sub_Agent")
SYNC_TEMPLATE_PATH = os.path.join(os.path.dirname(__file__), "sync_local_agents_template.py")
if not os.path.exists(SYNC_TEMPLATE_PATH):
    SYNC_TEMPLATE_PATH = os.path.join(CENTRAL_WORKSPACE, "sync_local_agents_template.py")

# 1. حزمة النواة الإلزامية لجميع المشاريع
CORE_MANDATORY_AGENTS = [
    "prompt-engineer",
    "code-architect",
    "code-reviewer-quality",
    "debugger",
    "git-github-manager",
    "persistent-memory-engine",
    "agent-optimizer"
]

CORE_MANDATORY_SKILLS = [
    "clean-code-guard",
    "test-guard",
    "docs-guard",
    "security-auditor",
    "writing-plans",
    "executing-plans",
    "systematic-debugging",
    "persistent-memory-engine",
    "fleet-orchestrator"
]

# 2. حزم التخصصات البرمجية والمهنية
STACK_BUNDLES: Dict[str, Dict[str, List[str]]] = {
    "android": {
        "name": "تطوير أندرويد و Jetpack Compose",
        "keywords": ["android", "kotlin", "compose", "jetpack", "room", "hilt", "gradle", "talkback", "أندرويد"],
        "agents": [
            "android-kotlin-pro",
            "jetpack-compose-ui",
            "android-testing",
            "offline-sync-db",
            "ui-ux-design-lead",
            "performance-optimizer"
        ],
        "skills": [
            "android-kotlin-pro",
            "jetpack-compose-ui",
            "android-testing",
            "offline-sync-db",
            "ui-ux-design-lead",
            "motion-transitions-pro",
            "apple-design",
            "improve-animations",
            "animate",
            "ask-sonner"
        ]
    },
    "web": {
        "name": "تطوير تطبيقات الويب و Full-Stack",
        "keywords": ["web", "react", "next.js", "vue", "node", "typescript", "javascript", "tailwind", "html", "css", "ويب", "واجهة"],
        "agents": [
            "backend-architect",
            "ux-product-designer",
            "devops-deployer",
            "vibe-coder"
        ],
        "skills": [
            "frontend-design-builder",
            "ui-ux-design-lead",
            "backend-architect",
            "webapp-qa-tester",
            "vibe-coder",
            "emil-design-eng",
            "apple-design",
            "web-artifacts-prototyper",
            "pick-ui-library"
        ]
    },
    "python_ai": {
        "name": "ذكاء اصطناعي وأتمتة بايثون و MCP",
        "keywords": ["python", "ai", "mcp", "fastapi", "django", "llm", "automation", "بايثون", "ذكاء اصطناعي"],
        "agents": [
            "ai-engineer",
            "backend-architect",
            "devops-deployer"
        ],
        "skills": [
            "ai-engineer",
            "backend-architect",
            "mcp-tool-builder",
            "excel-data-analyst",
            "uv",
            "script-hook-generator"
        ]
    },
    "quantity_surveying_contracts": {
        "name": "حصر الكميات والعقود والمستندات الهندسية",
        "keywords": ["حصر", "كميات", "عقود", "خزان", "أسقف", "مخططات", "primavera", "docx", "excel", "takeoff", "quantity surveying"],
        "agents": [
            "documentation-expert",
            "linguistic-assistant"
        ],
        "skills": [
            "arabic-docx-specialist",
            "excel-data-analyst",
            "contract-reviewer",
            "compliance-officer",
            "nda-triage"
        ]
    },
    "business_finance": {
        "name": "إدارة الأعمال والعمليات المالية والضرائب",
        "keywords": ["مالية", "محاسبة", "ضرائب", "فواتير", "أرباح", "finance", "tax", "invoice", "cash flow", "margin"],
        "agents": [
            "documentation-expert"
        ],
        "skills": [
            "cash-flow-watcher",
            "invoice-chaser",
            "margin-analyst",
            "tax-prepper",
            "financial-statements-builder",
            "variance-analyst",
            "close-management-lead"
        ]
    },
    "odoo": {
        "name": "أنظمة تخطيط الموارد Odoo ERP",
        "keywords": ["odoo", "أودو", "erp", "__manifest__.py"],
        "agents": [
            "backend-architect",
            "code-reviewer-feature-dev"
        ],
        "skills": [
            "backend-architect",
            "excel-data-analyst"
        ]
    },
    "civil_contracting_planning": {
        "name": "هندسة التخطيط والمقاولات العامة (BOQ, WBS, DOCX)",
        "keywords": ["مقاولات", "إنشاء", "ترميم", "سفلتة", "بلديات", "طرق", "تعليم", "مدرسة", "تطوير للمباني", "tbc", "منهجية", "جدول زمني", "مخاطر", "سلامة", "hse", "كود سعودي", "boq", "wbs", "إعمار الفرعة"],
        "agents": [
            "planning-engineer",
            "schedule-builder",
            "methodology-writer",
            "boq-analyst",
            "risk-planner",
            "saudi-code-validator",
            "document-formatter",
            "monorepo-architect",
            "windows-file-organizer",
            "documentation-expert",
            "linguistic-assistant"
        ],
        "skills": [
            "boq-generation",
            "methodology-generation",
            "schedule-generation",
            "document-formatting",
            "references",
            "arabic-docx-specialist",
            "excel-data-analyst",
            "contract-reviewer",
            "compliance-officer",
            "self-refinement-engine"
        ]
    },
    "education_presentations": {
        "name": "صناعة المحتوى التعليمي والعروض التقديمية واللغات (ESL & Presentations)",
        "keywords": ["تعليم", "محتوى", "عروض", "عرض", "شرائح", "إنجليزي", "لغة", "طفل", "تأسيس", "قراءة", "محادثة", "education", "english", "tutor", "presentation", "slides", "lesson", "phonics", "grammar", "vocabulary", "pedagogy"],
        "agents": [
            "documentation-expert",
            "linguistic-assistant",
            "ux-product-designer",
            "prompt-engineer",
            "vibe-coder"
        ],
        "skills": [
            "linguistic-assistant",
            "documentation-expert",
            "ui-ux-design-lead",
            "taste-design-critic",
            "frontend-design-builder",
            "web-artifacts-prototyper",
            "motion-transitions-pro",
            "script-hook-generator",
            "post-content-writer",
            "copywriting-lead",
            "arabic-docx-specialist",
            "self-refinement-engine"
        ]
    },
    "digital_marketing_growth": {
        "name": "التسويق الرقمي وحملات إعلانات جوجل والنمو (Google Ads & Growth)",
        "keywords": ["تسويق", "إعلانات", "جوجل", "حملات", "marketing", "google ads", "ads", "seo", "geo", "cro", "campaign", "leads", "copywriting", "analytics", "شجن", "حاويات", "autovemtech", "عميل", "clients"],
        "agents": [
            "documentation-expert",
            "linguistic-assistant",
            "ux-product-designer",
            "prompt-engineer"
        ],
        "skills": [
            "campaign-runner",
            "ad-creative-maker",
            "copywriting-lead",
            "ai-geo-seo-optimizer",
            "cro-conversion-lead",
            "customer-research-voice",
            "brand-kit-keeper",
            "post-content-writer",
            "script-hook-generator",
            "profile-optimizer",
            "excel-data-analyst",
            "arabic-docx-specialist",
            "self-refinement-engine",
            "web-artifacts-prototyper",
            "frontend-design-builder",
            "ui-ux-design-lead",
            "cash-flow-watcher",
            "invoice-chaser",
            "margin-analyst"
        ]
    }
}

def detect_project_stacks(project_path: str) -> List[str]:
    """
    تحليل سياق وملفات المشروع لتحديد الحزم البرمجية والمهنية المناسبة
    """
    matched_stacks: Set[str] = set()
    
    # 1. فحص الملفات الفيزيائية
    try:
        files = [f.lower() for f in os.listdir(project_path)]
    except Exception:
        files = []

    if any(f in files for f in ['build.gradle', 'build.gradle.kts', 'settings.gradle', 'app']):
        matched_stacks.add("android")
    if any(f in files for f in ['package.json', 'node_modules', 'vite.config.js', 'next.config.js']):
        matched_stacks.add("web")
    if any(f.endswith('.py') for f in files) or 'requirements.txt' in files or 'pyproject.toml' in files:
        matched_stacks.add("python_ai")
    if '__manifest__.py' in files or 'odoo' in files:
        matched_stacks.add("odoo")

    # 2. فحص محتوى PROJECT_CONTEXT.md
    context_files = [
        os.path.join(project_path, "PROJECT_CONTEXT.md"),
        os.path.join(project_path, ".agents", "PROJECT_CONTEXT.md")
    ]
    
    context_text = ""
    for ctx_file in context_files:
        if os.path.exists(ctx_file):
            try:
                with open(ctx_file, "r", encoding="utf-8") as f:
                    context_text += " " + f.read().lower()
            except Exception:
                pass

    if context_text:
        for stack_key, bundle in STACK_BUNDLES.items():
            for kw in bundle["keywords"]:
                if re.search(r'\b' + re.escape(kw.lower()) + r'\b', context_text):
                    matched_stacks.add(stack_key)
                    break

    # إذا لم يتم اكتشاف أي تخصص، يتم اعتماد الويب أو البايثون كخيار عام
    if not matched_stacks:
        matched_stacks.add("web")

    return sorted(list(matched_stacks))

def resolve_project_roster(stacks: List[str]) -> Tuple[List[str], List[str]]:
    """
    تجميع الوكلاء والمهارات المطلوبة بناءً على حزمة النواة والحزم المتخصصة
    """
    agents: Set[str] = set(CORE_MANDATORY_AGENTS)
    skills: Set[str] = set(CORE_MANDATORY_SKILLS)

    for stack in stacks:
        if stack in STACK_BUNDLES:
            bundle = STACK_BUNDLES[stack]
            agents.update(bundle.get("agents", []))
            skills.update(bundle.get("skills", []))

    return sorted(list(agents)), sorted(list(skills))

def tailor_project_environment(project_path: str, verbose: bool = True) -> Dict[str, any]:
    """
    تخصيص وتصدير بيئة الوكلاء والمهارات للمشروع مع التنظيف والمزامنة
    """
    project_name = os.path.basename(os.path.abspath(project_path))
    if verbose:
        print(f"\n=======================================================")
        print(f"🚀 بدء تخصيص بيئة الوكلاء للمشروع: [{project_name}]")
        print(f"📁 المسار: {project_path}")
        print(f"=======================================================")

    stacks = detect_project_stacks(project_path)
    stack_names = [STACK_BUNDLES[s]["name"] for s in stacks if s in STACK_BUNDLES]
    
    if verbose:
        print(f"🔍 التخصصات المكتشفة: {', '.join(stack_names)}")

    target_agents, target_skills = resolve_project_roster(stacks)

    local_agents_dir = os.path.join(project_path, ".agents")
    local_subagents_dir = os.path.join(local_agents_dir, "Sub_Agent")
    local_skills_dir = os.path.join(local_agents_dir, "skills")

    os.makedirs(local_subagents_dir, exist_ok=True)
    os.makedirs(local_skills_dir, exist_ok=True)

    # 1. تصدير الوكلاء
    copied_agents = 0
    candidate_subagent_sources = [
        CENTRAL_SUBAGENTS,
        r"C:\Users\Kt\.gemini\config\Sub_Agent",
        local_subagents_dir
    ]
    for agent in target_agents:
        dst_file = os.path.join(local_subagents_dir, f"{agent}.yaml")
        for src_dir in candidate_subagent_sources:
            src_file = os.path.join(src_dir, f"{agent}.yaml")
            if os.path.exists(src_file):
                if os.path.abspath(src_file) != os.path.abspath(dst_file):
                    shutil.copy2(src_file, dst_file)
                copied_agents += 1
                # مزامنة عكسية للمجلد المركزي إذا لم يكن موجوداً
                central_dst = os.path.join(r"C:\Users\Kt\.gemini\config\Sub_Agent", f"{agent}.yaml")
                if not os.path.exists(central_dst) and os.path.exists(src_file):
                    try:
                        shutil.copy2(src_file, central_dst)
                    except Exception:
                        pass
                break

    # نسخ ملفات الوكلاء المساندة
    for helper in ["sub_agents.yaml", "system_prompt.md"]:
        for src_dir in candidate_subagent_sources:
            src = os.path.join(src_dir, helper)
            dst = os.path.join(local_subagents_dir, helper)
            if os.path.exists(src) and os.path.abspath(src) != os.path.abspath(dst):
                shutil.copy2(src, dst)
                break

    # 2. تصدير المهارات
    copied_skills = 0
    candidate_skill_sources = [
        CENTRAL_SKILLS,
        r"C:\Users\Kt\.gemini\config\skills",
        local_skills_dir
    ]
    for skill in target_skills:
        dst_dir = os.path.join(local_skills_dir, skill)
        for src_parent in candidate_skill_sources:
            src_dir = os.path.join(src_parent, skill)
            if os.path.exists(src_dir) and os.path.isdir(src_dir):
                if os.path.abspath(src_dir) != os.path.abspath(dst_dir):
                    shutil.copytree(src_dir, dst_dir, dirs_exist_ok=True)
                copied_skills += 1
                # مزامنة عكسية لـ config
                central_skill_dst = os.path.join(r"C:\Users\Kt\.gemini\config\skills", skill)
                if not os.path.exists(central_skill_dst) and os.path.exists(src_dir):
                    try:
                        shutil.copytree(src_dir, central_skill_dst, dirs_exist_ok=True)
                    except Exception:
                        pass
                break

    # 3. عملية التنظيف (Pruning) للمهارات والوكلاء الفائضين
    pruned_skills = []
    if os.path.exists(local_skills_dir):
        for existing in os.listdir(local_skills_dir):
            skill_dir = os.path.join(local_skills_dir, existing)
            if os.path.isdir(skill_dir) and existing not in target_skills and not existing.startswith("custom-"):
                shutil.rmtree(skill_dir)
                pruned_skills.append(existing)

    pruned_agents = []
    if os.path.exists(local_subagents_dir):
        for existing in os.listdir(local_subagents_dir):
            if existing.endswith(".yaml") and existing not in ["sub_agents.yaml"]:
                agent_name = existing[:-5]
                if agent_name not in target_agents and not agent_name.startswith("custom-"):
                    os.remove(os.path.join(local_subagents_dir, existing))
                    pruned_agents.append(agent_name)

    # 4. كتابة وثيقة التعريف (agent_manifest.json)
    manifest = {
        "project_name": project_name,
        "stacks": stacks,
        "stack_names": stack_names,
        "agents": target_agents,
        "skills": target_skills,
        "pruned_skills": pruned_skills,
        "pruned_agents": pruned_agents
    }
    
    manifest_path = os.path.join(local_agents_dir, "agent_manifest.json")
    with open(manifest_path, "w", encoding="utf-8") as f:
        json.dump(manifest, f, ensure_ascii=False, indent=2)

    # 5. توليد/نسخ سكربت المزامنة المحلي
    local_sync_script = os.path.join(local_agents_dir, "sync_local_agents.py")
    if os.path.exists(SYNC_TEMPLATE_PATH):
        shutil.copy2(SYNC_TEMPLATE_PATH, local_sync_script)

    # 6. تصدير قوالب وإعدادات الأسطول التشاركي (Fleet Templates & Config) تلقائياً
    fleet_templates_src = os.path.join(os.path.dirname(__file__), "fleet_templates")
    fleet_seed_src = os.path.join(os.path.dirname(__file__), "fleet_config_seed.json")
    
    local_fleet_templates = os.path.join(project_path, "fleet_templates")
    local_fleet_config = os.path.join(project_path, "fleet_config.json")
    local_agents_fleet_templates = os.path.join(local_agents_dir, "fleet_templates")
    
    if os.path.exists(fleet_templates_src):
        if not os.path.exists(local_fleet_templates):
            shutil.copytree(fleet_templates_src, local_fleet_templates, dirs_exist_ok=True)
        if not os.path.exists(local_agents_fleet_templates):
            shutil.copytree(fleet_templates_src, local_agents_fleet_templates, dirs_exist_ok=True)
            
    if os.path.exists(fleet_seed_src):
        if not os.path.exists(local_fleet_config):
            shutil.copy2(fleet_seed_src, local_fleet_config)
        local_agents_seed = os.path.join(local_agents_dir, "fleet_config_seed.json")
        if not os.path.exists(local_agents_seed):
            shutil.copy2(fleet_seed_src, local_agents_seed)

    if verbose:
        print(f"\n✨ اكتمل تخصيص وتصدير البيئة بنجاح:")
        print(f"   - تم تصدير {copied_agents} وكيلاً متخصصاً.")
        print(f"   - تم تصدير {copied_skills} مهارة موجهة.")
        if pruned_agents:
            print(f"   - 🧹 تم تنظيف {len(pruned_agents)} وكيلاً فائضاً: {pruned_agents}")
        if pruned_skills:
            print(f"   - 🧹 تم تنظيف {len(pruned_skills)} مهارة فائضة: {pruned_skills}")
        print(f"   - 📄 تم إنشاء ملف التعريف: {manifest_path}")
        print(f"   - 🔄 تم توفير سكربت المزامنة المحلي: {local_sync_script}")
        print(f"   - 🚀 تم توفير وضبط قوالب الأسطول التشاركي: {local_fleet_templates}")

    return manifest

def scan_and_tailor_all(base_dir: str = r"F:\AI PROJECTS"):
    """
    فحص وتخصيص كافة المشاريع الموجودة في بيئة العمل
    """
    print(f"🌐 بدء فحص وتخصيص كافة مشاريع البيئة في: {base_dir}")
    if not os.path.exists(base_dir):
        print(f"❌ المسار غير موجود: {base_dir}")
        return

    subdirs = [os.path.join(base_dir, d) for d in os.listdir(base_dir) if os.path.isdir(os.path.join(base_dir, d))]
    
    for proj_dir in subdirs:
        # استبعاد المجلد المركزي نفسه ومجلدات الأدوات العامة
        proj_name = os.path.basename(proj_dir)
        if proj_name in ["Claude+Antigravity", ".git", "archive"]:
            continue
        try:
            tailor_project_environment(proj_dir)
        except Exception as e:
            print(f"⚠️ خطأ أثناء تخصيص المشروع {proj_name}: {e}")

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="محرك تخصيص وتصدير وكلاء ومهارات المشاريع")
    parser.add_argument("project_path", nargs="?", default=None, help="مسار المشروع المستهدف")
    parser.add_argument("--all", action="store_true", help="فحص وتخصيص كافة مشاريع البيئة")
    args = parser.parse_args()

    if args.all:
        scan_and_tailor_all()
    elif args.project_path:
        tailor_project_environment(args.project_path)
    else:
        print("💡 استخدم: python project_agent_tailor.py <مسار_المشروع> أو python project_agent_tailor.py --all")
