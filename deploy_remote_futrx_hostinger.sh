#!/usr/bin/env bash
# ==============================================================================
# Autovem Ecosystem & Remote.Futrx VPS Deployment Script
# Target: Hostinger VPS (srv1810150.hstgr.cloud - 187.55.226.225)
# Domain: remote.autovem.tech & autovem.tech
# ==============================================================================
set -euo pipefail

echo "=========================================================="
echo "🚀 [Step 1/5] Checking & Allocating 4GB Swap Memory..."
echo "=========================================================="
if [ ! -f /swapfile ]; then
    echo "Creating 4GB swapfile for KVM 1 stability..."
    fallocate -l 4G /swapfile
    chmod 600 /swapfile
    mkswap /swapfile
    swapon /swapfile
    echo '/swapfile none swap sw 0 0' >> /etc/fstab
    echo "✅ Swap memory enabled successfully."
else
    echo "ℹ️ Swapfile already exists."
fi

echo "=========================================================="
echo "📦 [Step 2/5] Installing Prerequisites..."
echo "=========================================================="
export DEBIAN_FRONTEND=noninteractive
apt-get update -qq
apt-get install -y -qq curl git ca-certificates ufw jq

echo "=========================================================="
echo "🔄 [Step 3/5] Transitioning Docker Caddy to Host Caddy..."
echo "=========================================================="
# If Docker Caddy is running in /root/server, stop it to free ports 80 & 443
if [ -d "/root/server" ]; then
    echo "Found /root/server Docker Compose project."
    cd /root/server
    if docker compose ps | grep -q "caddy"; then
        echo "Stopping Docker Caddy to release ports 80/443 for Host Caddy..."
        docker compose stop caddy || true
        docker compose rm -f caddy || true
    fi
    # Ensure pocketbase stays running on port 8090
    if ! docker compose ps | grep -q "pocketbase"; then
        echo "Starting PocketBase on port 8090..."
        docker compose up -d pocketbase
    fi
fi

echo "=========================================================="
echo "🌐 [Step 4/5] Installing remote.futrx on remote.autovem.tech..."
echo "=========================================================="
# Execute official installer with hostname remote.autovem.tech
curl -fsSL https://remote.futrx.com/get | sudo bash -s -- remote.autovem.tech

echo "=========================================================="
echo "🔗 [Step 5/5] Merging autovem.tech PocketBase Route into Caddyfile..."
echo "=========================================================="
CADDYFILE="/etc/caddy/Caddyfile"
if [ -f "$CADDYFILE" ]; then
    if ! grep -q "autovem.tech" "$CADDYFILE"; then
        echo "Appending autovem.tech reverse proxy block..."
        cat << 'EOF' >> "$CADDYFILE"

# Autovem Main Domain & PocketBase Routing
autovem.tech, www.autovem.tech {
    encode zstd gzip
    reverse_proxy 127.0.0.1:8090
}
EOF
        systemctl reload caddy
        echo "✅ Caddy reloaded with unified routing for PocketBase & Remote Futrx."
    else
        echo "ℹ️ autovem.tech route already present in Caddyfile."
    fi
fi

echo "=========================================================="
echo "🎉 Deployment Completed Successfully!"
echo "=========================================================="
echo "Check services:"
echo "  1. Remote Futrx: https://remote.autovem.tech"
echo "  2. PocketBase:   https://autovem.tech"
echo "  3. Service logs: journalctl -u remote.futrx -n 30 --no-pager"
echo "=========================================================="
