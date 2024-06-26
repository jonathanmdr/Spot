package com.cloud.stream.spot.infrastructure.order.api.in;

import com.cloud.stream.spot.JacksonIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@JacksonIntegrationTest
class OrderInputIntegrationTest {

    @Autowired
    private JacksonTester<OrderInput> jacksonTester;

    @Test
    void testMarshall() throws IOException {
        final var customerId = UUID.randomUUID();
        final var value = BigDecimal.valueOf(100);
        final var orderInput = new OrderInput(customerId, value);

        final var actual = this.jacksonTester.write(orderInput);

        assertThat(actual)
            .hasJsonPathStringValue("$.customer_id", customerId)
            .hasJsonPathNumberValue("$.value", value);
    }

    @Test
    void testUnMarshall() throws IOException {
        final var customerId = UUID.randomUUID();
        final var value = BigDecimal.valueOf(100);
        final var orderInput = new OrderInput(customerId, value);
        final var json = """
            {
                "customer_id": "%s",
                "value": %s
            }
            """.formatted(customerId, value);

        final var actual = this.jacksonTester.parse(json);

        assertThat(actual.getObject()).isEqualTo(orderInput);
    }

}
