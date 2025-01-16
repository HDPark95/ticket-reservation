package kr.hhplus.be.server.interfaces.api.payment;

import kr.hhplus.be.server.application.payment.PaymentResult;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentResponse(
        Long paymentId,
        BigDecimal price,
        LocalDate concertDate,
        Integer seatNumber
) {
    public static PaymentResponse from(PaymentResult result){
        return new PaymentResponse(result.paymentId(), result.price(), result.concertDate(), result.seatNumber());
    }
}
