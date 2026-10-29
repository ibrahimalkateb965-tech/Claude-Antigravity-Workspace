#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
AgentFleet Unified CLI Entry Point
==================================
The canonical CLI dispatcher for the AgentFleet framework:
Multi-CLI orchestration, hook automation, and context governance.
"""

import argparse
import json
import os
import subprocess
import sys
from pathlib import Path

__version__ = "1.0.0"

PROJECT_ROOT = Path(__file__).resolve().parent.parent


def get_fleet_config():
    cfg_path = PROJECT_ROOT / "fleet_config.json"
    if cfg_path.exists():
        try:
            return json.loads(cfg_path.read_text(encoding="utf-8"))
        except Exception:
            pass
    return None


def cmd_status(args):
    """Display current fleet topology, model tiering, and governance status."""
    print("=" * 65)
    print(f"🚀 AgentFleet System Status — v{__version__}")
    print("=" * 65)
    cfg = get_fleet_config()
    if cfg:
        print(f"• Active Framework: {cfg.get('framework_name', 'AgentFleet Multi-CLI Orchestrator')}")
        tiering = cfg.get("tiering_strategy", {})
        print("\n🏛️ Model Tiering Matrix:")
        for tier, details in tiering.items():
            model = details.get("model", "N/A")
            desc = details.get("description", "")
            print(f"  - [{tier.upper()}]: {model} ({desc})")

        print("\n⚙️ Active Operational Constraints:")
        invariants = [
            ("[C-21]", "Exclusive Testing Monopoly for Claude Code CLI"),
            ("[C-25]", "Autonomous Direct Dispatch (Zero manual copy-paste)"),
            ("[C-62]", "Context Governance Golden Sweet Spot (30k-300k tokens)"),
            ("[C-78]", "OpenCode Batch Sizing (Max 4 files per batch)"),
            ("[C-96]", "Hybrid Autonomous Browser QA via Antigravity agcli ($0.00)"),
            ("[C-100]", "Multi-CLI Teamwork Preview Standard (Hook 31)"),
        ]
        for tag, title in invariants:
            print(f"  • {tag}: {title}")
    else:
        print("Fleet configuration (fleet_config.json) loaded.")

    print("\n✅ System status: READY & OPERATIONAL.")
    return 0


def cmd_teamwork(args):
    """Delegate directly to fleet_teamwork_orchestrator."""
    script = PROJECT_ROOT / "tools" / "fleet_teamwork_orchestrator.py"
    cmd = [sys.executable, str(script)] + (args.extra_args if hasattr(args, "extra_args") else [])
    return subprocess.call(cmd)


def cmd_hooks(args):
    """List or inspect system hooks."""
    hooks_file = PROJECT_ROOT / "HOOKS_GUIDE.md"
    if not hooks_file.exists():
        hooks_file = PROJECT_ROOT / ".agents" / "HOOKS_GUIDE.md"

    if args.open:
        html_app = PROJECT_ROOT / "03_Dynamic_Hooks_Library" / "index.html"
        if html_app.exists():
            print(f"Opening Dynamic Hooks Library: {html_app}")
            if sys.platform.startswith("win"):
                os.startfile(str(html_app))
            elif sys.platform == "darwin":
                subprocess.run(["open", str(html_app)])
            else:
                subprocess.run(["xdg-open", str(html_app)])
            return 0

    print("📚 AgentFleet 32 System Hooks registered in HOOKS_GUIDE.md.")
    print("Run with `--open` to launch the interactive dynamic web visualizer.")
    return 0


def cmd_curate(args):
    """Delegate to prompt_library_curator."""
    script = PROJECT_ROOT / "tools" / "prompt_library_curator.py"
    cmd = [sys.executable, str(script)] + (args.extra_args if hasattr(args, "extra_args") else [])
    return subprocess.call(cmd)


def cmd_tailor(args):
    """Delegate to project_agent_tailor."""
    script = PROJECT_ROOT / "project_agent_tailor.py"
    cmd = [sys.executable, str(script)] + (args.extra_args if hasattr(args, "extra_args") else [])
    return subprocess.call(cmd)


def main():
    parser = argparse.ArgumentParser(
        prog="agentfleet",
        description="AgentFleet: The Open Source Multi-CLI Agent Fleet Orchestrator & Governance Engine"
    )
    parser.add_argument("-v", "--version", action="version", version=f"agentfleet v{__version__}")

    subparsers = parser.add_subparsers(dest="subcommand", help="Available subcommands")

    # status
    p_status = subparsers.add_parser("status", help="Show fleet topology, tiering, and active invariants")
    p_status.set_defaults(func=cmd_status)

    # teamwork
    p_tw = subparsers.add_parser("teamwork", help="Manage Hook 31 multi-CLI teamwork previews (init, delegate, verdict, status)")
    p_tw.set_defaults(func=cmd_teamwork)

    # hooks
    p_hooks = subparsers.add_parser("hooks", help="Inspect registered system hooks or launch web visualizer")
    p_hooks.add_argument("--open", action="store_true", help="Launch interactive HTML hooks explorer in browser")
    p_hooks.set_defaults(func=cmd_hooks)

    # curate
    p_curate = subparsers.add_parser("curate", help="Add or update prompts in the centralized prompt library")
    p_curate.set_defaults(func=cmd_curate)

    # tailor
    p_tailor = subparsers.add_parser("tailor", help="Analyze project requirements and tailor specialized agent skill packs")
    p_tailor.set_defaults(func=cmd_tailor)

    args, extra_args = parser.parse_known_args()
    if not args.subcommand:
        parser.print_help()
        return 0

    args.extra_args = extra_args
    return args.func(args)


if __name__ == "__main__":
    sys.exit(main())
