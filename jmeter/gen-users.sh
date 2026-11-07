#!/usr/bin/env bash

URL="http://localhost:8080/api/auth/public/register"
OUT="users.csv"

echo "username,password" > "$OUT"

for ((i = 0; i < 100; ++i)); do
  STATUS=$(curl -s -o /dev/null -w "%{http_code}" -X POST "$URL" \
    -H "Content-Type: application/json" \
    -d "{\"username\":\"user${i}\",\"password\":\"pass\"}")

  if [ "$STATUS" -ge 200 ] && [ "$STATUS" -lt 300 ]; then
    echo "user${i},pass" >> "$OUT"
    echo "user${i}: created"
  else
    echo "user${i}: error $STATUS"
  fi
done