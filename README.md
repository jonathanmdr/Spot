# Spot

---
This project is an example of how to use the "Spring Cloud Stream" with Java functional programming.

This example uses the message broker Kafka.

# Running environment

---

#### For the up environment:
```shell
# This command runs: Kafka Broker and Kafka UI
docker-compose up -d

# With makefile
make up
```

- For access the Kafka UI [click here](http://localhost:8580).
- For access the Open API UI [click here](http://localhost:8080).
- For access the Open API Docs [click here](http://localhost:8080/sync-api-docs).
- For access the Async API UI [click here](http://localhost:8080/springwolf/asyncapi-ui.html).
- For access the Async API Docs [click here](http://localhost:8080/async-api-docs).

#### For the down environment:
```shell
# This command down all environment and remove all unused files, folders and configurations
docker-compose down --remove-orphans --volumes

# With makefile
make down
```

# API

---

#### Request example:
> The order with a value less than or equal to 500.00 is valid and returns `APPROVED`.
> 
> The order with a value greater than to 500.00 is invalid and returns `REJECTED`.
```shell
curl --location --request POST 'http://localhost:8080/v1/orders' \
--header 'Content-Type: application/json' \
--data-raw '{
    "customer_id": "36a8ea26-4eb0-4b9d-b609-d095175a2f7b",
    "value": 1000.00
}'
```