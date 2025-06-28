package dev.luiiscarlos.academ_iq_api.features.identity.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInstructorResponse {

    private String fullname;

    private String avatar;

}
