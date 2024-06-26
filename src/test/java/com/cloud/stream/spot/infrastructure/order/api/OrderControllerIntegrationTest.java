package com.cloud.stream.spot.infrastructure.order.api;

import com.cloud.stream.spot.ApiIntegrationTest;
import com.cloud.stream.spot.MockSupportTest;
import com.cloud.stream.spot.application.usecase.order.create.DefaultCreateNewOrderUseCase;
import com.cloud.stream.spot.infrastructure.configuration.json.Json;
import com.cloud.stream.spot.infrastructure.order.api.in.OrderInput;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ApiIntegrationTest(controllers = OrderController.class)
class OrderControllerIntegrationTest extends MockSupportTest {

    @MockBean
    private DefaultCreateNewOrderUseCase createNewOrderUseCase;

    @Autowired
    private MockMvc mockMvc;

    @Override
    protected List<Object> mocks() {
        return List.of(this.createNewOrderUseCase);
    }

    @Test
    void givenAnNewOrder_whenSendRequest_thenReturnAccepted() throws Exception {
        final var orderInput = new OrderInput(UUID.randomUUID(), BigDecimal.valueOf(100.00));

        final var request = post("/v1/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(Json.writeValueAsString(orderInput))
            .accept(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(request)
            .andDo(print())
            .andExpect(status().isNoContent());

        verify(this.createNewOrderUseCase).execute(argThat(command -> {
            assertThat(command.customerId()).isEqualTo(orderInput.customerId());
            assertThat(command.value()).isEqualTo(orderInput.value());
            return true;
        }));
    }

}
