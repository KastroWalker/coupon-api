package dev.kastro.coupon_api.application.usecase;

import dev.kastro.coupon_api.domain.model.Coupon;
import dev.kastro.coupon_api.domain.port.CouponRepository;

import java.time.Clock;

public class CreateCouponUseCase {

    private final CouponRepository repository;
    private final Clock clock;

    public CreateCouponUseCase(CouponRepository repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    public Coupon execute(CreateCouponCommand command) {
        Coupon coupon = Coupon.create(
                command.code(),
                command.description(),
                command.discountValue(),
                command.expirationDate(),
                command.published(),
                clock);
        return repository.save(coupon);
    }
}
