package dev.luiiscarlos.academ_iq_api.features.course.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InstructorResponse {

    private String fullname;

    private String avatar;

}
