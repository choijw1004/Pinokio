pipeline {
    agent any

    environment {
        GIT_CREDENTIALS_ID = '11a82e07d17dd6fc212c993f9a811f81a3' // Jenkins에 저장된 Git 자격증명 ID
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    // Git 저장소를 안전한 디렉토리로 설정
                    sh 'git config --global --add safe.directory /var/lib/jenkins/workspace/ssafy_pipeline'
                }
                git(
                    url: 'https://lab.ssafy.com/s11-webmobile1-sub2/S11P12A601.git',
                    branch: 'develop',
                    credentialsId: env.GIT_CREDENTIALS_ID
                )
            }
        }
        stage('Prepare') {
            steps {
                script {
                    sh 'sudo chown -R jenkins:jenkins /home/ubuntu/S11P12A601'
                    sh 'sudo chmod -R 755 /home/ubuntu/S11P12A601'
                }
            }
        }
        stage('Build and Deploy') {
            steps {
                sh '''
                # 작업 디렉토리로 이동
                cd /home/ubuntu/S11P12A601

                # Git 저장소 갱신
                git pull origin develop

                # Docker Compose를 사용하여 빌드 및 배포
                docker-compose up --build -d
                '''
            }
        }
    }
}

