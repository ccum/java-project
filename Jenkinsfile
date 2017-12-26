pipeline {
  agent none

  options {
    buildDiscarder(logRotator(numToKeepStr: '2', artifactNumToKeepStr: '1'))
  }
  enviroment{
    MAJOR_VERSION = 1
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
        sh "if ![ -d '/var/www/html/rectangles/all/${env.BRANCH_NAME}' ]; then mkdir /var/www/html/rectangles/all/${env.BRANCH_NAME}; fi"
        sh "cp dist/rectangle_${env.MAJOR_VERSION}.${env.BUILD_NUMBER}.jar /var/www/html/rectangles/all/${env.BRANCH_NAME}/" 
      }
    }
    stage('Running on ubuntu local'){
      agent any
      steps{
        sh "wget http://ubuntu17.westus2.cloudapp.azure.com:8080/rectangles/all/${env.BRANCH_NAME}/rectangle_${env.MAJOR_VERSION}.${env.BUILD_NUMBER}.jar"
        sh "java -jar rectangle_${env.MAJOR_VERSION}.${env.BUILD_NUMBER}.jar 3 4"
      }

    }
    stage ('Run on Debian') {
      agent{
        docker 'openjdk:8u151-jre'
      }
      steps {
        sh "wget http://ubuntu17.westus2.cloudapp.azure.com:8080/rectangles/all/${env.BRANCH_NAME}/rectangle_${env.MAJOR_VERSION}.${env.BUILD_NUMBER}.jar"
        sh "java -jar rrectangle_${env.MAJOR_VERSION}.${env.BUILD_NUMBER}.jar 3 4"
      }

    }
    stage ('Promote to Green') {
      agent any
      when {
        branch 'master'
      }
      steps {
        sh "cp /var/www/html/rectangles/all/${env.BRANCH_NAME}/rectangle_${env.MAJOR_VERSION}.${env.BUILD_NUMBER}.jar /var/www/html/rectangles/green/rectangle_${env.MAJOR_VERSION}.${env.BUILD_NUMBER}.jar"
      }
    }
    stage ('Promete development branch to master'){
      agent any
      when {
        branch  'devlopment'
      }
      steps
      {
        echo "Stashing any local changes"
        sh "git stash"
        echo "checnking out devlopment branch"
        sh "git checkout devlopment"
        echo "checnking out master branch"
        sh "git checkout master"
        echo "Mergind devlopment into master branch"
        sh "git merge devlopment"
        echo "pushing to origin master"
        sh "git push origin master"
        echo "tagging the release"
        sh "git tag rectangle-${env.MAJOR_VERSION}.${env.BUILD_NUMBER}"
        sh "git push origin rectangle-${env.MAJOR_VERSION}.${env.BUILD_NUMBER}"
        echo "finish"

      }

    }

  }
}