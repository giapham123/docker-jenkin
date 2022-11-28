pipeline {
 agent any

  tools {
        gradle "gradle"
  }


 stages {
//   stage('Cloning Git') {
//    steps {
//     git ''
//    }
//   }
//   stage('Install dependencies') {
//    steps {
//
//     sh 'rm -rf out'
//     dir('front-end') {
//
//      sh 'pwd'
//      sh 'ls'
//      sh 'npm install -g yarn'
//      sh 'yarn install'
//      sh 'yarn build'
//
//
//     }
//    }
//   }



  stage('Build war file') {
   steps {
    bat " ./gradlew.bat publish_war"
//     sh 'gradle publish_war'

   }
  }
 }
}