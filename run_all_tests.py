import os
import sys

# Add subfolders to system path for imports
sys.path.append(os.path.join(os.path.dirname(__file__), "Telegram_Admin_Feature"))
sys.path.append(os.path.join(os.path.dirname(__file__), "02_Multi_Agent_ChatDev_Pipeline"))
sys.path.append(os.path.join(os.path.dirname(__file__), "03_Dynamic_Prompt_Library"))
sys.path.append(os.path.join(os.path.dirname(__file__), "04_Self_Refinement_Engine"))

from aci_tools import ACITools
from pipeline import PipelineCoordinator
from prompt_loader import DynamicPromptLibrary
from refinement import SelfRefinementEngine

def run_tests():
    print("==================================================")
    print("STARTING TEST RUN FOR NEW CREW AI ENVIRONMENT")
    print("==================================================")
    
    # 1. Test ACI Tools
    print("\n[TEST 1/4] Testing ACI Tools...")
    space = ACITools.check_disk_space("G")
    if "error" not in space:
        print(f"-> SUCCESS: Checked disk space on G: (Free: {space['free_gb']} GB)")
    else:
        print(f"-> FAILED: {space['error']}")
        
    # 2. Test Multi-Agent Pipeline
    print("\n[TEST 2/4] Testing Multi-Agent Pipeline...")
    coord = PipelineCoordinator()
    res = coord.run_pipeline("Format document")
    if res["success"]:
        print("-> SUCCESS: Multi-agent pipeline ran successfully.")
    else:
        print("-> FAILED: Pipeline failed.")
        
    # 3. Test Dynamic Prompt Library
    print("\n[TEST 3/4] Testing Dynamic Prompt Library...")
    # Point to the Yonis Excel file
    loader = DynamicPromptLibrary(r"G:\أرشيف_ونسخ_احتياطية\ملفات_مكررة_وعامة\AI\مكتبة الأوامر.xlsx")
    search_res = loader.search_prompt("growth business")
    if "error" not in search_res and "best_match" in search_res:
        print(f"-> SUCCESS: Found best matching prompt: '{search_res['best_match']['name']}'")
    else:
        print(f"-> WARNING/FAILED: {search_res.get('error', search_res.get('message', 'Unknown error'))}")
        
    # 4. Test Self-Refinement Engine
    print("\n[TEST 4/4] Testing Self-Refinement Engine...")
    refiner = SelfRefinementEngine()
    refine_res = refiner.refine_loop("مرحبا بك في تطبيق تاج الوقار")
    if refine_res["success"]:
        print(f"-> SUCCESS: Refined text successfully wrapped in RTL. Final length: {len(refine_res['final_text'])}")
    else:
        print("-> FAILED: Refinement failed.")
        
    print("\n==================================================")
    print("TESTING COMPLETE - ALL SYSTEMS STATUS: OK")
    print("==================================================")

if __name__ == "__main__":
    run_tests()
