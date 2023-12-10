package com.cloud.stream.spot.infrastructure.order.listener;

import com.cloud.stream.spot.KafkaIntegrationTest;
import com.cloud.stream.spot.domain.order.OrderStatus;
import com.cloud.stream.spot.infrastructure.configuration.json.Json;
import com.cloud.stream.spot.infrastructure.order.event.OrderProcessedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.cloud.stream.function.StreamBridge;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.springframework.boot.logging.LogLevel.INFO;

@KafkaIntegrationTest
@ExtendWith(OutputCaptureExtension.class)
class OrderProcessedListenerIntegrationTest {

    @Autowired
    private StreamBridge streamBridge;

    @SpyBean
    private OrderProcessedListener orderProcessedListener;

    @Test
    void givenAProcessedOrder_whenCallsAccept_thenConsumeOrderProcessedEvent(final CapturedOutput output) {
        final var orderProcessedEvent = new OrderProcessedEvent(
            UUID.randomUUID(),
            UUID.randomUUID(),
            BigDecimal.TEN,
            OrderStatus.APPROVED
        );

        this.streamBridge.send("order-processed", orderProcessedEvent);

        verify(this.orderProcessedListener).accept(argThat(
            event -> Objects.equals(event.orderId(), orderProcessedEvent.orderId())
                && Objects.equals(event.customerId(), orderProcessedEvent.customerId())
                && Objects.equals(event.value(), orderProcessedEvent.value())
                && Objects.equals(event.status(), OrderStatus.APPROVED)
        ));

        final var jsonEvent = Json.writeValueAsString(orderProcessedEvent);

        assertThat(output.getOut())
            .contains(INFO.name(), jsonEvent);
    }

}
