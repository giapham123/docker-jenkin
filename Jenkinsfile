pipeline {
    agent any
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
                echo DOCKER_TAG
            }
        }
    }
}