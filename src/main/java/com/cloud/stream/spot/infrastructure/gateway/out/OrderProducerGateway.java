package com.cloud.stream.spot.infrastructure.gateway.out;

import com.cloud.stream.spot.domain.Order;
import com.cloud.stream.spot.domain.OrderGateway;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

import static com.cloud.stream.spot.infrastructure.configuration.streambinding.OrderBindingProperties.ORDER_CREATED;

@Component
public class OrderProducerGateway implements OrderGateway {

    private final StreamBridge bridge;

    public OrderProducerGateway(StreamBridge bridge) {
        this.bridge = bridge;
    }

    @Override
    public void createNewOrder(final Order order) {
        final OrderCreatedEvent event = new OrderCreatedEvent(
            order.getOrderId(),
            order.getCustomerId(),
            order.getValue(),
            order.getStatus()
        );

        bridge.send(ORDER_CREATED, event);
    }

}
