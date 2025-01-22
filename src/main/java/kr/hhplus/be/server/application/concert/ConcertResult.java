package kr.hhplus.be.server.application.concert;

import kr.hhplus.be.server.domain.concert.ConcertSchedule;
import kr.hhplus.be.server.domain.concert.Seat;
import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ConcertResult() {

    public record ScheduleInfo(
            Long scheduleId,
            LocalDate concertDate
    ){
        public static ScheduleInfo from(ConcertSchedule schedule){
            return new ScheduleInfo(schedule.getId(), schedule.getDate());
        }
    }

    public record SeatInfo(
            Long seatId,
            Integer seatNumber,
            BigDecimal price
    ){
        public static SeatInfo from(Seat seat){
            return new SeatInfo(seat.getId(), seat.getSeatNumber(), seat.getPrice());
        }
    }

    public record ReservationResult(
            Long reservationId,
            ReservationStatus status,
            BigDecimal price
    ){
        public static ReservationResult from(Reservation reservation, BigDecimal price){
            return new ReservationResult(reservation.getId(), reservation.getStatus(), price);
        }
    }
}
