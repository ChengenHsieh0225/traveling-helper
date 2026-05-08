#!/bin/bash

# ==============================================================================
# Script: k8s-proxy.sh
# Description: Establishes port-forwarding tunnels for the Traveling Helper services.
# ==============================================================================

echo "Starting K8S Port-Forwarding Tunnels for Traveling Helper..."

kubectl port-forward svc/auth-service 8080:8080 > /dev/null &
kubectl port-forward svc/planning-service 8081:8080 > /dev/null &
kubectl port-forward svc/social-service 8082:8080 > /dev/null &

echo "Auth: http://localhost:8080"
echo "Planning: http://localhost:8081"
echo "Social: http://localhost:8082"

echo "------------------------------------------------"
echo "✅ Tunnels established:"
echo "  Auth Service     -> http://localhost:8080"
echo "  Planning Service -> http://localhost:8081"
echo "  Social Service   -> http://localhost:8082"
echo "------------------------------------------------"
echo "Press [Ctrl+C] to stop all port-forwarding."

# Trap Ctrl+C (SIGINT) to terminate all background kubectl processes
trap "pkill kubectl; exit" INT
wait