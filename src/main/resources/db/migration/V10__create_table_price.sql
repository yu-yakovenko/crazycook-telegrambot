CREATE TABLE price
(
    id    SERIAL PRIMARY KEY,
    name  varchar,
    value bigint,
    UNIQUE (name)
);

INSERT INTO price(name, value)
VALUES ('S', 280),
       ('M', 420),
       ('L', 630),
       ('delivery', 70)
