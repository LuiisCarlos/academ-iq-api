package dev.luiiscarlos.academ_iq_api.features.learning.course.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import dev.luiiscarlos.academ_iq_api.shared.util.EnumUtils;
import dev.luiiscarlos.academ_iq_api.shared.util.JsonValuedEnum;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CourseLevel implements JsonValuedEnum {

    BEGINNER("Beginner"),

    INTERMEDIATE("Intermediate"),

    ADVANCED("Advanced");

    private String value;

    @JsonValue
    public String value() {
        return value;
    }

    @JsonCreator
    public static CourseLevel fromValue(String value) {
        return EnumUtils.fromValue(CourseLevel.class, value);
    }

}
