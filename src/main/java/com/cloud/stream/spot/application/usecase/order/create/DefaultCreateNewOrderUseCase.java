package com.cloud.stream.spot.application.usecase.order.create;

import com.cloud.stream.spot.application.usecase.order.create.comand.CreateNewOrderCommand;
import com.cloud.stream.spot.domain.order.Order;
import com.cloud.stream.spot.domain.OrderGateway;

public class DefaultCreateNewOrderUseCase extends CreateNewOrderUseCase {

    private final OrderGateway gateway;

    public DefaultCreateNewOrderUseCase(OrderGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public void execute(final CreateNewOrderCommand command) {
        final Order orderReceived = command.toOrder();
        gateway.create(orderReceived);
    }

}
