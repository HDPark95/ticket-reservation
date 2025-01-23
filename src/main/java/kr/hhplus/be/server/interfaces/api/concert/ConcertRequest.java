package kr.hhplus.be.server.interfaces.api.concert;

public record ConcertRequest() {
    public static record Reserve(
            Long seatId
    ) {

    }
}
