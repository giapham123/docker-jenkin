pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "27121994/flask-docker"
    }

    tools {
        gradle "gradle"
    }
    stages {
        stage('Build war file') {
            environment {
                DOCKER_TAG="${GIT_BRANCH.tokenize('/').pop()}-${GIT_COMMIT.substring(0,7)}"
            }
            steps {
                sh 'ls'
                sh 'java -version'
                sh 'gradle --version'
                sh 'gradle publish_war'
                sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} . "
                sh "docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKER_IMAGE}:latest"
                sh "docker image ls | grep ${DOCKER_IMAGE}"
                withCredentials([usernamePassword(credentialsId: 'docker-hub', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                            sh 'echo $DOCKER_PASSWORD | docker login --username $DOCKER_USERNAME --password-stdin'
                            sh "docker push ${DOCKER_IMAGE}:${DOCKER_TAG}"
                            sh "docker push ${DOCKER_IMAGE}:latest"
                }

            }
        }
    }
}