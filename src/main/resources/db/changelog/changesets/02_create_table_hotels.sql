--liquibase formatted sql
--changeset swochhandita.ghimire:hotels-v1
--preconditions onFail: HALT onError: CONTINUE

CREATE TABLE hotels (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    address VARCHAR(400) NOT NULL,
    city VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL DEFAULT 'Nepal',
    description TEXT,
    phone VARCHAR(20),
    email VARCHAR(150),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_hotel_city ON hotels(city);