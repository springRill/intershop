#!/bin/sh

HOST=$1
PORT=$2
shift 2

echo "Ожидание доступности $HOST:$PORT..."

while ! nc -z "$HOST" "$PORT"; do
  sleep 2
done

echo "$HOST:$PORT доступен. Продолжаем..."
exec "$@"
