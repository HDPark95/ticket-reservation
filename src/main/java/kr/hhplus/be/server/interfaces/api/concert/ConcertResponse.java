package kr.hhplus.be.server.interfaces.api.concert;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ConcertResponse() {
    public static record AvailableDate(
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
            LocalDate date
    ) {

    }

    public record AvailableSeat(
            Long seatId,
            Integer seatNumber,
            BigDecimal price
    ) {
    }
}
