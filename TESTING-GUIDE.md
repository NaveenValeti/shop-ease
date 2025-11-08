# 🧪 ShopEase End-to-End Testing Guide

## 🏗️ Service Architecture Overview

### **Core Services & Current Status**

| Service | Port | Status | Database | Key Features |
|---------|------|--------|----------|--------------|
| **API Gateway** | 8765 | ✅ WORKING | - | Request routing, load balancing |
| **Discovery Server** | 8761 | ✅ WORKING | - | Service discovery, health monitoring |
| **Config Server** | 8888 | ✅ WORKING | - | External config management |
| **Product Service** | 8080 | ✅ WORKING | product-mysql:3306 | CRUD operations, caching |
| **Order Service** | 8081 | ✅ WORKING | order-mysql:3307 | Order processing |
| **Payment Service** | 8082 | ⚠️ STARTING | payment-mysql:3308 | Payment processing |
| **Customer Service** | 8084 | ⚠️ STARTING | customer-mysql:3309 | Customer management |
| **Notification Service** | 8083 | ⚠️ STARTING | notification-mysql:3310 | Notifications |
| **Security Service** | 8086 | ✅ WORKING | - | JWT authentication |

### **Infrastructure Services**

| Service | Port | Status | Purpose |
|---------|------|--------|---------|
| **MySQL DBs** | 3306-3310 | ✅ WORKING | Database per service |
| **Redis** | 6379 | ✅ WORKING | Caching layer |
| **RabbitMQ** | 5672/15672 | ✅ WORKING | Message broker |

## 🚀 Quick Start Testing

### **1. Start the Platform**
```bash
# Start all services
docker compose up -d

# Check all containers
docker compose ps
```

### **2. Verify Core Services**
```bash
# API Gateway health
curl http://localhost:8765/actuator/health

# Discovery Server
curl http://localhost:8761

# Product Service health
curl http://localhost:8080/actuator/health
```

## 🧪 Working End-to-End Tests

### **Scenario 1: Product Management (FULLY WORKING)**

#### **Create Products via API Gateway**
```bash
# Create Product 1
curl -X POST http://localhost:8765/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Gaming Laptop",
    "description": "High-performance gaming laptop",
    "price": 1299.99,
    "quantity": 10
  }'

# Expected Response: {"id":1,"name":"Gaming Laptop","description":"High-performance gaming laptop","price":1299.99,"quantity":10}

# Create Product 2
curl -X POST http://localhost:8765/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "MacBook Pro",
    "description": "Apple MacBook Pro 16-inch",
    "price": 2499.99,
    "quantity": 5
  }'

# Create Product 3
curl -X POST http://localhost:8765/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "iPhone 15 Pro",
    "description": "Latest iPhone with titanium design",
    "price": 1199.99,
    "quantity": 8
  }'
```

#### **Direct Product Service Testing**
```bash
# Create product directly
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Dell XPS 13",
    "description": "Ultrabook laptop",
    "price": 1399.99,
    "quantity": 5
  }'

# Get product by ID
curl http://localhost:8080/api/products/1

# Get all products
curl http://localhost:8080/api/products
```

### **Scenario 2: Service Discovery Testing**

#### **Check Eureka Dashboard**
```bash
# View registered services
curl http://localhost:8761

# Check service registration via API
curl http://localhost:8761/eureka/apps
```

### **Scenario 3: Order Processing (PARTIAL)**

#### **Test Order Creation**
```bash
# Create order via API Gateway
curl -X POST http://localhost:8765/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "productId": 1,
    "quantity": 2
  }'

# Note: May return 500 error due to service communication issues
```

#### **Direct Order Service Testing**
```bash
# Test order service directly
curl -X POST http://localhost:8081/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "productId": 1,
    "quantity": 2
  }'
```

## 🔧 Service-Specific Testing

### **Product Service (FULLY FUNCTIONAL)**
```bash
# Health check
curl http://localhost:8080/actuator/health

# Create product
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Test Product","description":"Test","price":99.99,"quantity":10}'

# Get all products
curl http://localhost:8080/api/products

# Get specific product
curl http://localhost:8080/api/products/1

# Update product
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Updated Product","description":"Updated","price":199.99,"quantity":5}'

# Delete product
curl -X DELETE http://localhost:8080/api/products/1
```

### **API Gateway (FULLY FUNCTIONAL)**
```bash
# Health check
curl http://localhost:8765/actuator/health

# Route to product service
curl -X POST http://localhost:8765/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Via Gateway","description":"Test via gateway","price":299.99,"quantity":3}'

# Check gateway routes
curl http://localhost:8765/actuator/gateway/routes
```

### **Discovery Server (FULLY FUNCTIONAL)**
```bash
# Eureka dashboard
curl http://localhost:8761

# Service instances
curl http://localhost:8761/eureka/apps

# Specific service info
curl http://localhost:8761/eureka/apps/PRODUCT-SERVICE
```

## 🐛 Troubleshooting Guide

### **Common Issues & Solutions**

#### **Service Not Starting**
```bash
# Check logs
docker logs [service-name]

# Check specific service logs
docker logs product-service --tail 20
docker logs order-service --tail 20
docker logs payment-service --tail 20
```

#### **Database Connection Issues**
```bash
# Check MySQL containers
docker logs product-mysql --tail 10
docker logs order-mysql --tail 10

# Connect to database
docker exec -it product-mysql mysql -u root -p
```

#### **Service Discovery Issues**
```bash
# Check if services are registered
curl http://localhost:8761/eureka/apps | grep -i "product\|order\|payment"

# Restart discovery server
docker compose restart discovery-server
```

#### **API Gateway Routing Issues**
```bash
# Test direct service access
curl http://localhost:8080/api/products  # Direct
curl http://localhost:8765/api/products  # Via Gateway

# Check gateway configuration
docker logs api-gateway --tail 20
```

## 📊 Current System Status

### **✅ Working Services**
- API Gateway (8765) - Request routing working
- Discovery Server (8761) - Service registry active
- Config Server (8888) - Configuration management
- Security Service (8086) - JWT authentication
- Product Service (8080) - Full CRUD operations
- Order Service (8081) - Basic functionality
- All MySQL databases (3306-3310)
- Redis (6379) - Caching
- RabbitMQ (5672) - Messaging

### **⚠️ Services Starting**
- Payment Service (8082) - Container running, service initializing
- Customer Service (8084) - Container running, service initializing  
- Notification Service (8083) - Container running, service initializing

### **🎯 Verified Functionality**
```bash
# Product creation via API Gateway - WORKING
curl -X POST http://localhost:8765/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Test Laptop","description":"Gaming laptop","price":999.99,"quantity":10}'

# Response: {"id":X,"name":"Test Laptop","description":"Gaming laptop","price":999.99,"quantity":10}
```

## 📈 Performance Testing

### **Load Testing Product Service**
```bash
# Simple load test
for i in {1..10}; do
  curl -X POST http://localhost:8765/api/products \
    -H "Content-Type: application/json" \
    -d "{\"name\":\"Product $i\",\"description\":\"Test product $i\",\"price\":$((i*100)).99,\"quantity\":$i}" &
done
wait
```

### **Cache Performance Test**
```bash
# First request (cache miss)
time curl http://localhost:8080/api/products/1

# Second request (cache hit - should be faster)
time curl http://localhost:8080/api/products/1
```

## 🎯 Testing Checklist

### **✅ Completed Tests**
- [x] API Gateway routing to Product Service
- [x] Product CRUD operations via Gateway
- [x] Service Discovery registration
- [x] Database per service architecture
- [x] Docker containerization
- [x] Health checks
- [x] Configuration management

### **⚠️ Partial Tests**
- [x] Order Service basic functionality
- [ ] Payment processing
- [ ] Customer management
- [ ] Notification system
- [ ] Inter-service communication
- [ ] Circuit breaker patterns

### **🔄 Next Steps**
1. Complete payment service initialization
2. Test customer service functionality
3. Verify notification service
4. Test complete e-commerce flow
5. Load testing across all services

This testing guide reflects the current state of the ShopEase platform with working core services and proper microservices architecture.