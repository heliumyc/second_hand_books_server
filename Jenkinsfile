pipeline {
  agent {
    docker {
      image 'gradle'
      args '-u root -v gradle-cache:/home/gradle/.gradle -v "$PWD":/home/gradle/project --env-file /home/myenv  -w /home/gradle/project --net="host"'
      reuseNode true
    }

  }
  stages {
    stage('Build') {
      steps {
        sh 'gradle build -i'
      }
    }
    stage('Deliver') {
      steps {
        sh '''ls -a
sh ./jenkins/scripts/deliver.sh'''
      }
    }
  }
    post {
      success {
          sh './jenkins/scripts/deploy.sh'
      }
    }
}