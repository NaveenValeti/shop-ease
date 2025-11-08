# 🚀 ShopEase Complete Deployment Guide

## 📋 Prerequisites

### **System Requirements**
- Java 17+
- Maven 3.6+
- Docker & Docker Compose
- Git
- 8GB RAM minimum
- 20GB free disk space

### **Verify Prerequisites**
```bash
# Check Java version
java -version

# Check Maven version
mvn -version

# Check Docker version
docker --version
docker compose version

# Check available memory
docker system info | grep "Total Memory"
```

## 🏗️ Step-by-Step Deployment

### **Step 1: Clone and Setup**
```bash
# Clone repository
git clone <repository-url>
cd shop-ease

# Verify project structure
ls -la
```

### **Step 2: Maven Compilation**
```bash
# Clean previous builds
mvn clean

# Compile all services
mvn compile

# Expected output: BUILD SUCCESS for all 8 modules
# - product-service
# - order-service  
# - payment-service
# - notification-service
# - customer-service
# - api-gateway
# - discovery-server
# - security-service
```

### **Step 3: Package Applications**
```bash
# Package all services (skip tests for faster build)
mvn clean package -DskipTests

# This creates JAR files in each service's target/ directory
# Verify JAR files created:
ls -la */target/*.jar
```

### **Step 4: Docker Build**
```bash
# Build all Docker images
docker compose build

# Verify images created
docker images | grep shop-ease

# Expected images:
# - shop-ease-api-gateway
# - shop-ease-discovery-server
# - shop-ease-config-server
# - shop-ease-security-service
# - shop-ease-product-service
# - shop-ease-order-service
# - shop-ease-payment-service
# - shop-ease-customer-service
# - shop-ease-notification-service
```

### **Step 5: Start Infrastructure Services**
```bash
# Start databases and supporting services first
docker compose up -d product-mysql order-mysql payment-mysql customer-mysql notification-mysql redis-container rabbitmq-container

# Wait for databases to initialize (30-60 seconds)
sleep 60

# Check database containers
docker compose ps | grep mysql
```

### **Step 6: Start Core Services**
```bash
# Start configuration and discovery services
docker compose up -d config-server discovery-server

# Wait for services to start
sleep 30

# Verify core services
curl http://localhost:8888/actuator/health  # Config Server
curl http://localhost:8761                  # Discovery Server
```

### **Step 7: Start Business Services**
```bash
# Start all business services
docker compose up -d product-service order-service payment-service customer-service notification-service

# Wait for services to register
sleep 45

# Start API Gateway and Security Service
docker compose up -d api-gateway security-service

# Final wait for complete startup
sleep 30
```

### **Step 8: Verify Deployment**
```bash
# Check all containers are running
docker compose ps

# Expected: 16 containers running
# - 5 MySQL databases
# - 1 Redis
# - 1 RabbitMQ  
# - 1 Config Server
# - 1 Discovery Server
# - 1 API Gateway
# - 1 Security Service
# - 5 Business services
```

## 🧪 Testing Deployment

### **Health Checks**
```bash
# Test core services
curl http://localhost:8765/actuator/health  # API Gateway
curl http://localhost:8761                  # Discovery Server
curl http://localhost:8080/actuator/health  # Product Service

# Check service registration
curl http://localhost:8761/eureka/apps
```

### **Functional Testing**
```bash
# Test product creation via API Gateway
curl -X POST http://localhost:8765/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Laptop",
    "description": "Gaming laptop for testing",
    "price": 1299.99,
    "quantity": 10
  }'

# Expected response: {"id":1,"name":"Test Laptop",...}
```

### **Run Automated Tests**
```bash
# Execute test script
./test-scripts/test-e2e.bat

# Or run individual tests
curl -X POST http://localhost:8765/api/products -H "Content-Type: application/json" -d '{"name":"MacBook Pro","description":"Apple laptop","price":2499.99,"quantity":5}'
curl -X POST http://localhost:8765/api/products -H "Content-Type: application/json" -d '{"name":"iPhone 15","description":"Latest iPhone","price":1199.99,"quantity":8}'
```

## 🔧 Service-by-Service Verification

### **1. Discovery Server (Port 8761)**
```bash
# Check Eureka dashboard
curl http://localhost:8761

# Verify registered services
curl http://localhost:8761/eureka/apps | grep -i "application"
```

### **2. API Gateway (Port 8765)**
```bash
# Health check
curl http://localhost:8765/actuator/health

# Check routes
curl http://localhost:8765/actuator/gateway/routes
```

### **3. Product Service (Port 8080)**
```bash
# Health check
curl http://localhost:8080/actuator/health

# CRUD operations
curl -X POST http://localhost:8080/api/products -H "Content-Type: application/json" -d '{"name":"Direct Product","description":"Created directly","price":99.99,"quantity":5}'
curl http://localhost:8080/api/products
curl http://localhost:8080/api/products/1
```

### **4. Order Service (Port 8081)**
```bash
# Test order creation
curl -X POST http://localhost:8081/api/orders -H "Content-Type: application/json" -d '{"productId":1,"quantity":2}'

# Check order status
curl http://localhost:8081/api/orders
```

### **5. Payment Service (Port 8082)**
```bash
# Test payment processing
curl -X POST http://localhost:8082/api/payments -H "Content-Type: application/json" -d '{"orderId":1,"amount":199.98,"paymentMode":"CREDIT_CARD"}'
```

## 🐛 Troubleshooting

### **Common Issues and Solutions**

#### **Services Not Starting**
```bash
# Check logs
docker logs [service-name] --tail 20

# Common fixes:
docker compose restart [service-name]
docker compose down && docker compose up -d
```

#### **Database Connection Issues**
```bash
# Check MySQL containers
docker logs product-mysql --tail 10

# Reset databases if needed
docker compose down -v
docker compose up -d
```

#### **Service Discovery Issues**
```bash
# Restart discovery server
docker compose restart discovery-server

# Check service registration
curl http://localhost:8761/eureka/apps
```

#### **Memory Issues**
```bash
# Check Docker memory usage
docker stats

# Increase Docker memory limit if needed
# Docker Desktop: Settings > Resources > Memory
```

### **Port Conflicts**
```bash
# Check port usage
netstat -an | grep :8765
netstat -an | grep :8080

# Kill processes using ports if needed
taskkill /F /PID [process-id]  # Windows
kill -9 [process-id]           # Linux/Mac
```

## 📊 Monitoring Deployment

### **Container Status**
```bash
# Check all containers
docker compose ps

# Check resource usage
docker stats

# Check logs
docker compose logs -f [service-name]
```

### **Service Health**
```bash
# Health check script
echo "=== Service Health Check ==="
echo "API Gateway: $(curl -s -o /dev/null -w '%{http_code}' http://localhost:8765/actuator/health)"
echo "Discovery Server: $(curl -s -o /dev/null -w '%{http_code}' http://localhost:8761)"
echo "Product Service: $(curl -s -o /dev/null -w '%{http_code}' http://localhost:8080/actuator/health)"
echo "Order Service: $(curl -s -o /dev/null -w '%{http_code}' http://localhost:8081/actuator/health)"
echo "Payment Service: $(curl -s -o /dev/null -w '%{http_code}' http://localhost:8082/actuator/health)"
```

## 🎯 Production Deployment

### **Environment Variables**
```bash
# Set production environment
export SPRING_PROFILES_ACTIVE=prod
export DATABASE_URL=jdbc:mysql://prod-mysql:3306/
export REDIS_URL=redis://prod-redis:6379
```

### **Docker Compose Override**
```yaml
# docker-compose.prod.yml
version: '3.8'
services:
  product-service:
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - JVM_OPTS=-Xmx512m -Xms256m
    deploy:
      replicas: 2
      resources:
        limits:
          memory: 1G
        reservations:
          memory: 512M
```

### **Kubernetes Deployment**
```bash
# Apply Kubernetes manifests
kubectl apply -f k8s/

# Check deployment status
kubectl get pods
kubectl get services
```

## ✅ Deployment Checklist

### **Pre-Deployment**
- [ ] Java 17+ installed
- [ ] Maven 3.6+ installed
- [ ] Docker & Docker Compose installed
- [ ] Sufficient system resources (8GB RAM, 20GB disk)
- [ ] All ports available (8080-8086, 8765, 8761, 8888, 3306-3310, 6379, 5672)

### **Build Phase**
- [ ] `mvn clean` successful
- [ ] `mvn compile` successful for all modules
- [ ] `mvn clean package -DskipTests` successful
- [ ] All JAR files created in target directories
- [ ] `docker compose build` successful
- [ ] All Docker images created

### **Deployment Phase**
- [ ] Infrastructure services started (MySQL, Redis, RabbitMQ)
- [ ] Core services started (Config Server, Discovery Server)
- [ ] Business services started and registered
- [ ] API Gateway and Security Service started
- [ ] All 16 containers running

### **Verification Phase**
- [ ] Health checks passing
- [ ] Service discovery working
- [ ] API Gateway routing functional
- [ ] Product CRUD operations working
- [ ] Database per service architecture verified
- [ ] Automated tests passing

### **Post-Deployment**
- [ ] Monitoring setup configured
- [ ] Log aggregation working
- [ ] Backup procedures in place
- [ ] Documentation updated
- [ ] Team notified of deployment

## 🚀 Quick Start Commands

```bash
# Complete deployment in one go
git clone <repository-url>
cd shop-ease
mvn clean package -DskipTests
docker compose up -d
sleep 120  # Wait for all services to start

# Verify deployment
curl -X POST http://localhost:8765/api/products -H "Content-Type: application/json" -d '{"name":"Quick Test","description":"Deployment test","price":99.99,"quantity":1}'

# Run tests
./test-scripts/test-e2e.bat
```

This guide ensures a successful deployment of the ShopEase microservices platform from compilation to full testing.