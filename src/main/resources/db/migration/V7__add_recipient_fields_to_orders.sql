ALTER TABLE orders
  ADD COLUMN recipient_first_name VARCHAR(100);

ALTER TABLE orders
  ADD COLUMN recipient_last_name VARCHAR(100);

ALTER TABLE orders
  ADD COLUMN recipient_phone VARCHAR(20);

ALTER TABLE orders
  ADD COLUMN pickup_address VARCHAR(255);

UPDATE orders
  SET recipient_first_name = NULL,
      recipient_last_name  = NULL,
      recipient_phone      = NULL,
      pickup_address       = NULL;