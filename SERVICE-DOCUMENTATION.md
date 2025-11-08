# 📋 ShopEase Services Documentation

## 🏗️ System Architecture

```
                    ┌─────────────────┐
                    │   API Gateway   │
                    │   (Port: 8765)  │
                    └─────────┬───────┘
                              │
        ┌─────────────────────┼─────────────────────┐
        │                     │                     │
┌───────▼───────┐    ┌────────▼────────┐    ┌──────▼──────┐
│ Discovery     │    │ Config Server   │    │ Security    │
│ Server :8761  │    │ :8888           │    │ Service     │
└───────────────┘    └─────────────────┘    │ :8086       │
                                            └─────────────┘
                              │
    ┌─────────────────────────┼─────────────────────────┐
    │                         │                         │
┌───▼───┐ ┌──▼──┐ ┌───▼───┐ ┌──▼──┐ ┌───▼───┐ ┌──▼──┐
│Product│ │Order│ │Payment│ │Notif│ │Customer│ │Redis│
│:8080  │ │:8081│ │:8082  │ │:8083│ │:8084   │ │:6379│
└───┬───┘ └──┬──┘ └───┬───┘ └──┬──┘ └───┬────┘ └─────┘
    │        │        │        │        │
    └────────┼────────┼────────┼────────┘
             │        │        │
        ┌────▼────────▼────────▼────┐
        │      MySQL Database       │
        │        (Port: 3306)       │
        └───────────────────────────┘
```

## 🎯 Service Responsibilities

### **🚪 API Gateway (Port: 8765)**
**Purpose**: Single entry point for all client requests
- **Responsibilities**:
  - Route requests to appropriate microservices
  - Load balancing across service instances
  - Authentication and authorization
  - Rate limiting and throttling
  - Request/response transformation
  - CORS handling

**Key Features**:
- Spring Cloud Gateway
- Service discovery integration
- Circuit breaker integration
- Request logging and monitoring

### **🔍 Discovery Server (Port: 8761)**
**Purpose**: Service registry for microservices
- **Responsibilities**:
  - Service registration and deregistration
  - Health monitoring of services
  - Service discovery for inter-service communication
  - Load balancing information

**Key Features**:
- Netflix Eureka Server
- Web dashboard for service monitoring
- Heartbeat mechanism
- Service metadata management

### **⚙️ Config Server (Port: 8888)**
**Purpose**: Centralized configuration management
- **Responsibilities**:
  - Store and serve configuration properties
  - Environment-specific configurations
  - Dynamic configuration updates
  - Configuration versioning

**Key Features**:
- Spring Cloud Config
- Git/File system backend
- Encryption support
- Refresh capabilities

### **🔐 Security Service (Port: 8086)**
**Purpose**: Authentication and authorization
- **Responsibilities**:
  - User authentication
  - JWT token generation and validation
  - Role-based access control
  - Security policy enforcement

**Key Features**:
- JWT token management
- Spring Security integration
- User credential validation
- Token refresh mechanism

### **🛍️ Product Service (Port: 8080)**
**Purpose**: Product catalog management
- **Responsibilities**:
  - Product CRUD operations
  - Inventory management
  - Product search and filtering
  - Price management
  - Category management

**Database**: `product_db`
**Key Features**:
- Redis caching for performance
- Full-text search capabilities
- Image and metadata storage
- Inventory tracking
- Price history

**API Endpoints**:
```
GET    /api/products           - Get all products
POST   /api/products           - Create new product
GET    /api/products/{id}      - Get product by ID
PUT    /api/products/{id}      - Update product
DELETE /api/products/{id}      - Delete product
GET    /api/products/search    - Search products
```

### **📦 Order Service (Port: 8081)**
**Purpose**: Order processing and management
- **Responsibilities**:
  - Order creation and management
  - Order status tracking
  - Inventory validation
  - Order history
  - Integration with payment service

**Database**: `order_db`
**Key Features**:
- Order lifecycle management
- Inventory checking via Product Service
- Payment integration
- Order status notifications
- Order cancellation handling

**API Endpoints**:
```
GET    /api/orders             - Get all orders
POST   /api/orders             - Create new order
GET    /api/orders/{id}        - Get order by ID
PUT    /api/orders/{id}/status - Update order status
DELETE /api/orders/{id}        - Cancel order
```

### **💳 Payment Service (Port: 8082)**
**Purpose**: Payment processing
- **Responsibilities**:
  - Payment processing
  - Payment method management
  - Transaction history
  - Refund processing
  - Payment gateway integration

**Database**: `payment_db`
**Key Features**:
- Multiple payment methods support
- Payment validation
- Transaction logging
- Fraud detection simulation
- Payment status tracking

**API Endpoints**:
```
POST   /api/payments           - Process payment
GET    /api/payments/{id}      - Get payment details
POST   /api/payments/{id}/refund - Process refund
GET    /api/payments/order/{orderId} - Get payments by order
```

### **👥 Customer Service (Port: 8084)**
**Purpose**: Customer management
- **Responsibilities**:
  - Customer registration and profiles
  - Customer authentication
  - Address management
  - Customer preferences
  - Customer support data

**Database**: `customer_db`
**Key Features**:
- Customer profile management
- Address book functionality
- Customer preferences
- Order history integration
- Customer support integration

**API Endpoints**:
```
GET    /api/customers          - Get all customers
POST   /api/customers          - Create new customer
GET    /api/customers/{id}     - Get customer by ID
PUT    /api/customers/{id}     - Update customer
DELETE /api/customers/{id}     - Delete customer
```

### **📧 Notification Service (Port: 8083)**
**Purpose**: Communication and notifications
- **Responsibilities**:
  - Email notifications
  - SMS notifications
  - Push notifications
  - Notification templates
  - Delivery tracking

**Database**: `notification_db`
**Key Features**:
- Multi-channel notifications
- Template management
- Delivery status tracking
- Event-driven notifications
- Notification preferences

**API Endpoints**:
```
POST   /api/notifications      - Send notification
GET    /api/notifications/{id} - Get notification status
GET    /api/notifications/customer/{id} - Get customer notifications
```

## 🗄️ Database Schema

### **Product Database (product_db)**
```sql
products (
  id BIGINT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  description TEXT,
  price DECIMAL(10,2) NOT NULL,
  quantity INT NOT NULL,
  category_id BIGINT,
  created_at TIMESTAMP,
  updated_at TIMESTAMP
)
```

### **Order Database (order_db)**
```sql
orders (
  id BIGINT PRIMARY KEY,
  customer_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  quantity INT NOT NULL,
  total_price DECIMAL(10,2),
  status VARCHAR(50),
  created_at TIMESTAMP,
  updated_at TIMESTAMP
)
```

### **Payment Database (payment_db)**
```sql
payments (
  id BIGINT PRIMARY KEY,
  order_id BIGINT NOT NULL,
  amount DECIMAL(10,2) NOT NULL,
  payment_mode VARCHAR(50),
  status VARCHAR(50),
  reference_number VARCHAR(100),
  payment_date TIMESTAMP,
  created_at TIMESTAMP
)
```

## 🔄 Service Communication Patterns

### **Synchronous Communication**
- **REST API calls** between services
- **Feign clients** for service-to-service communication
- **Circuit breakers** for resilience

### **Asynchronous Communication**
- **RabbitMQ** for event-driven messaging
- **Event publishing** for order status changes
- **Notification triggers** for customer updates

## 📊 Monitoring and Observability

### **Health Checks**
- Custom health indicators for each service
- Database connectivity checks
- External service dependency checks
- Redis connectivity validation

### **Metrics Collection**
- **Prometheus** metrics for all services
- Custom business metrics
- Performance counters
- Error rate monitoring

### **Logging**
- Structured JSON logging
- Distributed tracing with correlation IDs
- Centralized logging with ELK stack
- Audit logging for business operations

## 🚀 Deployment Strategy

### **Development Environment**
```bash
# Start basic services
docker compose up -d

# Start with monitoring
docker compose -f docker-compose-extended.yml up -d
```

### **Production Considerations**
- **Kubernetes deployment** with proper resource limits
- **Auto-scaling** based on CPU/memory usage
- **Blue-green deployment** for zero-downtime updates
- **Database clustering** for high availability
- **Redis clustering** for cache resilience

## 🔒 Security Implementation

### **Authentication Flow**
1. Client authenticates with Security Service
2. Receives JWT token
3. Includes token in subsequent API calls
4. API Gateway validates token
5. Routes to appropriate service

### **Authorization**
- Role-based access control (RBAC)
- Service-level permissions
- Resource-level authorization
- API rate limiting per user

This documentation provides a comprehensive overview of the ShopEase microservices platform, demonstrating enterprise-level architecture and implementation patterns.