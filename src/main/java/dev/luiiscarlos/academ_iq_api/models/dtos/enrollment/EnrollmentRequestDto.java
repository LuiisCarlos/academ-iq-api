package dev.luiiscarlos.academ_iq_api.models.dtos.enrollment;

import lombok.Data;

@Data
public class EnrollmentRequestDto {

    private Integer rating;

    private Double progress;

    private String comment;

    private Boolean isFavorite;

    private Boolean isArchived;

    private Boolean isCompleted;

    public Boolean isFavorite() {
        return this.isFavorite;
    }

    public Boolean isArchived() {
        return this.isArchived;
    }

    public Boolean isCompleted() {
        return this.isCompleted;
    }

}
