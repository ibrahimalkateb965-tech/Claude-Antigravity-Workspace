import os
import re
import sys

def audit_hooks():
    base_dir = os.path.dirname(os.path.abspath(__file__))
    md_path = os.path.join(base_dir, "HOOKS_GUIDE.md")
    
    if not os.path.exists(md_path):
        print(f"ERROR: {md_path} not found.")
        sys.exit(1)

    with open(md_path, "r", encoding="utf-8") as f:
        content = f.read()

    # Parse table rows
    hooks = []
    for line in content.split("\n"):
        if line.strip().startswith("| **"):
            parts = [p.strip() for p in line.split("|")[1:-1]]
            if len(parts) >= 5:
                raw_name = parts[0].replace("**", "")
                agents = [a.strip("` ") for a in parts[1].split(",") if a.strip("` ")]
                model = parts[2].replace("<br>", " ")
                triggers = parts[3].replace('"', '').replace('`', '')
                goal = parts[4]

                m = re.match(r"(\d+)\.", raw_name)
                idx = int(m.group(1)) if m else len(hooks) + 1

                hooks.append({
                    "id": idx,
                    "name": raw_name,
                    "agents": agents,
                    "model": model,
                    "triggers": triggers,
                    "goal": goal
                })

    print(f"==================================================")
    print(f"🔍 AUTOVEMTECH MASTER HOOKS AUDIT (Total: {len(hooks)})")
    print(f"==================================================")

    # Check skills existence
    skills_dirs = [
        os.path.join(base_dir, ".agents", "skills"),
        os.path.expanduser(r"~/.gemini/config/skills"),
        r"C:\Users\Kt\.gemini\config\skills"
    ]

    report_rows = []
    healthy_count = 0
    warning_count = 0

    for h in hooks:
        verified_skills = []
        missing_skills = []

        for agent in h["agents"]:
            clean_agent = agent.strip("[]")
            found = False
            for s_dir in skills_dirs:
                skill_path = os.path.join(s_dir, clean_agent, "SKILL.md")
                if os.path.exists(skill_path):
                    found = True
                    break
            if found:
                verified_skills.append(clean_agent)
            else:
                missing_skills.append(clean_agent)

        status = "✅ HEALTHY"
        issues = "None"
        if missing_skills:
            status = "⚠️ WARNING"
            warning_count += 1
            issues = f"Missing local skill: {', '.join(missing_skills)}"
        else:
            healthy_count += 1

        report_rows.append({
            "id": h["id"],
            "name": h["name"],
            "status": status,
            "skills": ", ".join(verified_skills) or "None",
            "trigger": h["triggers"][:40] + ("..." if len(h["triggers"]) > 40 else ""),
            "model": h["model"].split("\n")[0][:30],
            "issues": issues
        })
        print(f"[{status}] Hook {h['id']:02d}: {h['name'][:40]} | Skills: {len(verified_skills)} verified")

    # Generate Report Artifact
    report_md_path = os.path.join(base_dir, ".agents", "HOOKS_AUDIT_REPORT.md")
    os.makedirs(os.path.dirname(report_md_path), exist_ok=True)

    with open(report_md_path, "w", encoding="utf-8") as rf:
        rf.write("# 🛡️ AUTOVEMTECH MASTER HOOKS AUDIT REPORT\n\n")
        rf.write(f"- **Total Hooks Registered:** {len(hooks)}\n")
        rf.write(f"- **Highest Registered Hook:** Hook {hooks[-1]['id']} ({hooks[-1]['name']})\n")
        rf.write(f"- **Healthy Hooks:** {healthy_count}\n")
        rf.write(f"- **Hooks with Warnings:** {warning_count}\n\n")
        rf.write("## 📋 Hook Verification Scorecard\n\n")
        rf.write("| Hook # | Status | Verified Skills | Sample Trigger | Issues / Action Items |\n")
        rf.write("| :--- | :---: | :--- | :--- | :--- |\n")
        for r in report_rows:
            rf.write(f"| **Hook {r['id']:02d}: {r['name']}** | {r['status']} | `{r['skills']}` | `{r['trigger']}` | {r['issues']} |\n")

    print(f"\n✅ Audit complete! Report written to: {report_md_path}")
    print(f"Ceiling Verified: Hook {hooks[-1]['id']} (Highest Registered Operational Hook)")

if __name__ == "__main__":
    audit_hooks()
