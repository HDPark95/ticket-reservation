package kr.hhplus.be.server.intrastructure.dataportal;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReservationInfo(
        Long reservationId,
        LocalDate concertDate,
        Long seatId,
        BigDecimal price
) {
}
