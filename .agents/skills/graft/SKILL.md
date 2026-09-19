---
name: graft
description: "Autovem Graft Codebase Context & Knowledge Graph Specialist — uses Graft local knowledge graphs (graft/) to reduce token usage, tool calls, and exploration latency across Claude Code, Cursor, Codex, and Gemini. Use whenever indexing, exploring unfamiliar repositories, finding symbol definitions, tracing call graphs, checking blast radius before refactoring, or optimizing agent token budgets."
license: MIT
metadata:
  version: "0.18.0"
  author: NanoNets / trailhq
  ecosystem: Autovem Master Fleet
---

# Graft: Codebase Context & Knowledge Graph Specialist

`graft/` holds a persistent, lightweight graph of the codebase: small markdown nodes that each explain one subsystem or concept in clear prose and name the exact `file:line` spans they cover, plus a deterministic wiring graph of symbol relationships and call edges.

Querying a node costs a few hundred tokens; rebuilding that understanding by reading raw source files costs thousands, causes quadratic context bloat, and frequently misses cross-file edges.

Every local retrieval command is **$0**, requires **no API key**, and returns in **under a second**.

---

## 1. Quick Start & Setup

```bash
# Global CLI installation (run once)
npm install -g @nanonets/graft

# Initialize in project root and wire into Claude Code / agents
graft init --agents claude

# Verify graph status
graft check
```

`graft init` drops hooks and statusline configurations into `.claude/` and adds `graft/` to `.gitignore`. The graph is treated as a local, regenerable cache (like `node_modules`).

---

## 2. The Core Retrieval Tools

### 1 · `graft ask "<question>" --source` (Locate + Understand)
Ranked retrieval over the graph, routed automatically between prose nodes and the symbol graph, returning top hits with exact `file:line`.
- `--source`: Inlines the code at each hit (the ≤8-line **crux** carrying the actual logic), eliminating follow-up file reads. Add `--full` only when the crux is insufficient.
- `--in <path>`: Restricts search to a specific subtree or microservice.
- `-n <N>`: Caps results (default 8).
- **When to use**: Conceptual or locational questions ("how does auth work", "where is rate-limiting handled", "what handles request routing").
- **Best practice**: One query usually answers. For multi-part questions, ask one focused query per sub-aspect. If hits are weak, switch to `graft grep` or `graft callers` rather than rewording `ask`.

### 2 · `graft grep "<pattern>"` (Exhaustive Search)
Regex (or `--fixed` for literal) across every indexed file, hits **grouped by enclosing symbol** and ranked by coupling.
- **When to use**: When you need every occurrence (all call sites, all uses of a constant, all interface implementations). `ask` is top-N and will miss instances; `grep` is exhaustive.
- **Best practice**: Search a **short symbol name or literal**, not a guessed full signature. If a grep misses, loosen it (strip receiver/arguments, keep the bare name) and retry `graft grep`. Do NOT fall back to raw `grep -rn` unless searching unindexed non-code assets (docs, config files).
- `-i`: Case-insensitive.
- `--in <path>`: Scope to a specific directory.

### 3 · `graft skeleton <file>` (API at a Glance)
Signatures-only view of a single file (every function, method, class, and type with its line span) in ~200 tokens (~10× cheaper than reading the raw file).
- **When to use**: To inspect what an interface or module exports before editing or wiring into it.
- **Rule**: Run once per file; do NOT run skeleton on every file already summarized by `map`.

### 4 · `graft callers <symbol>` (Exact Graph Edges & Blast Radius)
Precomputed call/reference edges from tree-sitter AST, not string matching. Symbols can be bare (`Foo`), qualified (`Class.method`), or package-qualified (`pkg.Fn`).
- `--direction in` (default): **Who calls/references this**. Run before renaming, modifying signatures, or deleting a function.
- `--direction out`: **What this symbol calls/depends on**.
- `--depth N`: Walk transitively N hops. Use `--depth 2` to determine immediate impact ("what breaks if I touch this").
- `--depth all`: The **entire connected closure**. Mandatory before major refactoring, module decoupling, or wide renames to capture sibling and platform variants.

### 5 · `graft map` (Cold Onboarding & Architectural Tour)
A token-budgeted tour: directory clusters, per-directory hubs, and global hotspots derived straight from the code graph.
- **When to use**: When landing in an unfamiliar repository or asked to explain high-level system architecture.
- **Rule**: Read the hub cards named in `map`. Do NOT proceed to run `skeleton` on every subsystem listed. Use `--max-dirs <N>` to expand scope.

### 6 · `graft build` & `graft check` (Lifecycle & CI)
- Retrieval tools (`ask`, `grep`, `callers`, `skeleton`, `map`) **automatically refresh the graph in ~3ms** against working-tree modifications before answering, including uncommitted or staged changes. You do NOT need to run `graft build` after routine edits.
- `graft build --deep`: Runs the LLM summary pass to regenerate concept cards (use only when explicitly refreshing documentation).
- `graft check`: Exits with code 0 if the graph is fresh, or non-zero if stale (useful for CI/CD gates).
- `graft viz`: Launches a local browser visualization of the code graph.

---

## 3. Scenarios & Shortest Paths

| Scenario / Task | Optimal Command Sequence | Expected Tool Calls |
| :--- | :--- | :---: |
| **Cold onboarding / "explain this repo"** | `graft map` → inspect highlighted hub cards | 1 |
| **Understanding a flow ("how does X work")** | `graft ask "<flow>" --source` | 1 |
| **Locating where behavior lives** | `graft ask "where is <behavior>" --source` | 1 |
| **Editing a known symbol** | `graft grep "<symbol>"` → edit at `file:line` (skip `ask`) | 1 |
| **Changing signature or deleting symbol** | `graft callers <sym> --depth 2` first to inspect callers | 1 |
| **Major refactoring / Multi-file change** | `graft callers <sym> --depth all` to map full blast radius | 1 |
| **Mapping dependencies of a function** | `graft callers <sym> --direction out` | 1 |
| **Finding all instances of an identifier** | `graft grep "<literal>"` | 1 |
| **Inspecting a file's API surface** | `graft skeleton <file>` | 1 |
| **Debugging an error in a subsystem** | `graft ask "<error/symptom>" --source` → `callers` on suspect | 1–2 |

---

## 4. MCP Server Integration

When Graft is connected via MCP, the following tools are exposed natively to the agent:
- `graft_find_code`: Equivalent to `graft ask` with ranked semantic hits.
- `graft_find_all`: Equivalent to `graft grep` for exhaustive regex searching.
- `graft_file_api`: Equivalent to `graft skeleton` for signatures.
- `graft_trace_calls`: Equivalent to `graft callers` with `direction` and `depth` parameters.
- `graft_repo_map`: Equivalent to `graft map`.
- `graft_check_freshness`: Equivalent to `graft check`.

---

## 5. Token Conservation & Reporting Protocol

1. **Token Savings Line**: Every retrieval command opens with `[graft] tokens saved ≈ N`.
2. **Turn-End Tally**: When reporting completed actions in a turn that utilized Graft tools, append a concise summary:
   `🌱 graft saved ~12,400 tokens this turn (2 calls)`.
3. **Never Clip Output**: NEVER pipe Graft commands through `head`, `tail`, or `sed`. Every tool is internally budgeted and states what it truncated. Clipping destroys the token accounting line used by statusline parsers.

---

## 6. Strict Anti-Patterns

- ❌ **Anti-Pattern 1**: Guessing and blindly opening 10+ source files to understand architecture instead of running `graft map` or `graft ask "<topic>" --source`.
- ❌ **Anti-Pattern 2**: Running `graft ask` when the exact symbol name is already known (use `graft grep "<symbol>"` directly).
- ❌ **Anti-Pattern 3**: Re-opening files to "double check" line numbers when Graft's `covers:` already provided authoritative `file:line` coordinates.
- ❌ **Anti-Pattern 4**: Manually running `graft build` after every line edit (retrieval commands refresh against working-tree state in ~3ms automatically).
- ❌ **Anti-Pattern 5**: Modifying functions or classes without running `graft callers <sym> --depth 2` to verify blast radius.

---

## 7. Fleet Synergy (Autovem Multi-CLI Fleet)

- **Claude Code CLI (`claude`)**: Master consumer of Graft. Graft hooks into `.claude/` to slash input tokens by up to 42%, keeping Claude Code well within the calibrated 150k token safety cap.
- **OpenCode CLI (`opencode`)**: Can invoke `graft grep` and `graft skeleton` during scaffolding and database schema migrations to avoid full file context ingest.
- **Antigravity CLI / IDE**: Uses Graft MCP tools or CLI commands for instant symbol navigation and code verification without quadratic cache degradation.
