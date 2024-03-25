CREATE TABLE IF NOT EXISTS ticket (
    id SERIAL PRIMARY KEY,
    session_id INT NOT NULL REFERENCES sessions(id),
    seat_row INT NOT NULL,
    seat_cell INT NOT NULL,
    user_id INT NOT NULL REFERENCES users(id)
);

ALTER TABLE ticket ADD CONSTRAINT unique_session UNIQUE (session_id, seat_row, seat_cell);

