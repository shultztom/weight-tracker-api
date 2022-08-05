./gradlew clean build
docker build -t us-central1-docker.pkg.dev/shultzlab/docker-gcp/weight-tracker-api:latest .
docker push us-central1-docker.pkg.dev/shultzlab/docker-gcp/weight-tracker-api:latest