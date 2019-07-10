eval $(aws ecr get-login --no-include-email --region us-east-2)
docker tag leandrocgsi/rest-with-spring-boot-udemy:latest $IMAGE_REPO_URL:latest

echo "==========================="
echo "Images are Pushed to Amazon ECR!"
echo "==========================="

# ecs-deploy -c $CLUSTER_NAME -n $SERVICE_NAME -i $IMAGE_REPO_URL:latest 
# ecs-deploy -d $TASK_DEFINITION_NAME -i $IMAGE_REPO_URL:latest

ecs-deploy -c $CLUSTER_NAME -n $SERVICE_NAME -i $IMAGE_REPO_URL:latest

echo "==========================="
echo "Deploy to Amazon ECS is Done!"
echo "==========================="