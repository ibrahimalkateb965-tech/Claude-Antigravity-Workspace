# Remote Workspace Admin (Telegram ACI) 🚀

This module allows you to remotely control, manage, and interact with your AI Workspace via Telegram. It creates a seamless Agent-Computer Interface (ACI) where you can send commands from your phone, and your AI Agent will execute them locally and sync reports back to GitHub Pages.

## ✨ Features
1. **Always-On Telegram Daemon (`telegram_daemon.py`)**: Runs in the background, listening for Telegram messages, including photos and documents. It wakes up the AI Agent in the IDE upon receiving a message.
2. **One-Shot Polling (`poll_once.py`)**: Can be used to check for a single message and exit.
3. **Auto-Reply Engine (`send_reply.py`)**: Allows the AI Agent to send replies back to your Telegram account. Supports an optional `--sync` flag to push artifacts to GitHub Pages before replying.
4. **GitHub Pages Sync (`sync_artifacts_to_github.py`)**: Automatically commits and pushes your AI artifacts to a `docs/` folder on the `main` branch to be served via GitHub Pages.

## 🛠️ Setup Instructions

1. **Install Dependencies**:
   ```bash
   pip install pyTelegramBotAPI python-dotenv psutil
   ```
2. **Environment Variables**:
   Create a `.env` file in the root of your project using the provided `.env.example`:
   ```env
   TELEGRAM_TOKEN=your_bot_token_here
   ALLOWED_CHAT_ID=your_chat_id_here
   ```
3. **Run the Daemon**:
   ```bash
   python telegram_daemon.py
   ```

## 🔄 How it Works
When you send a message on Telegram, `telegram_daemon.py` captures it, saves it locally to `telegram_request.json`, and triggers a wakeup signal (`[TELEGRAM_DAEMON_WAKEUP]`). Your IDE Agent detects this output, reads the request, executes your task, and replies using `send_reply.py`.
