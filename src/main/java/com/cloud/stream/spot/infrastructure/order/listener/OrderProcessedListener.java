package com.cloud.stream.spot.infrastructure.order.listener;

import com.cloud.stream.spot.infrastructure.order.event.OrderProcessedEvent;
import com.cloud.stream.spot.infrastructure.configuration.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class OrderProcessedListener implements Consumer<OrderProcessedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(OrderProcessedListener.class);

    @Override
    public void accept(final OrderProcessedEvent event) {
        final var jsonEvent = Json.writeValueAsString(event);
        logger.info(jsonEvent);
    }

}
