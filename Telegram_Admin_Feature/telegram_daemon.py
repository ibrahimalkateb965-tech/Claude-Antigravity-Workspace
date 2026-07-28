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
    
    print(f"\n[TELEGRAM_DAEMON_WAKEUP] رسالة جديدة: {msg_text}")
    sys.stdout.flush()

if __name__ == "__main__":
    print("[TELEGRAM_DAEMON] الخادم يعمل الآن في الخلفية وينتظر الرسائل المستمرة...")
    bot = build_bot(TOKEN, ALLOWED_CHAT_ID, SHARED_PID_FILE, on_message)
    try:
        bot.infinity_polling(timeout=10, long_polling_timeout=5)
    except Exception as e:
        print(f"حدث خطأ: {e}")
        try:
            if SHARED_PID_FILE.exists():
                SHARED_PID_FILE.unlink()
        except:
            pass
        sys.exit(1)
