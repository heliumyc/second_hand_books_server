pipeline {
  agent any
  stages {
    stage('Build') {
      parallel {
        stage('Build') {
          steps {
            git(url: 'https://github.com/heliumyc/second_hand_books_server', branch: 'master')
          }
        }
        stage('Echo') {
          steps {
            sh 'echo \'hello world\' > /home/helium/jenkins_test.txt'
          }
        }
      }
    }
  }
}