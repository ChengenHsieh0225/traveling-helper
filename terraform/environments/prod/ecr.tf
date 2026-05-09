locals {
  services = [
    "auth-service",
    "planning-service",
    "social-service"
  ]
}

# ECR Repositories for Java Backend
resource "aws_ecr_repository" "app_repos" {
  for_each = toset(local.services)

  name = "traveling-helper-${each.key}"
  image_tag_mutability = "MUTABLE"

  # Scan for vulnerabilities on push
  image_scanning_configuration {
    scan_on_push = true
  }
}

# TODO: ECR Repositories for Python Backend