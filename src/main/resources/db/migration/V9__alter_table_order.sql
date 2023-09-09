ALTER TABLE cart
    ADD COLUMN delivery_method varchar(20);

ALTER TABLE user_order
    ADD COLUMN comment         varchar,
    ADD COLUMN delivery_method varchar(20);
