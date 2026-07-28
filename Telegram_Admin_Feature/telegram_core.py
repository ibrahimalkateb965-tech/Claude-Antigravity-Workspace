import os
import sys
import codecs
import json
import psutil
import datetime
from pathlib import Path

# Safe utf-8 output
sys.stdout = codecs.getwriter("utf-8")(sys.stdout.detach() if hasattr(sys.stdout, 'detach') else sys.stdout)

import telebot
from dotenv import load_dotenv

SHARED_PID_FILE = Path(__file__).parent.absolute() / "telegram_poller.pid"

def load_config():
    current_dir = Path(__file__).parent.absolute()
    project_root = current_dir.parent
    env_path = project_root / ".env"
    load_dotenv(dotenv_path=env_path)
    
    TOKEN = os.getenv("TELEGRAM_TOKEN")
    ALLOWED_CHAT_ID = os.getenv("ALLOWED_CHAT_ID")
    
    if not TOKEN or not ALLOWED_CHAT_ID:
        print(f"❌ خطأ: يرجى التأكد من توفر TELEGRAM_TOKEN و ALLOWED_CHAT_ID في ملف {env_path}")
        sys.exit(1)
        
    return TOKEN, ALLOWED_CHAT_ID, current_dir

def read_lock_holder(pid_file):
    """يُعيد PID المالك الحيّ للقفل، أو None إن كان القفل ميتًا/غير موجود."""
    if not pid_file.exists():
        return None
    try:
        old_pid = int(open(pid_file, 'r').read().strip())
    except Exception:
        return None

    if old_pid == os.getpid():
        return None

    try:
        if psutil.pid_exists(old_pid):
            process = psutil.Process(old_pid)
            if "python" in process.name().lower():
                return old_pid
    except psutil.NoSuchProcess:
        pass
    except Exception as e:
        print(f"⚠️ تحذير: تعذّر التحقّق من العملية {old_pid} - {e}")

    # القفل قديم (Stale) — العملية لم تعد موجودة
    try:
        pid_file.unlink()
    except Exception:
        pass
    return None

def acquire_lock(pid_file, takeover=False):
    """يستولي على قفل الـ polling. يرفض التشغيل إن كان القفل مملوكًا لعملية حيّة."""
    holder = read_lock_holder(pid_file)

    if holder is not None:
        if not takeover:
            print(f"⛔ يوجد مستهلك تليجرام يعمل بالفعل (PID: {holder}).")
            print("   تليجرام يسمح بعملية getUpdates واحدة فقط — التشغيل الآن يُنتج خطأ 409 Conflict.")
            print(f"   💡 لإنهاء العملية القائمة والاستيلاء على القفل: أضِف الوسيط --takeover")
            sys.exit(2)

        print(f"🔄 استيلاء صريح على القفل: جاري إنهاء العملية (PID: {holder})...")
        try:
            process = psutil.Process(holder)
            process.terminate()
            process.wait(timeout=5)
        except Exception as e:
            print(f"❌ فشل إنهاء العملية {holder}: {e}")
            sys.exit(2)
        try:
            if pid_file.exists():
                pid_file.unlink()
        except Exception:
            pass

    write_pid(pid_file)

def release_lock(pid_file):
    """يحرّر القفل فقط إن كنّا نحن مالكه."""
    try:
        if pid_file.exists() and int(open(pid_file).read().strip()) == os.getpid():
            pid_file.unlink()
    except Exception:
        pass

def write_pid(pid_file):
    with open(pid_file, 'w') as f:
        f.write(str(os.getpid()))

def is_duplicate(output_path, message_id):
    if output_path.exists():
        try:
            with open(output_path, "r", encoding="utf-8") as f:
                old_data = json.load(f)
                if message_id <= old_data.get("message_id", -1):
                    return True
        except Exception:
            pass
    return False

def download_media(bot, message, downloads_dir):
    media_path = ""
    downloads_dir.mkdir(exist_ok=True)
    
    file_id = None
    file_name = f"media_{datetime.datetime.now().strftime('%Y%m%d_%H%M%S')}"
    
    if message.photo:
        file_id = message.photo[-1].file_id
        file_name += ".jpg"
    elif message.document:
        file_id = message.document.file_id
        file_name = message.document.file_name or (file_name + ".bin")
    elif message.video:
        file_id = message.video.file_id
        file_name = message.video.file_name or (file_name + ".mp4")
    elif message.audio:
        file_id = message.audio.file_id
        file_name = message.audio.file_name or (file_name + ".mp3")
    elif message.voice:
        file_id = message.voice.file_id
        file_name += ".ogg"

    if file_id:
        try:
            print(f"📥 جاري تحميل المرفقات...")
            file_info = bot.get_file(file_id)
            downloaded_file = bot.download_file(file_info.file_path)
            
            save_path = downloads_dir / file_name
            with open(save_path, 'wb') as new_file:
                new_file.write(downloaded_file)
            media_path = str(save_path.absolute())
            print(f"✅ تم تحميل المرفق بنجاح: {save_path.name}")
        except Exception as e:
            print(f"⚠️ فشل تحميل المرفق: {e}")
            
    return media_path

def write_request(output_path, message, msg_text, media_path):
    request_data = {
        "message_id": message.message_id,
        "chat_id": message.chat.id,
        "text": msg_text,
        "media_path": media_path,
        "date": message.date,
        "user": message.from_user.first_name if message.from_user else "Unknown"
    }
    
    with open(output_path, "w", encoding="utf-8") as f:
        json.dump(request_data, f, ensure_ascii=False, indent=4)

def build_bot(token, allowed_chat_id, pid_file, on_message, takeover=False):
    acquire_lock(pid_file, takeover=takeover)
    
    bot = telebot.TeleBot(token)
    
    @bot.message_handler(func=lambda message: True, content_types=['text', 'photo', 'document', 'video', 'audio', 'voice'])
    def wrapper(message):
        if str(message.chat.id) != allowed_chat_id:
            return
        on_message(bot, message)
        
    return bot
