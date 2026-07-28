<div dir="rtl">

# خطة الإصلاح (الإصدار الثاني) — معالجة انحدارات ما بعد إعادة الهيكلة

**التاريخ:** 2026-07-29
**المهندس المعماري:** Claude (Architect)
**المنفّذ:** Antigravity / Gemini Pro
**السياق:** إعادة الهيكلة (commit `3770874`) نجحت في معظمها، لكنها أدخلت انحدارَين وظيفيَّين وتخطّت المرحلة الرابعة من التوثيق.

---

## ما تم التحقّق من عمله فعليًا (لا تلمسه)

| الفحص | النتيجة |
|---|---|
| `py_compile` لكل الملفات | ناجح |
| `run_all_tests.py` | 4/4 ناجح |
| `send_reply.py` (إرسال حقيقي) | وصلت الرسالة فعليًا ✅ |
| استقبال الرسائل | الرسالة #134 استُلمت، المرفق حُمِّل، الـ JSON كُتب |
| إصلاح تسريب Git | `telegram_request.json` خارج التتبّع، `.gitignore` صحيح |
| مسار مجلد العقل | موجود افتراضيًا، غياب `ARTIFACTS_BRAIN_DIR` غير ضار |

> ⚠️ **قاعدة حاكمة:** المراحل 0–3 من الخطة الأولى منتهية ومُتحقَّق منها. **لا** تُعِد تنفيذها ولا تُعِد هيكلة ما يعمل. هذه الخطة تُعالج ثلاث نقاط محدّدة فقط.

---

## الإصلاح #1 — قفل الـ Polling يقتل الخادم بصمت (الأهم)

### التشخيص

في `telegram_core.py` السطر 15 يوجد ملف PID مشترك واحد `telegram_poller.pid`، وفي `build_bot()` (السطر 123) يُنفَّذ `kill_previous_instance()` **تلقائيًا وبصمت**.

النتيجة: `telegram_daemon.py` و`poll_once.py` صارا يقتل كلٌّ منهما الآخر. وبما أنّ `.agents/HOOKS_GUIDE.md` (السطر 121) ينصّ على أنّ دورة الإيقاظ تُشغِّل `poll_once.py`، فإنّ أول تشغيل لهذه الدورة **يُنهي الخادم الدائم** (PID 19088 حاليًا) دون أي رسالة تحذير، فيتوقّف التحكّم عن بُعد تمامًا.

**هذا خطأ في تصميمي المعماري في الخطة الأولى**، لا خطأ تنفيذ من Antigravity: الملف المشترك صحيح لمنع تعارض 409 (تليجرام يسمح بمستهلك `getUpdates` واحد فقط)، لكن **القتل التلقائي الصامت** خاطئ. السكربتان **وضعان بديلان**، لا نسختان متنافستان من نفس العملية.

### القرار المعماري

- يبقى القفل **مشتركًا** — هذا صحيح ولا يُغيَّر.
- يصبح الاستيلاء على القفل **صريحًا وصاخبًا** بدل أن يكون تلقائيًا وصامتًا.
- **الوضع الرسمي المعتمَد للمشروع هو `telegram_daemon.py`** (خادم دائم)، ويبقى `poll_once.py` أداة تشخيص/احتياط.

### الخطوة 1.1 — استبدل `kill_previous_instance` في `telegram_core.py`

الدالة الحالية (الأسطر 32–53) تقتل دائمًا. استبدلها بدالة تُبلّغ وترفض التشغيل افتراضيًا:

```python
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
```

> **لماذا `release_lock` تتحقّق من الملكية؟** الكود الحالي يحذف ملف PID دون تحقّق، فتستطيع عملية منتهية أن تحرّر قفل عملية أخرى حيّة. الحذف المشروط بالملكية يمنع ذلك.
>
> **لماذا الخروج بالرمز `2`؟** لتمييز «القفل مشغول» عن الفشل العام (`1`)، ليتمكّن أي hook من التصرّف بناءً عليه.

### الخطوة 1.2 — عدّل `build_bot` في `telegram_core.py`

استبدل السطرين 123–124 داخل `build_bot` بـ:

```python
def build_bot(token, allowed_chat_id, pid_file, on_message, takeover=False):
    acquire_lock(pid_file, takeover=takeover)
    ...
```

وأبقِ بقية الدالة كما هي دون تغيير (نفس `content_types`، ونفس فلترة `allowed_chat_id`).

### الخطوة 1.3 — أضِف الوسيط `--takeover` للسكربتَين

في **كلٍّ من** `telegram_daemon.py` و`poll_once.py`، داخل `if __name__ == "__main__":` وقبل استدعاء `build_bot`:

```python
takeover = "--takeover" in sys.argv
bot = build_bot(TOKEN, ALLOWED_CHAT_ID, SHARED_PID_FILE, on_message, takeover=takeover)
```

### الخطوة 1.4 — حرِّر القفل عند الخروج بأمان

في `telegram_daemon.py` استبدل كتلة حذف ملف PID اليدوية (الأسطر 28–32) بـ `release_lock(SHARED_PID_FILE)`، وأضِف `KeyboardInterrupt` كي لا يتخلّف قفل يتيم عند `Ctrl+C`:

```python
    try:
        bot.infinity_polling(timeout=10, long_polling_timeout=5)
    except KeyboardInterrupt:
        print("\n👋 إيقاف الخادم بناءً على طلب المستخدم.")
        release_lock(SHARED_PID_FILE)
        sys.exit(0)
    except Exception as e:
        print(f"حدث خطأ: {e}")
        release_lock(SHARED_PID_FILE)
        sys.exit(1)
```

وفي `poll_once.py` استبدل كتلة الحذف اليدوي (الأسطر 22–26) بـ `release_lock(SHARED_PID_FILE)`. لا تنسَ إضافة `release_lock` إلى قائمة الاستيراد في كلا الملفَين.

---

## الإصلاح #2 — `poll_once.py` فقد معالجة الأخطاء

### التشخيص

السطر 34 في `poll_once.py` صار `bot.polling(none_stop=True)` بدون أي `try/except`. الخطة الأولى نصّت على الإبقاء على `infinity_polling` ومسار الخروج بالرمز 1.

**الأثر:** مع `none_stop=True` يُبتلَع خطأ 409 ويُعاد المحاولة إلى ما لا نهاية، فلا تخرج العملية أبدًا — و**آلية إيقاظ المحرر تعتمد كليًا على خروج العملية**. النتيجة: السكربت يعلق صامتًا ولا يستيقظ الوكيل أبدًا.

### الخطوة 2.1 — أعِد المنطق الأصلي في `poll_once.py`

استبدل السطر 34 بالكتلة التالية:

```python
    try:
        bot.infinity_polling(timeout=10, long_polling_timeout=5)
    except KeyboardInterrupt:
        release_lock(SHARED_PID_FILE)
        sys.exit(0)
    except Exception as e:
        print(f"حدث خطأ: {e}")
        release_lock(SHARED_PID_FILE)
        sys.exit(1)
```

### الخطوة 2.2 — أعِد رسالة الطباعة الأصلية

السطر 19 حاليًا يطبع `[تم استلام رسالة] تم حفظ رسالة تليجرام في: ...` في سطر واحد. النصّ الأصلي كان سطرَين، والسطر الثاني هو ما يفهمه المحرر كإشارة إيقاظ. أعِده كما كان:

```python
    print(f"\n[تم استلام رسالة]: {msg_text}")
    print(f"تم الحفظ في {output_path}. جاري الإغلاق لإيقاظ المحرر...")
    sys.stdout.flush()
```

> ⚠️ إن كان أيّ hook أو إعداد في المحرر يطابق هذا النصّ حرفيًا، فتغييره يكسر الإيقاظ. أعِده كما كان بالضبط.

---

## الإصلاح #3 — إكمال المرحلة الرابعة المُتخطّاة

رسالة الـ commit تقول «Phase 0-4 complete» لكن `Telegram_Admin_Feature/README.md` ما زال 29 سطرًا بالمحتوى الأصلي: صفر إشارات إلى `telegram_core.py` أو `requirements.txt` أو جدول متغيّرات البيئة أو مخطّط الـ JSON.

### الخطوة 3.1 — نفّذ المرحلة 4.2 من `claude_plan.md`

راجع الأقسام التسعة المطلوبة في `claude_plan.md` (المرحلة 4، الخطوة 4.2) ونفّذها كلها. `requirements.txt` أُنشئ فعلًا — لا تُعِد إنشاءه.

### الخطوة 3.2 — وثّق سلوك القفل الجديد

أضِف إلى قسم استكشاف الأخطاء في الـ README:

- الوضع الرسمي المعتمَد هو `telegram_daemon.py`؛ و`poll_once.py` أداة تشخيص/احتياط.
- **يُمنع تشغيل السكربتَين معًا** — تليجرام يسمح بمستهلك `getUpdates` واحد فقط.
- الخروج بالرمز `2` يعني «القفل مشغول بعملية حيّة»، والحل هو `--takeover`.
- جدول الرموز: `0` نجاح · `1` خطأ تشغيلي · `2` القفل مشغول.

### الخطوة 3.3 — صحّح `.agents/HOOKS_GUIDE.md`

السطر 121 ينصّ على أنّ دورة الإيقاظ تُشغِّل `poll_once.py`. حدِّثه ليعكس الوضع المعتمَد (`telegram_daemon.py`) وليذكر قاعدة القفل الواحد، وإلا بقي التوثيق يوجّه إلى السلوك الذي كسر النظام.

---

## قائمة التحقّق (نفّذها بالترتيب — لا تعلن الاكتمال قبل رؤية المخرجات)

1. الترجمة تنجح:
   ```bash
   python -m py_compile Telegram_Admin_Feature/*.py && echo COMPILE_OK
   ```
2. القفل يرفض التشغيل المزدوج بدل أن يقتل بصمت — **مع بقاء الخادم يعمل**، شغّل:
   ```bash
   python Telegram_Admin_Feature/poll_once.py
   ```
   **المتوقّع:** رسالة «يوجد مستهلك تليجرام يعمل بالفعل (PID: ...)» وخروج بالرمز `2`، و**الخادم الأصلي ما زال حيًّا**. تحقّق من بقائه حيًّا:
   ```bash
   python -c "import psutil; p=int(open('Telegram_Admin_Feature/telegram_poller.pid').read().strip()); print('daemon alive:', psutil.pid_exists(p))"
   ```
   هذا الفحص هو **جوهر الإصلاح**. إن قُتل الخادم فالإصلاح فاشل.
3. القفل القديم (Stale) يُنظَّف: أوقف الخادم بـ `Ctrl+C`، ثم تأكّد أنّ `telegram_poller.pid` حُذف. أنشئه يدويًا بمحتوى `999999` ثم شغّل الخادم — يجب أن يبدأ بشكل طبيعي دون شكوى.
4. الاستيلاء الصريح يعمل: مع خادم يعمل، شغّل `python Telegram_Admin_Feature/telegram_daemon.py --takeover` — يجب أن يُنهي القديم ويبدأ هو.
5. الاستقبال ما زال يعمل: أرسل رسالة نصية من تليجرام وتأكّد من ظهور `[TELEGRAM_DAEMON_WAKEUP]` وتحديث `telegram_request.json` بـ `message_id` جديد.
6. الإرسال ما زال يعمل:
   ```bash
   python Telegram_Admin_Feature/send_reply.py "اختبار الإصلاح v2 ✅"
   ```
7. لا انحدار في بقية المشروع:
   ```bash
   python run_all_tests.py
   ```
8. `git ls-files | grep -iE "telegram_request|\.pid|downloads/"` لا يُخرج شيئًا.

---

## ما يجب **عدم** فعله

- ❌ **لا** تُعِد تنفيذ المراحل 0–3 من `claude_plan.md` — منتهية ومُتحقَّق منها.
- ❌ **لا** تُلغِ ملف PID المشترك ولا تُعِد ملفَّي PID منفصلَين — القفل الواحد صحيح، المشكلة كانت في القتل الصامت فقط.
- ❌ **لا** تُغيّر `send_reply.py` ولا `sync_artifacts_to_github.py` ولا `telegram_core.py` في أجزائها العاملة (`download_media`, `write_request`, `is_duplicate`, `load_config`) — كلها مُتحقَّق من عملها.
- ❌ **لا** تُغيّر `content_types` ولا قيَم مهل الـ polling.
- ❌ **لا** تُعِد كتابة تاريخ Git.
- ❌ **لا** تكتب في رسالة الـ commit أنّ مرحلةً اكتملت قبل تنفيذ قائمة التحقّق فعليًا ورؤية مخرجاتها — هذا ما حدث في `3770874` وأدّى إلى تخطّي المرحلة الرابعة بصمت.

</div>
