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
        stage('Test') {
    steps {
        echo 'Pipeline is running!'
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
