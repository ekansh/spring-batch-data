"# spring-batch-data" 
To run on EC2/ Unix instance
sudo yum update -y
sudo amazon-linux-extras install docker
sudo yum install git -y
sudo service docker start
mkdir projects
cd projects
git clone https://github.com/ekansh/spring-batch-data.git
sudo docker build -t springio/gs-spring-boot-docker .
sudo docker run --mount type=bind,source="$(pwd)",target=/home -p 8080:8080 springio/gs-spring-boot-docker --inputFile=/home/vesting_events.csv --fromDate=2020-04-01
