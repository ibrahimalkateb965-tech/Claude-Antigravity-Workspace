# Milestone M2: Backend & Persistence Integration Handoff
**Role**: Backend & DB Migrations (`worker_backend`)  
**Agent**: OpenCode CLI (Worker A)  
**Date**: 2026-10-21  
**Target Resource**: `https://github.com/agentrhq/webcmd`

---

## 1. Storage Integration & Invariants
1. **Cache & Persistence Paths**:
   - `webcmd` stores browser state, profiles, and sitemap candidates in `~/.webcmd`.
   - In accordance with `[C-22]` (DevCache Junctions) and `[C-82]` (C: drive quarantine), if the user runs heavy automated browser tasks on Windows, `~/.webcmd` can be seamlessly symlinked to `D:\DevCache\webcmd` via `mklink /J`.
2. **Database & Sitemap Model**:
   - The sitemap memory architecture stores structured markdown (`SITE.md`), candidates JSON, and git checkpoint revisions without needing an external SQL/relational database.
   - Fits lightweight zero-dependency deployment across the fleet.

## 2. Verdict
**Status**: DONE  
**Verification**: Verified directory layout and persistence patterns.
