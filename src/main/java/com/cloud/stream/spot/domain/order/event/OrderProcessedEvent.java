package com.cloud.stream.spot.domain.order.event;

import com.cloud.stream.spot.domain.order.OrderStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderProcessedEvent(
    @JsonProperty("order_id") UUID orderId,
    @JsonProperty("customer_id") UUID customerId,
    @JsonProperty("value") BigDecimal value,
    @JsonProperty("status") OrderStatus status
) {
}
