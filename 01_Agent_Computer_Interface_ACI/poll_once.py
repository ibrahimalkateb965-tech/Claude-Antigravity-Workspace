import os
import sys
import codecs
sys.stdout = codecs.getwriter("utf-8")(sys.stdout.detach() if hasattr(sys.stdout, 'detach') else sys.stdout)

import json
import telebot
from dotenv import load_dotenv

# Load environment variables (from .env file in Manage Remotely)
env_path = r"F:\AI PROJECTS\Quran_Records\with antigravity\Manage Remotely\.env"
load_dotenv(dotenv_path=env_path)
TOKEN = os.getenv("TELEGRAM_TOKEN")
ALLOWED_CHAT_ID = os.getenv("ALLOWED_CHAT_ID")

if not TOKEN or not ALLOWED_CHAT_ID:
    print("❌ خطأ: يرجى التأكد من توفر TELEGRAM_TOKEN و ALLOWED_CHAT_ID في ملف .env")
    sys.exit(1)

bot = telebot.TeleBot(TOKEN)

# We use a custom message handler that catches the first message and exits
@bot.message_handler(func=lambda message: True, content_types=['text', 'photo', 'document', 'video', 'audio', 'voice'])
def handle_single_message(message):
    # Only process messages from the allowed chat ID
    if str(message.chat.id) != ALLOWED_CHAT_ID:
        return

    output_path = os.path.join(os.path.dirname(os.path.abspath(__file__)), "telegram_request.json")
    
    # Check last message_id to prevent processing duplicates
    if os.path.exists(output_path):
        try:
            with open(output_path, "r", encoding="utf-8") as f:
                old_data = json.load(f)
                if message.message_id <= old_data.get("message_id", -1):
                    return
        except Exception:
            pass

    # Extract text from message or caption
    msg_text = message.text or message.caption or ""

    # Write the message to telegram_request.json
    request_data = {
        "message_id": message.message_id,
        "chat_id": message.chat.id,
        "text": msg_text,
        "date": message.date,
        "user": message.from_user.first_name if message.from_user else "Unknown"
    }
    
    # Save file in the same directory as this script (or root dir)
    with open(output_path, "w", encoding="utf-8") as f:
        json.dump(request_data, f, ensure_ascii=False, indent=4)
        
    print(f"\n[تم استلام رسالة]: {msg_text}")
    print(f"تم الحفظ في {output_path}. جاري الإغلاق لإيقاظ المحرر...")
    
    # Stop polling and exit with 0 to wake up the IDE
    bot.stop_polling()
    os._exit(0)

if __name__ == "__main__":
    print("جاري الدوران (Polling) لانتظار رسالة واحدة من تليجرام...")
    try:
        # Infinity polling will run until stop_polling or exit is called
        bot.infinity_polling(timeout=10, long_polling_timeout=5)
    except Exception as e:
        print(f"حدث خطأ: {e}")
        sys.exit(1)
