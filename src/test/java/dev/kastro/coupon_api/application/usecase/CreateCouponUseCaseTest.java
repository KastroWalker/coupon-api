package dev.kastro.coupon_api.application.usecase;

import dev.kastro.coupon_api.domain.exception.InvalidCouponException;
import dev.kastro.coupon_api.domain.model.Coupon;
import dev.kastro.coupon_api.domain.port.CouponRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreateCouponUseCaseTest {

    private static final Instant NOW = Instant.parse("2025-11-04T12:00:00Z");

    private final CouponRepository repository = mock(CouponRepository.class);
    private final CreateCouponUseCase useCase =
            new CreateCouponUseCase(repository, Clock.fixed(NOW, ZoneOffset.UTC));

    @Test
    void shouldSaveAndReturnCouponWithSanitizedCode() {
        when(repository.save(any(Coupon.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Coupon coupon = useCase.execute(new CreateCouponCommand(
                "AB-C12-3", "desc", new BigDecimal("0.8"), NOW.plusSeconds(60), true));

        assertThat(coupon.getCode().value()).isEqualTo("ABC123");
        verify(repository).save(coupon);
    }

    @Test
    void shouldNotSaveWhenCouponIsInvalid() {
        CreateCouponCommand command = new CreateCouponCommand(
                "ABC123", "desc", new BigDecimal("0.8"), NOW.minusSeconds(60), false);

        assertThatThrownBy(() -> useCase.execute(command)).isInstanceOf(InvalidCouponException.class);
        verify(repository, never()).save(any());
    }
}
