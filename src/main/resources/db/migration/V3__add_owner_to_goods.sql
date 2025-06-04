ALTER TABLE goods
ADD COLUMN user_owner_id UUID;

ALTER TABLE goods
ADD CONSTRAINT fk_goods_owner
  FOREIGN KEY (user_owner_id)
  REFERENCES users (id)
  ON DELETE SET NULL;