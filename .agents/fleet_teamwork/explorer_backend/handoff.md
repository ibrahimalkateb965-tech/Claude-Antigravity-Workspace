# Milestone M1: Backend, Engine & Persistence Exploration Handoff
**Role**: Explorer Backend (`explorer_backend`)  
**Agent**: OpenCode CLI (`GLM 5.3 Flash`)  
**Date**: 2026-10-21  
**Target Resource**: `https://github.com/agentrhq/webcmd`

---

## 1. Engine & Runtime Architecture
1. **QuickJS WebAssembly Sandbox (`quickjs-emscripten` 0.32.0)**:
   - Agent scripts are executed in an isolated QuickJS WebAssembly sandbox.
   - Host filesystem (`fs`), `require`, and host `process` are blocked.
   - Artifacts (downloads, screenshots) are exported through logical relative paths via `writeArtifact()` or `download.saveAs()`, writing to `~/.webcmd/cache/browser-run/<artifactId>/`.
2. **Session & Profile Isolation**:
   - Profiles (`webcmd profile create <name>`) act as isolated cookie jars / authentication scopes.
   - Sessions (`webcmd --profile <name> session create <session-id>`) are separate browser windows per profile.
   - Parallel multi-agent execution is safe across distinct session IDs.
3. **Sitemap Memory Loop & Git Lifecycle**:
   - `webcmd site memory context <url> --task-id <id> -f json`: Loads prior navigational knowledge (`SITE.md`, references, draft path).
   - Candidate discovery: `webcmd site memory candidate add <product> --kind ...` (action_space, better_path, access, high_consequence, repeated_mistake).
   - Checkpointing: Published via `webcmd site memory checkpoint` with strict concurrency conflict resolution.

## 2. Directory & Windows Junction Compliance
- Default cache lands in `~/.webcmd`. Under Windows/WSL environments, compliance with `[C-22]` and `[C-82]` requires monitoring `~/.webcmd` disk footprint or creating a Directory Junction to secondary drive `D:\DevCache\webcmd` if heavy browser cache accumulates.

## 3. Verdict
**Status**: DONE  
**Verification**: Complete verification of `package.json`, `src/launcher.ts`, QuickJS bindings, and sitemap memory references.
