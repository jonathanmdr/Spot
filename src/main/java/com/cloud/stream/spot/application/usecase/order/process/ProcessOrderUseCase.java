package com.cloud.stream.spot.application.usecase.order.process;

import com.cloud.stream.spot.application.usecase.UseCase;
import com.cloud.stream.spot.application.usecase.order.process.command.ProcessOrderCommand;
import com.cloud.stream.spot.domain.order.event.OrderProcessedEvent;

public abstract class ProcessOrderUseCase extends UseCase<ProcessOrderCommand, OrderProcessedEvent> {

}
