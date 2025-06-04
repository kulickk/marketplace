CREATE TABLE orders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    total_amount NUMERIC(19,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    payment_id VARCHAR(100),
    payment_url VARCHAR(255)
);

ALTER TABLE orders
  ADD CONSTRAINT fk_orders_user
  FOREIGN KEY (user_id)
  REFERENCES users(id)
  ON DELETE CASCADE;


CREATE TABLE order_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id UUID NOT NULL,
    good_id UUID NOT NULL,
    quantity INTEGER NOT NULL,
    price NUMERIC(19,2) NOT NULL
);

ALTER TABLE order_items
  ADD CONSTRAINT fk_order_items_order
  FOREIGN KEY (order_id)
  REFERENCES orders(id)
  ON DELETE CASCADE;