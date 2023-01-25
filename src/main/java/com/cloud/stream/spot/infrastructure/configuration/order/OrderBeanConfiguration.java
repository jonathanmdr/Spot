package com.cloud.stream.spot.infrastructure.configuration.order;

import com.cloud.stream.spot.application.order.CreateNewOrderUseCase;
import com.cloud.stream.spot.application.order.DefaultCreateNewOrderUseCase;
import com.cloud.stream.spot.domain.OrderGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderBeanConfiguration {

    @Bean
    public CreateNewOrderUseCase createNewOrderUseCase(final OrderGateway orderGateway) {
        return new DefaultCreateNewOrderUseCase(orderGateway);
    }

}
