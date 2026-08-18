from fastmcp import FastMCP
from pydantic import BaseModel, Field
import asyncio
import os
from pathlib import Path

# Import our custom modules
from parser_visitor import parse_markdown
from docx_builder import build_docx_from_ast

# Initialize FastMCP Server
mcp = FastMCP("arabic-docx-builder")

# Determine base directory for assets
BASE_DIR = Path(__file__).parent.resolve()
DEFAULT_TEMPLATE = BASE_DIR / "assets" / "default_template.docx"

class GenerateDocxRequest(BaseModel):
    markdown_content: str = Field(..., description="The raw markdown text containing headings, paragraphs, lists, and tables.")
    output_path: str = Field(..., description="Absolute path where the generated Word document should be saved.")
    template_path: str = Field(None, description="Optional: Absolute path to a custom DOCX template. If not provided or invalid, the fallback template is used.")

def _sync_generate_docx(markdown_content: str, output_path: str, template_path: str = None) -> str:
    """Synchronous function that executes CPU-bound docx generation."""
    # 1. Path Sanitization & Validation
    out_path = Path(output_path).resolve()
    
    # We allow the user to save it anywhere in their workspace, 
    # but we should ensure the directory exists.
    if not out_path.parent.exists():
        raise ValueError(f"Output directory does not exist: {out_path.parent}")
        
    # 2. Template Resolution
    valid_template = str(DEFAULT_TEMPLATE)
    if template_path:
        tpl_path = Path(template_path).resolve()
        if tpl_path.exists() and tpl_path.is_file():
            valid_template = str(tpl_path)
    
    # 3. Parse Markdown
    ast = parse_markdown(markdown_content)
    
    # 4. Build Document
    build_docx_from_ast(ast, valid_template, str(out_path))
    
    return f"Successfully generated Arabic DOCX at {out_path}"

@mcp.tool()
async def generate_arabic_docx(markdown_content: str, output_path: str, template_path: str = None) -> str:
    """
    Generates a perfectly formatted Arabic Microsoft Word document (DOCX) from Markdown.
    Supports right-to-left (RTL) tables, lists, font overrides, inline styling, and auto-updating Table of Contents.
    """
    try:
        # Execute CPU-bound docx building in a separate thread to keep FastMCP async loop responsive
        result = await asyncio.to_thread(
            _sync_generate_docx, 
            markdown_content, 
            output_path, 
            template_path
        )
        return result
    except Exception as e:
        return f"ToolError: Failed to generate document. Reason: {str(e)}"

if __name__ == "__main__":
    # Start the FastMCP stdio server
    mcp.run()
