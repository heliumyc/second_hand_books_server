pipeline {
  agent any
  stages {
    stage('Echo') {
      parallel {
        stage('Echo') {
          steps {
            sh 'echo \'hello world\' > /home/helium/jenkins_test.txt'
          }
        }
        stage('') {
          steps {
            sh 'ls /home'
          }
        }
      }
    }
  }
}