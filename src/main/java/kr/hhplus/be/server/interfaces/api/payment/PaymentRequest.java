package kr.hhplus.be.server.interfaces.api.payment;

public record PaymentRequest() {
    public static record pay(
            Long reservationId
    ){

    }
}
