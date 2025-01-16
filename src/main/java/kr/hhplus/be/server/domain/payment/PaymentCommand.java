package kr.hhplus.be.server.domain.payment;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentCommand(
        Long userId,
        Long reservationId,
        LocalDate concertDate,
        Integer seatNumber,
        BigDecimal price
) {
}
