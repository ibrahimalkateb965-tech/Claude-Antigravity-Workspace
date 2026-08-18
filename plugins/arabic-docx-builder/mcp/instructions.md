# Arabic DOCX Builder MCP Server

## Overview
This MCP server provides an agentic tool to generate perfectly formatted, right-to-left (RTL) Arabic Word documents from Markdown text.

## Features
- **Strict RTL Processing**: Tables, lists, and paragraphs are correctly oriented for Arabic.
- **Font Normalization**: Strips Latin font properties and forcefully applies Arial, 18pt for Headings and 14pt for body text across the document.
- **Mistune AST Parsing**: Supports Markdown tables (with column alignments!), strikethrough, bold, italics, and soft/hard linebreaks.
- **Auto-updating TOC**: Injects `w:updateFields` to automatically update the document's Table of Contents upon opening.
- **Base64 Image Decoding**: Can extract Base64 images directly from Markdown and inject them into the Word document.

## Tool Usage
- Call the `generate_arabic_docx` tool.
- Provide `markdown_content`: The markdown text you want to inject. If you want a specific column alignment in a table, use standard markdown syntax like `|:---|---:|:---:|`. 
- Provide `output_path`: The absolute path where the `.docx` should be saved.
- Provide `template_path` (Optional): The absolute path to the DOCX template. If not provided or missing, the server will safely fallback to its built-in template.

## Requirements
To run this MCP server via Antigravity, add it to your `.gemini/config/mcp/` or use standard Node/Python bridge execution.
Dependencies are located in `requirements.txt`.
