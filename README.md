# sports-betting-settlement-simulator

## Overview

This project implements a backend service that simulates sports betting event outcome processing and bet settlement using Kafka and RocketMQ. It exposes an API endpoint for publishing event outcomes, processes those messages through Kafka, matches them against stored bets, and triggers settlement actions through RocketMQ.

The application is built with [Spring Cloud Stream](https://docs.spring.io/spring-cloud-stream/docs/current/reference/html/), using Spring Cloud Stream for Kafka and [Spring Cloud Stream RocketMQ](https://github.com/alibaba/spring-cloud-alibaba/tree/2025.1.x/spring-cloud-alibaba-starters/spring-cloud-starter-stream-rocketmq) to support the event-driven messaging flow in a clean and maintainable way.

Key capabilities include:

- Publishing sports event outcomes through a REST API
- Consuming event-outcome messages from Kafka
- Matching incoming outcomes with existing bets
- Producing settlement messages for RocketMQ
- Consuming and processing settlement messages to finalize bets

## Getting Started

Prerequisite for running the app

- [`Java`](https://adoptium.net)
- [`Container runtime (e.g., Docker, Podman)`](https://opencontainers.org)
- [`Intellij`](https://www.jetbrains.com/idea) or [`VSCode`](https://code.visualstudio.com)

You can run/debug the project by selecting the Intellij/VSCode Run Configuration:

- **betting-core**

When the application is started, the Docker Compose environment will be launched automatically to provide the required messaging infrastructure.

The application also includes a built-in data loader that pre-populates sample bets in memory when the app starts. You can review it in [src/main/java/com/betting/core/mock/InMemoryDataLoader.java](src/main/java/com/betting/core/mock/InMemoryDataLoader.java).

Once the services are running, you can access:

- [`Kafka UI`](http://localhost:8090)
- [`RocketMQ Dashboard`](http://localhost:8082)

### Usage Example

Example request to publish an event outcome:

```bash
curl --location 'localhost:8080/api/v1/event-outcomes' \
--header 'Content-Type: application/json' \
--data '{
    "eventId": "2eff1484-b51a-4794-a0c9-abb1723afdd4",
    "eventWinnerId": "3f0942a4-519c-435c-a3bc-e9144d4c3df2"
}'
```
