package ru.venidiktov.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    @NotEmpty(message = "Название книги обязательно")
    @Size(min = 2, message = "Название книги не может быть меньше 2х символов")
    private String name;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person owner;

    @Column
    @NotEmpty(message = "ФИО автора обязательно")
    @Size(min = 3, message = "ФИО автора не может быть меньше 3х символов")
    private String author;

    @Column(name = "year_publishing")
    //@NotEmpty(message = "Дата публикациполи обязательна")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate yearPublishing;

    public Book() {
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getYearPublishing() {
        return yearPublishing;
    }

    public void setYearPublishing(LocalDate yearPublishing) {
        this.yearPublishing = yearPublishing;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }
}
