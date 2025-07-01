package dev.luiiscarlos.academ_iq_api.domain.billing.payment.mapper;

import org.springframework.stereotype.Component;

import com.stripe.model.PaymentIntent;

import dev.luiiscarlos.academ_iq_api.domain.billing.payment.dto.PaymentRequest;
import dev.luiiscarlos.academ_iq_api.domain.billing.payment.dto.PaymentResponse;
import dev.luiiscarlos.academ_iq_api.domain.billing.payment.model.Payment;

@Component
public class PaymentMapper { // TODO Implement this mapper

    public Payment toEntity(PaymentRequest dto) {
        return null;
    }

    public PaymentResponse toDto(Payment entity) {
        return PaymentResponse.builder()
                .amount(entity.getAmount())
                .currency(entity.getCurrency())
                .status(entity.getStatus().value())
                .build();
    }

    public PaymentResponse toDto(Payment entity, PaymentIntent paymentIntent) {
        return PaymentResponse.builder()
                .amount(entity.getAmount())
                .currency(entity.getCurrency())
                .status(paymentIntent.getStatus())
                .clientSecret(paymentIntent.getClientSecret())
                .build();
    }

}
