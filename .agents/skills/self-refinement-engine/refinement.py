import re
from validators import LengthValidator, RTLValidator, NumericValidator
from feedback_loops import LoopManager

class SelfRefinementEngine:
    """
    Self-Refinement Engine.
    Validates output text against formatting constraints using modular validators.
    """
    def __init__(self, check_rtl=True, min_length=100, max_length=5000, allow_mixed_digits=False):
        self.check_rtl = check_rtl
        self.min_length = min_length
        self.max_length = max_length
        self.allow_mixed_digits = allow_mixed_digits

        # Initialize LoopManager with modular validators
        self.loop_manager = LoopManager([
            LengthValidator(min_length=self.min_length, max_length=self.max_length),
            RTLValidator(check_rtl=self.check_rtl),
            NumericValidator(allow_mixed=self.allow_mixed_digits)
        ])

    def validate_output(self, text):
        failures = self.loop_manager.run_validation(text)
        is_valid = len(failures) == 0
        feedback_prompt = self.loop_manager.generate_feedback_prompt(failures) if not is_valid else ""
        return {
            "valid": is_valid,
            "failures": failures,
            "feedback": "Self-validation passed successfully." if is_valid else "Validation failed. Please refine the output based on the failures listed.",
            "feedback_prompt": feedback_prompt
        }

    def refine_loop(self, text, max_iterations=3):
        """
        Simulates an iterative refinement loop.
        """
        current_text = text
        for iteration in range(1, max_iterations + 1):
            report = self.validate_output(current_text)
            if report["valid"]:
                return {
                    "success": True,
                    "iterations": iteration,
                    "final_text": current_text,
                    "report": report
                }
            else:
                # Simulate correction: wrap in RTL if that was the issue
                if any("RTL wrapper" in f for f in report["failures"]):
                    current_text = f'<div dir="rtl">\n{current_text}\n</div>'
                # Simulate correction: pad length if too short
                if any("below the minimum" in f for f in report["failures"]):
                    current_text = current_text + "\n" + " " * (self.min_length - len(current_text))
                    
        return {
            "success": False,
            "iterations": max_iterations,
            "final_text": current_text,
            "report": self.validate_output(current_text)
        }

if __name__ == "__main__":
    engine = SelfRefinementEngine()
    test_text = "مرحبا بك في تطبيق تاج الوقار"
    print("Initial validation:")
    init_report = engine.validate_output(test_text)
    print(init_report)
    print("\nGenerated Feedback Prompt for LLM:")
    print(init_report["feedback_prompt"])
    
    print("\nRunning refinement loop:")
    print(engine.refine_loop(test_text))
    
    # Test mixed numerals failure
    mixed_text = "<div dir=\"rtl\">هذا النص يحتوي على أرقام 123 وأرقام هندية ١٢٣</div>"
    print("\nMixed numerals validation:")
    print(engine.validate_output(mixed_text))
