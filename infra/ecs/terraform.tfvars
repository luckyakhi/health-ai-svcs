aws_region     = "ap-south-1"
aws_account_id = "273505519511"

# Use an existing ECR repo name, or set create_ecr = true to create it
ecr_repository = "tasks-api"
create_ecr     = true

# Image selection: either set image_tag (with repo above) or provide full image_uri
image_tag = "latest"
image_uri = ""

# Service/task settings
service_name   = "tasks-api"
desired_count  = 1
cpu            = 512     # 0.5 vCPU
memory         = 1024    # MiB
container_port = 4000
health_check_path = "/tasks"

# Optional environment variables for the container
environment = {
  # SPRING_PROFILES_ACTIVE = "prod"
}

# Common tags applied to resources
tags = {
  app = "tasks-api"
  env = "dev"
}

