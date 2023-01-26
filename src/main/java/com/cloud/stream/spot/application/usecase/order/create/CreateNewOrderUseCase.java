package com.cloud.stream.spot.application.usecase.order.create;

import com.cloud.stream.spot.application.usecase.UnitUseCase;

public abstract sealed class CreateNewOrderUseCase extends UnitUseCase<CreateNewOrderCommand> permits DefaultCreateNewOrderUseCase {

}
