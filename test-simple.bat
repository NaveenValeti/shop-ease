@echo off
echo ========================================
echo Simple E2E Test
echo ========================================

echo.
echo Checking Discovery Server...
curl "http://localhost:8761"

echo.
echo.
echo Checking API Gateway Health...
curl "http://localhost:8765/actuator/health"

echo.
echo.
echo Checking Product Service Health...
curl "http://localhost:8080/actuator/health"

echo.
echo.
echo Creating a Product...
curl -X POST "http://localhost:8765/api/products" -H "Content-Type: application/json" -d "{\"name\":\"Test Product\",\"description\":\"Test Description\",\"price\":99.99,\"quantity\":10}"

echo.
echo.
echo Getting Products...
curl "http://localhost:8765/api/products"

echo.
echo.
echo Creating a Customer...
curl -X POST "http://localhost:8765/api/customers" -H "Content-Type: application/json" -d "{\"name\":\"John Doe\",\"email\":\"john@test.com\",\"phone\":\"1234567890\"}"

echo.
echo.
echo Getting Customers...
curl "http://localhost:8765/api/customers"

echo.
echo.
echo Creating an Order...
curl -X POST "http://localhost:8765/api/orders" -H "Content-Type: application/json" -d "{\"customerId\":1,\"productId\":1,\"quantity\":2}"

echo.
echo.
echo Getting Orders...
curl "http://localhost:8765/api/orders"

echo.
echo.
echo ========================================
echo Simple Test Complete!
echo ========================================
pause