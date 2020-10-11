# Building image docker
docker build --build-arg WAR_FILE=<war_file_path> --build-arg CONTEXT=<app_context>  -t <image-target>

# Examples:
docker build --build-arg WAR_FILE=tasks-backend/target/tasks-backend.war --build-arg CONTEXT=tasks-backend -t tasks-backend .
docker run --rm --name tasks-backend -p 8003:8080  

docker build --build-arg WAR_FILE=tasks-frontend/target/tasks.war --build-arg CONTEXT=tasks -t tasks-frontend .
docker run --rm --name tasks-frontend -e BACKEND_HOST=192.168.1.87 -e BACKEND_PORT=8003 -p 8004:8080
