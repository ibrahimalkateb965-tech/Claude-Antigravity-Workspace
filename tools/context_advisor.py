#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Antigravity Context Window Strategic Advisor & Golden Productivity HUD
Author: Antigravity Multi-Agent Core
Description: Analyzes real-time context token usage calibrated strictly against 
the Golden Productivity & Peak Accuracy Zone (30,000 to 300,000 tokens), 
ensuring the model stays within maximum cognitive fidelity without attention decay.
"""

import os
import sys
import json
import argparse
from datetime import datetime

# Enforce UTF-8 on Windows
if hasattr(sys.stdout, 'reconfigure'):
    try:
        sys.stdout.reconfigure(encoding='utf-8')
    except Exception:
        pass

# Import core monitor functions
try:
    from ag_context_monitor import (
        resolve_target_db, query_conversation, format_tokens,
        DEFAULT_WINDOW_LIMIT, DEFAULT_CONV_DIR,
        RESET, BOLD, DIM, CYAN, MAGENTA, GREEN, YELLOW, RED, BLUE, WHITE
    )
except ImportError:
    sys.path.append(os.path.dirname(os.path.abspath(__file__)))
    from ag_context_monitor import (
        resolve_target_db, query_conversation, format_tokens,
        DEFAULT_WINDOW_LIMIT, DEFAULT_CONV_DIR,
        RESET, BOLD, DIM, CYAN, MAGENTA, GREEN, YELLOW, RED, BLUE, WHITE
    )

ORANGE = "\033[38;5;208m"

# Calibrated Golden Productivity Boundaries (Empirical Transformer Attention Range)
GOLDEN_ZONE_MIN = 30_000    # Bootstrap threshold
GOLDEN_ZONE_OPT = 180_000   # Peak Green ceiling (60% of 300k)
GOLDEN_ZONE_WARN = 240_000  # Yellow warning ceiling (80% of 300k)
GOLDEN_ZONE_MAX = 300_000   # Hard cognitive sweet-spot ceiling (100%)
HARDWARE_LIMIT = 1_048_576  # 1M tokens raw hardware crash limit

def evaluate_context_health(input_tokens, limit=HARDWARE_LIMIT, output_tokens=0, thinking_tokens=0, history=None):
    # Calculations against Golden Sweet Spot (300k)
    pct_golden = (input_tokens / GOLDEN_ZONE_MAX) * 100
    free_golden = max(0, GOLDEN_ZONE_MAX - input_tokens)

    # Calculations against Raw Hardware Ceiling (1M)
    pct_hardware = (input_tokens / limit) * 100
    free_hardware = max(0, limit - input_tokens)

    # Growth velocity across steps
    velocity = 0
    if history and len(history) > 1:
        recent = [h['input_tokens'] for h in history[:3]]
        if len(recent) >= 2:
            velocity = recent[0] - recent[-1]

    # Zonal Classification based on the 30k - 300k Golden Productivity Curve
    if input_tokens < GOLDEN_ZONE_OPT:
        zone = "GREEN"
        color = GREEN
        status_ar = "المنطقة الخضراء (الإنتاجية القصوى والدقة التامة [30k - 180k])"
        urgency = "LOW"
        decision_ar = "الاستمرار في التطوير دون أي تدخل؛ أنت في النطاق الذهبي المثالي"
        headroom_to_yellow = max(0, GOLDEN_ZONE_OPT - input_tokens)
        advice_ar = (
            f"• تركيز وانتباه النموذج في قمته (100% Attention Fidelity)؛ خالٍ تماماً من التشتت.\n"
            f"• متبقي لك {headroom_to_yellow:,} توكن قبل ملامسة النطاق الأصفر التحذيري.\n"
            f"• التوجيه: استمر في استخدام أدوات lean-ctx واقرأ التوقيعات (Signatures) للبقاء هنا أطول فترة ممكنة."
        )
        action_cmd = "ag-context"
    elif input_tokens < GOLDEN_ZONE_WARN:
        zone = "YELLOW"
        color = YELLOW
        status_ar = "المنطقة الصفراء (تحذير مبكر واقتراب من حافة النطاق الذهبي [180k - 240k])"
        urgency = "MEDIUM"
        decision_ar = "ترشيد فوري وحظر قراءة الملفات الكاملة لمنع الخروج من النطاق الذهبي"
        headroom_to_orange = max(0, GOLDEN_ZONE_WARN - input_tokens)
        advice_ar = (
            f"• السياق يقترب من حدود النطاق الذهبي؛ متبقي {headroom_to_orange:,} توكن قبل مرحلة التخطيط للتسليم.\n"
            f"• الإجراء الصارم: حظر قراءة الملفات الكاملة (mode='full')، والاقتصار على فحص أجزاء الأسطر المحددة.\n"
            f"• حافظ على إنهاء مهمتك الجزئية الحالية ولا تقم بالضغط أو التصفير الآن."
        )
        action_cmd = "ag-context -H 5"
    elif input_tokens <= GOLDEN_ZONE_MAX:
        zone = "ORANGE"
        color = ORANGE
        status_ar = "المنطقة البرتقالية (منطقة التخطيط للضغط أو التسليم [240k - 300k])"
        urgency = "HIGH"
        decision_ar = "التجهيز لحفظ الحالة وتثبيت الكود (Pre-Handoff Planning)"
        headroom_to_red = max(0, GOLDEN_ZONE_MAX - input_tokens)
        advice_ar = (
            f"• أنت عند الحافة القصوى لنطاق الإنتاجية الذهبية ({GOLDEN_ZONE_MAX:,} توكن)؛ متبقي {headroom_to_red:,} توكن فقط!\n"
            f"• الإجراء الإلزامي:\n"
            f"  1. إذا كانت ميزتك قيد الإنجاز: أكملها فوراً وتأكد من نجاح البناء (100% Green).\n"
            f"  2. تثبيت الكود بعمل git commit نظيف، وحفظ الدروس في MEMORY_STORE.md.\n"
            f"  3. التأهب لتطبيق الخطاف 25 (التصفير الاستراتيجي) أو تنفيذ أمر /compact للعودة فوراً للنطاق الأخضر."
        )
        action_cmd = "git status --short; ag-context -H 3"
    else:
        zone = "RED"
        color = RED
        status_ar = "المنطقة الحمراء (خروج من نطاق الإنتاجية القصوى > 300k - خطر تشتت الانتباه)"
        urgency = "CRITICAL"
        decision_ar = "التفعيل الإلزامي الفوري للخطاف 25 (التصفير الاستراتيجي) أو /compact"
        exceeded = input_tokens - GOLDEN_ZONE_MAX
        advice_ar = (
            f"• تنبيه حرج: تم تجاوز النطاق الذهبي للإنتاجية بمقدار {exceeded:,} توكن!\n"
            f"• النماذج في هذه المنطقة تبدأ في المعاناة من النسيان التراكمي وضعف الالتزام بالقيود الدقيقة.\n"
            f"• الإجراء الفوري: استدعاء خطاف التصفير الاستراتيجي (اكتب 'استعد للتصفير') أو تنفيذ /compact للعودة للنطاق الأخضر."
        )
        action_cmd = "استعد للتصفير (Hook 25)"

    return {
        "zone": zone,
        "color": color,
        "input_tokens": input_tokens,
        "output_tokens": output_tokens,
        "thinking_tokens": thinking_tokens,
        "golden_limit": GOLDEN_ZONE_MAX,
        "pct_golden": round(pct_golden, 2),
        "free_golden": free_golden,
        "hardware_limit": limit,
        "pct_hardware": round(pct_hardware, 2),
        "free_hardware": free_hardware,
        "velocity": velocity,
        "urgency": urgency,
        "status_ar": status_ar,
        "decision_ar": decision_ar,
        "advice_ar": advice_ar,
        "action_cmd": action_cmd
    }

def print_terminal_report(cid, eval_res):
    z_col = eval_res['color']
    inp = eval_res['input_tokens']
    g_max = eval_res['golden_limit']
    g_pct = eval_res['pct_golden']

    # Render Golden Zone Bar (28 chars)
    width = 28
    filled = min(width, int(round((g_pct / 100.0) * width)))
    empty = max(0, width - filled)
    bar_golden = f"{z_col}{'█' * filled}{DIM}{'░' * empty}{RESET}"

    print(f"\n{BOLD}{CYAN}╔══════════════════════════════════════════════════════════════════════╗{RESET}")
    print(f"{BOLD}{CYAN}║     🧭 ANTIGRAVITY CONTEXT ADVISOR — GOLDEN ZONE [30k - 300k]        ║{RESET}")
    print(f"{BOLD}{CYAN}╚══════════════════════════════════════════════════════════════════════╝{RESET}")
    print(f"{DIM}Conversation ID:{RESET} {cid[:8]}...{cid[-4:]}")
    print(f"{BOLD}🎯 النطاق الذهبي للإنتاجية (Golden Sweet Spot [30k - 300k]):{RESET}")
    print(f"  [{bar_golden}] {z_col}{BOLD}{g_pct:5.1f}%{RESET} ({inp:,} / {g_max:,} tokens)")
    print(f"{DIM}🛡️ سعة العتاد الصلبة (Hardware Ceiling):{RESET} {inp:,} / {eval_res['hardware_limit']:,} ({eval_res['pct_hardware']}%)")
    print(f"{DIM}الحالة التشغيلية:{RESET} {z_col}{BOLD}{eval_res['status_ar']}{RESET}")
    print(f"{DIM}درجة الاستعجال:{RESET}  {z_col}{eval_res['urgency']}{RESET}")
    print("─" * 70)
    print(f"{BOLD}🎯 القرار الهندسي الاستراتيجي:{RESET}")
    print(f"  {z_col}{BOLD}{eval_res['decision_ar']}{RESET}\n")
    print(f"{BOLD}📋 التوجيهات الفنية للبقاء في النطاق الأخضر:{RESET}")
    for line in eval_res['advice_ar'].split("\n"):
        print(f"  {line}")
    print("─" * 70)
    print(f"{BOLD}⚡ الإجراء السريع المقترح:{RESET} {CYAN}{eval_res['action_cmd']}{RESET}\n")

def main():
    parser = argparse.ArgumentParser(description="Antigravity Context Window Strategic Advisor")
    parser.add_argument("--cid", type=str, help="Conversation ID")
    parser.add_argument("--dir", type=str, default=DEFAULT_CONV_DIR, help="Conversations path")
    parser.add_argument("--limit", "-l", type=int, default=HARDWARE_LIMIT, help="Hardware limit (default: 1M)")
    parser.add_argument("--json", "-j", action="store_true", help="JSON output")
    args = parser.parse_args()

    try:
        db_path, cid = resolve_target_db(args.dir, args.cid)
        history = query_conversation(db_path, history_limit=5)
        if not history:
            print(json.dumps({"error": "No telemetry found"}) if args.json else f"{RED}No telemetry found.{RESET}")
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

    except Exception as e:
        if args.json:
            print(json.dumps({"error": str(e)}))
        else:
            print(f"{RED}Error: {e}{RESET}")
        sys.exit(1)

if __name__ == "__main__":
    main()
