package com.cloud.stream.spot.domain.order;

import com.cloud.stream.spot.domain.exception.DomainException;

import java.math.BigDecimal;
import java.util.Random;
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
        final OrderStatus status) {

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

    public boolean validate() {
        return new Random().nextBoolean();
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

}
