package app.carsharing.dto.request;

import app.carsharing.model.Payment;

public record PaymentRequestDto(Long rentalId, Payment.Type paymentType) {
}
