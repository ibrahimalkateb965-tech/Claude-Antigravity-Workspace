---
name: duckai-research-bridge
description: "Autonomous research, web intelligence, and zero-cost LLM query bridge using Duck.ai / DuckDuckGo AI. Provides zero-token privacy-preserving exploration, free access to frontier models (GPT-4o-mini, Claude 3 Haiku, Llama 3.3, Mistral) for prototyping, customer pre-screening, and fallback queries for Autovem agency workflows."
---

# Duck.ai Zero-Cost Research & Prototyping Bridge (`duckai-research-bridge`)

## 1. Overview & Architecture

`duckai-research-bridge` acts as an autonomous intermediary agent and skill connecting the Antigravity Multi-CLI Fleet and Autovem AI Agency workflows to DuckDuckGo's AI Chat infrastructure (`duck.ai`).

It leverages DuckDuckGo's reverse-engineered, privacy-centric AI gateway to provide free, no-API-key access to several top-tier generative models without consuming commercial token quotas.

### Supported Models (Zero-Cost Gateway)
- **GPT-4o mini** (OpenAI)
- **Claude 3 Haiku** (Anthropic)
- **Llama 3.3 70B** (Meta)
- **Mistral Small 3 / Mixtral 8x7B** (Mistral AI)

---

## 2. Core Capabilities & Value for Autovem Agency

1. **Zero-Token Discovery & Web Research (أبحاث مجانية غير محدودة الكوتا):**
   - Conduct preliminary market research, competitor analysis, and broad web syntheses without depleting paid Claude Opus/Sonnet or OpenAI API budgets.
2. **Local Staging & Testing Sandbox (بيئة تجريبية آمنة ومجانية):**
   - Provide an OpenAI-compatible proxy server endpoint for n8n workflows and FastMCP test scripts during local feature development.
3. **Emergency Quota Limit Fallback (خط دفاع احتياطي عند نفاد الكوتا):**
   - Serve as Tier-5 emergency query bridge for non-critical information lookup if third-party commercial LLM providers experience outages or quota limits.
4. **Privacy-Preserving Inquiry Pre-Screening (فلترة استفسارات العملاء):**
   - Screen incoming marketing leads and unauthenticated inquiries with zero server-side logging and strict metadata isolation.

---

## 3. Implementation Recipes

### Recipe A: Python Lightweight Interface (`duckduckgo_search` / `duckai`)

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Duck.ai Client Helper for Autovem Agent Fleet
"""
from duckduckgo_search import DDGS

def query_duckai(prompt: str, model: str = "gpt-4o-mini") -> str:
    """
    Query DuckDuckGo AI chat service programmatically.
    Models: 'gpt-4o-mini', 'claude-3-haiku', 'llama-3.3-70b', 'mistral-small'
    """
    ddgs = DDGS()
    try:
        response = ddgs.chat(keywords=prompt, model=model)
        return response
    except Exception as e:
        return f"[Error connecting to Duck.ai]: {str(e)}"

if __name__ == "__main__":
    result = query_duckai("لخص مميزات أتمتة الوكلاء الذكية في سطرين")
    print(result)
```

### Recipe B: FastMCP Tool Bridge (`duckai_mcp.py`)

```python
from mcp.server.fastmcp import FastMCP
from pydantic import BaseModel, Field
from duckduckgo_search import DDGS

mcp = FastMCP("DuckAI-Bridge")

class DuckAIRequest(BaseModel):
    prompt: str = Field(description="The question or prompt to send to Duck.ai")
    model: str = Field(default="gpt-4o-mini", description="Model: gpt-4o-mini | claude-3-haiku | llama-3.3-70b")

@mcp.tool()
async def ask_duckai(req: DuckAIRequest) -> str:
    """Send a zero-cost research inquiry to Duck.ai and return synthesized answer."""
    ddgs = DDGS()
    res = ddgs.chat(keywords=req.prompt, model=req.model)
    return res
```

---

## 4. Strict Engineering Guardrails & Invariants

> [!CAUTION]
> **Production Isolation Invariant [CRITICAL]:**
> Duck.ai endpoints are based on reverse-engineered web protocols. They MUST NEVER be deployed to customer-facing, SLA-backed, or business-critical production endpoints. Use strictly for:
> - Internal R&D and preliminary scraping.
> - Pre-production local testing in n8n / MCP sandbox.
> - Offline / failover auxiliary knowledge lookup.

- **Rate Limiting & Politeness (قيد معدل الاستعلام):** Queries must maintain a minimum 1.5s delay between bursts to avoid IP-based temporary blocks.
- **BiDi & Arabic Strict Compliance (قيد اللغة والأرقام):** All generated Arabic outputs must strictly follow the English numerals (0-9) standard [C-50] and avoid Eastern Arabic digits (٠-٩).
- **Graceful Degradation (معالجة الانقطاع الذاتي):** Always wrap calls in `try...except` and automatically cascade to Tier-1/Tier-2 official API providers if Duck.ai returns HTTP 429 or captcha challenges.

---

## 5. Verification Checklist

- [ ] Python dependencies installed (`duckduckgo-search >= 7.0.0` or `duckai`).
- [ ] Test query executes without authentication keys.
- [ ] Output formatting complies with project RTL and numeral standards.
- [ ] No commercial or sensitive client data sent over non-SLA endpoints.
