pipeline {
    agent any
    stages {
        stage('Build Backend') {
            steps {
                bat 'mvn clean package -DskipTests=true'
            }
        }
        stage('Unit Tests') {
            steps {
                bat 'mvn test'
            }
        }
        stage('Sonar Analysis') {
            environment {
                scannerHome = tool 'SONAR_SCANNER'
            }
            steps {
                withSonarQubeEnv('SONAR_LOCAL') {
                    bat "${scannerHome}/bin/sonar-scanner -e -Dsonar.projectKey=DeployBack -Dsonar.host.url=http://localhost:9000 -Dsonar.login=24327f726ff3013e6d9eea8b5d674b197fccd891 -Dsonar.java.binaries=target -Dsonar.coverage.exclusions=**/.mvn/**,**/src/test/**,**/model/**,**Application.java"
                }                
            } 
        }
        stage('Quality Gate') {
            steps {
                sleep(30)
                timeout(time: 1, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
        stage('Deploy Backend') {
            steps {
                deploy adapters: [tomcat8(credentialsId: 'TomcatLogin', path: '', url: 'http://localhost:8001')], contextPath: 'tasks-backend', war: 'target/tasks-backend.war'
            }
        }
        stage('API Test') {
            steps {
                dir('tasks-api-test') {
                    git credentialsId: 'GitHubLogin', url: 'https://github.com/racmenezes/tasks-api-test'
                    bat "mvn clean test"
                }
            }
        }
        stage('Build Frontend') {
            steps {
                dir('tasks-frontend') {
                    git credentialsId: 'GitHubLogin', url: 'https://github.com/racmenezes/tasks-frontend'
                    bat 'mvn clean package -DskipTests=true'
                }
            }
        }
        stage('Deploy Frontend') {
            steps {
                dir('tasks-frontend') {
                    deploy adapters: [tomcat8(credentialsId: 'TomcatLogin', path: '', url: 'http://localhost:8001')], contextPath: 'tasks', war: 'target/tasks.war'
                }
            }
        }
        stage('Functional Test') {
            steps {
                dir('tasks-functional-test') {
                    git credentialsId: 'GitHubLogin', url: 'https://github.com/racmenezes/tasks-functional-test'
                    bat "mvn clean test"
                }
            }
        }
        stage('Deploy in Production') {
            steps {
                bat "docker-compose build"
                bat "docker-compose up -d"
            }
        }
        stage('Health Check') {
            steps {
                sleep(30)
                dir('tasks-functional-test') {
                    git credentialsId: 'GitHubLogin', url: 'https://github.com/racmenezes/tasks-functional-test'
                    bat "mvn verify -Dskip.surefire.tests"
                }
            }
        }
    }
    post {
        always {
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml, tasks-api-test/target/surefire-reports/*.xml, tasks-functional-test/target/surefire-reports/*.xml, tasks-functional-test/target/failsafe-reports/*.xml'
            archiveArtifacts artifacts: 'target/tasks-backend.war, tasks-frontend/target/tasks.war', onlyIfSuccessful: true
        }
    }
}
