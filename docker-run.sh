#!/bin/bash

echo "Start sendy-service Application..."

echo "Cleaning up existing containers..."
docker-compose -f ./docker-compose/docker-compose.yml down --remove-orphans

echo "Building and starting"
docker-compose -f ./docker-compose/docker-compose.yml up --build -d