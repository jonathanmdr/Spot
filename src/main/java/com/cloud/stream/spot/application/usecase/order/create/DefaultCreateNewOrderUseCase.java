package com.cloud.stream.spot.application.usecase.order.create;

import com.cloud.stream.spot.domain.order.Order;
import com.cloud.stream.spot.domain.OrderGateway;

public final class DefaultCreateNewOrderUseCase extends CreateNewOrderUseCase {

    private final OrderGateway gateway;

    public DefaultCreateNewOrderUseCase(final OrderGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public void execute(final CreateNewOrderCommand command) {
        final Order orderReceived = command.toOrder();
        gateway.create(orderReceived);
    }

}
