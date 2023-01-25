package com.cloud.stream.spot.application.order;

import com.cloud.stream.spot.application.order.command.CreateNewOrderCommand;
import com.cloud.stream.spot.domain.Order;
import com.cloud.stream.spot.domain.OrderGateway;

public class DefaultCreateNewOrderUseCase extends CreateNewOrderUseCase {

    private final OrderGateway gateway;

    public DefaultCreateNewOrderUseCase(OrderGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public void execute(final CreateNewOrderCommand command) {
        final Order orderReceived = Order.createNewOrderWith(
            command.customerId(),
            command.value()
        );
        gateway.createNewOrder(orderReceived);
    }

}
