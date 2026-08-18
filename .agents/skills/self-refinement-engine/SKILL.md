---
name: self-refinement-engine
description: يتم تفعيله تلقائياً عند خطاف البدء "بسم الله". Closed-loop self-refinement engine that validates LLM outputs and generated documents against Arabic RTL formatting rules, single-page DOCX constraints, and numeral consistency before presentation.
---

# Self-Refinement Engine Skill (Global Skill)

## 1. Overview
The **Self-Refinement Engine** is a universal quality guard. It enforces a closed-loop validation pipeline (`validate -> fail/pass -> refine -> deliver`) to guarantee that all generated content (text, Markdown, and Word documents) adheres strictly to formatting standards before being shown to the user.

## 2. Core Validation Rules
1. **RTL Formatting**: All text containing Arabic characters must be wrapped in `<div dir="rtl">` tags.
2. **Numeral Consistency**: Document text must use appropriate numeral sets consistently (e.g. Hindi numerals ٠-٩ for formal Arabic documents, standard 0-9 for Excel/code).
3. **DOCX Single-Page Constraint**: Official letters must be checked by `DOCXValidator` to ensure margins (<=0.8in) and element counts do not overflow onto a second page.
4. **Length & Integrity**: Output text must meet required minimum and maximum lengths without truncation.

## 3. Usage & Integration Snippet

To validate any text or document programmatically in Python:

```python
import sys
sys.path.append(r"C:\Users\Kt\.gemini\config\skills\self-refinement-engine")

from refinement import SelfRefinementEngine
from validators import DOCXValidator

# Validate text output
engine = SelfRefinementEngine(check_rtl=True, min_length=50)
result = engine.validate_output(text_content)

if not result["valid"]:
    # Run closed-loop auto-refinement
    refined = engine.refine_loop(text_content)
    final_text = refined["final_text"]

# Validate DOCX document layout
docx_val = DOCXValidator(max_elements=40)
docx_failures = docx_val.validate("path/to/document.docx")
if docx_failures:
    print("DOCX Validation Warnings:", docx_failures)
```

## 4. Operational Workflow for Agents
- **Step 1 (Generate)**: Produce draft text or generate DOCX output.
- **Step 2 (Self-Validate)**: Run `SelfRefinementEngine` / `DOCXValidator` internally.
- **Step 3 (Refine)**: If errors are detected, fix formatting internally *before* user presentation.
- **Step 4 (Deliver)**: Present the clean, verified output.
