@echo off
echo ========================================
echo Progressive E2E Testing
echo ========================================

echo.
echo Phase 1: Testing Product Service Only...
echo Creating product...
curl -X POST http://localhost:8080/api/products -H "Content-Type: application/json" -d "{\"name\":\"MacBook Pro\",\"description\":\"Apple laptop\",\"price\":2499.99,\"quantity\":10}"

echo.
echo Getting products...
curl http://localhost:8080/api/products

echo.
echo.
echo Phase 2: Testing via API Gateway (if running)...
curl http://localhost:8765/api/products 2>nul || echo "API Gateway not running - skipping"

echo.
echo.
echo Phase 3: Testing Order Service (if running)...
curl -X POST http://localhost:8081/api/orders -H "Content-Type: application/json" -d "{\"productId\":1,\"quantity\":2}" 2>nul || echo "Order Service not running - skipping"

echo.
echo Getting orders...
curl http://localhost:8081/api/orders 2>nul || echo "Order Service not running - skipping"

echo.
echo.
echo ========================================
echo Progressive Test Complete!
echo Check which services are running: docker compose ps
echo ========================================
pause