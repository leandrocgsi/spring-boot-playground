sudo apt-get -y install python3-pip python-dev
sudo pip3 install -U setuptools
sudo pip3 install -U virtualenvwrapper
python3 -V
pip3 -V

sudo pip3 install wheel
sudo pip3 install awscli --upgrade --user

export PATH=$PATH:$HOME/.local/bin

add-apt-repository ppa:eugenesan/ppa
apt-get update
apt-get install jq -y

curl https://raw.githubusercontent.com/silinternational/ecs-deploy/master/ecs-deploy | sudo tee -a /usr/bin/ecs-deploy
sudo chmod +x /usr/bin/ecs-deploy

echo "==========================="
echo "AWS ECS-Deploy Setup is Configured!"
echo "==========================="