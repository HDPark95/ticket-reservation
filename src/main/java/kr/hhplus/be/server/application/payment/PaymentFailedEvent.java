package kr.hhplus.be.server.application.payment;

public record PaymentFailedEvent(
   Long reservationId
) {
}
