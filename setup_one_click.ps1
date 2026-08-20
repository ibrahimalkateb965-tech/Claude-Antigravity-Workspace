<#
.SYNOPSIS
    سكربت التثبيت والتفعيل الشامل بنقرة واحدة لمنظومة Claude & Antigravity
    One-Click Setup & Global Engine Activator
.DESCRIPTION
    يقوم هذا السكربت بإعداد وتفعيل المحرك المعماري بالكامل على جهاز المستخدم:
    1. التحقق من البيئة والمتطلبات (Python, Git).
    2. تهيئة وتعميم المهارات (80+) والوكلاء (25+) والإضافات (Plugins) عالمياً في ~/.gemini/config/.
    3. ربط وتفعيل محرك التخصيص والتنظيف وسكربتات المزامنة.
    4. فتح تطبيق مكتبة الأوامر التفاعلية في المتصفح.
#>

[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$ErrorActionPreference = "Continue"

Write-Host ""
Write-Host "=======================================================================" -ForegroundColor Cyan
Write-Host " 🚀 مرحباً بك في مثبت المحرك المعماري لمنظومة Claude & Antigravity" -ForegroundColor Yellow
Write-Host "=======================================================================" -ForegroundColor Cyan
Write-Host ""

$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$GlobalConfigDir = Join-Path $HOME ".gemini\config"

# 1. التحقق من مجلد المصدر
Write-Host "🔍 [1/5] التحقق من ملفات المنظومة والمستودع..." -ForegroundColor Green
$AgentsDir = Join-Path $ScriptDir ".agents"
$SkillsDir = Join-Path $AgentsDir "skills"
$SubAgentsDir = Join-Path $AgentsDir "Sub_Agent"

if (-not (Test-Path $SkillsDir) -or -not (Test-Path $SubAgentsDir)) {
    Write-Host "❌ خطأ: لم يتم العثور على مجلدات الوكلاء والمهارات في المسار الحالي!" -ForegroundColor Red
    Write-Host "يرجى تشغيل السكربت من المجلد الرئيسي للمستودع." -ForegroundColor Red
    Pause
    Exit 1
}

# 2. إنشاء المسارات العالمية
Write-Host "📁 [2/5] تجهيز مسار الإعدادات العالمي: $GlobalConfigDir" -ForegroundColor Green
$TargetSkills = Join-Path $GlobalConfigDir "skills"
$TargetSubAgents = Join-Path $GlobalConfigDir "Sub_Agent"
$TargetPlugins = Join-Path $GlobalConfigDir "plugins"
$TargetRefs = Join-Path $GlobalConfigDir "references"

New-Item -ItemType Directory -Force -Path $TargetSkills | Out-Null
New-Item -ItemType Directory -Force -Path $TargetSubAgents | Out-Null
New-Item -ItemType Directory -Force -Path $TargetPlugins | Out-Null
New-Item -ItemType Directory -Force -Path $TargetRefs | Out-Null

# 3. نشر المهارات والوكلاء والإضافات عالمياً
Write-Host "📦 [3/5] تعميم المهارات البرمجية والوكلاء والإضافات عالمياً..." -ForegroundColor Green

# نسخ المهارات
Copy-Item -Path "$SkillsDir\*" -Destination $TargetSkills -Recurse -Force
$SkillCount = (Get-ChildItem -Directory $TargetSkills).Count
Write-Host "   ✅ تم تفعيل $SkillCount مهارة تخصصية بنجاح." -ForegroundColor Gray

# نسخ الوكلاء
Copy-Item -Path "$SubAgentsDir\*" -Destination $TargetSubAgents -Recurse -Force
$AgentCount = (Get-ChildItem -File "$TargetSubAgents\*.yaml").Count
Write-Host "   ✅ تم تفعيل $AgentCount وكيلاً متخصصاً بنجاح." -ForegroundColor Gray

# نسخ الإضافات والمراجع إن وجدت
$PluginsSrc = Join-Path $ScriptDir "plugins"
if (Test-Path $PluginsSrc) {
    Copy-Item -Path "$PluginsSrc\*" -Destination $TargetPlugins -Recurse -Force
    Write-Host "   ✅ تم تفعيل حزم الإضافات (Plugins) عالمياً." -ForegroundColor Gray
}

$RefsSrc = Join-Path $ScriptDir "references"
if (Test-Path $RefsSrc) {
    Copy-Item -Path "$RefsSrc\*" -Destination $TargetRefs -Recurse -Force
    Write-Host "   ✅ تم تعميم مراجع وقواعد الذاكرة عالمياً." -ForegroundColor Gray
}

# 4. نسخ ملفات النواة والدساتير المعمارية
Write-Host "🛡️ [4/5] نشر الدساتير المعمارية ومحركات التخصيص والمزامنة..." -ForegroundColor Green
$CoreFiles = @(
    "AGENTS.md",
    "HOOKS_GUIDE.md",
    "ACTIVE_CONTEXT_INJECTION.md",
    "project_agent_tailor.py",
    "sync_local_agents_template.py",
    "mcp_config.json",
    "config.json"
)

foreach ($file in $CoreFiles) {
    $src = Join-Path $ScriptDir $file
    if (-not (Test-Path $src)) {
        $src = Join-Path $AgentsDir $file
    }
    if (Test-Path $src) {
        Copy-Item -Path $src -Destination $GlobalConfigDir -Force
    }
}
Write-Host "   ✅ تم تعميم الدستور المعماري وقواعد الـ 21 خطافاً بنجاح." -ForegroundColor Gray

# 5. تحديث الجداول وتشغيل مكتبة الأوامر
Write-Host "⚡ [5/5] إطلاق مكتبة الأوامر التفاعلية والتحقق النهائي..." -ForegroundColor Green

# تشغيل بايثون لتحديث الإكسيل ومكتبة الأوامر إن وجد
if (Get-Command python -ErrorAction SilentlyContinue) {
    $ConvScript = Join-Path $AgentsDir "convert_hooks_to_sheets.py"
    if (Test-Path $ConvScript) {
        python $ConvScript | Out-Null
    }
    $HtmlScript = Join-Path $AgentsDir "update_html.py"
    if (Test-Path $HtmlScript) {
        python $HtmlScript | Out-Null
    }
}

$HtmlPath = Join-Path $ScriptDir "03_Dynamic_Prompt_Library\index.html"
if (Test-Path $HtmlPath) {
    Start-Process $HtmlPath
    Write-Host "   🌐 تم فتح تطبيق مكتبة الأوامر التفاعلية في المتصفح!" -ForegroundColor Cyan
}

Write-Host ""
Write-Host "=======================================================================" -ForegroundColor Green
Write-Host " ✨ مبروك! تم تفعيل المحرك المعماري لمنظومة Claude & Antigravity بنجاح" -ForegroundColor Yellow
Write-Host "=======================================================================" -ForegroundColor Green
Write-Host ""
Write-Host "📌 الخطوات التالية لبدء العمل فوراً:" -ForegroundColor White
Write-Host "  1. افتح بيئة Antigravity IDE أو VS Code على مجلد مشروعك." -ForegroundColor Gray
Write-Host "  2. في مربع الدردشة، اكتب كلمة: 'بسم الله' لبدء تهيئة الجلسة." -ForegroundColor Gray
Write-Host "  3. استخدم تطبيق مكتبة الأوامر في المتصفح لنسخ وتجربة الخطافات الذكية." -ForegroundColor Gray
Write-Host ""
