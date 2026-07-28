import os
import sys
from telegram_core import load_config, is_duplicate, download_media, write_request, build_bot, SHARED_PID_FILE, release_lock

TOKEN, ALLOWED_CHAT_ID, current_dir = load_config()

def on_message(bot, message):
    output_path = current_dir / "telegram_request.json"
    
    if is_duplicate(output_path, message.message_id):
        return
        
    msg_text = message.text or message.caption or ""
    downloads_dir = current_dir / "downloads"
    
    media_path = download_media(bot, message, downloads_dir)
    write_request(output_path, message, msg_text, media_path)
    
    print(f"\n[تم استلام رسالة]: {msg_text}")
    print(f"تم الحفظ في {output_path}. جاري الإغلاق لإيقاظ المحرر...")
    sys.stdout.flush()
    
    release_lock(SHARED_PID_FILE)
        
    bot.stop_polling()
    os._exit(0)

if __name__ == "__main__":
    takeover = "--takeover" in sys.argv
    bot = build_bot(TOKEN, ALLOWED_CHAT_ID, SHARED_PID_FILE, on_message, takeover=takeover)
    print("جاري الاتصال بتليجرام وانتظار رسالة واحدة...")
    try:
        bot.infinity_polling(timeout=10, long_polling_timeout=5)
    except KeyboardInterrupt:
        release_lock(SHARED_PID_FILE)
        sys.exit(0)
    except Exception as e:
        print(f"حدث خطأ: {e}")
        release_lock(SHARED_PID_FILE)
        sys.exit(1)
