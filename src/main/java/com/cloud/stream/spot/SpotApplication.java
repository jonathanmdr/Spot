package com.cloud.stream.spot;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.config.GlobalChannelInterceptor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootApplication
public class SpotApplication {

    private static final Logger log = LoggerFactory.getLogger(SpotApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpotApplication.class, args);
    }

    @Bean
    public Function<Tuple2<Flux<OrderCreatedEvent>, Flux<OrderCreatedEvent>>, Tuple2<Flux<OrderProcessedEvent>, Flux<OrderProcessedEvent>>> orderCreatedListener() {
        return tuple -> {
            final var orderProcessedEventFlux = tuple.getT1().mergeWith(tuple.getT2())
                .map(orderCreatedEvent -> {
                    final var order = Order.buildOrderWith(
                        orderCreatedEvent.orderId(),
                        orderCreatedEvent.customerId(),
                        orderCreatedEvent.value(),
                        orderCreatedEvent.status()
                    ).process();
                    return new OrderProcessedEvent(
                        order.getOrderId(),
                        order.getCustomerId(),
                        order.getValue(),
                        order.getStatus()
                    );
                });
            return Tuples.of(orderProcessedEventFlux, orderProcessedEventFlux);
        };
    }

    @Bean
    public Consumer<OrderProcessedEvent> orderProcessedListenerLegacy() {
        return orderProcessedEvent -> {
            final var jsonEvent = Json.writeValueAsString(orderProcessedEvent);
            log.info("Order processed from cluster legacy: {}", jsonEvent);
        };
    }

    @Bean
    public Consumer<OrderProcessedEvent> orderProcessedListenerNew() {
        return orderProcessedEvent -> {
            final var jsonEvent = Json.writeValueAsString(orderProcessedEvent);
            log.info("Order processed from cluster new: {}", jsonEvent);
        };
    }

    @Component
    @GlobalChannelInterceptor(
        patterns = {
            "*-out-*"
        }
    )
    public static class CustomChannelInterceptor implements ChannelInterceptor {
        @Override
        public void afterSendCompletion(@NotNull final Message<?> message, @NotNull final MessageChannel channel, final boolean sent, final Exception ex) {
            final var jsonMessage = Json.writeValueAsString(message.getPayload());
            final var cleanedJsonMessage = jsonMessage.replace("\"", "");
            final var decodedJsonMessageBytes = Base64.getDecoder().decode(cleanedJsonMessage);
            final var decodedJsonMessage = new String(decodedJsonMessageBytes);
            log.info("Message sent: {}", decodedJsonMessage);
        }
    }

    @Bean
    public ObjectMapper objectMapper() {
        return Json.mapper();
    }

    @RestController
    @RequestMapping(path = "/orders", consumes = APPLICATION_JSON_VALUE)
    public static class OrderController {

        private final StreamBridge streamBridge;
        private final Random random;

        public OrderController(final StreamBridge streamBridge) {
            this.streamBridge = streamBridge;
            this.random = new Random();
        }

        @PostMapping
        @ResponseStatus(ACCEPTED)
        public void create(@RequestBody final OrderInput input) {
            final var newOrder = Order.createNewOrderWith(input.customerId(), input.value());
            final var orderCreatedEvent = new OrderCreatedEvent(
                newOrder.getOrderId(),
                newOrder.getCustomerId(),
                newOrder.getValue(),
                newOrder.getStatus()
            );

            final var randomBinding = this.random.nextInt(OrderBinding.values().length);
            final var randomChannel = OrderBinding.values()[randomBinding].channel();
            log.info("Sending order to channel: {}", randomChannel);
            this.streamBridge.send(randomChannel, orderCreatedEvent);
        }

        public record OrderInput(
            @JsonProperty("customer_id") UUID customerId,
            @JsonProperty("value") BigDecimal value
        ) { }

    }

    public record OrderCreatedEvent(
        @JsonProperty("order_id") UUID orderId,
        @JsonProperty("customer_id") UUID customerId,
        @JsonProperty("value") BigDecimal value,
        @JsonProperty("status") Order.OrderStatus status
    ) { }

    public record OrderProcessedEvent(
        @JsonProperty("order_id") UUID orderId,
        @JsonProperty("customer_id") UUID customerId,
        @JsonProperty("value") BigDecimal value,
        @JsonProperty("status") Order.OrderStatus status
    ) { }

}
