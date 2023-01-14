package com.cloud.stream.spot.processor;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Component
public class FizzBuzzProcessor implements Function<Flux<Integer>, Flux<String>> {

    @Override
    public Flux<String> apply(final Flux<Integer> integerFlux) {
        return integerFlux.map(this::evaluateFizzBuzz);
    }

    private String evaluateFizzBuzz(final Integer value) {
        String result = "";

        if (value % 3 == 0) {
            result += "Fizz";
        }

        if (value % 5 == 0) {
            result += "Buzz";
        }

        return result.length() == 0 ? String.valueOf(value) : result;
    }

}
