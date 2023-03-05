package ru.venidiktov.repo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
/*@EnableJpaRepositories(basePackages = {
        "ru.venidiktov.repo.BookRepoJpa"
})*/
public class BookRepoTest {

    /*@Autowired
    private BookRepoJpa bookRepo;*/

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:h2:mem:testDB", "sa", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    @Test
    void test() throws SQLException {
        ResultSet resultSet = null;
        try(Statement statement = getConnection().createStatement()) {
//            String createUsers = "drop table users";
//            statement.execute(createUsers);
            String sql = "select * from Person";
            resultSet = statement.executeQuery(sql);
            System.out.println("Fine, users = " + resultSet.getInt("id"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if(resultSet != null) resultSet.close();
            System.out.println("Fine!");
        }
    }


}
