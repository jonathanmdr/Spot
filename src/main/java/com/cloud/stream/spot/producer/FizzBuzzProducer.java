package com.cloud.stream.spot.producer;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

@Component
public class FizzBuzzProducer implements Supplier<Flux<Integer>> {

    @Override
    public Flux<Integer> get() {
        return Flux.range(1, 100).onBackpressureBuffer();
    }

}
