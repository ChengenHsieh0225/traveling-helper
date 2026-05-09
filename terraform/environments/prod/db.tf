# Security Group
resource "aws_security_group" "rds_sg" {
  name = "traveling-helper-rds-sg"
  description = "Allow MySQL traffic from within the VPC"
  vpc_id = module.vpc.vpc_id

  # Inbound rule: Allow 3306 from the entire VPC CIDR block
  ingress {
    from_port = 3306
    to_port = 3306
    protocol = "tcp"
    cidr_blocks = [module.vpc.vpc_cidr_block]
  }

  # Outbound rule: Allow all traffic out
  egress {
    from_port = 0
    to_port = 0
    protocol = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "traveling-helper-rds-sg"
  }
}

# RDS instance
module "db" {
  source = "terraform-aws-modules/rds/aws"
  version = "~> 6.0"

  identifier = "traveling-helper-db"
  
  engine = "mysql"
  engine_version = "8.0"
  family = "mysql8.0"
  major_engine_version = "8.0"
  instance_class = "db.t4g.micro" # Free tier eligible instance type
  allocated_storage = 20 # Storage size in GB

  db_name = "traveling_helper"
  username = "admin"
  port = "3306"

  # Automatic password management via AWS Secrets Manager
  manage_master_user_password = true

  # Network configuration
  # Place the DB in the dedicated database subnets created in vpc.tf
  db_subnet_group_name = module.vpc.database_subnet_group_name
  vpc_security_group_ids = [aws_security_group.rds_sg.id]

  # Skip final snapshot and disable deletion protection for easy cleanup
  skip_final_snapshot = true
  deletion_protection = false

  tags = {
    Project = "traveling-helper"
  }
}