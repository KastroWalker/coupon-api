package dev.kastro.coupon_api.domain.model;

import dev.kastro.coupon_api.domain.exception.InvalidCouponException;

import java.math.BigDecimal;

public record DiscountValue(BigDecimal value) {

    private static final BigDecimal MINIMUM = new BigDecimal("0.5");

    public DiscountValue {
        if (value == null || value.compareTo(MINIMUM) < 0) {
            throw new InvalidCouponException("discountValue must be at least " + MINIMUM);
        }
    }
}
