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
                    branches: [[name: '*/develop']],   // or '*/master' depending on your branch
                    userRemoteConfigs: [[
                        url: 'https://github.com/ELTANTAOUI-Y7/Projet-SE-.git',
                        credentialsId: '' // Add your GitHub credentials ID if repository is private
                    ]]
                ]
            }
        }
        
        stage('Build') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'mvn clean compile'
                    } else {
                        bat 'mvn clean compile'
                    }
                }
            }
        }
        
        stage('Test') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'mvn test'
                    } else {
                        bat 'mvn test'
                    }
                }
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
                SONAR_AUTH_TOKEN = credentials('sonar-token') // Store your token in Jenkins credentials
            }
            steps {
                script {
                    def sonarCmd = """mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184:sonar ^
                        -Dsonar.projectKey=projet-se ^
                        -Dsonar.host.url=%SONAR_HOST_URL% ^
                        -Dsonar.login=%SONAR_AUTH_TOKEN% ^
                        -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml"""
                    def sonarCmdUnix = "mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184:sonar " +
                        "-Dsonar.projectKey=projet-se " +
                        "-Dsonar.host.url=${SONAR_HOST_URL} " +
                        "-Dsonar.login=${SONAR_AUTH_TOKEN} " +
                        "-Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml"
                    if (isUnix()) {
                        sh sonarCmdUnix
                    } else {
                        bat sonarCmd
                    }
                }
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