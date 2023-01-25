package com.cloud.stream.spot.application.usecase;

public abstract class UseCase<I, O> {

    public abstract O execute(final I command);

}
