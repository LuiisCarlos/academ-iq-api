package dev.luiiscarlos.academ_iq_api.shared.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum FileType {

    AVATAR("avatar"),

    THUMBNAIL("thumbnail"),

    COURSE_VIDEO("course_video");

    private final String value;

    public String value() {
        return value;
    }

}