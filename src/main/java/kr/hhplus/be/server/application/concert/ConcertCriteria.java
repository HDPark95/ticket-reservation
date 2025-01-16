package kr.hhplus.be.server.application.concert;

public record ConcertCriteria() {

    public record ReserveSeat(
            Long scheduleId,
            Long seatId,
            Long userId
    ) {

    }

}
