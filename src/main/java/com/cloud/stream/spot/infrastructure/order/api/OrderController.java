package com.cloud.stream.spot.infrastructure.order.api;

import com.cloud.stream.spot.application.usecase.order.create.CreateNewOrderUseCase;
import com.cloud.stream.spot.application.usecase.order.create.CreateNewOrderCommand;
import com.cloud.stream.spot.infrastructure.order.api.in.OrderInput;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/orders", consumes = APPLICATION_JSON_VALUE)
public class OrderController {

    private final CreateNewOrderUseCase useCase;

    public OrderController(final CreateNewOrderUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    @ResponseStatus(ACCEPTED)
    public void create(@RequestBody OrderInput input) {
        final CreateNewOrderCommand command = new CreateNewOrderCommand(
            input.customerId(),
            input.value()
        );

        this.useCase.execute(command);
    }

}
