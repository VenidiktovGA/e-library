package ru.venidiktov.repo;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@SpringBootConfiguration
public class Config {

    @Bean
    public EmbeddedDatabase dataSource() {
       return (new EmbeddedDatabaseBuilder())
               .setType(EmbeddedDatabaseType.H2)
               .setName("testDB;MODE=MySQL")
               .addScript("classpath:schema.sql")
               .addScript("classpath:data.sql")
               .build();
    }
}
