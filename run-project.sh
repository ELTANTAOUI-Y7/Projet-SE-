#!/bin/bash

# ============================================
# Phone Shop Project - Complete Setup Script
# Runs: MySQL, Application, Prometheus, Grafana
# ============================================

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}Phone Shop Project - Setup Script${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""



# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo -e "${RED}Error: Docker is not running. Please start Docker first.${NC}"
    exit 1
fi

# Create Docker network
echo -e "${YELLOW}[1/5] Creating Docker network...${NC}"
docker network create monitoring-network 2>/dev/null || echo "Network already exists"
echo -e "${GREEN}✓ Network ready${NC}"
echo ""

# Stop and remove existing containers (but preserve MySQL data)
echo -e "${YELLOW}[2/6] Cleaning up existing containers...${NC}"
docker stop phone-shop-app prometheus grafana 2>/dev/null || true
docker rm phone-shop-app prometheus grafana 2>/dev/null || true
# Preserve MySQL container to keep all data (products, users, etc.)
if docker ps -a --format "{{.Names}}" | grep -q "^mysql$"; then
    echo -e "${GREEN}  Preserving MySQL container to keep all data${NC}"
    MYSQL_PRESERVED=true
    # Ensure MySQL is running
    if ! docker ps --format "{{.Names}}" | grep -q "^mysql$"; then
        echo -e "${YELLOW}  Starting existing MySQL container...${NC}"
        docker start mysql 2>/dev/null || true
    fi
else
    MYSQL_PRESERVED=false
fi
echo -e "${GREEN}✓ Cleanup complete${NC}"
echo ""

# Check and kill process on port 8080
echo -e "${YELLOW}[2.5/6] Freeing port 8080...${NC}"

# First, check for Docker containers using port 8080
DOCKER_CONTAINER=$(docker ps --filter "publish=8080" --format "{{.Names}}" 2>/dev/null | head -1 || true)
if [ ! -z "$DOCKER_CONTAINER" ]; then
    echo -e "${YELLOW}  Found Docker container '$DOCKER_CONTAINER' using port 8080, stopping it...${NC}"
    docker stop "$DOCKER_CONTAINER" 2>/dev/null || true
    docker rm "$DOCKER_CONTAINER" 2>/dev/null || true
    sleep 2
fi

# Then check for system processes
if command -v lsof > /dev/null 2>&1; then
    PID=$(lsof -ti :8080 2>/dev/null || true)
    if [ ! -z "$PID" ]; then
        echo -e "${YELLOW}  Found process $PID using port 8080, killing it...${NC}"
        kill -9 $PID 2>/dev/null || sudo kill -9 $PID 2>/dev/null || true
        sleep 2
    fi
elif command -v fuser > /dev/null 2>&1; then
    if fuser 8080/tcp > /dev/null 2>&1; then
        echo -e "${YELLOW}  Found process using port 8080, killing it...${NC}"
        fuser -k 8080/tcp 2>/dev/null || sudo fuser -k 8080/tcp 2>/dev/null || true
        sleep 2
    fi
fi

# Verify port is free
if command -v lsof > /dev/null 2>&1; then
    if lsof -ti :8080 > /dev/null 2>&1; then
        echo -e "${RED}  Warning: Port 8080 is still in use after cleanup attempt${NC}"
        echo -e "${YELLOW}  Please manually free port 8080 and try again${NC}"
    else
        echo -e "${GREEN}✓ Port 8080 is free${NC}"
    fi
else
    echo -e "${GREEN}✓ Port 8080 cleanup attempted${NC}"
fi
echo ""

# Start MySQL (only if not preserved)
if [ "$MYSQL_PRESERVED" != "true" ]; then
    echo -e "${YELLOW}[3/6] Starting MySQL...${NC}"
    docker run -d \
      --name mysql \
      --network monitoring-network \
      -e MYSQL_ROOT_PASSWORD=root \
      -e MYSQL_DATABASE=phone_shoop \
      -p 3307:3306 \
      -v mysql_data:/var/lib/mysql \
      mysql:8.0

    # Wait for MySQL to be ready
    echo "Waiting for MySQL to be ready..."
    until docker exec mysql mysqladmin ping -h localhost -uroot -proot --silent 2>/dev/null; do
        echo -n "."
        sleep 2
    done
    echo ""
    echo -e "${GREEN}✓ MySQL is ready${NC}"
else
    echo -e "${YELLOW}[3/6] MySQL already running (data preserved)...${NC}"
    # Wait for MySQL to be ready
    echo "Waiting for MySQL to be ready..."
    until docker exec mysql mysqladmin ping -h localhost -uroot -proot --silent 2>/dev/null; do
        echo -n "."
        sleep 2
    done
    echo ""
    echo -e "${GREEN}✓ MySQL is ready${NC}"
fi
echo ""

# Start Phone Shop Application
echo -e "${YELLOW}[4/6] Starting Phone Shop Application...${NC}"
if [ -f "Dockerfile" ]; then
    docker build -t phone-shop-app:latest .
    docker run -d \
      --name phone-shop-app \
      --network monitoring-network \
      -e DB_HOST=mysql \
      -e DB_PORT=3306 \
      -e DB_NAME=phone_shoop \
      -e DB_USER=root \
      -e DB_PASSWORD=root \
      -p 8080:8080 \
      phone-shop-app:latest
    echo -e "${GREEN}✓ Application is starting${NC}"
else
    echo -e "${YELLOW}⚠ Dockerfile not found. Skipping application container.${NC}"
    echo -e "${YELLOW}  You can run the app with: mvn jetty:run${NC}"
fi
echo ""

# Start Prometheus
echo -e "${YELLOW}[5/6] Starting Prometheus...${NC}"
if [ -f "prometheus.yml" ]; then
    docker run -d \
      --name prometheus \
      --network monitoring-network \
      --add-host=host.docker.internal:host-gateway \
      -p 9091:9090 \
      -v $(pwd)/prometheus.yml:/etc/prometheus/prometheus.yml \
      -v prometheus_data:/prometheus \
      prom/prometheus:v2.47.0 \
      --config.file=/etc/prometheus/prometheus.yml \
      --storage.tsdb.path=/prometheus
    echo -e "${GREEN}✓ Prometheus is running${NC}"
else
    echo -e "${YELLOW}⚠ prometheus.yml not found. Skipping Prometheus.${NC}"
fi
echo ""

# Start Grafana
echo -e "${YELLOW}[6/6] Starting Grafana...${NC}"
docker run -d \
  --name grafana \
  --network monitoring-network \
  -p 3001:3000 \
  -e GF_SECURITY_ADMIN_USER=admin \
  -e GF_SECURITY_ADMIN_PASSWORD=admin \
  -e GF_SERVER_ROOT_URL=http://localhost:3001 \
  -v grafana_data:/var/lib/grafana \
  grafana/grafana:10.4.0
echo -e "${GREEN}✓ Grafana is running${NC}"
echo ""

# Get Prometheus IP for Grafana configuration
PROMETHEUS_IP=$(docker inspect prometheus --format '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' 2>/dev/null || echo "N/A")

# Summary
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}Setup Complete!${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo -e "Access URLs:"
echo -e "  ${GREEN}Application:${NC}    http://localhost:8080"
echo -e "  ${GREEN}MySQL:${NC}          localhost:3307 (root/root)"
echo -e "  ${GREEN}Prometheus:${NC}      http://localhost:9091"
echo -e "  ${GREEN}Grafana:${NC}         http://localhost:3001 (admin/admin)"
echo ""
echo -e "Other Services:"
echo -e "  ${GREEN}Jenkins:${NC}        http://localhost:8090"
echo -e "  ${GREEN}SonarQube:${NC}      http://localhost:9000"
echo ""
if [ "$PROMETHEUS_IP" != "N/A" ]; then
    echo -e "Grafana Data Source URL:"
    echo -e "  ${YELLOW}http://${PROMETHEUS_IP}:9090${NC}"
    echo -e "  OR: ${YELLOW}http://host.docker.internal:9091${NC}"
fi
echo ""
echo -e "Useful commands:"
echo -e "  ${YELLOW}View logs:${NC}     docker logs -f <container-name>"
echo -e "  ${YELLOW}Stop all:${NC}      docker stop mysql phone-shop-app prometheus grafana"
echo -e "  ${YELLOW}Restart:${NC}       ./run-project.sh"
echo ""

