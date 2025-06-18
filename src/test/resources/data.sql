INSERT INTO item(id, title, description, price)
VALUES
  (1, 'item_1 title', 'item_1 description', 1),
  (2, 'item_2 title', 'item_2 description', 1);

INSERT INTO orders(id)
VALUES (1);

SELECT setval(pg_get_serial_sequence('orders', 'id'), 1, true);

INSERT INTO cart(id, item_id, count, order_id)
VALUES
  (1, 1, 1, 1),
  (2, 2, 2, null);
