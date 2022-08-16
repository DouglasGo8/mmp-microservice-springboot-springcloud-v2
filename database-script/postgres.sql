
--drop table reviews;
select * from reviews;

/*
create table reviews
(
    id serial not null primary key,
    productId integer not null,
    reviewId integer not null,
    author text not null,
    subject text not null,
    content text null
);
*/