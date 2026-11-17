# Microservices for the DS assignment

This folder contains the Spring Cloud architecture and monitoring setup for the coursework:

- Config Server
- Service discovery (Eureka)
- API Gateway
- Product service with resilience patterns
- Order service using Axon CQRS and a MySQL read model
- Facture service with an independent MySQL database
- Prometheus metrics collection and a provisioned Grafana dashboard

## Start the infrastructure

From the repository root, start both databases and Axon Server:

`docker compose -f .\microservices\cqrs-compose.yaml up -d`

Wait until both MySQL containers are healthy, then start each application in a separate terminal from the repository root:

1. `.\backend\mvnw -f .\microservices\pom.xml -pl config-server spring-boot:run`
2. `.\backend\mvnw -f .\microservices\pom.xml -pl service-discovery spring-boot:run`
3. `.\backend\mvnw -f .\microservices\pom.xml -pl product-service clean spring-boot:run`
4. `.\backend\mvnw -f .\microservices\pom.xml -pl order-service spring-boot:run`
5. `.\backend\mvnw -f .\microservices\pom.xml -pl facture-service spring-boot:run`
6. `.\backend\mvnw -f .\microservices\pom.xml -pl api-gateway spring-boot:run`

Start monitoring in another terminal:

`docker compose -f .\microservices\compose.yaml up -d`

Then test the routing:

- `http://localhost:8761` -> Eureka dashboard
- `http://localhost:8081/api/products` -> product service direct call
- `http://localhost:8080/product/api/products` -> API gateway route
- `http://localhost:8080/api/orders` -> CQRS order query through the gateway
- `http://localhost:8080/api/factures` -> facture service through the gateway
- `http://localhost:8083/api/factures` -> facture service direct call
- `http://localhost:8024` -> Axon Server dashboard
- `http://localhost:8081/actuator/prometheus` -> Prometheus metrics
- `http://localhost:8082/actuator/prometheus` -> order service metrics
- `http://localhost:8083/actuator/prometheus` -> facture service metrics
- `http://localhost:9090/targets` -> Prometheus scrape target status
- `http://localhost:3001` -> Grafana (`admin` / `admin` on first start); open the **Achat / Achat Microservices** dashboard

## CQRS order API

Axon Server stores the order events; the order service rebuilds and updates its query projection in its own MySQL database (`achat_cqrs`). Flyway creates the projection and Axon persistence tables. Reads may briefly lag behind accepted commands while the event projection is being updated.

Create an order:

```http
POST http://localhost:8080/api/orders
Content-Type: application/json

{"productId":"p-1","quantity":2}
```

Cancel an order with `POST http://localhost:8080/api/orders/{orderId}/cancel`, then query the projection with `GET http://localhost:8080/api/orders`.

The local database defaults in `order-service/application.yml` are for development only. Override them with `CQRS_DB_URL`, `CQRS_DB_USERNAME`, and `CQRS_DB_PASSWORD` for other environments.

## Facture API

The facture service owns invoices and invoice lines in the `achat_factures` database. Supplier and product IDs are stored as references; this standalone service does not call the supplier or product services. Each line snapshots its unit price so existing invoices are unaffected by later product price changes. Totals and discounts are calculated by the service.

Create an invoice through the gateway:

```http
POST http://localhost:8080/api/factures
Content-Type: application/json

{
  "fournisseurId": 42,
  "detailsFacture": [
    {
      "productId": "p-1",
      "qteCommandee": 2,
      "prixUnitaire": 1200.00,
      "pourcentageRemise": 10
    }
  ]
}
```

List invoices with `GET /api/factures`, fetch one with `GET /api/factures/{id}`, filter by supplier with `GET /api/factures/fournisseur/{fournisseurId}`, and archive one with `PUT /api/factures/{id}/archive`.

## Resilience and metrics

The product service uses the `productService` Resilience4j instance:

- Retry: 3 total attempts with a fixed 500 ms wait.
- Rate limiter: 5 calls per second, rejecting excess calls immediately.
- Circuit breaker: opens at a 50% failure rate after at least 5 calls in a 10-call window, then waits 10 seconds before recovery checks.
- Fallback methods return a safe response when a protected call fails.

Spring Actuator exposes Prometheus-format metrics at `/actuator/prometheus` for each application. Prometheus scrapes those endpoints from Docker using `host.docker.internal`; ensure the applications are running before checking targets. Resilience4j metrics are also exported by Micrometer and visualized on the provisioned dashboard.

Stop monitoring with `docker compose -f .\microservices\compose.yaml down`. Stop the CQRS dependencies separately with `docker compose -f .\microservices\cqrs-compose.yaml down`; this keeps their named volumes. Add `-v` only when intentionally deleting local metrics, events, or database data.
