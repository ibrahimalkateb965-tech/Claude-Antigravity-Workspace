<div dir="rtl">

# خطة إصلاح وحدة `Telegram_Admin_Feature`

**التاريخ:** 2026-07-29
**المهندس المعماري:** Claude (Architect)
**المنفّذ:** Antigravity / Gemini Pro
**الهدف:** إزالة التكرار، إخراج القيم المضمّنة إلى متغيرات البيئة، إصلاح تسريب البيانات في Git، وإكمال التوثيق.

---

## ملخّص التشخيص

الملفات الأربعة في `Telegram_Admin_Feature/` **مطابقة بايت-ببايت** لنسخها في `01_Agent_Computer_Interface_ACI/`. أي إصلاح يجب أن يُطبَّق مرة واحدة فقط في مصدر واحد للحقيقة.

القرار المعماري المتبنّى: **`Telegram_Admin_Feature/` هو المصدر الوحيد للحقيقة (Single Source of Truth)**، ويصبح مجلد `01_Agent_Computer_Interface_ACI/` مستهلكًا له عبر استيراد بايثون، لا نسخة مكرّرة.

---

## المرحلة 0 — إصلاح عاجل لتسريب البيانات في Git (أولوية قصوى)

**المشكلة:** الملف `01_Agent_Computer_Interface_ACI/telegram_request.json` **متتبَّع في Git**، أي أنّ نصوص رسائل تليجرام الخاصة تُرفَع إلى المستودع العام مع كل commit.

### الخطوة 0.1 — إزالة الملف من التتبّع (مع الإبقاء عليه على القرص)

```bash
git rm --cached "01_Agent_Computer_Interface_ACI/telegram_request.json"
```

> ⚠️ **لا** تستخدم `git rm` بدون `--cached` — الملف مطلوب لعمل الوكيل محليًا.

### الخطوة 0.2 — تحديث `.gitignore`

أضِف الكتلة التالية في نهاية ملف `.gitignore` في جذر المشروع:

```gitignore
# Telegram ACI runtime artifacts (لا تُرفَع أبدًا)
telegram_request.json
*.pid
**/downloads/
__pycache__/
*.pyc
```

### الخطوة 0.3 — التحقّق

```bash
git status --short
git ls-files | grep -iE "telegram_request|\.pid|downloads/"
```

**معيار النجاح:** الأمر الثاني لا يُخرج أي نتيجة.

> ملاحظة: هذا يمنع التسريب المستقبلي فقط. الرسائل الموجودة في تاريخ الـ commits السابق تبقى موجودة. إن كانت حساسة، أبلِغ المستخدم بأن تنظيف التاريخ (`git filter-repo`) يحتاج إذنًا صريحًا — **لا تنفّذه من تلقاء نفسك**.

---

## المرحلة 1 — إخراج القيم المضمّنة إلى متغيرات البيئة

### الخطوة 1.1 — تحديث `Telegram_Admin_Feature/.env.example`

استبدل كامل محتوى الملف بالتالي:

```env
# ── إلزامي ──
TELEGRAM_TOKEN=your_telegram_bot_token_here
ALLOWED_CHAT_ID=your_chat_id_here

# ── اختياري: مزامنة GitHub Pages (تُستخدم مع الوسيط --sync) ──
# رابط موقع GitHub Pages الذي يُضاف في تذييل الرسالة
GITHUB_PAGES_URL=https://<username>.github.io/<repo-name>/
# مسار مجلد "العقل" الذي تُسحب منه التقارير (افتراضيًا مسار Antigravity)
ARTIFACTS_BRAIN_DIR=
```

### الخطوة 1.2 — إصلاح الرابط المضمّن في `send_reply.py`

**الملف:** `Telegram_Admin_Feature/send_reply.py`

الرابط مضمّن حاليًا في السطر 56 داخل `main()`. نفّذ التعديلين:

**أ) بعد سطر `ALLOWED_CHAT_ID = os.getenv("ALLOWED_CHAT_ID")` (السطر 20) أضِف:**

```python
GITHUB_PAGES_URL = os.getenv("GITHUB_PAGES_URL", "").strip()
```

**ب) استبدل كتلة `if args.sync:` بالكامل (الأسطر 48–62) بالتالي:**

```python
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
```

> هذا يزيل أيضًا المتغيّر `result` غير المستخدَم.

### الخطوة 1.3 — إصلاح المسار المضمّن في `sync_artifacts_to_github.py`

**الملف:** `Telegram_Admin_Feature/sync_artifacts_to_github.py`

**أ) أضِف بعد `from datetime import datetime` (السطر 7):**

```python
from dotenv import load_dotenv

load_dotenv(dotenv_path=Path(__file__).parent.parent / ".env")
```

**ب) استبدل الأسطر 32–34 (تحديد `brain_dir`) بالتالي:**

```python
    # مجلد "العقل" — قابل للتهيئة عبر .env مع مسار Antigravity الافتراضي
    brain_dir = os.getenv("ARTIFACTS_BRAIN_DIR", "").strip()
    if not brain_dir:
        brain_dir = os.path.join(
            os.path.expanduser("~"), ".gemini", "antigravity-ide", "brain"
        )
```

**ج) وسّع رسالة الخطأ في كتلة `if not os.path.exists(brain_dir)` لتشمل الحل:**

```python
    if not os.path.exists(brain_dir):
        print(f"❌ خطأ: لم يتم العثور على مجلد العقل (Brain) في المسار {brain_dir}")
        print("   💡 الحل: أضِف ARTIFACTS_BRAIN_DIR=<المسار الصحيح> إلى ملف .env")
        sys.exit(1)
```

---

## المرحلة 2 — إزالة التكرار (≈120 سطرًا)

### الخطوة 2.1 — إنشاء `Telegram_Admin_Feature/telegram_core.py`

الملفان `telegram_daemon.py` و`poll_once.py` متطابقان في الأسطر 1–134 ويختلفان فقط في آخر ~15 سطرًا. أنشئ ملفًا جديدًا يحتوي المنطق المشترك:

المحتوى المطلوب — انقل إليه بالضبط ما يلي من `telegram_daemon.py` دون تغيير في السلوك:

1. الاستيرادات وإعداد ترميز UTF-8 (الأسطر 1–9).
2. تحديد `current_dir` / `project_root` / `env_path` وتحميل `.env` والتحقّق من `TOKEN` و`ALLOWED_CHAT_ID` — لكن ضعها داخل دالة `load_config()` تُعيد `(TOKEN, ALLOWED_CHAT_ID)` وتُنهي البرنامج عند النقص.
3. `kill_previous_instance(pid_file)` و`write_pid(pid_file)` — بنفس المنطق، لكن باستقبال مسار ملف PID كوسيط بدل الثابت العام.
4. `download_media(bot, message, downloads_dir)` تُعيد `media_path` (منطق الأسطر 83–119).
5. `is_duplicate(output_path, message_id)` (منطق الأسطر 70–78).
6. `write_request(output_path, message)` تكتب `telegram_request.json` (منطق الأسطر 121–133).
7. `build_bot(token, allowed_chat_id, pid_file, on_message)` تُنشئ `TeleBot` وتُسجّل المعالج بنفس `content_types=['text','photo','document','video','audio','voice']`، وتستدعي `on_message(...)` في النهاية — وهي نقطة الاختلاف الوحيدة بين الوضعين.

### الخطوة 2.2 — إعادة كتابة `telegram_daemon.py`

يجب أن يبقى سلوكه مطابقًا تمامًا: ملف PID اسمه `telegram_daemon.pid`، يطبع `[TELEGRAM_DAEMON_WAKEUP] رسالة جديدة: {نص}` ثم `sys.stdout.flush()`، **ولا يخرج** بعد الرسالة، ويستخدم `bot.infinity_polling(timeout=10, long_polling_timeout=5)`.

### الخطوة 2.3 — إعادة كتابة `poll_once.py`

يجب أن يبقى سلوكه مطابقًا تمامًا: يطبع `[تم استلام رسالة]` ثم رسالة الحفظ، يحذف ملف PID، ثم `bot.stop_polling()` و`os._exit(0)`.

### الخطوة 2.4 — إصلاح تعارض 409 بين السكربتين

**المشكلة:** الاثنان يستخدمان ملفَي PID مختلفَين (`telegram_daemon.pid` و`poll_once.pid`)، لذا لا يُنهي أحدهما الآخر، وتشغيلهما معًا يُنتج خطأ `409 Conflict` من تليجرام — وهو تحديدًا ما كُتبت الحمايةُ لمنعه.

**الحل:** استخدم ملف PID **مشتركًا واحدًا** لكلا السكربتين في `telegram_core.py`:

```python
SHARED_PID_FILE = current_dir / "telegram_poller.pid"
```

بحيث يُنهي أيّ سكربت يبدأ العمل السكربتَ الآخر تلقائيًا. احرص على أن `kill_previous_instance` تتحقّق من أن العملية عملية بايثون فعلًا (كما في الكود الحالي) قبل إنهائها.

### الخطوة 2.5 — التحقّق من عدم انكسار السلوك

```bash
python -c "import ast,sys; [ast.parse(open(f,encoding='utf-8').read()) for f in ['Telegram_Admin_Feature/telegram_core.py','Telegram_Admin_Feature/telegram_daemon.py','Telegram_Admin_Feature/poll_once.py','Telegram_Admin_Feature/send_reply.py','Telegram_Admin_Feature/sync_artifacts_to_github.py']]; print('OK')"
```

ثم اختبار فعلي:

```bash
python Telegram_Admin_Feature/send_reply.py "اختبار الإصلاح ✅"
```

**معيار النجاح:** وصول الرسالة إلى تليجرام فعليًا. لا تعلن اكتمال العمل قبل رؤية مخرجات النجاح.

---

## المرحلة 3 — توحيد مجلد `01_Agent_Computer_Interface_ACI`

بعد اكتمال المراحل 0–2 والتحقّق منها:

1. احذف الملفات المكرّرة الأربعة من `01_Agent_Computer_Interface_ACI/`:
   `telegram_daemon.py`، `poll_once.py`، `send_reply.py`، `sync_artifacts_to_github.py`.
2. **انقل** `aci_tools.py` إلى `Telegram_Admin_Feature/` (هو الملف الوحيد الموجود هناك ولا نسخة له في الوحدة الجديدة).
3. افحص كل مرجع في المشروع لهذه المسارات القديمة وحدِّثه:

```bash
grep -rn "01_Agent_Computer_Interface_ACI" --include="*.md" --include="*.py" --include="*.json" .
```

> ⚠️ توقّع مراجع في `.agents/AGENTS.md`، `MEMORY_STORE.md`، `.agents/HOOKS_GUIDE.md`، وملفات إعداد الـ hooks. **حدّثها كلها** — أي مرجع مفقود يكسر آلية الإيقاظ (wakeup) للوكيل.
4. أبقِ على `01_Agent_Computer_Interface_ACI/downloads/` و`telegram_request.json` **إن كانت الـ hooks الحالية تعتمد عليها**؛ وإلا انقلهما هما أيضًا.

---

## المرحلة 4 — إكمال التوثيق

### الخطوة 4.1 — إنشاء `Telegram_Admin_Feature/requirements.txt`

```
pyTelegramBotAPI>=4.14.0
python-dotenv>=1.0.0
psutil>=5.9.0
```

### الخطوة 4.2 — تحديث `Telegram_Admin_Feature/README.md`

أضِف الأقسام الناقصة التالية إلى الملف الحالي:

1. **`telegram_core.py`** في قائمة المكوّنات (الوحدة المشتركة الجديدة).
2. **`aci_tools.py`** — وصف وظيفته بعد نقله.
3. **التثبيت** — استبدل أمر `pip install` اليدوي بـ:
   ```bash
   pip install -r Telegram_Admin_Feature/requirements.txt
   ```
4. **جدول متغيّرات البيئة** — الأربعة كلها: `TELEGRAM_TOKEN`، `ALLOWED_CHAT_ID`، `GITHUB_PAGES_URL`، `ARTIFACTS_BRAIN_DIR`، مع تحديد الإلزامي والاختياري.
5. **مخطّط `telegram_request.json`** — وثّق الحقول الستة:

   | الحقل | النوع | الوصف |
   |---|---|---|
   | `message_id` | int | معرّف الرسالة (يُستخدم لمنع التكرار) |
   | `chat_id` | int | معرّف المحادثة |
   | `text` | str | نص الرسالة أو التعليق على المرفق |
   | `media_path` | str | مسار مطلق للمرفق المحمَّل، أو `""` |
   | `date` | int | طابع زمني Unix |
   | `user` | str | الاسم الأول للمُرسل |

6. **الملفات المُنشَأة وقت التشغيل** — `downloads/`، `telegram_poller.pid`، `telegram_request.json`، مع التنبيه إلى أنها كلها في `.gitignore` ولا يجوز رفعها.
7. **التشغيل في الخلفية على Windows** — أضِف:
   ```powershell
   Start-Process -NoNewWindow python -ArgumentList "Telegram_Admin_Feature/telegram_daemon.py"
   ```
8. **تحذير أمني** — `ALLOWED_CHAT_ID` هو الحاجز الأمني الوحيد؛ الوحدة تنفّذ أوامر محليًا، فلا تشاركه ولا ترفع `.env` أبدًا.
9. **قسم استكشاف الأخطاء** — الخطأ `409 Conflict` يعني وجود نسخة أخرى من البوت تعمل؛ الحل: أوقف كل عمليات بايثون العائدة للوحدة وأعِد التشغيل.

---

## قائمة التحقّق النهائية

- [ ] `git ls-files` لا يُظهر `telegram_request.json` ولا أي ملف `.pid`
- [ ] لا يوجد أي رابط GitHub أو مسار `.gemini` مضمّن في الكود (تحقّق بـ `grep -rn "github.io\|\.gemini" Telegram_Admin_Feature/`)
- [ ] `.env.example` يشمل المتغيّرات الأربعة
- [ ] `telegram_core.py` موجود، والملفان الآخران يستورد منه — لا تكرار
- [ ] ملف PID مشترك واحد بين الـ daemon و`poll_once`
- [ ] كل الملفات تجتاز `ast.parse`
- [ ] `send_reply.py` أرسل رسالة اختبار فعلية بنجاح
- [ ] `grep -rn "01_Agent_Computer_Interface_ACI"` لا يُظهر أي مرجع مكسور
- [ ] `requirements.txt` موجود، والـ README يشمل الأقسام التسعة أعلاه

---

## ما يجب **عدم** فعله

- ❌ **لا** تُعِد كتابة تاريخ Git (`filter-repo` / `rebase`) — يحتاج إذنًا صريحًا من المستخدم.
- ❌ **لا** تحذف `.env` ولا تُعدّل قيمه الحقيقية.
- ❌ **لا** تحذف `telegram_request.json` من القرص — احذفه من التتبّع فقط.
- ❌ **لا** تُغيّر منطق `content_types` أو قيَم مهل الـ polling — يعمل حاليًا كما هو مطلوب.
- ❌ **لا** تُعلن اكتمال المهمة قبل تنفيذ قائمة التحقّق ورؤية مخرجاتها.

</div>
