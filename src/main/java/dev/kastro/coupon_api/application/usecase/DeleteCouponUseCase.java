package dev.kastro.coupon_api.application.usecase;

import dev.kastro.coupon_api.domain.exception.CouponNotFoundException;
import dev.kastro.coupon_api.domain.model.Coupon;
import dev.kastro.coupon_api.domain.port.CouponRepository;

import java.util.UUID;

public class DeleteCouponUseCase {

    private final CouponRepository repository;

    public DeleteCouponUseCase(CouponRepository repository) {
        this.repository = repository;
    }

    public void execute(UUID id) {
        Coupon coupon = repository.findById(id).orElseThrow(() -> new CouponNotFoundException(id));
        repository.save(coupon.delete());
    }
}
