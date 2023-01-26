package com.cloud.stream.spot.domain;

import com.cloud.stream.spot.domain.order.Order;

public interface OrderGateway {

    void create(final Order order);

}
