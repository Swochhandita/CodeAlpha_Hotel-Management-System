--liquibase formatted sql
--changeset swochhandita.ghimire: 05-create-payments
--preconditions onFail: HALT onError: CONTINUE
CREATE TABLE payments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    reservation_id INT NOT NULL UNIQUE,
    amount DECIMAL(10,2) NOT NULL,
    payment_method ENUM('CASH','CARD','ESEWA','KHALTI') NOT NULL DEFAULT 'CARD',
    payment_status ENUM('PENDING','SUCCESS','FAILED','REFUNDED') NOT NULL DEFAULT 'PENDING',
    transaction_ref VARCHAR(100),
    paid_at DATETIME,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_pay_reservation
        FOREIGN KEY (reservation_id)
        REFERENCES reservations(id)
);