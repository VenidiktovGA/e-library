package ru.venidiktov.enums;

public enum Gender {
    MALE("мужской"), FEMALE("женский");

    private final String description;

    Gender(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
