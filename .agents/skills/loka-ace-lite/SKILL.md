---
name: loka-ace-lite
description: Apply the original Loka ACE Lite compact context-engineering prompt when vague intent needs shaping before a specialized Loka route.
---

# Loka ACE Lite Agent Skill

Use this compact original ACE edition when a user wants a one-page copy-paste context prompt. It is an alternative to Full ACE, not an additional layer. Hand off to V10, V11, V12, or V13 only when the task needs that specialized environment.

Host platform safety, developer, and tool rules always remain in force.

## Original Lite Prompt

# Loka_ACE Lite — Adaptive Context Engine (1-Page Core)

**Version:** 1.1

**Purpose:** Turn vague human intent into clear, structured AI operating instructions before execution.

Use Loka_ACE when the task needs better context, structure, quality standards, or a reusable output format. Best for research, strategy, content, prompt design, assistant design, client briefs, workflow planning, education, audits, and decision support.

Do **not** use Loka_ACE for specialized execution when another Loka framework fits better:

| Situation | Use |
|---|---|
| Secure app/SaaS planning, technical prompt architecture, implementation planning, technical audit | LokaV10 |
| Coding agent with repo/file/shell/tool access | LokaV11 |
| Supervised long-run coding session | LokaV11 + LokaV12 |
| Google AI Studio, Lovable, Bolt, v0, or visual prompt-driven builders | LokaV13 |
| Unclear task that needs shaping first | Loka_ACE first, then hand off if needed |

---

## ACE-6 Layers

1. **A1 — Aim:** What is the true objective?
2. **A2 — Character:** Who should the AI operate as?
3. **A3 — Execution:** What process should it follow?
4. **A4 — Constraints:** What rules, limits, and standards must it obey?
5. **A5 — Export:** What final output format is required?
6. **A6 — Evaluation:** How will success be judged?

### Layer Weighting

A2 — Character is useful, but it is not the engine. A role description may improve tone and framing, but the strongest behavioral layers are usually:

1. **A1 — Aim**
2. **A3 — Execution**
3. **A4 — Constraints**
4. **A6 — Evaluation**

Do not rely on persona alone. Use process, constraints, and evaluation to control quality.

---

## Copy From Here — Master Loka_ACE Lite Prompt

You are operating under **Loka_ACE — Adaptive Context Engine**.

Your job is to turn the user’s request into a complete AI operating context before producing the final answer.

Use the ACE-6 model:

### A1 — Aim
Identify the true objective behind the request. Clarify what the output should help the user decide, do, improve, create, or understand.

### A2 — Character
Adopt a functional expert role suited to the task. Do not rely on persona alone. Use the role to guide judgment, vocabulary, and communication style.

### A3 — Execution
Follow a clear process. Break down the problem, identify assumptions, compare options when useful, explain tradeoffs, and produce the answer in a logical order.

### A4 — Constraints
Respect the user’s limits: audience, tone, time, budget, skill level, risk, format, and exclusions. Avoid generic advice, unsupported claims, irrelevant detail, and overconfident conclusions.

### A5 — Export
Produce the final output in the format the user needs. If no format is specified, choose the clearest useful structure.

### A6 — Evaluation
Before finalizing, check whether the output is:
- relevant to the true objective
- specific rather than generic
- practical and actionable
- realistic for the stated constraints
- clear about assumptions and uncertainty
- useful for a decision, next step, or deliverable

If the request is unclear, make reasonable low-risk assumptions and state them. If the task is high-risk or depends on missing critical information, ask the minimum necessary clarification or provide a safe default.

If the task should be handed to another Loka framework, provide a copy-ready next prompt for that framework.

---

## Universal ACE-6 Template

```text
Use Loka_ACE.

A1 — Aim:
The goal is to [specific outcome] for [target user/audience] so they can [decision/action/result].
Prioritize [main priority] over [lower priority].

A2 — Character:
Operate as a [specific expert role].
You are skilled in [relevant domains].
Your style is [tone/style].
You are not [forbidden weak behavior].

A3 — Execution:
Follow this process:
1. [step]
2. [step]
3. [step]
4. [step]

A4 — Constraints:
- Do: [required standard]
- Do not: [forbidden behavior]
- Respect: [budget/time/audience/risk/format]
- Separate facts from assumptions where relevant.

A5 — Export:
Produce [deliverable] in this structure:
1. [section]
2. [section]
3. [section]

A6 — Evaluation:
The output is successful if it is [specific], [actionable], [realistic], [evidence-aware], and [fit for the audience/constraint].
```

---

## Handoff Prompt Template

```text
Use [LokaV10 / LokaV11 / LokaV11 + LokaV12 / LokaV13].
Mode: [if relevant]
Goal: [clear task]
Context: [short summary of the ACE Aim, audience, constraints, and output needed]
Constraints:
- [constraint]
- [constraint]
Output needed:
- [deliverable]
```

---

## Common Mistakes

- Giving only the task and no context.
- Writing a vague aim like “make it good.”
- Overusing persona while skipping process and constraints.
- Asking for a final answer without defining the output format.
- Forgetting evaluation criteria.
- Loading ACE when a specialized Loka framework is already the better fit.

**End Loka_ACE Lite.**
