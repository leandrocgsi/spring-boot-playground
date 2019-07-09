pip install --user awscli
export PATH=$PATH:$HOME/.local/bin

add-apt-repository ppa:eugenesan/ppa
apt-get update
apt-get install jq -y

curl https://raw.githubusercontent.com/silinternational/ecs-deploy/master/ecs-deploy | sudo tee -a /usr/bin/ecs-deploy
sudo chmod +x /usr/bin/ecs-deploy


echo "We are in the Docker folder!"
# Starting Docker Compose Build!"

docker-compose build

# Use this for AWS ECR
# eval $(aws ecr get-login --region us-east-1)

# Push to Docker Hub
echo "${DOCKER_PASSWORD}" | docker login --username "${DOCKER_USERNAME}" --password-stdin
docker push leandrocgsi/rest-with-spring-boot-udemy

# Push to Amazon AWS ECR
eval $(aws ecr get-login --no-include-email --region us-east-2)
docker tag leandrocgsi/rest-with-spring-boot-udemy:latest $IMAGE_REPO_URL:latest
echo "Deploy to AWS are Started!" 
docker push $IMAGE_REPO_URL:latest

# Push to Amazon AWS ECS
ecs-deploy -c $CLUSTER_NAME -n $SERVICE_NAME -i $IMAGE_REPO_URL:latest 