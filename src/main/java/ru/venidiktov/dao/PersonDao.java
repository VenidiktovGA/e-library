package ru.venidiktov.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import ru.venidiktov.model.Person;

import java.util.List;

@Component
public class PersonDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final BeanPropertyRowMapper<Person> personRowMapper = new BeanPropertyRowMapper<>(Person.class);

    public PersonDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<Person> getAll() {
        return namedParameterJdbcTemplate.query("select * from person", personRowMapper);
    }


    public List<Person> getAllWithoutBook() {
        return namedParameterJdbcTemplate.query(
                "select person.id, person.surname, person.name, person.middle_name, person.birthday " +
                        "from person\n" +
                        "left join book on person.id = book.person_id\n" +
                        "where book.person_id is null;",
                personRowMapper
        );
    }

    public void create(Person person) {
        SqlParameterSource sqlParameter = new BeanPropertySqlParameterSource(person);
        namedParameterJdbcTemplate.update(
                "insert into person (surname, name, middle_name, birthday) values(:surname, :name, :middleName, :birthday)",
                sqlParameter
        );
    }

    public Person getById(int id) {
        SqlParameterSource sqlParameter = new MapSqlParameterSource("id", id);
        return namedParameterJdbcTemplate.queryForObject(
                "select * from PERSON where id = :id",
                sqlParameter,
                personRowMapper
        );
    }

    public void update(Person person) {
        SqlParameterSource sqlParameter = new BeanPropertySqlParameterSource(person);
        namedParameterJdbcTemplate.update(
                "update person set surname = :surname, name = :name, middle_name = :middleName, birthday = :birthday where id = :id",
                sqlParameter
        );
    }

    public void deleteById(int id) {
        SqlParameterSource sqlParameter = new MapSqlParameterSource("id", id);
        namedParameterJdbcTemplate.update(
                "delete from person where id = :id",
                sqlParameter
        );
    }
}
