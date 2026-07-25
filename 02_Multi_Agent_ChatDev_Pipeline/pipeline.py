class Agent:
    def __init__(self, name, role):
        self.name = name
        self.role = role

class ProgrammerAgent(Agent):
    def generate_code(self, task_desc):
        # Simulates writing code
        return f'# Code written by {self.name} for: {task_desc}\ndef process_data():\n    return "Processing completed successfully"'

class ReviewerAgent(Agent):
    def review(self, code):
        # Simulates reviewing code
        feedback = []
        if "TODO" in code:
            feedback.append("Please resolve TODO comments.")
        if "def " not in code:
            feedback.append("Missing function definition.")
            
        is_passed = len(feedback) == 0
        return {
            "passed": is_passed,
            "feedback": feedback if not is_passed else "Code looks clean and conforms to guidelines."
        }

class TesterAgent(Agent):
    def run_tests(self, code):
        # Simulates checking syntax/executing tests
        try:
            # Check syntax via compile
            compile(code, "<string>", "exec")
            return {"status": "success", "message": "All syntax checks passed."}
        except Exception as e:
            return {"status": "failed", "message": f"Syntax error: {e}"}

class PipelineCoordinator:
    """
    Coordinates execution flow between different developer sub-agents.
    """
    def __init__(self):
        self.programmer = ProgrammerAgent("AndroidDev", "المبرمج")
        self.reviewer = ReviewerAgent("QualityReviewer", "المراجع وضمان الجودة")
        self.tester = TesterAgent("QA_Automation", "مختبر الأكواد")

    def run_pipeline(self, task_description):
        log = []
        log.append(f"Starting pipeline for task: '{task_description}'")
        
        # 1. Generate code
        code = self.programmer.generate_code(task_description)
        log.append(f"[{self.programmer.name}] generated initial code.")
        
        # 2. Review code
        review_result = self.reviewer.review(code)
        log.append(f"[{self.reviewer.name}] review result: Passed={review_result['passed']}. Feedback={review_result['feedback']}")
        
        # 3. Test code
        test_result = self.tester.run_tests(code)
        log.append(f"[{self.tester.name}] test result: {test_result['status']} ({test_result['message']})")
        
        success = review_result['passed'] and (test_result['status'] == 'success')
        return {
            "success": success,
            "final_code": code,
            "log": "\n".join(log)
        }

if __name__ == "__main__":
    coord = PipelineCoordinator()
    res = coord.run_pipeline("Create simple data processor")
    print(res["log"])
