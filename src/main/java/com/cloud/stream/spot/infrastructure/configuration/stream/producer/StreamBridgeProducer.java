package com.cloud.stream.spot.infrastructure.configuration.stream.producer;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class StreamBridgeProducer implements StreamProducer {

    private final StreamBridge streamBridge;

    public StreamBridgeProducer(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Override
    public boolean send(final String channel, final Object payload) {
        return this.streamBridge.send(channel, payload);
    }

}
