ALTER TABLE flavor
    ADD COLUMN is_in_stock boolean;

INSERT INTO flavor(name, is_in_stock)
VALUES ('Манго-маракуйя', TRUE),
       ('Шоколад-банан', TRUE),
       ('Космополітан', TRUE),
       ('Полуничне мохіто', TRUE);

