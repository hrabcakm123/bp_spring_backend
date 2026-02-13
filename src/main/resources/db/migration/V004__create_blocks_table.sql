CREATE TABLE blocks (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    max_points DOUBLE PRECISION NOT NULL CHECK (max_points > 0),
    required_points DOUBLE PRECISION NOT NULL CHECK (required_points >= 0),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    CHECK (required_points <= max_points)
);