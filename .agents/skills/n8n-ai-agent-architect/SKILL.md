---
name: n8n-ai-agent-architect
description: "Autovem n8n AI Agent Architect — designs, builds, and optimizes production-grade AI agent workflows, multi-agent router systems, custom tool integrations, memory layers, and marketing automation pipelines in n8n."
---

# 🤖 n8n AI Agent Systems Architect & Playbook

This skill encodes the battle-tested engineering patterns and architecture blueprints for building autonomous AI Agent workflows, multi-agent systems, and enterprise automation pipelines using **n8n**, LangChain nodes, Vector Stores, and Model Context Protocol (MCP).

---

## 🎯 1. Core Architectural Patterns

### Pattern A: Multi-Agent Router / Supervisor System
- **Supervisor / Router Node:** Uses an LLM with strict classification prompt or router tools to analyze incoming payload/query and direct to specialized sub-workflows.
- **Worker Sub-Workflows:** Dedicated n8n workflows exposed as Tools via the `Execute Workflow Tool` or Webhook node.
- **Consolidation Node:** Aggregates outputs from parallel or sequential workers into a standardized JSON response.

### Pattern B: Tool-Calling AI Agent (ReAct Framework)
- **AI Agent Node:** Configured in `Tools Agent` mode (OpenAI Functions / Anthropic Tools).
- **Custom Tool Nodes:**
  - **HTTP Request Tool:** Direct API integrations with strict OpenAPI/JSON schemas.
  - **Execute Workflow Tool:** Calls modular sub-workflows (e.g., Lead Scorer, CRM Updater, Email Drafter).
  - **Custom Code Tool (JavaScript/Python):** For data transformations, calculations, and JSON normalization.
- **Model Node:** Connected to high-reasoning models (Claude 3.5 Sonnet / GPT-4o / Gemini 1.5 Pro).

### Pattern C: Evaluator-Optimizer Quality Loop
- **Generator Agent:** Produces initial draft/artifact (copywriting, analysis, lead classification).
- **Evaluator Agent:** Validates output against strict criteria (scoring rubric 1-10, missing fields check).
- **Conditional Branch (IF Node):**
  - If Score >= Threshold: Passes to Delivery / Production execution.
  - If Score < Threshold: Loops back with feedback up to `Max Iterations = 3`.

### Pattern D: Human-in-the-Loop (HITL) Guardrails
- **Wait Node (Webhook Resume):** Pauses workflow execution for high-risk actions (e.g., sending client invoices, publishing ads, sending mass emails).
- **Interactive Action:** Dispatches approval button via Slack / Telegram / Email.
- **Resume Execution:** User clicks approve/reject -> triggers webhook resume token.

---

## 🧠 2. Memory & Context Architecture

| Memory Type | n8n Node / Integration | Best Use Case | Retention / Scope |
| :--- | :--- | :--- | :--- |
| **Short-Term Window Memory** | `Window Buffer Memory` Node | Ongoing chat session, follow-up queries | Last 5–10 messages per `sessionId` |
| **Persistent Session Memory** | `Postgres / Redis Chat Memory` Node | Customer support bots, multi-day user interactions | Persistent across restarts, indexed by User ID |
| **Long-Term Knowledge (RAG)** | `Vector Store Tool` (Qdrant / Pinecone / Supabase) | Company SOPs, product catalogs, competitor databases | Semantic search & embeddings (`text-embedding-3-small`) |
| **Workflow Execution State** | `Workflow Static Data` / Redis | Rate-limiting, daily quotas, deduplication keys | Cross-execution state |

---

## 🛠️ 3. Standard JSON Schemas for Tool Calling

When building custom Tools in n8n, enforce strict JSON schemas so LLMs never hallucinate arguments:

```json
{
  "type": "object",
  "properties": {
    "lead_name": {
      "type": "string",
      "description": "Full name of the prospect"
    },
    "phone_number": {
      "type": "string",
      "description": "Normalized phone number with country code e.g. +966500000000"
    },
    "service_requested": {
      "type": "string",
      "enum": ["container_12yd", "container_20yd", "container_30yd", "commercial_contract"],
      "description": "Specific service or container size requested"
    },
    "urgency_level": {
      "type": "string",
      "enum": ["immediate_today", "this_week", "price_inquiry"],
      "description": "Urgency of delivery"
    }
  },
  "required": ["phone_number", "service_requested"]
}
```

---

## 📊 4. Agency & Marketing Production Workflows

### 1. Inbound Google Ads Lead Qualification & Instant Dispatch:
1. **Trigger:** Webhook from Landing Page Form / Google Ads Lead Extension.
2. **AI Agent (Lead Scorer):** Analyzes lead quality, validates phone number format, checks budget intent.
3. **Branch 1 (High Intent / Immediate):**
   - Sends instant WhatsApp notification to sales rep via WhatsApp Business API / UltraMsg.
   - Pushes deal to CRM (HubSpot / Google Sheets).
4. **Branch 2 (Spam / Negative Keywords):**
   - Logs to negative search query tracker and filters out.

### 2. Automated Google Ads & GA4 Performance Digest:
1. **Trigger:** Schedule Trigger (Every Monday 8:00 AM).
2. **Data Extraction:** HTTP Requests to Google Ads API & GA4 Reporting API.
3. **AI Agent (Performance Analyst):** Generates executive summary, highlights CTR/CPA variances, and recommends negative keyword additions.
4. **Delivery:** Formats beautiful markdown / PDF report and dispatches to client via Email/Slack.

---

## 🛡️ 5. Reliability & Error-Handling Protocol

1. **Error Trigger Node:** Always attach an `Error Trigger` workflow to catch unhandled crashes and alert the engineering team on Telegram/Slack with the failed execution ID.
2. **Fallback Models:** Configure fallback LLM credentials (e.g., if Anthropic API rate-limits, fall back to OpenAI).
3. **Rate-Limiting & Retries:** Enable `Retry on Fail` (3 attempts, exponential backoff) on all external HTTP requests and AI nodes.
4. **Sanitization:** Strip PII or sensitive API keys before passing payload to logging or analytics sinks.
