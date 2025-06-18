package dev.luiiscarlos.academ_iq_api.domain.billing.payment.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import dev.luiiscarlos.academ_iq_api.shared.util.EnumUtils;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PaymentType {

    INDIVIDUAL("Individual"),

    SUBSCRIPTION("Subscription");

    private String value;

    @JsonValue
    public String value() {
        return value;
    }

    @JsonCreator
    public static PaymentType fromValue(String value) {
        return EnumUtils.fromValue(PaymentType.class, value);
    }

}
