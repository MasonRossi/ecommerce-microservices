#!/bin/bash
set -euo pipefail

# Configuration
UNLEASH_HOST="${UNLEASH_HOST:-unleash-server}"
UNLEASH_PORT="${UNLEASH_PORT:-4242}"
UNLEASH_URL="http://${UNLEASH_HOST}:${UNLEASH_PORT}"
PROJECT_ID="default"

# REQUIRED: Admin API token
: "${UNLEASH_ADMIN_TOKEN:?UNLEASH_ADMIN_TOKEN is required}"

AUTH_HEADER="Authorization: Bearer ${UNLEASH_ADMIN_TOKEN}"

# Wait for Unleash
echo "Waiting for Unleash server..."
until curl -sf "${UNLEASH_URL}/health" > /dev/null; do
  echo "Waiting 3s..."
  sleep 3
done
echo "Unleash is healthy"

# Create feature flag function
create_flag() {
  local FLAG_NAME="$1"
  local DESCRIPTION="$2"

  echo "Ensuring feature flag exists: ${FLAG_NAME}"

  curl -sf -X POST \
    "${UNLEASH_URL}/api/admin/projects/${PROJECT_ID}/features" \
    -H "${AUTH_HEADER}" \
    -H "Content-Type: application/json" \
    -d "{
      \"name\": \"${FLAG_NAME}\",
      \"description\": \"${DESCRIPTION}\",
      \"type\": \"release\"
    }" || echo "Feature '${FLAG_NAME}' already exists"
}

# Initialize required flags
create_flag "premium-pricing" "Enable 10% discount for premium users"
create_flag "order-notifications" "Log order confirmation notifications"
create_flag "bulk-order-discount" "Apply 15% discount for bulk orders"

echo "All feature flags initialized successfully"