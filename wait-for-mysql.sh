#!/bin/bash
# Wait for MySQL to be ready and start the app

HOST=${1:-mysql}
PORT=${2:-3306}
TIMEOUT=${3:-30}
ELAPSED=0

while [ $ELAPSED -lt $TIMEOUT ]; do
  if nc -z $HOST $PORT 2>/dev/null; then
    echo "MySQL is available! Starting application..."
    exec java -jar /app/app.jar
  fi
  echo "Waiting for MySQL to be ready... ($ELAPSED/$TIMEOUT)"
  sleep 2
  ELAPSED=$((ELAPSED + 2))
done

echo "MySQL did not become available within $TIMEOUT seconds"
exit 1


