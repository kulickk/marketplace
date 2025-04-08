DROP TABLE IF EXISTS good_categories;

CREATE TABLE good_categories (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    parent_id UUID,
    CONSTRAINT fk_good_category_parent FOREIGN KEY (parent_id)
        REFERENCES good_categories (id)
        ON DELETE SET NULL
);
