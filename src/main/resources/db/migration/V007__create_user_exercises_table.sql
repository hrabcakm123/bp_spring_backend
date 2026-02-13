CREATE TABLE user_exercises (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    exercise_id INTEGER NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT uq_user_exercise
        UNIQUE (user_id, exercise_id),
    CONSTRAINT fk_user
        FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_exercise
        FOREIGN KEY (exercise_id) REFERENCES exercises (id)
);