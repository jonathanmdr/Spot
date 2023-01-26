package com.cloud.stream.spot.application.usecase.order.process;

import com.cloud.stream.spot.domain.order.Order;

public class DefaultProcessOrderUseCase extends ProcessOrderUseCase {

    @Override
    public ProcessOrderOutput execute(final ProcessOrderCommand command) {
        final Order orderReceived = command.toOrder();

        final Order order = orderReceived.validate() ? orderReceived.approve() : orderReceived.reject();

        return new ProcessOrderOutput(
            order.getOrderId(),
            order.getCustomerId(),
            order.getValue(),
            order.getStatus()
        );
    }

}
