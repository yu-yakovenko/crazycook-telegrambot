CREATE TABLE flavor_quantity
(
    id        SERIAL PRIMARY KEY,
    quantity  varchar,
    box_id    bigint,
    flavor_id bigint,
    CONSTRAINT "flavor_quantity_box_key" FOREIGN KEY (box_id) REFERENCES box (id),
    CONSTRAINT "flavor_quantity_flavor_key" FOREIGN KEY (flavor_id) REFERENCES flavor (id)
);

