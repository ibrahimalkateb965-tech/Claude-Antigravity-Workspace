# دليل تشغيل منصة remote.futrx على خوادم Hostinger VPS
<!-- Hostinger VPS Deployment & MCP Automation Playbook -->

## 1. التجهيز السحابي لخادم Hostinger VPS

عند استخدام خوادم Hostinger VPS (Ubuntu 24.04 64-bit):
- **الحد الأدنى الموصى به:** باقة **KVM 2** (ذاكرة 8GB، مساحة 100GB NVMe، معالجان vCPU) لضمان سرعة بناء صور الحاويات واستيعاب عدة مشاريع في وقت واحد.
- **الباقة الاقتصادية:** باقة **KVM 1** (ذاكرة 4GB، مساحة 50GB NVMe، معالج 1 vCPU) تتطلب حتماً إنشاء ملف تبادل (4GB Swap File) قبل تشغيل أمر التثبيت لتجنب انهيار الذاكرة.

### 🌟 بيانات السيرفر الفعلي النشط للمنظومة (Verified Live VPS):
تم التحقق عبر أدوات Hostinger MCP من توفر سيرفر VPS نشط ومجهز للمنظومة:
- **معرف الخادم (VPS ID):** `1810150`
- **اسم المضيف (Hostname):** `srv1810150.hstgr.cloud`
- **عنوان IP الثابت:** `187.55.226.225`
- **نظام التشغيل:** `Ubuntu 24.04 with Docker`
- **الباقة:** `KVM 1` (ذاكرة 4GB RAM + مساحة 50GB NVMe)
- **النطاق المعتمد للربط:** `autovem.tech` (يشير بالفعل إلى `187.55.226.225`)
- **رابط المنصة المقترح:** `remote.autovem.tech` مع تفعيل الـ Wildcards:
  - `*.code.remote.autovem.tech` -> `187.55.226.225`
  - `*.dev.remote.autovem.tech` -> `187.55.226.225`

---

## 2. إدارة وتأمين الجدار الناري والمفاتيح عبر أدوات Hostinger MCP

يوفر سيرفر الـ MCP لخوادم Hostinger أدوات برمجية لأتمتة التهيئة قبل التثبيت:

### 1) حقن المفتاح العام لحماية SSH (منع الإغلاق المفاجئ):
تأكد من استخدام أداة `VPS_attachPublicKeyV1` لربط مفتاح SSH العام بالسيرفر:
```json
{
  "ServerName": "hostinger",
  "ToolName": "VPS_attachPublicKeyV1",
  "Arguments": {
    "virtualMachineId": "<VM_ID>",
    "publicKeyId": "<KEY_ID>"
  }
}
```

### 2) فتح المنافذ الضرورية في الجدار الناري:
استخدم `VPS_createFirewallRuleV1` لفتح المنافذ الثلاثة الإلزامية:
- المنفذ 22 (SSH - TCP)
- المنفذ 80 (HTTP - TCP)
- المنفذ 443 (HTTPS - TCP)

---

## 3. ربط وتكوين سجلات DNS عبر Hostinger DNS

عند إدارة النطاق عبر Hostinger، يمكن إضافة سجلات الـ Wildcard مباشرة:

1. سجل A للنطاق الرئيسي:
   - Name: `remote` -> Target: `<VPS_IP>`
2. سجل A لمحرر الكود العام:
   - Name: `code.remote` -> Target: `<VPS_IP>`
3. سجل A العريض لمحررات المشاريع:
   - Name: `*.code.remote` -> Target: `<VPS_IP>`
4. سجل A العريض لمعاينات المشاريع وتطبيقات الويب:
   - Name: `*.dev.remote` -> Target: `<VPS_IP>`

---

## 4. سكربت التهيئة والتثبيت المباشر (One-Shot Bootstrap)

نفذ هذه الأوامر متتالية على السيرفر:

```bash
#!/usr/bin/env bash
set -euo pipefail

# 1. إعداد ملف التبادل 4GB
if [ ! -f /swapfile ]; then
    echo "==> Creating 4GB Swap file..."
    fallocate -l 4G /swapfile
    chmod 600 /swapfile
    mkswap /swapfile
    swapon /swapfile
    echo '/swapfile none swap sw 0 0' >> /etc/fstab
fi

# 2. تحديث الحزم الأساسية
apt-get update -qq && apt-get install -y -qq curl git ca-certificates ufw

# 3. فتح المنافذ في UFW
ufw allow 22/tcp
ufw allow 80/tcp
ufw allow 443/tcp
ufw --force enable

# 4. تشغيل مثبت remote.futrx
echo "==> Running remote.futrx installer..."
curl -fsSL https://remote.futrx.com/get | sudo bash -s -- remote.yourdomain.com
```

---

## 5. استعادة الوصول والتعافي في حالات الطوارئ (Emergency Recovery)

إذا حدث انقطاع في الاتصال أو واجهت مشكلة في شهادات SSL:
- **الوصول عبر لوحة Hostinger:** استخدم ميزة **Web Terminal / VNC Console** المدمجة في لوحة تحكم Hostinger VPS للوصول الفوري للسيرفر بصلاحية root دون الحاجة لـ SSH.
- **فحص سجلات التثبيت:**
  ```bash
  cat /opt/remote.futrx/install.log
  journalctl -u remote.futrx -n 100 --no-pager
  ```
- **إعادة استخراج رمز المسؤول:**
  ```bash
  remote setup-token
  ```
