package com.cloud.stream.spot.infrastructure.order.gateway;

import com.cloud.stream.spot.domain.OrderGateway;
import com.cloud.stream.spot.domain.order.Order;
import com.cloud.stream.spot.infrastructure.order.event.OrderCreatedEvent;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

import static com.cloud.stream.spot.infrastructure.configuration.streambinding.OrderBindingProperties.ORDER_CREATED;

@Component
public class OrderPubSubGateway implements OrderGateway {

    private final StreamBridge bridge;

    public OrderPubSubGateway(StreamBridge bridge) {
        this.bridge = bridge;
    }

    @Override
    public void create(final Order order) {
        final OrderCreatedEvent event = new OrderCreatedEvent(
            order.getOrderId(),
            order.getCustomerId(),
            order.getValue(),
            order.getStatus()
        );

        bridge.send(ORDER_CREATED, event);
    }

}
