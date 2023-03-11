TRUNCATE TABLE Book, Person, Users RESTART IDENTITY;

insert into Users (username, year_of_birth, password) values('user', 1980, 'pass');

insert into Person (surname, name, middle_name, birthday, gender)
values ('Ivanov', 'Ivan', 'Ivanovitch', '1960-01-01', 'MALE');

insert into Book (name, author, year_publishing, person_id)
values ('Властелин колец', 'Джон Рональд Руэл Толкин', '1954-07-27', 1);
insert into Book (name, author, year_publishing, person_id)
values ('451 градус по Фаренгейту', 'Рэй Дуглас Брэдбери', '1959-07-27', 1);
insert into Book (name, author, year_publishing, person_id)
values ('Метро 2033', 'Дмитрий Алексеевич Глуховский', '1960-07-27', NULL);