package dev.kastro.coupon_api.domain.model;

import dev.kastro.coupon_api.domain.exception.InvalidCouponException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CouponTest {

    private static final Instant NOW = Instant.parse("2025-11-04T12:00:00Z");
    private static final Instant FUTURE = NOW.plusSeconds(3600);
    private final Clock clock = Clock.fixed(NOW, ZoneOffset.UTC);

    private Coupon create(String code, String description, BigDecimal discount, Instant expiration, boolean published) {
        return Coupon.create(code, description, discount, expiration, published, clock);
    }

    private Coupon create(String code) {
        return create(code, "10% off", BigDecimal.ONE, FUTURE, false);
    }

    @Test
    void shouldRemoveSpecialCharactersFromCode() {
        Coupon coupon = create("AB#C-12!3");

        assertThat(coupon.getCode().value()).isEqualTo("ABC123");
        assertThat(coupon.getId()).isNotNull();
        assertThat(coupon.isRedeemed()).isFalse();
        assertThat(coupon.getStatus()).isEqualTo(CouponStatus.INACTIVE);
    }

    @Test
    void shouldPreserveCodeCase() {
        assertThat(create("aBc123").getCode().value()).isEqualTo("aBc123");
    }

    @Test
    void shouldRejectCodeWithLessThanSixCharactersAfterSanitization() {
        assertThatThrownBy(() -> create("A#B@C1")).isInstanceOf(InvalidCouponException.class);
    }

    @Test
    void shouldRejectCodeWithMoreThanSixCharactersAfterSanitization() {
        assertThatThrownBy(() -> create("ABC1234")).isInstanceOf(InvalidCouponException.class);
    }

    @Test
    void shouldRejectNullCode() {
        assertThatThrownBy(() -> create(null)).isInstanceOf(InvalidCouponException.class);
    }

    @Test
    void shouldCreatePublishedCouponAsActive() {
        Coupon coupon = create("ABC123", "desc", BigDecimal.ONE, FUTURE, true);

        assertThat(coupon.isPublished()).isTrue();
        assertThat(coupon.getStatus()).isEqualTo(CouponStatus.ACTIVE);
    }

    @Test
    void shouldAcceptMinimumDiscountValue() {
        assertThat(create("ABC123", "desc", new BigDecimal("0.5"), FUTURE, false).getDiscountValue().value())
                .isEqualByComparingTo("0.5");
    }

    @Test
    void shouldRejectDiscountValueBelowMinimum() {
        assertThatThrownBy(() -> create("ABC123", "desc", new BigDecimal("0.49"), FUTURE, false))
                .isInstanceOf(InvalidCouponException.class);
    }

    @Test
    void shouldRejectNullDiscountValue() {
        assertThatThrownBy(() -> create("ABC123", "desc", null, FUTURE, false))
                .isInstanceOf(InvalidCouponException.class);
    }

    @Test
    void shouldAcceptExpirationDateEqualToNow() {
        assertThat(create("ABC123", "desc", BigDecimal.ONE, NOW, false).getExpirationDate()).isEqualTo(NOW);
    }

    @Test
    void shouldRejectExpirationDateInThePast() {
        assertThatThrownBy(() -> create("ABC123", "desc", BigDecimal.ONE, NOW.minusSeconds(1), false))
                .isInstanceOf(InvalidCouponException.class);
    }

    @Test
    void shouldRejectNullExpirationDate() {
        assertThatThrownBy(() -> create("ABC123", "desc", BigDecimal.ONE, null, false))
                .isInstanceOf(InvalidCouponException.class);
    }

    @Test
    void shouldRejectBlankDescription() {
        assertThatThrownBy(() -> create("ABC123", " ", BigDecimal.ONE, FUTURE, false))
                .isInstanceOf(InvalidCouponException.class);
    }

    @Test
    void shouldAcceptDescriptionWith1000Characters() {
        assertThat(create("ABC123", "a".repeat(1000), BigDecimal.ONE, FUTURE, false).getDescription())
                .hasSize(1000);
    }

    @Test
    void shouldRejectDescriptionWithMoreThan1000Characters() {
        assertThatThrownBy(() -> create("ABC123", "a".repeat(1001), BigDecimal.ONE, FUTURE, false))
                .isInstanceOf(InvalidCouponException.class)
                .hasMessageContaining("1000");
    }

    @Test
    void shouldRestoreExpiredCouponWithoutRevalidating() {
        Instant past = NOW.minusSeconds(86400);

        Coupon coupon = Coupon.restore(UUID.randomUUID(), new CouponCode("ABC123"), "desc",
                new DiscountValue(BigDecimal.ONE), past, true, false, CouponStatus.ACTIVE);

        assertThat(coupon.getExpirationDate()).isEqualTo(past);
    }
}
