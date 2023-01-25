package com.cloud.stream.spot.application.usecase.order.process;

import com.cloud.stream.spot.application.usecase.order.process.command.ProcessOrderCommand;
import com.cloud.stream.spot.domain.OrderGateway;
import com.cloud.stream.spot.domain.order.Order;
import com.cloud.stream.spot.domain.order.event.OrderProcessedEvent;

public class DefaultProcessOrderUseCase extends ProcessOrderUseCase {

    private final OrderGateway gateway;

    public DefaultProcessOrderUseCase(OrderGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public OrderProcessedEvent execute(final ProcessOrderCommand command) {
        final Order orderReceived = command.toOrder();

        final boolean hasRisk = orderReceived.validate();
        final Order order = hasRisk ? orderReceived.reject() : orderReceived.approve();

        return gateway.process(order);
    }

}
