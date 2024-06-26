package com.cloud.stream.spot.domain.order;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class OrderUnitTest {

    @Test
    void recursiveComparison() {
        final var orderId = UUID.randomUUID();
        final var customerId = UUID.randomUUID();
        final var value = BigDecimal.valueOf(100);
        final var status = OrderStatus.CREATED;

        final var orderOne = Order.buildOrderWith(orderId, customerId, value, status);
        final var orderTwo = Order.buildOrderWith(orderId, customerId, value, status);

        // ❌ This approach will fail when the equals method is not implemented
        assertThat(orderOne).isEqualTo(orderTwo);

        // ✅ This approach does not require the equals method to be implemented
        assertThat(orderOne)
            .usingRecursiveComparison()
            .isEqualTo(orderTwo);
    }

    @Test
    void softAssertions() {
        final var orderId = UUID.randomUUID();
        final var customerId = UUID.randomUUID();
        final var value = BigDecimal.valueOf(100);
        final var status = OrderStatus.CREATED;

        final var order = Order.buildOrderWith(orderId, customerId, value, status);

        // ❌ This approach will fail one by one
        assertThat(order.getOrderId()).isEqualTo(orderId);
        assertThat(order.getCustomerId()).isEqualTo(customerId);
        assertThat(order.getValue()).isEqualTo(value);
        assertThat(order.getStatus()).isEqualTo(status);

        // ✅ This approach will fail all at once
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(order.getOrderId()).isEqualTo(orderId);
            softly.assertThat(order.getCustomerId()).isEqualTo(customerId);
            softly.assertThat(order.getValue()).isEqualTo(value);
            softly.assertThat(order.getStatus()).isEqualTo(status);
        });
    }

    @Test
    void customizedAssertion() {
        final var orderId = UUID.randomUUID();
        final var customerId = UUID.randomUUID();
        final var value = BigDecimal.valueOf(500);
        final var status = OrderStatus.CREATED;

        final var order = Order.buildOrderWith(orderId, customerId, value, status);

        OrderAssert.assertThat(order).containsAValidValue();
    }

    @Test
    void customComparatorAssertion() {
        final var orderId = UUID.randomUUID();
        final var customerId = UUID.randomUUID();
        final var value = BigDecimal.valueOf(500);
        final var status = OrderStatus.CREATED;

        final var orderOne = Order.buildOrderWith(orderId, customerId, value, status);
        final var orderTwo = Order.buildOrderWith(orderId, customerId, value, status);

        assertThat(orderOne)
            .usingComparator(Order::compareTo)
            .isEqualTo(orderTwo);
    }

}
