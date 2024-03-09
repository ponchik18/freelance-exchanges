#!/bin/bash
# Wait until PostgreSQL is ready to accept connections
until pg_isready -h "localhost" -p 5432 -U admin; do
  echo "Waiting for PostgreSQL to start..."
  sleep 1
done