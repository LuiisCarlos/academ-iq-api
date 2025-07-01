package dev.luiiscarlos.academ_iq_api.domain.billing.payment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dev.luiiscarlos.academ_iq_api.domain.billing.payment.dto.PaymentRequest;
import dev.luiiscarlos.academ_iq_api.domain.billing.payment.dto.PaymentResponse;

public interface PaymentService {

    PaymentResponse create(long userId, PaymentRequest request);

    Page<PaymentResponse> getAll(Pageable pageable);

    PaymentResponse get(long userId);

    PaymentResponse get(String intentId);

}
