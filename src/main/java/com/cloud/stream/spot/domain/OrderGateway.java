package com.cloud.stream.spot.domain;

import com.cloud.stream.spot.domain.order.Order;

@FunctionalInterface
public interface OrderGateway {

    void create(final Order order);

}
