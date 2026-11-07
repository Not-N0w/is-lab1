#!/usr/bin/env bash
set -u

BASE_URL="${1:-http://localhost:8080}"
USERNAME="${2:-test}"
PASSWORD="${3:-test}"
COUNT="${4:-1000}"

AUTH="{\"username\":\"$USERNAME\",\"password\":\"$PASSWORD\"}"

token() {
  curl -s -X POST "$BASE_URL/api/auth/public/$1" -H "Content-Type: application/json" -d "$AUTH" \
    | sed -n 's/.*"accessToken":"\([^"]*\)".*/\1/p'
}

TOKEN=$(token login)
[ -z "$TOKEN" ] && TOKEN=$(token register)
[ -z "$TOKEN" ] && { echo "Failed to get a token for $USERNAME"; exit 1; }

TOPICS=(Sorting Graphs "Dynamic Programming" "Search Trees" "Hash Tables" Strings Heaps "Greedy Algorithms" Geometry "Network Flows")
DISCIPLINES=(Algorithms Programming "Discrete Math" Databases "Operating Systems" Networks Compilers "Computer Architecture" "Information Systems" "Software Engineering")
PEOPLE=(Alice Bob Carol Dave Eve Frank Grace Heidi Ivan Judy)
CITIES=(London Paris Berlin Madrid Rome Oslo Vienna Prague Dublin Lisbon)
DIFFICULTIES=(VERY_EASY HARD TERRIBLE null)
COLORS=(RED BLUE ORANGE WHITE)
COUNTRIES=(USA SPAIN SOUTH_KOREA NORTH_KOREA)

pick() { local a=("$@"); echo "${a[RANDOM % ${#a[@]}]}"; }
str_or_null() { [ "$1" = null ] && echo null || echo "\"$1\""; }
maybe_null() { [ $((RANDOM % 4)) -eq 0 ] && echo null || pick "$@"; }

OUT=$(mktemp)
OK=0

for i in $(seq 1 "$COUNT"); do
  if [ $((i % 5)) -eq 0 ]; then
    AUTHOR=null
  else
    AUTHOR="{
      \"name\": \"$(pick "${PEOPLE[@]}") $i\",
      \"eyeColor\": \"$(pick "${COLORS[@]}")\",
      \"hairColor\": $(str_or_null "$(maybe_null "${COLORS[@]}")"),
      \"height\": $((RANDOM % 50 + 150)),
      \"nationality\": $(str_or_null "$(maybe_null "${COUNTRIES[@]}")"),
      \"location\": {
        \"name\": \"$(pick "${CITIES[@]}")\",
        \"x\": $((RANDOM % 200 - 100)),
        \"y\": $((RANDOM % 200 - 100)),
        \"z\": $((RANDOM % 200 - 100))
      }
    }"
  fi

  BODY="{
    \"name\": \"$(pick "${TOPICS[@]}") #$i\",
    \"description\": \"Test lab work $i\",
    \"difficulty\": $(str_or_null "$(pick "${DIFFICULTIES[@]}")"),
    \"minimalPoint\": $((RANDOM % 50 + 1)),
    \"tunedInWorks\": $((RANDOM % 20)),
    \"coordinates\": { \"x\": $((RANDOM % 400 - 200)), \"y\": $((RANDOM % 1000)) },
    \"discipline\": { \"name\": \"$(pick "${DISCIPLINES[@]}") $i\", \"practiceHours\": $((RANDOM % 60 + 10)) },
    \"author\": $AUTHOR
  }"

  STATUS=$(curl -s -o "$OUT" -w "%{http_code}" -X POST "$BASE_URL/api/lab-works/create" \
    -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" -d "$BODY")

  if [ "$STATUS" -ge 200 ] && [ "$STATUS" -lt 300 ]; then
    OK=$((OK + 1))
    echo "[$i/$COUNT] created"
  else
    echo "[$i/$COUNT] error $STATUS: $(cat "$OUT")"
  fi
done

rm -f "$OUT"
echo "Done: $OK of $COUNT lab works created"