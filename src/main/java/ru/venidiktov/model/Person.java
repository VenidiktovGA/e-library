package ru.venidiktov.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class Person {

    private int id;

    @NotEmpty(message = "Имя обязательно")
    @Size(min = 2, message = "Имя не может быть меньше 2х символов")
    private String name;

    @NotEmpty(message = "Фамилия обязательна")
    @Size(min = 2, message = "Фамилия не может быть меньше 2х символов")
    private String surname;

    private String middleName;
    //@NotEmpty(message = "Дата рождения обязательна")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    public Person() {
    }

    public Person(String name, String surname, String middleName, LocalDate birthday) {
        this.name = name;
        this.surname = surname;
        this.middleName = middleName;
        this.birthday = birthday;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
}