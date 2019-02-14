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
        sh '''sh \'gradle -v\'
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