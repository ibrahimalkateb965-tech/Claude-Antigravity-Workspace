# Contributing to AgentFleet

Thank you for your interest in contributing to **AgentFleet**!
AgentFleet is a production-grade multi-CLI agent orchestration framework designed for developer teams. We welcome contributions that improve multi-agent collaboration, add robust skills, optimize context window consumption, or expand CLI harnesses.

---

## 🧭 Code of Conduct

By participating in this project, you agree to abide by our [Code of Conduct](CODE_OF_CONDUCT.md).

---

## 🚀 How to Contribute

### 1. Proposing New Features or Reporting Bugs
- **Issues**: Before starting major work, please open an Issue describing the proposed feature or bug report.
- **Discussions**: Share ideas about new model integrations (e.g., emerging frontier models) or CLI adapters.

### 2. Development Setup
```bash
git clone https://github.com/ibrahimalkateb965-tech/Claude-Antigravity-Workspace.git
cd Claude-Antigravity-Workspace
python -m venv .venv
source .venv/bin/activate  # On Windows: .venv\Scripts\activate
pip install -e ".[dev]"
```

### 3. Adding a New Agent Skill
Skills live in `.agents/skills/<skill-name>/` and must follow the standardized format:
- `SKILL.md`: Must have YAML frontmatter with `name` and `description`.
- Scripts and reference files must avoid hardcoded absolute paths; use dynamic environment variables or relative paths.
- Skills must be stateless and modular.

### 4. Adding or Modifying Hooks
- System hooks are governed by `HOOKS_GUIDE.md`.
- Each hook must define:
  1. Systematic trigger keywords.
  2. The primary and fallback agent models.
  3. Deterministic execution actions.
  4. Non-breaking compatibility with the existing 32 hooks.

### 5. Architectural Invariants to Uphold
- **Rule 50**: Use Western Arabic numerals (`0-9`) exclusively in documentation and code.
- **Rule 51**: Zero cross-project data bleed. Never include private client names or credentials.
- **[C-21] Exclusive Testing Monopoly**: Automated test suites are exclusively run and certified by Claude Code CLI.
- **Context Governance**: Tools should optimize for token efficiency and avoid streaming giant raw files into LLM contexts.

---

## 🧪 Testing & Verification

Run the local test harness before opening a pull request:
```bash
python run_all_tests.py
agentfleet status
```

---

## 📬 Pull Request Workflow

1. Fork the repository and create your branch from `main`:
   ```bash
   git checkout -b feat/my-new-feature
   ```
2. Commit your changes with clear, descriptive commit messages:
   ```bash
   git commit -m "feat(orchestrator): add support for new CLI adapter"
   ```
3. Push to your fork:
   ```bash
   git push origin feat/my-new-feature
   ```
4. Open a Pull Request targeting `main`. Describe your changes, why they are needed, and how you tested them.
