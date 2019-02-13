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
        sh 'echo "hello"'
      }
    }
  }
}