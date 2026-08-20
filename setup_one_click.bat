@echo off
chcp 65001 >nul
title تثبيت وتفعيل المحرك المعماري لمنظومة Claude & Antigravity
echo =======================================================================
echo  جاري تشغيل مثبت المحرك المعماري لمنظومة Claude & Antigravity...
echo =======================================================================
echo.
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0setup_one_click.ps1"
if %errorlevel% neq 0 (
    echo.
    echo حدث خطأ أثناء التثبيت. اضغط على أي مفتاح للإغلاق...
    pause >nul
)
