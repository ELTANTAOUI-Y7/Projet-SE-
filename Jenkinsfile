pipeline {
    agent any

    tools {
        jdk 'JDK17'       
        maven 'Maven3'    
    }

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    triggers {
        githubPush()      // Automatically builds when GitHub push occurs
    }

    environment {
        MAVEN_OPTS = '-Dmaven.test.failure.ignore=false'
        SONAR_HOST_URL = 'http://localhost:9000'  // Jenkins credential ID for Sonar server URL
        SONAR_AUTH_TOKEN = credentials('Ecommerce') // Jenkins credential ID for Sonar token
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm  // Automatically checks out the current branch
            }
        }

        stage('Build & Package') {
            steps {
                bat 'mvn clean package'
            }
        }
                stage('3. Run Unit Tests') {
            steps {
                echo 'Running unit tests...'
                bat 'mvn test'  // Changed 'bat' to 'sh' for Linux
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('4. Generate JAR Package') {
            steps {
                echo 'Creating JAR package...'
                bat 'mvn package -DskipTests'  // Changed 'bat' to 'sh' for Linux
            }
            post {
                success {
                    archiveArtifacts artifacts: '**/target/*.jar', allowEmptyArchive: true, fingerprint: true
                }
            }
        }

        stage('SonarQube Analysis') {
            when {
                expression { return env.SONAR_HOST_URL != null }
            }
            environment {
                SONAR_SCANNER_OPTS = '-Xmx512m'
            }
            steps {
                withSonarQubeEnv('SonarQube') {
                    bat """
                        mvn sonar:sonar \
                        -Dsonar.projectKey=Projet-SE \
                        -Dsonar.host.url=$SONAR_HOST_URL \
                        -Dsonar.login=$SONAR_AUTH_TOKEN
                    """
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

// stage('Run Jetty') {
//     steps {
//         bat 'mvn jetty:run'
//     }
// }
    }

    post {
        success {
            echo "Pipeline succeeded!"
        }
        failure {
            echo "Pipeline failed. Check logs above."
        }
    }
}
