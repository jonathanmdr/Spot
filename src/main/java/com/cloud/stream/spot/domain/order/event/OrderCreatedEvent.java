package com.cloud.stream.spot.domain.order.event;

import com.cloud.stream.spot.domain.order.OrderStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * The '@JsonProperty' annotation is a workaround for use correctly the Jackson Serializer/Deserializer
 * this problem exists only for 'record' type.
 * However, the '@JsonProperty' is not recommended for use in the domain objects.
 *
 * @param orderId
 * @param customerId
 * @param value
 * @param status
 */
public record OrderCreatedEvent(
        @JsonProperty("order_id") UUID orderId,
        @JsonProperty("customer_id") UUID customerId,
        @JsonProperty("value") BigDecimal value,
        @JsonProperty("status") OrderStatus status
) {
}
