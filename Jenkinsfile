pipeline {
  agent {
    docker {
      image 'gradle'
      args '-v /home/helium/app:/home/helium/app'
    }

  }
  stages {
    stage('Build') {
      steps {
        sh '''echo \'Starting build with gradle\'
sh \'gradle build\'
'''
      }
    }
    stage('Deliver') {
      steps {
        sh 'sh \'./jenkins/scripts/deliver.sh\''
      }
    }
  }
}