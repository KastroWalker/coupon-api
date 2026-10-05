package dev.kastro.coupon_api.infrastructure.config;

import dev.kastro.coupon_api.application.usecase.CreateCouponUseCase;
import dev.kastro.coupon_api.application.usecase.DeleteCouponUseCase;
import dev.kastro.coupon_api.application.usecase.GetCouponUseCase;
import dev.kastro.coupon_api.domain.port.CouponRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class UseCaseConfig {

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    CreateCouponUseCase createCouponUseCase(CouponRepository repository, Clock clock) {
        return new CreateCouponUseCase(repository, clock);
    }

    @Bean
    DeleteCouponUseCase deleteCouponUseCase(CouponRepository repository) {
        return new DeleteCouponUseCase(repository);
    }

    @Bean
    GetCouponUseCase getCouponUseCase(CouponRepository repository) {
        return new GetCouponUseCase(repository);
    }
}
