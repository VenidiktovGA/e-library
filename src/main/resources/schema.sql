--Читатели
create table Person(
    id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    surname VARCHAR NOT NULL,
    name VARCHAR NOT NULL,
    middle_name VARCHAR,
    birthday DATE NOT NULL,
    gender VARCHAR
);

--Книги
create table Book(
    id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR NOT NULL,
    author VARCHAR NOT NULL,
    year_publishing DATE NOT NULL,
    person_id INT REFERENCES Person(id) ON DELETE SET NULL,
    assign_date DATE
);

--Пользователи
create table Users(
	id int primary key generated by default as identity,
	username varchar(100) not null,
	year_of_birth int not null,
	password varchar not null
);