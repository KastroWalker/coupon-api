package dev.kastro.coupon_api.infrastructure.persistence;

import dev.kastro.coupon_api.domain.model.Coupon;
import dev.kastro.coupon_api.domain.port.CouponRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
class CouponRepositoryAdapter implements CouponRepository {

    private final SpringDataCouponRepository jpaRepository;
    private final CouponMapper mapper;

    CouponRepositoryAdapter(SpringDataCouponRepository jpaRepository, CouponMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Coupon save(Coupon coupon) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(coupon)));
    }

    @Override
    public Optional<Coupon> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }
}
