pipeline {
  agent {
    docker {
      image 'gradle'
      args '-u root -v "$PWD":/home/gradle/project  -w /home/gradle/project'
    }

  }
  stages {
    stage('Build') {
      agent any
      steps {
        sh '''gradle build -i
ls -a
ls /build/libs'''
      }
    }
    stage('Deliver') {
      steps {
        sh '''echo \'deliver\'
#sh \'./jenkins/scripts/deliver.sh\''''
      }
    }
  }
}