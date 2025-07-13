package dev.luiiscarlos.academ_iq_api.features.identity.user.dto;

import dev.luiiscarlos.academ_iq_api.shared.security.validation.password.Password;

public record AdminPasswordRequest(@Password String password) {

}
