package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.intrastructure.dataportal.ReservationInfo;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentCompleteEvent(
        Long reservationId,
        LocalDate concertDate,
        Long seatId,
        BigDecimal price
) {
    public ReservationInfo getReservationInfo() {
        return new ReservationInfo(reservationId, concertDate, seatId , price);
    }
}
