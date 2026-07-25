class LengthValidator:
    """
    Validates output text length against configured minimum and maximum limits.
    """
    def __init__(self, min_length=100, max_length=5000):
        self.min_length = min_length
        self.max_length = max_length

    def validate(self, text):
        failures = []
        text_len = len(text)
        if text_len < self.min_length:
            failures.append(f"Output length ({text_len} chars) is below the minimum limit of {self.min_length} chars.")
        if text_len > self.max_length:
            failures.append(f"Output length ({text_len} chars) exceeds the maximum limit of {self.max_length} chars.")
        return failures
