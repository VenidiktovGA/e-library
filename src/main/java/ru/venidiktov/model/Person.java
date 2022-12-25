package ru.venidiktov.model;

import org.springframework.format.annotation.DateTimeFormat;
import ru.venidiktov.enums.Gender;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    @NotEmpty(message = "Имя обязательно")
    @Size(min = 2, message = "Имя не может быть меньше 2х символов")
    private String name;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.PERSIST)
    private List<Book> bookList;

    @Column
    @NotEmpty(message = "Фамилия обязательна")
    @Size(min = 2, message = "Фамилия не может быть меньше 2х символов")
    private String surname;

    @Column(name = "middle_name")
    private String middleName;

    @Column
    //@NotEmpty(message = "Дата рождения обязательна")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    private Gender gender;

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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }
}
