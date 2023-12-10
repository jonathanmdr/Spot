package com.cloud.stream.spot.infrastructure.order.gateway;

import com.cloud.stream.spot.KafkaIntegrationTest;
import com.cloud.stream.spot.domain.OrderGateway;
import com.cloud.stream.spot.domain.order.Order;
import com.cloud.stream.spot.domain.order.OrderStatus;
import com.cloud.stream.spot.infrastructure.configuration.json.Json;
import com.cloud.stream.spot.infrastructure.order.event.OrderCreatedEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.test.OutputDestination;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@KafkaIntegrationTest
class OrderMessageBrokerGatewayIntegrationTest {

    @Autowired
    private OrderGateway orderGateway;

    @Autowired
    private OutputDestination outputDestination;

    @Test
    void givenAnOrder_whenCallsCreate_thenPublishOrderCreatedEvent() {
        final var order = Order.createNewOrderWith(UUID.randomUUID(), BigDecimal.TEN);
        this.orderGateway.create(order);

        final var actual = this.outputDestination.receive(Duration.ofSeconds(1).toMillis(), "order-processed");

        final var expected = new OrderCreatedEvent(
            order.getOrderId(),
            order.getCustomerId(),
            order.getValue(),
            OrderStatus.APPROVED
        );

        assertThat(Json.readValue(actual.getPayload(), OrderCreatedEvent.class)).isEqualTo(expected);
    }

}
