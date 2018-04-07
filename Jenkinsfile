pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        echo "Building ${env.BRANCH_NAME} branch"
        sh 'mvn compile'
      }
    }
    stage('Test') {
      steps {
        echo 'Testing..'
      }
    }
    stage('Deploy') {
      steps {
        echo 'Deploying....'
      }
    }
  }
  post {
    always {
      echo currentBuild.result
      slackSend(color: '#00FF00', message: "${currentBuild.result}")
      
    }
    
  }
}