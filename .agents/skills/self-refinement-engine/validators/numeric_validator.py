import re

class NumericValidator:
    """
    Validates that a text does not mix Arabic numerals (0-9) and Eastern Arabic (Hindi) numerals (٠-٩)
    in an inconsistent manner, keeping formatting clean.
    """
    def __init__(self, allow_mixed=False):
        self.allow_mixed = allow_mixed

    def validate(self, text):
        failures = []
        if not self.allow_mixed:
            has_arabic_digits = bool(re.search(r'[0-9]', text))
            has_hindi_digits = bool(re.search(r'[\u0660-\u0669]', text))
            if has_arabic_digits and has_hindi_digits:
                failures.append("Mixed numeral formats detected: Text contains both Arabic numerals (0-9) and Eastern Arabic (Hindi) numerals (٠-٩). Please unify the format.")
        return failures
