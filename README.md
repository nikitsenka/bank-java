# Build

```bash
docker build --no-cache -f ./docker/app.dockerfile . --platform=linux/amd64
```

# Deploy in AWS

```bash
aws cloudformation create-stack --stack-name myteststack --template-body file://./aws/cloudformation.yml
```
