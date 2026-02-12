This project contains two Spring Boot microservices:
- Product Service
- Order Service
They are containerized using Docker and orchestrated with Docker Compose.

Prerequisites
- Ensure the following are installed:
- Docker Desktop (running)
- Git
- Java 21 (only needed if running without Docker)
- Maven (or use Maven wrapper)

1. Clone The Repository
git clone <your-repo-url>
cd ecommerce-microservices

2. Run With Docker Compose
From the project root (where docker-compose.yml exists):
docker compose up --build
This will:
- Build both microservices
- Create a shared network
- Start Product Service
- Start Order Service (after Product Service is healthy)

3.1 Service URLs
Product Service: http://localhost:8081/api/products
Order Service: http://localhost:8082/api/orders

3.2 Testing (Postman):
- Get all products:
GET http://localhost:8081/api/products

- Create product:
POST http://localhost:8081/api/products
{
  "name": "Laptop",
  "price": 1200,
  "quantity": 5
}

- Get all orders:
GET http://localhost:8082/api/orders

- Create order:
POST http://localhost:8082/api/orders
{
  "productId": 1,
  "quantity": 2
}

4. Stop services:
docker compose down
