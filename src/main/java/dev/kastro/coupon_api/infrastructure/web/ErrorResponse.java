package dev.kastro.coupon_api.infrastructure.web;

import java.util.List;

public record ErrorResponse(int status, String error, List<String> messages) {
}
