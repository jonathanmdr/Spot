package com.cloud.stream.spot.infrastructure.gateway.out;

import com.cloud.stream.spot.domain.OrderStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderCreatedEvent(
    UUID orderId,
    UUID customerId,
    BigDecimal value,
    OrderStatus status
) {
}
