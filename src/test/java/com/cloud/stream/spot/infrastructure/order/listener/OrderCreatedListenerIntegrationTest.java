package com.cloud.stream.spot.infrastructure.order.listener;

import com.cloud.stream.spot.KafkaIntegrationTest;
import com.cloud.stream.spot.domain.OrderGateway;
import com.cloud.stream.spot.domain.order.Order;
import com.cloud.stream.spot.domain.order.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

@KafkaIntegrationTest
class OrderCreatedListenerIntegrationTest {

    @Autowired
    private OrderGateway orderGateway;

    @SpyBean
    private OrderCreatedListener orderCreatedListener;

    @SpyBean
    private OrderProcessedListener orderProcessedListener;

    @Test
    void givenAValidOrder_whenCallsCreate_thenProduceOrderCreatedEvent() {
        final var order = Order.createNewOrderWith(
            UUID.randomUUID(),
            BigDecimal.TEN
        );

        this.orderGateway.create(order);

        verify(this.orderCreatedListener).apply(argThat(
            orderCreatedEvent -> Objects.equals(orderCreatedEvent.orderId(), order.getOrderId())
                && Objects.equals(orderCreatedEvent.customerId(), order.getCustomerId())
                && Objects.equals(orderCreatedEvent.value(), order.getValue())
                && Objects.equals(orderCreatedEvent.status(), order.getStatus())
        ));

        verify(this.orderProcessedListener).accept(argThat(
            orderProcessedEvent -> Objects.equals(orderProcessedEvent.orderId(), order.getOrderId())
                && Objects.equals(orderProcessedEvent.customerId(), order.getCustomerId())
                && Objects.equals(orderProcessedEvent.value(), order.getValue())
                && Objects.equals(orderProcessedEvent.status(), OrderStatus.APPROVED)
        ));
    }

    @Test
    void givenAnInvalidOrder_whenCallsCreate_thenProduceOrderCreatedEvent() {
        final var order = Order.createNewOrderWith(
            UUID.randomUUID(),
            BigDecimal.valueOf(500.01)
        );

        this.orderGateway.create(order);

        verify(this.orderCreatedListener).apply(argThat(
            orderCreatedEvent -> Objects.equals(orderCreatedEvent.orderId(), order.getOrderId())
                && Objects.equals(orderCreatedEvent.customerId(), order.getCustomerId())
                && Objects.equals(orderCreatedEvent.value(), order.getValue())
                && Objects.equals(orderCreatedEvent.status(), order.getStatus())
        ));

        verify(this.orderProcessedListener).accept(argThat(
            orderProcessedEvent -> Objects.equals(orderProcessedEvent.orderId(), order.getOrderId())
                && Objects.equals(orderProcessedEvent.customerId(), order.getCustomerId())
                && Objects.equals(orderProcessedEvent.value(), order.getValue())
                && Objects.equals(orderProcessedEvent.status(), OrderStatus.REJECTED)
        ));
    }

}
