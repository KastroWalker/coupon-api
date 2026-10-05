package dev.kastro.coupon_api.domain.exception;

import java.util.UUID;

public class CouponNotFoundException extends RuntimeException {

    public CouponNotFoundException(UUID id) {
        super("coupon " + id + " not found");
    }
}
