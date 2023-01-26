package com.cloud.stream.spot.infrastructure.order.listener;

import com.cloud.stream.spot.application.usecase.order.process.ProcessOrderCommand;
import com.cloud.stream.spot.application.usecase.order.process.ProcessOrderOutput;
import com.cloud.stream.spot.application.usecase.order.process.ProcessOrderUseCase;
import com.cloud.stream.spot.infrastructure.order.event.OrderCreatedEvent;
import com.cloud.stream.spot.infrastructure.order.event.OrderProcessedEvent;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class OrderCreatedListener implements Function<OrderCreatedEvent, OrderProcessedEvent> {

    private final ProcessOrderUseCase useCase;

    public OrderCreatedListener(final ProcessOrderUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public OrderProcessedEvent apply(final OrderCreatedEvent order) {
        final ProcessOrderCommand command = new ProcessOrderCommand(
            order.orderId(),
            order.customerId(),
            order.value(),
            order.status()
        );

        final ProcessOrderOutput output = this.useCase.execute(command);

        return new OrderProcessedEvent(
            output.orderId(),
            output.customerId(),
            output.value(),
            output.status()
        );
    }

}
