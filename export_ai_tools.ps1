<#
.SYNOPSIS
    أمر التصدير السريع لأدوات الذكاء الاصطناعي إلى المستودع المركزي
    Universal AI Tools Exporter to Central Hub

.DESCRIPTION
    يقوم بنسخ وتصدير كافة أدوات الذكاء الاصطناعي المحدثة (الوكلاء، المهارات، الذاكرة،
    القواعد، وقوالب الأسطول) من المشروع الحالي أو مشروع محدد إلى:
    F:\AI PROJECTS\Claude+Antigravity

.PARAMETER Source
    مسار المشروع المصدر (افتراضياً: المجلد الحالي)

.PARAMETER All
    تصدير ومزامنة كافة المشاريع في F:\AI PROJECTS\
#>

[CmdletBinding()]
param (
    [Parameter(Position=0)]
    [string]$Source = (Get-Location).Path,

    [switch]$All
)

$CentralHub = "F:\AI PROJECTS\Claude+Antigravity"
$PythonScript = Join-Path $CentralHub "sync_global_ecosystem.py"

if (-not (Test-Path $PythonScript)) {
    Write-Error "تعذر العثور على سكربت المزامنة المركزي: $PythonScript"
    exit 1
}

Write-Host "================================================================" -ForegroundColor Cyan
Write-Host " 🚀 تشغيل وكيل الأوامر لتصدير أدوات الذكاء الاصطناعي للمركز" -ForegroundColor Green
Write-Host " 🎯 المستودع المستهدف: $CentralHub" -ForegroundColor Yellow
Write-Host "================================================================" -ForegroundColor Cyan

if ($All) {
    & python "$PythonScript" --all
} else {
    Write-Host " 📁 المصدر: $Source" -ForegroundColor Gray
    & python "$PythonScript" --from "$Source"
}

if ($LASTEXITCODE -eq 0) {
    Write-Host "`n✅ تم التصدير والمزامنة بنجاح تام إلى المستودع المركزي!" -ForegroundColor Green
} else {
    Write-Host "`n❌ حدث خطأ أثناء التصدير (كود: $LASTEXITCODE)" -ForegroundColor Red
}
