package dev.kastro.coupon_api.infrastructure.web;

import dev.kastro.coupon_api.domain.model.Coupon;
import dev.kastro.coupon_api.domain.model.CouponStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record CouponResponse(
        @Schema(description = "Identificador do cupom", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6") UUID id,
        @Schema(description = "Código sanitizado (6 caracteres alfanuméricos)", example = "ABC123") String code,
        @Schema(description = "Descrição do cupom", example = "Cupom de 10% de desconto") String description,
        @Schema(description = "Valor absoluto do desconto, sem moeda", example = "10.5") BigDecimal discountValue,
        @Schema(description = "Data de expiração (ISO-8601)", example = "2030-12-31T23:59:59Z") Instant expirationDate,
        @Schema(description = "Se o cupom está publicado", example = "false") boolean published,
        @Schema(description = "Se o cupom já foi resgatado", example = "false") boolean redeemed,
        @Schema(description = "Status do cupom", example = "INACTIVE") CouponStatus status) {

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
