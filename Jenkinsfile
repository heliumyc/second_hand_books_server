pipeline {
  agent {
    docker {
      image 'gradle'
      args '--rm -u root -v "$PWD":/home/gradle/project  -w /home/gradle/project'
    }

  }
  stages {
    stage('Build') {
      agent any
      steps {
        sh '''echo $JAVA_HOME
#sh \'gradle -v\'
'''
      }
    }
    stage('Deliver') {
      steps {
        sh '#sh \'./jenkins/scripts/deliver.sh\''
      }
    }
  }
}