package dev.kastro.coupon_api.domain.model;

import dev.kastro.coupon_api.domain.exception.InvalidCouponException;

public record CouponCode(String value) {

    private static final int LENGTH = 6;
    private static final String NON_ALPHANUMERIC = "[^A-Za-z0-9]";

    public CouponCode {
        if (value == null || !value.matches("[A-Za-z0-9]{" + LENGTH + "}")) {
            throw new InvalidCouponException("code must have exactly " + LENGTH + " alphanumeric characters");
        }
    }

    public static CouponCode sanitize(String raw) {
        if (raw == null) {
            throw new InvalidCouponException("code is required");
        }
        return new CouponCode(raw.replaceAll(NON_ALPHANUMERIC, ""));
    }
}
