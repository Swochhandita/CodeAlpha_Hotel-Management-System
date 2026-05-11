package com.codealpha.hotel_management_system.dto.requests;

import com.codealpha.hotel_management_system.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class PaymentRequest {
    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;  // CASH / CARD / ESEWA / KHALTI

    /*
     * transactionRef: optional for CARD/ESEWA/KHALTI where a gateway
     * returns a reference ID. For CASH it's null.
     * In simulation, the service will generate a fake ref if null.
     */
    private String transactionRef;
}
