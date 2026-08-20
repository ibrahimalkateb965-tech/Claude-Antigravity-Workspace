"""
اختبارات التحقق الشاملة لمحرك التخصيص والتصدير الذكي والتنظيف
Comprehensive Test Suite for Project Agent Tailor & Pruner
"""

import os
import shutil
import json
import unittest
from project_agent_tailor import (
    detect_project_stacks,
    resolve_project_roster,
    tailor_project_environment,
    CORE_MANDATORY_AGENTS,
    CORE_MANDATORY_SKILLS
)

class TestProjectAgentTailor(unittest.TestCase):
    def setUp(self):
        self.test_dir = os.path.abspath(r"F:\AI PROJECTS\Claude+Antigravity\scratch\test_tailor_sandbox")
        if os.path.exists(self.test_dir):
            shutil.rmtree(self.test_dir)
        os.makedirs(self.test_dir, exist_ok=True)

    def tearDown(self):
        if os.path.exists(self.test_dir):
            shutil.rmtree(self.test_dir)

    def test_01_detect_android_stack(self):
        # محاكاة ملفات أندرويد
        with open(os.path.join(self.test_dir, "build.gradle.kts"), "w") as f:
            f.write("// Android build file")
        
        ctx_content = """# سياق المشروع
اسم المشروع: تطبيق تجريبي
التقنيات: تطبيق أندرويد باستخدام Jetpack Compose و Kotlin.
"""
        with open(os.path.join(self.test_dir, "PROJECT_CONTEXT.md"), "w", encoding="utf-8") as f:
            f.write(ctx_content)

        stacks = detect_project_stacks(self.test_dir)
        self.assertIn("android", stacks)

    def test_02_detect_quantity_surveying_stack(self):
        ctx_content = """# سياق المشروع
المشروع يختص بأعمال حصر الكميات والمخططات الإنشائية وتوليد جداول Primavera وعقود المقاولات.
"""
        with open(os.path.join(self.test_dir, "PROJECT_CONTEXT.md"), "w", encoding="utf-8") as f:
            f.write(ctx_content)

        stacks = detect_project_stacks(self.test_dir)
        self.assertIn("quantity_surveying_contracts", stacks)

    def test_03_tailor_export_and_pruning(self):
        # 1. إعداد مشروع أندرويد
        with open(os.path.join(self.test_dir, "build.gradle"), "w") as f:
            f.write("// Android build file")
        
        with open(os.path.join(self.test_dir, "PROJECT_CONTEXT.md"), "w", encoding="utf-8") as f:
            f.write("# سياق المشروع\nتطبيق القرآن الكريم بنظام أندرويد Jetpack Compose.")

        # 2. حقن مهارة ووكيل فائضين عمداً لاختبار الحذف التلقائي (Pruning)
        local_agents = os.path.join(self.test_dir, ".agents")
        local_skills = os.path.join(local_agents, "skills")
        local_subagents = os.path.join(local_agents, "Sub_Agent")
        os.makedirs(local_skills, exist_ok=True)
        os.makedirs(local_subagents, exist_ok=True)

        unwanted_skill = os.path.join(local_skills, "ad-creative-maker")
        os.makedirs(unwanted_skill, exist_ok=True)
        with open(os.path.join(unwanted_skill, "SKILL.md"), "w") as f:
            f.write("Unwanted ad skill")

        unwanted_agent = os.path.join(local_subagents, "campaign-runner.yaml")
        with open(unwanted_agent, "w") as f:
            f.write("name: campaign-runner")

        # 3. تشغيل محرك التخصيص
        manifest = tailor_project_environment(self.test_dir, verbose=False)

        # 4. التحقق من التصدير الصحيح لحزم النواة والأندرويد
        self.assertTrue(os.path.exists(os.path.join(local_subagents, "code-architect.yaml")))
        self.assertTrue(os.path.exists(os.path.join(local_subagents, "android-kotlin-pro.yaml")))
        self.assertTrue(os.path.exists(os.path.join(local_skills, "clean-code-guard")))
        self.assertTrue(os.path.exists(os.path.join(local_skills, "jetpack-compose-ui")))

        # 5. التحقق من نجاح التنظيف والحذف التلقائي (Pruning)
        self.assertFalse(os.path.exists(unwanted_skill), "فشل التنظيف: المهارة الزائدة لم تحذف!")
        self.assertFalse(os.path.exists(unwanted_agent), "فشل التنظيف: الوكيل الزائد لم يحذف!")
        self.assertIn("ad-creative-maker", manifest["pruned_skills"])
        self.assertIn("campaign-runner", manifest["pruned_agents"])

        # 6. التحقق من إنشاء المانيفست وسكربت المزامنة
        self.assertTrue(os.path.exists(os.path.join(local_agents, "agent_manifest.json")))
        self.assertTrue(os.path.exists(os.path.join(local_agents, "sync_local_agents.py")))

if __name__ == "__main__":
    unittest.main()
