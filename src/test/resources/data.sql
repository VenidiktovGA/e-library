--delete from users;
--delete from person;

insert into Users (username, year_of_birth, password) values('user', 1980, 'pass');

insert into Person (surname, name, middle_name, birthday, gender)
values ('Ivanov', 'Ivan', 'Ivanovitch', '1960-01-01', 'male');

insert into Book (name, author, year_publishing)
values ('Властелин колец', 'Джон Рональд Руэл Толкин', '1954-07-27');