import openpyxl

file_path = r"f:\AI PROJECTS\Quran_Records\with antigravity\New Crew\.agents\HOOKS_GUIDE.xlsx"

try:
    wb = openpyxl.load_workbook(file_path)
    sheet = wb.active

    new_row = [
        "13. خطاف إدارة تليجرام",
        "[devops-deployer], [persistent-memory-engine]",
        "\"شغل البوت\", \"تفعيل تليجرام\", \"Telegram Bot\"",
        "تشغيل بوت تليجرام في الخلفية بنمط الاستماع للطلبات عن بعد دون تعارض."
    ]

    sheet.append(new_row)
    wb.save(file_path)
    print("تم إضافة خطاف إدارة تليجرام بنجاح إلى ملف الإكسيل (HOOKS_GUIDE.xlsx).")
except Exception as e:
    print(f"حدث خطأ أثناء التعديل: {e}")
