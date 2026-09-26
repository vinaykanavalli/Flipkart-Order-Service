# Flipkart-Style Order Processing with Spring Boot + Kafka (Single-Project Monolith)

Order -> Payment -> Delivery -> Notification, all organized into modular sub-packages within a single Spring Boot application running on port 8080, connected through Kafka topics.

| Component / Package | Port | DB | Consumer Group | Consumes | Produces |
|---|---|---|---|---|---|
| `order` | 8080 | flipkart_single_db | order-group | payment-success, payment-failed, delivery-events | order-created |
| `payment` | 8080 | flipkart_single_db | payment-group | order-created | payment-success, payment-failed |
| `delivery` | 8080 | flipkart_single_db | delivery-group | payment-success | delivery-created, delivery-events |
| `notification` | 8080 | flipkart_single_db | notification-group | all topics | - |

Topics: `order-created`, `payment-success`, `payment-failed`, `delivery-created`, `delivery-events` + a `.DLT` for each, 3 partitions each, message key = `orderId`.

## Prerequisites
Java 17+, Maven 3.9+, Docker, Postman.

## Run
```bash
docker compose up -d            # Kafka, Zookeeper, and Kafka UI 
mvn clean package -DskipTests   # build the project
mvn spring-boot:run             # run the single application instance on port 8080
---

**## 📂 Project Package Structure**
```text
src/main/java/com/shop/
├── order/         # Order creation, REST controller, entity, & repository
├── payment/       # Payment consumer, retry logic, entity, & repository
├── delivery/      # Delivery tracking lifecycle handler, entity, & repository
└── notification/  # Centralized event listener for customer notifications

Customer → POST /orders → ORDER SERVICE
                        │ ORDER_CREATED
                        ▼
                Kafka: order-created ─────────────► NOTIFICATION SERVICE
                        ▼
                    PAYMENT SERVICE
                ┌───────┴───────┐
         PAYMENT_SUCCESS     PAYMENT_FAILED
                │               │
         Kafka: payment-success  Kafka: payment-failed ──► NOTIFICATION
                │ ├────────────────────────► NOTIFICATION
                ▼
           DELIVERY SERVICE (Ekart)
                │ DELIVERY_CREATED          → delivery-created
                │ DELIVERY_IN_TRANSIT       → delivery-in-transit
                │ DELIVERY_OUT_FOR_DELIVERY → delivery-out-for-delivery
                │ ORDER_DELIVERED           → order-delivered
                ▼
        NOTIFICATION SERVICE  +  ORDER SERVICE (updates order status)
