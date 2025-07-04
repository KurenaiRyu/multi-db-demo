create table if not exists "user" (
    id serial primary key ,
    name varchar(50)
);

create table if not exists book(
    id serial primary key ,
    title varchar(200),
    author varchar(200),
    update timestamp without time zone not null
);

create table if not exists loan(
    id serial primary key ,
    user_id int,
    book_id int,
    loan_date_time timestamp without time zone ,
    return_date_time timestamp without time zone
)