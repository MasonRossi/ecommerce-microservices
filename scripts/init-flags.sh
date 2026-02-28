#!/bin/bash
set -euo pipefail

UNLEASH_HOST="${UNLEASH_HOST:-localhost}"
UNLEASH_PORT="${UNLEASH_PORT:-4242}"
UNLEASH_URL="http://${UNLEASH_HOST}:${UNLEASH_PORT}"
PROJECT_ID="default"

: "${UNLEASH_ADMIN_TOKEN:?UNLEASH_ADMIN_TOKEN is required}"

AUTH_HEADER="Authorization: Bearer ${UNLEASH_ADMIN_TOKEN}"

echo "Waiting for Unleash server..."
until curl -sf "${UNLEASH_URL}/health" > /dev/null; do
  echo "Waiting 3s..."
  sleep 3
done
echo "Unleash is healthy"

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

create_flag "premium-pricing" "Enable 10 percent discount for premium users"
create_flag "order-notifications" "Log order confirmation notifications"
create_flag "bulk-order-discount" "Apply 15 percent discount for bulk orders"

echo "All feature flags initialized successfully"