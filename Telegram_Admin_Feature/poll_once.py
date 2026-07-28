import os
import sys
from telegram_core import load_config, is_duplicate, download_media, write_request, build_bot, SHARED_PID_FILE

TOKEN, ALLOWED_CHAT_ID, current_dir = load_config()

def on_message(bot, message):
    output_path = current_dir / "telegram_request.json"
    
    if is_duplicate(output_path, message.message_id):
        return
        
    msg_text = message.text or message.caption or ""
    downloads_dir = current_dir / "downloads"
    
    media_path = download_media(bot, message, downloads_dir)
    write_request(output_path, message, msg_text, media_path)
    
    print(f"\n[تم استلام رسالة] تم حفظ رسالة تليجرام في: {output_path}")
    sys.stdout.flush()
    
    try:
        if SHARED_PID_FILE.exists():
            SHARED_PID_FILE.unlink()
    except:
        pass
        
    bot.stop_polling()
    os._exit(0)

if __name__ == "__main__":
    bot = build_bot(TOKEN, ALLOWED_CHAT_ID, SHARED_PID_FILE, on_message)
    print("جاري الاتصال بتليجرام وانتظار رسالة واحدة...")
    bot.polling(none_stop=True)
