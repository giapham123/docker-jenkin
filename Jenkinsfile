pipeline {
 agent any

 tools {
  nodejs 'nodejs'
  gradle 'gradle'
 }


//  stages {
//   stage('Cloning Git') {
//    steps {
//     git ''
//    }
//   }
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
   steps {
    sh 'ls'
    sh 'gradle --version'
    sh 'gradle publish_war'

   }
  }


//   stage('Extract war file') {
//    steps {
//     dir('out') {
//      dir('artifacts') {
//       sh 'pwd'
//       sh 'ls'
//       sh 'rm -rf ROOT.war'
//       sh 'rm -rf ROOT'
//
//       sh 'cp -var UNDERWRITING.v1.0.0.0.war ROOT.war'
//       sh 'unzip ROOT.war -d ./ROOT'
//      }
//     }
//    }
//   }
//
//   stage('Building image') {
//    steps {
//     script {
//      sh 'ls'
//      sh 'docker build -f Dockerfile -t underwriting:$BUILD_NUMBER .'
//
//     }
//    }
//   }
//   stage('Push image to regitry') {
//    steps {
//     script {
//      sh 'docker image tag underwriting:$BUILD_NUMBER 10.91.0.86:5000/underwriting/underwriting:$BUILD_NUMBER'
//      sh 'docker push 10.91.0.86:5000/underwriting/underwriting:$BUILD_NUMBER'
//
//
//     //  sh 'docker image tag riskbox 10.91.0.86:5000/riskbox/riskbox:latest'
//     //  sh 'docker push 10.91.0.86:5000/riskbox/riskbox:latest'
//
//     }
//    }
//   }
//   stage('remove on docker-compose 10.91.8.60') {
//    steps {
//     script {

//      sh 'sshpass -p "mafc123!@#$" ssh -o StrictHostKeyChecking=no root@10.91.8.60 pwd'


//      sh 'sshpass -p "mafc123!@#$" ssh -o StrictHostKeyChecking=no root@10.91.8.60 docker-compose -f "/root/home/docker-compose.yml" down'



//      sh 'sshpass -p "mafc123!@#$"  ssh -o StrictHostKeyChecking=no root@10.91.8.60 mv /root/home/ROOT /root/home/ROOT$BUILD_NUMBER'
//      sh 'sshpass -p "mafc123!@#$"  ssh -o StrictHostKeyChecking=no root@10.91.8.60 mv /root/home/ROOT.war /root/home/ROOT$BUILD_NUMBER.war'





//     }
//    }
//   }
//   stage('Deploy on docker-compose 10.91.8.60') {
//    steps {
//     script {

//      sh 'sshpass -p "mafc123!@#$" ssh -o StrictHostKeyChecking=no root@10.91.8.60 pwd'
//       sh 'sshpass -p "mafc123!@#$" ssh -o StrictHostKeyChecking=no root@10.91.8.60 rm -rf /root/home/ftp/*'
//       sh 'sshpass -p "mafc123!@#$" scp -o StrictHostKeyChecking=no  out/artifacts/UNDERWRITING.v1.0.0.0.war root@10.91.8.60:/root/home/'

//      sh 'sshpass -p "mafc123!@#$" ssh -o StrictHostKeyChecking=no root@10.91.8.60 mv /root/home/UNDERWRITING.v1.0.0.0.war /root/home/ROOT.war'

//      sh 'sshpass -p "mafc123!@#$" ssh -o StrictHostKeyChecking=no root@10.91.8.60 unzip /root/home/ROOT.war -d /root/home/ROOT'


//      sh 'sshpass -p "mafc123!@#$" ssh -o StrictHostKeyChecking=no root@10.91.8.60 docker-compose -f "/root/home/docker-compose.yml" up -d --build '

//     }
//    }
//   }
 }
}