package com.cloud.stream.spot;

public enum OrderBinding {

    ORDER_CREATED_ONE("orderCreatedProducer-out-0"),
    ORDER_CREATED_TWO("orderCreatedProducer-out-1");

    private final String channel;

    OrderBinding(final String channel) {
        this.channel = channel;
    }

    public String channel() {
        return this.channel;
    }

}
