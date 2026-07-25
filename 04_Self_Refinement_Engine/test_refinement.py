import unittest
from validators import LengthValidator, RTLValidator, NumericValidator
from feedback_loops import LoopManager
from refinement import SelfRefinementEngine

class TestLengthValidator(unittest.TestCase):
    def setUp(self):
        self.validator = LengthValidator(min_length=10, max_length=50)

    def test_valid_length(self):
        failures = self.validator.validate("Hello world")
        self.assertEqual(len(failures), 0)

    def test_too_short(self):
        failures = self.validator.validate("Short")
        self.assertTrue(any("below the minimum" in f for f in failures))

    def test_too_long(self):
        failures = self.validator.validate("A very very long text that exceeds the limit of fifty characters")
        self.assertTrue(any("exceeds the maximum" in f for f in failures))


class TestRTLValidator(unittest.TestCase):
    def setUp(self):
        self.validator = RTLValidator(check_rtl=True)

    def test_no_arabic(self):
        # Pure English text should not trigger RTL check
        failures = self.validator.validate("Pure English text")
        self.assertEqual(len(failures), 0)

    def test_arabic_with_correct_wrapper(self):
        failures = self.validator.validate('<div dir="rtl">مرحبا بك</div>')
        self.assertEqual(len(failures), 0)

    def test_arabic_missing_wrapper(self):
        failures = self.validator.validate("مرحبا بك")
        self.assertTrue(any("missing the mandatory RTL wrapper" in f for f in failures))

    def test_arabic_mismatched_wrapper(self):
        failures = self.validator.validate('<div dir="rtl">مرحبا بك')
        self.assertTrue(any("Mismatched RTL wrapper tags" in f for f in failures))


class TestNumericValidator(unittest.TestCase):
    def setUp(self):
        self.validator = NumericValidator(allow_mixed=False)

    def test_only_arabic_digits(self):
        failures = self.validator.validate("Numbers 123 and 456")
        self.assertEqual(len(failures), 0)

    def test_only_hindi_digits(self):
        failures = self.validator.validate("أرقام ١٢٣ و ٤٥٦")
        self.assertEqual(len(failures), 0)

    def test_mixed_digits(self):
        failures = self.validator.validate("خلط الأرقام ١٢٣ مع 456")
        self.assertTrue(any("Mixed numeral formats detected" in f for f in failures))


class TestSelfRefinementEngine(unittest.TestCase):
    def setUp(self):
        # Low limits for easier testing
        self.engine = SelfRefinementEngine(check_rtl=True, min_length=20, max_length=150)

    def test_successful_validation(self):
        valid_text = '<div dir="rtl">نص عربي سليم تماماً ومستوفٍ للطول</div>'
        report = self.engine.validate_output(valid_text)
        self.assertTrue(report["valid"])
        self.assertEqual(report["feedback_prompt"], "")

    def test_failed_validation_and_feedback(self):
        invalid_text = "مرحبا"
        report = self.engine.validate_output(invalid_text)
        self.assertFalse(report["valid"])
        self.assertIn("[SYSTEM VALIDATION FAILURE]", report["feedback_prompt"])

    def test_refinement_loop_success(self):
        test_text = "نص عربي يحتاج لتغليف وإكمال طول"
        result = self.engine.refine_loop(test_text)
        self.assertTrue(result["success"])
        self.assertTrue(result["report"]["valid"])
        self.assertIn('<div dir="rtl">', result["final_text"])
        self.assertGreaterEqual(len(result["final_text"]), 20)

if __name__ == "__main__":
    unittest.main()
