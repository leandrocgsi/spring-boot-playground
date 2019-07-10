sudo apt-get -y install python3-pip python-dev
sudo pip3 install -U setuptools
sudo pip3 install -U virtualenvwrapper
python3 -V
pip3 -V

sudo pip3 uninstall wheel
sudo pip3 install wheel

sudo pip3 uninstall awscli
sudo pip3 install awscli --upgrade --user

aws --version

export PATH=$PATH:$HOME/.local/bin

add-apt-repository ppa:eugenesan/ppa
apt-get update
apt-get install jq -y

curl https://raw.githubusercontent.com/silinternational/ecs-deploy/master/ecs-deploy | sudo tee -a /usr/bin/ecs-deploy
sudo chmod +x /usr/bin/ecs-deploy

echo "==========================="
echo "AWS ECS-Deploy Setup is Configured!"
echo "==========================="

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