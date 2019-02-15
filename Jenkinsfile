pipeline {
  agent {
    docker {
      image 'gradle'
      args '-u root -v "$PWD":/home/gradle/project --env-file /home/myenv  -w /home/gradle/project'
    }

  }
  stages {
    stage('Build') {
      agent any
      steps {
        sh '''echo $REDIS_PORT
gradle build -i
ls -a'''
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