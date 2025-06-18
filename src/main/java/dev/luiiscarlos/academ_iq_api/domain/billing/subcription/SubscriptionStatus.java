package dev.luiiscarlos.academ_iq_api.domain.billing.subcription;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import dev.luiiscarlos.academ_iq_api.shared.util.EnumUtils;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SubscriptionStatus {

    ACTIVE("Active"),

    INCOMPLETE("Incomplete"),

    CANCELED("Canceled"),

    PAUSED("Paused");

    private String value;

    @JsonValue
    public String value() {
        return value;
    }

    @JsonCreator
    public static SubscriptionStatus fromValue(String value) {
        return EnumUtils.fromValue(SubscriptionStatus.class, value);
    }

}
