package kr.hhplus.be.server.domain.reservation;

public class ReservationCompleteFailException extends RuntimeException {
    public ReservationCompleteFailException(String message) {
        super(message);
    }
}
