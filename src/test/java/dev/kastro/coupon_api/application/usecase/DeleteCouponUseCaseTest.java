package dev.kastro.coupon_api.application.usecase;

import dev.kastro.coupon_api.domain.exception.CouponAlreadyDeletedException;
import dev.kastro.coupon_api.domain.exception.CouponNotFoundException;
import dev.kastro.coupon_api.domain.model.Coupon;
import dev.kastro.coupon_api.domain.model.CouponStatus;
import dev.kastro.coupon_api.domain.port.CouponRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DeleteCouponUseCaseTest {

    private static final Instant NOW = Instant.parse("2025-11-04T12:00:00Z");

    private final CouponRepository repository = mock(CouponRepository.class);
    private final DeleteCouponUseCase useCase = new DeleteCouponUseCase(repository);

    private Coupon coupon() {
        return Coupon.create("ABC123", "desc", new BigDecimal("0.8"), NOW.plusSeconds(60), true,
                Clock.fixed(NOW, ZoneOffset.UTC));
    }

    @Test
    void shouldSaveCouponWithDeletedStatus() {
        Coupon coupon = coupon();
        when(repository.findById(coupon.getId())).thenReturn(Optional.of(coupon));

        useCase.execute(coupon.getId());

        ArgumentCaptor<Coupon> saved = ArgumentCaptor.forClass(Coupon.class);
        verify(repository).save(saved.capture());
        assertThat(saved.getValue().getStatus()).isEqualTo(CouponStatus.DELETED);
        assertThat(saved.getValue().getId()).isEqualTo(coupon.getId());
    }

    @Test
    void shouldThrowNotFoundAndNotSaveWhenCouponDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(id)).isInstanceOf(CouponNotFoundException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowAndNotSaveWhenCouponIsAlreadyDeleted() {
        Coupon deleted = coupon().delete();
        when(repository.findById(deleted.getId())).thenReturn(Optional.of(deleted));

        assertThatThrownBy(() -> useCase.execute(deleted.getId())).isInstanceOf(CouponAlreadyDeletedException.class);
        verify(repository, never()).save(any());
    }
}
