# Remote Workspace Admin (Telegram ACI) 🚀

This module allows you to remotely control, manage, and interact with your AI Workspace via Telegram. It creates a seamless Agent-Computer Interface (ACI) where you can send commands from your phone, and your AI Agent will execute them locally and sync reports back to GitHub Pages.

## ✨ Features
1. **Always-On Telegram Daemon (`telegram_daemon.py`)**: The primary daemon that runs in the background, listening for Telegram messages, including photos and documents. It wakes up the AI Agent in the IDE upon receiving a message.
2. **One-Shot Polling (`poll_once.py`)**: A fallback/diagnostic tool that checks for a single message and exits.
3. **Auto-Reply Engine (`send_reply.py`)**: Allows the AI Agent to send replies back to your Telegram account. Supports an optional `--sync` flag to push artifacts to GitHub Pages before replying.
4. **GitHub Pages Sync (`sync_artifacts_to_github.py`)**: Automatically commits and pushes your AI artifacts to a `docs/` folder on the `main` branch to be served via GitHub Pages.

## 📁 Project Structure
- `telegram_core.py`: The single source of truth for the core logic (Locking, configuration, media downloading, JSON parsing).
- `telegram_daemon.py`: The primary long-running bot.
- `poll_once.py`: The single-shot diagnostic bot.
- `send_reply.py`: Utility for the AI agent to send messages back.
- `sync_artifacts_to_github.py`: Utility to synchronize generated Markdown artifacts to GitHub Pages.
- `telegram_poller.pid`: Lock file to ensure only one bot instance connects at a time (preventing 409 Conflict).
- `requirements.txt`: Project dependencies.

## 🛠️ Setup Instructions

1. **Install Dependencies**:
   ```bash
   pip install -r requirements.txt
   ```
2. **Environment Variables**:
   Create a `.env` file in the root of your project using the provided `.env.example`:

   | Variable | Required | Description |
   |---|---|---|
   | `TELEGRAM_TOKEN` | Yes | Your Telegram Bot token from BotFather |
   | `ALLOWED_CHAT_ID` | Yes | Your personal chat ID to restrict access |
   | `GITHUB_PAGES_URL` | No | URL to your GitHub Pages site for syncing reports |
   | `ARTIFACTS_BRAIN_DIR` | No | Absolute path to the AI artifacts directory |

3. **Run the Daemon**:
   ```bash
   python telegram_daemon.py
   ```

## 🔄 How it Works
When you send a message on Telegram, the daemon captures it, saves it locally to `telegram_request.json`, and triggers a wakeup signal (`[TELEGRAM_DAEMON_WAKEUP]`). Your IDE Agent detects this output, reads the request, executes your task, and replies using `send_reply.py`.

### 📄 JSON Request Schema
The request is stored in `telegram_request.json` with the following schema:
```json
{
    "message_id": 1234,
    "chat_id": 567890,
    "text": "Your message here",
    "media_path": "C:\\path\\to\\media.jpg",
    "date": 1680000000,
    "user": "Kt"
}
```

## ⚠️ Troubleshooting & Locking Behavior

- **Official Mode**: The official mode for the project is running `telegram_daemon.py`. The `poll_once.py` is for diagnostics/fallback only.
- **Single Instance Policy**: **Do not run both scripts at the same time**. Telegram allows only one `getUpdates` consumer. If a bot is running, starting another will result in an error or a lock block.
- **Locking**: The lock file `telegram_poller.pid` prevents multiple instances. If the lock is busy, the script exits with code `2`.
- **Takeover Mode**: If the previous instance is stuck, you can force a takeover:
  ```bash
  python telegram_daemon.py --takeover
  ```
- **Exit Codes**:
  - `0`: Success / Normal Exit
  - `1`: Operational Error
  - `2`: Lock is busy (Another instance is alive)
