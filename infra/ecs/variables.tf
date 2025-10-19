variable "aws_region" {
  description = "AWS region to deploy to"
  type        = string
  default     = "ap-south-1"
}

variable "aws_account_id" {
  description = "AWS account ID hosting the ECR repository"
  type        = string
  default     = "273505519511"
}

variable "ecr_repository" {
  description = "Existing ECR repository name (without registry/account)"
  type        = string
}

variable "create_ecr" {
  description = "Whether to create the ECR repository (if it doesn't exist)"
  type        = bool
  default     = false
}

variable "image_tag" {
  description = "Image tag to deploy from the ECR repository"
  type        = string
  default     = "latest"
}

variable "image_uri" {
  description = "Optional full image URI (if provided, overrides repository/tag)"
  type        = string
  default     = ""
}

variable "service_name" {
  description = "ECS service and task family name"
  type        = string
  default     = "tasks-api"
}

variable "desired_count" {
  description = "Number of ECS service tasks"
  type        = number
  default     = 1
}

variable "cpu" {
  description = "Fargate task CPU units (256=0.25 vCPU, 512=0.5 vCPU, 1024=1 vCPU)"
  type        = number
  default     = 512
}

variable "memory" {
  description = "Fargate task memory in MiB (e.g., 1024, 2048)"
  type        = number
  default     = 1024
}

variable "container_port" {
  description = "Container port exposed by the app"
  type        = number
  default     = 4000
}

variable "health_check_path" {
  description = "HTTP path for ALB health checks"
  type        = string
  default     = "/tasks"
}

variable "environment" {
  description = "Environment variables for the container"
  type        = map(string)
  default     = {}
}

variable "tags" {
  description = "Common tags to apply to resources"
  type        = map(string)
  default     = {
    app = "tasks-api"
  }
}
