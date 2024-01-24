package it.alten.doublechargg.pawtropolis.game.enums;

import java.util.Arrays;

public enum SpecialFeature {
    YELLOW("Yellow"),
    MAGENTA("Magenta"),
    CYAN("Cyan"),
    NONE("None");

    private final String name;

    SpecialFeature(String name) {
        this.name = name;
    }

    public static SpecialFeature findByName(String name) {
        return Arrays.stream(values())
                .filter(specialFeature -> specialFeature.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
