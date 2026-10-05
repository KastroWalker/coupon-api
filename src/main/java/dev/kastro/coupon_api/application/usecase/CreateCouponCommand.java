package dev.kastro.coupon_api.application.usecase;

import java.math.BigDecimal;
import java.time.Instant;

public record CreateCouponCommand(
        String code,
        String description,
        BigDecimal discountValue,
        Instant expirationDate,
        boolean published) {
}
