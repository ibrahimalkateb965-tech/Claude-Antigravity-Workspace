#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Multi-CLI Fleet Teamwork Preview Orchestrator (Hook 31)
======================================================
Reverse-engineered from Antigravity's /teamwork-preview command using Al Mustafa REA principles.
Coordinates the distributed Multi-CLI Fleet (Claude Code CLI, OpenCode CLI, Antigravity CLI,
Cline CLI, and Local Gemini Quad Fleet) to execute parallel Survey, Implementation, Adversarial
Review, Empirical Stress Testing, and Forensic Architectural Sign-off.

Enforces:
- [C-21] Exclusive Testing Monopoly for Claude Code CLI.
- [C-77] Distributed Fleet Delegation replacing single-harness token burn.
- [C-78] OpenCode batch sizing (max 4 files per batch).
- [C-96] Hybrid Autonomous Browser QA via Antigravity agcli.
- [C-100] Multi-CLI Fleet Teamwork Preview Standard.

Security & Concurrency Invariants:
- Atomic File Locking: All ledger writes are guarded by advisory OS locks (fcntl.flock).
- Structural Positional Table Parsing: Data rows are parsed strictly based on position after
  the Markdown separator, completely neutralizing cell-content injection attacks (D18).
- Milestone M4 Access Control: M4 is strictly locked; only Claude Code CLI acting as
  sole_testing_authority may write to Milestone M4 (D19).
- Upstream Dependency Gate: Milestone M4 cannot certify PASS unless all upstream milestones
  (M1, M2, M3) are in a verified PASS state (D20).

Author: Autovemtech AI Systems Pillar (Pillar 2)
"""

import argparse
import fcntl
import json
import re
import sys
from datetime import datetime, timezone
from pathlib import Path


PROJECT_ROOT = Path(__file__).resolve().parent.parent
DEFAULT_TEAMWORK_DIR = Path(".agents/fleet_teamwork")


def get_timestamp() -> str:
    return datetime.now(timezone.utc).strftime("%Y-%m-%dT%H:%M:%SZ")


def ensure_dir(path: Path) -> Path:
    path.mkdir(parents=True, exist_ok=True)
    return path


def resolve_teamwork_dir(custom_workspace: str | Path | None = None) -> Path:
    """Resolve teamwork workspace directory relative to PROJECT_ROOT (D8)."""
    if custom_workspace:
        p = Path(custom_workspace)
        return p if p.is_absolute() else (PROJECT_ROOT / p).resolve()
    return (PROJECT_ROOT / DEFAULT_TEAMWORK_DIR).resolve()


def normalize_identifier(s: str) -> str:
    s = s.lower()
    s = re.sub(r"\(.*?\)", "", s)
    s = re.sub(r"[\-_\s]+", " ", s).strip()
    return s


def roles_match(r1: str, r2: str) -> bool:
    """Robust canonical role matching supporting natural language and CLI slugs."""
    n1 = normalize_identifier(r1)
    n2 = normalize_identifier(r2)
    if n1 == n2:
        return True
    role_aliases = {
        "sole testing authority": {"sole testing authority", "auditor", "auditor claude", "testing authority", "qa auditor"},
        "explorer frontend": {"explorer frontend", "frontend explorer"},
        "explorer backend": {"explorer backend", "backend explorer"},
        "explorer domain": {"explorer domain", "domain explorer"},
        "backend & db migrations": {"backend & db migrations", "backend and db migrations", "worker backend", "backend worker"},
        "ui & presentation": {"ui & presentation", "ui and presentation", "worker ui", "ui worker"},
        "terminal scaffolding": {"terminal scaffolding", "worker scaffold", "scaffold worker"},
        "reviewer architecture": {"reviewer architecture", "architecture reviewer"},
        "reviewer standards": {"reviewer standards", "standards reviewer"},
        "challenger stress": {"challenger stress", "stress challenger"},
        "challenger browser qa": {"challenger browser qa", "browser qa challenger", "qa challenger", "browser qa"},
    }
    for canon, aliases in role_aliases.items():
        if (n1 == canon or n1 in aliases) and (n2 == canon or n2 in aliases):
            return True
    return False


def agents_match(a1: str, a2: str) -> bool:
    """Agent identity matching for authorization guards."""
    n1 = normalize_identifier(a1)
    n2 = normalize_identifier(a2)
    if n1 == n2:
        return True
    agent_aliases = {
        "claude code cli": {"claude code cli", "claude code", "claude", "auditor claude", "auditor_claude", "claude opus"},
        "antigravity cli": {"antigravity cli", "agcli", "gemini", "antigravity"},
        "opencode cli": {"opencode cli", "opencode", "opencode worker a", "opencode glm flash", "glm flash"},
        "cline cli": {"cline cli", "cline", "cline worker c"},
    }
    for canon, aliases in agent_aliases.items():
        if (n1 == canon or n1 in aliases) and (n2 == canon or n2 in aliases):
            return True
    return False


def sanitize_field(s: str) -> str:
    """Sanitize table fields against Markdown corruption and newline injection (D5, D13, D18)."""
    if not s:
        return ""
    # Flatten newlines and carriage returns to space
    cleaned = s.replace("\r\n", " ").replace("\n", " ").replace("\r", " ")
    # Neutralize leading colons/dashes to prevent pseudo-separator injection (D18)
    cleaned = re.sub(r"^:\-+", lambda m: " " + m.group(0), cleaned)
    # Escape unescaped pipe characters
    cleaned = re.sub(r"(?<!\\)\|", r"\|", cleaned)
    # Strip trailing backslashes so they cannot escape the closing table pipe
    cleaned = cleaned.rstrip("\\")
    return cleaned.strip()


def is_separator_line(line: str) -> bool:
    """Detect standard Markdown table separator line strictly by structure."""
    stripped = line.strip()
    return bool(re.match(r"^\|(?:\s*:?-{3,}:?\s*\|)+\s*$", stripped))


def extract_data_row(line: str) -> list[str] | None:
    """Extract cell content from a data row respecting escaped pipes."""
    stripped = line.strip()
    if not (stripped.startswith("|") and stripped.endswith("|")):
        return None
    inner = stripped[1:-1]
    raw_parts = [p.strip() for p in re.split(r"(?<!\\)\|", inner)]
    if len(raw_parts) < 3:
        return None
    return [p.replace(r"\|", "|") for p in raw_parts]


def init_teamwork_workspace(project_name: str, objective: str, workspace_dir: Path | None = None) -> dict:
    """Initialize fleet teamwork workspace structure."""
    teamwork_dir = workspace_dir or resolve_teamwork_dir()
    ensure_dir(teamwork_dir)

    roles = [
        "orchestrator",
        "explorer_frontend",
        "explorer_backend",
        "explorer_domain",
        "worker_ui",
        "worker_backend",
        "worker_scaffold",
        "reviewer_architecture",
        "reviewer_standards",
        "challenger_stress",
        "challenger_browser_qa",
        "auditor_claude",
    ]
    for r in roles:
        ensure_dir(teamwork_dir / r)

    # 1. ORIGINAL_REQUEST.md
    req_file = teamwork_dir / "ORIGINAL_REQUEST.md"
    if not req_file.exists():
        req_file.write_text(
            f"# Authoritative Project Request\n\n"
            f"**Project Name**: {project_name}\n"
            f"**Initialized**: {get_timestamp()}\n"
            f"**Objective**:\n{objective}\n\n"
            f"## Non-Negotiable Invariants\n"
            f"1. Zero breaking changes to existing contracts.\n"
            f"2. Exclusive Testing Monopoly: Only Claude Code CLI runs tests and grants [APPROVED].\n"
            f"3. Metadata-only in workspace — no source code or binary files here.\n"
            f"4. Rule 50: Use Western Arabic numerals (0-9) strictly.\n"
            f"5. Rule 51: Clean templates, zero cross-client data bleed.\n",
            encoding="utf-8"
        )

    # 2. PROJECT.md
    proj_file = teamwork_dir / "PROJECT.md"
    if not proj_file.exists():
        proj_file.write_text(
            f"# Master Project Architecture & Milestone Plan: {project_name}\n\n"
            f"## High-Level Architecture\n"
            f"Distributed Multi-CLI Fleet Execution Architecture governed by Hook 31.\n\n"
            f"### Operational Lanes\n"
            f"- **Master Orchestrator & Quality Gatekeeper**: Claude Code CLI (Opus 5.5 Max)\n"
            f"- **Backend & Database Executor**: OpenCode CLI (GLM 5.3 Flash / DeepSeek V4)\n"
            f"- **UI & Domain Terminal Executor**: Antigravity CLI (agcli / Gemini 3.8 Flash High)\n"
            f"- **Parallel Scaffolding**: Cline CLI (GLM 5.3 Flash - Auto-approve)\n"
            f"- **Interactive Cockpit & Visual Inspection**: Antigravity IDE\n\n"
            f"## Milestones\n"
            f"| # | Name | Scope | Assigned Fleet Workers | Status |\n"
            f"|---|------|-------|------------------------|--------|\n"
            f"| M1 | Discovery & Survey | Parallel Frontend, Persistence, and Domain Exploration | agcli, OpenCode, Spark | PLANNED |\n"
            f"| M2 | Core Implementation | Modular Construction & Scaffolding (Max 4 files/batch) | OpenCode GLM, Cline, agcli | PLANNED |\n"
            f"| M3 | Adversarial Review & Stress | Code Review, Edge Cases, Browser E2E Assertion Matrix | DeepSeek V4, agcli QA | PLANNED |\n"
            f"| M4 | Final Forensic Audit & Verification | Exclusive Test Run & Architectural Sign-Off | Claude Code CLI (Opus Max) | PLANNED |\n",
            encoding="utf-8"
        )

    # 3. GATE_STATUS.md - ZERO facades! All 4 milestones start clean as PENDING (D6)
    gate_file = teamwork_dir / "GATE_STATUS.md"
    if not gate_file.exists():
        gate_file.write_text(
            f"# Multi-CLI Fleet Gate Status Log\n\n"
            f"**Project**: {project_name}\n"
            f"**Last Updated**: {get_timestamp()}\n\n"
            f"## Milestone M1: Discovery & Survey\n"
            f"| Agent / Fleet Member | Assigned Role | Verdict | Source | Notes |\n"
            f"| :--- | :--- | :--- | :--- | :--- |\n"
            f"| Antigravity CLI (agcli) | Explorer Frontend | PENDING | pending | UI components & assets mapping |\n"
            f"| OpenCode CLI (GLM Flash) | Explorer Backend | PENDING | pending | DB schema & persistence mapping |\n"
            f"| OpenCode Spark Worker | Explorer Domain | PENDING | pending | API endpoints & compliance mapping |\n\n"
            f"Gate Result: **PENDING**\n\n"
            f"---\n\n"
            f"## Milestone M2: Core Implementation\n"
            f"| Agent / Fleet Member | Assigned Role | Verdict | Source | Notes |\n"
            f"| :--- | :--- | :--- | :--- | :--- |\n"
            f"| OpenCode CLI (Worker A) | Backend & DB Migrations | PENDING | pending | Building data models & DAOs |\n"
            f"| Antigravity CLI (Worker B) | UI & Presentation | PENDING | pending | Building layouts & Compose/Web views |\n"
            f"| Cline CLI (Worker C) | Terminal Scaffolding | PENDING | pending | Boilerplate & package configs |\n\n"
            f"Gate Result: **PENDING**\n\n"
            f"---\n\n"
            f"## Milestone M3: Adversarial Review & Stress Testing\n"
            f"| Agent / Fleet Member | Assigned Role | Verdict | Source | Notes |\n"
            f"| :--- | :--- | :--- | :--- | :--- |\n"
            f"| OpenCode DeepSeek V4 | Reviewer Architecture | PENDING | pending | Boundary & clean code audit |\n"
            f"| Antigravity CLI (agcli) | Reviewer Standards | PENDING | pending | BiDi, RTL & DOM ID audit |\n"
            f"| OpenCode DeepSeek V4 | Challenger Stress | PENDING | pending | Boundary probing & data load tests |\n"
            f"| agcli + webapp-qa-tester | Challenger Browser QA | PENDING | pending | Playwright & DOM assertions ($0) |\n\n"
            f"Gate Result: **PENDING**\n\n"
            f"---\n\n"
            f"## Milestone M4: Final Forensic Audit & Verification Sign-Off\n"
            f"| Agent / Fleet Member | Assigned Role | Verdict | Source | Notes |\n"
            f"| :--- | :--- | :--- | :--- | :--- |\n"
            f"| Claude Code CLI (Opus Max) | Sole Testing Authority | PENDING | pending | Unit/Integration suites & forensic audit |\n\n"
            f"Gate Result: **PENDING**\n",
            encoding="utf-8"
        )

    # 4. orchestrator/BRIEFING.md - Idempotent (D7)
    briefing_file = teamwork_dir / "orchestrator" / "BRIEFING.md"
    if not briefing_file.exists():
        briefing_file.write_text(
            f"# FLEET ORCHESTRATOR BRIEFING — {get_timestamp()}\n\n"
            f"## Mission\n"
            f"Coordinate the Multi-CLI Fleet to execute: {objective}\n\n"
            f"## 🔒 Fleet Architecture & Roles\n"
            f"- Master Orchestrator: Claude Code CLI (Opus 5.5 Max) / Antigravity IDE Cockpit\n"
            f"- Working Directory: {teamwork_dir.resolve()}\n"
            f"- Exclusive Testing Monopoly: Claude Code CLI exclusively executes test suites [C-21].\n"
            f"- Zero-Waste Token Conservation: Routine tasks routed to OpenCode / agcli [C-31, C-62].\n\n"
            f"## Current Milestone Status\n"
            f"- Active Milestone: M1 (Discovery & Survey)\n"
            f"- Gate Status: PENDING\n",
            encoding="utf-8"
        )

    return {
        "status": "initialized",
        "teamwork_dir": str(teamwork_dir.resolve()),
        "roles_scaffolded": len(roles),
    }


def generate_delegate_prompt(role: str, task: str, target_cli: str, workspace_dir: Path | None = None) -> str:
    teamwork_dir = workspace_dir or resolve_teamwork_dir()
    prompt = f"""[FLEET TASK DELEGATION PROTOCOL — TEMPLATE 02]

>>> TARGET FLEET WORKER: {target_cli}
>>> ASSIGNED ROLE: {role}
>>> TIMESTAMP: {get_timestamp()}

1. OPERATIONAL CONTEXT & AUTHORITY:
- You are operating under Hook 31 (Multi-CLI Fleet Teamwork Preview).
- Authoritative Request: {teamwork_dir / 'ORIGINAL_REQUEST.md'}
- Master Project Plan: {teamwork_dir / 'PROJECT.md'}
- Your Working Directory (metadata only): {teamwork_dir / role.lower().replace(' ', '_')}

2. ASSIGNED TASK SPECIFICATION:
{task}

3. MANDATORY FLEET INVARIANTS:
- STRICT TESTING BAN: Under [C-21], you are STRICTLY PROHIBITED from executing tests or granting test sign-offs.
- BATCH SIZING: Never touch or edit more than 4 files per session [C-78].
- CLEAN METADATA RULE: Place only Markdown logs/evidence inside {teamwork_dir}; all actual code belongs to target repository paths.
- HANDOFF FORMAT: Document your completed work in handoff.md with:
  * Observation
  * Logic Chain
  * Caveats & Risks
  * Conclusion
  * Verification Method (syntax/build check only, no tests)

4. NEXT ACTION:
Complete implementation, update your role's handoff.md, and return control to the Master Orchestrator.
"""
    return prompt


def check_milestone_pass_state(full_content: str, m_id: str) -> bool:
    """Check if a specific milestone has reached verified PASS status within its own section body only (D20b)."""
    m_norm = m_id.upper().strip()
    if not m_norm.startswith("M"):
        m_norm = f"M{m_norm}"
    pattern = rf"## Milestone {re.escape(m_norm)}:[^\n]*\n(.*?)(?=(\n## Milestone|\Z))"
    match = re.search(pattern, full_content, re.DOTALL)
    if not match:
        return False
    body = match.group(1)
    return bool(re.search(r"^Gate Result:\s*\*\*PASS\*\*", body, re.MULTILINE))


def sync_m4_gate_result(full_content: str) -> str:
    """Recompute Milestone M4 Gate Result based on auditor verdict and upstream M1-M3 pass status (D20b, D22)."""
    m4_match = re.search(r"(## Milestone M4:.*?\n)(.*?)(?=(\n## Milestone|\Z))", full_content, re.DOTALL)
    if not m4_match:
        return full_content
    header = m4_match.group(1)
    body = m4_match.group(2)

    parsed_rows = []
    past_sep = False
    for line in body.split("\n"):
        stripped = line.strip()
        if not past_sep:
            if is_separator_line(stripped):
                past_sep = True
            continue
        if not stripped.startswith("|"):
            continue
        parts = extract_data_row(line)
        if parts:
            parsed_rows.append({
                "agent": parts[0],
                "role": parts[1],
                "verdict": parts[2].upper(),
                "notes": parts[4] if len(parts) >= 5 else ""
            })

    has_fail = any(r["verdict"] in ["REJECT", "REQUEST_CHANGES"] for r in parsed_rows)
    has_pending = any(r["verdict"] in ["PENDING", "IN_PROGRESS"] for r in parsed_rows)

    all_upstream_pass = (
        check_milestone_pass_state(full_content, "M1") and
        check_milestone_pass_state(full_content, "M2") and
        check_milestone_pass_state(full_content, "M3")
    )

    auditor_row = next(
        (r for r in parsed_rows if r["verdict"] == "APPROVE" and agents_match(r["agent"], "claude code cli") and roles_match(r["role"], "sole testing authority")),
        None
    )

    if has_fail:
        fail_row = next(r for r in parsed_rows if r["verdict"] in ["REJECT", "REQUEST_CHANGES"])
        m4_result = f"Gate Result: **FAIL** (Changes requested by {fail_row['agent']}: {fail_row['notes']})"
    elif has_pending:
        m4_result = "Gate Result: **IN_PROGRESS**"
    elif auditor_row:
        if not all_upstream_pass:
            m4_result = "Gate Result: **BLOCKED** (Awaiting completion and PASS of upstream milestones M1-M3)"
        else:
            m4_result = f"Gate Result: **PASS** (Certified [APPROVED] by {auditor_row['agent']})"
    else:
        m4_result = "Gate Result: **PENDING**"

    new_body = re.sub(r"^Gate Result:.*$", lambda _m: m4_result, body, flags=re.M)
    new_m4_section = header + new_body
    return full_content[:m4_match.start()] + new_m4_section + full_content[m4_match.end():]


def record_gate_verdict(
    milestone: str,
    agent_id: str,
    role: str,
    verdict: str,
    notes: str,
    workspace_dir: Path | None = None
) -> dict:
    """Update GATE_STATUS.md with agent verdict under strict concurrency locking (D21)."""
    teamwork_dir = workspace_dir or resolve_teamwork_dir()
    gate_file = teamwork_dir / "GATE_STATUS.md"
    if not gate_file.exists():
        raise FileNotFoundError(f"GATE_STATUS.md not found at {gate_file}. Initialize workspace first.")

    milestone_norm = milestone.upper().strip()
    if not milestone_norm.startswith("M"):
        milestone_norm = f"M{milestone_norm}"

    v_upper = verdict.upper()
    is_claude_auditor = (
        agents_match(agent_id, "claude code cli") and
        roles_match(role, "sole testing authority")
    )

    # Authorization Check (D2, D16, D17 & [C-21]):
    if v_upper == "APPROVE":
        if milestone_norm != "M4":
            raise ValueError(
                f"Unauthorized verdict: 'APPROVE' is reserved exclusively for Claude Code CLI on Milestone M4 [C-21]. "
                f"Workers on Milestones M1-M3 record 'DONE', 'CLEAN', 'IN_PROGRESS', or 'REQUEST_CHANGES'."
            )
        if not is_claude_auditor:
            raise ValueError(
                f"Unauthorized approval: only Claude Code CLI acting as Sole Testing Authority can grant 'APPROVE' on Milestone M4 [C-21]. "
                f"Agent '{agent_id}' with role '{role}' is not authorized."
            )

    # D19: Milestone M4 is strictly reserved for Claude Code CLI acting as sole_testing_authority
    if milestone_norm == "M4":
        if not agents_match(agent_id, "claude code cli"):
            raise ValueError(
                f"Unauthorized write on Milestone M4: Milestone M4 is strictly reserved for Claude Code CLI [C-21]. "
                f"Agent '{agent_id}' is not authorized to write to Milestone M4."
            )
        if not roles_match(role, "sole testing authority"):
            raise ValueError(
                f"Unauthorized role on Milestone M4: Only 'sole_testing_authority' role is permitted on Milestone M4. "
                f"Role '{role}' is not authorized."
            )

    # Sanitize all fields against Markdown table corruption and newline injection (D5, D13, D18)
    clean_agent = sanitize_field(agent_id)
    clean_role = sanitize_field(role)
    clean_notes = sanitize_field(notes)

    # Concurrency safe atomic update with OS file locking (D21)
    with open(gate_file, "r+", encoding="utf-8") as f:
        fcntl.flock(f.fileno(), fcntl.LOCK_EX)
        try:
            content = f.read()

            pattern = rf"(## Milestone {re.escape(milestone_norm)}:.*?\n)(.*?)(?=(\n## Milestone|\Z))"
            match = re.search(pattern, content, re.DOTALL)
            if not match:
                raise ValueError(f"Milestone '{milestone_norm}' not found in GATE_STATUS.md")

            header = match.group(1)
            body = match.group(2)

            lines = body.split("\n")
            updated_lines = []
            found_row = False
            past_separator = False

            # D18: Structural positional parsing — only process data rows AFTER the separator line
            for line in lines:
                stripped = line.strip()
                if not past_separator:
                    if is_separator_line(stripped):
                        past_separator = True
                    updated_lines.append(line)
                    continue

                if not stripped.startswith("|"):
                    updated_lines.append(line)
                    continue

                parts = extract_data_row(line)
                if parts:
                    row_role = parts[1]
                    # D10: Match on ROLE ONLY (unique key per milestone table). Replace exactly ONE row.
                    if not found_row and roles_match(row_role, role):
                        updated_line = f"| {clean_agent} | {clean_role} | {v_upper} | handoff.md | {clean_notes} |"
                        updated_lines.append(updated_line)
                        found_row = True
                        continue
                updated_lines.append(line)

            if not found_row:
                # Insert after the last table row
                insert_pos = -1
                for idx, line in enumerate(updated_lines):
                    if is_separator_line(line) or (idx > 0 and updated_lines[idx].strip().startswith("|")):
                        insert_pos = idx
                new_row = f"| {clean_agent} | {clean_role} | {v_upper} | handoff.md | {clean_notes} |"
                if insert_pos != -1:
                    updated_lines.insert(insert_pos + 1, new_row)
                else:
                    updated_lines.append(new_row)

            # Parse data rows strictly after separator to compute genuine milestone Gate Result (D18)
            parsed_rows = []
            past_sep = False
            for line in updated_lines:
                stripped = line.strip()
                if not past_sep:
                    if is_separator_line(stripped):
                        past_sep = True
                    continue
                if not stripped.startswith("|"):
                    continue
                parts = extract_data_row(line)
                if parts:
                    parsed_rows.append({
                        "agent": parts[0],
                        "role": parts[1],
                        "verdict": parts[2].upper(),
                        "notes": parts[4] if len(parts) >= 5 else ""
                    })

            has_fail = any(r["verdict"] in ["REJECT", "REQUEST_CHANGES"] for r in parsed_rows)
            has_pending = any(r["verdict"] in ["PENDING", "IN_PROGRESS"] for r in parsed_rows)
            all_done_or_approved = all(r["verdict"] in ["DONE", "APPROVE", "CLEAN"] for r in parsed_rows)

            if has_fail:
                fail_row = next(r for r in parsed_rows if r["verdict"] in ["REJECT", "REQUEST_CHANGES"])
                gate_result = f"Gate Result: **FAIL** (Changes requested by {fail_row['agent']}: {fail_row['notes']})"
            elif has_pending:
                gate_result = "Gate Result: **IN_PROGRESS**"
            elif all_done_or_approved and len(parsed_rows) > 0:
                gate_result = "Gate Result: **PASS** (All milestone deliverables verified)"
            else:
                gate_result = "Gate Result: **PENDING**"

            new_body = "\n".join(updated_lines)

            # D12, D15: Replace whole Gate Result line anchored to beginning of line
            new_body = re.sub(
                r"^Gate Result:.*$",
                lambda _m: gate_result,
                new_body,
                flags=re.M
            )

            # Reconstruct whole section
            new_section = header + new_body
            content = content[:match.start()] + new_section + content[match.end():]

            # D22, N1: Single source of truth for Milestone M4 Gate Result recomputed on any milestone write
            content = sync_m4_gate_result(content)

            # D23, N2: Refresh Last Updated timestamp on first header match only without multiline bleed
            content = re.sub(r"\*\*Last Updated\*\*:[ \t]*[^\n]*", f"**Last Updated**: {get_timestamp()}", content, count=1)

            if milestone_norm == "M4":
                m4_m = re.search(r"## Milestone M4:.*?\n.*?^Gate Result:\s*(.*?)$", content, re.DOTALL | re.M)
                if m4_m:
                    gate_result = f"Gate Result: {m4_m.group(1).strip()}"

            f.seek(0)
            f.write(content)
            f.truncate()

        finally:
            fcntl.flock(f.fileno(), fcntl.LOCK_UN)

    return {
        "status": "recorded",
        "milestone": milestone_norm,
        "agent": clean_agent,
        "verdict": v_upper,
        "updated": True,
        "gate_result": gate_result
    }


def main():
    parent_parser = argparse.ArgumentParser(add_help=False)
    parent_parser.add_argument("-w", "--workspace", default=argparse.SUPPRESS, help="Custom teamwork workspace path (defaults to .agents/fleet_teamwork)")

    parser = argparse.ArgumentParser(description="Multi-CLI Fleet Teamwork Preview Orchestrator (Hook 31)", parents=[parent_parser])
    subparsers = parser.add_subparsers(dest="command", required=True)

    # init
    p_init = subparsers.add_parser("init", parents=[parent_parser], help="Initialize teamwork preview workspace")
    p_init.add_argument("--name", default="Active Project", help="Project name")
    p_init.add_argument("--objective", required=True, help="High-level project objective")

    # delegate
    p_del = subparsers.add_parser("delegate", parents=[parent_parser], help="Generate structured delegation prompt")
    p_del.add_argument("--role", required=True, help="Assigned role")
    p_del.add_argument("--cli", required=True, help="Target CLI tool")
    p_del.add_argument("--task", required=True, help="Detailed task instructions")

    # verdict
    p_ver = subparsers.add_parser("verdict", parents=[parent_parser], help="Record a gate verdict")
    p_ver.add_argument("--milestone", default="M2", help="Milestone ID (M1, M2, M3, M4)")
    p_ver.add_argument("--agent", required=True, help="Fleet worker ID")
    p_ver.add_argument("--role", required=True, help="Worker role")
    p_ver.add_argument("--verdict", choices=["APPROVE", "REQUEST_CHANGES", "REJECT", "CLEAN", "DONE"], required=True)
    p_ver.add_argument("--notes", default="", help="Auditor or worker notes")

    # status
    p_stat = subparsers.add_parser("status", parents=[parent_parser], help="Print gate status")

    args = parser.parse_args()
    workspace_arg = getattr(args, "workspace", None)
    # D8: Resolve relative custom workspace through resolve_teamwork_dir
    workspace = resolve_teamwork_dir(workspace_arg) if workspace_arg else None

    try:
        if args.command == "init":
            res = init_teamwork_workspace(args.name, args.objective, workspace_dir=workspace)
            print(json.dumps(res, indent=2, ensure_ascii=False))

        elif args.command == "delegate":
            prompt = generate_delegate_prompt(args.role, args.task, args.cli, workspace_dir=workspace)
            print(prompt)

        elif args.command == "verdict":
            res = record_gate_verdict(args.milestone, args.agent, args.role, args.verdict, args.notes, workspace_dir=workspace)
            print(json.dumps(res, indent=2, ensure_ascii=False))

        elif args.command == "status":
            t_dir = workspace or resolve_teamwork_dir()
            gate_file = t_dir / "GATE_STATUS.md"
            if not gate_file.exists():
                sys.stderr.write(f"Error: GATE_STATUS.md not found at {gate_file}. Run 'init' first.\n")
                sys.exit(1)
            print(gate_file.read_text(encoding="utf-8"))

    except Exception as e:
        sys.stderr.write(f"Error: {e}\n")
        sys.exit(1)


if __name__ == "__main__":
    main()
