pipeline {
    agent any
    tools {
        maven 'Maven-3.9.11'
        jdk 'JDK-17'
    }

    environment {
        MAVEN_OPTS = '-Xmx1024m'
        SCANNER_HOME = tool 'SonarScanner'
    }

    stages {
        stage('1. Clone Repository') {
            steps {
                echo 'Cloning repository from GitHub...'
                checkout scm
            }
        }

        stage('2. Compile Project') {
            steps {
                echo 'Compiling Maven project...'
                bat 'mvn clean package'  // Changed 'bat' to 'sh' for Linux
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
                bat 'mvn package -DskipTests'  // Changed 'bat' to 'bat' for Linux
            }
            post {
                success {
                    archiveArtifacts artifacts: '**/target/*.jar', allowEmptyArchive: true, fingerprint: true
                }
            }
        }

        
        stage('5. SonarQube Analysis') {
    steps {
        echo 'Running SonarQube analysis...'
        withSonarQubeEnv('SonarQube') {
            bat '''
            # Create empty test directories for modules that don't have tests
            mkdir -p sm-core-model/src/test/java
            mkdir -p sm-core-modules/src/test/java
            mkdir -p sm-shop-model/src/test/java
            
            # Run SonarQube analysis
            mvn sonar:sonar \
                -Dsonar.projectKey=yourwaytoltaly \
                -Dsonar.coverage.exclusions=**/sm-core-model/**,**/sm-core-modules/**,**/sm-shop-model/** \
                -Dsonar.cpd.exclusions=**/sm-core-model/**,**/sm-core-modules/**,**/sm-shop-model/**
            '''
        }
    }
}




        stage('6. Quality Gate Check') {
    steps {
        echo 'Checking Quality Gate...'
        timeout(time: 10, unit: 'MINUTES') {
            waitForQualityGate abortPipeline: false  // Change to false
        }
    }
}
    }

    post {
        success {
            echo ' Pipeline executed successfully!'
        }
        failure {
            echo ' Pipeline failed.'
        }
        always {
            echo 'Cleaning workspace...'
            cleanWs()
        }
    }
}
