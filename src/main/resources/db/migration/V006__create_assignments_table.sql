CREATE TABLE assignments (
    id SERIAL PRIMARY KEY,
    block_id INTEGER NOT NULL,
    name VARCHAR(255) NOT NULL,
    max_points DOUBLE PRECISION NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT uq_name_block
        UNIQUE (block_id, name),
    CONSTRAINT fk_block
        FOREIGN KEY (block_id) REFERENCES blocks (id)
);