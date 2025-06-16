package dev.luiiscarlos.academ_iq_api.shared.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RoleType {

    USER("USER"),

    ADMIN("ADMIN"),

    ACADEMIQ_ADMIN("ACADEMIQ_ADMIN");

    private final String value;

    public String value() {
        return value;
    }

}
