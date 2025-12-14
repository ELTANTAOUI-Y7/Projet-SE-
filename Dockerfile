# ============================================
# STAGE 1: Build the WAR file
# ============================================
FROM maven:3.9.4-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom.xml for dependency caching
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build WAR (skip tests - Jenkins already ran them)
RUN mvn clean package -DskipTests

# ============================================
# STAGE 2: Runtime with Tomcat
# ============================================
FROM tomcat:9.0-jdk17-temurin

# Clean default webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy WAR from builder
COPY --from=builder /app/target/phone_shoop-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Database connection environment variables
ENV DB_HOST=mysql
ENV DB_PORT=3306
ENV DB_NAME=phone_shoop
ENV DB_USER=root
ENV DB_PASSWORD=root

# Expose Tomcat port
EXPOSE 8080

# Start Tomcat
CMD ["catalina.sh", "run"]