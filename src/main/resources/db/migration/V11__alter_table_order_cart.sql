ALTER TABLE cart
    ADD COLUMN address varchar(1000);

ALTER TABLE user_order
    ADD COLUMN address varchar(1000);
