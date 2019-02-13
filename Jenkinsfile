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
        sh '''#sh \'gradle build\'
echo $REDIS_PORT'''
      }
    }
    stage('Deliver') {
      steps {
        sh '#sh \'./jenkins/deliver.sh\''
      }
    }
  }
}