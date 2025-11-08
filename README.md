# ShopEase Microservices Platform

A production-ready e-commerce microservices platform built with Spring Boot, Spring Cloud, and Docker.

## 🏗️ Architecture

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   API Gateway   │────│ Discovery Server │────│   Load Balancer │
│   (Port: 8765)  │    │   (Port: 8761)   │    │                 │
└─────────────────┘    └──────────────────┘    └─────────────────┘
         │
    ┌────┴────┬────────┬────────┬────────┬────────┐
    │         │        │        │        │        │
┌───▼───┐ ┌──▼──┐ ┌───▼───┐ ┌──▼──┐ ┌───▼───┐ ┌──▼──┐
│Product│ │Order│ │Payment│ │Notif│ │Customer│ │Redis│
│Service│ │Serv │ │Service│ │Serv │ │Service │ │Cache│
│:8080  │ │:8081│ │:8082  │ │:8083│ │:8084   │ │:6379│
└───────┘ └─────┘ └───────┘ └─────┘ └────────┘ └─────┘
    │         │        │        │        │
    └─────────┼────────┼────────┼────────┘
              │        │        │
         ┌────▼────────▼────────▼────┐
         │      MySQL Database       │
         │        (Port: 3306)       │
         └───────────────────────────┘
```

## 🚀 Services

| Service | Port | Database | Description |
|---------|------|----------|-------------|
| **API Gateway** | 8765 | - | Single entry point, routing, load balancing |
| **Discovery Server** | 8761 | - | Service registry (Eureka) |
| **Product Service** | 8080 | product_db | Product catalog management |
| **Order Service** | 8081 | order_db | Order processing & management |
| **Payment Service** | 8082 | payment_db | Payment processing |
| **Customer Service** | 8084 | customer_db | Customer management |
| **Notification Service** | 8083 | notification_db | Email/SMS notifications |

## 🛠️ Technology Stack

- **Backend**: Spring Boot 3.x, Spring Cloud
- **Database**: MySQL 8.0
- **Cache**: Redis
- **Service Discovery**: Netflix Eureka
- **API Gateway**: Spring Cloud Gateway
- **Monitoring**: Prometheus + Grafana
- **Containerization**: Docker & Docker Compose
- **Build Tool**: Maven

## 📋 Prerequisites

- Java 17+
- Docker & Docker Compose
- Maven 3.6+

## 🚀 Quick Start

### 1. Clone & Build
```bash
git clone <repository-url>
cd shop-ease
mvn clean package -DskipTests
```

### 2. Start Infrastructure
```bash
docker compose up -d
```

### 3. Verify Services
```bash
# Check all containers
docker compose ps

# Check service registry
curl http://localhost:8761

# Check API Gateway
curl http://localhost:8765/actuator/health
```

## 🔗 API Endpoints

### Via API Gateway (Recommended)
```bash
# Products
GET    http://localhost:8765/api/products
POST   http://localhost:8765/api/products
GET    http://localhost:8765/api/products/{id}

# Orders
GET    http://localhost:8765/api/orders
POST   http://localhost:8765/api/orders

# Customers
GET    http://localhost:8765/api/customers
POST   http://localhost:8765/api/customers

# Payments
POST   http://localhost:8765/api/payments
```

### Direct Service Access
```bash
# Product Service
curl http://localhost:8080/api/products

# Order Service  
curl http://localhost:8081/api/orders

# Discovery Server UI
http://localhost:8761
```

## 📊 Monitoring & Observability

### Start Monitoring Stack
```bash
cd monitoring
docker compose -f docker-compose-monitoring.yml up -d
```

### Access Dashboards
- **Prometheus**: http://localhost:9090
- **Grafana**: http://localhost:3000 (admin/admin)
- **Service Health**: http://localhost:8765/actuator/health

## 🔧 Development

### Add New Service
1. Create service directory
2. Add to parent `pom.xml`
3. Configure in `docker-compose.yml`
4. Add routes in API Gateway
5. Register with Eureka

### Local Development
```bash
# Start only infrastructure
docker compose up -d mysql redis discovery-server

# Run services locally
mvn spring-boot:run -pl product-service
mvn spring-boot:run -pl order-service
```

## 🏢 Production Features

✅ **API Gateway** - Single entry point, routing, load balancing  
✅ **Service Discovery** - Automatic service registration  
✅ **Database per Service** - Data isolation  
✅ **Caching** - Redis for performance  
✅ **Monitoring** - Prometheus + Grafana  
✅ **Health Checks** - Actuator endpoints  
✅ **Inter-Service Communication** - Feign clients  
✅ **Containerization** - Docker deployment  

## 🔄 Next Enhancements

- [ ] **Security**: OAuth2/JWT authentication
- [ ] **Resilience**: Circuit breaker pattern
- [ ] **Messaging**: RabbitMQ/Kafka integration
- [ ] **Testing**: Contract testing with Pact
- [ ] **CI/CD**: Jenkins/GitHub Actions pipeline
- [ ] **Kubernetes**: K8s deployment manifests
- [ ] **Distributed Tracing**: Zipkin/Jaeger

## 🐛 Troubleshooting

### Common Issues
```bash
# Service not registering with Eureka
docker logs discovery-server

# Database connection issues
docker logs mysql-container

# Check service logs
docker compose logs -f [service-name]
```

### Reset Environment
```bash
docker compose down -v
docker system prune -f
mvn clean package -DskipTests
docker compose up -d
```

## 📝 Sample API Calls

### Create Product
```bash
curl -X POST http://localhost:8765/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop",
    "description": "Gaming Laptop",
    "price": 999.99,
    "quantity": 10
  }'
```

### Create Order
```bash
curl -X POST http://localhost:8765/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "productId": 1,
    "quantity": 2
  }'
```

This microservices platform demonstrates enterprise-level patterns and is ready for production deployment with proper monitoring, caching, and service communication.