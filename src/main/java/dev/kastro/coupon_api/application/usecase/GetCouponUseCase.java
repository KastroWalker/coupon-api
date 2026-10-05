package dev.kastro.coupon_api.application.usecase;

import dev.kastro.coupon_api.domain.exception.CouponNotFoundException;
import dev.kastro.coupon_api.domain.model.Coupon;
import dev.kastro.coupon_api.domain.port.CouponRepository;

import java.util.UUID;

public class GetCouponUseCase {

    private final CouponRepository repository;

    public GetCouponUseCase(CouponRepository repository) {
        this.repository = repository;
    }

    public Coupon execute(UUID id) {
        return repository.findById(id).orElseThrow(() -> new CouponNotFoundException(id));
    }
}
