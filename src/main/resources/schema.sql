DROP TABLE IF EXISTS cart;
DROP TABLE IF EXISTS item;
DROP TABLE IF EXISTS orders;

CREATE TABLE item (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(100),
    description TEXT,
    img_path VARCHAR(255),
    price DOUBLE PRECISION
);

CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY
);

CREATE TABLE cart (
    id BIGSERIAL PRIMARY KEY,
    item_id BIGINT REFERENCES item(id),
    count INTEGER NOT NULL,
    order_id BIGINT REFERENCES orders(id)
);