echo $DOCKER_PASSWORD | docker login --username $DOCKER_USERNAME --password-stdin
docker push leandrocgsi/rest-with-spring-boot-udemy

echo "==========================="
echo "Images are Pushed to Docker Hub!"
echo "==========================="