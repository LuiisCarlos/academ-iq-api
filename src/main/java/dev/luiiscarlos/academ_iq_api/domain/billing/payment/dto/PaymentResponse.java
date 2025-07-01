package dev.luiiscarlos.academ_iq_api.domain.billing.payment.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponse {

    private long amount;

    private String currency;

    private String status;

    private String clientSecret;

}
