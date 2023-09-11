CREATE TABLE promo
(
    id            SERIAL PRIMARY KEY,
    name          varchar,
    percent       integer,
    expiring_date date,
    UNIQUE (name)
);

INSERT INTO promo (name, percent, expiring_date)
VALUES ('test_promo', 10, '2023-12-31')
