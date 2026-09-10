# المرجع المعماري لمنصة remote.futrx
<!-- Architecture Topology & Security Boundaries Reference -->

## 1. نظرة عامة على الهيكلية المعمارية

تعتمد منصة `remote.futrx` معمارية خادومية مدمجة (Single-Host Self-Hosted Architecture) مصممة للعمل على نظام **Ubuntu 24.04 LTS**، حيث يتم عزل كل مشروع داخل حاوية **LXD** مستقلة غير ممتازة (Unprivileged Container).

### المكونات الرئيسية:
1. **خادم الواجهة الخلفية (Go Backend):**
   - عملية تنفيذية أحادية (Single Binary) تستمع حصرياً على المنفذ الداخلي `127.0.0.1:7682`.
   - تعمل بصلاحية `root` لأنها تقود سطر أوامر `lxc` وتدير نقاط الوصل (Bind Mounts) وملكية الملفات لمعرفات الحاويات (UID/GID Mapping).
   - توفر واجهات REST، وقنوات WebSocket للبث اللحظي، ومحرك Preact/React المدمج.

2. **بروكسي Caddy (Public HTTPS Edge):**
   - يستمع حصرياً على المنفذين العامين 80 و 443.
   - يدير شهادات SSL التلقائية عبر Let's Encrypt مع ميزة **On-Demand TLS** للنطاقات الفرعية.
   - يطبق سياسة **تجريد الكوكيز (Cookie Stripping)**: إزالة كوكيز المنصة الحساسة (`remote_session`, `remote_share`, `remote_oauth_state`) قبل توجيه أي طلب لمعاينات المشاريع لحماية الجلسات من الأكواد الخبيثة.

3. **حاويات المشاريع (LXD Project Containers):**
   - حاويات غير ممتازة تشارك نواة لينكس وتُعزل في مساحات أسماء (User & PID Namespaces).
   - مبنية على صورة مجهزة مسبقاً (`futrx-remote-dev-base`).
   - تحتوي على: Node 22، Chromium للـ Headed Browsing، محرر الأكواد `code-server` على المنفذ `8842`، خادم VNC للـ Browser على المنفذ `6080`، ومحركات الوكلاء البرمجية.

---

## 2. جدول توجيه المنافذ والشبكات (Routing & Host Classes)

| نمط النطاق (Host Pattern) | الوجهة (Upstream Target) | سياسة المصادقة والتأمين |
| :--- | :--- | :--- |
| `remote.domain.com` | Go Backend على `127.0.0.1:7682` | فحص الجلسة عبر كوكيز `remote_session` |
| `code.remote.domain.com` | `code-server` على المنفذ `8842` | مصادقة `forward_auth` للمستخدمين المسجلين |
| `<slug>.code.remote.domain.com` | `code-server` للمشروع المحدد | التحقق من صلاحية المستخدم المسجل |
| `<slug>--<port>.dev.remote.domain.com` | خادم التطوير الداخلي `<slug>.lxd:<port>` | مصادقة عضوية المشروع وتجريد الكوكيز تماماً |
| `<slug>--6080.dev.remote.domain.com` | Chromium noVNC على المنفذ `6080` | مصادقة عضوية المشروع وتجريد الكوكيز |

---

## 3. طبقات التخزين واستمرارية البيانات (Data Persistence)

- **بيانات المنصة:** تُحفظ في `/opt/remote.futrx/data/`:
  - `auth/`: ملفات JSON لمصادقة المسؤولين والمستخدمين.
  - `projects/`: تعريفات المشاريع والمتغيرات السرية المشفرة.
  - `chats/`: سجلات JSONL للأحداث والرسائل مع فهرس SQLite خفيف للبحث السريع.
- **مساحات عمل المشاريع (`/workspace`):**
  - مسار تخزين حقيقي على السيرفر المضيف يتم ربطه بنظام (Bind-Mount) داخل الحاوية.
  - تبقى ملفات الكود والـ Git آمنة حتى لو تم تدمير أو إعادة بناء الحاوية بالكامل.
