---
name: remote-futrx
description: "Autonomous cloud multi-agent container platform orchestrator — deploys, manages, and operates remote.futrx on Ubuntu 24.04 (Hostinger VPS) with LXD containers, Caddy wildcard SSL, headless agent runtimes (Claude Code, Antigravity agy, Codex, MiniMax, Kimi), live dev preview, and scheduled tasks."
---

# مهارة منصة الوكلاء السحابية المستقلة: remote-futrx
<!-- Cloud Multi-Agent Container Platform Orchestrator -->

تتيح هذه المهارة لوكلاء الذكاء الاصطناعي والمطورين نشر، تهيئة، إدارة، وتشغيل منصة **`remote.futrx`** كبيئة تشغيل سحابية مستقلة 24/7 للوكلاء البرمجيين المتعددين على خوادم **Hostinger VPS (Ubuntu 24.04 KVM)** داخل حاويات Linux معزولة (LXD Containers).

---

## 1. المبدأ المعماري والقيد 53 (Hybrid Cloud Architecture)

> [!IMPORTANT]
> **القيد 53 (Cloud VPS Multi-Agent Standard):**
> المنصات الخادومية المعتمدة على محركات حاويات لينكس (مثل LXD / Docker في `remote.futrx`) تُنشر وتُدار حصرياً على خوادم سحابية مستقلة (**Hostinger VPS Ubuntu 24.04**). 
> **يُحظر تماماً محاولة تثبيتها محلياً على Windows أو عبر WSL2** لحماية موارد حاسوب المطور وتفادي تعقيدات المحاكاة المتداخلة (Nested Virtualization) واستهلاك القرص C. يبقى حاسوب Windows مخصصاً للواجهات والبرمجة التفاعلية السريعة عبر Antigravity IDE.

### الطوبولوجيا التشغيلية (Runtime Topology)
```
                                 ┌─────────────────────────────────────────────────────────┐
                                 │                   Hostinger VPS (Ubuntu 24.04)          │
  المطور / المتصفح               │                                                         │
  ┌──────────────┐     HTTPS     │  ┌───────────────────────────────────────────────────┐  │
  │ Web Browser  │ ────────────> │  │ Caddy Reverse Proxy (Wildcard TLS + Cookie Strip) │  │
  └──────────────┘               │  └─────────────────┬─────────────────────────────────┘  │
                                 │                    │                                    │
                                 │                    ▼                                    │
                                 │  ┌───────────────────────────────────────────────────┐  │
                                 │  │ Go Backend Daemon (127.0.0.1:7682) [Runs as Root] │  │
                                 │  └─────────────────┬─────────────────────────────────┘  │
                                 │                    │ lxc CLI                            │
                                 │                    ▼                                    │
                                 │  ┌───────────────────────────────────────────────────┐  │
                                 │  │ LXD Container Daemon (Unprivileged Namespaces)    │  │
                                 │  └───┬───────────────────────────────┬───────────────┘  │
                                 │      │                               │                  │
                                 │      ▼ Project A                     ▼ Project B        │
                                 │  ┌───────────────────────┐       ┌───────────────────┐  │
                                 │  │ Ubuntu 24.04 Container│       │ Ubuntu Container  │  │
                                 │  │ • Claude Code CLI     │       │ • Antigravity agy │  │
                                 │  │ • code-server (:8842) │       │ • MiniMax / Kimi  │  │
                                 │  │ • Chromium (:6080)    │       │ • Dev App Ports   │  │
                                 │  └───────────────────────┘       └───────────────────┘  │
                                 └─────────────────────────────────────────────────────────┘
```

---

## 2. متطلبات ما قبل التثبيت وبوابة حماية SSH (Pre-Flight Safety Gate)

> [!CAUTION]
> **تحذير أمني حرج (SSH Lockout Risk):**
> يقوم سكربت التثبيت (`06-ssh-hardening.sh`) تلقائياً بتعطيل تسجيل الدخول بكلمة المرور (`PasswordAuthentication no`) وحصر الدخول بمفاتيح SSH العامة.
> **يجب التأكد بنسبة 100% من تسجيل مفتاح SSH العام الخاص بك في `~/.ssh/authorized_keys`** على خادم VPS واختبار الدخول بالمفتاح بنجاح قبل تشغيل أمر التثبيت.

### المتطلبات الدنيا لخادم VPS:
- **نظام التشغيل:** Ubuntu 24.04 LTS (x86_64).
- **المواصفات:** يُفضل باقة KVM 2 (2 vCPU, 8GB RAM, 100GB NVMe) كحد أدنى مريح، أو باقة KVM 1 (1 vCPU, 4GB RAM, 50GB NVMe) مع إلزام تفعيل ملف التبادل (4GB Swap).
- **المنافذ المفتوحة:** المنفذ 80 (HTTP) والمنفذ 443 (HTTPS) والمنفذ 22 (SSH).
- **صلاحيات root:** الوصول بصلاحية `sudo` أو المستخدم `root`.

---

## 3. إعداد النطاق و Wildcard DNS

تتطلب منصة Remote نطاقاً بنظام Subdomains العريضة (Wildcard) لتوجيه واجهات المشاريع والمعاينات ومحرر code-server تلقائياً مع إصدار شهادات SSL ديناميكية عبر Let's Encrypt:

### الخيار 1: نطاقك الخاص عبر Hostinger DNS
أضف سجلات A التالية مشيرة إلى عنوان IP خادم الـ VPS الخاص بك:

| اسم السجل (DNS Record) | النوع | القيمة (Target) | الغرض |
| :--- | :--- | :--- | :--- |
| `remote.yourdomain.com` | `A` | `<VPS_IP>` | واجهة التطبيق الرئيسية (Main App) |
| `code.remote.yourdomain.com` | `A` | `<VPS_IP>` | محرر الأكواد العام (Browser IDE) |
| `*.code.remote.yourdomain.com` | `A` | `<VPS_IP>` | محررات الأكواد لكل مشروع مستقل |
| `*.dev.remote.yourdomain.com` | `A` | `<VPS_IP>` | المعاينات الحية للمشاريع وتطبيقات الويب |

### الخيار 2: النطاق المجاني الفوري عبر DuckDNS
إذا لم تكن تمتلك نطاقاً مخصصاً، يمكنك استخدام [DuckDNS](https://www.duckdns.org):
1. قم بإنشاء اسم نطاق (مثل: `myagenthub.duckdns.org`).
2. اضبط عنوان IP ليشير إلى سيرفر الـ VPS.
3. يدعم DuckDNS كافة الـ Wildcards تلقائياً دون الحاجة لإضافة سجلات فرعية.

---

## 4. خطوات التثبيت على خادم Hostinger VPS

### الخطوة 1: الاتصال وتفعيل التبادل (Swap)
اتصل بالسيرفر عبر SSH ونفّذ أمر إعداد التبادل (لحماية السيرفر أثناء بناء الحاوية الأساسية):
```bash
# الاتصال بالسيرفر
ssh -i ~/.ssh/id_rsa root@<VPS_IP>

# إنشاء وتفعيل ملف التبادل 4GB
sudo fallocate -l 4G /swapfile
sudo chmod 600 /swapfile
sudo mkswap /swapfile
sudo swapon /swapfile
echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab
```

### الخطوة 2: تشغيل أمر التثبيت التلقائي الرسمي
```bash
curl -fsSL https://remote.futrx.com/get | sudo bash -s -- remote.yourdomain.com
```
*(استبدل `remote.yourdomain.com` باسم النطاق الذي أعددته في الخطوة 3).*

يقوم السكربت تلقائياً بـ:
1. تحديث الحزم وتثبيت Snapd و LXD و Git و Caddy و Go.
2. بناء واجهة الـ Frontend (Vite) وخلفية Go.
3. تكوين خدمة النظام `remote.futrx.service` وتشغيلها.
4. بناء صورة الحاوية الأساسية `futrx-remote-dev-base` (قد تستغرق 10-15 دقيقة لأول مرة).
5. تفعيل وتأمين Caddy وإصدار شهادات SSL التلقائية.

### الخطوة 3: استرداد رمز الإعداد الأولي (First-Time Setup Token)
بمجرد اكتمال التثبيت، اطبع رابط التفعيل الأول من سجلات النظام:
```bash
# استعراض رابط الإعداد
remote setup-token
# أو عبر سجلات systemd
journalctl -u remote --since "-10 min" | grep -A2 "first-time setup"
```
افتح الرابط في متصفحك (ينتهي بـ `?token=...`) وأنشئ حساب المسؤول الرئيسي (Admin).

---

## 5. إدارة وتشغيل الوكلاء (Agent CLI Bridge)

تدعم المنصة الوكلاء الخمسة الكبار عبر سطر الأوامر المستقل بدون نوافذ تفاعلية:

| الوكيل (Agent) | أمر الاستدعاء في الحاوية | نمط التدفق (Streaming) | وسائط الأمان والتشغيل |
| :--- | :--- | :--- | :--- |
| **Claude Code** | `claude -p "<prompt>"` | `--output-format stream-json` | `--dangerously-skip-permissions` |
| **Antigravity** | `agy --print "<prompt>"` | UTF-8 Stream | `--dangerously-skip-permissions --print-timeout 240m` |
| **Codex** | `codex --headless` | JSON-RPC Event Stream | `codexharness` |
| **MiniMax** | `codex --provider minimax` | Token Plan API Bridge | عبر محاكي Codex |
| **Kimi Code** | `kimi --headless` | Standard Stream | عبر واجهة Kimi المدمجة |

### ربط مفاتيح وحسابات الوكلاء (Host Provider Auth)
من لوحة التحكم في المتصفح:
1. انتقل إلى **Settings → Agents**.
2. قم بربط حساب **Claude Code** (OAuth) أو **Google Antigravity** أو إدخال مفتاح **MiniMax Token Plan**.
3. يتم تخزين المفاتيح مركزياً على السيرفر ومشاركتها بشكل مشفر مع حاويات المشاريع دون كشفها للمتصفح.

---

## 6. أوامر الصيانة والمراقبة واستكشاف الأخطاء (Cheat Sheet)

### فحص حالة المنصة والخدمات:
```bash
# حالة خدمة المنصة
sudo systemctl status remote.futrx

# مراقبة سجلات السيرفر اللحظية
sudo journalctl -u remote.futrx -f

# إعادة تشغيل المنصة
sudo systemctl restart remote.futrx
```

### فحص وإدارة حاويات LXD:
```bash
# استعراض الحاويات النشطة واستهلاكها
lxc list

# الدخول المباشر إلى حاوية مشروع معين
lxc exec <project-slug> -- bash

# فحص استهلاك الذاكرة والموارد للحاويات
lxc info <project-slug>
```

### تحديث المنصة إلى أحدث إصدار:
```bash
# من داخل مجلد التثبيت
cd /opt/remote.futrx
sudo bash infra/update.sh
```

---

## 7. أدوات الأتمتة المرفقة بالمهارة

تحتوي هذه المهارة على أدوات وأدلة تشغيل تخصصية:
- [`scripts/remote_vps_helper.py`](scripts/remote_vps_helper.py): سكربت بايثون لفحص الاتصال، التحقق من سجلات DNS للـ Wildcard، والتأكد من متطلبات التثبيت.
- [`references/architecture_topology.md`](references/architecture_topology.md): تفصيل المعمارية والطبقات والحدود الأمنية.
- [`references/hostinger_vps_playbook.md`](references/hostinger_vps_playbook.md): دليل تكوين VPS عبر أدوات Hostinger MCP.
- [`references/agent_cli_bridge.md`](references/agent_cli_bridge.md): توثيق بروتوكول استدعاء الـ CLIs بصيغة JSON.
