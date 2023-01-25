package com.cloud.stream.spot.application.usecase;

public abstract class UnitUseCase<T> {

    public abstract void execute(final T command);

}
