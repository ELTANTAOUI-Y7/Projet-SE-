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
        SONAR_HOST_URL = credentials('https://undefaced-erich-nondisciplining.ngrok-free.dev ')  // Jenkins credential ID for Sonar server URL
        SONAR_AUTH_TOKEN = credentials('squ_054bafd83ef7067698e514a1ce98e982584f1bf5') // Jenkins credential ID for Sonar token
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm  // Automatically checks out the current branch
            }
        }

        stage('Build & Package') {
            steps {
                sh 'mvn clean package'
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
                withSonarQubeEnv('Sonar-Server') {
                    sh """
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
//         sh 'mvn jetty:run'
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
