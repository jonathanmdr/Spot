package com.cloud.stream.spot.application.usecase.order.process;

import com.cloud.stream.spot.domain.order.OrderStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record ProcessOrderOutput(
    UUID orderId,
    UUID customerId,
    BigDecimal value,
    OrderStatus status
) { }
