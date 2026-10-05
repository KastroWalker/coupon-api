package dev.kastro.coupon_api.infrastructure.web;

import dev.kastro.coupon_api.domain.model.Coupon;
import dev.kastro.coupon_api.domain.model.CouponStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record CouponResponse(
        UUID id,
        String code,
        String description,
        BigDecimal discountValue,
        Instant expirationDate,
        boolean published,
        boolean redeemed,
        CouponStatus status) {

    static CouponResponse from(Coupon coupon) {
        return new CouponResponse(
                coupon.getId(),
                coupon.getCode().value(),
                coupon.getDescription(),
                coupon.getDiscountValue().value(),
                coupon.getExpirationDate(),
                coupon.isPublished(),
                coupon.isRedeemed(),
                coupon.getStatus());
    }
}
