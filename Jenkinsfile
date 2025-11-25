pipeline {
    agent any
    
    tools {
        jdk 'JDK' // Configure JDK tool in Jenkins Global Tool Configuration (e.g., JDK-11, JDK-17, or JDK)
        maven 'maven' // Configure Maven tool in Jenkins Global Tool Configuration
    }
    
    stages {
        stage('Checkout Code') {
            steps {
                checkout scm: [
                    $class: 'GitSCM',
                    branches: [[name: '*/main']], // or '*/master' depending on your branch
                    userRemoteConfigs: [[
                        url: 'https://github.com/ELTANTAOUI-Y7/Projet-SE-.git',
                        credentialsId: '' // Add your GitHub credentials ID if repository is private
                    ]]
                ]
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }
        
        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml' // Publish test results
                }
            }
        }
        
        stage('SonarQube Analysis') {
            environment {
                SONAR_HOST_URL = 'http://localhost:9000' // Replace with your SonarQube URL
                SONAR_AUTH_TOKEN = credentials('sonarqube') // Store your token in Jenkins credentials
            }
            steps {
                // Use fully qualified plugin name - no need to add to pom.xml
                sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184:sonar -Dsonar.projectKey=projet-se -Dsonar.host.url=$SONAR_HOST_URL -Dsonar.login=$SONAR_AUTH_TOKEN -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml'
            }
        }
        
    }
    
    post {
        always {
            cleanWs() // Clean workspace after build
        }
        success {
            echo 'Pipeline succeeded!'
        }
        failure {
            echo 'Pipeline failed!'
    }
}
}