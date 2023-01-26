package com.cloud.stream.spot.application.usecase.order.process;

import com.cloud.stream.spot.application.usecase.UseCase;

public abstract sealed class ProcessOrderUseCase extends UseCase<ProcessOrderCommand, ProcessOrderOutput> permits DefaultProcessOrderUseCase {

}
