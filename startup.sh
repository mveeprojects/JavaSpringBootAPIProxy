echo "Building JAR..."
./gradlew build

echo "Shutting down old containers and networks..."
docker-compose down

echo "Removing old docker image..."
docker rmi -f javaspringbootapiproxy-application

echo "Building and running application and wiremock..."
docker-compose up -d

echo "Startup complete."

sleep 1

docker ps -a

echo "http://localhost/customer/summary/abc"
