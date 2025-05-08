package dev.luiiscarlos.academ_iq_api.models.dtos.enrollment;

import io.micrometer.common.lang.Nullable;

import lombok.Data;

@Data
public class EnrollmentRequestDto {

    @Nullable
    private Boolean isFavorite;

    public Boolean isFavorite() {
        return this.isFavorite;
    }

}
