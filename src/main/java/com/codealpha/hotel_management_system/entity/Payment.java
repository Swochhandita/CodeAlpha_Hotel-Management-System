package com.codealpha.hotel_management_system.entity;
import com.codealpha.hotel_management_system.core.entity.BaseEntity;
import com.codealpha.hotel_management_system.enums.PaymentMethod;
import com.codealpha.hotel_management_system.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity
@Table(name = "payments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends BaseEntity {
        /*
         * RELATIONSHIP: Payment → Reservation  (@OneToOne, owning side)
         *
         * Payment is the OWNING side — it holds reservation_id as a FK column.
         * @JoinColumn specifies the FK column name in the payments table.
         */
        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "reservation_id", nullable = false, unique = true)
        private Reservation reservation;

        @Column(nullable = false, precision = 10, scale = 2)
        private BigDecimal amount;

        @Enumerated(EnumType.STRING)
        @Column(name = "payment_method", nullable = false)
        @Builder.Default
        private PaymentMethod paymentMethod = PaymentMethod.CARD;

        @Enumerated(EnumType.STRING)
        @Column(name = "payment_status", nullable = false)
        @Builder.Default
        private PaymentStatus paymentStatus = PaymentStatus.PENDING;

        @Column(name = "transaction_ref", length = 100)
        private String transactionRef;   // simulated reference ID

        @Column(name = "paid_at")
        private LocalDateTime paidAt;    // null until payment succeeds

        @CreationTimestamp  // No @UpdateTimestamp — payments are immutable once created
        @Column(name = "created_at", nullable = false, updatable = false)
        private LocalDateTime createdAt;
}
