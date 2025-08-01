INSERT INTO users (first_name, last_name, email, password_hash, role_enum)
VALUES
    ('Admin', 'Admin', 'admin@admin.admin', '$2a$10$4Uj7RJd86oAcW.SKA4k1DO7zLerSxeC8T5TBitBavGZ5DEupMWHEi', 'ADMIN'),
    ('Teacher', 'Teacher', 'teacher@teacher.teacher', '$2a$10$earHcOFRD9IQBIQBPWhOWuZwtFsMemm5ahuUIRlwADebkpHE9Q/Z6', 'TEACHER'),
    ('Helper', 'Helper', 'helper@helper.helper', '$2a$10$f4DqBZEOp8r3XX5kzuDjMOG04OObQFB1vUzzKYn7..pZ/m3ZugtRS', 'HELPER');
