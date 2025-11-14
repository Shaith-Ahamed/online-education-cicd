pipeline {
    agent any
    tools {
        jdk 'jdk17'
        maven 'maven3'
    }
    environment {
        GITHUB_CRED = 'GitHub-Token'
        DOCKERHUB_CRED = 'DockerHub-Token'
        SONAR_CRED = 'SonarQube-Token'
        BACKEND_IMAGE = 'shaith/online-education-backend'
        FRONTEND_IMAGE = 'shaith/online-education-frontend'
    }
    stages {
        // ----------------------------------------
        stage('Code Checkout') {
            steps {
                git branch: 'main',
                    changelog: false,
                    poll: false,
                    url: 'https://github.com/Shaith-Ahamed/online-education-cicd.git',
                    credentialsId: "${GITHUB_CRED}"
            }
        }

        // ----------------------------------------
        stage('Backend: Clean & Compile') {
            steps {
                dir('backend') {  
                    sh "mvn clean compile"
                }
            }
        }

        // ----------------------------------------
        stage('Backend: SonarQube Analysis') {
            steps {
                dir('backend') { 
                    withCredentials([string(credentialsId: "${SONAR_CRED}", variable: 'SONAR_TOKEN')]) {
                        sh '''
                            mvn sonar:sonar \
                                -Dsonar.host.url=http://localhost:9000 \
                                -Dsonar.token=$SONAR_TOKEN \
                                -Dsonar.java.binaries=target/classes
                        '''
                    }
                }
            }
        }

        // ----------------------------------------
        stage('Backend: Package') {
            steps {
                dir('backend') {  
                    sh "mvn package -DskipTests"
                }
            }
        }

        // ----------------------------------------
        stage('Frontend: SonarQube Analysis') {
            agent { docker { image 'node:20-alpine' } }  // Node.js for frontend scan
            steps {
                dir('frontend') {
                    withCredentials([string(credentialsId: "${SONAR_CRED}", variable: 'SONAR_TOKEN')]) {
                        sh '''
                            npm ci
                            npm run build
                            npm run test -- --coverage

                            npm install -g sonar-scanner

                            sonar-scanner \
                                -Dsonar.projectKey=frontend \
                                -Dsonar.sources=src \
                                -Dsonar.tests=src \
                                -Dsonar.language=js \
                                -Dsonar.javascript.lcov.reportPaths=coverage/lcov.info \
                                -Dsonar.host.url=http://localhost:9000 \
                                -Dsonar.login=$SONAR_TOKEN
                        '''
                    }
                }
            }
        }

        // ----------------------------------------
        stage('Backend: Docker Build & Push') {
            steps {
                dir('backend') {  
                    script {
                        withDockerRegistry(credentialsId: "${DOCKERHUB_CRED}", url: '') {
                            def buildTag = "${BACKEND_IMAGE}:${BUILD_NUMBER}"
                            def latestTag = "${BACKEND_IMAGE}:latest"
                            sh "docker build -t ${BACKEND_IMAGE} -f Dockerfile ."
                            sh "docker tag ${BACKEND_IMAGE} ${buildTag}"
                            sh "docker tag ${BACKEND_IMAGE} ${latestTag}"
                            sh "docker push ${buildTag}"
                            sh "docker push ${latestTag}"
                            env.BACKEND_BUILD_TAG = buildTag
                        }
                    }
                }
            }
        }

        // ----------------------------------------
        stage('Frontend: Docker Build & Push') {
            steps {
                dir('frontend') {  
                    script {
                        withDockerRegistry(credentialsId: "${DOCKERHUB_CRED}", url: '') {
                            def buildTag = "${FRONTEND_IMAGE}:${BUILD_NUMBER}"
                            def latestTag = "${FRONTEND_IMAGE}:latest"
                            sh "docker build -t ${FRONTEND_IMAGE} ."
                            sh "docker tag ${FRONTEND_IMAGE} ${buildTag}"
                            sh "docker tag ${FRONTEND_IMAGE} ${latestTag}"
                            sh "docker push ${buildTag}"
                            sh "docker push ${latestTag}"
                            env.FRONTEND_BUILD_TAG = buildTag
                        }
                    }
                }
            }
        }

        // ----------------------------------------
        stage('Staging Deployment') {
            steps {
                sh 'docker compose down || true'
                sh 'docker compose pull'
                sh 'docker compose up -d'
            }
        }
    }

    post {
        always {
            echo 'Cleaning up workspace...'
            cleanWs()
        }
        success {
            echo 'Pipeline completed successfully!'
            echo "Backend Image: ${env.BACKEND_BUILD_TAG}"
            echo "Frontend Image: ${env.FRONTEND_BUILD_TAG}"
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
