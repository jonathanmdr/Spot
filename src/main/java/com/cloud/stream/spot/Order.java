package com.cloud.stream.spot;

import java.math.BigDecimal;
import java.util.UUID;

public class Order {

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

    public Order process() {
        return isValid() ? approve() : reject();
    }

    private boolean isValid() {
        final BigDecimal maxValueAccepted = BigDecimal.valueOf(500);
        return maxValueAccepted.compareTo(this.value) >= 0;
    }

    private Order approve() {
        return new Order(
            this.orderId,
            this.customerId,
            this.value,
            OrderStatus.APPROVED
        );
    }

    private Order reject() {
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

    public enum OrderStatus {
        CREATED,
        APPROVED,
        REJECTED
    }

}
