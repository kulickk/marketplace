DROP TABLE IF EXISTS goods;

CREATE TABLE goods (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(10, 2) NOT NULL,
    stock INTEGER NOT NULL,
    brand VARCHAR(255),
    category_id UUID,
    CONSTRAINT fk_goods_category FOREIGN KEY (category_id)
        REFERENCES good_categories (id)
        ON DELETE SET NULL
);
