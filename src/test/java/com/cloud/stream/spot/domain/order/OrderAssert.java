package com.cloud.stream.spot.domain.order;

import org.assertj.core.api.AbstractAssert;

public final class OrderAssert extends AbstractAssert<OrderAssert, Order> {

    public OrderAssert(final Order actual) {
        super(actual, OrderAssert.class);
    }

    public static OrderAssert assertThat(final Order actual) {
        return new OrderAssert(actual);
    }

    public void containsAValidValue() {
        isNotNull();

        if (!actual.isValid()) {
            failWithMessage("Order value should be less than or equal to <500> but was <%s>", actual.getValue());
        }
    }

}
