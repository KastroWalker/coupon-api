package dev.kastro.coupon_api.infrastructure.web;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ErrorResponse(
        @Schema(description = "Status HTTP", example = "400") int status,
        @Schema(description = "Descrição do status HTTP", example = "Bad Request") String error,
        @Schema(description = "Mensagens de erro", example = "[\"code must have exactly 6 alphanumeric characters\"]")
        List<String> messages) {
}
