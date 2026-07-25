class LoopManager:
    """
    Coordinates validation tasks and manages the iterative feedback loop for text refinement.
    """
    def __init__(self, validators=None):
        self.validators = validators if validators is not None else []

    def add_validator(self, validator):
        self.validators.append(validator)

    def run_validation(self, text):
        failures = []
        for validator in self.validators:
            failures.extend(validator.validate(text))
        return failures

    def generate_feedback_prompt(self, failures):
        """
        Generates a structured prompt to guide an LLM on correcting validation failures.
        """
        if not failures:
            return ""

        prompt_lines = [
            "[SYSTEM VALIDATION FAILURE]",
            "The generated output failed to meet the required formatting constraints.",
            "Please rewrite your response to address the following specific validation failures:",
        ]
        for index, failure in enumerate(failures, 1):
            prompt_lines.append(f"  {index}. {failure}")

        prompt_lines.append("\nRequirements:")
        prompt_lines.append("- If Arabic text is used, ensure it is completely wrapped in '<div dir=\"rtl\">...</div>'.")
        prompt_lines.append("- Do not mix Arabic (0-9) and Eastern Arabic/Hindi (٠-٩) numerals; stick to one consistent format.")
        prompt_lines.append("- Adhere to the length boundaries specified.")
        prompt_lines.append("\nPlease output the refined, corrected response now.")
        
        return "\n".join(prompt_lines)
