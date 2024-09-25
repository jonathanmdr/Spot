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

import java.math.BigDecimal;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
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
    public Function<Tuple2<Flux<OrderCreatedEvent>, Flux<OrderCreatedEvent>>, Flux<OrderProcessedEvent>> orderCreatedListener() {
        return tuple -> tuple.getT1().mergeWith(tuple.getT2())
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
    }

    @Bean
    public Consumer<OrderProcessedEvent> orderProcessedListener() {
        return orderProcessedEvent -> {
            final var jsonEvent = Json.writeValueAsString(orderProcessedEvent);
            log.info("Order processed: {}", jsonEvent);
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
        private final AtomicInteger roundRobinIndex;

        public OrderController(final StreamBridge streamBridge) {
            this.streamBridge = streamBridge;
            this.roundRobinIndex = new AtomicInteger(0);
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

            final var currentBinding = this.roundRobinIndex.getAndUpdate(i -> (i + 1) % OrderBinding.values().length);
            final var currentChannel = OrderBinding.values()[currentBinding].channel();
            log.info("Sending order to channel: {}", currentChannel);
            this.streamBridge.send(currentChannel, orderCreatedEvent);
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
