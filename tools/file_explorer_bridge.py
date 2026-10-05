#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
=============================================================================
File Explorer Bridge & Windows Desktop Discovery Engine
=============================================================================
Bridges WSL/Linux runtime with Windows File Explorer, solving the path
disconnect issue where users cannot find or open generated files (PDF, DOCX, XLSX).
Provides automatic Windows path translation, direct non-blocking explorer reveal,
default viewer launch, and Desktop output hub mirroring.
=============================================================================
"""

import os
import sys
import shutil
import subprocess
import argparse
import re
from pathlib import Path


def get_windows_user() -> str:
    """Detect active Windows username dynamically from environment or filesystem."""
    try:
        res = subprocess.run(
            ["cmd.exe", "/c", "echo %USERNAME%"],
            capture_output=True,
            text=True,
            stdin=subprocess.DEVNULL,
            timeout=2,
        )
        user = res.stdout.strip()
        if user and not user.startswith("%"):
            return user
    except Exception:
        pass

    try:
        users_dir = "/mnt/c/Users"
        if os.path.exists(users_dir):
            ignored = {"all users", "default", "default user", "public", "desktop.ini"}
            candidates = [
                u for u in os.listdir(users_dir)
                if u.lower() not in ignored and os.path.isdir(os.path.join(users_dir, u))
            ]
            if "Kt" in candidates:
                return "Kt"
            if candidates:
                return candidates[0]
    except Exception:
        pass
    return "Kt"


def wsl_to_windows_path(wsl_path: str) -> str:
    """
    Convert a WSL/Linux path to a valid Windows absolute path.
    Uses 'wslpath -w' first for full compatibility (including /tmp and /home -> \\wsl.localhost).
    Falls back to regex mapping for /mnt/<drive>/.
    """
    path_obj = Path(wsl_path).expanduser()
    path_str = str(path_obj)

    # 1. Native wslpath check
    try:
        res = subprocess.run(
            ["wslpath", "-w", path_str],
            capture_output=True,
            text=True,
            check=True,
            timeout=2,
        )
        out = res.stdout.strip()
        if out:
            return out
    except Exception:
        pass

    # 2. Regex fallback for /mnt/<drive>/
    match = re.match(r"^/mnt/([a-zA-Z])(?:/(.*))?$", path_str)
    if match:
        drive_letter = match.group(1).upper()
        rest = match.group(2) or ""
        win_rest = rest.replace("/", "\\")
        return f"{drive_letter}:\\{win_rest}" if win_rest else f"{drive_letter}:\\"

    # 3. Already windows format?
    if re.match(r"^[a-zA-Z]:[\\/]", path_str):
        return path_str.replace("/", "\\")

    return path_str


def windows_to_wsl_path(win_path: str) -> str:
    """
    Convert a Windows absolute path to a WSL path.
    Uses 'wslpath -u' first, falling back to drive mapping.
    """
    try:
        res = subprocess.run(
            ["wslpath", "-u", win_path],
            capture_output=True,
            text=True,
            check=True,
            timeout=2,
        )
        out = res.stdout.strip()
        if out:
            return out
    except Exception:
        pass

    match = re.match(r"^([a-zA-Z]):[\\/](.*)$", win_path)
    if match:
        drive = match.group(1).lower()
        rest = match.group(2).replace("\\", "/")
        return f"/mnt/{drive}/{rest}"
    return win_path


def get_desktop_dir(subfolder: str = "المستندات_المولدة", create: bool = True) -> tuple[str, str]:
    """
    Returns (wsl_desktop_path, win_desktop_path) for the target subfolder on the Windows Desktop.
    Automatically detects OneDrive Desktop if active.
    """
    try:
        res = subprocess.run(
            [
                "cmd.exe",
                "/c",
                'if exist "%USERPROFILE%\\OneDrive\\Desktop" (echo %USERPROFILE%\\OneDrive\\Desktop) else (echo %USERPROFILE%\\Desktop)',
            ],
            capture_output=True,
            text=True,
            stdin=subprocess.DEVNULL,
            timeout=2,
        )
        win_desk = res.stdout.strip()
        if win_desk and not win_desk.startswith("%"):
            wsl_desk = windows_to_wsl_path(win_desk)
            target_wsl = os.path.join(wsl_desk, subfolder) if subfolder else wsl_desk
            if create:
                os.makedirs(target_wsl, exist_ok=True)
            target_win = f"{win_desk}\\{subfolder}" if subfolder else win_desk
            return target_wsl, target_win
    except Exception:
        pass

    # Static fallback
    win_user = get_windows_user()
    wsl_desktop = f"/mnt/c/Users/{win_user}/Desktop"
    target_wsl = os.path.join(wsl_desktop, subfolder) if subfolder else wsl_desktop
    if create:
        os.makedirs(target_wsl, exist_ok=True)
    target_win = wsl_to_windows_path(target_wsl)
    return target_wsl, target_win


def mirror_to_desktop(file_path: str, subfolder: str = "المستندات_المولدة") -> dict:
    """
    Copies a generated file to the user's Windows Desktop under a dedicated hub folder.
    """
    src = Path(file_path).resolve()
    if not src.exists():
        raise FileNotFoundError(f"Source file does not exist: {file_path}")

    target_wsl_dir, target_win_dir = get_desktop_dir(subfolder, create=True)
    dst_wsl = os.path.join(target_wsl_dir, src.name)
    shutil.copy2(src, dst_wsl)
    dst_win = wsl_to_windows_path(dst_wsl)

    return {
        "src_wsl": str(src),
        "src_win": wsl_to_windows_path(str(src)),
        "dst_wsl": dst_wsl,
        "dst_win": dst_win,
        "filename": src.name,
    }


def reveal_in_explorer(file_path: str) -> bool:
    """
    Launches Windows File Explorer with the target file highlighted.
    Directly spawns explorer.exe without cmd.exe to avoid quoting and environment issues.
    """
    win_path = wsl_to_windows_path(file_path)
    try:
        subprocess.Popen(
            ["explorer.exe", f"/select,{win_path}"],
            start_new_session=True,
            stdin=subprocess.DEVNULL,
            stdout=subprocess.DEVNULL,
            stderr=subprocess.DEVNULL,
            close_fds=True,
        )
        return True
    except Exception as e:
        print(f"[Warning] Failed to launch explorer.exe: {e}", file=sys.stderr)
        return False


def open_file_in_windows(file_path: str) -> bool:
    """
    Opens the file directly using Windows default associated application
    via direct explorer.exe invocation.
    """
    win_path = wsl_to_windows_path(file_path)
    try:
        subprocess.Popen(
            ["explorer.exe", win_path],
            start_new_session=True,
            stdin=subprocess.DEVNULL,
            stdout=subprocess.DEVNULL,
            stderr=subprocess.DEVNULL,
            close_fds=True,
        )
        return True
    except Exception as e:
        print(f"[Warning] Failed to open file in Windows: {e}", file=sys.stderr)
        return False


def process_generated_file(
    file_path: str,
    mirror_desktop: bool = True,
    reveal_explorer: bool = True,
    open_viewer: bool = False,
    subfolder: str = "المستندات_المولدة",
) -> dict:
    """
    Complete post-generation lifecycle handler:
    1. Translates paths.
    2. Mirrors file to Desktop output hub.
    3. Triggers Windows Explorer reveal.
    4. Optionally launches default viewer.
    5. Returns rich path metadata.
    """
    wsl_abs = str(Path(file_path).resolve())
    win_abs = wsl_to_windows_path(wsl_abs)

    results = {
        "wsl_path": wsl_abs,
        "win_path": win_abs,
        "win_uri": f"file:///{win_abs.replace(chr(92), '/')}",
        "mirrored": False,
        "desktop_wsl": None,
        "desktop_win": None,
        "revealed": False,
        "opened": False,
    }

    if mirror_desktop:
        try:
            m_res = mirror_to_desktop(wsl_abs, subfolder=subfolder)
            results["mirrored"] = True
            results["desktop_wsl"] = m_res["dst_wsl"]
            results["desktop_win"] = m_res["dst_win"]
            results["desktop_uri"] = f"file:///{m_res['dst_win'].replace(chr(92), '/')}"
        except Exception as e:
            print(f"[Warning] Desktop mirror failed: {e}", file=sys.stderr)

    # Reveal the desktop mirror if available, else original
    target_to_reveal = results["desktop_wsl"] if results.get("desktop_wsl") else wsl_abs

    if reveal_explorer:
        results["revealed"] = reveal_in_explorer(target_to_reveal)

    if open_viewer:
        results["opened"] = open_file_in_windows(target_to_reveal)

    return results


def main():
    parser = argparse.ArgumentParser(
        description="Windows File Explorer Bridge & Desktop Discovery Hub"
    )
    subparsers = parser.add_subparsers(dest="command", help="Sub-commands")

    # Reveal
    rev_parser = subparsers.add_parser("reveal", help="Reveal file in Windows Explorer")
    rev_parser.add_argument("path", help="Path to file")

    # Open
    open_parser = subparsers.add_parser("open", help="Open file in Windows default viewer")
    open_parser.add_argument("path", help="Path to file")

    # Mirror
    mir_parser = subparsers.add_parser("mirror", help="Mirror file to Windows Desktop")
    mir_parser.add_argument("path", help="Path to file")
    mir_parser.add_argument("--subfolder", default="المستندات_المولدة", help="Desktop subfolder")

    # Process (All-in-one)
    proc_parser = subparsers.add_parser("process", help="Process generated file (mirror, reveal, open)")
    proc_parser.add_argument("path", help="Path to file")
    proc_parser.add_argument("--open", action="store_true", help="Also open file in default viewer")
    proc_parser.add_argument("--no-mirror", action="store_true", help="Skip desktop mirroring")
    proc_parser.add_argument("--no-reveal", action="store_true", help="Skip explorer reveal")
    proc_parser.add_argument("--subfolder", default="المستندات_المولدة", help="Desktop subfolder")

    args = parser.parse_args()

    if not args.command:
        parser.print_help()
        sys.exit(1)

    if args.command == "reveal":
        ok = reveal_in_explorer(args.path)
        print(f"Revealed {args.path}: {'SUCCESS' if ok else 'FAILED'}")

    elif args.command == "open":
        ok = open_file_in_windows(args.path)
        print(f"Opened {args.path}: {'SUCCESS' if ok else 'FAILED'}")

    elif args.command == "mirror":
        res = mirror_to_desktop(args.path, subfolder=args.subfolder)
        print(f"Mirrored to Desktop: {res['dst_win']}")

    elif args.command == "process":
        res = process_generated_file(
            args.path,
            mirror_desktop=not args.no_mirror,
            reveal_explorer=not args.no_reveal,
            open_viewer=args.open,
            subfolder=args.subfolder,
        )
        print("\n--- [FILE DISCOVERY METADATA] ---")
        print(f"Windows Path:  {res['win_path']}")
        if res.get("desktop_win"):
            print(f"Desktop Copy:  {res['desktop_win']}")
        print(f"Revealed:      {res['revealed']}")
        print(f"Opened:        {res['opened']}")


if __name__ == "__main__":
    main()
