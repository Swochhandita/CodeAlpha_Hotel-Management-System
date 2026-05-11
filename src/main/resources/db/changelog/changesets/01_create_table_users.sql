--liquibase formatted sql
--changeset swochhandita.ghimire:users-v1
--preconditions onFail: HALT onError: CONTINUE

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,

    name VARCHAR(100) NOT NULL,

    email VARCHAR(150) NOT NULL UNIQUE,

    password VARCHAR(255) NOT NULL,

    phone VARCHAR(20),

    role ENUM('GUEST', 'ADMIN') NOT NULL DEFAULT 'GUEST',

    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);