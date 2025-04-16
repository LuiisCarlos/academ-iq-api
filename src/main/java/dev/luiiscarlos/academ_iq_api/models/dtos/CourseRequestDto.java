package dev.luiiscarlos.academ_iq_api.models.dtos;

import java.util.List;

import dev.luiiscarlos.academ_iq_api.models.Category;
import lombok.Data;

@Data
public class CourseRequestDto {

    private String name;

    private String description;

    private String author;

    private List<String> requirements;

    private Category category;

    private String level;

}
