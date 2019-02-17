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
        println wxDir
        sh '''gradle build -i
echo $JOB_BASE_NAME
echo $JOB_NAME'''
      }
    }
    stage('Deliver') {
      steps {
        sh 'sh ./jenkins/scripts/deliver.sh'
      }
    }
  }
}