package com.cloud.stream.spot.application.usecase.order.process;

import com.cloud.stream.spot.domain.order.Order;

public final class DefaultProcessOrderUseCase extends ProcessOrderUseCase {

    @Override
    public ProcessOrderOutput execute(final ProcessOrderCommand command) {
        final Order orderReceived = command.toOrder();

        final Order order = orderReceived.isValid() ? orderReceived.approve() : orderReceived.reject();

        return new ProcessOrderOutput(
            order.getOrderId(),
            order.getCustomerId(),
            order.getValue(),
            order.getStatus()
        );
    }

}
