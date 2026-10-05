package dev.kastro.coupon_api.infrastructure.web;

import dev.kastro.coupon_api.application.usecase.CreateCouponUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CouponController {

    private final CreateCouponUseCase createCouponUseCase;

    public CouponController(CreateCouponUseCase createCouponUseCase) {
        this.createCouponUseCase = createCouponUseCase;
    }

    @PostMapping("/coupon")
    @ResponseStatus(HttpStatus.CREATED)
    public CouponResponse create(@Valid @RequestBody CreateCouponRequest request) {
        return CouponResponse.from(createCouponUseCase.execute(request.toCommand()));
    }
}
