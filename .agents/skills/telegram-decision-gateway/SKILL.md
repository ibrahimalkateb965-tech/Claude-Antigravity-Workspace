---
name: telegram-decision-gateway
description: >
  بوابة القرار الآني عبر تليجرام — يُستخدم عندما يحتاج الوكيل لقرار من المستخدم
  (اختيار بين خيارين، موافقة على خطة، تحديد أولوية) دون انتظار تفاعل مباشر في المحرر.
  يُفعَّل بالمحفّزات: "انتظر قراري", "اسألني عبر تليجرام", "شغل البوت", "تفعيل تليجرام".
triggers:
  - "انتظر قراري"
  - "اسألني عبر تليجرام"
  - "شغل البوت"
  - "تفعيل تليجرام"
  - "Telegram Bot"
---

# مهارة: بوابة القرار عبر تليجرام

## الغرض
استقبال قرار آني من المستخدم عبر تليجرام عندما يحتاج الوكيل تأكيداً قبل الاستمرار.

## مسار الأداة
```
F:\AI PROJECTS\Claude+Antigravity\Telegram_Admin_Feature\
├── poll_once.py          ← استقبال رسالة واحدة → telegram_request.json
├── send_reply.py         ← إرسال ردود نصية للمستخدم
├── telegram_core.py      ← المكتبة الأساسية (لا تعدّل)
├── .env                  ← TOKEN + ALLOWED_CHAT_ID (لا تُلمس)
└── reply_*.txt           ← قوالب الردود الجاهزة
```

## سير العمل القياسي

### الخطوة 1 — إرسال السؤال للمستخدم
```powershell
# اكتب الرد في ملف نصي مؤقت ثم أرسله
Set-Content "F:\AI PROJECTS\Claude+Antigravity\Telegram_Admin_Feature\reply_question.txt" `
  "🤔 أحتاج قرارك:
الخيار الأول: [وصف الخيار 1]
الخيار الثاني: [وصف الخيار 2]
أرسل: 'الخيار الأول' أو 'الخيار الثاني'" -Encoding UTF8

python "F:\AI PROJECTS\Claude+Antigravity\Telegram_Admin_Feature\send_reply.py" `
  --file "F:\AI PROJECTS\Claude+Antigravity\Telegram_Admin_Feature\reply_question.txt"
```

### الخطوة 2 — تشغيل بوابة الاستقبال (تنتظر رسالة واحدة)
```powershell
python "F:\AI PROJECTS\Claude+Antigravity\Telegram_Admin_Feature\poll_once.py"
# تخرج تلقائياً فور استقبال الرسالة وتوقظ المحرر
```

### الخطوة 3 — قراءة القرار والاستئناف
```powershell
$json = Get-Content `
  "F:\AI PROJECTS\Claude+Antigravity\Telegram_Admin_Feature\telegram_request.json" `
  -Encoding UTF8 | ConvertFrom-Json

$decision = $json.text   # → "الخيار الثاني افضل"
$user     = $json.user   # → "Ibrahim Alkateb"

Write-Host "قرار المستخدم: $decision"
```

### الخطوة 4 — إرسال تأكيد الاستلام
```powershell
# استخدم أحد القوالب الجاهزة:
python "...\send_reply.py" --file "reply_approved.txt"
# أو اكتب رداً مخصصاً مباشرةً
```

## قواعد صارمة
- **لا تُشغّل `poll_once.py` مرتين في آنٍ واحد** — يوجد قفل PID يمنع التداخل.
- **لا تحذف `telegram_request.json`** قبل قراءته — الوكيل يتحقق من `message_id` لتجنب التكرار.
- **لا تُضمّن TOKEN أو CHAT_ID في الكود** — يُحمَّلان حصراً من `.env`.
- الملفات `reply_*.txt` هي قوالب مُعتمدة — أضف قوالب جديدة ولا تعدّل القديمة.

## سيناريوهات الاستخدام المُثبتة
| السيناريو | القرار المُستقبَل | النتيجة |
|---|---|---|
| اختيار بين نسختي تطبيق عهدة | "الخيار الثاني افضل" | بناء النسخة الفارغة + package |
| الموافقة على خطة GitHub Pages | "وافق" | رفع الملفات ونشرها |
| تأكيد اكتمال مرحلة EduTrackPro | "تمام" | الانتقال للمرحلة التالية |
