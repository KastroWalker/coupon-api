package dev.kastro.coupon_api.infrastructure.web;

import dev.kastro.coupon_api.application.usecase.CreateCouponCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;

public record CreateCouponRequest(
        @Schema(description = "Código alfanumérico. Caracteres especiais são removidos antes da validação; "
                + "o resultado deve ter exatamente 6 caracteres.", example = "ABC-123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank String code,
        @Schema(description = "Descrição do cupom", example = "Cupom de 10% de desconto", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank String description,
        @Schema(description = "Valor absoluto do desconto, sem moeda. Mínimo 0.5, sem máximo.", example = "10.5",
                minimum = "0.5", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull BigDecimal discountValue,
        @Schema(description = "Data de expiração (ISO-8601). Não pode estar no passado.", example = "2030-12-31T23:59:59Z",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull Instant expirationDate,
        @Schema(description = "Se o cupom já é criado publicado", example = "false", defaultValue = "false",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        Boolean published) {

    CreateCouponCommand toCommand() {
        return new CreateCouponCommand(code, description, discountValue, expirationDate, Boolean.TRUE.equals(published));
    }
}
