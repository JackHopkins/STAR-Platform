export AWS_CREDENTIAL_FILE=/Users/jack/Downloads/rootkey.csv
export ELASTICBEANSTALK_URL="https://elasticbeanstalk.eu-west-1.amazonaws.com" 
activator docker:stage
rm -r target/template/files
mv target/docker/files target/template
cd target/template
version="latest"
docker build -t noddybear/star:$version .
docker create noddybear/star:$version
docker push noddybear/star:$version
#docker run -d -p 0.0.0.0:9000:9000 noddybear/star:$version

#sed -i "s/dockerfile/library/g" Dockerfile

#find . -name "*." -exec sed -i '' s/dockerfile/library/g \;
#zip -r ../deployments/kickadvisor-$(date "+%Y.%m.%d-%H.%M.%S").zip .

#rm -rf kickadvisor-$(date "+%Y.%m.%d-%H.%M.%S")
#eb console
