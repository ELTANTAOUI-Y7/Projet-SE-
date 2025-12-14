# 📦 Docker - Récapitulatif Complet

> **Projet:** Phone Shop - Application JEE E-commerce  
> **Docker Hub:** `xxxxxxxx15339/phone-shop`  
> **Date:** Novembre 2025

---

## 🎯 Objectif de l'Étape Docker

**Pourquoi containeriser l'application ?**

| Avantage | Description |
|----------|-------------|
| **Portabilité** | L'application fonctionne de manière identique sur n'importe quelle machine |
| **Isolation** | Chaque container a ses propres dépendances, pas de conflits |
| **Déploiement simplifié** | Un seul fichier (image) contient tout le nécessaire |
| **Préparation Kubernetes** | K8s déploie des containers Docker |

---

## 📁 Fichiers Créés

### 1. `Dockerfile`

| Aspect | Description |
|--------|-------------|
| **Rôle** | Instructions pour construire l'image Docker de l'application |
| **Pourquoi multi-stage ?** | Sépare build (Maven) et runtime (Tomcat) → image finale plus légère (~485MB au lieu de ~1GB) |
| **Base images** | `maven:3.9.4-eclipse-temurin-17` (build) + `tomcat:9.0-jdk17-temurin` (runtime) |
| **Résultat** | WAR déployé automatiquement dans Tomcat |

**Structure du Dockerfile :**

```
Stage 1 (builder):           Stage 2 (runtime):
┌─────────────────────┐      ┌─────────────────────┐
│ Maven + JDK 17      │      │ Tomcat 9 + JDK 17   │
│ pom.xml             │ ───▶ │ ROOT.war            │
│ src/                │      │ Port 8080           │
│ ➜ Génère WAR file   │      └─────────────────────┘
└─────────────────────┘            (Image Finale)
```

**Contenu du Dockerfile :**

```dockerfile
# Stage 1: Build
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM tomcat:9.0-jdk17-temurin
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=builder /app/target/phone_shoop-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
ENV DB_HOST=mysql DB_PORT=3306 DB_NAME=phone_shoop DB_USER=root DB_PASSWORD=root
EXPOSE 8080
CMD ["catalina.sh", "run"]
```

---

### 2. `.dockerignore`

| Aspect | Description |
|--------|-------------|
| **Rôle** | Exclure les fichiers inutiles du contexte de build |
| **Pourquoi ?** | Build plus rapide, image plus petite, sécurité (pas de secrets) |
| **Fichiers exclus** | `.git/`, `target/`, IDE files, `k8s/`, docs, logs |

**Contenu du .dockerignore :**

```
.git
.gitignore
.idea
*.iml
target/
Dockerfile
docker-compose*.yml
k8s/
docs/
*.md
Jenkinsfile
prometheus/
grafana/
*.log
```

---

### 3. `docker-compose.yml`

| Aspect | Description |
|--------|-------------|
| **Rôle** | Orchestrer plusieurs containers (app + MySQL) localement |
| **Pourquoi ?** | Test de l'application complète avant déploiement K8s |
| **Services** | `mysql` (base de données) + `phone-shop` (application) |
| **Réseau** | `phone-shop-network` - permet aux containers de communiquer |
| **Volume** | `mysql_data` - persiste les données MySQL |

**Architecture docker-compose :**

```
┌────────────────────────────────────────────────────┐
│                 docker-compose                      │
│                                                     │
│  ┌──────────────────┐      ┌──────────────────┐   │
│  │      MySQL       │◄─────│   Phone-Shop     │   │
│  │    Port 3306     │      │  Port 8080/8081  │   │
│  │   (interne)      │      │   (exposé)       │   │
│  └──────────────────┘      └──────────────────┘   │
│          │                          │              │
│          ▼                          ▼              │
│    mysql_data volume         Accessible via        │
│    (persistence)             localhost:8081        │
└────────────────────────────────────────────────────┘
```

---

## 🔧 Commandes Docker - Récapitulatif

### Construction de l'image

```bash
# Build simple (local)
docker build -t phone-shop:1.0 .

# Build avec tag Docker Hub
docker build -t xxxxxxxx15339/phone-shop:1.0 .
```

### Gestion des images

```bash
# Lister les images
docker images

# Supprimer une image
docker rmi phone-shop:1.0

# Voir la taille des images
docker images --format "{{.Repository}}:{{.Tag}} - {{.Size}}"
```

### Docker Hub

```bash
# Connexion à Docker Hub
docker login

# Pousser l'image (version spécifique)
docker push xxxxxxxx15339/phone-shop:1.0

# Tagger comme latest
docker tag xxxxxxxx15339/phone-shop:1.0 xxxxxxxx15339/phone-shop:latest

# Pousser latest
docker push xxxxxxxx15339/phone-shop:latest
```

### Docker Compose

```bash
# Construire et démarrer tous les services
docker-compose up --build

# Démarrer en arrière-plan (detached)
docker-compose up -d

# Voir les logs de tous les services
docker-compose logs -f

# Voir les logs d'un service spécifique
docker-compose logs -f phone-shop

# État des containers
docker-compose ps

# Arrêter tous les services
docker-compose down

# Arrêter et supprimer les volumes (reset complet)
docker-compose down -v
```

### Debug / Troubleshooting

```bash
# Entrer dans un container en cours d'exécution
docker exec -it phone-shop-app /bin/bash

# Voir les logs d'un container
docker logs phone-shop-app

# Suivre les logs en temps réel
docker logs -f phone-shop-app

# Inspecter un container (détails complets)
docker inspect phone-shop-app

# Voir l'utilisation des ressources (CPU, RAM)
docker stats

# Vérifier quel processus utilise un port
sudo lsof -i :8080
```

---

## 📊 Réponses aux Questions du Projet

### Q1: Quel est le contenu de votre Dockerfile ?

**Réponse:** Notre Dockerfile utilise un **multi-stage build** avec deux étapes :
1. **Stage Builder** : Utilise `maven:3.9.4-eclipse-temurin-17` pour compiler le code Java et générer le fichier WAR
2. **Stage Runtime** : Utilise `tomcat:9.0-jdk17-temurin` comme serveur d'application léger

Cette approche réduit la taille de l'image finale de ~1GB à ~485MB car Maven et le code source ne sont pas inclus dans l'image finale.

### Q2: Quel est le nom et la version de votre image publiée ?

**Réponse:** 
- **Image** : `xxxxxxxx15339/phone-shop`
- **Tags** : `1.0` et `latest`
- **URL Docker Hub** : `https://hub.docker.com/r/xxxxxxxx15339/phone-shop`

### Q3: Donnez la commande de lancement

**Réponse:**

Avec Docker seul (nécessite une base MySQL externe) :
```bash
docker run -d -p 8080:8080 \
  -e DB_HOST=host.docker.internal \
  -e DB_PORT=3306 \
  -e DB_NAME=phone_shoop \
  -e DB_USER=root \
  -e DB_PASSWORD=root \
  --name phone-shop-app \
  xxxxxxxx15339/phone-shop:1.0
```

Avec docker-compose (recommandé - inclut MySQL) :
```bash
docker-compose up -d
```

---

## 🔄 Workflow Complet Docker

```
1. Développement Local
        │
        ▼
2. docker build -t xxxxxxxx15339/phone-shop:1.0 .
        │
        ▼
3. docker-compose up --build (test local)
        │
        ▼
4. docker login
        │
        ▼
5. docker push xxxxxxxx15339/phone-shop:1.0
        │
        ▼
6. Image disponible pour Kubernetes
```

---

## ✅ Checklist Docker

- [x] `Dockerfile` créé avec multi-stage build
- [x] `.dockerignore` configuré pour exclure fichiers inutiles
- [x] `docker-compose.yml` avec services MySQL + Application
- [x] Image buildée localement avec succès
- [x] Tests avec docker-compose réussis (app accessible sur localhost:8081)
- [x] Compte Docker Hub créé
- [x] Image poussée sur Docker Hub : `xxxxxxxx15339/phone-shop:1.0`
- [x] Image téléchargeable : `docker pull xxxxxxxx15339/phone-shop:1.0`

---

## 🐛 Problèmes Rencontrés et Solutions

| Problème | Cause | Solution Appliquée |
|----------|-------|-------------------|
| `port already in use` | Port 8080 occupé par Jenkins | Changé le port dans docker-compose à 8081:8080 |
| `Temporary failure in name resolution` | Problème DNS réseau | Résolu en fixant le DNS / réessayant |
| `version is obsolete` warning | docker-compose version ancienne | Warning ignoré (non bloquant) |

---

## 📸 Captures d'écran (à ajouter)

- [ ] Build Docker réussi
- [ ] docker-compose up fonctionnel
- [ ] Application accessible sur localhost:8081
- [ ] Image visible sur Docker Hub

---

## 📚 Ressources Utiles

- [Documentation Docker](https://docs.docker.com/)
- [Docker Hub](https://hub.docker.com/)
- [Best Practices Dockerfile](https://docs.docker.com/develop/develop-images/dockerfile_best-practices/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
