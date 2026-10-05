package dev.kastro.coupon_api.application.usecase;

import dev.kastro.coupon_api.domain.exception.CouponNotFoundException;
import dev.kastro.coupon_api.domain.model.Coupon;
import dev.kastro.coupon_api.domain.port.CouponRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetCouponUseCaseTest {

    private static final Instant NOW = Instant.parse("2025-11-04T12:00:00Z");

    private final CouponRepository repository = mock(CouponRepository.class);
    private final GetCouponUseCase useCase = new GetCouponUseCase(repository);

    @Test
    void shouldReturnCouponWhenIdExists() {
        Coupon coupon = Coupon.create("ABC123", "desc", new BigDecimal("0.8"), NOW.plusSeconds(60), true,
                Clock.fixed(NOW, ZoneOffset.UTC));
        when(repository.findById(coupon.getId())).thenReturn(Optional.of(coupon));

        assertThat(useCase.execute(coupon.getId())).isSameAs(coupon);
    }

    @Test
    void shouldThrowNotFoundWhenCouponDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(id)).isInstanceOf(CouponNotFoundException.class);
    }
}
