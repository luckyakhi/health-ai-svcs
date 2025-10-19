output "alb_dns_name" {
  description = "Public DNS name of the Application Load Balancer"
  value       = aws_lb.this.dns_name
}

output "cluster_name" {
  value       = aws_ecs_cluster.this.name
  description = "ECS cluster name"
}

output "service_name" {
  value       = aws_ecs_service.this.name
  description = "ECS service name"
}

output "target_group_arn" {
  value       = aws_lb_target_group.this.arn
  description = "Target group ARN"
}

output "vpc_id" {
  value       = aws_vpc.this.id
  description = "VPC ID"
}

output "public_subnets" {
  value       = [aws_subnet.public_a.id, aws_subnet.public_b.id]
  description = "Public subnet IDs"
}

output "ecr_repository_url" {
  description = "ECR repository URL to push images"
  value       = var.create_ecr ? aws_ecr_repository.this[0].repository_url : local.repository_url
}
