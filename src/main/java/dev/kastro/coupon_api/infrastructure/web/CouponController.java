package dev.kastro.coupon_api.infrastructure.web;

import dev.kastro.coupon_api.application.usecase.CreateCouponUseCase;
import dev.kastro.coupon_api.application.usecase.GetCouponUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class CouponController {

    private final CreateCouponUseCase createCouponUseCase;
    private final GetCouponUseCase getCouponUseCase;

    public CouponController(CreateCouponUseCase createCouponUseCase, GetCouponUseCase getCouponUseCase) {
        this.createCouponUseCase = createCouponUseCase;
        this.getCouponUseCase = getCouponUseCase;
    }

    @GetMapping("/coupon/{id}")
    public CouponResponse get(@PathVariable UUID id) {
        return CouponResponse.from(getCouponUseCase.execute(id));
    }

    @PostMapping("/coupon")
    @ResponseStatus(HttpStatus.CREATED)
    public CouponResponse create(@Valid @RequestBody CreateCouponRequest request) {
        return CouponResponse.from(createCouponUseCase.execute(request.toCommand()));
    }
}
