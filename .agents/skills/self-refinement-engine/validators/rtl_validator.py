import re

class RTLValidator:
    """
    Validates that Arabic text is properly wrapped in an RTL container (<div dir="rtl">...</div>).
    """
    def __init__(self, check_rtl=True):
        self.check_rtl = check_rtl

    def validate(self, text):
        failures = []
        if self.check_rtl:
            # Check for Arabic characters (Unicode block: 0600-06FF)
            has_arabic = bool(re.search(r'[\u0600-\u06ff]', text))
            if has_arabic:
                # Must be wrapped in <div dir="rtl"> ... </div>
                open_tags = text.count('<div dir="rtl">')
                close_tags = text.count('</div>')
                if open_tags == 0 and close_tags == 0:
                    failures.append("Arabic text detected but it is missing the mandatory RTL wrapper '<div dir=\"rtl\">...</div>'.")
                elif open_tags != close_tags:
                    failures.append(f"Mismatched RTL wrapper tags: Found {open_tags} opening tag(s) but {close_tags} closing tag(s).")
        return failures
