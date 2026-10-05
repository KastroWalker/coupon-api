package dev.kastro.coupon_api.domain.exception;

import java.util.UUID;

public class CouponAlreadyDeletedException extends RuntimeException {

    public CouponAlreadyDeletedException(UUID id) {
        super("coupon " + id + " is already deleted");
    }
}
