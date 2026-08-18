import mistune

def get_ast_parser():
    """
    Returns a configured mistune markdown parser that outputs an AST.
    Includes plugins for tables and strikethrough.
    """
    return mistune.create_markdown(
        renderer='ast',
        plugins=['table', 'strikethrough']
    )

def parse_markdown(text: str) -> list:
    """
    Parses markdown text into an Abstract Syntax Tree (AST).
    """
    parser = get_ast_parser()
    ast = parser(text)
    return ast
