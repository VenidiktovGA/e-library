package ru.venidiktov.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import ru.venidiktov.model.Book;
import ru.venidiktov.model.Person;

import java.util.List;

@Component
public class BookDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final BeanPropertyRowMapper<Book> bookRowMapper = new BeanPropertyRowMapper<>(Book.class);
    private final BeanPropertyRowMapper<Person> personRowMapper = new BeanPropertyRowMapper<>(Person.class);

    public BookDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<Book> getAll() {
        return namedParameterJdbcTemplate.query(
                "select * from book", bookRowMapper
        );
    }

    public Book getById(int id) {
        SqlParameterSource sqlParameter = new MapSqlParameterSource("id", id);
        return namedParameterJdbcTemplate.queryForObject(
                "select * from book where id = :id",
                sqlParameter,
                bookRowMapper
        );
    }

    public void create(Book book) {
        SqlParameterSource sqlParameter = new BeanPropertySqlParameterSource(book);
        namedParameterJdbcTemplate.update(
                "insert into book (name, author, year_publishing) values(:name, :author, :yearPublishing)",
                sqlParameter
        );
    }

    public void deleteById(int id) {
        SqlParameterSource sqlParameter = new MapSqlParameterSource("id", id);
        namedParameterJdbcTemplate.update(
                "delete from book where id = :id",
                sqlParameter
        );
    }

    public void update(Book book) {
        SqlParameterSource sqlParameter = new BeanPropertySqlParameterSource(book);
        namedParameterJdbcTemplate.update(
                "update book set name = :name, author = :author, year_publishing = :yearPublishing, person_id = :personId where id = :id",
                sqlParameter
        );
    }

    public List<Book> getBooksByPersonId(int id) {
        SqlParameterSource sqlParameter = new MapSqlParameterSource("id", id);
        return namedParameterJdbcTemplate.query(
                "select * from book where person_id = :id",
                sqlParameter,
                bookRowMapper
        );
    }

    public void assign(Book book) {
        SqlParameterSource sqlParameter = new BeanPropertySqlParameterSource(book);
        namedParameterJdbcTemplate.update(
                "update book set person_id = :personId where id = :id",
                sqlParameter
        );
    }

    public void release(int id) {
        SqlParameterSource sqlParameter = new MapSqlParameterSource("id", id);
        namedParameterJdbcTemplate.update(
                "update book set person_id = NULL where id = :id",
                sqlParameter
        );
    }
}
