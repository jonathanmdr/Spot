package com.cloud.stream.spot.domain;

import com.cloud.stream.spot.domain.order.Order;
import com.cloud.stream.spot.domain.order.event.OrderProcessedEvent;

public interface OrderGateway {

    void create(final Order order);
    OrderProcessedEvent process(final Order order);

}
