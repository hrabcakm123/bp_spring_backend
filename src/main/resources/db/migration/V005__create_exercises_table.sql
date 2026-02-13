CREATE TABLE exercises (
    id SERIAL PRIMARY KEY,
    first_session_date DATE NOT NULL,
    start_time TIME NOT NULL,
    room_enum VARCHAR(255) NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);