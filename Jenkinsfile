pipeline {
  agent {
    docker {
      image 'gradle'
      args '-u root -v gradle-cache:/home/gradle/.gradle -v "$PWD":/home/gradle/project --env-file /home/myenv  -w /home/gradle/project --net="host"'
    }

  }
  stages {
    stage('Build') {
      steps {
        ws(dir: '/home/wwwroot/$JOB_NAME') {
          sh '''ls -a
gradle build -i'''
        }

      }
    }
    stage('Deliver') {
      steps {
        sh 'sh ./jenkins/scripts/deliver.sh'
        ws(dir: '/home/wwwroot/$JOB_NAME') {
          sh '''ls -a
sh ./build/libs/deliver.sh'''
        }

      }
    }
  }
}