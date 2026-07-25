[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
$repo = "PowerShell/PowerShell"
$apiUrl = "https://api.github.com/repos/$repo/releases/latest"

Write-Host "Fetching latest release information from GitHub API..." -ForegroundColor Cyan

$response = Invoke-RestMethod -Uri $apiUrl -Method Get

$asset = $response.assets | Where-Object { $_.name -match "win-x64\.msi$" } | Select-Object -First 1

if (-not $asset) {
    Write-Error "Could not find the win-x64 MSI installer in the latest release."
    exit
}

$downloadUrl = $asset.browser_download_url
$fileName = $asset.name
$downloadPath = Join-Path $env:USERPROFILE "Downloads\$fileName"

Write-Host "Found latest version: $($response.tag_name)" -ForegroundColor Green
Write-Host "Downloading $fileName to Downloads folder using BITS Transfer..." -ForegroundColor Cyan

Import-Module BitsTransfer
Start-BitsTransfer -Source $downloadUrl -Destination $downloadPath

Write-Host "Download complete!" -ForegroundColor Green
Write-Host "File saved to: $downloadPath" -ForegroundColor Yellow
Write-Host "Run this file to update PowerShell." -ForegroundColor White
