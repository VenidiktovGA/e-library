package ru.venidiktov.enums;

public enum Role {
    ROLE_USER("пользователь"),
    ROLE_ADMIN("администратор");

    private String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}
