package com.cloud.stream.spot.infrastructure.api.in;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderInput(
    @JsonProperty("customer_id") UUID customerId,
    @JsonProperty("value") BigDecimal value
) {

    @Override
    public String toString() {
        return "OrderInput{" +
                "customerId=" + customerId +
                ", value=" + value +
                '}';
    }

}
