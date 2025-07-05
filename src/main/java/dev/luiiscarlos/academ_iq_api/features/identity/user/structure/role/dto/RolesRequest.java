package dev.luiiscarlos.academ_iq_api.features.identity.user.structure.role.dto;

import java.util.List;

import dev.luiiscarlos.academ_iq_api.features.identity.user.structure.role.model.RoleType;

public record RolesRequest(List<RoleType> roles) {

}

