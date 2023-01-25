package com.cloud.stream.spot.application.order;

public abstract class UseCase<T> {

    public abstract void execute(final T command);

}
