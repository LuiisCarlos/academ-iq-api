package dev.luiiscarlos.academ_iq_api.features.identity.user.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import dev.luiiscarlos.academ_iq_api.shared.util.EnumUtils;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RoleType {

    USER("USER"),

    ADMIN("ADMIN"),

    ACADEMIQ_ADMIN("ACADEMIQ_ADMIN");

    private String value;

    @JsonValue
    public String value() {
        return value;
    }

    @JsonCreator
    public static RoleType fromValue(String value) {
        return EnumUtils.fromValue(RoleType.class, value);
    }

}
