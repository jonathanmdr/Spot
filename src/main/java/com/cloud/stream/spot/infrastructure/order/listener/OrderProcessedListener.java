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
        try {
            // Force delay to simulate processing
            Thread.sleep(1500);

            final var jsonEvent = Json.writeValueAsString(event);

            logger.info(jsonEvent);
        } catch (final InterruptedException ex) {
            logger.error("Error while processing order", ex);
            Thread.currentThread().interrupt();
        }
    }

}
