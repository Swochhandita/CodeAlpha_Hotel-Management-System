--liquibase formatted sql
--changeset swochhandita.ghimire: reservations-v1
--preconditions onFail: HALT onError: CONTINUE

CREATE TABLE reservations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    room_id INT NOT NULL,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    status ENUM('PENDING','CONFIRMED','CANCELLED','COMPLETED') NOT NULL DEFAULT 'PENDING',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_res_user
        FOREIGN KEY (user_id)
        REFERENCES users(id),
    CONSTRAINT fk_res_room
        FOREIGN KEY (room_id)
        REFERENCES rooms(id)
);
CREATE INDEX idx_res_dates
ON reservations(room_id, check_in_date, check_out_date);
CREATE INDEX idx_res_user
ON reservations(user_id);