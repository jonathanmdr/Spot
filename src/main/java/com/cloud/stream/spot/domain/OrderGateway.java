package com.cloud.stream.spot.domain;

public interface OrderGateway {

    void createNewOrder(final Order order);

}
