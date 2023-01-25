# Spot

---
This project is an example of how to use the "Spring Cloud Stream" with Java functional programming.

This example uses the message broker Kafka.

# Running environment

---

#### For the up environment:
```shell
# This command runs: Kafka Broker, Zookeeper and Kafka UI
docker-compose up -d
```

For access the Kafka UI [click here](http://localhost:8085).

#### For the down environment:
```shell
# This command down all environment and remove all unused files, folders and configurations
docker-compose down --remove-orphans --volumes
```

# API

---

#### Request example:
```shell
curl --location --request POST 'http://localhost:8080/spot/orders' \
--header 'Content-Type: application/json' \
--data-raw '{
    "customer_id": "36a8ea26-4eb0-4b9d-b609-d095175a2f7b",
    "value": 1000.00
}'
```