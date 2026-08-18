$ErrorActionPreference = 'Stop'
Set-Location -Path "$PSScriptRoot"
python -m venv venv
.\venv\Scripts\Activate.ps1
pip install -r requirements.txt
