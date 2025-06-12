package dev.luiiscarlos.academ_iq_api.features.enrollment.dto;

import lombok.Data;

@Data
public class EnrollmentUpdateDto {

    private String progressState;

    private Boolean favorite;

    private Boolean archived;

    private Boolean completed;

    public Boolean isFavorite() {
        return this.favorite;
    }

    public Boolean isArchived() {
        return this.archived;
    }

    public Boolean isCompleted() {
        return this.completed;
    }

}