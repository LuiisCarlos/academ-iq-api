package dev.luiiscarlos.academ_iq_api.shared.util;

public class EnumUtils {

    public static <E extends Enum<E> & JsonValuedEnum> E fromValue(Class<E> enumClass, String value) {
        String normalized = normalize(value);

        for (E constant : enumClass.getEnumConstants()) {
            if (normalize(constant.value()).equals(normalized))
                return constant;
        }

        // TODO Handle custom exception
        throw new IllegalArgumentException(
                String.format("Invalid value '%s' for enum %s", value, enumClass.getSimpleName()));
    }

    private static String normalize(String value) {
        return value.toLowerCase()
                .replaceAll("[_\\s]+", " ")
                .trim();
    }

}
