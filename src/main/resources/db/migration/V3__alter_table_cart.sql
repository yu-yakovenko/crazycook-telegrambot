ALTER TABLE cart
    ADD COLUMN box_in_progress_id bigint,
    ADD COLUMN current_flavor_id  bigint,
    ADD CONSTRAINT "cart_box_in_progress_key" FOREIGN KEY (box_in_progress_id) REFERENCES box (id),
    ADD CONSTRAINT "cart_current_flavor_id_key" FOREIGN KEY (current_flavor_id) REFERENCES flavor (id);
