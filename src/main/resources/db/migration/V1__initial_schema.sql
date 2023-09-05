CREATE TABLE customer
(
    chat_id  SERIAL PRIMARY KEY,
    username varchar
);

CREATE TABLE user_order
(
    id               SERIAL PRIMARY KEY,
    customer_chat_id INTEGER,
    status           varchar,
    CONSTRAINT "order_customer_key" FOREIGN KEY (customer_chat_id) REFERENCES "customer" (chat_id)
);

CREATE TABLE cart
(
    id               SERIAL PRIMARY KEY,
    customer_chat_id INTEGER,
    status           varchar,
    s_number         INTEGER,
    m_number         INTEGER,
    l_number         INTEGER,
    CONSTRAINT "cart_customer_key" FOREIGN KEY (customer_chat_id) REFERENCES "customer" (chat_id)
);

CREATE TABLE box
(
    id       SERIAL PRIMARY KEY,
    box_size varchar,
    order_id bigint,
    cart_id  bigint,
    CONSTRAINT "box_order_key" FOREIGN KEY (order_id) REFERENCES user_order (id),
    CONSTRAINT "box_cart_key" FOREIGN KEY (cart_id) REFERENCES cart (id)
);

CREATE TABLE flavor
(
    id   SERIAL PRIMARY KEY,
    name varchar
);
