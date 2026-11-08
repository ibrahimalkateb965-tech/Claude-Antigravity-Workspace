---
name: vibe-coding-security-shield
description: "Six-pillar defense shield for AI & Vibe Coding: automates and orchestrates Semgrep (SAST), Gitleaks (Secrets), Trivy (CVEs & Dependencies), OWASP ZAP (DAST), Checkov (IaC), and Renovate (Dependency PRs) before production deployment."
---

# Vibe Coding Security Shield (درع أمان البرمجة بالذكاء الاصطناعي)

إطار العمل الدفاعي الشامل المخصص لمطوري الـ Vibe Coding وبيئات تطوير الذكاء الاصطناعي، لسد الثغرات وتأمين الأكواد والمفاتيح والتبعيات والبنية التحتية قبل رفع أي مشروع لبيئة الإنتاج (Production).

---

## الركائز الدفاعية الست (The 6 Pillars)

| الركيزة | الأداة | النطاق المستهدف | التوقيت والتنفيذ | الدور الأساسي |
| :--- | :--- | :--- | :--- | :--- |
| **1. كود المصدر** | `Semgrep` | Static Analysis (SAST) | فحص محلي + CI/CD | كشف ثغرات المنطق، أخطاء الحقن، وأنماط الـ AI غير الآمنة. |
| **2. الأسرار والمفاتيح** | `Gitleaks` | Secret Scanning | Pre-commit + Git History | منع تسريب مفاتيح API والـ Tokens وكلمات المرور في المستودع. |
| **3. التبعيات والحاويات** | `Trivy` | Dependencies & Images | مرحلة البناء + CI/CD | كشف ثغرات CVEs في المكتبات الخارجية وصور Docker وتوليد SBOM. |
| **4. التطبيق بعد التشغيل** | `OWASP ZAP` | Dynamic Analysis (DAST) | بيئة Staging / الاختبار | فحص الواجهات ونقاط الـ API ضد ثغرات OWASP Top 10 بعد التشغيل. |
| **5. البنية التحتية** | `Checkov` | Infrastructure as Code (IaC) | فحص محلي + CI/CD | كشف الـ Misconfigurations في Dockerfile و Terraform و Kubernetes. |
| **6. التحديثات الدورية** | `Renovate` | Dependency Automation | تشغيل تلقائي في الخلفية | مراقبة حزم التبعيات وإرسال Pull Requests مؤتمتة لترقية النسخ وسد الثغرات. |

---

## 1. فحص كود المصدر (Semgrep - SAST)

### الهدف:
فحص الأكواد المولدة بنماذج الذكاء الاصطناعي للتأكد من خلوها من الثغرات الشائعة (SQL Injection, XSS, Insecure Deserialization, Hardcoded URLs).

### أوامر التشغيل السريع:
```bash
# فحص سريع للمستودع باستخدام حزم القواعد الموصى بها لأمان الويب
semgrep scan --config auto

# فحص مركز على ثغرات OWASP Top 10
semgrep scan --config "p/owasp-top-ten"

# فحص كود بايثون وجافاسكريبت المولد بالذكاء الاصطناعي
semgrep scan --config "p/security-audit" --error
```

### توصيات Vibe Coding:
- تشغيل `semgrep scan --config auto` قبل عمل أي commit رئيسي.
- حظر استخدام التوابع الحساسة مثل `eval()`, `dangerouslySetInnerHTML`, أو الاتصالات غير المشفرة `http://`.

---

## 2. درع حماية الأسرار (Gitleaks - Secret Scanning)

### الهدف:
منع كارثة تسريب المفاتيح الحساسة (OpenAI Keys, Claude Keys, AWS Credentials, Database Passwords) إلى GitHub.

### ملف التهيئة المعتمد (`.gitleaks.toml`):
```toml
title = "Vibe Coding Secret Shield"

[extend]
useDefault = true

[allowlist]
description = "Ignore documentation and sample envs"
paths = [
    '''\.env\.example$''',
    '''README\.md$''',
    '''package-lock\.json$'''
]
```

### أوامر التشغيل:
```bash
# فحص الملفات المجهزة للرفع (Staged files)
gitleaks protect --staged -v

# فحص سجل المستودع التاريخي بالكامل للتأكد من عدم وجود تسريبات سابقة
gitleaks detect --verbose
```

### تفعيل الخطاف الوقائي (Pre-Commit Hook):
إضافة السطر التالي في `.git/hooks/pre-commit`:
```bash
#!/bin/sh
gitleaks protect --staged --verbose
```

---

## 3. فحص التبعيات وصور الحاويات (Trivy - Supply Chain Security)

### الهدف:
التأكد من أن المكتبات الخارجية (npm, pip, pub, go modules) وصور الحاويات لا تحتوي على ثغرات منشورة (CVEs) حرجة.

### أوامر الفحص:
```bash
# فحص مجلد المشروع وملفات القفل (package-lock, yarn.lock, requirements.txt, pubspec.lock)
trivy fs --severity HIGH,CRITICAL .

# فحص ملفات Dockerfile بحثاً عن صور أساسية ضعيفة
trivy config ./Dockerfile

# فحص صورة دوكر كاملة
trivy image my-app:latest
```

---

## 4. فحص التطبيق بعد التشغيل (OWASP ZAP - DAST)

### الهدف:
اختبار تطبيق الويب ونقاط الـ REST / GraphQL APIs بعد تشغيلها في بيئة الاختبار (Local / Staging) لمحاكاة هجمات الاختراق الفعلية.

### تشغيل الفحص الأساسي عبر Docker:
```bash
# فحص أساسي سريع (Baseline Scan) لموقع محلي أو تجريبي
docker run -t ghcr.io/zaproxy/zaproxy:stable zap-baseline.py -t https://staging.example.com -r zap_report.html

# فحص نقاط الـ API باستخدام مواصفات OpenAPI / Swagger
docker run -t ghcr.io/zaproxy/zaproxy:stable zap-api-scan.py -t https://staging.example.com/swagger.json -f openapi -r api_report.html
```

---

## 5. فحص أمان البنية التحتية (Checkov - IaC Guard)

### الهدف:
اكتشاف الأخطاء التكوينية (Misconfigurations) في ملفات Dockerfile, Docker Compose, Terraform, و Kubernetes التي قد تفتح ثغرات غير مقصودة في السيرفرات.

### أوامر الفحص:
```bash
# فحص كامل ملفات البنية التحتية في المشروع
checkov -d .

# فحص ملف Dockerfile فقط
checkov -f Dockerfile --framework dockerfile

# فحص مستودع Terraform مع استثناء التحذيرات البسيطة
checkov -d ./terraform --soft-fail
```

---

## 6. الأتمتة المستمرة للتحديثات (Renovate Bot)

### الهدف:
مراقبة كافة مكتبات المشروع وإرسال Pull Requests تلقائياً بمجرد صدور تحديث أمني، لتجنب تراكم الديون التقنية للنسخ القديمة.

### ملف التهيئة المعياري (`renovate.json`):
```json
{
  "$schema": "https://docs.renovatebot.com/renovate-schema.json",
  "extends": [
    "config:recommended",
    ":dependencyDashboard",
    ":semanticCommits"
  ],
  "packageRules": [
    {
      "matchUpdateTypes": ["patch", "pin", "digest"],
      "automerge": true
    },
    {
      "matchUpdateTypes": ["minor"],
      "matchPackagePatterns": ["*"],
      "groupName": "minor dependencies"
    },
    {
      "matchUpdateTypes": ["major"],
      "labels": ["breaking-change", "needs-review"]
    }
  ],
  "vulnerabilityAlerts": {
    "labels": ["security"],
    "assignees": ["@developer"]
  }
}
```

---

## خط سير عمل GitHub Actions المتكامل (`vibe-security-shield.yml`)

يمكن نسخ القالب التالي إلى `.github/workflows/vibe-security-shield.yml` لتفعيل الفحص التلقائي عند كل Pull Request:

```yaml
name: Vibe Coding Security Shield

on:
  push:
    branches: [ main, master, develop ]
  pull_request:
    branches: [ main, master ]

jobs:
  code-and-secrets:
    name: Code & Secrets Inspection
    runs-on: ubuntu-latest
    steps:
      - name: Checkout Code
        uses: actions/checkout@v4
        with:
          fetch-depth: 0

      - name: Run Gitleaks Secret Scanner
        uses: gitleaks/gitleaks-action@v2
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}

      - name: Run Semgrep SAST
        uses: returntocorp/semgrep-action@v1
        with:
          config: >-
            p/security-audit
            p/owasp-top-ten

  dependencies-and-iac:
    name: Dependencies & IaC Inspection
    runs-on: ubuntu-latest
    steps:
      - name: Checkout Code
        uses: actions/checkout@v4

      - name: Run Trivy Vulnerability Scanner
        uses: aquasecurity/trivy-action@master
        with:
          scan-type: 'fs'
          ignore-unfixed: true
          severity: 'CRITICAL,HIGH'

      - name: Run Checkov IaC Scanner
        uses: bridgecrewio/checkov-action@master
        with:
          framework: all
          output_format: cli
          soft_fail: true
```

---

## قائمة التحقق الذاتي قبل النشر (Pre-Production Self-Check)
- [ ] فحص الكود خالٍ من أخطاء Semgrep الحرجة.
- [ ] فحص Gitleaks يؤكد عدم وجود أي Secrets في الـ Staging أو الـ Git History.
- [ ] فحص Trivy يؤكد خلو ملفات القفل (Lockfiles) من مكتبات ذات ثغرات CRITICAL.
- [ ] فحص Checkov يؤكد تشغيل الحاويات بغير مستخدم root وعدم كشف منافذ غير ضرورية.
- [ ] تطبيق Web / API خضع لفحص ZAP الأساسي بعد النشر التجريبي.
