import os
import sys
import argparse
import codecs
import subprocess
from pathlib import Path

# Safe utf-8 output
sys.stdout = codecs.getwriter("utf-8")(sys.stdout.detach() if hasattr(sys.stdout, 'detach') else sys.stdout)

current_dir = Path(__file__).parent.absolute()
project_root = current_dir.parent
env_path = project_root / ".env"

import telebot
from dotenv import load_dotenv

load_dotenv(dotenv_path=env_path)
TOKEN = os.getenv("TELEGRAM_TOKEN")
ALLOWED_CHAT_ID = os.getenv("ALLOWED_CHAT_ID")
GITHUB_PAGES_URL = os.getenv("GITHUB_PAGES_URL", "").strip()

def main():
    if not TOKEN or not ALLOWED_CHAT_ID:
        print(f"❌ خطأ: يرجى التأكد من توفر TELEGRAM_TOKEN و ALLOWED_CHAT_ID في ملف {env_path}")
        sys.exit(1)

    parser = argparse.ArgumentParser(description="Send a message to Telegram.")
    parser.add_argument("message", type=str, nargs='?', help="The message text to send.")
    parser.add_argument("--file", type=str, help="Path to a text file to read the message from.")
    parser.add_argument("--sync", action="store_true", help="Sync artifacts to GitHub Pages and append link.")
    args = parser.parse_args()

    message_text = args.message

    if args.file:
        file_path = Path(args.file)
        if file_path.exists():
            with open(file_path, "r", encoding="utf-8") as f:
                message_text = f.read()
        else:
            print(f"❌ خطأ: الملف المحدد غير موجود - {file_path}")
            sys.exit(1)

    if not message_text:
        print("❌ خطأ: يرجى توفير نص الرسالة إما كمعامل مباشر أو باستخدام --file")
        sys.exit(1)

    if args.sync:
        print("🔄 جاري مزامنة التقارير إلى GitHub Pages قبل إرسال الرسالة...")
        sync_script = current_dir / "sync_artifacts_to_github.py"
        try:
            subprocess.run([sys.executable, str(sync_script)], check=True)
            print("✅ تمت مزامنة التقارير بنجاح.")

            if GITHUB_PAGES_URL:
                footer = (
                    "\n\n---\n🔗 **للمعاينة والتقرير الكامل من جوالك:**\n"
                    f"[اضغط هنا]({GITHUB_PAGES_URL})"
                )
                message_text += footer
            else:
                print("⚠️ تحذير: GITHUB_PAGES_URL غير محدّد في .env — سيتم الإرسال بدون رابط.")

        except subprocess.CalledProcessError:
            print("⚠️ فشلت عملية المزامنة. سيتم إرسال الرسالة بدون الرابط المباشر.")
            message_text += "\n\n---\n⚠️ *لم يتم تحديث التقارير لسبب تقني.*"

    bot = telebot.TeleBot(TOKEN)
    
    try:
        bot.send_message(chat_id=ALLOWED_CHAT_ID, text=message_text, parse_mode='Markdown')
        print("✅ تم إرسال الرسالة إلى تليجرام بنجاح.")
    except Exception as e:
        print(f"❌ حدث خطأ أثناء إرسال الرسالة: {e}")
        sys.exit(1)

if __name__ == "__main__":
    main()
