#!/bin/bash

# Jenkins Build Script with JaCoCo Coverage and SonarQube Integration
# Make sure to set these environment variables in Jenkins:
# - SONAR_HOST_URL (e.g., http://localhost:9000)
# - SONAR_AUTH_TOKEN (your SonarQube authentication token)

set -e  # Exit on error

echo "=== Stage: Build ==="
mvn clean compile

echo "=== Stage: Test ==="
mvn clean test

# Publish test results (configure in Jenkins: Post-build action -> Publish JUnit test result report)
# Pattern: target/surefire-reports/*.xml

echo "=== Stage: Generate Coverage Report ==="
mvn jacoco:report

# Publish HTML coverage report (configure in Jenkins: Post-build action -> Publish HTML reports)
# HTML directory to archive: target/site/jacoco
# Index page: index.html

echo "=== Stage: SonarQube Analysis ==="
mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184:sonar \
    -Dsonar.projectKey=projet-se \
    -Dsonar.host.url=${SONAR_HOST_URL} \
    -Dsonar.login=${SONAR_AUTH_TOKEN} \
    -Dsonar.java.coveragePlugin=jacoco \
    -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
    -Dsonar.coverage.exclusions=**/entities/**,**/servlets/**,**/helper/**

echo "=== Build completed successfully ==="

