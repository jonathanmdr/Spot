package com.cloud.stream.spot.domain;

import com.cloud.stream.spot.domain.order.Order;
import io.github.springwolf.bindings.kafka.annotations.KafkaAsyncOperationBinding;
import io.github.springwolf.core.asyncapi.annotations.AsyncMessage;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import io.github.springwolf.core.asyncapi.annotations.AsyncPublisher;

@FunctionalInterface
public interface OrderGateway {

    @AsyncPublisher(
        operation = @AsyncOperation(
            channelName = "order-created",
            servers = {
                "kafka-server"
            },
            message = @AsyncMessage(
                contentType = "application/json"
            ),
            headers = @AsyncOperation.Headers(
                values = {
                    @AsyncOperation.Headers.Header(
                        name = "Content-Type",
                        value = "application/json"
                    )
                }
            )
        )
    )
    @KafkaAsyncOperationBinding
    void create(final Order order);

}
