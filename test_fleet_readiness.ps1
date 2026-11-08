<#
.SYNOPSIS
    سكربت فحص واختبار جاهزية أسطول الـ CLIs على الجهاز الثاني
    Multi-CLI Fleet Readiness & Diagnostics Suite
.DESCRIPTION
    يقوم هذا السكربت بفحص واختبار كافة أدوات أسطول الـ CLIs والبيئة البرمجية:
    1. فحص بيئات التشغيل: Node.js, npm, Python, Git.
    2. فحص أدوات أسطول الـ CLIs: Claude Code, OpenCode CLI, Antigravity CLI.
    3. فحص أدوات البناء: Java JDK 17, Android SDK & ADB.
    4. فحص المجلد العالمي للمهارات والوكلاء (~/.gemini/config).
    5. إصدار بطاقة درجات تفصيلية ملونة (Scorecard) مع أوامر التثبيت السريعة للمكونات الناقصة.
#>

[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$ErrorActionPreference = "Continue"

Write-Host ""
Write-Host "================================================================================" -ForegroundColor Cyan
Write-Host " 🚀 فحص واختبار جاهزية أسطول الـ CLIs على الحاسوب الثاني (Fleet Diagnostics) " -ForegroundColor Yellow
Write-Host "================================================================================" -ForegroundColor Cyan
Write-Host ""

$PassedCount = 0
$TotalCount = 0

function Test-Component {
    param(
        [string]$Name,
        [scriptblock]$CheckBlock,
        [string]$InstallCommand
    )
    $script:TotalCount++
    Write-Host -NoNewline "🔍 [$script:TotalCount] فحص $Name... "
    try {
        $result = & $CheckBlock
        if ($result.Success) {
            Write-Host "[ جاهز ✅ ]" -ForegroundColor Green
            Write-Host "    └─ الإصدار: $($result.Version)" -ForegroundColor DarkGray
            $script:PassedCount++
        } else {
            Write-Host "[ غير متوفر ❌ ]" -ForegroundColor Red
            if ($InstallCommand) {
                Write-Host "    └─ التثبيت المقترح: $InstallCommand" -ForegroundColor Yellow
            }
        }
    } catch {
        Write-Host "[ خطأ ❌ ]" -ForegroundColor Red
        if ($InstallCommand) {
            Write-Host "    └─ التثبيت المقترح: $InstallCommand" -ForegroundColor Yellow
        }
    }
}

# 1. فحص Git
Test-Component -Name "Git Version Control" -InstallCommand "winget install --id Git.Git -e" -CheckBlock {
    $ver = git --version 2>$null
    if ($ver) { return @{ Success = $true; Version = $ver.Trim() } }
    return @{ Success = $false }
}

# 2. فحص Node.js
Test-Component -Name "Node.js Runtime" -InstallCommand "winget install OpenJS.NodeJS.LTS" -CheckBlock {
    $ver = node -v 2>$null
    if ($ver) { return @{ Success = $true; Version = $ver.Trim() } }
    return @{ Success = $false }
}

# 3. فحص npm
Test-Component -Name "NPM Package Manager" -InstallCommand "يأتي مدمجاً مع Node.js" -CheckBlock {
    $ver = npm -v 2>$null
    if ($ver) { return @{ Success = $true; Version = "v" + $ver.Trim() } }
    return @{ Success = $false }
}

# 4. فحص Python
Test-Component -Name "Python 3 Runtime" -InstallCommand "winget install Python.Python.3.12" -CheckBlock {
    $ver = python --version 2>$null
    if ($ver) { return @{ Success = $true; Version = $ver.Trim() } }
    return @{ Success = $false }
}

# 5. فحص Claude Code CLI
Test-Component -Name "Claude Code CLI (claude)" -InstallCommand "npm install -g @anthropic-ai/claude-code" -CheckBlock {
    $ver = claude --version 2>$null
    if ($ver) { return @{ Success = $true; Version = $ver.Trim() } }
    return @{ Success = $false }
}

# 6. فحص OpenCode CLI
Test-Component -Name "OpenCode CLI (opencode)" -InstallCommand "npm install -g opencode-ai" -CheckBlock {
    $ver = opencode --version 2>$null
    if ($ver) { return @{ Success = $true; Version = $ver.Trim() } }
    return @{ Success = $false }
}

# 7. فحص Antigravity CLI (agcli / agy)
Test-Component -Name "Antigravity CLI (agy/agcli)" -InstallCommand "مدمج مع Antigravity IDE" -CheckBlock {
    $ver = agy --version 2>$null
    if (-not $ver) { $ver = agcli --version 2>$null }
    if ($ver) { return @{ Success = $true; Version = $ver.Trim() } }
    return @{ Success = $false }
}

# 8. فحص Java JDK
Test-Component -Name "Java JDK 17+" -InstallCommand "winget install EclipseAdoptium.Temurin.17.JDK" -CheckBlock {
    $ver = java -version 2>&1
    if ($ver -and $ver[0] -match 'version "([^"]+)"') {
        return @{ Success = $true; Version = "JDK " + $matches[1] }
    }
    return @{ Success = $false }
}

# 9. فحص Android ADB
Test-Component -Name "Android SDK (adb)" -InstallCommand "ثبّت Android Studio أو Platform-Tools" -CheckBlock {
    $adb = Get-Command adb -ErrorAction SilentlyContinue
    if (-not $adb) {
        $candidate = "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe"
        if (Test-Path $candidate) { $adb = $candidate }
    }
    if ($adb) {
        $ver = & $adb version 2>$null | Select-Object -First 1
        return @{ Success = $true; Version = $ver.Trim() }
    }
    return @{ Success = $false }
}

# 10. فحص مجلد الإعدادات والمهارات العالمي
Test-Component -Name "Global Skills & Agents (~/.gemini/config)" -InstallCommand "& 'F:\AI PROJECTS\Claude+Antigravity\setup_one_click.ps1'" -CheckBlock {
    $cfg = Join-Path $HOME ".gemini\config\skills"
    if (Test-Path $cfg) {
        $cnt = (Get-ChildItem -Directory $cfg).Count
        return @{ Success = $true; Version = "$cnt مهارة مفعلة" }
    }
    return @{ Success = $false }
}

Write-Host ""
Write-Host "================================================================================" -ForegroundColor Cyan
Write-Host " 📊 ملخص بطاقة التقييم (Fleet Scorecard): $PassedCount من أصل $TotalCount جاهز" -ForegroundColor $(if ($PassedCount -eq $TotalCount) { "Green" } else { "Yellow" })
Write-Host "================================================================================" -ForegroundColor Cyan
Write-Host ""

if ($PassedCount -eq $TotalCount) {
    Write-Host "🎉 تهانينا! اللاب توب الثاني جاهز تماماً 100% لتشغيل أسطول الـ CLIs بدون أي نواقص." -ForegroundColor Green
    Write-Host ""
    Write-Host "لتجربة تشغيل أمر تجريبي للأسطول:" -ForegroundColor Cyan
    Write-Host "  opencode run -m opencode/muse-spark-1.3-contributor-free `"echo Fleet Ready on Second Laptop`"" -ForegroundColor White
} else {
    Write-Host "⚠️ هناك بعض المكونات الناقصة، يرجى تشغيل أوامر التثبيت المقترحة أعلاه لإكمال الجاهزية." -ForegroundColor Yellow
}
Write-Host ""
