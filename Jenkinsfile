pipeline {
    agent any

    environment {
        APP_NAME = "health-insurance-portal"
        DOCKER_IMAGE = "health-insurance-portal:latest"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/sadik789diwan/health-insurance-portal.git'
            }
        }

        stage('Build with Maven') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t $DOCKER_IMAGE ."
            }
        }

        stage('Run Docker Container') {
            steps {
                sh "docker run -d -p 8082:8082 --name $APP_NAME $DOCKER_IMAGE || true"
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                sh "kubectl apply -f k8s/deployment.yaml"
                sh "kubectl apply -f k8s/service.yaml"
            }
        }
    }

    post {
        always {
            echo 'Pipeline finished!'
        }
    }
}
