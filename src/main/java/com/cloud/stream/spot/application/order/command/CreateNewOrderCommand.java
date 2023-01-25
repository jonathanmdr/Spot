package com.cloud.stream.spot.application.order.command;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateNewOrderCommand(UUID customerId, BigDecimal value) {
}
