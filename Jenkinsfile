pipeline {
  agent {
    docker {
      image 'gradle'
      args '-u root -v "$PWD":/home/gradle/project  -w /home/gradle/project'
    }

  }
  stages {
    stage('Build') {
      agent any
      steps {
        sh '''echo $REDIS_PORT
gradle build -i
ls -a
ls /build/libs'''
      }
    }
    stage('Deliver') {
      steps {
        sh '''echo \'deliver\'
#sh \'./jenkins/scripts/deliver.sh\''''
      }
    }
  }
  environment {
    REDIS_PORT = '$REDIS_PORT'
    MYSQL_HOST = '$MYSQL_HOST'
    WXAPPID = '$WXAPPID'
    WXSECRET = '$WXSECRET'
    MYSQL_USERNAME = '$MYSQL_USERNAME'
    MYSQL_PASSWORD = '$MYSQL_PASSWORD'
    REDIS_HOST = '$REDIS_HOST'
  }
}