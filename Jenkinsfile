pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo "Building ${env.BRANCH_NAME} branch"
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
        slackSend (color: '#00FF00', message: "${currentBuild.result}")
    }
  }
}
