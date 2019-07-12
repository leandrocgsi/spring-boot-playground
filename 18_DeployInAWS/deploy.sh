# Push to Amazon AWS ECR
eval $(aws ecr get-login --no-include-email --region us-east-2)
docker tag leandrocgsi/rest-with-spring-boot-udemy:latest $IMAGE_REPO_URL:latest
echo "Deploy to AWS are Started!" 
# docker push $IMAGE_REPO_URL:latest

# Push to Amazon AWS ECS
# echo "ecs-deploy -c $CLUSTER_NAME -n $SERVICE_NAME -i $IMAGE_REPO_URL:latest"
# ecs-deploy -d $TASK_DEFINITION_NAME -i $IMAGE_REPO_URL:latest
# ecs-deploy -c $CLUSTER_NAME -n $SERVICE_NAME -i $IMAGE_REPO_URL:latest 
ecs-deploy -c $CLUSTER_NAME -n $SERVICE_NAME -i $IMAGE_REPO_URL:latest -r $AWS_DEFAULT_REGION -t 240