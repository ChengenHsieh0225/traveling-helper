module "vpc" {
  source = "terraform-aws-modules/vpc/aws"
  version = "~> 5.0"

  name = "traveling-helper-vpc"
  cidr = "10.0.0.0/16"

  # Available zones & Subnets
  azs = ["ap-east-2a", "ap-east-2b", "ap-east-2c"]
  private_subnets = ["10.0.1.0/24", "10.0.2.0/24", "10.0.3.0/24"]
  public_subnets = ["10.0.101.0/24", "10.0.102.0/24", "10.0.103.0/24"]
  create_database_subnet_group = true
  database_subnets = ["10.0.21.0/24", "10.0.22.0/24", "10.0.23.0/24"]

  # NAT Gateway
  enable_nat_gateway = true
  single_nat_gateway = true

  # Required DNS settings for EKS
  enable_dns_hostnames = true
  enable_dns_support = true

  public_subnet_tags = {
    "kubernetes.io/role/elb" = "1"
    "kubernetes.io/cluster/traveling-helper-eks"  = "shared"
  }

  private_subnet_tags = {
    "kubernetes.io/role/internal-elb" = "1"
    "kubernetes.io/cluster/traveling-helper-eks"  = "shared"
  }
}