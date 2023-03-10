package com.cloud.stream.spot.infrastructure.order.api.in;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderInput(
    @JsonProperty("customer_id") UUID customerId,
    @JsonProperty("value") BigDecimal value
) { }
