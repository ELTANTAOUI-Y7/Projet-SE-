pipeline {
    agent any

    tools {
        jdk 'JDK11'
        maven 'Maven3'
    }

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    triggers {
        githubPush()
    }

    environment {
        MAVEN_OPTS = '-Dmaven.test.failure.ignore=false'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                bat 'mvn -B clean compile'
            }
        }

        stage('Test') {
            steps {
                bat 'mvn -B test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                bat 'mvn -B package'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/*.war', fingerprint: true
                }
            }
        }

        stage('SonarQube') {
            when {
                allOf {
                    expression { return env.SONAR_HOST_URL }
                }
            }
            environment {
                SONAR_SCANNER_OPTS = '-Xmx512m'
            }
            steps {
                withSonarQubeEnv('Sonar-Server') {
                    bat 'mvn -B sonar:sonar'
                }
            }
            post {
                always {
                    script {
                        timeout(time: 1, unit: 'HOURS') {
                            waitForQualityGate abortPipeline: true
                        }
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline succeeded!'
        }
        failure {
            echo 'Pipeline failed. Check logs above.'
        }
    }
}

