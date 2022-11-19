create table Person(
    id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    surname VARCHAR NOT NULL,
    name VARCHAR NOT NULL,
    middle_name VARCHAR,
    birthday DATE NOT NULL
);

create table Book(
    id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR NOT NULL,
    author VARCHAR NOT NULL,
    year_publishing DATE NOT NULL,
    person_id INT REFERENCES Person(id) NULL
);