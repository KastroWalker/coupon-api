package dev.kastro.coupon_api.domain.port;

import dev.kastro.coupon_api.domain.model.Coupon;

public interface CouponRepository {

    Coupon save(Coupon coupon);
}
