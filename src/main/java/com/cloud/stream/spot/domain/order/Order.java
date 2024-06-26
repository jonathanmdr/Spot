package com.cloud.stream.spot.domain.order;

import com.cloud.stream.spot.domain.exception.DomainException;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;

public class Order implements Comparable<Order> {

    private final UUID orderId;
    private final UUID customerId;
    private final BigDecimal value;
    private final OrderStatus status;

    private Order(final UUID orderId, final UUID customerId, final BigDecimal value, final OrderStatus status) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.value = value;
        this.status = status;
    }

    public static Order createNewOrderWith(final UUID customerId, final BigDecimal value) {
        return new Order(
            UUID.randomUUID(),
            customerId,
            value,
            OrderStatus.CREATED
        );
    }

    public static Order buildOrderWith(
        final UUID orderId,
        final UUID customerId,
        final BigDecimal value,
        final OrderStatus status
    ) {

        if (!OrderStatus.CREATED.equals(status)) {
            throw new DomainException(
                String.format("Order '%s' has already been processed", orderId)
            );
        }

        return new Order(
            orderId,
            customerId,
            value,
            status
        );
    }

    public boolean isValid() {
        final BigDecimal maxValueAccepted = BigDecimal.valueOf(500);
        return maxValueAccepted.compareTo(this.value) >= 0;
    }

    public Order approve() {
        return new Order(
            this.orderId,
            this.customerId,
            this.value,
            OrderStatus.APPROVED
        );
    }

    public Order reject() {
        return new Order(
            this.orderId,
            this.customerId,
            this.value,
            OrderStatus.REJECTED
        );
    }

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public BigDecimal getValue() {
        return value;
    }

    public OrderStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final Order order)) return false;
        return Objects.equals(orderId, order.orderId)
            && Objects.equals(customerId, order.customerId)
            && Objects.equals(value, order.value)
            && status == order.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, customerId, value, status);
    }

    @Override
    public int compareTo(@NotNull final Order o) {
        return Comparator.comparing(Order::getOrderId)
            .thenComparing(Order::getCustomerId)
            .thenComparing(Order::getValue)
            .thenComparing(Order::getStatus)
            .compare(this, o);
    }

}
