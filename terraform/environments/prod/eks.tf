module eks {
  source = "terraform-aws-modules/eks/aws"
  version = "~> 20.0"

  cluster_name = "traveling-helper-eks"
  cluster_version = "1.35"

  # Enable public access to API server for local kubectl management
  cluster_endpoint_public_access = true

  vpc_id = module.vpc.vpc_id
  subnet_ids = module.vpc.private_subnets

  # EKS Managed Node Group configuration
  eks_managed_node_groups = {
    main = {
      # t3.small provides 2 vCPUs and 2GB RAM
      instance_types = ["t3.small"]

      min_size = 1
      max_size = 3
      desired_size = 2
    }
  }

  # Ensure VPC and Subnet Group are ready before EKS creation
  depends_on = [module.vpc]

  # Grant the current IAM entity admin permissions to the cluster
  # (Simplified access management for personal side project)
  enable_cluster_creator_admin_permissions = true

  tags = {
    Project = "traveling-helper"
  }
}