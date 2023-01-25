package com.cloud.stream.spot.application.usecase.order.create.comand;

import com.cloud.stream.spot.domain.order.Order;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateNewOrderCommand(
    UUID customerId,
    BigDecimal value
) {

    public Order toOrder() {
        return Order.createNewOrderWith(
            this.customerId,
            this.value
        );
    }

}
