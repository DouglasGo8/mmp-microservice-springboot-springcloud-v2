CREATE TABLE reviews
(
    id        integer not null primary key,
    version   integer not null,
    productId integer not null,
    reviewId  integer not null,
    author    text    not null,
    subject   text    not null,
    content   text    not null
);

SELECT *
FROM reviews;

INSERT INTO reviews
VALUES (1, 1, 123, 1, 'dougdb',
        'Camel Reactive Streams', 'No Subscriptions Enabled');