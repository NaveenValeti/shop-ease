@echo off
echo ========================================
echo ShopEase Normal E2E Test
echo ========================================

echo.
echo Step 1: Building services...
mvn clean package -DskipTests

echo.
echo Step 2: Starting all services...
docker compose up -d

echo.
echo Waiting for services to start (3 minutes)...
timeout /t 180

echo.
echo Step 3: Check service status...
docker compose ps

echo.
echo Step 4: Check Discovery Server...
curl http://localhost:8761

echo.
echo.
echo Step 5: Check API Gateway...
curl "http://localhost:8765/actuator/health"

echo.
echo.
echo Step 6: Create Products...
curl -X POST http://localhost:8765/api/products -H "Content-Type: application/json" -d "{\"name\":\"MacBook Pro\",\"description\":\"Apple laptop\",\"price\":2499.99,\"quantity\":10}"
curl -X POST http://localhost:8765/api/products -H "Content-Type: application/json" -d "{\"name\":\"iPhone 15\",\"description\":\"Latest iPhone\",\"price\":1199.99,\"quantity\":15}"

echo.
echo.
echo Step 7: Get Products...
curl http://localhost:8765/api/products

echo.
echo.
echo Step 8: Create Customers...
curl -X POST http://localhost:8765/api/customers -H "Content-Type: application/json" -d "{\"name\":\"John Doe\",\"email\":\"john@example.com\",\"phone\":\"1234567890\"}"

echo.
echo.
echo Step 9: Create Orders...
curl -X POST http://localhost:8765/api/orders -H "Content-Type: application/json" -d "{\"customerId\":1,\"productId\":1,\"quantity\":1}"

echo.
echo.
echo Step 10: Process Payment...
curl -X POST http://localhost:8765/api/payments -H "Content-Type: application/json" -d "{\"orderId\":1,\"amount\":2499.99,\"paymentMode\":\"CREDIT_CARD\"}"

echo.
echo.
echo Step 11: Send Notification...
curl -X POST http://localhost:8765/api/notifications -H "Content-Type: application/json" -d "{\"customerId\":1,\"message\":\"Order confirmed!\",\"type\":\"EMAIL\"}"

echo.
echo.
echo Step 12: Verify all data...
echo Products:
curl http://localhost:8765/api/products
echo.
echo Customers:
curl http://localhost:8765/api/customers
echo.
echo Orders:
curl http://localhost:8765/api/orders
echo.
echo Payments:
curl http://localhost:8765/api/payments
echo.
echo Notifications:
curl http://localhost:8765/api/notifications

echo.
echo.
echo ========================================
echo Normal E2E Test Complete!
echo ========================================
echo.
echo Check Kibana: http://localhost:5601
echo Check Zipkin: http://localhost:9411
echo Check RabbitMQ: http://localhost:15672
echo.
pause