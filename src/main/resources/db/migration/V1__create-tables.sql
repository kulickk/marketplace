-- 0. Удаляем старые таблицы, если они существуют
DROP TABLE IF EXISTS basket_item;
DROP TABLE IF EXISTS basket;
DROP TABLE IF EXISTS goods;
DROP TABLE IF EXISTS good_categories;
DROP TABLE IF EXISTS users;

-- 1. Создаем таблицу пользователей (без basket_id)
CREATE TABLE users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE,
    CONSTRAINT valid_email CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]+$')
);

-- 2. Создаем таблицу категорий товаров
CREATE TABLE good_categories (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    parent_id UUID
);

-- самоссылочная связь parent → good_categories(id)
ALTER TABLE good_categories
    ADD CONSTRAINT fk_good_categories_parent
    FOREIGN KEY (parent_id)
    REFERENCES good_categories (id)
    ON DELETE SET NULL;

-- 3. Создаем таблицу товаров
CREATE TABLE goods (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    price NUMERIC(19,2) NOT NULL,
    stock INTEGER NOT NULL,
    brand VARCHAR(255),
    category_id UUID
);

-- связь goods → good_categories
ALTER TABLE goods
    ADD CONSTRAINT fk_goods_category
    FOREIGN KEY (category_id)
    REFERENCES good_categories (id)
    ON DELETE SET NULL;

-- 4. Создаем таблицу корзин
CREATE TABLE basket (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE
);

-- связь basket → users
ALTER TABLE basket
    ADD CONSTRAINT fk_basket_user
    FOREIGN KEY (user_id)
    REFERENCES users (id)
    ON DELETE CASCADE;

-- 5. Добавляем столбец basket_id в users и связываем обратно
ALTER TABLE users
    ADD COLUMN basket_id UUID;

ALTER TABLE users
    ADD CONSTRAINT fk_users_basket
    FOREIGN KEY (basket_id)
    REFERENCES basket (id)
    ON DELETE SET NULL;

-- 6. Создаем таблицу элементов корзины
CREATE TABLE basket_item (
    id UUID PRIMARY KEY,
    basket_id UUID NOT NULL,
    good_id UUID NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0)
);

-- связь basket_item → basket
ALTER TABLE basket_item
    ADD CONSTRAINT fk_basket_item_basket
    FOREIGN KEY (basket_id)
    REFERENCES basket (id)
    ON DELETE CASCADE;


-- 7. Добавляем колонку role в users
ALTER TABLE users
  ADD COLUMN role VARCHAR(20) NOT NULL DEFAULT 'BUYER';

ALTER TABLE users
  ADD CONSTRAINT chk_users_role
    CHECK (role IN ('BUYER','SELLER','ADMIN'));

-- 8. Добавляем метку времени создания в basket и basket_item
ALTER TABLE basket
  ADD COLUMN created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW();

ALTER TABLE basket_item
  ADD COLUMN created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW();


-- Добавляем поле gender в users
ALTER TABLE users
  ADD COLUMN gender VARCHAR(20) NOT NULL DEFAULT 'NOT_STATED';

-- Ограничение по допустимым значениям
ALTER TABLE users
  ADD CONSTRAINT chk_users_gender
    CHECK (gender IN ('MALE','FEMALE','NOT_STATED'));
