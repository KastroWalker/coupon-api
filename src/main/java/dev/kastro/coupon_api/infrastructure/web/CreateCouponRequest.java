package dev.kastro.coupon_api.infrastructure.web;

import dev.kastro.coupon_api.application.usecase.CreateCouponCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

public record CreateCouponRequest(
        @NotBlank String code,
        @NotBlank String description,
        @NotNull BigDecimal discountValue,
        @NotNull Instant expirationDate,
        Boolean published) {

    CreateCouponCommand toCommand() {
        return new CreateCouponCommand(code, description, discountValue, expirationDate, Boolean.TRUE.equals(published));
    }
}
