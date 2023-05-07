package ru.venidiktov.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Objects;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
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
    //@NotEmpty(message = "Дата публикации обязательна")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate yearPublishing;

    @Column(name = "assign_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate assignDate;

    @Transient
    private boolean expired;

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

    public LocalDate getAssignDate() {
        return assignDate;
    }

    public void setAssignDate(LocalDate assignDate) {
        this.assignDate = assignDate;
    }

    public boolean isExpired() {
        if (assignDate == null) return false;
        return assignDate.plusDays(10L).isBefore(LocalDate.now());
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book book)) return false;

        if (expired != book.expired) return false;
        if (!Objects.equals(name, book.name)) return false;
        if (!Objects.equals(owner, book.owner)) return false;
        if (!Objects.equals(author, book.author)) return false;
        if (!Objects.equals(yearPublishing, book.yearPublishing))
            return false;
        return Objects.equals(assignDate, book.assignDate);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (yearPublishing != null ? yearPublishing.hashCode() : 0);
        result = 31 * result + (assignDate != null ? assignDate.hashCode() : 0);
        result = 31 * result + (expired ? 1 : 0);
        return result;
    }
}
