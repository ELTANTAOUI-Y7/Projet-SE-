# Commands to Run the Project

## Prerequisites
- Docker installed and running
- Jenkins running on port 8090
- SonarQube running on port 9000

## 1. Create Docker Network (if needed)
```bash
docker network create monitoring-network
```

## 2. Run Prometheus

### Stop and remove existing Prometheus (if any)
```bash
docker stop prometheus 2>/dev/null
docker rm prometheus 2>/dev/null
```

### Run Prometheus
```bash
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
```

## 3. Run Grafana

### Stop and remove existing Grafana (if any)
```bash
docker stop grafana 2>/dev/null
docker rm grafana 2>/dev/null
```

### Run Grafana
```bash
docker run -d \
  --name grafana \
  --network monitoring-network \
  -p 3001:3000 \
  -e GF_SECURITY_ADMIN_USER=admin \
  -e GF_SECURITY_ADMIN_PASSWORD=admin \
  -e GF_SERVER_ROOT_URL=http://localhost:3001 \
  -v grafana_data:/var/lib/grafana \
  grafana/grafana:10.4.0
```

## 4. Check Status
```bash
# Check running containers
docker ps | grep -E "(prometheus|grafana)"

# Check logs
docker logs prometheus
docker logs grafana

# Check Prometheus IP (for Grafana data source)
docker inspect prometheus --format '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}'
```

## 5. Restart Services
```bash
# Restart both
docker restart prometheus grafana

# Restart individually
docker restart prometheus
docker restart grafana
```

## 6. Stop Services
```bash
# Stop both
docker stop prometheus grafana

# Stop individually
docker stop prometheus
docker stop grafana
```

## 7. Remove Services
```bash
# Stop and remove both
docker stop prometheus grafana
docker rm prometheus grafana

# Remove volumes (optional - deletes data)
docker volume rm prometheus_data grafana_data
```

## 8. View Logs
```bash
# View logs
docker logs prometheus
docker logs grafana

# Follow logs (real-time)
docker logs -f prometheus
docker logs -f grafana

# View last N lines
docker logs prometheus --tail=20
docker logs grafana --tail=20
```

## 9. Test Connectivity
```bash
# Test Prometheus from Grafana container
docker exec grafana curl http://prometheus:9090/api/v1/status/config

# Test Jenkins from Prometheus container
docker exec prometheus curl http://host.docker.internal:8090/prometheus
```

## 10. Access URLs
- **Prometheus**: http://localhost:9091
- **Grafana**: http://localhost:3001 (admin/admin)
- **Jenkins**: http://localhost:8090
- **SonarQube**: http://localhost:9000

## 11. Grafana Data Source Configuration
When adding Prometheus as a data source in Grafana, use one of these URLs:
- `http://172.19.0.2:9090` (IP address - check current IP with inspect command)
- `http://host.docker.internal:9091` (host gateway)
- `http://prometheus:9090` (container name - may not always work)

## Quick Start Script
```bash
#!/bin/bash
# Create network
docker network create monitoring-network 2>/dev/null || true

# Stop and remove existing containers
docker stop prometheus grafana 2>/dev/null
docker rm prometheus grafana 2>/dev/null

# Run Prometheus
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

# Run Grafana
docker run -d \
  --name grafana \
  --network monitoring-network \
  -p 3001:3000 \
  -e GF_SECURITY_ADMIN_USER=admin \
  -e GF_SECURITY_ADMIN_PASSWORD=admin \
  -e GF_SERVER_ROOT_URL=http://localhost:3001 \
  -v grafana_data:/var/lib/grafana \
  grafana/grafana:10.4.0

echo "Prometheus: http://localhost:9091"
echo "Grafana: http://localhost:3001"
echo "Get Prometheus IP for Grafana: docker inspect prometheus --format '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}'"
```

