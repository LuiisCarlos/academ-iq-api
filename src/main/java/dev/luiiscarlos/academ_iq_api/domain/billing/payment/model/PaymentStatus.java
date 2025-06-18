package dev.luiiscarlos.academ_iq_api.domain.billing.payment.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import dev.luiiscarlos.academ_iq_api.shared.util.EnumUtils;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PaymentStatus {

    PENDING("Pending"),

    SUCCEEDED("Succeeded"),

    FAILED("Failed"),

    CANCELED("Canceled");

    private String value;

    @JsonValue
    public String value() {
        return value;
    }

    @JsonCreator
    public static PaymentStatus fromValue(String value) {
        return EnumUtils.fromValue(PaymentStatus.class, value);
    }

}