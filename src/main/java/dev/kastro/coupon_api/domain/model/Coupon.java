package dev.kastro.coupon_api.domain.model;

import dev.kastro.coupon_api.domain.exception.InvalidCouponException;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

public class Coupon {

    public static final int DESCRIPTION_MAX_LENGTH = 1000;

    private final UUID id;
    private final CouponCode code;
    private final String description;
    private final DiscountValue discountValue;
    private final Instant expirationDate;
    private final boolean published;
    private final boolean redeemed;
    private final CouponStatus status;

    private Coupon(UUID id, CouponCode code, String description, DiscountValue discountValue,
                   Instant expirationDate, boolean published, boolean redeemed, CouponStatus status) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.discountValue = discountValue;
        this.expirationDate = expirationDate;
        this.published = published;
        this.redeemed = redeemed;
        this.status = status;
    }

    public static Coupon create(String code, String description, BigDecimal discountValue,
                                Instant expirationDate, boolean published, Clock clock) {
        if (description == null || description.isBlank()) {
            throw new InvalidCouponException("description is required");
        }
        if (description.length() > DESCRIPTION_MAX_LENGTH) {
            throw new InvalidCouponException("description must have at most " + DESCRIPTION_MAX_LENGTH + " characters");
        }
        if (expirationDate == null) {
            throw new InvalidCouponException("expirationDate is required");
        }
        if (expirationDate.isBefore(clock.instant())) {
            throw new InvalidCouponException("expirationDate cannot be in the past");
        }
        return new Coupon(
                UUID.randomUUID(),
                CouponCode.sanitize(code),
                description,
                new DiscountValue(discountValue),
                expirationDate,
                published,
                false,
                published ? CouponStatus.ACTIVE : CouponStatus.INACTIVE);
    }

    public static Coupon restore(UUID id, CouponCode code, String description, DiscountValue discountValue,
                                 Instant expirationDate, boolean published, boolean redeemed, CouponStatus status) {
        return new Coupon(id, code, description, discountValue, expirationDate, published, redeemed, status);
    }

    public UUID getId() { return id; }
    public CouponCode getCode() { return code; }
    public String getDescription() { return description; }
    public DiscountValue getDiscountValue() { return discountValue; }
    public Instant getExpirationDate() { return expirationDate; }
    public boolean isPublished() { return published; }
    public boolean isRedeemed() { return redeemed; }
    public CouponStatus getStatus() { return status; }
}
