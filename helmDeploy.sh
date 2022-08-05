gcloud auth print-access-token | helm registry login -u oauth2accesstoken \
--password-stdin https://us-central1-docker.pkg.dev

helm upgrade --install weight-tracker-api oci://us-central1-docker.pkg.dev/shultzlab/shultzlab-helm/spring-boot-http-deploy --version 0.1.1 -f .helm/deploy/values.yaml