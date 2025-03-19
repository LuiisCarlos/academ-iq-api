package dev.luiiscarlos.academ_iq_api.models.dtos;

import lombok.Data;

@Data
public class EnrollmentRequestDto {

    private Long courseId;

    private Integer rating;

    private Double progress;

    private String comment;

    private Boolean isFavorite;

    private Boolean isArchived;

    private Boolean isCompleted;

    public Boolean isFavorite() {
        return this.isFavorite;
    }

    public void setFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public Boolean isArchived() {
        return this.isArchived;
    }

    public void setArchived(Boolean isArchived) {
        this.isArchived = isArchived;
    }

    public Boolean isCompleted() {
        return this.isCompleted;
    }

    public void setCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

}
