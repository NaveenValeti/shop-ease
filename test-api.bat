@echo off
echo Testing ShopEase Microservices...
echo.

echo 1. Testing Discovery Server:
curl -s http://localhost:8761/actuator/health
echo.
echo.

echo 2. Testing API Gateway:
curl -s http://localhost:8765/actuator/health
echo.
echo.

echo 3. Testing Product Service directly:
curl -s http://localhost:8080/actuator/health
echo.
echo.

echo 4. Testing Product Service via API Gateway:
curl -s http://localhost:8765/api/products
echo.
echo.

echo 5. Creating a test product:
curl -X POST http://localhost:8765/api/products -H "Content-Type: application/json" -d "{\"name\":\"Test Laptop\",\"description\":\"Gaming Laptop\",\"price\":999.99,\"quantity\":10}"
echo.
echo.

pause