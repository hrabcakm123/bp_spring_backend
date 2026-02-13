CREATE TABLE student_assignments (
    id SERIAL PRIMARY KEY,
    assignment_id INTEGER NOT NULL,
    student_id INTEGER NOT NULL,
    earned_points DOUBLE PRECISION NOT NULL,
    note TEXT,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_by INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_by INTEGER,
    updated_at TIMESTAMP,
    CONSTRAINT uq_assignment_student
        UNIQUE (assignment_id, student_id),
    CONSTRAINT fk_assignment
        FOREIGN KEY (assignment_id) REFERENCES assignments (id),
    CONSTRAINT fk_student
        FOREIGN KEY (student_id) REFERENCES students (id),
    CONSTRAINT fk_created_by
        FOREIGN KEY (created_by) REFERENCES users (id),
    CONSTRAINT fk_updated_by
        FOREIGN KEY (updated_by) REFERENCES users (id)
);