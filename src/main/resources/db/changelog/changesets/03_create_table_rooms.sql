--liquibase formatted sql
--changeset swochhandita.ghimire : rooms-v1
--preconditions onFail: HALT onError: CONTINUE

CREATE TABLE rooms (
    id INT AUTO_INCREMENT PRIMARY KEY,
    hotel_id BIGINT NOT NULL,
    room_number VARCHAR(20) NOT NULL,
    room_type ENUM('STANDARD','DELUXE','SUITE') NOT NULL,
    status ENUM('AVAILABLE','OCCUPIED','MAINTENANCE') NOT NULL DEFAULT 'AVAILABLE',
    price_per_night DECIMAL(10,2) NOT NULL,
    max_occupancy TINYINT NOT NULL DEFAULT 2,
    description TEXT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_room_hotel
        FOREIGN KEY (hotel_id)
        REFERENCES hotels(id)
        ON DELETE CASCADE,
    CONSTRAINT uq_hotel_room
        UNIQUE (hotel_id, room_number)
);

CREATE INDEX idx_room_type ON rooms(room_type);
CREATE INDEX idx_room_status ON rooms(status);