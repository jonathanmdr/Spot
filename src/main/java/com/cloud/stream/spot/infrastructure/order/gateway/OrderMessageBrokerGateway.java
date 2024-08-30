package com.cloud.stream.spot.infrastructure.order.gateway;

import com.cloud.stream.spot.domain.OrderGateway;
import com.cloud.stream.spot.domain.exception.DomainException;
import com.cloud.stream.spot.domain.order.Order;
import com.cloud.stream.spot.infrastructure.configuration.stream.producer.StreamProducer;
import com.cloud.stream.spot.infrastructure.order.event.OrderCreatedEvent;
import org.springframework.stereotype.Component;

import static com.cloud.stream.spot.infrastructure.configuration.stream.bindings.OrderBinding.ORDER_CREATED;

@Component
public class OrderMessageBrokerGateway implements OrderGateway {

    private final StreamProducer producer;

    public OrderMessageBrokerGateway(final StreamProducer producer) {
        this.producer = producer;
    }

    @Override
    public void create(final Order order) {
        final OrderCreatedEvent event = new OrderCreatedEvent(
            order.getOrderId(),
            order.getCustomerId(),
            order.getValue(),
            order.getStatus()
        );

        final boolean hasSent = this.producer.send(ORDER_CREATED.channel(), event);

        if (!hasSent) {
            throw new DomainException("Failed to send order created event");
        }
    }

}
