TRUNCATE TABLE Book, Person, Users RESTART IDENTITY;

insert into Users (username, year_of_birth, role, password) values('user', 1980, 'ROLE_USER', '$2a$10$4JamR8nEkpeXlY0wkyIQo.5E25DspL/sOyp6i6YU6Fgoy5dm5cSjO');
insert into Users (username, year_of_birth, role, password) values('admin', 1960, 'ROLE_ADMIN', '$2a$10$99mpk9YObo3KzxB8WK5IceM1.N0PHfBMgtXYG/Nnl4Ghhuba5UVzm');

insert into Person (surname, name, middle_name, birthday, gender)
values ('Ivanov', 'Ivan', 'Ivanovitch', '1960-01-01', 'MALE');
insert into Person (surname, name, middle_name, birthday, gender)
values ('Petrov', 'Petr', 'Petrovitch', '1960-01-01', 'MALE');

insert into Book (name, author, year_publishing, person_id)
values ('Властелин колец', 'Джон Рональд Руэл Толкин', '1954-07-27', 1);
insert into Book (name, author, year_publishing, person_id)
values ('451 градус по Фаренгейту', 'Рэй Дуглас Брэдбери', '1959-07-27', 1);
insert into Book (name, author, year_publishing, person_id)
values ('Метро 2033', 'Дмитрий Алексеевич Глуховский', '1960-07-27', NULL);