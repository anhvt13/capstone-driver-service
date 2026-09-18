#!/bin/sh
set -e

# ======================
# KEYSTORE FILE LOADING
# ======================
KEYSTORE_PATH="/certs/driver/driver-service-keystore.p12"

# Local Docker: use existing mounted certificate
if [ -f "$KEYSTORE_PATH" ]; then
    echo "Keystore already exists. Using existing certificate."

# ECS/Fargate: create certificate from Secrets Manager
elif [ -n "$DRIVER_KEYSTORE_BASE64" ]; then
    echo "Creating keystore from Secrets Manager..."
    mkdir -p /certs/driver
    printf '%s' "$DRIVER_KEYSTORE_BASE64" | base64 -d > "$KEYSTORE_PATH"
else
    echo "ERROR: No keystore file found and DRIVER_KEYSTORE_BASE64 is not set."
    exit 1
fi

# =======================
# TRUSTSTORE FILE LOADING
# =======================
TRUSTSTORE_PATH="/certs/driver/driver-service-truststore.p12"

# Local Docker: use existing mounted certificate
if [ -f "$TRUSTSTORE_PATH" ]; then
    echo "Truststore already exists. Using existing truststore."

# ECS/Fargate: create certificate from Secrets Manager
elif [ -n "$DRIVER_TRUSTSTORE_BASE64" ]; then
    echo "Creating truststore from Secrets Manager..."
    mkdir -p /certs/driver
    printf '%s' "$DRIVER_TRUSTSTORE_BASE64" | base64 -d > "$TRUSTSTORE_PATH"
else
    echo "ERROR: No truststore file found and DRIVER_KEYSTORE_BASE64 is not set."
    exit 1
fi

# ===================
# START APPLICATION
# ===================
exec java -jar /app/app.jar