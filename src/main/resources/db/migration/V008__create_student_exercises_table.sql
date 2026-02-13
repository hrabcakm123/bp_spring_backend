CREATE TABLE student_exercises (
    id SERIAL PRIMARY KEY,
    student_id INTEGER NOT NULL,
    exercise_id INTEGER NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT uq_student
        UNIQUE (student_id),
    CONSTRAINT fk_student
        FOREIGN KEY (student_id) REFERENCES students (id),
    CONSTRAINT fk_exercise
        FOREIGN KEY (exercise_id) REFERENCES exercises (id)
);