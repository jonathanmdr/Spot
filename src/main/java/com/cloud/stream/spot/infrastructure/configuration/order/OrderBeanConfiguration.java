package com.cloud.stream.spot.infrastructure.configuration.order;

import com.cloud.stream.spot.application.usecase.order.create.CreateNewOrderUseCase;
import com.cloud.stream.spot.application.usecase.order.create.DefaultCreateNewOrderUseCase;
import com.cloud.stream.spot.application.usecase.order.process.DefaultProcessOrderUseCase;
import com.cloud.stream.spot.application.usecase.order.process.ProcessOrderUseCase;
import com.cloud.stream.spot.domain.OrderGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderBeanConfiguration {

    @Bean
    public CreateNewOrderUseCase createNewOrderUseCase(final OrderGateway orderGateway) {
        return new DefaultCreateNewOrderUseCase(orderGateway);
    }

    @Bean
    public ProcessOrderUseCase processOrderUseCase() {
        return new DefaultProcessOrderUseCase();
    }

}
