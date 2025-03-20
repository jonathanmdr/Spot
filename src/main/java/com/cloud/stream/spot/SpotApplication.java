package com.cloud.stream.spot;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.binder.kafka.ListenerContainerWithDlqAndRetryCustomizer;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Objects;
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
    public Function<OrderCreatedEvent, OrderProcessedEvent> orderCreatedListener() {
        return orderCreatedEvent -> {
            final Order order = Order.buildOrderWith(
                orderCreatedEvent.orderId(),
                orderCreatedEvent.customerId(),
                orderCreatedEvent.value(),
                orderCreatedEvent.status()
            ).process();

            if (order.isRejected()) {
                throw new DomainException("Order value is too high");
            }

            return new OrderProcessedEvent(
                order.getOrderId(),
                order.getCustomerId(),
                order.getValue(),
                order.getStatus()
            );
        };
    }

    @Bean
    public Consumer<OrderProcessedEvent> orderProcessedListener() {
        return orderProcessedEvent -> {
            final var jsonEvent = Json.writeValueAsString(orderProcessedEvent);
            log.info("Order processed: {}", jsonEvent);
        };
    }

    @Bean
    public ListenerContainerWithDlqAndRetryCustomizer customizer(final KafkaOperations<?, ?> kafkaOperations) {
        return (container, destinationName, group, dlqDestinationResolver, backOff) -> {
            if (Objects.isNull(dlqDestinationResolver) || Objects.isNull(backOff)) {
                return;
            }

            final DeadLetterPublishingRecoverer deadLetterPublishingRecoverer = new DeadLetterPublishingRecoverer(kafkaOperations, dlqDestinationResolver);
            deadLetterPublishingRecoverer.setExceptionHeadersCreator((kafkaHeaders, exception, isKey, headerNames) -> {
                final String exceptionType = ExceptionUtils.getRootCauseMessage(exception);
                kafkaHeaders.add("exception-type", exceptionType.getBytes());
            });
            container.setCommonErrorHandler(new DefaultErrorHandler(deadLetterPublishingRecoverer, backOff));
        };
    }

    @Bean
    public ObjectMapper objectMapper() {
        return Json.mapper();
    }

    @RestController
    @RequestMapping(path = "/orders", consumes = APPLICATION_JSON_VALUE)
    public static class OrderController {

        private final StreamBridge streamBridge;

        public OrderController(final StreamBridge streamBridge) {
            this.streamBridge = streamBridge;
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

            this.streamBridge.send(OrderBinding.ORDER_CREATED.channel(), orderCreatedEvent);
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
