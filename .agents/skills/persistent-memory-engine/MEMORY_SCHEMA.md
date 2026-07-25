# Autovem Memory Schema (MEMORY_SCHEMA.md)

## Memory Entry Types

| Type | Description | Example |
|:-----|:------------|:--------|
| `lesson` | A problem encountered and its proven solution | "Room DB migration crashes if version not incremented" |
| `decision` | An architectural or technology choice with rationale | "Chose Hilt over Koin for DI due to compile-time safety" |
| `preference` | A user or team formatting/workflow preference | "All responses must be in Arabic with RTL formatting" |
| `pattern` | A recurring code pattern or best practice | "Always use StateFlow over LiveData in ViewModels" |
| `bug-fix` | A specific bug's root cause and fix | "OOM on image processing — fixed with periodic GPU cache clearing" |
| `tool-discovery` | A new library, MCP tool, or technique found | "kornia library required for BiRefNet model preprocessing" |

## Memory Entry Schema (YAML)

```yaml
- id: "MEM-{YYYY-MM-DD}-{NNN}"
  type: "lesson | decision | preference | pattern | bug-fix | tool-discovery"
  timestamp: "ISO-8601 datetime"
  agents:
    - "agent-name-contributing"
  context: "Brief situation description (1-2 sentences)"
  content: "The actual knowledge or solution (detailed)"
  tags:
    - "tag1"
    - "tag2"
  status: "active | superseded | archived"
  superseded_by: "MEM-ID (optional, only if status=superseded)"
```

## Export Filter Rules

Each agent receives only memories relevant to its domain:

| Agent Category | Receives Memory Types | Tag Filters |
|:---------------|:---------------------|:------------|
| **Architecture** (code-architect, monorepo-architect) | decision, pattern | architecture, structure, modules |
| **Development** (android-kotlin-pro, jetpack-compose-ui) | lesson, pattern, bug-fix | kotlin, compose, android, coroutines |
| **Testing** (android-testing, test-automator) | bug-fix, pattern, lesson | testing, junit, mockk, ci |
| **Database** (offline-sync-db) | decision, bug-fix, pattern | room, pocketbase, migration, sync |
| **Security** (security-auditor) | lesson, bug-fix | security, owasp, encryption, validation |
| **DevOps** (devops-deployer) | decision, tool-discovery | ci-cd, signing, deployment, staging |
| **Design** (ui-ux-design-lead, etc.) | preference, decision | design, colors, typography, rtl |
| **Content** (copywriting-lead, etc.) | preference, pattern | content, tone, style, arabic |
| **Finance** (financial-*, etc.) | pattern, preference | finance, accounting, reporting |
| **Legal** (contract-*, compliance-*, etc.) | pattern, decision | legal, compliance, license, nda |
| **Memory** (persistent-memory-engine) | ALL | ALL |
| **Optimizer** (agent-optimizer, prompt-engineer) | ALL | ALL |

## Status Lifecycle

```
active → superseded (when a newer entry replaces it)
active → archived (when no longer relevant)
superseded → archived (after confirmation)
```

## File Locations

| File | Purpose |
|:-----|:--------|
| `.agents/MEMORY_STORE.md` | Central append-only memory log |
| `.agents/AGENTS.md` (Section 8) | Curated highlights for quick reference |
| `.agents/PROJECT_CONTEXT.md` | Active project state and constraints |
| `.agents/MEMORY_EXPORT_PROTOCOL.md` | Rules for filtering and injecting context |
