#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Antigravity IDE Context Window Telemetry HUD
Author: Antigravity Multi-Agent Core
Description: Live real-time token monitor for Antigravity IDE conversations.
Zero-dependency, read-only, non-intrusive SQLite WAL parser.
"""

import os
import sys
import time
import glob
import json
import sqlite3
import argparse
from datetime import datetime

# Enforce UTF-8 output on Windows
if hasattr(sys.stdout, 'reconfigure'):
    try:
        sys.stdout.reconfigure(encoding='utf-8')
    except Exception:
        pass

# ANSI Color Codes
RESET = "\033[0m"
BOLD = "\033[1m"
DIM = "\033[2m"
CYAN = "\033[36m"
MAGENTA = "\033[35m"
GREEN = "\033[32m"
YELLOW = "\033[33m"
RED = "\033[31m"
BLUE = "\033[34m"
WHITE = "\033[37m"
BG_DARK = "\033[40m"

DEFAULT_WINDOW_LIMIT = 1048576  # 1M tokens (Gemini 3.8 Flash High)
DEFAULT_CONV_DIR = os.path.expanduser(r"~\.gemini\antigravity-ide\conversations")

def decode_varint(stream, pos):
    res = 0
    shift = 0
    while pos < len(stream):
        b = stream[pos]
        pos += 1
        res |= (b & 0x7F) << shift
        shift += 7
        if not (b & 0x80):
            break
    return res, pos

def parse_telemetry(payload):
    """
    Extract token telemetry from Protobuf step payload.
    Tag 0x4A (field 9) contains session token metadata.
    """
    if not payload:
        return None
    pos = 0
    while True:
        idx = payload.find(b'J', pos)
        if idx == -1 or idx + 1 >= len(payload):
            return None
        length = payload[idx + 1]
        # Telemetry chunk length is typically 0x40 to 0x70 bytes
        if 0x30 <= length <= 0x80 and (idx + 2 + length) <= len(payload):
            sub = payload[idx + 2 : idx + 2 + length]
            p = 0
            tokens = {}
            valid = False
            while p < len(sub):
                if sub[p:p + 10] == b'B!\n\tsessio':
                    valid = True
                    break
                tag = sub[p]
                p += 1
                fn = tag >> 3
                val, p = decode_varint(sub, p)
                tokens[fn] = val
            if valid and (5 in tokens or (2 in tokens and tokens[2] > 8192)):
                f2 = tokens.get(2, 0)
                f5 = tokens.get(5, 0)
                f3 = tokens.get(3, 0)
                f6 = tokens.get(6, 0)

                # If field 2 > 8192 (Gemini max output token limit), field 2 represents cached prompt tokens
                if f2 > 8192:
                    total_input = f2 + f5
                    output_tokens = f3 if f3 < 8192 else 0
                    thinking_tokens = 0
                    cached_tokens = f2
                else:
                    total_input = f5 if f5 > 0 else f2
                    output_tokens = f2
                    thinking_tokens = f3
                    cached_tokens = f6

                return {
                    'input_tokens': total_input,
                    'output_tokens': output_tokens,
                    'thinking_tokens': thinking_tokens,
                    'cached_tokens': cached_tokens
                }
        pos = idx + 1

def resolve_target_db(conv_dir, specific_cid=None):
    if specific_cid:
        db_path = os.path.join(conv_dir, f"{specific_cid}.db")
        if os.path.exists(db_path):
            return db_path, specific_cid
        raise FileNotFoundError(f"Database not found for CID: {specific_cid}")

    dbs = glob.glob(os.path.join(conv_dir, "*.db"))
    if not dbs:
        raise FileNotFoundError(f"No conversation databases found in {conv_dir}")
    dbs.sort(key=lambda x: os.path.getmtime(x), reverse=True)
    latest_db = dbs[0]
    cid = os.path.splitext(os.path.basename(latest_db))[0]
    return latest_db, cid

def query_conversation(db_path, history_limit=1):
    uri_path = f"file:{os.path.abspath(db_path)}?mode=ro"
    conn = sqlite3.connect(uri_path, uri=True, timeout=5.0)
    conn.execute("PRAGMA query_only = ON")
    cur = conn.cursor()

    rows = cur.execute(
        "SELECT idx, step_payload FROM steps WHERE step_type=15 ORDER BY idx DESC LIMIT ?",
        (history_limit * 3,)  # fetch extra to ensure we find telemetry payloads
    ).fetchall()

    conn.close()

    history = []
    for idx, payload in rows:
        telemetry = parse_telemetry(payload)
        if telemetry:
            telemetry['step_idx'] = idx
            history.append(telemetry)
            if len(history) >= history_limit:
                break

    return history

GOLDEN_LIMIT = 300_000
ORANGE = "\033[38;5;208m"

def render_gauge(used, limit=GOLDEN_LIMIT, width=24):
    pct = (used / limit) * 100.0
    filled = min(width, int(round((min(100.0, pct) / 100.0) * width)))
    empty = max(0, width - filled)

    if used < 180_000:
        color = GREEN
        status = "🟢 PEAK ACCURACY (30k-180k)"
    elif used < 240_000:
        color = YELLOW
        status = "🟡 SWEET SPOT WARNING (180k-240k)"
    elif used <= 300_000:
        color = ORANGE
        status = "🟠 PRE-HANDOFF (240k-300k)"
    else:
        color = RED
        status = "🔴 EXCEEDED 300k SWEET SPOT"

    bar = f"{color}{'█' * filled}{DIM}{'░' * empty}{RESET}"
    return bar, pct, status, color

def format_tokens(num):
    if num >= 1_000_000:
        return f"{num / 1_000_000:.2f}M"
    elif num >= 1_000:
        return f"{num / 1_000:.1f}k"
    return str(num)

def display_dashboard(cid, telemetry, limit, db_mtime):
    inp = telemetry['input_tokens']
    out = telemetry['output_tokens']
    thk = telemetry['thinking_tokens']
    step = telemetry['step_idx']
    rem_golden = max(0, GOLDEN_LIMIT - inp)
    pct_hard = (inp / limit) * 100.0

    bar, pct_golden, status, status_color = render_gauge(inp, GOLDEN_LIMIT)
    updated_at = datetime.fromtimestamp(db_mtime).strftime("%H:%M:%S")

    w = 58
    line = "─" * w
    print(f"{CYAN}┌{line}┐{RESET}")
    print(f"{CYAN}│{BOLD}{WHITE}   ⚡ ANTIGRAVITY IDE — GOLDEN CONTEXT MONITOR (30k-300k)   {RESET}{CYAN}│{RESET}")
    print(f"{CYAN}├{line}┤{RESET}")
    print(f"{CYAN}│{RESET}  {DIM}Conversation:{RESET} {BOLD}{cid[:8]}...{cid[-4:]}{RESET}  {DIM}Last Step:{RESET} {CYAN}#{step}{RESET} ({updated_at})  {CYAN}│{RESET}")
    print(f"{CYAN}│{RESET}  {DIM}Sweet Spot Limit:{RESET} {BOLD}300,000 tokens (Peak Accuracy 30k-300k){RESET} {CYAN}│{RESET}")
    print(f"{CYAN}├{line}┤{RESET}")
    print(f"{CYAN}│{RESET}  {DIM}Sweet Spot Bar:{RESET}   [{bar}] {status_color}{BOLD}{pct_golden:5.1f}%{RESET}  {CYAN}│{RESET}")
    print(f"{CYAN}│{RESET}  {DIM}Fidelity Health:{RESET}  {status_color}{BOLD}{status}{RESET}  {CYAN}│{RESET}")
    print(f"{CYAN}├{line}┤{RESET}")
    print(f"{CYAN}│{RESET}  {DIM}Current Context (Input):{RESET} {BOLD}{inp:>10,}{RESET}  ({format_tokens(inp):>5})          {CYAN}│{RESET}")
    print(f"{CYAN}│{RESET}  {DIM}Remaining in Sweet Spot:{RESET} {BOLD}{GREEN if rem_golden > 0 else RED}{rem_golden:>10,}{RESET}  ({format_tokens(rem_golden):>5})          {CYAN}│{RESET}")
    print(f"{CYAN}│{RESET}  {DIM}Hardware Floor (1M Limit):{RESET} {inp:>8,} / {limit:,} ({pct_hard:4.1f}%) {CYAN}│{RESET}")
    print(f"{CYAN}│{RESET}  {DIM}Output / Thinking Tokens:{RESET}  {out:>6,} / {thk:>5,} tokens          {CYAN}│{RESET}")
    print(f"{CYAN}└{line}┘{RESET}")

def main():
    parser = argparse.ArgumentParser(description="Antigravity IDE Context Window Monitor")
    parser.add_argument("--watch", "-w", nargs="?", const=3, type=int, help="Live refresh every N seconds (default: 3s)")
    parser.add_argument("--history", "-H", nargs="?", const=5, type=int, help="Show token history across last N steps")
    parser.add_argument("--advise", "-a", action="store_true", help="Strategic Context Advisor & Decision HUD")
    parser.add_argument("--json", "-j", action="store_true", help="Output raw telemetry as JSON")
    parser.add_argument("--cid", type=str, help="Specific conversation ID (UUID)")
    parser.add_argument("--limit", "-l", type=int, default=DEFAULT_WINDOW_LIMIT, help="Max context token limit (default: 1M)")
    parser.add_argument("--dir", type=str, default=DEFAULT_CONV_DIR, help="Conversations directory path")
    args = parser.parse_args()

    try:
        db_path, cid = resolve_target_db(args.dir, args.cid)
    except Exception as e:
        print(f"{RED}Error: {e}{RESET}", file=sys.stderr)
        sys.exit(1)

    if args.advise:
        from context_advisor import evaluate_context_health, print_terminal_report
        history = query_conversation(db_path, history_limit=5)
        if not history:
            print(f"{RED}No telemetry found in conversation {cid}.{RESET}")
            sys.exit(1)
        latest = history[0]
        eval_res = evaluate_context_health(
            input_tokens=latest['input_tokens'],
            limit=args.limit,
            output_tokens=latest['output_tokens'],
            thinking_tokens=latest['thinking_tokens'],
            history=history
        )
        if args.json:
            eval_res_clean = {k: v for k, v in eval_res.items() if k != "color"}
            eval_res_clean['conversation_id'] = cid
            print(json.dumps(eval_res_clean, ensure_ascii=False, indent=2))
        else:
            print_terminal_report(cid, eval_res)
        return

    if args.json:
        history = query_conversation(db_path, history_limit=1)
        if not history:
            print(json.dumps({"error": "No telemetry found"}))
            sys.exit(1)
        data = history[0]
        data['conversation_id'] = cid
        data['limit'] = args.limit
        data['pct_used'] = round((data['input_tokens'] / args.limit) * 100, 2)
        data['free_tokens'] = max(0, args.limit - data['input_tokens'])
        print(json.dumps(data, indent=2))
        return

    if args.history:
        history = query_conversation(db_path, history_limit=args.history)
        if not history:
            print(f"{YELLOW}No telemetry history found.{RESET}")
            return
        print(f"\n{BOLD}{CYAN}Token History for Conversation {cid[:8]}...{cid[-4:]}{RESET}")
        print(f"{DIM}{'Step':<8} {'Context (Input)':<18} {'Output':<12} {'Thinking':<12} {'% Limit':<10}{RESET}")
        print("─" * 60)
        for h in reversed(history):
            pct = (h['input_tokens'] / args.limit) * 100
            bar, _, _, col = render_gauge(h['input_tokens'], args.limit, width=10)
            print(f"#{h['step_idx']:<7} {h['input_tokens']:>10,} ({format_tokens(h['input_tokens']):<5}) {h['output_tokens']:>6,}      {h['thinking_tokens']:>6,}      {col}{pct:>5.1f}%{RESET}")
        print()
        return

    if args.watch:
        interval = args.watch
        try:
            while True:
                # Clear terminal in-place
                os.system('cls' if os.name == 'nt' else 'clear')
                db_path, cid = resolve_target_db(args.dir, args.cid)
                db_mtime = os.path.getmtime(db_path)
                history = query_conversation(db_path, history_limit=1)
                if history:
                    display_dashboard(cid, history[0], args.limit, db_mtime)
                    print(f"\n{DIM}Refreshing every {interval}s... Press Ctrl+C to stop.{RESET}")
                else:
                    print(f"{YELLOW}Waiting for step telemetry in {cid}...{RESET}")
                time.sleep(interval)
        except KeyboardInterrupt:
            print(f"\n{GREEN}Monitor stopped.{RESET}")
            return

    # Default Snapshot Mode
    db_mtime = os.path.getmtime(db_path)
    history = query_conversation(db_path, history_limit=1)
    if not history:
        print(f"{RED}No telemetry found in conversation {cid}.{RESET}")
        sys.exit(1)
    display_dashboard(cid, history[0], args.limit, db_mtime)

if __name__ == "__main__":
    main()
