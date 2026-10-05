package dev.kastro.coupon_api.infrastructure.persistence;

import dev.kastro.coupon_api.domain.model.Coupon;
import dev.kastro.coupon_api.domain.model.CouponCode;
import dev.kastro.coupon_api.domain.model.DiscountValue;
import org.springframework.stereotype.Component;

@Component
class CouponMapper {

    CouponJpaEntity toEntity(Coupon coupon) {
        return new CouponJpaEntity(
                coupon.getId(),
                coupon.getCode().value(),
                coupon.getDescription(),
                coupon.getDiscountValue().value(),
                coupon.getExpirationDate(),
                coupon.isPublished(),
                coupon.isRedeemed(),
                coupon.getStatus());
    }

    Coupon toDomain(CouponJpaEntity entity) {
        return Coupon.restore(
                entity.getId(),
                new CouponCode(entity.getCode()),
                entity.getDescription(),
                new DiscountValue(entity.getDiscountValue()),
                entity.getExpirationDate(),
                entity.isPublished(),
                entity.isRedeemed(),
                entity.getStatus());
    }
}
