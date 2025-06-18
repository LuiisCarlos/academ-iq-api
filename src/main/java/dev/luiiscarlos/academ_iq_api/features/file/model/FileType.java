package dev.luiiscarlos.academ_iq_api.features.file.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import dev.luiiscarlos.academ_iq_api.shared.util.EnumUtils;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum FileType {

    AVATAR("Avatar"),

    THUMBNAIL("Thumbnail"),

    COURSE_VIDEO("Course Video");

    private String value;

    @JsonValue
    public String value() {
        return value;
    }

    @JsonCreator
    public static FileType fromValue(String value) {
        return EnumUtils.fromValue(FileType.class, value);
    }

}