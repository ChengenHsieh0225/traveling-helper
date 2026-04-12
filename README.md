```mermaid
graph TD
    User((User)) -->|React PWA| Frontend[Frontend]
    Frontend -->|REST API| Gateway[API Gateway / Nginx]

    subgraph "Java Spring Boot Microservices (DDD)"
        subgraph "Planning Service"
            PC[Planning Controller] --> PAS[Planning App Service]
            PAS --> PR[TravelPlan Repository]
            PAS -->|Publish Event| MQ{RabbitMQ}
        end

        subgraph "Social Service"
            MQ --> PSL[PlanSyncListener]
            PSL --> SS[Sync Service]
            SS --> SR[SharedPlan Repository]
            SC[SharedPlan Controller]
        end

        subgraph "Auth Service"
            AC[Auth Controller] --> UR[User Repository]
        end
    end

    subgraph "Python FastAPI External Support Services"
        Gateway --> Weather[Weather Service]
        subgraph "Weather Logic (CoR Pattern)"
            Weather --> RH[RedisHandler]
            RH -->|Cache Miss| OMH[OpenMeteoHandler]
            OMH -->|Failover| OWH[OpenWeatherHandler]
        end
        Gateway --> Currency[Currency Service]
        Gateway --> News[News Service]
    end

    %% Storage & Infrastructure
    PR --> DB[(MySQL DB)]
    SR --> DB
    UR --> DB
    RH --> Redis[(Redis)]
    OMC[OpenMeteo API] <-.- OMH
    OWC[OpenWeather API] <-.- OWH
```

## Features & Technical Highlights

### 1. Polyglot Microservices Architecture
- **Java / Spring Boot**: Handles core business logic (Planning, Social, Auth) with a focus on type safety and maintainability.
- **Python / FastAPI**: Manages external data aggregation (Weather, Currency, News) leveraging Python's rich ecosystem for third-party API integration.

### 2. Domain-Driven Design (DDD) Implementation
- Strictly separated into **Domain**, **Application**, and **Infrastructure** layers.
- Encapsulated business logic within Entities and Value Objects to ensure high cohesion and low coupling.

### 3. Resilience & Design Patterns
- **Chain of Responsibility (CoR)**: Implemented in the Weather service for seamless failover between multiple providers (Redis -> Open-Meteo -> OpenWeather).
- **Event-Driven Architecture (EDA)**: Utilizes **RabbitMQ** to decouple the `Planning` and `Social` services, ensuring eventual consistency when plans are published.
- **Caching**: Distributed caching with **Redis** to reduce external API latency and manage rate limits.

### 4. Security & DevOps
- **JWT & RSA Encryption**: Secure authentication using asymmetric key pairs for token signing.
- **Containerization**: Fully Dockerized environment with `docker-compose` for local development and AWS-ready deployment.

---

## Project Evolution Roadmap

- [x] **Phase 1: Infrastructure & Resilience**: Established Python/FastAPI services with Chain of Responsibility (CoR) pattern, Redis caching, and PostgreSQL integration (v3-v6).
- [x] **Phase 2: Mobile-First Experience**: Migrated to React and implemented PWA support with optimized data fetching using `useQuery` and `asyncio` (v4-v5).
- [x] **Phase 3: Core Domain Expansion**: Architected `Auth`, `Planning`, and `Social` services from scratch using **Spring Boot + DDD** to manage complex business logic (v7).
- [x] **Phase 4: Service Orchestration**: Integrated **RabbitMQ** for asynchronous event-driven synchronization between Java microservices (v7).
- [ ] **Phase 5: Cloud Infrastructure**: (In Progress) Deploying core services, MySQL, and RabbitMQ to **AWS ECS** with RDS for production-grade reliability.
- [ ] **Phase 6: Advanced Domain Logic**: Enhancing interactive logic between `Planning`, `Social`, and `Auth` services to handle complex travel collaboration scenarios.
- [ ] **Phase 7: Full-Stack Integration**: Developing dedicated React modules for core domains and connecting the PWA frontend to Spring Boot services via API Gateway.

---

## Live Demo & Deployment

- Frontend (PWA): https://traveling-helper.vercel.app/currency (Powered by Vercel).
- Utility Services (FastAPI): https://traveling-helper.onrender.com (Powered by Render).
- Core Microservices (Java): [Ongoing] (Deploying to AWS ECS).

---

## Getting Started (Core Microservices)

### Prerequisites
- Docker & Docker Compose
- Java 21+

### 1. Clone the repository
```bash
# Git Clone
git clone https://github.com/ChengenHsieh0225/traveling-helper.git

# Move to the root directory of the core microservices
cd traveling-helper/backend-java

```

### 2. Environment Configuration
Please set up your environment variables before starting:
```bash
# Copy the example environment file
cp .env.example .env

# Open .env and fill in your credentials
# (You can use any text editor like vim, nano, or VS Code)
vim .env
```

### 3. Security Setup (Required)
The `auth-service` requires Google OAuth credentials and RSA keys for JWT signing.

#### A. Google Auth
Obtain `CLIENT_ID` and `CLIENT_SECRET` from the Google Cloud Console. 

#### B. RSA Key Generation
Generate the asymmetric keys required for secure token handling:
```bash
# 1. Generate private key
openssl genrsa -out private_key.pem 2048

# 2. Convert to PKCS#8 format (.der) for Java compatibility
openssl pkcs8 -topk8 -inform PEM -outform DER -in private_key.pem -out private_key.der -nocrypt

# 3. Export public key (.pem)
openssl rsa -in private_key.pem -pubout -out public_key.pem
```

#### C. Key Placement
Ensure the keys are placed in the following directory for each service:
- auth-service: `src/main/resources/certs/` (requires `private_key.der` & `public_key.pem`)
- planning-service: `src/main/resources/certs/` (requires `public_key.pem`)
- social-service: `src/main/resources/certs/` (requires `public_key.pem`)

### 4. Running with Docker
This will orchestrate the core ecosystem, including Auth, Planning, and Social services, along with RabbitMQ and MySQL:
```bash
# Start all core services in detached mode
docker-compose up -d
```

### 5. Verification
Once the containers are up and healthy, you can access the service APIs at:
- Auth Service: http://localhost:8081
- Planning Service: http://localhost:8082
- Social Service: http://localhost:8083
