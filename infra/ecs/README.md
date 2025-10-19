# ECS Fargate Deployment (Terraform)

This Terraform stack deploys the Spring Boot app to AWS ECS (Fargate) behind an Application Load Balancer. It uses an existing ECR repository for the container image.

## Prerequisites

- Terraform >= 1.4
- AWS credentials configured with permissions for VPC, IAM, ECS, ALB, CloudWatch Logs, etc.
- ECR repository with your image pushed.

## Variables

- `aws_region` (default `ap-south-1`)
- `aws_account_id` (default `273505519511`)
- `ecr_repository` (required) — ECR repo name (existing or to be created)
- `create_ecr` (default `false`) — set `true` to create the ECR repository
- `image_tag` (default `latest`) — image tag in the repo
- `image_uri` (optional) — full image URI; overrides repo/tag if set
- `service_name` (default `tasks-api`)
- `desired_count` (default `1`)
- `cpu` (default `512`) and `memory` (default `1024`) for Fargate
- `container_port` (default `4000`)
- `health_check_path` (default `/tasks`)
- `environment` (map) — environment variables for the container
- `tags` (map) — resource tags

## Usage

From this directory:

```
terraform init
terraform plan -var="ecr_repository=your-repo" -var="image_tag=latest"
terraform apply -auto-approve -var="ecr_repository=your-repo" -var="image_tag=latest"
```

Alternatively, provide a full `image_uri`:

```
terraform apply -auto-approve \
  -var="image_uri=273505519511.dkr.ecr.ap-south-1.amazonaws.com/your-repo:latest"
```

Outputs will include the ALB DNS name. Access the app at:

```
http://<alb_dns_name>/swagger-ui.html
```

## Notes

- This stack creates a simple VPC with two public subnets and runs the service with a public IP. For private networking and NAT, we can extend the stack.
- Health check path is `/tasks`. Adjust if you add Actuator (e.g., `/actuator/health`).
- IAM execution role is attached to `AmazonECSTaskExecutionRolePolicy` for ECR pulls and logs.
- Logs are sent to CloudWatch Logs at `/ecs/<service_name>`.

### Create ECR repo with Terraform

If you want Terraform to create the ECR repository, enable `create_ecr`:

```
terraform apply -auto-approve \
  -var="ecr_repository=tasks-api" \
  -var="create_ecr=true"
```

The output `ecr_repository_url` will contain the full push URL.
