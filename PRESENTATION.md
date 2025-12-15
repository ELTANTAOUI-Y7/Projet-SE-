# Grafana & Prometheus Presentation

---

## Slide 1: What are Grafana & Prometheus?

### Prometheus
- **Time-series database** - Stores metrics over time
- **Data collection** - Scrapes metrics from applications/services
- **Monitoring & Alerting** - Tracks system performance and health
- **Pull-based** - Actively fetches data from configured targets
- **Open-source** - Industry standard for metrics monitoring

### Grafana
- **Visualization platform** - Creates beautiful dashboards and graphs
- **Data visualization** - Displays metrics from Prometheus (and other sources)
- **Real-time monitoring** - Shows live data updates
- **User-friendly** - Drag-and-drop dashboard creation
- **Alerting** - Can send notifications based on metrics

### How They Work Together
- Prometheus **collects** metrics → Grafana **displays** them
- Prometheus = Data storage → Grafana = Data visualization
- Prometheus scrapes every 15-30 seconds → Grafana queries Prometheus for graphs

---

## Slide 2: Files Created & Their Purpose

### `prometheus.yml`
- **Configuration file** for Prometheus
- Defines **what to monitor** (targets: Jenkins, Phone Shop app)
- Sets **scrape intervals** (how often to collect data)
- Specifies **metrics paths** (where to get data from)

### `docker-compose.yml`
- **Multi-container setup** - Defines all services together
- **MySQL** - Database for the application
- **Phone Shop App** - Main application container
- **Prometheus** - Monitoring service (port 9091)
- **Grafana** - Visualization service (port 3001)
- **Networks** - Connects all containers together

### `run-project.sh`
- **Automation script** - Runs everything with one command
- Starts MySQL, builds app, starts Prometheus & Grafana
- Handles port conflicts automatically
- Preserves database data between runs

---

## Slide 3: Setup Instructions

### Prerequisites
- Docker installed
- Docker Compose installed (optional)

### Quick Setup (Using Script)
```bash
./run-project.sh
```

### Manual Setup Steps
1. **Start MySQL**
   - `docker run -d --name mysql -p 3307:3306 -e MYSQL_ROOT_PASSWORD=root mysql:8.0`

2. **Start Prometheus**
   - `docker run -d --name prometheus -p 9091:9090 -v ./prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus`

3. **Start Grafana**
   - `docker run -d --name grafana -p 3001:3000 -e GF_SECURITY_ADMIN_PASSWORD=admin grafana/grafana`

4. **Configure Grafana**
   - Access: `http://localhost:3001`
   - Login: `admin` / `admin`
   - Add Prometheus data source: `http://prometheus:9090`

### Access URLs
- **Grafana**: http://localhost:3001 (admin/admin)
- **Prometheus**: http://localhost:9091
- **Application**: http://localhost:8080

---

