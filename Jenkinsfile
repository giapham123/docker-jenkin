pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "27121994/spring-boot-jenkin-docker"
    }

    tools {
        gradle "gradle"
        nodejs "nodejs"
    }
    stages {
        stage('Install dependencies') {
            steps {
                sh 'rm -rf out'
                dir('front-end') {
                    sh 'pwd'
                    sh 'ls'
                    sh 'npm install -g yarn'
                    sh 'yarn install'
                    sh 'yarn build'
                }
            }
        }
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
                //clean to save disk
//                 sh "docker image rm ${DOCKER_IMAGE}:${DOCKER_TAG}"
//                 sh "docker image rm ${DOCKER_IMAGE}:latest"
            }
        }
    }
    post {
        success {
            echo "SUCCESSFUL"
        }
        failure {
            echo "FAILED"
        }
    }
}