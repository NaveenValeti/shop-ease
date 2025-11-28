@echo off
echo ========================================
echo ShopEase E2E Testing Script
echo ========================================

echo.
echo Step 1: Testing Authentication...
curl -X POST http://localhost:8765/api/auth/login -H "Content-Type: application/json" -d "{\"username\":\"admin\",\"password\":\"password\"}"

echo.
echo.
echo Step 2: Creating Products...
curl -X POST http://localhost:8765/api/products -H "Content-Type: application/json" -d "{\"name\":\"MacBook Pro\",\"description\":\"Apple laptop\",\"price\":2499.99,\"quantity\":10}"
curl -X POST http://localhost:8765/api/products -H "Content-Type: application/json" -d "{\"name\":\"iPhone 15\",\"description\":\"Latest iPhone\",\"price\":1199.99,\"quantity\":15}"
curl -X POST http://localhost:8765/api/products -H "Content-Type: application/json" -d "{\"name\":\"iPad Air\",\"description\":\"Apple tablet\",\"price\":799.99,\"quantity\":20}"

echo.
echo.
echo Step 3: Creating Customers...
curl -X POST http://localhost:8765/api/customers -H "Content-Type: application/json" -d "{\"name\":\"John Doe\",\"email\":\"john@example.com\",\"phone\":\"1234567890\"}"
curl -X POST http://localhost:8765/api/customers -H "Content-Type: application/json" -d "{\"name\":\"Jane Smith\",\"email\":\"jane@example.com\",\"phone\":\"0987654321\"}"

echo.
echo.
echo Step 4: Creating Orders...
curl -X POST http://localhost:8765/api/orders -H "Content-Type: application/json" -d "{\"customerId\":1,\"productId\":1,\"quantity\":1}"
curl -X POST http://localhost:8765/api/orders -H "Content-Type: application/json" -d "{\"customerId\":2,\"productId\":2,\"quantity\":2}"

echo.
echo.
echo Step 5: Processing Payments...
curl -X POST http://localhost:8765/api/payments -H "Content-Type: application/json" -d "{\"orderId\":1,\"amount\":2499.99,\"paymentMode\":\"CREDIT_CARD\"}"
curl -X POST http://localhost:8765/api/payments -H "Content-Type: application/json" -d "{\"orderId\":2,\"amount\":2399.98,\"paymentMode\":\"DEBIT_CARD\"}"

echo.
echo.
echo Step 6: Sending Notifications...
curl -X POST http://localhost:8765/api/notifications -H "Content-Type: application/json" -d "{\"customerId\":1,\"message\":\"Your MacBook Pro order has been confirmed!\",\"type\":\"EMAIL\"}"
curl -X POST http://localhost:8765/api/notifications -H "Content-Type: application/json" -d "{\"customerId\":2,\"message\":\"Your iPhone 15 order has been shipped!\",\"type\":\"SMS\"}"

echo.
echo.
echo Step 7: Verifying Data...
echo Getting all products:
curl http://localhost:8765/api/products

echo.
echo Getting all customers:
curl http://localhost:8765/api/customers

echo.
echo Getting all orders:
curl http://localhost:8765/api/orders

echo.
echo Getting all payments:
curl http://localhost:8765/api/payments

echo.
echo Getting all notifications:
curl http://localhost:8765/api/notifications

echo.
echo.
echo ========================================
echo E2E Testing Complete!
echo ========================================
pause