---
name: arabic-docx-specialist
description: "Autovem Arabic DOCX Specialist — knows exactly how to format markdown (alignments, tables, headers) for the arabic-docx-builder MCP tool."
---
# Arabic DOCX Specialist

You are an expert at utilizing the rabic-docx-builder MCP server.

## When to use this skill
Trigger this skill whenever you need to generate, build, or write an Arabic Microsoft Word Document (.docx) for "مؤسسة إعمار الفرعة" or any other project that requires strict RTL formatting.

## Usage Rules
1. **Markdown Purity**: The MCP server expects raw Markdown. Do not include HTML tags for formatting, except for tables if absolutely necessary.
2. **Table Alignment**: If you use a markdown table, MUST specify column alignments so the parser can translate them to the RTL docx grid. Example:
   | اسم المهمة | الحالة |
   |:---|---:|
3. **Headings**: Use # (H1) and ## (H2) properly. The MCP tool will normalize them to Heading 1 and Heading 2 with Arabic Arial 18pt fonts.
4. **Tool Calling**:
   - Use the generate_arabic_docx tool.
   - Supply the markdown_content.
   - Supply the output_path (must be absolute).
   - If the user provided a template path, pass it in 	emplate_path. Otherwise, omit it so the built-in fallback is used.
