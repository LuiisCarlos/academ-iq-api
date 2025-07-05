package dev.luiiscarlos.academ_iq_api.features.identity.user.structure.role.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import dev.luiiscarlos.academ_iq_api.shared.util.EnumUtils;
import dev.luiiscarlos.academ_iq_api.shared.util.JsonValuedEnum;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RoleType implements JsonValuedEnum {

    USER("USER"),

    ADMIN("ADMIN");

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
