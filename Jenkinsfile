pipeline {
  agent {
    docker {
      image 'gradle'
      args '-u root -v gradle-cache:/home/gradle/.gradle -v "$PWD":/home/gradle/project --env-file /home/myenv  -w /home/gradle/project --net="host"'
    }

  }
  stages {
    stage('Build') {
      agent any
      steps {
        sh '''gradle build -i
ls -a'''
      }
    }
    stage('Deliver') {
      steps {
        sh '''sh ./jenkins/scripts/deliver.sh'''
      }
    }
  }
}