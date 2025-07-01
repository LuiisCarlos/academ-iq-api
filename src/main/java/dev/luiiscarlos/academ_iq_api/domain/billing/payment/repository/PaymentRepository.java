package dev.luiiscarlos.academ_iq_api.domain.billing.payment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.luiiscarlos.academ_iq_api.domain.billing.payment.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByUserId(Long userId);

}
