# --- IAM Role for EBS CSI Driver ---
# This explicitly creates the role to ensure permissions are granted.

module "ebs_csi_irsa_role" {
  source = "terraform-aws-modules/iam/aws//modules/iam-role-for-service-accounts-eks"
  version = "~> 5.0"

  role_name = "traveling-helper-ebs-csi-role"
  attach_ebs_csi_policy = true

  oidc_providers = {
    ex = {
      provider_arn = module.eks.oidc_provider_arn
      namespace_service_accounts = ["kube-system:ebs-csi-controller-sa"]
    }
  }
}

# --- EKS Add-on Configuration ---
resource "aws_eks_addon" "ebs_csi" {
  cluster_name = module.eks.cluster_name
  addon_name = "aws-ebs-csi-driver"
  addon_version = "v1.59.0-eksbuild.1" # Standard for EKS 1.35
  service_account_role_arn = module.ebs_csi_irsa_role.iam_role_arn

  # Force overwrite if there's a conflict from previous failed attempts
  resolve_conflicts_on_create = "OVERWRITE"
  resolve_conflicts_on_update = "OVERWRITE"
}