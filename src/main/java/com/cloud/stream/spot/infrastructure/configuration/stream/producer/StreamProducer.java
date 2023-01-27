package com.cloud.stream.spot.infrastructure.configuration.stream.producer;

@FunctionalInterface
public interface StreamProducer {

    boolean send(final String channel, final Object payload);

}
