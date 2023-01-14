package com.cloud.stream.spot.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class FizzBuzzConsumer implements Consumer<String> {

    private final Logger log = LoggerFactory.getLogger(FizzBuzzConsumer.class);

    @Override
    public void accept(final String s) {
        log.info(s);
    }

}
