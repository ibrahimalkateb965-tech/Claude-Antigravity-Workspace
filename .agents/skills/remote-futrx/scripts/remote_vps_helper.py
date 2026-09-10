#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
remote_vps_helper.py — Automation & Diagnostic Helper for remote.futrx on Hostinger VPS.
Part of the remote-futrx skill suite.
Zero external dependencies (Standard Library only).
"""

import argparse
import os
import socket
import sys


def check_dns(domain: str) -> None:
    """Verifies DNS resolution for the base domain and wildcard subdomains."""
    print(f"[+] Verifying DNS resolution for: {domain}")
    domains_to_test = [
        domain,
        f"code.{domain}",
        f"test-preview.dev.{domain}",
        f"test-ide.code.{domain}",
    ]

    all_resolved = True
    for d in domains_to_test:
        try:
            ip = socket.gethostbyname(d)
            print(f"  [OK] {d} -> {ip}")
        except socket.gaierror:
            print(f"  [FAIL] {d} -> Could not resolve hostname (Check Wildcard DNS)")
            all_resolved = False

    if all_resolved:
        print("\n[SUCCESS] Wildcard DNS is properly configured and reachable!")
    else:
        print("\n[WARNING] Some wildcard subdomains failed to resolve.")
        print("          Ensure you added '*.code' and '*.dev' A-records pointing to your VPS IP.")


def verify_ssh_key(key_path: str) -> None:
    """Validates presence and format of the SSH public key."""
    expanded_path = os.path.expanduser(key_path)
    if not os.path.exists(expanded_path):
        print(f"[ERROR] SSH key file not found at: {expanded_path}")
        sys.exit(1)

    with open(expanded_path, "r", encoding="utf-8") as f:
        content = f.read().strip()

    valid_prefixes = ("ssh-rsa", "ssh-ed25519", "ecdsa-sha2-nistp256", "ecdsa-sha2-nistp384", "ecdsa-sha2-nistp521")
    if any(content.startswith(p) for p in valid_prefixes):
        print(f"[OK] Valid SSH Public Key identified at: {expanded_path}")
        print("     Key preview:", content[:40] + "..." + content[-20:])
        print("\n[CRITICAL SAFETY CHECK]")
        print("Ensure this exact public key is in your VPS '~/.ssh/authorized_keys' BEFORE running the installer.")
        print("The installer will disable password login ('PasswordAuthentication no').")
    else:
        print(f"[WARNING] Key at {expanded_path} does not match standard OpenSSH public key format.")


def generate_install_cmd(domain: str) -> None:
    """Generates the verified, hardened installation script for Hostinger VPS."""
    cmd = f"""# ==============================================================================
# remote.futrx Hardened Installation Command for Hostinger VPS (Ubuntu 24.04)
# ==============================================================================

# 1. Enable 4GB Swap File (Protects RAM during container base image compilation)
sudo fallocate -l 4G /swapfile && sudo chmod 600 /swapfile && sudo mkswap /swapfile && sudo swapon /swapfile
echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab

# 2. Run Official Remote Installer for domain: {domain}
curl -fsSL https://remote.futrx.com/get | sudo bash -s -- {domain}

# 3. Retrieve Setup Token for Admin Account Creation
sudo journalctl -u remote --since "-10 min" | grep -A2 "first-time setup"
# Or run: remote setup-token
"""
    print(cmd)


def print_requirements() -> None:
    """Prints the hardware and OS requirements for remote.futrx."""
    print("=" * 65)
    print(" remote.futrx - Hostinger VPS Prerequisites (Constraint 53)")
    print("=" * 65)
    print("- Operating System : Ubuntu 24.04 LTS (64-bit x86_64)")
    print("- Recommended Tier : Hostinger KVM 2 (2 vCPU, 8GB RAM, 100GB NVMe)")
    print("- Minimum Tier     : Hostinger KVM 1 (1 vCPU, 4GB RAM + 4GB Swap)")
    print("- Open Ports       : 22 (SSH), 80 (HTTP), 443 (HTTPS)")
    print("- Required Domain  : Valid domain with wildcard (*.code, *.dev) DNS")
    print("- Target Agents    : Claude Code, Antigravity (agy), Codex, MiniMax, Kimi")
    print("- Storage Type     : NVMe SSD with at least 25GB free space")
    print("=" * 65)


def main():
    parser = argparse.ArgumentParser(
        description="remote.futrx Automation & Diagnostic Helper for Hostinger VPS"
    )
    parser.add_argument("--check-dns", metavar="DOMAIN", help="Verify Wildcard DNS resolution for domain")
    parser.add_argument("--verify-ssh-key", metavar="KEY_PATH", help="Validate OpenSSH public key file")
    parser.add_argument("--generate-install-cmd", metavar="DOMAIN", help="Generate safe install command block")
    parser.add_argument("--check-requirements", action="store_true", help="Display server specifications and requirements")

    if len(sys.argv) == 1:
        parser.print_help()
        sys.exit(0)

    args = parser.parse_args()

    if args.check_dns:
        check_dns(args.check_dns)
    elif args.verify_ssh_key:
        verify_ssh_key(args.verify_ssh_key)
    elif args.generate_install_cmd:
        generate_install_cmd(args.generate_install_cmd)
    elif args.check_requirements:
        print_requirements()


if __name__ == "__main__":
    main()
