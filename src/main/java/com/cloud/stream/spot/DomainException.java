package com.cloud.stream.spot;

public class DomainException extends RuntimeException {

    private static final boolean ENABLE_SUPPRESSION = true;
    private static final boolean WRITEABLE_STACKTRACE = false;

    public DomainException(final String message) {
        super(message, null, ENABLE_SUPPRESSION, WRITEABLE_STACKTRACE);
    }

    public DomainException(final Exception ex) {
        super(ex.getMessage(), ex, ENABLE_SUPPRESSION, WRITEABLE_STACKTRACE);
    }

}
