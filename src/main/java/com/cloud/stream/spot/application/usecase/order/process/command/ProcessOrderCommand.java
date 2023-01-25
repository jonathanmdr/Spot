package com.cloud.stream.spot.application.usecase.order.process.command;

import com.cloud.stream.spot.domain.order.Order;
import com.cloud.stream.spot.domain.order.OrderStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record ProcessOrderCommand(
    UUID orderId,
    UUID customerId,
    BigDecimal value,
    OrderStatus status
) {

    public Order toOrder() {
        return Order.buildOrderWith(
            this.orderId,
            this.customerId,
            this.value,
            this.status
        );
    }

}
