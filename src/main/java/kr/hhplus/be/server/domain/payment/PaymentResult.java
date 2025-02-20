package kr.hhplus.be.server.domain.payment;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentResult(
        Long paymentId,
        BigDecimal price,
        LocalDate concertDate,
        Integer seatNumber
) {
    public static PaymentResult from(Payment payment){
        return new PaymentResult(payment.getId(), payment.getSeatPrice(), payment.getConcertDate(), payment.getSeatNumber());
    }
}
