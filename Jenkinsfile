pipeline {
  agent {
    docker {
      image 'gradle'
      args '-v /home/helium/app:/home/helium/app'
    }

  }
  stages {
    stage('Build') {
      agent any
      steps {
        sh '''echo $JAVA_HOME
sh \'gradle -v\'
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