pipeline {
  agent none

  options {
    buildDiscarder(logRotator(numToKeepStr: '2', artifactNumToKeepStr: '1'))
  }

  stages {
    stage('Unit Tests'){
        agent any
        steps{
            sh 'ant -f test.xml -v'
            junit 'reports/result.xml'
        }
    }
    stage('build') {
      agent any
      steps {
        sh 'ant -f build.xml -v'
      }
      post {
        success {
          archiveArtifacts artifacts: 'dist/*.jar', fingerprint: true
        }
      }
    }   
    stage('Deploy'){
      agent any
      steps{
        sh "cp dist/rectangle_${env.BUILD_NUMBER}.jar /var/www/html/rectangles/all/" 
      }
    }
    stage('Running on ubuntu local'){
      agent any
      steps{
        sh "wget http://ubuntu17.westus2.cloudapp.azure.com:8080/rectangles/all/rectangle_${env.BUILD_NUMBER}.jar"
        sh "java -jar rectangle_${env.BUILD_NUMBER}.jar 3 4"
      }

    }
    stage ('Run on Debian') {
      agent{
        docker 'openjdk:8u151-jre'
      }
      steps {
        sh "wget http://ubuntu17.westus2.cloudapp.azure.com:8080/rectangles/all/rectangle_${env.BUILD_NUMBER}.jar"
        sh "java -jar rectangle_${env.BUILD_NUMBER}.jar 3 4"
      }

    }
    stage('Promote to Green') {
      steps {
        sh "cp /var/www/html/rectangles/all/rectangle_${env.BUILD_NUMBER}.jar /var/www/html/rectangles/green/rectangle_${env.BUILD_NUMBER}.jar"
      }
    }

  }
}