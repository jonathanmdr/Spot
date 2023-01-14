package com.cloud.stream.spot.producer;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.stream.IntStream;

@Component
public class NonFunctionalProducer {

    private final StreamBridge streamBridge;

    public NonFunctionalProducer(final StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @PostConstruct
    public void init() {
        IntStream.range(1, 101)
            .forEach(value -> streamBridge.send("fizzBuzzProcessor-out-0", String.format("Hello World %s", value)));
    }

}
