package kr.hhplus.be.server.application.concert;

public record ConcertCriteria() {

    public record ReserveSeat(
            Long seatId,
            Long userId
    ) {

    }

}
