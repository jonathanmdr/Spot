package com.cloud.stream.spot.infrastructure.configuration.stream.bindings;

public enum OrderBinding {

    ORDER_CREATED("orderCreatedProducer-out-0");

    private final String channel;

    OrderBinding(final String channel) {
        this.channel = channel;
    }

    public String channel() {
        return this.channel;
    }

}
