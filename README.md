# Task API (Spring Boot)

Spring Boot 3.3 REST API implementing the provided OpenAPI for task management with JPA + H2.

## Run

Requirements: Java 17+ and Maven.

```
mvn spring-boot:run
```

Server runs on `http://localhost:4000`.

Swagger UI: `http://localhost:4000/swagger-ui.html`

## Endpoints (summary)

- `GET /tasks` — list with filters `status, group, ownerId, unassigned, q, page, pageSize`
- `POST /tasks` — create task
- `GET /tasks/{id}` — get task
- `PATCH /tasks/{id}` — partial update (send only fields to change)
- `DELETE /tasks/{id}` — delete task
- `POST /tasks/{id}/lock` — claim unassigned task, body `{ "userId": "u2" }`
- `POST /tasks/{id}/unlock` — unassign task, body optional `{ "userId": "u2" }`

## Notes

- Persistence uses H2 in-memory DB; two users are pre-seeded: `u1 (Alex)`, `u2 (Sam)`.
- `ownerId` must reference an existing user; otherwise 400 is returned.
- Pagination is 1-based per the spec.

## AWS Deployment (ECS Fargate)

Prerequisites
- AWS CLI configured with permissions for VPC, ECS, IAM, ALB, Logs
- Terraform >= 1.4
- Docker installed

1) Create the ECR repository first (Terraform)

```
set REGION=ap-south-1
set ACCOUNT_ID=273505519511
set REPO=tasks-api

cd infra/ecs
terraform init
terraform apply -auto-approve -target=aws_ecr_repository.this -var="aws_region=$REGION" -var="aws_account_id=$ACCOUNT_ID" -var="ecr_repository=$REPO" -var="create_ecr=true"

  terraform apply -auto-approve -target=aws_ecr_repository.this[0] -target=aws_ecr_lifecycle_policy.this[0]

:: Optionally capture the repo URL from Terraform output (PowerShell):
::   $ECR_URL = terraform output -raw ecr_repository_url
```

2) Build and push the Docker image to ECR

```
set TAG=latest

aws ecr get-login-password --region %REGION% | docker login --username AWS --password-stdin %ACCOUNT_ID%.dkr.ecr.%REGION%.amazonaws.com

mvn -DskipTests package
docker build -t %REPO% .
docker tag %REPO%:latest %ACCOUNT_ID%.dkr.ecr.%REGION%.amazonaws.com/%REPO%:%TAG%
docker push %ACCOUNT_ID%.dkr.ecr.%REGION%.amazonaws.com/%REPO%:%TAG%
docker push $ACCOUNT_ID.dkr.ecr.$REGION.amazonaws.com/$REPO:$TAG

:: Alternatively, if using PowerShell and you set $ECR_URL from Terraform output:
::   docker tag %REPO%:latest $ECR_URL:%TAG%
::   docker push $ECR_URL:%TAG%
```

3) Provision the ECS Fargate service and ALB (Terraform)

```
cd infra/ecs
terraform apply -auto-approve ^
  -var="aws_region=%REGION%" ^
  -var="aws_account_id=%ACCOUNT_ID%" ^
  -var="ecr_repository=%REPO%" ^
  -var="image_tag=%TAG%"

:: Or provide the full image URI instead of repo/tag:
:: terraform apply -auto-approve ^
::   -var="image_uri=%ACCOUNT_ID%.dkr.ecr.%REGION%.amazonaws.com/%REPO%:%TAG%"
```

4) Access the service
- After apply, Terraform outputs `alb_dns_name`.
- Open: `http://<alb_dns_name>/swagger-ui.html`

Customization
- Container port defaults to 4000. Change via `-var="container_port=4000"` if needed.
- Health check path defaults to `/tasks`. Adjust with `-var="health_check_path=/actuator/health"` if you add Spring Actuator.
- Inject environment variables into the container with `-var='environment={"SPRING_PROFILES_ACTIVE"="prod"}'`.
- For private subnets, TLS, or autoscaling, extend the Terraform under `infra/ecs`.
