# جسر تشغيل الوكلاء البرمجية في remote.futrx
<!-- Headless Agent CLI Bridge & Stream Protocol Reference -->

## 1. فلسفة تشغيل الوكلاء في البيئة المعزولة

في منصة `remote.futrx`، لا يتم تشغيل واجهات المستخدم الرسومية للوكلاء، بل يتم استدعاؤهم كأوامر طرفية مستقلة (Headless CLI Subprocesses) داخل حاوية المشروع، مع التقاط قنوات `stdout` و `stderr` وتحويلها لحظياً إلى أحداث JSON عبر قنوات WebSocket للمتصفح.

---

## 2. تفاصيل استدعاء Claude Code CLI

يتم استدعاء `claude` مع إيقاف طلبات الصلاحيات التفاعلية وتفعيل تدفق الـ JSON:

```bash
claude \
  -p "<prompt>" \
  --output-format stream-json \
  --include-partial-messages \
  --verbose \
  --dangerously-skip-permissions \
  --model "<selected-model>" \
  --effort "<effort-level>"
```

### استئناف الجلسات والتفرع (Session Resume & Fork):
- **الاستئناف:** يمرر علم `--resume <session_id>` لإكمال المحادثة بنفس الذاكرة السياقية.
- **التفرع (Fork):** يمرر علم `--fork-session` لإنشاء مسار تجريبي مستقل دون التعديل على الجلسة الأصلية.

---

## 3. تفاصيل استدعاء Antigravity CLI (`agy`)

يتم استدعاء أداة Antigravity CLI داخل الحاوية بالأمر:

```bash
agy \
  --print "<prompt>" \
  --print-timeout 240m \
  --dangerously-skip-permissions \
  --model "<selected-model>" \
  --effort "<effort-level>"
```

### معالجة تدفق النصوص (UTF-8 Stream):
- تستقبل المنصة تدفق الـ UTF-8 من `agy` وتقوم بتجميعه ومعالجته عبر مجزئ الأحداث (`utf8_stream.go`) لتقسيم الكتل البرمجية، الردود النصية، وأدوات التنفيذ وعرضها بتنسيق جذاب في واجهة المتصفح.

---

## 4. استدعاء Codex و MiniMax و Kimi

- **Codex:** يعمل عبر `codex` مع تمرير بروتوكول JSON-RPC لاستقبال تدفقات الأحداث والتحكم في شجرة الملفات.
- **MiniMax:** يستفيد من بنية Codex (`codexharness`) مع توجيه الاتصال لمزود MiniMax باستخدام مفتاح اشتراك خطة التوكنز (Token Plan Key).
- **Kimi Code:** يعمل بنمط سطر الأوامر المستقل مع دعم نماذج Kimi الطويلة لنافذة السياق.

---

## 5. تكامل المتصفح المشترك (Headless / Headed Chromium via noVNC)

عند حاجة أي وكيل لتصفح موقع ويب، تشغيل اختبارات، أو فحص واجهات:
1. تقوم الحاوية بتشغيل جلسة Chromium حقيقية موصولة بشاشة وهمية (Xvfb).
2. يتم تشغيل خادم `x11vnc` ومحول `websockify` عبر المنفذ `6080`.
3. يربط Caddy المنفذ بالرابط الفرعي `<slug>--6080.dev.<host>` لتمكين المطور من مشاهدة المتصفح مباشرة والتفاعل معه يدوياً في أي لحظة.
